<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/inc/taglib_jstl.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- 오늘날짜 만들고, 날짜패턴 아래와같이 바꾸기 -->
<jsp:useBean id="currDate" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${currDate}" pattern="yyyy-MM-dd HH:mm:ss" />
<!-- 오늘날짜 관련 끝 -->
<!doctype html>
<html>
<head>
<meta charset='utf-8' />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>대출된 도서 목록</title>

<%@ include file="/WEB-INF/inc/head.jsp"%>
</head>

<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>

	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">
				
				<div class="card mb-3">
					<div class="card-header">
						<h6 class='float-left'>대출된 도서 목록</h6>
					</div>
					<div class="card-body" style="overflow-x:scroll;">
						<!-- 검색폼 + 추가버튼 -->
						<div style="">
							<form method='get' name="searchFormBookHeldBrwed"
								action='${pageContext.request.contextPath}/book/book_held_list_brwd.do'
								style="float:left;">
								<div class="input-group input-group-sm">
									<span class="input-group-prepend">
										<input type="hidden" id="searchMemberClass" value="${memberClassName}" />
										<select name="memberClassName" id="selectMemberClass" class="form-control form-control-sm">
											<option value="">회원분류(전체)</option>
											<option value="unClass">미분류</option>
											<c:choose>
												<c:when test="${fn:length(memberClassList) > 0}">
													<c:forEach var="item" items="${memberClassList}" varStatus="status">
														<option value="${item.className}">${item.className}</option>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<option>분류 없음</option>
												</c:otherwise>
											</c:choose>
										</select>
									</span>
									<span class="input-group-prepend">
										<select name="searchOpt" class="form-control form-control-sm">
												<option value="1"
													<c:if test="${searchOpt == 1}">selected</c:if>>대출회원</option>
												<option value="2"
													<c:if test="${searchOpt == 2}">selected</c:if>>도서제목</option>
												<option value="3"
													<c:if test="${searchOpt == 3}">selected</c:if>>도서등록번호</option>
										</select>
									</span>
									<input type="text" name='keyword' class="form-control form-control-sm"
										placeholder="검색" value="${keyword}" /> <span
										class="input-group-append">
										<button class="btn btn-success btn-sm" type="submit">
											<i class='fas fa-search'></i>
										</button>
									</span>
								</div>
							</form>
							
							<div class="float-right">
								<button class="btn btn-sm btn-secondary" onclick="clickedBrwedBookHeldListToExcel()">대출도서목록 엑셀변환</button>
							</div>
						</div>
						

						<!-- 조회결과를 출력하기 위한 표 -->
						<table class="table table-sm">
							<thead>
								<tr>
									<th class="table-info text-center" style="width:40px;">번호</th>
									<th class="table-info text-center" style="width:100px;">대출회원</th>
									<th class="table-info text-center" style="width:150px;">회원연락처</th>
									<th class="table-info text-center" style="width:100px;">회원분류</th>
									<th class="table-info text-center" style="width:200px;">도서제목</th>
									<th class="table-info text-center" style="width:200px;">도서저자</th>
									<th class="table-info text-center" style="width:100px;">도서바코드</th>
									<th class="table-info text-center" style="width:150px;">대출 일시</th>
									<th class="table-info text-center" style="width:100px;">반납 예정일</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${fn:length(brwList) > 0}">
										<c:forEach var="item" items="${brwList}" varStatus="status">
											<tr>
												<td class="text-center">${page.indexStart + status.index}</td>
												<td class="text-center">${item.name}</td>
												<td class="text-center">${item.phone}</td>
												<td class="text-center">${item.className}</td>
												<td class="text-center">${item.title}</td>
												<td class="text-center">${item.writer}</td>
												<td class="text-center">${item.localIdBarcode}</td>
												<fmt:parseDate var="parseStartDate" value="${item.startDateBrw}" pattern="yyyy-MM-dd HH:mm:ss"/>
												<fmt:formatDate var="startDateTime" value="${parseStartDate}" pattern="yyyy-MM-dd HH:mm:ss" />
												<td class="text-center">${startDateTime}</td>
												<!-- 반납예정일은 시간 필요없음. -->
												<fmt:parseDate var="parseDueDate" value="${item.dueDateBrw}" pattern="yyyy-MM-dd"/>
												<fmt:formatDate var="dueDate" value="${parseDueDate}" pattern="yyyy-MM-dd" />
													<c:set var="delay" value="" />
													<c:if test="${item.dueDateBrw < currentDate}">
														<c:set var="delay" value="text-danger" />
													</c:if>
												<td class="text-center ${delay}">${dueDate}</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="8" class="text-center"
												style="line-height: 100px;">조회된 데이터가 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>

						<!-- 페이지 번호 -->
						<%@ include file="/WEB-INF/inc/common_pagination_bottom.jsp"%>
					</div>
					<!-- 대출도서목록 card body 끝 -->
				</div>
				<!-- 대출도서목록 card 끝 -->
				
				
				
				
				
				
				<!-- 오늘 반납예정, 즉 연체 도서 목록 -->
				<div class="card mb-3">
					<div class="card-header">
						<h6 class='float-left'>오늘 반납예정 도서 목록</h6>
					</div>
					<div class="card-body" style="overflow-x:scroll;">
						<!-- 검색폼 + 추가버튼 -->

						<!-- 조회결과를 출력하기 위한 표 -->
						<table class="table table-sm">
							<thead>
								<tr>
									<th class="table-info text-center" style="width:40px;">번호</th>
									<th class="table-info text-center" style="width:60px;">상태</th>
									<th class="table-info text-center" style="width:100px;">대출회원</th>
									<th class="table-info text-center" style="width:120px;">회원연락처</th>
									<th class="table-info text-center" style="width:200px;">도서제목</th>
									<th class="table-info text-center" style="width:100px;">도서바코드</th>
									<th class="table-info text-center" style="width:150px;">대출 일시</th>
									<th class="table-info text-center" style="width:100px;">반납 예정일</th>
									<th class="table-info text-center" style="width:60px;">연체일</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${fn:length(brwListRtnToday) > 0}">
										<c:forEach var="item" items="${brwListRtnToday}" varStatus="status">
											<tr>
												<td class="text-center">${status.count}</td>
												<c:choose>
													<c:when test="${item.dueDateBrw lt currentDate}">
														<c:set var="state" value="연체" />
														<c:set var="backColor" value="background-color:red; color:white;" />
													</c:when>
													<c:otherwise>
														<c:set var="state" value="예정" />
														<c:set var="backColor" value="background-color:yellow;" />
													</c:otherwise>
												</c:choose>
												<td class="text-center" style="${backColor}">
														${state}
												</td>
												<td class="text-center">${item.name}</td>
												<td class="text-center">${item.phone}</td>
												<td class="text-center">${item.title}</td>
												<td class="text-center">${item.localIdBarcode}</td>
												<!-- 대출일시 -->
												<fmt:parseDate var="parseStartDate" value="${item.startDateBrw}" pattern="yyyy-MM-dd HH:mm:ss"/>
												<fmt:formatDate var="startDateTime" value="${parseStartDate}" pattern="yyyy-MM-dd HH:mm:ss" />
												<td class="text-center">${startDateTime}</td>
												<!-- 반납예정일 -->
												<fmt:parseDate var="parseDueDate" value="${item.dueDateBrw}" pattern="yyyy-MM-dd"/>
												<fmt:formatDate var="dueDate" value="${parseDueDate}" pattern="yyyy-MM-dd" />
												<td class="text-center">${dueDate}</td>
												<fmt:parseDate var="parseDueDateBrw" value="${item.dueDateBrw}" pattern="yyyy-MM-dd"/>
												<fmt:parseNumber value="${parseDueDateBrw.time/(1000*60*60*24)}" integerOnly="true" var="dueDate" />
												<%-- <fmt:formatDate value="${parseDueDateBrw}" type="date"/> --%>
												<fmt:parseDate var="parseCurrentDate" value="${currentDate}" pattern="yyyy-MM-dd" />
												<fmt:parseNumber value="${parseCurrentDate.time/(1000*60*60*24)}" integerOnly="true" var="curDate" />
												<%-- <fmt:formatDate value="${parseCurrentDate}" type="date" /> --%>
												<td class="text-center text-danger">${curDate - dueDate}</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="8" class="text-center"
												style="line-height: 100px;">조회된 데이터가 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>

						<!-- 페이지 번호 연체 도서는 페이지 없이 가자-->
						<%-- <%@ include file="/WEB-INF/inc/common_pagination_bottom.jsp"%> --%>
					</div>
					<!-- 오늘반납예정 card body 끝 -->
				</div>
				<!-- 오늘반납예정 card 끝 -->
				
				
			</div>
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
	</div>


	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
	<!-- 엑셀 다운로드 처리에 필요한 js -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/book/data-import-export/book_list_to_excel.js"></script>
	<!-- 대출된 도서 회원분류관련 js -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/book/borrow/book_list_brwed.js"></script>
</body>
</html>



