

	/*
	 *	print tag setup js 시작
	 *	 
	 */
	const tagTypeClass = document.querySelector('.tagTypeClass');
	let tagTypeByName = document.getElementsByName('tagType');
	//txt 일괄 라벨 인쇄를 위한 변수
	let tagTypeTxtBatch = document.getElementById('tagTypeTxtBatch');
	//라벨 선택시 선택한 라벨만 보이도록.
	const divPickedLabel = document.getElementById('divPickedLabel');
	const imgPickedLabel = document.getElementById('imgPickedLabel');
	const btnLabelCollapse = document.getElementById('btnLabelCollapse');
	
	let startPoint = document.querySelector('#startPoint');
	startPoint.addEventListener('change', () => {
		let startPointTxtBatch = document.querySelector('#startPointTxtBatch');
		let num = startPoint.value;
		if(num=='') {
			num = 1;
		}
		startPointTxtBatch.value = num;
	});
	
	window.addEventListener('load', function() {
		for(var i=0; i<tagTypeByName.length; i++){
			if(tagTypeByName[i].checked) {
				let pickedLabel = tagTypeByName[i].parentNode.parentNode.childNodes;
				let pickedImg = pickedLabel[1];
//				console.log(pickedLabel[1].getAttribute('src'));
				let pickedSrc = pickedLabel[1].getAttribute('src');
				imgPickedLabel.setAttribute('src', pickedSrc);
			}
		}
		
		//색상 고정 및 불러오기 위한 로컬스토리지 사용
		useLocalStorageForColor();
	})
	
	
	
	tagTypeClass.addEventListener('change', (e)=> {
		for(var i=0; i<tagTypeByName.length; i++){
			if(tagTypeByName[i].checked) {
				//안쓰는 박스 지우기.
				deleteAnotherBox();
				
				tagTypeSepA4(tagTypeByName[i].value, tagTypeByName[i].parentNode.innerText);
				//console.log(tagTypeByName[i]);
				
				//일괄 출력부분 태그 타입 변경
				tagTypeTxtBatch.value = tagTypeByName[i].value;
				
				//특수 태그색상 관리 버튼 보이게/안보이게 토글버튼
				/*colorSetupBtnPop(tagTypeByName[i].value);*/
				
				//선택한 태그 이미지 보이기
				let pickedLabel = tagTypeByName[i].parentNode.parentNode.childNodes;
				let pickedImg = pickedLabel[1];
				let pickedSrc = pickedLabel[1].getAttribute('src');
				imgPickedLabel.setAttribute('src', pickedSrc);
				//상단 모습 바꾸고 다시 컬랩스 닫기.
				btnLabelCollapse.click();
			}
		}
	});
	
	//별도(separate) 타입인 경우, 화면에 이미지 전달이 아니라, 텍스트만 전달하기.
	function tagTypeSepA4(TypeSep, typeText) {
		if(TypeSep==3||TypeSep==4||TypeSep==5||TypeSep==6) {
			
			//안쓰는 박스 지우기.
			deleteAnotherBox();
			
			const divText = document.createElement('div');
			divText.classList.add('sep__type__div');
			divText.innerText = typeText;
			
			divPickedLabel.appendChild(divText);
		}
	}
	
	//이미지와 박스 통일하기 귀찮아서 이런식으로 붙여씀.
	//박스 지우기.
	function deleteAnotherBox() {
		if(divPickedLabel.querySelector('.sep__type__div') != null) {
			divPickedLabel.querySelector('.sep__type__div').remove();
		}
	}
	
	//롤타입 라벨 16번 특수로 버튼 하나 보이게하기. 색상 별도 관리.
	const btnColorSet16 = document.querySelector('.btn__color__set__16');
	function colorSetupBtnPop(tagType) {
		if(tagType==16) {
			btnColorSet16.classList.remove('modal__hidden');
		} else {
			btnColorSet16.classList.add('modal__hidden');
		}
	}
	
	//클릭했을 경우 클릭된 라벨 보이기
//	tagTypeClass.addEventListener('click', (e)=> {
//		if(e.target.classList.contains('labelSet')) {
//			console.log(e.target);
//			divPickedLabel.innerHTML = '';
//			let curCheckedLabel = e.target.parentNode.cloneNode();
//			let divShowLabel = document.importNode(curCheckedLabel);
//			divPickedLabel.appendChild(divShowLabel);
			//appendChild 기능은 복사해서 붙여넣는 것이 아니라 이동시키는 것.
//		}
//	});
	
	function autoSheetSortingPopup() {
		var tagT = 0;
		tagT = $('input:radio[name=tagType]:checked').val();
		
		var printingEa = document.getElementById('printingEa').value;
		var printingSheetCount = document.getElementById('printingSheetCount').value;
		
		var rangeS = ((printingSheetCount - 1) * printingEa) + 1;
		var rangeE = printingEa * printingSheetCount ;
		
		var url = '/book/print_tag_page.do?tagType='+tagT;
		url = url + '&rangeStart='+rangeS +'&rangeEnd='+rangeE;
		window.open(url, '_blank', 'width=1080,height=800,scrollbars=yes');
		
		$.ajax({
			url: "/book/autoSheetCountUp.do",
			type: 'POST',
			data: {
				printingEa,
				printingSheetCount
			},
			/* dataType: "json", */
			success: function(data) {
				document.getElementById('printingSheetCount').value = data.result;
			}
			,error:function(request,status,error){
				alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	};
	
	//제목 titleSort 엔터
	function enterTitle() {
		if(event.keyCode==13){
			openPopupTitle();
		}
	}
	
	function openPopupTitle() {
		var tagT = 0;
		tagT = $('input:radio[name=tagType]:checked').val();
		var titleBook = document.getElementById('titleSorting').value;
		
		var url = '/book/print_tag_page.do?tagType='+tagT;
		url = url + '&titleSorting='+titleBook;
		window.open(url, '_blank', 'width=1080,height=800,scrollbars=yes');
	}
	
	//등록번호 targetsort 엔터
	function enterTarget() {
		if(event.keyCode==13){
			openPopupTarget();
		}
	}
	
	//등록번호 target 출력 
	function openPopupTarget() {
		var tagT = 0;
		tagT = $('input:radio[name=tagType]:checked').val();
		var targetBarcode = document.getElementById('targetSorting').value;
		//시작위치 변수 그때그때 객체선언으로 값가져와야한다.
		let startPoint = document.querySelector('#startPoint').value;
		
		var url = '/book/print_tag_page.do?tagType='+tagT;
		url = url + '&targetSorting='+targetBarcode+"&startPoint="+startPoint;
		if(tagT==16) {
			let spcCode = encodeURIComponent(localStorage.getItem("colorArr"));
			url = url + "&colorArr="+spcCode;
			let keyArr = encodeURIComponent(localStorage.getItem("keywordArr"));
			url = url + "&keyArr="+keyArr;
		}
		window.open(url, '_blank', 'width=1080,height=800,scrollbars=yes');
	}
	
	//범위출력 등록번호
	function openPopup() {
		var tagT = 0;
		tagT = $('input:radio[name=tagType]:checked').val();
		
		var rangeS = $("#rangeStart").val();
		var rangeE = $("#rangeEnd").val();
		
		let startPoint = document.querySelector('#startPoint').value;
		
		var url = '/book/print_tag_page.do?tagType='+tagT;
		url = url + '&rangeStart='+rangeS +'&rangeEnd='+rangeE;
		url = url + "&startPoint="+startPoint;
		if(tagT==16) {
			let spcCode = encodeURIComponent(localStorage.getItem("colorArr"));
			url = url + "&colorArr="+spcCode;
			let keyArr = encodeURIComponent(localStorage.getItem("keywordArr"));
			url = url + "&keyArr="+keyArr;
		}
		window.open(url, '_blank', 'width=1080,height=800,scrollbars=yes');
	};

//범위 직접 기입 바코드
function openRangeInputBarcode() {
	let rangeInputBarcode = document.querySelector("#rangeInputBarcode").value;
	let ribStartNum = document.querySelector("#ribStartNum").value;
	let ribEndNum = document.querySelector("#ribEndNum").value;
	//저자기호만 골라서 처리
	let inputAuthorCode = document.querySelector("#inputAuthorCode").value;
	
	var tagT = 0;
		tagT = $('input:radio[name=tagType]:checked').val();
	
	var url = '/book/print_tag_page.do?tagType='+tagT+"&sortingType=1";
	url = url + "&rangeInputBarcode="+rangeInputBarcode
				+ "&inputAuthorCode=" +inputAuthorCode
				+ "&ribStartNum="+ribStartNum
				+ "&ribEndNum="+ribEndNum;
	url = url + "&tempTitleMt="+localStorage.getItem("titleSectionMarginTop");
	if(tagT==16) {
		let spcCode = encodeURIComponent(localStorage.getItem("colorArr"));
		url = url + "&colorArr="+spcCode;
		let keyArr = encodeURIComponent(localStorage.getItem("keywordArr"));
		url = url + "&keyArr="+keyArr;
	}
	window.open(url, '_blank', 'width=1080,height=800,scrollbars=yes');
}




//라벨 모달창 토글버튼
function modalLabelsOpenClose() {
	tagTypeClass.classList.toggle('modal__hidden');
}


//로컬스토리지 사용
function useLocalStorageForColor() {
	let loadedArr = localStorage.getItem('colorArr');
	if(loadedArr == null) {
		let keywordArr = ["예술과문학","인물","기억","공존","변화","삶","동심","존재","교육","자연","인기도서"];
		localStorage.setItem('keywordArr', keywordArr);
		let colorArr = ["#008843","#e2007f","#908078","#f3cd16","#00573b"
		,"#142b88","#605047","#7ebe28","#eb6d00","#00a0e9","#000000"];
		localStorage.setItem('colorArr', colorArr);
	}
	
	let titleSectionMarginTop = localStorage.getItem("titleSectionMarginTop");
	if(titleSectionMarginTop==null) {
		let marginTop = 1;
		localStorage.setItem('titleSectionMarginTop', marginTop);
	}
	
	//console.log(loadedArr);
}

//색상 및 키워드분류 초기화 클릭 버튼
let btnInitColor = document.querySelector(".btn__init__color");
btnInitColor.addEventListener('click', (e)=>{
	e.preventDefault();
	//초기화
	let keywordColumn = document.querySelector(".div__keyword__column");
	let colorColumn = document.querySelector(".div__color__column");
	keywordColumn.innerHTML = "";
	colorColumn.innerHTML = "";
	
	let keywordArr = ["예술과문학","인물","기억","공존","변화","삶","동심","존재","교육","자연","인기도서"];
	let colorArr = ["#008843","#e2007f","#908078","#f3cd16","#00573b"
	,"#142b88","#605047","#7ebe28","#eb6d00","#00a0e9","#000000"];
	//키워드 돌리기
	keywordArr.map((item)=>{
		let newInput = document.createElement("input");
		newInput.value = item;
		newInput.setAttribute("name","addiKeyword");
		keywordColumn.append(newInput);
	});
	//마지막 한칸 만들어주기
	let newInputLastKeyword = document.createElement("input");
	newInputLastKeyword.setAttribute("name","addiKeyword");
	keywordColumn.append(newInputLastKeyword);
	//색상 돌리기
	colorArr.map((item)=>{
		let newInput = document.createElement("input");
		newInput.value = item;
		newInput.setAttribute("name","addiColor");
		colorColumn.append(newInput);
	});
	//마지막 한칸 만들어주기
	let newInputLastColor = document.createElement("input");
	newInputLastColor.setAttribute("name","addiColor");
	colorColumn.append(newInputLastColor);
});

//16라벨 경기도청 어쩌구 색상 별도관리 버튼 기능 모달 on/off
const divColorSet16 = document.querySelector(".div__color__set__16");
btnColorSet16.addEventListener('click', (e)=>{
	e.preventDefault();
	divColorSet16.classList.toggle('modal__hidden');
	//안보일땐 동작 종료.
	if(divColorSet16.classList.contains("modal__hidden")) return;
	getColorSetArr();
	
	//임시 마진탑 
	getTempSize();
});


//임시 타이틀공간에 마진탑 가져오기.
function getTempSize() {
	let marginTopSize = localStorage.getItem("titleSectionMarginTop");
	let tempTitleMt = document.querySelector("#tempTitleMt");
	tempTitleMt.value = marginTopSize;
}

//모달 띄울때, 데이터 셋 가져오기.
function getColorSetArr() {
	let keywordArrStr = localStorage.getItem("keywordArr");
	let colorArrStr = localStorage.getItem("colorArr");
	let keywordArr = keywordArrStr.split(",");
	let colorArr = colorArrStr.split(",");
	
	let keywordColumn = document.querySelector(".div__keyword__column");
	let colorColumn = document.querySelector(".div__color__column");
	//초기화
	keywordColumn.innerHTML = "";
	colorColumn.innerHTML = "";
	
	//키워드 돌리기
	keywordArr.map((item)=>{
		let newInput = document.createElement("input");
		newInput.value = item;
		newInput.setAttribute("name","addiKeyword");
		keywordColumn.append(newInput);
	});
	//마지막 한칸 만들어주기
	let newInputLastKeyword = document.createElement("input");
	newInputLastKeyword.setAttribute("name","addiKeyword");
	keywordColumn.append(newInputLastKeyword);
	//색상 돌리기
	colorArr.map((item)=>{
		let newInput = document.createElement("input");
		newInput.value = item;
		newInput.setAttribute("name","addiColor");
		colorColumn.append(newInput);
	});
	//마지막 한칸 만들어주기
	let newInputLastColor = document.createElement("input");
	newInputLastColor.setAttribute("name","addiColor");
	colorColumn.append(newInputLastColor);
}

//로컬스토리지 색상 변경 버튼
const btnChangeColorSet = document.querySelector(".btn__change__color__set");
btnChangeColorSet.addEventListener("click",(e)=>{
	e.preventDefault();
	let addiKeyword = document.querySelectorAll("input[name=addiKeyword]");
	let addiColor = document.querySelectorAll("input[name=addiColor]");
	
	let keywordArr = [];
	addiKeyword.forEach((item)=>{
		if(item.value!="") {
			keywordArr.push(item.value);
		}
	})
	localStorage.removeItem("keywordArr");
	localStorage.setItem("keywordArr",keywordArr);
	
	let colorArr = [];
	addiColor.forEach((jtem)=>{
		if(jtem.value!=""){
			colorArr.push(jtem.value);
		}
	})
	localStorage.removeItem("colorArr");
	localStorage.setItem("colorArr",colorArr);
	
	
	
	//마진 탑도 여기서 변경
	let tempTitleMt = document.querySelector("#tempTitleMt");
	localStorage.setItem('titleSectionMarginTop', tempTitleMt.value);
})












