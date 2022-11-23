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
	<link href="/assets/css/book_check/book_check_list.css" rel="stylesheet" >
	<style type="text/css">
		
	</style>
</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>
	<div id='wrapper'>
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">
			
				<div class="card">
					
					<div class="card-header">
						<h6 style="float:left;">
							전체 도서
						</h6>
						<button class="btn btn-info btn-sm" style="float:right;" onclick="clickedBookHeldList()">도서불러오기</button>
					</div>

					<div class="card-body" style="height:180px; overflow:auto;">
						<table class="table table-sm">
							<thead>
								<tr>
									<th class="table-info text-center" style="width:80px;">번호</th>
									<th class="table-info text-center" style="width:120px;">점검용번호</th>
									<th class="table-info text-center" style="width:100px;">바코드번호</th>
									<th class="table-info text-center">도서명</th>
									<th class="table-info text-center" style="width:260px;">저자명</th>
									<th class="table-info text-center" style="width:150px;">출판사명</th>
									<th class="table-info text-center" style="width:60px;">복본기호</th>
								</tr>
							</thead>
							<tbody class="wholetbodyBookList">
								
							</tbody>
						</table>
					</div>
					<!-- card body 전체 도서 끝 -->
				</div>
				<!-- card 전체 도서 끝 -->
			
			
				<div class="card" style="margin-top: 10px;">
					
					<div class="card-header">
						<h6 style="float:left;">
							확인 도서
						</h6>
						<div class="input-group input-group-sm" style="float:left; width:200px; margin-left:20px;">
							<input type="text" name="input_barcode" id="input_barcode"
								class="form-control input-group-prepend" onKeyDown="enterKeyDownCheckBook()" />
							<button class="btn btn-warning btn-sm input-group-append" onclick="clickedCheckBook()" >점검</button>
						</div>
						<div class="ml-1" style="float:left;">
							<button class="btn btn-primary btn-sm" onclick="openFileBatch()">일괄점검</button>
							<progress class="ml-2" id="batchProgress" value="0" max="100"></progress>
						</div>
						<div class="float-left ml-2" >
							<button class="btn btn-secondary btn-sm" onclick="makeBookCheckExcelFile()">엑셀생성</button>
							<!-- <button class="btn btn-secondary btn-sm" onclick="makeBookCheckExcelFile()">엑셀생성</button> -->
						</div>
						<div class="location_chkBox_soundEff" style="float:right;">
							
						</div>
						<div style="float:right; margin-right:5px;">
							<c:set var="pick__checker1" value="" />
							<c:if test="${checker eq 1}">
								<c:set var="pick__checker1" value="selected" />
							</c:if>
							<c:set var="pick__checker2" value="" />
							<c:if test="${checker eq 2}">
								<c:set var="pick__checker2" value="selected" />
							</c:if>
							<c:set var="pick__checker3" value="" />
							<c:if test="${checker eq 3}">
								<c:set var="pick__checker3" value="selected" />
							</c:if>
							<c:set var="pick__checker4" value="" />
							<c:if test="${checker eq 4}">
								<c:set var="pick__checker4" value="selected" />
							</c:if>
							<c:set var="pick__checker5" value="" />
							<c:if test="${checker eq 5}">
								<c:set var="pick__checker5" value="selected" />
							</c:if>
							<select id="select-check-ip" style="width:100px;">
								<option value="">-- 점검자를 선택해주세요 --</option>
								<option value="1" ${pick__checker1}>첫번째</option>
								<option value="2" ${pick__checker2}>두번째</option>
								<option value="3" ${pick__checker3}>세번째</option>
								<option value="4" ${pick__checker4}>네번째</option>
								<option value="5" ${pick__checker5}>다섯번째</option>
							</select>
						</div>
					</div>

					<div class="card-body" style="height:460px;">
						<ul class="nav nav-tabs" role="tablist">
							<li>
								<a class="nav-link active" id="check-list-tab"
									data-toggle="tab" href="#checkList" role="tab">
									점검목록 <span class="num__circle num__circle__whole">0</span>
								</a>
							</li>
							<li>
								<a class="nav-link" id="check-status-tab"
									data-toggle="tab" href="#checkStatus" role="tab" onclick="currentBookCheckStatus()">
									점검현황
								</a>
							</li>
							<li>
								<a class="nav-link" id="checked-book-tab"
									data-toggle="tab" href="#normalBook" role="tab" onclick="getOverallBookList('tbodyNormalBook')">
									정상도서 <span class="num__circle num__circle__normal">0</span>
								</a>
							</li>
							<li>
								<a class="nav-link" id="double-checked-book-tab"
									data-toggle="tab" href="#doubleCheckedBook" role="tab" onclick="getOverallBookList('tbodyDoubleCheckedBook')">
									중복도서 <span class="num__circle num__circle__doubleChecked">0</span>
								</a>
							</li>
							<li>
								<a class="nav-link" id="rented-book-tab"
									data-toggle="tab" href="#rentedBook" role="tab" onclick="getOverallBookList('tbodyRentedBook')">
									발견된대출중도서 <span class="num__circle num__circle__rented">0</span>
								</a>
							</li>
							<li>
								<a class="nav-link" id="unregistered-book-tab"
									data-toggle="tab" href="#unregisteredBook" role="tab" onclick="getOverallBookList('tbodyUnregisteredBook')">
									미등록도서 <span class="num__circle num__circle__unregistered">0</span>
								</a>
							</li>
							<li>
								<a class="nav-link" id="unchecked-book-tab"
									data-toggle="tab" href="#uncheckedBook" role="tab" onclick="getOverallBookList('tbodyUncheckedBook')">
									미점검도서 <span class="num__circle num__circle__unchecked">0</span>
								</a>
							</li>
							<li>
								<a class="nav-link" id="whole-brwed-book-tab"
									data-toggle="tab" href="#wholeBrwedBook" role="tab" onclick="getOverallBookList('tbodyWholeBrwedBook')">
									전체대출중도서<span class="num__circle num__circle__wholeBrwed">0</span>
								</a>
							</li>
						</ul>
						
						<div class="tab-content" id="bookCheckContent">
							<!-- check list 탭 -->
							<div class="tab-pane fade show active check__tab__char" id="checkList" role="tabpanel">
								<table class="table table-sm table-bordered">
									<thead>
										<tr>
											<th class="table-info text-center" style="width:50px;">번호</th>
											<th class="table-info text-center" style="width:120px;">점검결과</th>
											<th class="table-info text-center" style="width:100px;">점검용번호</th>
											<th class="table-info text-center" style="width:100px;">바코드번호</th>
											<th class="table-info text-center" style="width:80px;">서가</th>
											<th class="table-info text-center" style="width:380px;">도서명</th>
											<th class="table-info text-center" style="width:300px;">저자명</th>
											<th class="table-info text-center" style="width:100px;">출판사명</th>
											<th class="table-info text-center" style="width:80px;">점검자</th>
										</tr>
									</thead>
									<tbody id="tbodyCheckedBookList">
										
									</tbody>
								</table>
							</div>
							<!-- check list 탭 끝 -->
							
							<!-- 전체 점검 현황 탭 -->
							<div class="tab-pane fade check__tab__char" id="checkStatus">
								<table class="board__wrap table-bordered">
                  <tbody>
                    <tr>
                      <th class="table-info text-right">전체도서수 :</th>
                      <td class="text-center td__width"></td>
                      <td class="text-center td__width"><span id="boardWholeCount"></span></td>
                    </tr>
                    <tr>
                      <th class="table-info text-right">점검횟수 :</th>
                      <td class="text-center num__circle__whole"><span id="boardCheckedCount"></span></td>
                      <td class="text-center td__width"></td>
                    </tr>
                    <tr>
                      <th class="table-info text-right">정상 확인도서 :</th>
                      <td class="text-center td__width"></td>
                      <td class="text-center num__circle__normal"><span id="boardConfirmCount"></span></td>
                    </tr>
                    <tr>
                      <th class="table-info text-right">미확인 도서 :</th>
                      <td class="text-center td__width"></td>
                      <td class="text-center"><span id="boardUncheckedCount"></span></td>
                    </tr>
                    <tr>
                      <th class="table-info text-right">발견된대출중/전체대출중 :</th>
                      <td class="text-center num__circle__rented"><span id="boardRentedCount"></span></td>
                      <td class="text-center num__circle__wholeBrwed"><span id="boardBrwedCount"></span></td>
                    </tr>
                    <tr>
                      <th class="table-info text-right">발견된 중복바코드 :</th>
                      <td class="text-center num__circle__doubleChecked"><span id="boardRedupCount"></span></td>
                      <td class="text-center td__width"></td>
                    </tr>
                    <tr>
                      <th class="table-info text-right">발견된 미등록도서 :</th>
                      <td class="text-center num__circle__unregistered"><span id="boardUnregCount"></span></td>
                      <td class="text-center td__width"></td>
                    </tr>
                  </tbody>
								</table>
							</div>
							<!-- 전체 점검 현황 탭 끝 -->
							
							<!-- 정상점검도서 탭 -->
							<div class="tab-pane fade check__tab__char" id="normalBook">
								<table class="table table-sm table-bordered">
									<thead>
										<tr>
											<th class="table-info text-center" style="width:50px;">번호</th>
											<th class="table-info text-center" style="width:120px;">점검결과</th>
											<th class="table-info text-center" style="width:100px;">점검용번호</th>
											<th class="table-info text-center" style="width:100px;">바코드번호</th>
											<th class="table-info text-center" style="width:80px;">서가</th>
											<th class="table-info text-center" style="width:380px;">도서명</th>
											<th class="table-info text-center" style="width:300px;">저자명</th>
											<th class="table-info text-center" style="width:100px;">출판사명</th>
											<th class="table-info text-center" style="width:80px;">점검자</th>
										</tr>
									</thead>
									<tbody id="tbodyNormalBook">
										
									</tbody>
								</table>
							</div>
							<!-- 정상점검도서 탭 끝 -->
							
							<!-- 중복도서 탭 -->
							<div class="tab-pane fade check__tab__char" id="doubleCheckedBook">
								<table class="table table-sm table-bordered">
									<thead>
										<tr>
											<th class="table-info text-center" style="width:50px;">번호</th>
											<th class="table-info text-center" style="width:120px;">점검결과</th>
											<th class="table-info text-center" style="width:100px;">점검용번호</th>
											<th class="table-info text-center" style="width:100px;">바코드번호</th>
											<th class="table-info text-center" style="width:80px;">서가</th>
											<th class="table-info text-center" style="width:380px;">도서명</th>
											<th class="table-info text-center" style="width:300px;">저자명</th>
											<th class="table-info text-center" style="width:100px;">출판사명</th>
											<th class="table-info text-center" style="width:80px;">점검자</th>
										</tr>
									</thead>
									<tbody id="tbodyDoubleCheckedBook">
										
									</tbody>
								</table>
							</div>
							<!-- 중복도서 탭 끝 -->
							
							<!-- 대출중인 도서 탭 -->
							<div class="tab-pane fade check__tab__char" id="rentedBook">
								<table class="table table-sm table-bordered">
									<thead>
										<tr>
											<th class="table-info text-center" style="width:50px;">번호</th>
											<th class="table-info text-center" style="width:120px;">점검결과</th>
											<th class="table-info text-center" style="width:100px;">점검용번호</th>
											<th class="table-info text-center" style="width:100px;">바코드번호</th>
											<th class="table-info text-center" style="width:80px;">서가</th>
											<th class="table-info text-center" style="width:380px;">도서명</th>
											<th class="table-info text-center" style="width:300px;">저자명</th>
											<th class="table-info text-center" style="width:100px;">출판사명</th>
											<th class="table-info text-center" style="width:80px;">점검자</th>
										</tr>
									</thead>
									<tbody id="tbodyRentedBook">
										
									</tbody>
								</table>
							</div>
							<!-- 대출중인 도서 탭 끝 -->
							
							<!-- 미등록 도서 탭 -->
							<div class="tab-pane fade check__tab__char" id="unregisteredBook">
								<table class="table table-sm table-bordered">
									<thead>
										<tr>
											<th class="table-info text-center" style="width:50px;">번호</th>
											<th class="table-info text-center" style="width:120px;">점검결과</th>
											<th class="table-info text-center" style="width:100px;">점검용번호</th>
											<th class="table-info text-center" style="width:100px;">바코드번호</th>
											<th class="table-info text-center" style="width:80px;">서가</th>
											<th class="table-info text-center" style="width:380px;">도서명</th>
											<th class="table-info text-center" style="width:300px;">저자명</th>
											<th class="table-info text-center" style="width:100px;">출판사명</th>
											<th class="table-info text-center" style="width:80px;">점검자</th>
										</tr>
									</thead>
									<tbody id="tbodyUnregisteredBook">
										
									</tbody>
								</table>
							</div>
							<!-- 미등록 도서 탭 끝 -->
							
							<!-- 미점검도서 탭 -->
							<div class="tab-pane fade check__tab__char" id="uncheckedBook">
								<table class="table table-sm table-bordered">
									<thead>
										<tr>
											<th class="table-info text-center" style="width:50px;">번호</th>
											<th class="table-info text-center" style="width:120px;">점검결과</th>
											<th class="table-info text-center" style="width:100px;">점검용번호</th>
											<th class="table-info text-center" style="width:100px;">바코드번호</th>
											<th class="table-info text-center" style="width:80px;">서가</th>
											<th class="table-info text-center" style="width:380px;">도서명</th>
											<th class="table-info text-center" style="width:300px;">저자명</th>
											<th class="table-info text-center" style="width:100px;">출판사명</th>
											<th class="table-info text-center" style="width:80px;">점검자</th>
										</tr>
									</thead>
									<tbody id="tbodyUncheckedBook">
										
									</tbody>
								</table>
							</div>
							<!-- 미점검도서 탭 끝 -->
							
							<!-- 전체 대출중인 도서 탭 -->
							<div class="tab-pane fade check__tab__char" id="wholeBrwedBook">
								<table class="table table-sm table-bordered">
									<thead>
										<tr>
											<th class="table-info text-center" style="width:50px;">번호</th>
											<th class="table-info text-center" style="width:120px;">점검결과</th>
											<th class="table-info text-center" style="width:100px;">점검용번호</th>
											<th class="table-info text-center" style="width:100px;">바코드번호</th>
											<th class="table-info text-center" style="width:80px;">서가</th>
											<th class="table-info text-center" style="width:380px;">도서명</th>
											<th class="table-info text-center" style="width:300px;">저자명</th>
											<th class="table-info text-center" style="width:100px;">출판사명</th>
											<th class="table-info text-center" style="width:80px;">점검자</th>
										</tr>
									</thead>
									<tbody id="tbodyWholeBrwedBook">
										
									</tbody>
								</table>
							</div>
							<!-- 전체 대출중인 도서 탭 끝 -->
							
						</div> <!-- tab div 끝 -->
					</div>
					<!-- card body 끝 -->
				</div>
				<!-- card 끝 -->
				<input type="hidden" id="id_bcs" name="id_bcs" value="${idBcs}"/>

			</div>
			<!-- container-fluid 끝 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
		<!-- content wrapper 끝 -->
	</div>
	<!-- wrapper 끝 -->
	<!-- book_check 소리 -->
	<%@ include file="/WEB-INF/inc/book_check_sound_script.jsp"%>
	<!-- book_check 소리 끝 -->
	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
	<%@ include file="/WEB-INF/inc/korToEng.jsp"%>

	<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/js/book/book_check/book_check_list.js"></script>
</body>
</html>