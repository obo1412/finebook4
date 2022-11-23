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
	<style>
		.card {
			max-width: 100%;
		}
	
		table { 
			table-layout: fixed;
		}
		
		tr > td {
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
		}
		
		.clsExpDate {
			width:150px;
		}
		
	</style>
</head>

<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>

	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">

				<div class="card mb-3" style="height:800px;">
					<div class="card-header">
						
						<!-- 검색폼 + 추가버튼 -->
						<div style=''>
							<div class="float-left mr-2">
								<button class="btn btn-sm btn-secondary" onclick="checkAll()">전체선택</button>
								<button class="btn btn-sm btn-secondary" onclick="uncheckAll()">전체해제</button>
							</div>
							<div class="float-left">
								<form method='get'
									action='${pageContext.request.contextPath}/admin_setting/admin_lib_list.do'
									style="width: 300px;">
									<div class="input-group input-group-sm">
										<span class="input-group-prepend">
											<select name="searchOpt" class="form-control form-control-sm">
													<option value="1"
														<c:if test="${searchOpt == 1}">selected</c:if>>도서관명</option>
													<option value="2"
														<c:if test="${searchOpt == 2}">selected</c:if>>위치</option>
											</select>
										</span>
										
										<input type="text" name='keyword'
											class="form-control form-control-sm" placeholder="검색어"
											value="${keyword}" autofocus/>
										
										<input type="hidden" name="keywordHolder" value="${keywordHolder}" />
										
										<span class="input-group-append">
											<button class="btn btn-success btn-sm" type="submit">
												<i class='fas fa-search'></i>
											</button>
										</span>
										
									</div>
								</form>
							</div> <!-- 좌측 정렬 -->
							
							<div class="float-right">
								<!-- 도서관 추가 모달 -->
								<%@ include file="/WEB-INF/views/admin_setting/modal_admin_add_new_lib.jsp" %>
								
								<a href="#" data-toggle="modal" data-target="#clicked_add_new_lib_modal"
									class="btn btn-primary btn-sm">도서관 추가</a>
							</div> <!-- 우측 정렬 -->
							
						</div>
						
					</div> <!-- card-header 끝 -->
					
					<div class="card-body" style="overflow-y: scroll;">
						<!-- 조회결과를 출력하기 위한 표 -->
						<table class="table table-sm">
							<thead>
								<tr>
									<th class="info text-center" style="width:20px;">-</th>
									<th class="info text-center" style="width:50px;">번호</th>
									<th class="info text-center" style="width:150px;">도서관명</th>
									<th class="info text-center" style="width:70px;">위치</th>
									<th class="info text-center" style="width:70px;">식별키(SKL)</th>
									<th class="info text-center" style="width:70px;">계정용도</th>
									<th class="info text-center" style="width:100px;">등록일</th>
									<th class="info text-center" style="width:100px;">정산일</th>
									<th class="info text-center" style="width:100px;">만료일</th>
								</tr>
							</thead>
							<tbody class="searchCls">
								<c:choose>
									<c:when test="${fn:length(libList) > 0}">
										<c:forEach var="item" items="${libList}" varStatus="status">
											<tr>
												<td class="cls-id-lib" style="display:none;">${item.idLib}</td>
												<td class="text-center">
													<input type="checkbox" class="chk-box-book-id"/>
												</td>
												<td class="text-center">${page.indexLast - status.index}</td>
												<td class="text-center" style="white-space:nowrap; text-overflow:ellipsis;">
													<c:url var="viewUrl" value="/admin_setting/admin_lib_edit.do">
														<c:param name="idLib" value="${item.idLib}" />
													</c:url>
													<a href="${viewUrl}"
													onclick="window.open(this.href, '_blank','width=550,height=500,scrollbars=yes');return false;">
														${item.nameLib}
													</a>
												</td>
												<td class="text-center">${item.locLib}</td>
												<td class="text-center">${item.stringKeyLib}</td>
												<td class="text-center">${item.purpose}</td>
												<td class="text-center">
													<fmt:parseDate var="parseRegDate" value="${item.regDateLib}" pattern="yyyy-MM-dd"/>
													<fmt:formatDate var="regDate" value="${parseRegDate}" pattern="yyyy-MM-dd" />
													<span class="spanRegDate">
														${regDate}
													</span>
												</td>
												<td class="text-center">
													<fmt:parseDate var="parseStatementDate" value="${item.statementDate}" pattern="yyyy-MM-dd"/>
													<fmt:formatDate var="statementDate" value="${parseStatementDate}" pattern="yyyy-MM-dd" />
													<span class="spanStatementDate">
														${statementDate}
													</span>
													<span>
														<button class="btn btn-secondary btn-sm" onclick="updateStatementDateLastestAsync()">정산</button>
														<span class="clsIdLib" style="display:none;">${item.idLib}</span>
													</span>
												</td>
												<fmt:parseDate var="parseExpDate" value="${item.expDate}" pattern="yyyy-MM-dd"/>
												<fmt:formatDate var="expDate" value="${parseExpDate}" pattern="yyyy-MM-dd" />
												<td class="text-center">
													<span class="spanExpDate">
														<input class="clsIdLib" type="hidden" value="${item.idLib}" />
														<input class="clsExpDate" type="date" max="9999-12-31" value="${expDate}"/>
													</span>
													<span>
														<button class="btn btn-secondary btn-sm" onclick="clickedUpdateExpDate()">변경</button>
													</span>
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
					</div>
					<!-- card body 끝 -->
					<div class="card-footer">
						<!-- 페이지 번호 -->
						<%@ include file="/WEB-INF/inc/common_pagination_bottom.jsp"%>
					</div>
				</div>
				<!-- card 끝 -->
			</div>
			<!-- container-fluid 끝 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
	</div>

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
	
	<!-- 현재 페이지 js 처리 -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/admin-setting/admin_lib_list.js"></script>
	<!-- 체크박스 처리를 위한 js 호출 -->
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/book-side-list-check-box/book_check_box.js"></script> --%>
	<script type="text/javascript">
	
	</script>
</body>
</html>



