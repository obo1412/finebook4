<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset='utf-8' />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>국립중앙도서관 api 검색</title>

<%@ include file="/WEB-INF/inc/head.jsp"%>
<style type="text/css">
.table-sm {
	font-size: 12px;
}
</style>
</head>

<body>

	<div id="wrapper">

		<div id="content-wrapper" style="padding-bottom: 0px;">
			<div class="container-fluid">

				<div class="card mb-3">
					<div class="card-header">
						<h6 class='pull-left'>국립중앙도서관 정보</h6>
					</div>
					<div class="card-body">
						<!-- 검색폼 + 추가버튼 -->
						<div style='margin-top: 30px;'>
							<!-- search-submit 버튼에 e.preventDefault()를 걸어두어서
								form action과 submit 버튼 타입은 소용이 없으나, 그냥 남겨둠. -->
							<div style="width:300px;" class="float-left">
								<form method='get'
									action='${pageContext.request.contextPath}/book/search_nl_book.do'>
									<div class="input-group input-group-sm">
									
										<span class="input-group-prepend">
											<select name="searchOpt" id="searchOptId" class="form-control form-control-sm">
												<option value="1" <c:if test="${searchOpt == 1}">selected</c:if>>
													ISBN</option>
												<option value="2" <c:if test="${searchOpt == 2}">selected</c:if>>
													제목</option>
												<option value="3" <c:if test="${searchOpt == 3}">selected</c:if>>
													저자</option>
												<option value="4" <c:if test="${searchOpt == 4}">selected</c:if>>
													제목+저자</option>
											</select>
										</span>
										<input type="text" name='search-book-info' id='search-book-info'
											class="form-control form-control-sm" placeholder="검색" value="${keyword}" />
										<span class="input-group-append">
											<button id="search-submit" class="btn btn-sm btn-success" type="submit">
												<i class='fas fa-search'></i>
											</button>
										</span>
										
									</div><!-- input group 끝 -->
								</form>
							</div>
							
							<div id="warningMsg" style="margin-left:10px; font-size:15px; color:red;" class="float-left">
								
							</div>

						</div>

						<!-- 조회결과를 출력하기 위한 표 -->
						<div class="table-responsive" style="margin-top:65px;">
							<table class="table table-sm">
								<thead>
									<tr>
										<th class="info text-center">도서명</th>
										<th class="info text-center">도서저자</th>
										<th class="info text-center">출판사</th>
										<th class="info text-center">ISBN</th>
										<th class="info text-center">분류기호</th>
										<th class="info text-center">선택</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${fn:length(xmlArray) > 0}">
											<c:forEach var="item" items="${xmlArray}" varStatus="status">
												<tr>
													<td class="text-center">${item.title_info}</td>
													<td class="text-center">${item.author_info}</td>
													<td class="text-center">${item.pub_info}</td>
													<td class="text-center">${item.isbn}</td>
													<td class="text-center">
														<a class="pick-clscode" thisClassNo="${item.class_no}">
															${item.class_no}
														</a>
													</td>
													<td class="text-center">
														<button class="pick-book btn btn-primary btn-sm"
															thisTitle="${item.title_info}"
															thisAuthor="${item.author_info}"
															thisPub="${item.pub_info}"
															thisIsbn="${item.isbn}"
															thisClassNo="${item.class_no}">
															선택</button></td>
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

						<!-- 페이지 번호 -->
						<%@ include file="/WEB-INF/inc/common_pagination_bottom.jsp"%>
					</div>
					<!-- card body 끝 -->
				</div>
				<!-- card 끝 -->
			</div>
			<!-- container-fluid 끝 -->
			<%-- <%@ include file="/WEB-INF/inc/footer.jsp"%> --%>
		</div>
	</div>

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
	<script type="text/javascript">
		window.onload = function() {
			document.getElementById('keyword').focus();
		}
	
		//제목+저자시 예시 표시
		let warningMsg = document.getElementById('warningMsg');
		let searchOpt = document.getElementById('searchOptId');
		
		searchOpt.addEventListener('change', (e)=>{
			if(searchOpt.value == 4) {
				warningMsg.innerText = '[제목]은 띄어쓰기하지 않음(예시: 엄마를부탁해 신경숙)';
			} else {
				warningMsg.innerText = '';
			}
		})
		//제목+저자 검색 예시 표시
		
		$(document).on('click', '.pick-book', function() {
			var titleR = $(this).attr('thisTitle');
			var authorR = $(this).attr('thisAuthor');
			var pubR = $(this).attr('thisPub');
			var isbnR = $(this).attr('thisIsbn');
			var classNoR = $(this).attr('thisClassNo');
			
			$(opener.document).find("#bookTitle").val(titleR);
			$(opener.document).find("#author").val(authorR);
			$(opener.document).find("#publisher").val(pubR);
			$(opener.document).find("#isbn13").val(isbnR);
			$(opener.document).find("#classificationCode").val(classNoR);
			
			var url = location.search;
			var params = url.substr(url.indexOf("?")+1);
			var sval = "";
			params = params.split("&");
			for(var i=0; i<params.length; i++){
				temp=params[i].split("=");
				if(temp[0]=='sort-idx') {
					sval=temp[1];
				}
			}
			if(sval != "") {
				var preState = window.opener.document;
				preState.getElementById('title'+sval).value = titleR;
				preState.getElementById('author'+sval).value = authorR;
				preState.getElementById('cls'+sval).value = classNoR;
			}
			
			window.close();
		});
		
		$(document).on('click', '.pick-clscode', function() {
			var url = location.search;
			var params = url.substr(url.indexOf("?")+1);
			var sval = "";
			params = params.split("&");
			for(var i=0; i<params.length; i++){
				temp=params[i].split("=");
				if(temp[0]=='this-clscode-idx') {
					sval=temp[1];
				}
			}
			var classNoR = $(this).attr('thisClassNo');
			window.opener.document.getElementById(sval).value = classNoR;
			window.close();
		});
		
		/* 일괄등록시 sortidx와clscodeidx를 계속 전달하기 위해서 js사용 */
		$(document).on('click', '#search-submit', function(e) {
			e.preventDefault();
			var urlParams = location.search;
			var params = urlParams.substr(urlParams.indexOf("?")+1);
			var clsval="";
			var sortval="";
			params = params.split("&");
			for(var i=0; i<params.length; i++){
				temp=params[i].split("=");
				if(temp[0]=='this-clscode-idx') {
					clsval=temp[1];
				} else if(temp[0]=='sort-idx'){
					sortval=temp[1];
				}
			}
			
			var keyword = $("#search-book-info").val();
			var searchOpt = $("#searchOptId").val();
			var url = '${pageContext.request.contextPath}/book/search_nl_book.do?searchOpt='+searchOpt+'&search-book-info='+keyword;
			url = url +'&sort-idx='+sortval+'&this-clscode-idx='+clsval;
			window.open(url, '_self', 'width=800,height=600,scrollbars=yes');
		});
	</script>
</body>
</html>



