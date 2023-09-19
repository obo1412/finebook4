<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true"%>

<!-- 페이지 번호 시작 -->
<nav class="text-center">
	<ul class="pagination" style="justify-content:center; margin-bottom:0px;">
		
		<li class="input-group mr-2" style="width:120px;">
			<!-- 각 페이지 번호로 이동할 수 있는 URL을 생성하여 page_url에 저장 -->
			<c:url var="targetUrl" value="${pageDefUrl}" >
				<c:if test="${category ne null}">
					<c:param name="category" value="${category}"></c:param>
				</c:if>
				<c:if test="${searchOpt ne null}">
					<c:param name="searchOpt" value="${searchOpt}"></c:param>
				</c:if>
				<c:param name="keyword" value="${keyword}"></c:param>
				<c:param name="keywordHolder" value="${keywordHolder}"></c:param>
				<!-- 서가 분류처리 -->
				<c:if test="${bookShelf ne null}">
					<c:param name="bookShelf" value="${bookShelf}"></c:param>
				</c:if>
				<!-- 별치기호 분류처리 -->
				<c:if test="${addiCode ne null}">
					<c:param name="addiCode" value="${addiCode}"></c:param>
				</c:if>
				<%-- <c:param name="page" value=""></c:param> --%>
				<!-- 회원도서 조회용 파라미터 -->
					<c:if test="${stringKeyLib ne null}">
						<c:param name="lib" value="${idLib}" />
						<c:param name="skl" value="${stringKeyLib}" />
					</c:if>
					<!-- 도서 조회용 파라미터 -->
			</c:url>
			
			<input type="text" id="inputTargetPage" class="form-control" onKeyDown="enterTargetPage()" />
			<div class="input-group-append">
				<a class="btn btn-secondary text-white" id="btnTargetPageUrl" href="${targetUrl}">이동</a>
			</div>
		</li>
		<!-- 이전 그룹으로 이동 -->
		<c:choose>
			<c:when test="${page.prevPage > 0}">
				<!-- 이전 그룹에 대한 페이지 번호가 존재한다면? -->
				<!-- 이전 그룹으로 이동하기 위한 URL을 생성해서 "prevUrl"에 저장 -->
				<c:url var="prevUrl" value="${pageDefUrl}">
					<c:if test="${category ne null}">
						<c:param name="category" value="${category}"></c:param>
					</c:if>
					<c:if test="${searchOpt ne null}">
						<c:param name="searchOpt" value="${searchOpt}"></c:param>
					</c:if>
					<c:param name="keyword" value="${keyword}"></c:param>
					<c:param name="keywordHolder" value="${keywordHolder}"></c:param>
					<!-- 서가 분류처리 -->
					<c:if test="${bookShelf ne null}">
						<c:param name="bookShelf" value="${bookShelf}"></c:param>
					</c:if>
					<!-- 별치기호 분류처리 -->
				<c:if test="${addiCode ne null}">
					<c:param name="addiCode" value="${addiCode}"></c:param>
				</c:if>
					<c:param name="page" value="${page.prevPage}"></c:param>
					<!-- 회원도서 조회용 파라미터 -->
					<c:if test="${stringKeyLib ne null}">
						<c:param name="lib" value="${idLib}" />
						<c:param name="skl" value="${stringKeyLib}" />
					</c:if>
					<!-- 도서 조회용 파라미터 -->
				</c:url>

				<li class="page-item"><a class="page-link" href="${prevUrl}">&laquo;</a></li>
			</c:when>

			<c:otherwise>
				<!-- 이전 그룹에 대한 페이지 번호가 존재하지 않는다면? -->
				<li class='disabled page-item'><a class="page-link" href="#">&laquo;</a></li>
			</c:otherwise>
		</c:choose>
			
		<!-- 페이지 번호 -->
		<!-- 현재 그룹의 시작페이지~끝페이지 사이를 1씩 증가하면서 반복 -->
		<c:forEach var="i" begin="${page.startPage}" end="${page.endPage}" step="1">

			<!-- 각 페이지 번호로 이동할 수 있는 URL을 생성하여 page_url에 저장 -->
			<c:url var="pageUrl" value="${pageDefUrl}" >
				<c:if test="${category ne null}">
					<c:param name="category" value="${category}"></c:param>
				</c:if>
				<c:if test="${searchOpt ne null}">
					<c:param name="searchOpt" value="${searchOpt}"></c:param>
				</c:if>
				<c:param name="keyword" value="${keyword}"></c:param>
				<c:param name="keywordHolder" value="${keywordHolder}"></c:param>
				<!-- 서가 분류처리 -->
				<c:if test="${bookShelf ne null}">
					<c:param name="bookShelf" value="${bookShelf}"></c:param>
				</c:if>
				<!-- 별치기호 분류처리 -->
				<c:if test="${addiCode ne null}">
					<c:param name="addiCode" value="${addiCode}"></c:param>
				</c:if>
				<c:param name="page" value="${i}"></c:param>
				<!-- 회원도서 조회용 파라미터 -->
					<c:if test="${stringKeyLib ne null}">
						<c:param name="lib" value="${idLib}" />
						<c:param name="skl" value="${stringKeyLib}" />
					</c:if>
					<!-- 도서 조회용 파라미터 -->
			</c:url>
				
			<!-- 반복중의 페이지 번호와 현재 위치한 페이지 번호가 같은 경우에 대한 분기 -->
			<c:choose>
				<c:when test="${page.page == i}">
					<li class='active page-item'><a class="page-link" href="#">${i}</a></li>
				</c:when>
				<c:otherwise>
					<li class="page-item"><a class="page-link" href="${pageUrl}">${i}</a></li>
				</c:otherwise>
			</c:choose>	

		</c:forEach>
			
		<!-- 다음 그룹으로 이동 -->
		<c:choose>
			<c:when test="${page.nextPage > 0}">
				<!-- 다음 그룹에 대한 페이지 번호가 존재한다면? -->
				<!-- 다음 그룹으로 이동하기 위한 URL을 생성해서 "nextUrl"에 저장 -->
				<c:url var="nextUrl" value="${pageDefUrl}">
					<c:if test="${category ne null}">
						<c:param name="category" value="${category}"></c:param>
					</c:if>
					<c:if test="${searchOpt ne null}">
						<c:param name="searchOpt" value="${searchOpt}"></c:param>
					</c:if>
					<c:param name="keyword" value="${keyword}"></c:param>
					<c:param name="keywordHolder" value="${keywordHolder}"></c:param>
					<!-- 서가 분류처리 -->
					<c:if test="${bookShelf ne null}">
						<c:param name="bookShelf" value="${bookShelf}"></c:param>
					</c:if>
					<!-- 별치기호 분류처리 -->
				<c:if test="${addiCode ne null}">
					<c:param name="addiCode" value="${addiCode}"></c:param>
				</c:if>
					<c:param name="page" value="${page.nextPage}"></c:param>
					<!-- 회원도서 조회용 파라미터 -->
					<c:if test="${stringKeyLib ne null}">
						<c:param name="lib" value="${idLib}" />
						<c:param name="skl" value="${stringKeyLib}" />
					</c:if>
					<!-- 도서 조회용 파라미터 -->
				</c:url>

				<li class="page-item"><a class="page-link" href="${nextUrl}">&raquo;</a></li>
			</c:when>

			<c:otherwise>
				<!-- 이전 그룹에 대한 페이지 번호가 존재하지 않는다면? -->
				<li class='disabled page-item'><a class="page-link" href="#">&raquo;</a></li>
			</c:otherwise>
		</c:choose>
		
		
	</ul>
</nav>
<!--// 페이지 번호 끝 -->

<script type="text/javascript">
	const btnTargetPageUrl = document.getElementById('btnTargetPageUrl');
	const inputTargetPage = document.getElementById('inputTargetPage');
	
	btnTargetPageUrl.addEventListener('click', (e) => {
		e.preventDefault();
		//타겟 페이지 이동 함수 실행
		moveTargetPage();
	})
	
	//타겟 페이지 input 에서 엔터 쳤을때
	function enterTargetPage() {
		if(event.keyCode == 13) {
			//타겟 페이지 이동 함수 실행
			moveTargetPage();
		}
	}
	
	//타겟 페이지 이동 함수 정의
	function moveTargetPage() {
		//함수 실행시 페이지 이동
		location.href = btnTargetPageUrl.href + '&page='+inputTargetPage.value;
	}
</script>
	
	
	
	