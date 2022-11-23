const idBcs = document.getElementById('id_bcs').value;
const tbodyCheckedBook = document.getElementById('tbodyCheckedBookList');
const inputBarcode = document.getElementById('input_barcode');

//작업자 선택 관련 변수
const selectCheckIp = document.getElementById('select-check-ip');

//요약정보에 고정 변수들
const boardWholeCount = document.getElementById('boardWholeCount');
const boardCheckedCount = document.getElementById('boardCheckedCount');
const boardConfirmCount = document.getElementById('boardConfirmCount');
const boardRedupCount = document.getElementById('boardRedupCount');
const boardBrwedCount = document.getElementById('boardBrwedCount');
const boardRentedCount = document.getElementById('boardRentedCount');
const boardUnregCount = document.getElementById('boardUnregCount');
const boardUncheckedCount = document.getElementById('boardUncheckedCount');

//진행율 막대
const batchProgress = document.getElementById('batchProgress');

//점검 안된 도서 목록
const divUncheckedBookList = document.getElementById('div-uncheckedBookList');

window.addEventListener('load', () => {
	console.log('idbcs 값은: '+idBcs);
	
	new Promise((resolve) => {
		//첫번째 함수 실행
		//이미 점검된 도서 목록 호출하기.
		selectCheckedBookList();
		//리졸브? 리턴 다음 then 실행
		console.log(resolve());
		resolve();
	}).then(() => {
		//console.log('first then')
		//각종 도서 점검목록수 구하기.
		currentBookCheckStatus();
	})
//	.then(() => {
//		//진행율 채우기.
//		batchProgressInit();
//	})
	
});

//진행막대를 위한 함수
function batchProgressInit(whole, brwed, confirm) {
	
	batchProgress.max = parseInt(whole)-parseInt(brwed); 
	batchProgress.value = parseInt(confirm);
}

function setMaxBatchProgress(maxValue) {
	batchProgress.max = maxValue;
}
//진행 막대를 위한 함수 모음




//현재 점검현황 요약  book check status
function currentBookCheckStatus() {
	$.ajax({
		url: "/book_check/current_checked_book_status.do",
		type:'GET',
		data: {
			idBcs
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				let checkBookStatus = data.bookCheck;
				
				boardWholeCount.innerText = checkBookStatus.wholeCount;
				boardCheckedCount.innerText = checkBookStatus.checkedCount;
				getBookCountByType('tbodyWholeCheckList', checkBookStatus.checkedCount);
				
				boardConfirmCount.innerText = checkBookStatus.confirmCount;
				getBookCountByType('tbodyNormalBook', checkBookStatus.confirmCount);
				
				boardRedupCount.innerText = checkBookStatus.redupCount;
				getBookCountByType('tbodyDoubleCheckedBook', checkBookStatus.redupCount);
				
				boardRentedCount.innerText = checkBookStatus.rentedCheckedCount;
				getBookCountByType('tbodyRentedBook', checkBookStatus.rentedCheckedCount);
				
				boardUnregCount.innerText = checkBookStatus.unregCount;
				getBookCountByType('tbodyUnregisteredBook', checkBookStatus.unregCount);
				
				boardUncheckedCount.innerText = checkBookStatus.uncheckedCount;
				getBookCountByType('tbodyUncheckedBook', checkBookStatus.uncheckedCount);
				
				boardBrwedCount.innerText = checkBookStatus.brwedCount;
				getBookCountByType('tbodyWholeBrwedBook', checkBookStatus.brwedCount);
				
				batchProgressInit(checkBookStatus.wholeCount, checkBookStatus.brwedCount, checkBookStatus.confirmCount)
			}
		}
	});
}


//각종 상태의 도서 목록 type에따라서 컨트롤러에서 다른 값을 준다.
function getOverallBookList(type) {
	//console.log(type);
	//type에 따라서 컨트롤러도 하나로 쓰자!
	
	let targetTbody = document.getElementById(type);
	
	$.ajax({
		url: "/book_check/by_type_book_list.do",
		type:'GET',
		data: {
			idBcs,
			type
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				//리스트에 쌓이는 것을 방지하기 위해
				targetTbody.innerText = '';
				let dataList = data.dataList;
				//결과값 없으면 그냥 null 리턴
				if(dataList == null){
					return;
				}
				
				for(var i=0; i<dataList.length; i++){
					let newRowData = makeRowReturnRow(dataList[i],i+1)
					
					targetTbody.append(newRowData);
				}
				
				//점검목록 개수 구하기 함수.
				getBookCountByType(type, dataList.length);
			}
		}
	});
}

//숫자 카운트 함수 데이터 통신말고 그냥 화면에 뜬 번호 전달 용도.
function getBookCountByType(type, count) {
	//아래처럼 쓸수있음.
	let curTbody = document.querySelector('#'+type);
	//카운트
	let countSpan;
	
	if(type == 'tbodyNormalBook') {
		document.querySelector('.num__circle__normal').innerText = count;
	} else if(type == 'tbodyDoubleCheckedBook'){
		document.querySelector('.num__circle__doubleChecked').innerText = count;
	} else if(type == 'tbodyRentedBook'){
		document.querySelector('.num__circle__rented').innerText = count;
	} else if(type == 'tbodyUnregisteredBook'){
		document.querySelector('.num__circle__unregistered').innerText = count;
	} else if(type == 'tbodyUncheckedBook'){
		document.querySelector('.num__circle__unchecked').innerText = count;
	} else if(type == 'tbodyWholeCheckList') {
		document.querySelector('.num__circle__whole').innerText = count;
	} else if(type == 'tbodyWholeBrwedBook') {
		//전체 대출중 도서 목록 발견된거 아님.
		document.querySelector('.num__circle__wholeBrwed').innerText = count;
	}
}

//점검목록 도서 막 등록할때, 화면에 표시되는 각종 목록수 +1 해주기
//두번째 매개변수는 어쩔수 없이 넣음.
function updateTypedCountPlusOne(checkVal, rentedCount) {
	let curCount;
	
	if(checkVal=='확인') {
		//정상도서 카운트 변경
		curCount = document.querySelector('.num__circle__normal').innerText;
		document.querySelector('.num__circle__normal').innerText = parseInt(curCount)+1;
		//진행율 +1
		document.querySelector('#batchProgress').value = parseInt(curCount)+1;
		//정상도서면, 미점검도서 숫자 -1
		curCount = document.querySelector('.num__circle__unchecked').innerText;
		document.querySelector('.num__circle__unchecked').innerText = parseInt(curCount)-1;
		
	} else if(checkVal=='중복점검도서') {
		curCount = document.querySelector('.num__circle__doubleChecked').innerText;
		document.querySelector('.num__circle__doubleChecked').innerText = parseInt(curCount)+1;
	} else if(checkVal=='대출중도서') {
		curCount = document.querySelector('.num__circle__rented').innerText;
		document.querySelector('.num__circle__rented').innerText = parseInt(rentedCount);
	} else if(checkVal=='미등록도서') {
		curCount = document.querySelector('.num__circle__unregistered').innerText;
		document.querySelector('.num__circle__unregistered').innerText = parseInt(curCount)+1;
	}
}


//색상 결정 함수
function resultBackgroundColor(checkResult) {
	var colorResult = null; 
	if(checkResult == '확인'){
		colorResult = '#1BE37C';
	} else if(checkResult == '중복도서'){
		colorResult = '#ba9b00';
	} else if(checkResult == '대출중도서'){
		colorResult = '#ad9e00';
	} else if(checkResult == '미등록도서') {
		colorResult = '#E35D2B';
	} else if(checkResult == null) {
		//미점검도서 그냥 배경색.
	} else {
		colorResult = '#E3B209';
	}
	return colorResult;
}

//이미 점검된 도서 목록 호출 함수.
function selectCheckedBookList() {
	$.ajax({
		url: "/book_check/checked_book_list.do",
		type:'GET',
		data: {
			idBcs
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				let checkedBookList = data.checkedBookList;
				tbodyCheckedBook.innerHTML = '';
				
				if(checkedBookList.length>0){
					for(var i=0; i<checkedBookList.length; i++) {
						//데이터 row 만들기만 하기.
						let newRow = makeRowReturnRow(checkedBookList[i],i+1);
						//테이블에 값 붙여넣기.
						tbodyCheckedBook.prepend(newRow);
					}
					
					//각 tab에 전체 개수 표시 함수
					getBookCountByType('tbodyWholeCheckList', checkedBookList.length);
					
				} else {
					row = tbodyCheckedBook.insertRow();
					cell = row.insertCell(0);
					cell.classList.add('text-center');
					cell.setAttribute('colspan','7');
					cell.innerText = '점검된 도서 정보가 없습니다.'
				}
			}
		}
	});
}

//낱권 점검 엔터키
function enterKeyDownCheckBook() {
	if(event.keyCode == 13) {
		clickedCheckBook();
	}
}

//낱권 점검 함수
function clickedCheckBook(line) {
	//점검자를 선택해야한다.
	if(selectCheckIp.value == null || selectCheckIp.value == '') {
		alert('점검자를 선택해주세요');
		return;
	}
	var inputBarcodeVal = inputBarcode.value;
	if(inputBarcodeVal == null||inputBarcodeVal==''){
		//line에는 값이 일괄등록시 값이 들어간다.
		inputBarcodeVal = line;
		console.log('line값은: '+line+'입니다');
	}
	if(inputBarcodeVal == null || inputBarcodeVal == '') {
		alert('등록번호를 입력해주세요.');
	} else {
		$.ajax({
			
			url: "/book_check/add_new_book_check.do",
			type:'POST',
			async: false, //ajax결과값이 나오면 다음 함수가 진행된다.
			data: {
				idBcs,
				inputBarcodeVal,
				checkIp: selectCheckIp.value
			},
			success: function(data) {
				if(data.rt != 'OK') {
					alert(data.rt);
				} else {
					//맨 왼쪽 번호를 체크하기 위한 변수들
					var lastTrCheckedBook = tbodyCheckedBook.childNodes;
					var childrenTdInLastTr = lastTrCheckedBook[0].childNodes;
					var lastCheckedNum = childrenTdInLastTr[0].innerText;
					if(lastCheckedNum == null || lastCheckedNum == '' || lastCheckedNum =='점검된 도서 정보가 없습니다.'){
						//초기 문구(도서정보없음) 없애기.
						tbodyCheckedBook.innerHTML = '';
						lastCheckedNum = 0;
					} else {
						lastCheckedNum = parseInt(lastCheckedNum);
					}
					//번호 +1을 위한 변수들, 글자를 숫자로 끝 
					//bookCheckItem은 bookCheckList 테이블, idBookHeld는 bookHeld테이블
					let bookCheckItem = data.bookCheckItem;
					let idBookHeld = data.idBookHeld;
					
					//데이터 row 만들기만 하기.
					let newRow = makeRowReturnRow(bookCheckItem,lastCheckedNum+1);
					//테이블에 값 붙여넣기.
					tbodyCheckedBook.prepend(newRow);
					
					//rentedCheckedCount 발견된 대출중 도서가 있다면,
					let rentedCheckedCount = data.rentedCheckedCount;
					
					//점검결과에 따라서 점검결과 숫자 화면 표시 업데이트
					updateTypedCountPlusOne(bookCheckItem.checkResult, rentedCheckedCount);
					
					//입력한 값 초기화
					inputBarcode.value = '';
					//결과에 따른 소리재생 일괄등록이 아닐 경우에만 소리내기
					if(line==null || line==''){
						clickedSound(bookCheckItem.checkResult);
					}
					
					//체크 숫자 업데이트
					getBookCountByType('tbodyWholeCheckList', lastCheckedNum+1);
					
					//입력 후 input에 포커싱
					document.getElementById('input_barcode').focus();
				}
			}
		});
	}
}


//row 데이터 만들기만하고, return row 해서 다른 틀에 붙일 수 있도록
//각종 탭에서 테이블 데이터 만드는데에 사용됨.
//여기다가 각탭 목록수 업데이트하면, 이 함수가 이미 처음 불러오는 함수에 사용되기 때문에
//이미 불러온 숫자에다가 한번더 값을 더해버림. 그래서 낱권 점검실행 함수에 추가.
function makeRowReturnRow(jsonData, rowNum) {
	//데이터 arr로 통일, 그래야 숫자만으로 사용가능.
	let dataArr = [];
	dataArr.push(rowNum);
	dataArr.push(jsonData.checkResult);
	dataArr.push(jsonData.inputBarcode);
	dataArr.push(jsonData.localIdBarcode);
	dataArr.push(jsonData.bookShelf);
	dataArr.push(jsonData.title);
	dataArr.push(jsonData.writer);
	dataArr.push(jsonData.publisher);
	dataArr.push(jsonData.checkIp);
	//row 개별 생성
	let curTr = document.createElement('tr');
	//row 색상 처리
	curTr.style.backgroundColor = resultBackgroundColor(jsonData.checkResult);
	
	for(let j=0; j<dataArr.length; j++){
		let curTd = document.createElement('td');
		let newDiv = document.createElement('div');
		newDiv.style.height = '30px';
		newDiv.style.overflowY = 'scroll';
		if(dataArr[j]==jsonData.bookShelf){
			newDiv.style.backgroundColor = checkShelfMakeColor(dataArr[j]);
			newDiv.style.borderRadius = '5px';
			newDiv.style.display = 'flex';
			newDiv.style.alignItems = 'center';
			newDiv.style.justifyContent = 'center';
			
			let textSpan = document.createElement('span');
//			textSpan.style.backgroundColor = 'black';
//			textSpan.style.color = 'white';
			textSpan.classList.add('oppositeColor');
			textSpan.style.borderRadius = '5px';
			textSpan.textContent = dataArr[j];
			
			newDiv.append(textSpan);
		} else {
			newDiv.textContent = dataArr[j];
		}
		curTd.append(newDiv);
		curTr.append(curTd);
	}
	
	//만들어준 Tr을 리턴
	return curTr;
}


//비교대조용 화면 상단에 전체 도서 불러오기, 모든도서 목록 다불러옴.
function clickedBookHeldList() {
	$.ajax({
		url: "/book_check/book_held_list.do",
		type:'GET',
		data: {
			
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				let bookHeldList = data.bookHeldList;
				let tbody = document.querySelector('.wholetbodyBookList');
				tbody.innerHTML = '';
				let row;
				let curTd;
				let divText;
				//객체 하나 값을 임시적으로 넣어줄
				let dataArr;
				
				if(bookHeldList.length>0){
					for(let i=0; i<bookHeldList.length; i++) {
						//객체 리스트 중 객체 하나씩 처리 과정.
						dataArr = null;
						dataArr = [];
						dataArr.push(bookHeldList.length - i);
						dataArr.push(bookHeldList[i].sortingIndex);
						dataArr.push(bookHeldList[i].localIdBarcode);
						dataArr.push(bookHeldList[i].title);
						dataArr.push(bookHeldList[i].writer);
						dataArr.push(bookHeldList[i].publisher);
						dataArr.push('C'+bookHeldList[i].copyCode);
						
						row = document.createElement('tr');
						//데이터 값 하나씩 처리.
						for(let j=0; j<dataArr.length; j++){
							curTd = document.createElement('td');
							divText = document.createElement('div');
							divText.style.height = "22px";
							divText.style.overflow = "auto";
							divText.innerText = dataArr[j];
							//div값 td에 넣고.
							curTd.appendChild(divText);
							//td값 row 한줄에 넣기.
							row.appendChild(curTd);
						}
						tbody.appendChild(row);
					}
				} else {
					row = document.createElement('tr');
					let noData = document.createElement('td');
					noData.classList.add('text-center');
					noData.setAttribute('colspan','7');
					noData.innerText = '등록된 도서 정보가 없습니다.'
					row.appendChild(noData);
					tbody.appendChild(row);
				}
			}
		}
	});
}

//서가 bookShelf 값을 판단해서 색상을 만들어주는 기능
//해당 맵은 매번 초기화되며, 서가값이 같으면 같은 색을 호출하고
//서가값이 다르면 다른색 호출
//서가값이 없으면, 새로운 색을 랜덤하게 생성하여 대입. 후 색 호출
let mapShelfColor = {};

function checkShelfMakeColor(shelf) {
	if(shelf == null) {
		return null;
	}
	
	if(!mapShelfColor.hasOwnProperty(shelf)) {
		mapShelfColor[shelf] = makeRandomColor(); 
	}
	
	//console.log(shelf+' '+mapShelfColor[shelf]);
	return mapShelfColor[shelf];
}

//랜덤 색상 만들어주기 함수
function makeRandomColor() {
	let result = "#"+Math.round(Math.random()*0xffffff).toString(16);
	return result;
}



//파일 기능
//file open 기능 input 생성, file 불러오기
function openFileBatch() {
	var input = document.createElement('input');
	input.type = "file";
	input.accept = "text/plain, text/html, .jsp";
	
	input.click();
	input.onchange = function(event) {
		processFile(event.target.files[0]);
	}
}

//file open 내에서 파일이 불러져오면 실행되는 기능.
function processFile(file) {
	//점검자가 선택안되었을 경우
	if(selectCheckIp.value == null || selectCheckIp.value == '') {
		alert('점검자를 선택해주세요');
		return;
	}
	
	//progress 바 초기화
	batchProgressInit();
	
	var reader = new FileReader();
	reader.readAsText(file, 'UTF-8');
	reader.onload = function() {
		//값을 하나하나 엔터기준으로 끊어서 배열로 넣기.
		var wholeLines = reader.result.split('\n');
		//progress 최대치 지정.
		setMaxBatchProgress(wholeLines.length);
		for(var line = 0; line < wholeLines.length; line++) {
			//아래 도서등록 함수 반복실행
			clickedCheckBook(wholeLines[line]);
			
		}
	}
}

//엑셀파일만들기
function makeBookCheckExcelFile() {
	$.ajax({
		url: "/book_check/book_check_list_to_excel.do",
		type:'POST',
		data: {
			idBcs
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				let downUrl = "/download.do?file="+data.filePath;
				location.href = downUrl;
			}
		}
	});
}




