<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/inc/head.jsp"%>

<link href="/assets/css/user-self/user_self.css" rel="stylesheet" >
<style type="text/css">
</style>

</head>
<body>

	<!-- wrapper -->
	<div>
		<!-- image section -->
		<div class="wrap">
			<div class="content-wrap">
					<!-- <img src="/assets/img/user_self_page/user_self_main_img.jpg" alt="img_user_self_main"> -->
					<div class="search-bar-set">
						<div class="search-bar-book">
							<div class="search-bar-box">
								<label>도서검색</label>
								<input type="text" id="searchBookKeyword" style="width:250px;" onkeydown="searchBookEnterKeyDown()"/>
								<button onclick="searchBook()">검색</button>
							</div>
						</div>
						<div class="search-bar-member-book">
							<div class="brw-search-bar-box">
								<span>
									<label>대출/반납</label>
									<input class="input-search-brw" type="text" id="bookMemberBrwKeyword" onkeydown="searchBookMemberBrwKeyDown()"/>
									<button class="btn__brw__return" id="btnBrwReturn">대출/반납</button>
								</span>
								<!-- 회원검색전용 input 필요없다고 판단되어 주석처리 -->
								<!-- <span class="span-search-member-only">
									<label>회원검색전용</label>
									<input type="text" id="searchMemberKeyword" onkeydown="searchMemberEnterKeyDown()"/>
									<button onclick="searchMember()">검색</button>
								</span> -->
							</div>
						</div>
					</div>
					<div class="search-section">
						
						<div class="div-book-content">
							<div class="div-book-list">
	
								<table class="scroll__table">
									<thead>
										<tr>
											<th>번호</th>
											<th rowspan="2" style="width:50%;">도서명</th>
											<th>저자명</th>
										</tr>
										<tr>
											<th>상태</th>
											<th>등록번호</th>
										</tr>
									</thead>
									<tbody class="tbody__book__list">
										
									</tbody>
								</table>
								
								<template id="tmpl__book__tr">
									<tr>
										<td class="tbl__num"></td>
										<td class="tbl__title" rowspan="2">
											<div class="tbl__title__div">
												
											</div>
											<div class="book__data" style="display:none;">
											</div>
										</td>
										<td class="tbl__author">
											<div class="tbl__author__div">
												
											</div>
										</td>
									</tr>
									<tr>
										<td class="tbl__state"></td>
										<td class="tbl__barcode"></td>
									</tr>
								</template>
								
							</div>
							<div class="div-book-detail">
								<div class="book__detail__barcode">
									도서 등록번호
									<div class="div__clear__book__detail">
										<button id="btnClearBookDetail">x</button>
									</div>
									<div>
										<input type="text" id="bookBarcode" value="" readonly />
									</div>
								</div>
								<div class="book__img">
									<img id="img__book" src="" alt="">
								</div>
								<div class="book__detail__text">
									<input id="book__detail__id" type="hidden" />
									<table>
										<tbody>
											<tr>
												<th>도서상태</th>
											</tr>
											<tr>
												<td id="book__detail__state"></td>
											</tr>
										
											<tr>
												<th>도서명</th>
											</tr>
											<tr>
												<td id="book__detail__title"></td>
											</tr>
											
											<tr>
												<th>서가</th>
											</tr>
											<tr>
												<td id="book__detail__bookShelf"></td>
											</tr>

											<tr>
												<th>저자명</th>
											</tr>
											<tr>
												<td id="book__detail__writer"></td>
											</tr>
											
											<tr>
												<th>출판사</th>
											</tr>
											<tr>
												<td id="book__detail__publisher"></td>
											</tr>
											
											<tr>
												<th>출판일</th>
											</tr>
											<tr>
												<td id="book__detail__pubDate"></td>
											</tr>
											
											<tr>
												<th>설명</th>
											</tr>
											<tr>
												<td id="book__detail__description"></td>
											</tr>
											
											<tr>
												<th>청구기호</th>
											</tr>
											<tr>
												<td id="book__detail__addiCode"></td>
											</tr>
											<tr>
												<td id="book__detail__classCode"></td>
											</tr>
											<tr>
												<td id="book__detail__authorCode"></td>
											</tr>
											<tr>
												<td id="book__detail__volumeCode"></td>
											</tr>
											<tr>
												<td id="book__detail__copyCode"></td>
											</tr>
											
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					
					<div class="rental-section">
						
						<div class="div-member-content">
							<div class="div-member-detail">
								<div class="member__img">
									<img id="img__member" src="" alt="">
								</div>
								
								<div class="div__clear__member__detail">
									<button id="btnClearMemberDetail">x</button>
								</div>
								
								<div class="member__detail">
									<div class="member__data">
										<span>회원명:</span>
										<span id="member__detail__name"></span>
									</div>
									<div class="member__data">
										<span>연락처:</span>
										<span id="member__detail__phone"></span>
									</div>
									<div class="member__data">
										<span>회원번호:</span>
										<span>
											<input id="member__detail__memberCode" type="text" value="" readonly>
										</span>
									</div>
									<div class="member__data">
										<span>회원등급:</span>
										<span id="member__detail__grade"></span>
									</div>
									<input id="member__detail__id" type="hidden" />

								</div>
							</div>

							<!-- 회원검색시 목록 띄우기 회원별도 검색 기능 필요없으므로
										주석처리 -->
							<!-- <div class="div-member-list">
								<table class="member__list__table">
									<thead>
										<tr>
											<th>회원명</th>
											<th>연락처</th>
										</tr>
									</thead>
									<tbody class="tbody__member__list">

									</tbody>
								</table>
								
								<template id="tmpl__member__tr">
									<tr>
										<td class="member__name">
										</td>
										<td class="member__phone">
										</td>
										<td style="display:none;">
											<div class="member__data" style="display:none;">
											</div>
										</td>
									</tr>
								</template>
							</div> -->
							
						</div>
						<div class="div-brw-list">
							<table class="brw__list__table">
								<thead>
									<tr>
										<th>번호</th>
										<th rowspan="2" style="width:50%;">도서명</th>
										<th>저자명</th>
										<th>대출일</th>
									</tr>
									<tr>
										<th>상태</th>
										<th>등록번호</th>
										<th>반납일</th>
									</tr>
								</thead>
								<tbody class="tbody__brw__list">
									
								</tbody>
							</table>
								
							<template id="tmpl__brw__tr">
								<tr>
									<td class="tbl__num"></td>
									<td class="tbl__title" rowspan="2">
										<div class="tbl__title__div">
											
										</div>
										<div class="brw__data" style="display:none;">
										</div>
									</td>
									<td class="tbl__author">
										<div class="tbl__author__div">
											
										</div>
									</td>
									<td class="tbl__startDate"></td>
								</tr>
								<tr>
									<td class="tbl__state">
										<button class="tbl__state__btn"></button>
									</td>
									<td class="tbl__barcode"></td>
									<td class="tbl__endDate"></td>
								</tr>
							</template>
						</div>
					</div>
			</div>

			<!-- image section -->
			<!-- button section -->
			<!-- <div class="list">
				<div>
					<button class="menu btn btn-secondary">목록</button>
					<button class="return btn btn-secondary">대출/반납</button>
					<button class="inquiry btn btn-secondary">조회</button>
				</div>
			</div> -->
			<!-- button section -->
		</div>
	</div>
	<!-- wrapper 끝 -->

</body>

<script type="text/javascript" src="/assets/js/date_handler.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/js/user-self/user_self.js"></script>

</html>