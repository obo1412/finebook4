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
			max-width: 1500px;
		}
	
		table { 
			table-layout: fixed;
		}
		
		tr > td {
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
		}
		
		@media screen and (max-width:600px) {
			.mobile-hide {display: none;}
		}
	</style>
</head>

<body class="sidebar-toggled">
	<%@ include file="/WEB-INF/inc/topbar_member.jsp"%>

	<div id="wrapper">
		<div id="content-wrapper">
			<div class="container-fluid">

						<!-- 검색폼 + 추가버튼 -->
						<div style='margin: 10px auto;'>
							<div style="margin-bottom:5px;">
								<form method='get'
									action='${pageContext.request.contextPath}/blook_around.do'
									style="width: 300px;">
									<div class="input-group input-group-sm">
										<span class="input-group-prepend">
											<select name="searchOpt" class="form-control form-control-sm">
													<option value="1"
														<c:if test="${searchOpt == 1}">selected</c:if>>제목</option>
													<option value="2"
														<c:if test="${searchOpt == 2}">selected</c:if>>저자</option>
													<option value="3"
														<c:if test="${searchOpt == 3}">selected</c:if>>출판사</option>
											</select>
										</span>
										<input type="text" name='keyword'
											class="form-control form-control-sm" placeholder="도서 검색"
											value="${keyword}" autofocus/>
										<span class="input-group-append">
											<button class="btn btn-success btn-sm" type="submit">
												<i class='fas fa-search'></i>
											</button>
										</span>
										<input type="hidden" name="lib" value="${idLib}">
										<input type="hidden" name="skl" value="${stringKeyLib}">
									</div>
								</form>
							</div>
						</div>

						<!-- 조회결과를 출력하기 위한 표 -->
						<table class="table table-sm">
							<thead>
								<tr>
									<th class="info text-center" style="width:50px;">번호</th>
									<th class="info text-center" style="width:120px;">도서명</th>
									<th class="info text-center" style="width:70px;">저자명</th>
									<th class="info text-center" style="width:70px;">상태</th>
									<th class="info text-center" style="width:70px;">서가</th>
									<th class="info text-center mobile-hide" style="width:70px;">출판사</th>
									<th class="info text-center mobile-hide" style="width:70px;">출판일</th>
									<th class="info text-center mobile-hide" style="width:90px;">ISBN13</th>
									<th class="info text-center mobile-hide" style="width:80px;">청구기호</th>
									<th class="info text-center mobile-hide" style="width:80px;">등록일</th>
									<th class="info text-center mobile-hide" style="width:60px;">등록번호</th>
									<th class="info text-center mobile-hide" style="width:60px;">권차기호</th>
									<th class="info text-center mobile-hide" style="width:60px;">복본기호</th>
									<th class="info text-center mobile-hide" style="width:30px;">색상</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${fn:length(bookHeldList) > 0}">
										<c:forEach var="item" items="${bookHeldList}" varStatus="status">
											<tr>
												<td class="text-center">
													<c:url var="viewUrl" value="/blook_details.do">
														<c:param name="localIdBarcode" value="${item.localIdBarcode}" />
														<c:param name="bookHeldId" value="${item.id}" />
														<c:param name="lib" value="${idLib}" />
														<c:param name="skl" value="${stringKeyLib}" />
													</c:url>
													<a href="${viewUrl}" onclick="window.open(this.href, '_blank','width=550,height=800,scrollbars=yes');return false;">
														${page.indexLast - status.index}
													</a>
												</td>
												<td class="text-center" data-toggle="tooltip" data-placement="top" title="${item.title}">${item.title}</td>
												<td class="text-center" data-toggle="tooltip" data-placement="top" title="${item.writer}">${item.writer}</td>
												<td class="text-center text-danger" data-toggle="tooltip" data-placement="top" title="${item.brwStatus}">${item.brwStatus}</td>
												<td class="text-center">${item.bookShelf}</td>
												<td class="text-center mobile-hide" data-toggle="tooltip" data-placement="top" title="${item.publisher}">${item.publisher}</td>
												<fmt:parseDate var="parsePubDate" value="${item.pubDate}" pattern="yyyy-MM-dd"/>
												<fmt:formatDate var="pubDate" value="${parsePubDate}" pattern="yyyy-MM-dd" />
												<td class="text-center mobile-hide">${pubDate}</td>
												<td class="text-center mobile-hide">${item.isbn13}</td>
												<td class="text-center mobile-hide">
													<div>
														<c:if test="${not empty item.additionalCode}">${item.additionalCode} </c:if>
													</div>
													<div>
														<c:if test="${not empty item.classificationCode}">${item.classificationCode} </c:if>
													</div>
													<div>
														<c:if test="${not empty item.authorCode}">${item.authorCode} </c:if>
													</div>
													<div>
														<c:if test="${not empty item.volumeCode}">V${item.volumeCode} </c:if>
													</div>
													<div>
														<c:if test="${item.copyCode ne '0'}">C${item.copyCode} </c:if>
													</div>
												</td>
												<td class="text-center mobile-hide">
													<fmt:parseDate var="regDateStr" value="${item.regDate}" pattern="yyyy-MM-dd" />
													<!-- DB에서 가져온 String형 데이터를 Date로 변환 var 변수명, pattern 날짜형식, value 컨트롤러에서 받은값 var에 저장 -->
													<fmt:formatDate var="regDateFmt" value="${regDateStr}" pattern="yyyy-MM-dd" />
													<!-- Date형으로 저장된 값을 String으로 변환 var변수에 넣고 아래처럼 변수를 사용 -->
													${regDateFmt}
												</td>
												<td class="text-center mobile-hide">${item.localIdBarcode}</td>
												<td class="text-center mobile-hide">
													<c:choose>
														<c:when test="${not empty item.volumeCode}">
															V${item.volumeCode}
														</c:when>
														<c:otherwise>
															-
														</c:otherwise>
													</c:choose>
												</td>
												<c:choose>
													<c:when test="${item.copyCode eq 0}">
														<td class="text-center mobile-hide">-</td>
													</c:when>
													<c:otherwise>
														<td class="text-center mobile-hide">C${item.copyCode}</td>
													</c:otherwise>
												</c:choose>
												<td class="text-center mobile-hide">
													<div style="margin:auto; width:30px; min-height:15px; background-color:${item.classCodeColor}"></div>
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
						<%@ include file="/WEB-INF/inc/common_pagination_bottom.jsp"%>
			</div>
			<!-- container-fluid 끝 -->
		</div><!-- content-wrapper 끝 -->
	</div><!-- wrapper 끝 -->
	<%@ include file="/WEB-INF/inc/footer.jsp"%>

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
	
	<script type="text/javascript">
		$(function () {
		  $('[data-toggle="tooltip"]').tooltip()
		});
		
	</script>
</body>
</html>



