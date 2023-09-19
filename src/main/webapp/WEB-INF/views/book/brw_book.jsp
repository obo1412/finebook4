<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!-- 오늘날짜 만들고, 날짜패턴 아래와같이 바꾸기 -->
<jsp:useBean id="currDate" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${currDate}" pattern="yyyy-MM-dd HH:mm:ss" />
<!-- 오늘날짜 관련 끝 -->
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/inc/head.jsp"%>
	<link href="${pageContext.request.contextPath}/assets/css/book/brw/brw_book.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
		
	</style>
</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>
	<div id='wrapper'>
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>

		<div id="content-wrapper">

			<div class="container-fluid">
				<div class="card mb-3 upsideCard">
					<div class="card-header">
						<h6>
							<label for='search-name'>도서 대출하기</label>
						</h6>
					</div>

					<div class="card-body">

							<div class="form-group form-inline">
								<label for='search-name' class="col-md-3 float-right">회원
									검색</label>
								<div class="input-group input-group-sm col-md-9">
									<input type="text" name="search-name" id="search-keyword" onKeyDown="searchMemberEnterKeyDown()"
										class="form-control korean-first" placeholder="회원정보(이름, 전화번호, 회원번호)를 입력해주세요" value="" />
									<span class="input-group-append">
										<button class="btn btn-sm btn-secondary" id="btn-search-mbr"
											onclick="clickedSearchMember()">
											<i class="fas fa-search"></i>
										</button>
									</span>
								</div>
							</div>


						<div class="table-responsive" style="height:90px; max-height:90px;">
							<table class="table table-sm mb-0 table-fixed">
								<thead>
									<tr>
										<th class="table-info text-center">이름</th>
										<!-- <th class="info text-center">회원코드</th> -->
										<th class="table-info text-center">연락처</th>
										<th class="table-info text-center">회원등급</th>
										<th class="table-info text-center">대출권수</th>
										<th class="table-info text-center">대출기한</th>
									</tr>
								</thead>
								<tbody class="searchMemberListClass">
									<tr>
										<td colspan="5" class="text-center"
											style="line-height: 30px;">조회된 회원 정보가 없습니다.</td>
									</tr>
								</tbody>
							</table>
						</div>

						<!-- 회원정보, 도서정보 수집 시작 -->

						<input type="hidden" name="memberId" id="memberId" value="" />

						<div class="card mt-2 mb-2" style="width: 100%;">
              <div class="row no-gutters" style="width:100%; height:100%;">
                <div class="col-4 w-25 rounded" style="background-color:grey; display:flex;">
                  <div class="rounded div__img">
                  		<img src="" id="profileImg" class="card-img" style="margin:auto;">
                  </div>
                </div>
                <div class="col-8" style="height: 100%; font-size: 14px;">
                  <div class="card-body">
                    <table class="table table-sm table-bordered mb-1">
                      <tr>
                        <th scope="row" class="text-center table-secondary"
                        	style="width:90px;">
                        	이름
                        </th>
                        <td colspan="2" id="brwMemberName">-</td>
                      </tr>
                      <tr>
                        <th scope="row" class="text-center table-secondary"
                        	style="width:90px;">
                        	연락처
                        </th>
                        <td colspan="2" id="brwMemberPhone">-</td>
                      </tr>
                      <tr>
                        <th scope="row" class="text-center table-secondary"
                        	style="width:90px;">
                        	회원번호
                        </th>
                        <td colspan="2" id="brwMemberCode">-</td>
                      </tr>
                     </table>
                     <table class="table table-sm table-striped table-bordered mb-1">
                      <tbody class="text-center">
                        <tr>
                          <th>대출권수</th>
                          <th>대출중</th>
                          <th>대출가능</th>
                        </tr>
                        <tr>
                          <td id="brwMemberBrwLimit">0</td>
                          <td id="brwMemberBrwNow">0</td>
                          <td id="brwMemberBrwPsb">0</td>
                        </tr>
                      </tbody>
                    </table>
                    <table class="table table-sm table-bordered mb-0">
                    	<tbody>
                    		<tr>
                    			<td id="memberNotice">
                    				-
                    			</td>
                    			<td style='display:none;'>
                    				<!-- value가 block이면 도서 대출 제한 -->
                    				<input type="hidden" id="blockBrw" value="block" />
                    			</td>
                    		</tr>
                    	</tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div><!-- 회원정보 전체 -->

							<div class="form-group form-inline mb-1">
								<label for='barcodeBook' class="col-3" style="font-size:12px;">도서등록번호</label>
								<div class="input-group input-group-sm col-9">
									<span class="input-group-prepend">
										<input type="button"
										value="검색" class="btn btn-sm btn-secondary fas fa-search english-first"
										onclick="window.open('${pageContext.request.contextPath}/book/book_held_list_popup.do', '_blank', 'width=750,height=700,scrollbars=yes')" />
									</span>
									<input type="text" name="barcodeBook" id="barcodeBook"
										class="form-control" placeholder="or 직접 입력" onKeyDown="brwBookEnterKeyDown()" />
									<div class="input-group-append">
										<button onclick="clickedBrwBook()" class="btn btn-primary">도서대출</button>
										<!-- <button onclick="resetMemberBoard()" class="btn btn-danger">취소</button> -->
									</div>
								</div>
							</div>
							
						<!-- 회원정보, 도서정보 끝 -->
					</div>
					<!-- card body 끝 -->
				</div>
				<!-- card 끝 -->

				<div class="card mb-3 upsideCard clear" style="margin-left:11px;">
					<div class="card-header">
						<h6>
							<label for='barcodeBookRtn'>반납하기</label>
						</h6>
					</div>
					<div class="card-body">
					
						<div class="form-group form-inline">
							<label for='barcodeBookRtn' id="labelRtnOrSearch" class="col-md-3">등록번호 검색</label>
							<div class="input-group input-group-sm col-md-9">
								<span class="input-group-prepend">
									<select class="form-control form-control-sm" id="rtnOrSearch">
										<option>등록번호</option>
										<option>제목검색</option>
									</select>
								</span>
								<input type="text" name="barcodeBookRtn" id="barcodeBookRtn"
									class="form-control" placeholder="도서바코드를 입력해주세요" onKeyDown="rtnBookEnterKeyDown()"
									value="" /> <span class="input-group-append">
									<button class="btn btn-sm btn-secondary" id="btnRtnOrSearch"
										onclick="clickedReturnBook()">
										반납
									</button>
								</span>
							</div>
						</div>
						<!-- 도서 검색폼 -->

						<div class="table-responsive">
							<table class="table table-sm table-fixed">
								<thead>
									<tr>
										<th class="table-info text-center" style="width:25%; vertical-align:middle;">도서명</th>
										<th class="table-info text-center" style="width:20%; vertical-align:middle;">등록번호</th>
										<th class="table-info text-center" style="width:20%; vertical-align:middle;" rowspan="2">대출일<br />반납일</th>
										<th class="table-info text-center" style="vertical-align:middle;">기간연장/상태</th>
									</tr>
								</thead>
								<tbody class="returnRmnListClass" style="font-size:12px;">
									
								</tbody>
							</table>
						</div>
						<!-- 도서 검색시, 그 도서를 빌린 사람의 대여현황을 보여주기 위함. -->
					</div>
					<!-- card body2 끝 -->
				</div>
				<!-- card2 끝 -->

				<div class="card mb-3 bottomCard">
					<div class="card-header">
						<h4 class='pull-left'>
							<span id="banner-pick-day-id">오늘의</span> 대출/반납 현황
						</h4>
					</div>
					<div class="card-body">
						<!-- 조회결과를 출력하기 위한 표 -->
						<div class="table-responsive">
							<table class="table table-sm table-fixed">
								<thead>
									<tr>
										<th class="info text-center" style="width:20px;">번호</th>
										<th class="info text-center" style="width:20px;">상태</th>
										<th class="info text-center" style="width:30px;">이름</th>
										<th class="info text-center" style="width:40px;">연락처</th>
										<th class="info text-center" style="width:30px;">회원등급</th>
										<th class="info text-center" style="width:60px;">도서명</th>
										<th class="info text-center" style="width:34px;">도서등록번호</th>
										<th class="info text-center" style="width:40px;">반납예정일</th>
										<th class="info text-center" style="width:40px;">대출일</th>
										<th class="info text-center" style="width:40px;">반납일</th>
									</tr>
								</thead>
								<tbody class="brwListDdayClass">
									
								</tbody>
							</table>
						</div>

					</div>
					<!-- card3 body 끝 -->
				</div>
				<!-- card3 끝 -->
			</div>
			<!-- container-fluid 끝 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
		<!-- content wrapper 끝 -->
	</div>
	<!-- wrapper 끝 -->

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
	<!-- 한글로 바코드 기입되었을때, 영어로 바꿔주는거
		현재 아드님 하면 드가 EM으로 변경됨. 이거 문제 처리 고민해보고 다시 등록하기 -->
	<%-- <%@ include file="/WEB-INF/inc/korToEng.jsp"%> --%>

	<!-- date 형식 바꿔주는 date 핸들러 js -->
	<script type="text/javascript" src="/assets/js/date_handler.js"></script>
	
	<script type="text/javascript" src="/assets/js/book/borrow/brw_book.js"></script>
	
	<script type="text/javascript">
		window.onload = function() {
			/* 페이지 호출시 회원검색에 포커싱 */
			document.getElementById('search-keyword').focus();
			
			selectBrwListDday();
			
		}
		
		$(function () {
			$('[data-toggle="tooltip"]').tooltip()
		});
		
		//오늘의 대출/반납 현황에서 회원선택을 위한 클릭.
		function clickedStatusOnDdayBoard(memberId) {
			//해당인원을 선택했을때 선택된 회원 타겟팅
			brwPickMember(memberId);
			//해당 회원 대출중인 도서 목록
			selectBrwRmnListByMemberId(memberId);
			
		};
		
		//반납리스트에 상태버튼 클릭시 동작 함수 반납/취소
		function clickedReturnOrCancelThisBook() {
			var parentRow = event.target.parentNode.parentNode;
			var children = parentRow.childNodes;
			var barcodeBookRtn = children[1].innerText;
			var dateRow = children[2].childNodes;
			var endDateBrw = dateRow[2].innerText;
			if(endDateBrw==null||endDateBrw==''||endDateBrw=='대출중'){
				//반납시간이 비어있으면, 반납 처리
				clickedReturnBook(barcodeBookRtn);
			} else {
				//반납시간이 비어있지 않으면, 취소 처리
				clickedReturnCancelBook(barcodeBookRtn);
			}
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
						
						document.getElementById('barcodeBookRtn').focus();
					}
				}
			});
		}
		
		// 기간 연장기능 버튼
		function clickedExtendDateBook(barcodeBook) {
			var parentRow = event.target.parentNode.parentNode;
			var children = parentRow.childNodes;
			var barcodeBook = children[1].innerText;
			var extendDay = event.target.previousElementSibling.value;
			
			extendDayBookDueDate(barcodeBook, extendDay);
		}
		
		// 기간 연장 기능 동작 함수
		// ReturnBookOk.java 에서 사용 
		function extendDayBookDueDate(barcodeBook, extendDay) {
			$.ajax({
				url: "${pageContext.request.contextPath}/book/extend_book_due_date.do",
				type:'POST',
				data: {
					barcodeBook,
					extendDay
				},
				success: function(data) {
					if(data.rt != 'OK') {
						alert(data.rt);
					} else {
						new Promise((resolve)=> {
							//도서 반납 처리 후, 오늘의 대출/반납 현황 업데이트
							selectBrwListDday();
							resolve();
						}).then(() => {
							//도서 반납 처리 후, 해당 회원의 남아있는 대출 도서 조회
							/* selectBrwRmnListByMemberId(data.memberId); */
						}).then(() => {
							//도서 반납 처리 후, 해당 회원 정보 업데이트
							brwPickMember(data.memberId);
						});
						
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
								var inputExtend = document.createElement('input');
								inputExtend.style.width = '34px';
								inputExtend.value = 3;
								var btnExtend = document.createElement('button');
								btnExtend.classList.add('btn');
								btnExtend.classList.add('btn-sm');
								btnExtend.classList.add('btn-info');
								btnExtend.classList.add('mr-1');
								btnExtend.innerHTML = '기간연장';
								btnExtend.setAttribute('onclick', 'clickedExtendDateBook()');
								//반복할 필요 없는 변수 처리 끝
								row = tbody.insertRow();
								for(var j=0; j<4; j++){
									cell = row.insertCell(j);
									cell.classList.add('text-center');
								}
								var children = row.childNodes;
								children[0].setAttribute('data-toggle','tooltip');
								children[0].setAttribute('data-placement','top');
								children[0].setAttribute('title',brwRmnList[i].title);
								children[0].innerText = brwRmnList[i].title;
								children[1].innerText = brwRmnList[i].localIdBarcode;
								let endDateView = dateFormChange(brwRmnList[i].endDateBrw);
								if(endDateView === null || endDateView === '') {
									endDateView = '대출중';
								}
								children[2].innerHTML = 
									'<span>'+dateFormChange(brwRmnList[i].startDateBrw)+'</span>'
									+'<br />'
									+ '<span>'+endDateView+"</span>";
								// 기간연장 / 상태버튼 하나로 통합
								children[3].appendChild(inputExtend);
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
								children[3].appendChild(btnExtend)
								children[3].appendChild(btn);
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
								children[1].setAttribute('onclick', "clickedStatusOnDdayBoard("+brwListDday[i].idMemberBrw+")");
								children[2].innerText = brwListDday[i].name;
								children[2].setAttribute('onclick', "popUpMemberView("+brwListDday[i].idMemberBrw+")");
								children[3].innerText = brwListDday[i].phone;
								children[4].innerText = brwListDday[i].gradeName;
								children[5].innerText = brwListDday[i].title;
								children[6].innerText = brwListDday[i].localIdBarcode;
								children[6].setAttribute('onclick',"popUpBookHeldView("+brwListDday[i].bookHeldId+")");
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
		
		//회원 상세 정보 보기
		function popUpMemberView(memberId) {
			var url = "${pageContext.request.contextPath}/member/member_view.do?memberId="+memberId;
			window.open(url, '_blank', 'width=400,height=650,scrollbars=yes');
			return false;
		}
		
		//도서 상세 정보 보기
		function popUpBookHeldView(bookHeldId) {
			var url = "${pageContext.request.contextPath}/book/book_held_view.do?bookHeldId="+bookHeldId;
			window.open(url, '_blank', 'width=550,height=800,scrollbars=yes');
			
		}
		
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
			
			if(searchKeywordValue==null||searchKeywordValue==''){
				alert('검색 값을 입력하세요');
				return;
			}
			
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
									var profileImgDir = '/filesMapping/'+memberList[0].profileImg.substring(memberList[0].profileImg.lastIndexOf("upload"));
									document.getElementById('profileImg').src = profileImgDir;
								} else {
									document.getElementById('profileImg').src = '/filesMapping/upload/finebook4/no-image.png';
								}
								
								document.getElementById('memberId').value = memberList[0].id;
								document.getElementById('brwMemberName').innerText = memberList[0].name;
								document.getElementById('brwMemberPhone').innerText = memberList[0].phone;
								document.getElementById('brwMemberCode').innerText = memberList[0].barcodeMbr;
								document.getElementById('brwMemberBrwLimit').innerText = memberList[0].brwLimit;
								document.getElementById('brwMemberBrwNow').innerText = memberList[0].brwNow;
								var brwPsb = memberList[0].brwLimit - memberList[0].brwNow;
								document.getElementById('brwMemberBrwPsb').innerText = brwPsb;
								
								
								new Promise((resolve)=> {
									//도서 반납 처리 후, 오늘의 대출/반납 현황 업데이트
									/* selectBrwListDday(); */
									resolve();
								}).then(() => {
									//도서 반납 처리 후, 해당 회원의 남아있는 대출 도서 조회
									selectBrwRmnListByMemberId(memberId);
								}).then(() => {
									//도서 반납 처리 후, 해당 회원 정보 업데이트
									brwPickMember(memberId);
								});
								
								/* brwPickMember(memberId);
								//해당 회원의 남아있는 빌린도서.
								selectBrwRmnListByMemberId(memberId); */
								
								//회원이 로딩되면 도서등록번호에 포커싱
								document.getElementById('barcodeBook').focus();
							}
						} else {
							//memberList.length < 0 일 경우
							//초기화
							tbody.innerHTML = "";
							var row = tbody.insertRow();
							var cell = row.insertCell();
							cell.innerHTML = "해당 입력값의 회원정보가 없습니다.";
							cell.colSpan = 5;
						}
					} else {
						//data.memberList == null 일경우
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
				if(children[8].innerText != 'null') {
					var profileImgDir = '/filesMapping/'+children[8].innerText.substring(children[8].innerText.lastIndexOf("upload"));
					document.getElementById('profileImg').src = profileImgDir;
				} else {
					document.getElementById('profileImg').src = '/filesMapping/upload/finebook4/no-image.png';
				}
				
				document.getElementById('memberId').value = memberId;
				document.getElementById('brwMemberName').innerText = children[0].innerText;
				document.getElementById('brwMemberPhone').innerText = children[1].innerText;
				document.getElementById('brwMemberCode').innerText = children[6].innerText
				document.getElementById('brwMemberBrwLimit').innerText = children[3].innerText;
				document.getElementById('brwMemberBrwNow').innerText = children[7].innerText
				var brwPsb = children[3].innerText - children[7].innerText;
				document.getElementById('brwMemberBrwPsb').innerText = brwPsb;
				
				new Promise((resolve)=> {
					//도서 반납 처리 후, 오늘의 대출/반납 현황 업데이트
					/* selectBrwListDday(); */
					resolve();
				}).then(() => {
					//도서 반납 처리 후, 해당 회원의 남아있는 대출 도서 조회
					selectBrwRmnListByMemberId(memberId);
				}).then(() => {
					//도서 반납 처리 후, 해당 회원 정보 업데이트
					brwPickMember(memberId);
				});
				
				/* brwPickMember(memberId);
				//회원이 로딩되면 도서등록번호에 포커싱
				selectBrwRmnListByMemberId(memberId); */
				
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
					if(data.rt != 'OK'){
						alert(data.rt);
					} else {
						var memberItem = data.memberItem;
						if(memberItem.profileImg != null && memberItem.profileImg != ''){
							var profileImgDir = '/filesMapping/'+memberItem.profileImg.substring(memberItem.profileImg.lastIndexOf("upload"));
							document.getElementById('profileImg').src = profileImgDir;
						} else {
							document.getElementById('profileImg').src = '/filesMapping/upload/finebook4/no-image.png';
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
			//바코드 번호 한글 -> 영어로 바꿔주는거. 아직 사용 안함.
			//barcodeVal = convertKorToEng(barcodeVal);
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
							//바코드 입력창 초기화
							document.getElementById('barcodeBook').value = null;
							alert(data.rt);
						} else {
							document.getElementById('barcodeBook').value = null;
							
							new Promise((resolve)=> {
								//도서 반납 처리 후, 오늘의 대출/반납 현황 업데이트
								selectBrwListDday();
								resolve();
							}).then(() => {
								//도서 반납 처리 후, 해당 회원의 남아있는 대출 도서 조회
								selectBrwRmnListByMemberId(memberId);
							}).then(() => {
								//도서 반납 처리 후, 해당 회원 정보 업데이트
								brwPickMember(memberId);
							});
							
							
							/* //도서 대출 처리 후, 회원정보테이블 업데이트
							brwPickMember(memberId);
							//도서 대출 처리 후, 오늘 대출/반납 현황 업데이트
							selectBrwListDday();
							//도서 대출 처리 후, 해당 회원의 대출 도서 목록 조회
							selectBrwRmnListByMemberId(memberId); */
						}
					}
				});
			}
		};
		
	</script>
</body>
</html>