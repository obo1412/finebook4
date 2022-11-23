


//도서목록 tbody
let tbodyBookList = document.querySelector('.tbody__book__list');
let btnBrwReturn = document.querySelector('#btnBrwReturn');
let bookMemberBrwKeyword = document.querySelector('#bookMemberBrwKeyword');
//대출/반납을 위한 도서 및 회원 id 값
let inputMemberId = document.querySelector('#member__detail__id');
let inputBookId = document.querySelector('#book__detail__id');
//타이머 스위치
let timerSwitch = 0;
//타이머 함수를 담을 객체
let memberTimer = null;

//화면 로딩시
window.addEventListener('load', () => {
	bookMemberBrwKeyword.focus();
});


//회원정보 불러오면, 시간 생성. 시간 지날동안 아무것도 안하면 회원 상세정보 없애기.
document.querySelector('body').addEventListener('click', (e) => {
	if(e.target.closest('#btnClearMemberDetail')) {
		console.log('회원정보 초기화 버튼 클릭');
		//타이머종료함수
		stopTimerMemberDetail();
		//회원상세 정보 비우기 
		clearMemberDetail();
	} else {
		//클릭하면 타이머 숫자 세팅 및 시간 초기화
		setTimerMemberDetail();
	}
});

//함수하나로 해서 이것만 수정하면 나머지 다른 곳에서도 숫자 다수정되도록.
function setSecondTimer() {
	document.querySelector('#btnClearMemberDetail').innerHTML = 60;
}

//타이머 숫자 세팅.
function setTimerMemberDetail() {
	//회원 정보가 있는 상태에서만 시간 연장.
	if(inputMemberId.value != "") {
		console.log('회원상세정보 시간 주입');
		//숫자 세팅
		setSecondTimer();
		//스위치가 켜져있지 않을 경우 타이머가 동작중이 아닐때만 타이머 동작.
		if(timerSwitch == 0) {
			initTimerMemberDetail();
		}
	}
}

//타이머 실행
function initTimerMemberDetail() {
	console.log('initTimerMemberdetail 함수 동작');
	//시작안됐을 경우에만 숫자 주입하고 타이머처리
	setSecondTimer();

	if(timerSwitch == 0) {
		console.log('setinterval 함수 동작 중복되면 안된다.');
		
		//스위치 올림.
		timerSwitch++;
		
		//타이머 함수만 밖으로 빼기. 다른 곳에서도 타이머를 종료해야하므로.
		memberTimer = setInterval(() => {
			//console.log('카운트다운 실행');
			let curSec = document.querySelector('#btnClearMemberDetail').innerHTML;
			//console.log(curSec);
			document.querySelector('#btnClearMemberDetail').innerHTML = curSec-1;
			
			if(curSec == 0) {
				//타이머종료함수
				stopTimerMemberDetail()
				//회원상세 정보 칸 비우기.
				clearMemberDetail();
			}
			
		}, 1000);
	}
};

//타이머 종료.
function stopTimerMemberDetail() {
	console.log('타이머 종료/스위치0');
	//타이머 인터벌 클리어
	clearInterval(memberTimer);
	document.querySelector('#btnClearMemberDetail').innerHTML = "x";
	timerSwitch = 0;
}

//대출/반납 버튼 클릭 동작
btnBrwReturn.addEventListener('click', searchBookMemberBrw);

//대출/반납 처리 함수 종합적으로 다 처리해야됨.
function searchBookMemberBrw() {
	let searchKeyword = bookMemberBrwKeyword.value;
	console.log("대출/반납 입력 검색어"+searchKeyword);
	
	$.ajax({
		url: "/user_self_page/search_brw_book_member.do",
		type:'GET',
		data: {
			searchKeyword
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				//회원정보
				let memberList = data.memberList;
				//도서정보
				let bookItem = data.bookHeld;
				if(memberList<1 && bookItem==null){
					alert('해당하는 도서 또는 회원정보가 없습니다.');
					return;
				}
				
				//검색어 초기화.
				bookMemberBrwKeyword.value = "";
				
				
				//회원 길이가 2개 이상이면 리스트로 뿌리기.
				if(memberList.length>1) {
					memberDataToList(memberList);
				} else if(memberList.length ==1) {
					memberDetailView(memberList[0]);
				} else {
					//회원정보 결과가 없으면 아무것도 하지 않음.
				}
				
				//도서 정보 상세 보기. 어쨌거나저쨌거나 도서상태는 보여주기.
				//결과값 없을 경우 도서상세정보 칸 내용은 초기화됨.
				bookDetailView(bookItem);
				
				//화면에 회원번호가 없고, 도서정보만 있을 경우.
				if((inputMemberId.value=='' || inputMemberId.value==null) && bookItem!=null){
					//도서만 찍었을 때, 도서 반납 또는 그냥 디스플레이 처리.
					doBrwRtnBookCheck(bookItem.id);
				}
				
				//화면에 표현된 회원번호가 있고, 도서 값이 입력되면 대출처리.
				if(inputMemberId.value!='' && inputMemberId.value!=null && bookItem!=null) {
					doBrwRtn(inputMemberId.value, bookItem.id);
				}
				
			}
		}
	});
};

//회원정보가 없는 상태에서, 즉 도서만 찍었을때 대출/반납 처리.
function doBrwRtnBookCheck(bookId) {
	
	$.ajax({
		url: "/user_self_page/brw_rtn_book_check.do",
		type:'POST',
		data: {
			bookId
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				let resultMsg = data.resultMsg;
				//반납처리가 되었다면, 반납 알림 띄우기
				if(resultMsg != null){
					//대출 반납 상태 0이면 대출중, 1이면 대출가능
					let bookStateNum = data.bookStateNum;
					//대출중 대출가능 상태만 변경하는 함수
					showBookAvailableState(bookStateNum);
					
					//알람
					alert(resultMsg);
					console.log('회원정보없는 대출반납처리 반납처리 성공')
				}
				
			}
		}
	});
}


//회원정보가 올라와있는 상태에서 대출/반납 처리.
function doBrwRtn(memberId, bookId) {
	
	$.ajax({
		url: "/user_self_page/brw_rtn_book_member_ok.do",
		type:'POST',
		data: {
			memberId,
			bookId
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				let brwList = data.brwList;
				
				displayBrwList(brwList);
				
				//대출 반납 상태 0이면 대출중, 1이면 대출가능
				let bookStateNum = data.bookStateNum;
				//대출중 대출가능 상태만 변경하는 함수
				showBookAvailableState(bookStateNum);
				
				//메시지 띄우기
				let resultMsg = data.resultMsg;
				if(resultMsg != null){
					alert(resultMsg);
				}
			}
		}
	});
	
}

//도서 검색 버튼 엔터
function searchBookMemberBrwKeyDown() {
	if(event.keyCode==13){
		searchBookMemberBrw();
	}
};


//도서목록 클릭시 동작. 도서 상세보기로
tbodyBookList.addEventListener('click', (e)=>{
	if(e.target.classList.contains('tbl__title__div')) {
		//console.log(e.target.parentNode.querySelector('.book__data'));
		let data = e.target.parentNode.querySelector('.book__data').innerHTML;
		//console.log(data);
		let jsonData = JSON.parse(data);
		//console.log(jsonData.title+" "+jsonData.writer);
		
		//도서 정보 상세 보기.
		bookDetailView(jsonData);
		
		bookMemberBrwKeyword.value = jsonData.localIdBarcode;
	}
});

//도서 상태 (대출중, 대출가능) 만 변경하는 함수.
function showBookAvailableState(bookStateNum) {
	let bookState = "";
	if(bookStateNum == 1) {
		bookState = "대출가능";
		document.getElementById('book__detail__state').style.color = "black";
	} else {
		bookState = "대출중";
		document.getElementById('book__detail__state').style.color = "red";
	}
	document.getElementById('book__detail__state').innerHTML = bookState;
}


//도서 상세 보기로 전환.
function bookDetailView(jsonData) {
	//상세보기 지우기.
	clearBookDetail();
	
	if(jsonData == null) {
		return;
	}
	//도서 id 고유값 데이터에 넣기.
	document.getElementById('book__detail__id').value = jsonData.id;
	
	document.getElementById('bookBarcode').value = jsonData.localIdBarcode;
	let bookState = "대출가능<br/>회원정보 입력 하신 후<br/>대출 진행해주세요.";
	document.getElementById('book__detail__state').style.color = "#1300ed";
	if(jsonData.available == 0) {
		bookState = "대출중";
		document.getElementById('book__detail__state').style.color = "red";
	}
	document.getElementById('book__detail__state').innerHTML = bookState;
	document.getElementById('book__detail__title').textContent = jsonData.title;
	document.getElementById('book__detail__writer').textContent = jsonData.writer;
	document.getElementById('img__book').src = jsonData.imageLink;
	document.getElementById('book__detail__publisher').textContent = jsonData.publisher;
	document.getElementById('book__detail__pubDate').textContent = jsonData.pubDate;
	document.getElementById('book__detail__description').textContent = jsonData.description;
	document.getElementById('book__detail__addiCode').textContent = jsonData.additionalCode;
	document.getElementById('book__detail__classCode').textContent = jsonData.classificationCode;
	document.getElementById('book__detail__authorCode').textContent = jsonData.authorCode;
	if(jsonData.volumeCode != null){
		document.getElementById('book__detail__volumeCode').textContent = "v."+jsonData.volumeCode;
	} else {
		document.getElementById('book__detail__volumeCode').textContent = "";
	}
	if(jsonData.copyCode != null && jsonData.copyCode != 0){
		document.getElementById('book__detail__copyCode').textContent = "c."+jsonData.copyCode;
	} else {
		document.getElementById('book__detail__copyCode').textContent = "";
	}
}

//도서 상세보기 정보 지우기.
function clearBookDetail() {

	document.getElementById('book__detail__id').value = null;
	document.getElementById('bookBarcode').value = null;
	document.getElementById('book__detail__state').textContent = null;
	document.getElementById('book__detail__state').style.color = "black";
	document.getElementById('book__detail__title').textContent = null;
	document.getElementById('book__detail__writer').textContent = null;
	document.getElementById('img__book').src = "";
	document.getElementById('book__detail__publisher').textContent = null;
	document.getElementById('book__detail__pubDate').textContent = null;
	document.getElementById('book__detail__description').textContent = null;
	document.getElementById('book__detail__addiCode').textContent = null;
	document.getElementById('book__detail__classCode').textContent = null;
	document.getElementById('book__detail__authorCode').textContent = null;
	document.getElementById('book__detail__volumeCode').textContent = null;
	document.getElementById('book__detail__copyCode').textContent = null;
	
	bookMemberBrwKeyword.value= "";
}

//도서상세정보칸에 x 버튼 누르면 동작.
document.querySelector('#btnClearBookDetail').addEventListener('click', ()=> {
	clearBookDetail();
});



//도서 검색 버튼 엔터
function searchBookEnterKeyDown() {
	if(event.keyCode==13){
		searchBook();
	}
};

//도서 검색 버튼 동작
function searchBook() {
	let searchBookKeyword = document.getElementById('searchBookKeyword').value;
	//console.log(searchBookKeyword);
	
	if(searchBookKeyword=='') {
		alert('검색어를 입력해주세요.');
		return;
	}
	
	$.ajax({
		url: "/user_self_page/search_book_list.do",
		type:'GET',
		data: {
			searchBookKeyword
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				let bookList = data.bookList;
				
				//검색어 초기화
				document.getElementById('searchBookKeyword').value = "";
				
				//tbody 인스턴스
				const tbody = document.querySelector('.tbody__book__list');
				//tbody 초기화
				tbody.innerHTML = "";
				//결과값이 없을때
				if(bookList.length==0) {
					let trNoResult = document.createElement("tr");
					let tdNoResult = document.createElement("td");
					tdNoResult.colSpan = "3";
					tdNoResult.textContent = "검색 결과가 없습니다.";
					trNoResult.appendChild(tdNoResult);
					tbody.appendChild(trNoResult);
					
					return;
				}
				//템플릿 컨텐츠 인스턴스
				let tmpl = document.querySelector('#tmpl__book__tr').content;
				
				for(let i=0; i<bookList.length; i++){
					let tmplClone = tmpl.cloneNode(true);
					tmplClone.querySelector('.tbl__num').textContent = i+1;
					tmplClone.querySelector('.tbl__title__div').textContent = bookList[i].title;
					tmplClone.querySelector('.tbl__author__div').textContent = bookList[i].writer;
					let bookState = "-";
					if(bookList[i].available==0) {
						bookState = "대출중";
						tmplClone.querySelector('.tbl__state').style.color = "red";
					}
					tmplClone.querySelector('.tbl__state').textContent = bookState;
					tmplClone.querySelector('.tbl__barcode').textContent = bookList[i].localIdBarcode;
					
					tmplClone.querySelector('.book__data').innerHTML = JSON.stringify(bookList[i]);
					tbody.appendChild(tmplClone);
				}
				
			}
		}
	});
};









//회원목록 tbody
let tbodyMemberList = document.querySelector('.tbody__member__list');
//회원 이름 클릭시 상세 정보로 이동
//아래기능은 회원목록이 삭제되었으므로 사용 안함.
//tbodyMemberList.addEventListener('click', (e)=>{
//	if(e.target.classList.contains('member__name')) {
//		//console.log(e.target.parentNode.querySelector('.book__data'));
//		let data = e.target.parentNode.querySelector('.member__data').innerHTML;
//		//console.log(data);
//		let jsonData = JSON.parse(data);
//		//console.log(jsonData.title+" "+jsonData.writer);
//		
//		//회원 상세보기 함수 실행.
//		memberDetailView(jsonData);
//	}
//});

//회원 상세 보기로 전환.
//brwListMember 회원의 대출/반납리스트 기능 사용
function memberDetailView(jsonData) {
	
	//저 위에 정의됨. 타이머 동작함수
	initTimerMemberDetail();
	
	document.getElementById('member__detail__memberCode').value = jsonData.barcodeMbr;
	document.getElementById('member__detail__name').textContent = jsonData.name;
	document.getElementById('member__detail__phone').textContent = jsonData.phone;
	//이미지 경로 추출
	document.getElementById('img__member').src = '';
	if(jsonData.profileImg!=''&&jsonData.profileImg!=null&& jsonData.profileImg != 'null') {
		let profileImg = jsonData.profileImg;
		profileImg = profileImg.substring(profileImg.indexOf("/upload"));
		profileImg = "/filesMapping"+profileImg;
		document.getElementById('img__member').src = profileImg;
	}
		//이미지 경로 추출
	document.getElementById('member__detail__grade').textContent = jsonData.gradeName
		+"("+jsonData.dateLimit+"일/"+jsonData.brwLimit+"권)";
	
	//데이터 다 넣기
	//document.querySelector('.member__detail__data').textContent = jsonData;
	//회원 id 고유값
	document.querySelector('#member__detail__id').value = jsonData.id;
	
	//회원의 대출/반납리스트
	brwListMember(jsonData.id);
}

//회원상세정보칸에 x 버튼 누르면 회원 정보 초기화.
//아래 기능은 전체클릭 이벤트가 더 우선순위라 동작안함.
//document.querySelector('#btnClearMemberDetail').addEventListener('click', ()=> {
//	//타이머종료함수
//	stopTimerMemberDetail();
//	//회원상세 정보 비우기 
//	clearMemberDetail();
//});

//회원상세 정보 비우기
function clearMemberDetail() {
	document.getElementById('member__detail__memberCode').value = null;
	document.getElementById('member__detail__name').textContent = null;
	document.getElementById('member__detail__phone').textContent = null;
	document.getElementById('img__member').src = '';
	document.getElementById('member__detail__grade').textContent = null;
	
	document.querySelector('#member__detail__id').value = null;
	//대출 반납 목록까지 비우기.
	document.querySelector('.tbody__brw__list').innerHTML = null;
}


//회원 검색 버튼 엔터
function searchMemberEnterKeyDown() {
	if(event.keyCode==13){
		searchMember();
	}
};

//회원 검색 버튼 동작
function searchMember() {
	let searchMemberKeyword = document.getElementById('searchMemberKeyword').value;
	//console.log(searchMemberKeyword);
	
	if(searchMemberKeyword=='') {
		alert('검색어를 입력해주세요.');
		return;
	}
	
	$.ajax({
		url: "/user_self_page/search_member_list.do",
		type:'GET',
		data: {
			searchMemberKeyword
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				let memberList = data.memberList;
				
				memberDataToList(memberList);
			}
		}
	});
};

//object 데이터를, 리스트로 뿌려주기.
function memberDataToList(memberList) {
	
	//검색어 초기화
	document.getElementById('searchMemberKeyword').value = "";
	
	//tbody 인스턴스
	const tbodyMember = document.querySelector('.tbody__member__list');
	//tbody 초기화
	tbodyMember.innerHTML = "";
	//결과값이 없을때
	if(memberList.length==0) {
		let trNoResult = document.createElement("tr");
		let tdNoResult = document.createElement("td");
		tdNoResult.colSpan = "2";
		tdNoResult.textContent = "검색 결과가 없습니다.";
		trNoResult.appendChild(tdNoResult);
		tbodyMember.appendChild(trNoResult);
		
		return;
	}
	//템플릿 컨텐츠 인스턴스
	let tmpl = document.querySelector('#tmpl__member__tr').content;
	
	for(let i=0; i<memberList.length; i++){
		let tmplClone = tmpl.cloneNode(true);
		tmplClone.querySelector('.member__name').textContent = memberList[i].name;
		tmplClone.querySelector('.member__phone').textContent = memberList[i].phone;
		
		tmplClone.querySelector('.member__data').innerHTML = JSON.stringify(memberList[i]);
		tbodyMember.appendChild(tmplClone);
	}
}


//회원의 대출/반납 리스트
let brwListMember = (memberId) => {
	console.log('해당 회원의 대출/반납 리스트 함수 - 회원id:'+memberId);
	$.ajax({
		url: "/user_self_page/brw_list_member_id.do",
		type:'GET',
		data: {
			memberId
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				let brwRmnList = data.brwRmnList;
				
				displayBrwList(brwRmnList);
			}
		}
	});
}

//해당 회원의 대출반납 목록 리스트로 옮기기 함수
function displayBrwList(dataList) {
	
	//tbody 인스턴스
	const tbodyBrw = document.querySelector('.tbody__brw__list');
	//tbody 초기화
	tbodyBrw.innerHTML = "";
	
	//결과값이 없을때
	if(dataList.length<1) {
		let trNoResult = document.createElement("tr");
		let tdNoResult = document.createElement("td");
		tdNoResult.colSpan = "4";
		tdNoResult.textContent = "대출/반납 자료가 없습니다.";
		trNoResult.appendChild(tdNoResult);
		tbodyBrw.appendChild(trNoResult);
		
		return;
	}
	
	//템플릿 컨텐츠 인스턴스
	let tmplBrw = document.querySelector('#tmpl__brw__tr').content;
	
	for(let i=0; i<dataList.length; i++){
		let tmplBrwClone = tmplBrw.cloneNode(true);
		
		tmplBrwClone.querySelector('.tbl__num').textContent = i+1;
		tmplBrwClone.querySelector('.tbl__title__div').textContent = dataList[i].title;
		tmplBrwClone.querySelector('.tbl__author__div').textContent = dataList[i].writer;
		let brwState = "대출중";
		if(dataList[i].endDateBrw!=null) {
			brwState = "반납됨";
			tmplBrwClone.querySelector('.tbl__state__btn').style.color = "blue";
		}
		tmplBrwClone.querySelector('.tbl__state__btn').textContent = brwState;
		tmplBrwClone.querySelector('.tbl__barcode').textContent = dataList[i].localIdBarcode;
		
		tmplBrwClone.querySelector('.tbl__startDate').textContent = dateFormChange(dataList[i].startDateBrw);
		tmplBrwClone.querySelector('.tbl__endDate').textContent = dateFormChange(dataList[i].endDateBrw);
		
		tmplBrwClone.querySelector('.brw__data').innerHTML = JSON.stringify(dataList[i]);
		tbodyBrw.appendChild(tmplBrwClone);
	}
	
}









//로그아웃
function backToManagerPage() {
	location.href='/user_self_page/log_out_user.do';
}



