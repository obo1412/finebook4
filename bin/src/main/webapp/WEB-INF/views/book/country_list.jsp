<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!doctype html>
<html>
<head>
<meta charset='utf-8' />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>국가 목록</title>

<%@ include file="/WEB-INF/inc/head.jsp"%>
</head>

<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>

	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">

				<div class="card mb-3" style="width:400px;">
					<div class="card-header">
						<h6 class='float-left'>국가 목록</h6>
					</div>
					<div class="card-body">
						<!-- 검색폼 + 추가버튼 -->
						<div style='margin-top: 10px;'>
							<div class="float-left">
								<form method='get'
									action='${pageContext.request.contextPath}/book/country_list.do'
									style="width: 120px;">
									<div class="input-group input-group-sm">
										<input type="text" name='keyword'
										class="form-control form-control-sm" placeholder="국가 검색"
											value="${keyword}" autofocus/>
										<span class="input-group-append">
											<button class="btn btn-success btn-sm" type="submit">
												<i class='fas fa-search'></i>
											</button>
										</span>
									</div>
								</form>
							</div>
							
							<div class="float-right">
								<form method='post' action="${pageContext.request.contextPath}/book/insert_country.do"
									style="width: 200px;" >
									<div class="input-group input-group-sm">
										<input type="text" name="newCountry" 
											class="form-control form-control-sm" />
										<span class="input-group-append">
											<input class="btn btn-sm btn-primary" type="submit" value='국가추가' />
										</span>
									</div>
								</form>
							</div>
						</div>

						<!-- 조회결과를 출력하기 위한 표 -->
						<table class="table table-sm" style="width:300px;">
							<thead>
								<tr>
									<th class="table-info text-center">번호</th>
									<th class="table-info text-center">국가명</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${fn:length(countryList) > 0}">
										<c:forEach var="item" items="${countryList}" varStatus="status">
											<tr>
												<td class="text-center">${status.count}</td>
												<td class="text-center">${item.nameCountry}</td>
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
					<!-- card body 끝 -->
				</div>
				<!-- card 끝 -->
			</div>
			<!-- container-fluid 끝 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
	</div>

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>
</html>



