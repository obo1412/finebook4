<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/inc/head.jsp"%>
<link href="/assets/css/book_req/document_list_book_req.css" rel="stylesheet" >
</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>
	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">
				
				<div class="dup__check__box">
					<div class="search__bar__group">
            <div>
              <div class="child__search__bar">
                <label for="isbn_req" class="label__width"><span>1차검색</span> ISBN:</label>
                <input type="text" id="isbn_req" name="isbn_req" onkeydown="doSearchEnterKeyDown()" />
              </div>
              <div class="child__search__bar">
                <label for="title_req" class="label__width"><span>2차검색</span> 도서명:</label>
                <input type="text" id="title_req" name="title_req" onkeydown="doSearchEnterKeyDown()" />
                <label for="author_req" class="label__author">저자명:</label>
                <input type="text" id="author_req" name="author_req" onkeydown="doSearchEnterKeyDown()" />
              </div>
            </div>
						<button id="btnSearchAndCheckDup" class="btn__search">검색</button>
					</div>
					
					<div class="div__content">
            <div class="inlib__whole__box">

              <div class="hori__box">
                <div class="box__title">보유도서 목록</div>
                <div class="book__in__lib__list common__box">
                  <table class="table__lib">
                    <thead>
                      <tr>
                        <th class="tb__lib__num text-center">번호</th>
                        <th class="tb__lib__title">도서명</th>
                        <th class="tb__lib__author">저자명</th>
                        <th class="tb__lib__publisher text-center">출판사</th>
                        <th class="tb__lib__isbn13 text-center">ISBN</th>
                        <th class="tb__lib__barcode text-center">등록번호</th>
                      </tr>
                    </thead>
                    <tbody class="tb__lib__body">
                      
                    </tbody>
                  </table>
                  
                </div>
              </div>
              <template id="tmpl__book__in__lib">
                <tr class="inlib__book">
                  <td class="tb__lib__num text-center"></td>
                  <td class="tb__lib__title">
                    <div class="inlib__title width__fix__scrollable">
                      
                    </div>
                    <div class="inlib__book__data" style="display: none;"></div>
                  </td>
                  <td class="tb__lib__author">
                    <div class="inlib__author width__fix__scrollable">
                      
                    </div>
                  </td>
                  <td class="tb__lib__publisher text-center">
                    <div class="inlib__publisher width__fix__scrollable"></div>
                  </td>
                  <td class="tb__lib__isbn13 text-center">
                    <div class="inlib__isbn13"></div>
                  </td>
                  <td class="tb__lib__barcode text-center">
                    <div class="inlib__barcode"></div>
                  </td>
                </tr>
              </template>

            </div>  <!--inlib__whole__box-->
						
            <div class="api__whole__box">

              <div class="hori__box">
                <div class="box__title">온라인 도서 정보 검색 목록</div>
                <div class="book__api__list common__box api__box__height">
                  <table class="table__lib">
                    <thead>
                      <tr>
                        <th class="tb__api__num text-center">번호</th>
                        <th class="tb__api__title" colspan="2">도서명</th>
                      </tr>
                      <tr>
                        <th class="tb__api__publisher text-center">출판사</th>
                        <th class="tb__api__author">저자명</th>
                        <th class="tb__api__isbn13 text-center">ISBN</th>
                      </tr>
                    </thead>
                    <tbody class="tb__api__body">
                      
                    </tbody>
                  </table>
                </div>
              </div>
              <template id="tmpl__book__api">
                <tr class="api__book">
                	<td class="tb__api__num"></td>
                	<td class="tb__api__title" colspan="2">
                		<div class="api__title width__fix__scrollable"></div>
	                  <div class="api__book__data" style="display:none;"></div>
                	</td>
                </tr>
                <tr>
                	<td class="tb__api__publisher">
                		<div class="api__publisher width__fix"></div>
                	</td>
                	<td class="tb__api__author">
	                  <div class="api__author width__fix__scrollable"></div>
                	</td>
                	<td>
	                  <div class="api__isbn13"></div>
                	</td>
                </tr>
              </template>
              
              <div class="hori__box">
                <div class="box__title">온라인 도서 정보 상세보기</div>
                <div class="detail__book__api common__box api__box__height">
               
               		<input type="hidden" name="category" value="request" />
                  <div class="div__img__book__api text-center">
                    <img id="img__book__api" class="img__book__api" src="" alt="">
                    <input type="hidden" name="inputImageLink" id="imageLink" value=""/>
                  </div>
                  <div class="content__divider"></div>
                  <div class="detail__api__title text-center">
                  	<input type="text" name="inputApiTitle" id="inputApiTitle" value=""/>
                  </div>
                  <div class="content__divider"></div>
                  <div class="detail__api__author text-center">
                  	<input type="text" name="inputApiAuthor" id="inputApiAuthor" value=""/>
                  </div>
                  <div class="content__divider"></div>
                  <div class="detail__api__publisher text-center">
                  	<input type="text" name="inputApiPublisher" id="inputApiPublisher" value=""/>
                  </div>
                  <div class="content__divider"></div>
                  <div class="detail__api__isbn13 text-center">
                  	<input type="text" name="inputApiIsbn" id="inputApiIsbn" value=""/>
                  </div>
                  
                  <div class="text-center">
                    <button class="btn btn-primary btn__req__book" id="btnWriteBookReq">
                    	등록신청하기
                    </button>
                  </div>
                  
                </div>
              </div>

            </div> <!-- api__whole__box 끝 -->
					</div><!-- div-content 끝 -->
				</div><!--dup check box 끝-->
				
				<div class="">
					<h1 class="page-header">${bbsName}
						- <small>요청 목록</small>
					</h1>
	
					<!-- 글 목록 시작 -->
					<div class="table-responsive">
						<table class="table table-hover">
							<thead>
								<tr>
									<th class="text-center" style="width: 100px">번호</th>
									<th class="text-center">제목</th>
									<th class="text-center" style="width: 120px">신청부서</th>
									<th class="text-center" style="width: 100px">신청자</th>
									<th class="text-center" style="width: 100px">조회수</th>
									<th class="text-center" style="width: 120px">작성일</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${fn:length(documentList) > 0}">
										<c:forEach var="document" items="${documentList}">
											<tr>
												<td class="text-center">${maxPageNo}</td>
												<td class="text-center"><c:url var="readUrl" value="/bbs/document_read.do">
														<c:param name="category" value="${document.category}" />
														<c:param name="document_id" value="${document.id}" />
													</c:url> <a href="${readUrl}">${document.subject}</a></td>
												<td class="text-center">${document.teamDoc}</td>
												<td class="text-center">${document.personDoc}</td>
												<td class="text-center">${document.hit}</td>
												<td class="text-center">${document.regDate}</td>
											</tr>
											<c:set var="maxPageNo" value="${maxPageNo-1}" />
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="5" class="text-center"
												style="line-height: 100px;">조회된 글이 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
					<!--// 글 목록 끝 -->
					<!-- 목록 페이지 하단부의 쓰기버튼+검색폼+페이지 번호 공통 영역 include -->
					<%@ include file="/WEB-INF/inc/bbs_list_bottom.jsp"%>
				</div>

			</div> <!-- container-fluid 종료 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
	</div>
	<%@ include file="/WEB-INF/inc/script-common.jsp" %>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/js/book/book_prevent_redundancy/document_list_book_req.js"></script>
</body>
</html>



