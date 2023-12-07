<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!doctype html>
<html>
<head>
<meta charset='utf-8' />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>회원 분류 목록</title>

<%@ include file="/WEB-INF/inc/head.jsp"%>
	<style>
		table { 
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
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>

	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">
				<div class="card mb-3 col-12">
					<div class="card-header">
						<h4 class='pull-left'>회원 분류 목록</h4>
					</div>
					<div class="card-body">
						<!-- 검색폼 + 추가버튼 -->
						<div style='margin-top: 30px;' class="pull-right">
							<form method='get'
								action='${pageContext.request.contextPath}/member/class_list_member.do'
								style="width: 300px;">
								<div class="input-group">
									<input type="text" name='keyword' class="form-control"
										placeholder="회원 분류 검색" value="${keyword}" autofocus/> <span
										class="input-group-append">
											<button class="btn btn-success" type="submit">
												<i class='fas fa-search'></i>
											</button>
										<a href="${pageContext.request.contextPath}/member/class_member_add.do"
										class="btn btn-primary">분류 추가</a>
									</span>
								</div>
							</form>
						</div>

						<!-- 조회결과를 출력하기 위한 표 -->

						<table class="table table-sm">
							<thead>
								<tr>
									<th class="info text-center" style="width:50px;">번호</th>
									<th class="info text-center" style="width:60px;">분류명</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${fn:length(classList) > 0}">
										<c:forEach var="item" items="${classList}" varStatus="status">
											<tr>
												<td class="text-center">${page.indexStart + status.index}</td>
												<td class="text-center">
													<c:url var="readUrl" value="/member/class_member_edit.do">
														<c:param name="idMbrClass" value="${item.idMbrClass}" />
													</c:url>
													<a href="${readUrl}">${item.className}</a>
												</td>
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
						<%@ include file="/WEB-INF/inc/common_pagination_bottom.jsp" %>
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
	<!-- wrapper 끝 -->


	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>
</html>



