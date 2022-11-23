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
	<style type="text/css">
		
		@media ( min-width : 768px) {
			.upsideCard {
				max-width: 500px;
				float: left;
				width: 49%;
				margin-right: 0.2em;
				min-height: 520px;
			}
			.bottomCard {
				float: left;
				width: 100%;
				/* 페이지가 모니터 최대치일 경우, 아래칸이 위로 올라옴 */
				clear: left;
				min-height: 300px;
			}
			.table-sm {
				font-size: 12px;
			}
			label {
				margin-bottom: 0;
				font-size: 14px;
			}
			.btn-sm {
				font-size: 10px;
			}
		}
		
		table {
			table-layout: auto;
		}
		
		.table-fixed { 
			table-layout: fixed;
		}
		
		tr > td {
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
		}
	</style>
</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar_member.jsp"%>
	<div id='wrapper'>
		<%@ include file="/WEB-INF/inc/sidebar_left_member.jsp"%>

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
                  <div class="rounded" style="width:90%; height:80%; margin:auto; background-color: white; display:flex;">
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

				<div class="card mb-3 upsideCard clear">
					<div class="card-header">
						<h6>
							<label for='barcodeBookRtn'>반납하기</label>
						</h6>
					</div>
					<div class="card-body">
						<div class="form-group form-inline">
							<label for='barcodeBookRtn' class="col-md-3">도서 검색</label>
							<div class="input-group input-group-sm col-md-9">
								<input type="text" name="barcodeBookRtn" id="barcodeBookRtn"
									class="form-control" placeholder="도서바코드를 입력해주세요" onKeyDown="rtnBookEnterKeyDown()"
									value="" /> <span class="input-group-append">
									<button class="btn btn-sm btn-secondary" id="btn-search-mbr"
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
										<th class="table-info text-center" style="width:25%;">도서명</th>
										<th class="table-info text-center" style="width:20%;">등록번호</th>
										<th class="table-info text-center" style="width:20%;">대출일</th>
										<th class="table-info text-center" style="width:20%;">반납일</th>
										<th class="table-info text-center" style="width:15%;">상태</th>
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
	<%@ include file="/WEB-INF/inc/korToEng.jsp"%>

	<script type="text/javascript">
		window.onload = function() {
			/* 페이지 호출시 회원검색에 포커싱 */
			document.getElementById('search-keyword').focus();
			//오늘 대출/반납 목록
			selectBrwListDday();
			
			//회원별로 자신의 아이디로 로그인하여 처리하도록 할경우,
			//본인의 아이디로만 처리를 해야하니까 이렇게 처리
			/* var memberId = document.getElementById('memberId').value;
			console.log(memberId);
			brwPickMember(memberId);
			selectBrwRmnListByMemberId(memberId) */
			
		};
		
		$(function () {
			$('[data-toggle="tooltip"]').tooltip()
		});
		
		// 회원로그인 상태에서는 상태 눌렀을 경우 다른 회원이 선택되는 기능을 사용하지
		// 않기 때문에 주석처리.
		/*  //오늘의 대출/반납 현황에서 회원선택을 위한 클릭.
		function clickedStatusOnDdayBoard(memberId) {
			//해당인원을 선택했을때 선택된 회원 타겟팅
			brwPickMember(memberId);
			//해당 회원 대출중인 도서 목록
			selectBrwRmnListByMemberId(memberId);
			
		};  */
		
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
					if(data.rt != 'OK'){
						alert(data.rt);
					} else {
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
		
	</script>
</body>
</html>