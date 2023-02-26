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
						<h4>도서 일괄 등록하기(txt)</h4>
					</div>
					
					<div style="float:left; margin-left:10px;">
						<progress id="progBar" max="${lastCellCount[0]}" value="0"></progress>
					</div>
					
					<div class="card-body">
						<div class="table-responsive row">
							
							
							<div class="form-group float-right div-btns">
								<input id="curRow" value="" />
								<button class="btn btn-primary btn-sm" onclick="clickedBtnTxtBatchSubmit()">등록하기</button>
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
										<tr>
											<th class="text-center cur-th table-info">등록체크</th>
											<th class="text-center cur-th table-info">도서등록번호</th>
											<th class="text-center cur-th table-info">도서명</th>
											<th class="text-center cur-th table-info">저자명</th>
											<th class="text-center cur-th table-info">저자기호</th>
											<th class="text-center cur-th table-info">분류기호</th>
											<th class="text-center cur-th table-info">별치기호</th>
											<th class="text-center cur-th table-info">권차기호</th>
											<th class="text-center cur-th table-info">복본기호</th>
											<th class="text-center cur-th table-info">도서분류</th>
											<th class="text-center cur-th table-info">출판사</th>
											<th class="text-center cur-th table-info">출판일</th>
											<th class="text-center cur-th table-info">페이지</th>
											<th class="text-center cur-th table-info">가격</th>
											<th class="text-center cur-th table-info">ISBN13</th>
											<th class="text-center cur-th table-info">서가</th>
										</tr>
									</thead>
									
									<tbody>
										<c:forEach var="row" items="${theArr}" varStatus="firStat">
											<tr>
												<td class="text-center cur-td">
													<input type="checkbox" name="row${firStat.index}" checked/>
												</td>
												<c:forEach var="col" items="${row}" varStatus="seStat">
													<td class="text-center cur-td">
														<input type="text" name="row${firStat.index}" value="${col}" />
													</td>
												</c:forEach>
												<c:if test="${firStat.last eq true}">
												<!-- 마지막값일 경우 마지막 숫자 담을 input -->
													<td>
														<input type="hidden" id="lastRow" value="${firStat.count}" />
													</td>
												</c:if>
											</tr>
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