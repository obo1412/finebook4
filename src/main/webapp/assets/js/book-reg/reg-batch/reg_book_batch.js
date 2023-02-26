
//form 가져오기
const formImportExcelCheck = document.getElementById('formImportExcelCheck');
//행(로우) 개수
const lastRowCount = document.getElementById('lastRowCount');
//열(컬럼) 개수
const lastColCount = document.getElementById('lastColCount');
//서버에올라간 파일 경로
const loadFilePath = document.getElementById('loadFilePath');
//db에 등록된 파일 id
const fileId = document.getElementById('fileId');
let importProcess = null;
//현재 번호 input
const curRow = document.getElementById('curRow');
//진행율 progress
const progBar = document.getElementById('progBar');
//등록프로세스 기준 시간
const timeStandard = document.getElementById('timeStandard').value;
//인터벌 중단 재시작을 위한 변수
let dInterval = null;

//select가 하나이상 선택된다면, 그것을 담을 배열 생성.
let arrColH = [];

//필수정보 처리 체크박스
let mustCode = document.getElementById('mustCode');



//버튼 눌렸을 경우.
function clickedBtnSubmit() {
	//현재 몇번째인지 번호
	let curRowNum = curRow.value;
	
	console.log("["+curRowNum+"] 등록처리");
	if(curRowNum==""||curRowNum==null||curRowNum==0) {
		curRowNum = 1;
	}
	
	//현재 페이지의 셀렉트값 가져와서 배열에 넣기.
	for(let i=0; i<lastColCount.value; i++){
		//배열 순서대로 값 넣기.
		let tempVal = document.getElementById('colH'+i).value;
		arrColH[i] = tempVal;
		//console.log(i+'번째 값:'+arrColH[i]);
		
	}
	
	//필수항목 체크 되면, 변수처리.
	let mustCodeChk = null;
	if(mustCode.checked){
		mustCodeChk = 'on';
	}
	
	importProcess = $.ajax({
		url: "/book/reg_book_batch_ok.do",
		type: 'POST',
		timeout: 0, //timeout 기본은 30000으로 되어있음. 여기선 타임아웃없애기위해 명시.
		traditional:true, //arr 데이터방식 수신하기 위해 명시
		data: {
			loadFilePath: loadFilePath.value,
			lastColCount: lastColCount.value,
			curRow: curRowNum,
			arrColH,
			mustCodeChk
		},
		/* dataType: "json", */
		/*beforeSend: (e) => {
		alert('데이터 통신을 시작합니다.');
		},*/
		success: function(data) {
			if(data.rt != 'OK') {
				//중간 확인 row 인터벌 중단 함수 실행
				cancelProcess();
				
				alert(data.rt);
			} else {
				//서버로부터 받은 현재 단계 input에 넣어주기.
				let thisCurRow = data.curRow;
				curRow.value = thisCurRow;
				progBar.value = thisCurRow;
				//alert(data.msg);
				//전부다 등록되면 페이지 이동처리.
				if(Number(data.curRow)>=lastRowCount.value) {
					//중간 확인 row 인터벌 중단
					clearInterval(dInterval);
					location.href = '/book/import_book_excel.do?menuHolder=setting';
				}
			}
		}
	}); //ajax 문 종료
	
	//인터벌 함수 실행
	displayInterval();
}

//ajax 중단
function cancelProcess() {
	//console.log();
	
	//중간 확인 row 인터벌 중단
	clearInterval(dInterval);
	
	$.ajax({
		url: "/book/reg_book_batch_pause.do",
		type: 'POST',
		data: {
			//서버 단에서 전역변수로 선언한 스토퍼를 조작하기 때문에
			//여기선 그냥 컨트롤러 클래스만 호출해주면 된다.
			//stopper: 1
		},
		/* dataType: "json", */
		/*beforeSend: (e) => {
		alert('데이터 통신을 시작합니다.');
		},*/
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				
			}
		}
	}); //ajax 문 종료
}

//현재 몇건 등록됬는지 db에 값 가져오는 비동기함수
function displayProgressing() {
	$.ajax({
		url: "/book/import_book_excel_display_current_progress.do",
		type: 'GET',
		data: {
			//기준시간 넣어주고
			timeStandard
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				progBar.value = data.regOkRow;
			}
		}
	}); //ajax 문 종료
}

//위 함수를 인터벌로 실행
function displayInterval() {
	dInterval = setInterval(displayProgressing, 5000);
}


//txt 일괄 등록함수
//버튼 눌렸을 경우.
function clickedBtnTxtBatchSubmit() {
	//for문의 i를 위한 마지막 숫자 input으로부터 구하기.
	const lastRow = document.querySelector("#lastRow").value;
	//보내기 위한 2차원 배열 데이터
	let theArr = [];
	
	for(let i=0; i<lastRow; i++) {
		const row = document.getElementsByName("row"+i);
		let itemArr = [];
		for(let j=0; j<15; j++) {
			if(j==0) {
				//체크박스 값 읽어와서, 참거짓 값 넣기.
				itemArr.push(row[j].checked);
			} else {
				//일반 텍스트 값.
				let curData = row[j].value;
				while(curData.indexOf(",")>-1) {
					curData = curData.replace(/,/g, "#@$");
				}
				itemArr.push(curData);
			}
		}
		//건너띄기 체크되어있으면 건너띄고, 체크 안되어있으면 최종함수에 넣지 않기.
		if(itemArr[0] == true) {
			theArr.push(itemArr);
		}
	}
	
	//console.log(theArr);
	
	importProcess = $.ajax({
		url: "/book/reg_book_txt_batch_ok.do",
		type: 'POST',
		timeout: 0, //timeout 기본은 30000으로 되어있음. 여기선 타임아웃없애기위해 명시.
		traditional:true, //arr 데이터방식 수신하기 위해 명시
		data: {
			theArr
		},
		success: function(data) {
			if(data.rt != 'OK') {
				//완료가 아닐 경우 알럿 띄우기
				alert(data.rt);
			} else {
				//전부다 등록되면 페이지 이동처리.
				if(Number(data.curRow)>=lastRowCount.value) {
					location.href = '/book/book_held_list.do?chkBox_tag_search=checked?menuHolder=book';
				}
			}
		}
	});
	//ajax 문 종료
}


//제목과 저자명 input 값이 바뀌면 저자기호 코드 호출
const makeAtc = document.querySelector('.makeAtc');
makeAtc.addEventListener('change', function(){
	makeAuthorCode();
});

//저자코드 생성 ajax 호출문
function makeAuthorCode() {
	
	var thisIsbn = document.getElementById('search-book-info').value;
	var thisTitle = document.getElementById('bookTitle').value;
	var thisAuthor = document.getElementById('author').value;
	var atcout = null;
	
	if(thisTitle!=null&&thisAuthor!=null) {
		$.ajax({
			url: "${pageContext.request.contextPath}/book/author_code.do",
			type: 'POST',
			data: {
				thisIsbn,
				thisTitle,
				thisAuthor,
			},
			/* dataType: "json", */
			success: function(data) {
				if(data.rt != 'OK') {
					alert(data.rt);
				} else {
					document.getElementById('authorCode').value = data.result;
					document.getElementById('copyCode').value = data.copyCode;
				}
			}
		});
	}
};




