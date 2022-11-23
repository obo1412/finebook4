//select 태그
let rtnOrSearch = document.getElementById('rtnOrSearch');
//라벨 태그
let labelRtnOrSearch = document.getElementById('labelRtnOrSearch');
//버튼 태그
let btnRtnOrSearch = document.getElementById('btnRtnOrSearch');
//input 태그
let barcodeBookRtn = document.getElementById('barcodeBookRtn');


rtnOrSearch.addEventListener('change', (e) => {
	if(rtnOrSearch.value=='등록번호'){
		labelRtnOrSearch.innerText = '등록번호 검색';
		btnRtnOrSearch.innerText = '반납';
		barcodeBookRtn.placeholder = '도서바코드를 입력해주세요.';
		barcodeBookRtn.setAttribute("onKeyDown",'rtnBookEnterKeyDown()');
		btnRtnOrSearch.setAttribute("onclick", 'clickedReturnBook()');
	} else {
		labelRtnOrSearch.innerText = '도서제목 검색';
		btnRtnOrSearch.innerText = '도서검색';
		barcodeBookRtn.placeholder = '제목을 입력해주세요.';
		barcodeBookRtn.setAttribute("onKeyDown",'searchBookTitleEnterKeyDown()');
		btnRtnOrSearch.setAttribute("onclick", 'searchBookTitle()');
	}
});





//반납 버튼 엔터
function searchBookTitleEnterKeyDown() {
	if(event.keyCode==13){
		searchBookTitle();
	}
};

//해당 일의 대출/반납 목록
function searchBookTitle(searchBookTitle) {
	searchBookTitle = barcodeBookRtn.value;
	
	console.log(searchBookTitle);
	
	$.ajax({
		url: "/borrow/brw_list_by_title.do",
		type:'GET',
		data: {
			searchBookTitle
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				//console.log(data.brwListByTitle);
				//다른 제목을 검색했을 경우 비워야함.
				var tbody = document.querySelector('.returnRmnListClass');
				var row = null;
				tbody.innerHTML = '';
				let brwRmnList = data.brwListByTitle;
				if(brwRmnList.length>0){
					for(var i=0; i<brwRmnList.length; i++){
						//반복할 필요 없는 변수 처리
						var btn = document.createElement('button');
						btn.classList.add('btn');
						btn.classList.add('btn-sm');
						btn.setAttribute('onclick','clickedReturnOrCancelThisBook()');
						//반복할 필요 없는 변수 처리 끝
						row = tbody.insertRow();
						for(var j=0; j<5; j++){
							cell = row.insertCell(j);
							cell.classList.add('text-center');
						}
						var children = row.childNodes;
						children[0].setAttribute('data-toggle','tooltip');
						children[0].setAttribute('data-placement','top');
						children[0].setAttribute('title',brwRmnList[i].title);
						children[0].innerText = brwRmnList[i].title;
						children[1].innerText = brwRmnList[i].localIdBarcode;
						children[2].innerText = dateFormChange(brwRmnList[i].startDateBrw);
						children[3].innerText = dateFormChange(brwRmnList[i].endDateBrw);
						var endDateBrw = brwRmnList[i].endDateBrw;
						if(endDateBrw!=null&&endDateBrw!=''){
							btn.classList.remove('btn-warning');
							btn.classList.add('btn-primary');
							btn.innerHTML = '반납됨';
						} else {
							btn.classList.remove('btn-primary');
							btn.classList.add('btn-warning');
							btn.innerHTML = '대출중';
						}
						children[4].appendChild(btn);
					}
				} else {
					row = tbody.insertRow();
					cell = row.insertCell(0);
					cell.classList.add('text-center');
					cell.setAttribute('colspan','5');
					cell.innerText = '조회된 대출중 도서가 없습니다.'
				}
			}
		}
	});
};


//반납 버튼 엔터
function rtnBookEnterKeyDown() {
	if(event.keyCode==13){
		clickedReturnBook();
	}
};
//반납 버튼 클릭
function clickedReturnBook(barcodeBookRtn) {
	if(barcodeBookRtn==null||barcodeBookRtn==''){
		var barcodeBookRtnVal = document.getElementById('barcodeBookRtn').value;
		//바코드 번호 한글 -> 영어로 바꿔주는거. 아직 사용 안함.
		//barcodeBookRtnVal = convertKorToEng(barcodeBookRtnVal);
		document.getElementById('barcodeBookRtn').value = barcodeBookRtnVal;
		//컨트롤러쪽과도 변수명 통일을 위해서 생성.
		barcodeBookRtn = barcodeBookRtnVal;
	}
	//매개변수가 존재하지 않으면 위 if 조건 거친 후 반납 바코드 주입
	//매개변수가 존재하면 그냥 바로 아래 ajax 실행
	$.ajax({
		url: "/book/return_book_ok.do",
		type:'POST',
		data: {
			barcodeBookRtn
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				document.getElementById('barcodeBookRtn').value = null;
				
				new Promise((resolve)=> {
					//도서 반납 처리 후, 오늘의 대출/반납 현황 업데이트
					selectBrwListDday();
					resolve();
				}).then(() => {
					//도서 반납 처리 후, 해당 회원의 남아있는 대출 도서 조회
					selectBrwRmnListByMemberId(data.memberId);
				}).then(() => {
					//도서 반납 처리 후, 해당 회원 정보 업데이트
					brwPickMember(data.memberId);
				});
				
				/*//도서 반납 처리 후, 오늘의 대출/반납 현황 업데이트
				selectBrwListDday();
				//도서 반납 처리 후, 해당 회원의 남아있는 대출 도서 조회
				selectBrwRmnListByMemberId(data.memberId);
				//도서 반납 처리 후, 해당 회원 정보 업데이트
				brwPickMember(data.memberId);*/
				
				document.getElementById('barcodeBookRtn').focus();
			}
		}
	});
}










