<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/inc/taglib_jstl.jsp"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/inc/head.jsp"%>
<style type="text/css">
.card {
	float:left;
}

.card-body {
	font-size: 11pt;
}
</style>

</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>
	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">
			
				<div class="card mb-3 mr-2">
					<div class="card-header">
						<h4>도서 일괄 등록하기</h4>
					</div>
					
					<div style="float:left; margin-left:10px;">
						<progress id="progBar" max="${lastCellCount[0]}" value="0"></progress>
					</div>
					
					<div class="card-body">
						<div class="table-responsive row">
							<div class="float-left">
								<label for="mustCode">
									<input type="checkbox" id="mustCode" value="mustCode" />
									필수항목처리(체크시필수항목있는도서만등록)
								</label>
							</div>
							
							<div class="form-group float-right div-btns">
								<input id="curRow" value="" />
								<button class="btn btn-primary btn-sm" onclick="clickedBtnSubmit()">등록하기</button>
								<button class="btn btn-danger btn-sm" onclick="cancelProcess()">전송중단</button>
							</div>

							<!-- 회원정보, 도서정보 수집 시작 -->
							<form class="form-horizontal info-section" name="batchForm" method="post">
								<div>
									<input type="hidden" id="timeStandard" name="timeStandard" value="${timeStandard}"/>
									<input type="hidden" id="lastRowCount" name="lastRowCount" value="${lastCellCount[0]}"/>
									<input type="hidden" id="lastColCount" name="lastColCount" value="${lastCellCount[1]}"/>
									<input type="hidden" id="loadFilePath" name="loadFilePath" value="${loadFilePath}"/>
								</div>
								
								<table class="table table-sm">
									<thead>
										<c:forEach var="row" items="${theArr}" varStatus="firStat">
											<c:if test="${firStat.index eq 0}">
												<tr>
													<c:forEach var="col" items="${row}" varStatus="seStat">
														<th class="text-center cur-th">
															<select name="colH${seStat.index}" id="colH${seStat.index}">
																<option value="">목록제외</option>
																<option value="isbn13">ISBN13</option>
															</select>
														</th>
													</c:forEach>
												</tr>
												<tr>
													<c:forEach var="col" items="${row}" varStatus="seStat">
														<th class="table-info text-center cur-th">${col}</th>
														<c:if test="${(firStat.index eq 0) and (seStat.last eq true)}" >
															<!-- 마지막 col값을 주기 위한 변수로 -->
															<input type="hidden" name="colLast" value="${seStat.index}" />
														</c:if>
													</c:forEach>
												</tr>
											</c:if>
										</c:forEach>
									</thead>
									
									<tbody>
										<c:forEach var="row" items="${theArr}" varStatus="firStat">
											<c:if test="${firStat.index gt 0}">
											<tr>
												<c:forEach var="col" items="${row}" varStatus="seStat">
													<td name="col${seStat.index}" class="text-center cur-td">
														${col}
														<input type="hidden" name="col${seStat.index}" value="${col}" />
													</td>
												</c:forEach>
											</tr>
											</c:if>
										</c:forEach>
									</tbody>
									
								</table>
								
							</form>
							<!-- 회원정보, 도서정보 끝 -->
						</div>
						<!-- table responsive 끝 -->
					</div>
					<!-- card body 끝 -->
				</div>
				<!-- card 끝 -->
			</div>
			<!-- container fluid 끝 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
		<!-- content wrapper 끝 -->
	</div>
	<!-- wrapp 끝 -->

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
	<script type="text/javascript" src="/assets/js/book-reg/reg-batch/reg_book_batch.js"></script>
</body>

</html>