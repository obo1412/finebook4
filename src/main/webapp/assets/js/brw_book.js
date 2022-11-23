
window.onload = function() {
	/* 페이지 호출시 회원검색에 포커싱 */
	document.getElementById('search-keyword').focus();
	
	selectBrwListDday();
	
};

$(function () {
	$('[data-toggle="tooltip"]').tooltip()
});

//반납리스트에 상태버튼 클릭시 동작 함수 반납/취소
function clickedReturnOrCancelThisBook() {
	var parentRow = event.target.parentNode.parentNode;
	var children = parentRow.childNodes;
	var barcodeBookRtn = children[1].innerText;
	var endDateBrw = children[3].innerText;
	if(endDateBrw==null||endDateBrw==''){
		//반납시간이 비어있으면, 반납 처리
		clickedReturnBook(barcodeBookRtn);
	} else {
		//반납시간이 비어있지 않으면, 취소 처리
		clickedReturnCancelBook(barcodeBookRtn);
	}
}

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
		barcodeBookRtnVal = convertKorToEng(barcodeBookRtnVal);
		document.getElementById('barcodeBookRtn').value = barcodeBookRtnVal;
		//컨트롤러쪽과도 변수명 통일을 위해서 생성.
		barcodeBookRtn = barcodeBookRtnVal;
	}
	//매개변수가 존재하지 않으면 위 if 조건 거친 후 반납 바코드 주입
	//매개변수가 존재하면 그냥 바로 아래 ajax 실행
	$.ajax({
		url: "${pageContext.request.contextPath}/book/return_book_ok.do",
		type:'POST',
		data: {
			barcodeBookRtn
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				document.getElementById('barcodeBookRtn').value = null;
				//도서 반납 처리 후, 오늘의 대출/반납 현황 업데이트
				selectBrwListDday();
				//도서 반납 처리 후, 해당 회원의 남아있는 대출 도서 조회
				selectBrwRmnListByMemberId(data.memberId);
				//도서 반납 처리 후, 해당 회원 정보 업데이트
				brwPickMember(data.memberId);
				
				document.getElementById('barcodeBookRtn').focus();
			}
		}
	});
}

//반납 취소 버튼 클릭시 작동 함수
function clickedReturnCancelBook(barcodeBookRtn) {
	$.ajax({
		url: "${pageContext.request.contextPath}/book/return_book_cancel.do",
		type:'POST',
		data: {
			barcodeBookRtn
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				document.getElementById('barcodeBookRtn').value = null;
				//도서 반납 처리 후, 오늘의 대출/반납 현황 업데이트
				selectBrwListDday();
				//도서 반납 처리 후, 해당 회원의 남아있는 대출 도서 조회
				selectBrwRmnListByMemberId(data.memberId);
				//도서 반납 처리 후, 해당 회원 정보 업데이트
				brwPickMember(data.memberId);
				
				document.getElementById('barcodeBookRtn').focus();
			}
		}
	});
}

//해당 회원의 남아있는 도서 대출 목록 조회
function selectBrwRmnListByMemberId(memberId) {
	$.ajax({
		url: "${pageContext.request.contextPath}/book/brw_remain_list_member.do",
		type:'GET',
		data: {
			memberId
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				//다른 회원을 선택했을 경우 비워야함.
				var tbody = document.querySelector('.returnRmnListClass');
				var row = null;
				tbody.innerHTML = '';
				var brwRmnList = data.brwRmnList;
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
}

//반납 날짜 비교를 위한 오늘 날짜
//달력에 today 변수가 이미 존재함,
//today변수는 달력의 날짜를 누르면 그 값이 그 누른 날짜로 바뀜.
var todayCompare = new Date();
//날짜 포멧 변경(yyyy-MM-dd)을 위한 함수
function dateFormChange(d) {
	var result = null;
	if(d != null && d != '') {
		var date = new Date(d);
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		month = month > 9 ? month : '0'+month;
		var day = date.getDate();
		day = day > 9 ? day : '0'+day;
		result = year+'-'+month+'-'+day;
	}
	return result;
};

//해당 일의 대출/반납 목록
function selectBrwListDday(pickDate) {
	$.ajax({
		url: "${pageContext.request.contextPath}/book/brw_list_D_day.do",
		type:'GET',
		data: {
			pickDate
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				console.log(data.brwListDday);
				var brwListDday = data.brwListDday;
				var tbody = document.querySelector('.brwListDdayClass');
				tbody.innerHTML = "";
				if(brwListDday.length>0) {
					for(var i=0; i<brwListDday.length; i++){
						var row = tbody.insertRow();
						for(var j=0; j<10; j++){
							cell = row.insertCell(j);
							cell.classList.add('text-center');
						}
						var children = row.childNodes;
						var dueDate = new Date(brwListDday[i].dueDateBrw);
						children[0].innerText = i+1;
						if(brwListDday[i].endDateBrw == null || brwListDday[i].endDateBrw == ''){
							if(dueDate < todayCompare) {
								children[1].innerHTML = "<button class='btn btn-sm btn-danger' style='font-size:12px;'>연체";
							} else {
								children[1].innerHTML = "<button class='btn btn-sm btn-warning' style='font-size:12px;'>대출";
							}
						} else {
							children[1].innerHTML = "<button class='btn btn-sm btn-primary' style='font-size:12px;'>반납";
						}
						children[2].innerText = brwListDday[i].name;
						children[3].innerText = brwListDday[i].phone;
						children[4].innerText = brwListDday[i].gradeName;
						children[5].innerText = brwListDday[i].title;
						children[6].innerText = brwListDday[i].localIdBarcode;
						if(dueDate < todayCompare) {
							children[7].style.color = 'red';
						}
						children[7].innerText = dateFormChange(brwListDday[i].dueDateBrw);
						children[8].innerText = dateFormChange(brwListDday[i].startDateBrw);
						children[9].innerText = dateFormChange(brwListDday[i].endDateBrw);
					}
				}
			}
		}
	});
};

//회원 목록 검색 엔터
function searchMemberEnterKeyDown() {
	if(event.keyCode==13){
		clickedSearchMember();
	}
};
//회원 목록 검색
function clickedSearchMember() {
	const searchKeywordValue = document.getElementById('search-keyword').value;
	console.log(searchKeywordValue);
	
	$.ajax({
		url: "${pageContext.request.contextPath}/book/brw_search_member.do",
		type:'POST',
		data: {
			searchKeywordValue
		},
		success: function(data) {
			if(data.memberList != null){
				var memberList = data.memberList;
				var tbody = document.querySelector('.searchMemberListClass');
				var memberNotice = document.getElementById('memberNotice');
				if(memberList.length>0) {
					tbody.innerHTML = "";
					for(var i=0; i<memberList.length; i++){
						tbody.innerHTML +=
							"<tr class='srchedMember'>"
								+ "<td class='text-center'><button class='pick-member btn btn-sm btn-secondary' style='font-size:12px'>" + memberList[i].name
								+ "<td class='text-center' data-toggle='tooltip' data-placement='top' title='"+memberList[i].phone+"'>" + memberList[i].phone
								+ "<td class='text-center'>" + memberList[i].gradeName
								+ "<td class='text-center'>" + memberList[i].brwLimit
								+ "<td class='text-center'>" + memberList[i].dateLimit
								+ "<td style='display:none;'>" + memberList[i].id
								+ "<td style='display:none;'>" + memberList[i].barcodeMbr
								+ "<td style='display:none;'>" + memberList[i].brwNow
								+ "<td style='display:none;'>" + memberList[i].profileImg
							+"</tr>";
					}
					
					if(memberList.length==1){
						var memberId = memberList[0].id;
						if(memberList[0].profileImg != null) {
							var profileImgDir = '/files/'+memberList[0].profileImg.substring(memberList[0].profileImg.lastIndexOf("upload"));
							document.getElementById('profileImg').src = profileImgDir;
						} else {
							document.getElementById('profileImg').src = '/files/upload/finebook4/no-image.png';
						}
						
						document.getElementById('memberId').value = memberList[0].id;
						document.getElementById('brwMemberName').innerText = memberList[0].name;
						document.getElementById('brwMemberPhone').innerText = memberList[0].phone;
						document.getElementById('brwMemberCode').innerText = memberList[0].barcodeMbr;
						document.getElementById('brwMemberBrwLimit').innerText = memberList[0].brwLimit;
						document.getElementById('brwMemberBrwNow').innerText = memberList[0].brwNow;
						var brwPsb = memberList[0].brwLimit - memberList[0].brwNow;
						document.getElementById('brwMemberBrwPsb').innerText = brwPsb;
						
						
						brwPickMember(memberId);
						//회원이 로딩되면 도서등록번호에 포커싱
						document.getElementById('barcodeBook').focus();
					}
				} else {
					tbody.innerHTML = "";
				}
			} else {
				
			}
		}
	});
};

//목록 중 회원 선택
const pickMember = document.querySelector('.searchMemberListClass');
pickMember.addEventListener('click', (e) => {
	if(e.target.classList.contains('pick-member')){
		var parentRow = e.target.parentNode.parentNode;
		var children = parentRow.childNodes;
		var memberId = children[5].innerText;
		if(children[8].innerText != 'null'){
			var profileImgDir = '/files/'+children[8].innerText.substring(children[8].innerText.lastIndexOf("upload"));
			document.getElementById('profileImg').src = profileImgDir;
		} else {
			document.getElementById('profileImg').src = '/files/upload/finebook4/no-image.png';
		}
		
		document.getElementById('memberId').value = memberId;
		document.getElementById('brwMemberName').innerText = children[0].innerText;
		document.getElementById('brwMemberPhone').innerText = children[1].innerText;
		document.getElementById('brwMemberCode').innerText = children[6].innerText
		document.getElementById('brwMemberBrwLimit').innerText = children[3].innerText;
		document.getElementById('brwMemberBrwNow').innerText = children[7].innerText
		var brwPsb = children[3].innerText - children[7].innerText;
		document.getElementById('brwMemberBrwPsb').innerText = brwPsb;
		
		brwPickMember(memberId);
		//회원이 로딩되면 도서등록번호에 포커싱
		selectBrwRmnListByMemberId(memberId);
		document.getElementById('barcodeBook').focus();
	}
});

//회원선택시 선택된 회원 정보 보이기
function brwPickMember(memberId) {
	$.ajax({
		url: "${pageContext.request.contextPath}/book/brw_pick_member.do",
		type:'POST',
		data: {
			memberId
		},
		success: function(data) {
			var memberItem = data.memberItem;
			if(memberItem.profileImg != null && memberItem.profileImg != ''){
				var profileImgDir = '/files/'+memberItem.profileImg.substring(memberItem.profileImg.lastIndexOf("upload"));
				document.getElementById('profileImg').src = profileImgDir;
			} else {
				document.getElementById('profileImg').src = '/files/upload/finebook4/no-image.png';
			}
			document.getElementById('memberId').value = memberId;
			document.getElementById('brwMemberName').innerText = memberItem.name;
			document.getElementById('brwMemberPhone').innerText = memberItem.phone;
			document.getElementById('brwMemberCode').innerText = memberItem.barcodeMbr;
			document.getElementById('brwMemberBrwLimit').innerText = memberItem.brwLimit;
			var brwLimit = document.getElementById('brwMemberBrwLimit').innerText;
			var brwNow = data.brwNow;
			document.getElementById('brwMemberBrwNow').innerText = brwNow;
			var brwPsb = brwLimit - brwNow;
			document.getElementById('brwMemberBrwPsb').innerText = brwPsb;
			
			/* var brwPsb = document.getElementById('brwMemberBrwPsb').innerText; */
			var memberNotice = document.getElementById('memberNotice');
			var blockBrw = document.getElementById('blockBrw');
			blockBrw.value = 'block';
			memberNotice.innerText = '';
			memberNotice.style.color = 'red';
			if(data.overDueCount > 0 && data.restrictDate != null) {
				memberNotice.innerText = data.overDueCount+'권 연체중/대출제한일:'+dateFormChange(data.restrictDate);
			} else if(data.restrictDate != null) {
				memberNotice.innerText = '대출제한일:'+dateFormChange(data.restrictDate);
			} else if(data.overDueCount > 0) {
				memberNotice.innerText = data.overDueCount+'권 연체중';
			} else if(brwPsb<=0) {
				memberNotice.innerText = '더이상 대출할 수 없습니다.';
			} else {
				memberNotice.style.color = 'black';
				memberNotice.innerText = '대출가능한 회원입니다.';
				blockBrw.value = '';
			}
		}
	});
};

//대출 제한 회원 해제
const memberNotice = document.getElementById('memberNotice');
memberNotice.addEventListener("dblclick", (e)=> {
	e.target.style.color ='black';
	var blockBrw = document.getElementById('blockBrw');
	if(blockBrw.value == 'block') {
		blockBrw.value = '';
	}
});

//도서 대출 엔터키
function brwBookEnterKeyDown() {
	if(event.keyCode==13){
		clickedBrwBook();
	}
};
//도서 대출 처리
function clickedBrwBook() {
	var memberId = document.getElementById('memberId').value;
	var barcodeVal = document.getElementById('barcodeBook').value;
	barcodeVal = convertKorToEng(barcodeVal);
	document.getElementById('barcodeBook').value = barcodeVal;
	//컨트롤러쪽과도 변수명 통일을 위해서 생성.
	var barcodeBook = barcodeVal;
	
	var blockBrw = document.getElementById('blockBrw').value;
	if(blockBrw == 'block') {
		alert('대출이 제한된 회원입니다.');
	} else {
		$.ajax({
			url: "${pageContext.request.contextPath}/book/brw_book_ok.do",
			type:'POST',
			data: {
				memberId,
				barcodeBook
			},
			success: function(data) {
				if(data.rt != 'OK') {
					alert(data.rt);
				} else {
					document.getElementById('barcodeBook').value = null;
					//도서 대출 처리 후, 회원정보테이블 업데이트
					brwPickMember(memberId);
					//도서 대출 처리 후, 오늘 대출/반납 현황 업데이트
					selectBrwListDday();
					//도서 대출 처리 후, 해당 회원의 대출 도서 목록 조회
					selectBrwRmnListByMemberId(memberId);
				}
			}
		});
	}
};