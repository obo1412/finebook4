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
		.table {
			font-size: 12px;
		}
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
							장서점검
						</h6>
						<button class="btn btn-info" style="float:right;" onclick="clickedNewChecking()">점검 추가하기</button>
					</div>

					<div class="card-body">
						<table class="table">
							<thead>
								<tr>
									<th class="table-info text-center" style="width:80px;">번호</th>
									<th class="table-info text-center" style="width:110px;">점검일시</th>
									<th class="table-info text-center" style="width:150px;">전체도서</th>
									<th class="table-info text-center" style="width:100px;">점검횟수</th>
									<th class="table-info text-center" style="width:100px;">정상확인도서</th>
									<th class="table-info text-center" style="width:100px;">미점검도서</th>
									<th class="table-info text-center" style="width:100px;">중복점검</th>
									<th class="table-info text-center" style="width:200px;">발견된대출중/전체대출</th>
									<th class="table-info text-center" style="width:160px;">점검자선택/점검하기</th>
									<th class="table-info text-center" style="width:100px;">인쇄</th>
									<th class="table-info text-center" style="width:80px;">삭제</th>
								</tr>
							</thead>
							<tbody class="tb__class">
								<c:choose>
									<c:when test="${fn:length(bookCheckStatList) > 0}">
										<c:forEach var="item" items="${bookCheckStatList}" varStatus="status">
											<tr class="tr__class">
												<td class="text-center">${(fn:length(bookCheckStatList))-status.index}</td>
												<fmt:parseDate var="parseCheckDate" value="${item.checkDate}" pattern="yyyy-MM-dd"/>
												<fmt:formatDate var="checkDate" value="${parseCheckDate}" pattern="yyyy-MM-dd" />
												<td class="text-center">${checkDate}</td>
												<td class="text-center">${item.wholeCount}</td>
												<td class="text-center">${item.checkedCount}</td>
												<td class="text-center">${item.confirmCount}</td>
												<td class="text-center">${item.uncheckedCount}</td>
												<td class="text-center">${item.redupCount}</td>
												<td class="text-center">${item.rentedCheckedCount}<span>/</span>${item.brwedCount}</td>
												<td class="text-center">
												<c:url var="checkIdBcsUrl" value="/book_check/book_check_list.do" >
													<c:param name="id_bcs" value="${item.idBcs}" />
												</c:url>
													<span class="input-group">
														<select name="checker" class="class__checker form-control input-group-prepend">
															<option value="1">첫번째</option>
															<option value="2">두번째</option>
															<option value="3">세번째</option>
															<option value="4">네번째</option>
															<option value="5">다섯번째</option>
														</select>
														<a href="#" class="btn btn-success input-group-append" onclick="enterBookCheckList()">점검</a>
													</span>
												</td>
												<td class="text-center td__id__bcs">
													<input type="hidden" class="cls__id__bcs" value="${item.idBcs}">
													<button class="btn btn-warning" onclick="makeBookCheckExcelFile()">엑셀</button>
												</td>
												<td class="text-center">
													<button class="btn btn-danger btnDeliverIdBcs" data-toggle="modal" data-target="#modalDeleteBcs"
														value="${item.idBcs}" dateValue="${checkDate}" onclick="deliverIdBcs()">
														<span class="iconDeliverIdBcs">&times;</span>
													</button>
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="8" class="text-center">
												아직 시행된 점검 기록이 존재하지 않습니다.
											</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
					<!-- card body 끝 -->
				</div>
				<!-- card 끝 -->
				
				<div class="modal fade" id="modalDeleteBcs" tabindex="-1" >
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="modalDeleteBcsLabel">장서점검 현황을 삭제하시겠습니까?</h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body">
								<div>
									점검생성일자: <span id="dateKey"></span>
								</div>
								지금까지 진행된 도서 점검 목록이 모두 삭제됩니다.<br>
								삭제하시겠습니까?
							</div>
							<input type="hidden" id="receiveIdBcs"/>
							<div class="modal-footer">
								<button type="button" class="btn btn-danger" onclick="deleteIdBcs()">삭제하기</button>
								<button type="button" class="btn btn-primary" data-dismiss="modal">취소</button>
							</div>
						</div>
					</div>
				</div>


			</div>
			<!-- container-fluid 끝 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
		<!-- content wrapper 끝 -->
	</div>
	<!-- wrapper 끝 -->

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
	<%@ include file="/WEB-INF/inc/korToEng.jsp"%>

	<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/js/book/book_check/book_check_status.js"></script>
</body>
</html>