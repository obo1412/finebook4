<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/inc/head.jsp"%>
<style type="text/css">


.card {
	float:left;
	width: 100%;
	height:800px;
}

.card-body {
	font-size: 11pt;
	overflow: auto;
}

.cur-th, .cur-td {
	width: 100px;
	white-space: nowrap;
	overflow: hidden;
}

.div-btns {
	margin-bottom:0;
}

label {
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
						<div style="float:left;">
							<h4>도서 데이터 엑셀 등록</h4>
						</div>
						
						<div style="float:left; margin-left:10px;">
							<progress id="progBar" max="${row_crt}" value="0"></progress>
						</div>
						
						<div class="form-group float-right div-btns">
							<input id="curRow" value="" />
							<button class="btn btn-primary btn-sm" onclick="clickedBtnSubmit()">등록하기</button>
							<button class="btn btn-danger btn-sm" onclick="cancelProcess()">전송중단</button>
						</div>
					</div>
					
					<div class="card-body">
						<div class="table-responsive row">

							<!-- 회원정보, 도서정보 수집 시작 -->
							<form class="form-horizontal info-section" id="formImportExcelCheck"
								method="post">

								<div>
									<input type="hidden" id="timeStandard" name="timeStandard" value="${timeStandard}"/>
									<input type="hidden" id="row_crt" name="row_crt" value="${row_crt}"/>
									<input type="hidden" id="lastCellCount" name="lastCellCount" value="${lastCellCount}"/>
									<input type="hidden" id="loadFilePath" name="loadFilePath" value="${loadFilePath}"/>
									<input type="hidden" id="fileId" name="fileId" value="${fileId}"/>
								</div>

								<table class="table table-sm">

									<thead>
										<c:forEach var="row" items="${theArr}" varStatus="firStat">
											<c:if test="${firStat.index eq 0}">
												<tr>
													<c:forEach var="col" items="${row}" varStatus="seStat">
														<th class="text-center cur-th">
															<c:set var="opt" value="" />
															<c:if test="">
																<c:set var="opt" value="selected" />
															</c:if>
															<select name="colH${seStat.index}" id="colH${seStat.index}">
																<option value="">목록제외</option>
																<option value="barcode">등록번호</option>
																<option value="title">도서명</option>
																<option value="author">저자명</option>
																<option value="publisher">출판사</option>
																<option value="pubDate">출판일시</option>
																<!-- <option value="pubDateYear">발행연도</option> -->
																<option value="regDate">등록일</option>
																<option value="price">가격</option>
																<option value="rfId">RFID</option>
																<option value="isbn10">ISBN10</option>
																<option value="isbn13">ISBN13</option>
																<option value="category">카테고리</option>
																<option value="description">내용요약</option>
																<option value="purOrDon">수입구분</option>
																<option value="available">도서상태</option>
																<option value="callNumber">청구기호</option>
																<option value="additionalCode">별치기호</option>
																<option value="classificationCode">분류기호</option>
																<option value="authorCode">저자기호</option>
																<option value="volumeCode">권차기호</option>
																<option value="copyCode">복본기호</option>
																<option value="bookOrNot">도서비도서</option>
																<option value="pageItem">페이지</option>
																<option value="imageLink">이미지링크</option>
																<option value="bookShelf">서가</option>
																<option value="bookSize">도서크기</option>
																<option value="bookTag">북태그-메모</option>
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
</body>
<script type="text/javascript" src="/assets/js/book/data-import-export/import_book_excel.js"></script>
</html>