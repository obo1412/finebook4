<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="currDate" class="java.util.Date" />
<!doctype html>
<html>
<head>
	<meta charset='utf-8' />
	<meta name="viewport"
		content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<title>보유도서 목록</title>
	
	<%@ include file="/WEB-INF/inc/head.jsp"%>
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/statistics/statistics.css" />
</head>

<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>

	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">

				<div class="card mb-3">
					<div class="card-header">
						<h6 class='float-left'>통계</h6>
					</div>
					<div class="card-body dp__flex">
						<!-- 검색폼 + 추가버튼 -->
						<div>
							<div class="float-left">
								<button class="btn btn-secondary" onclick="todayBrwRtnList()">일일 대출/반납</button>
								<!-- <button class="btn btn-secondary" onclick="dataListArray()">함수실행</button> -->
							</div>
							
							<div class="float-left form-inline ml-2">
								<button class="btn btn-secondary" onclick="clickedDataListPrint()">인쇄</button>
								<select name="yearOptionBookHeldList" id="yearOptionBookHeldList" class="form-control">
									<option value="">전체목록</option>
									<fmt:formatDate value="${currDate}" pattern="yyyy" var="yearStart" />
									<c:forEach begin="0" end="10" var="pastYear" step="1">
										<option value="<c:out value='${yearStart-pastYear}'/>">
											<c:out value='${yearStart-pastYear}'/>
										</option>
									</c:forEach>
								</select>
								<button class="btn btn-secondary" onclick="clickedDataListToExcel()">엑셀변환</button>
							</div>
						</div>
						
						<div class="stat__line">
							<div class="form-inline">
								<input type="date" class="form-control" id="staDateStart" max="3000-12-31" value="2021-01-01">
								<input type="date" class="form-control" id="staDateEnd" max="3000-12-31" value="2021-12-31">
								<button class="btn btn-warning" onclick="makeExcelBrwAndMemberByDateDownload()">기간별대출통계</button>
							</div>
						</div>
						
						<input type="hidden" class="form-control" id="dataRow" value=""/>
						<input type="hidden" class="form-control" id="dataCol" value=""/>
						<!-- 조회결과를 출력하기 위한 표 -->
						<table class="table table-sm" style="margin-top: 80px;">
							<caption id="tcaption"></caption>
							<thead id="thead">
							</thead>
							<tbody id="tbody">
							</tbody>
						</table>

						<%-- <!-- 페이지 번호 -->
						<%@ include file="/WEB-INF/inc/common_pagination_bottom.jsp"%> --%>
					</div>
					<!-- card body 끝 -->
				</div>
				<!-- card 끝 -->
			</div>
			<!-- container-fluid 끝 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
	</div>

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
	
	<!-- date 형식 바꿔주는 date 핸들러 js -->
	<script type="text/javascript" src="/assets/js/date_handler.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/statistics/statistics.js"></script>

</body>
</html>



