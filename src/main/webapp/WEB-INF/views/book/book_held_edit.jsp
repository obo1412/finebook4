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
<title>도서 정보 수정하기</title>

<%@ include file="/WEB-INF/inc/head.jsp"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/book/book_held_edit.css" />
<style type="text/css">
</style>
</head>

<body>
	<div id="wrapper">
		<div id="content-wrapper">
			<div class="container-fluid">
				<h4 class='page-header'>도서 정보 수정</h4>

				<form class="horizontal" name="edit_book_held" method="post" action="${pageContext.request.contextPath}/book/book_held_edit_ok.do">
					
					<div class="form-group mt-2">
						<div class="col-md-offset-2 col-md-6">
							<button type="submit" class="btn btn-primary btn-sm">저장하기</button>
							
							<button type="reset" class="btn btn-secondary btn-sm">다시작성</button>
							<div class="float-right">
								<a href="#" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#delete_book_modal">삭제</a>
								<input type="button" class="btn btn-warning btn-sm closeRefresh" value="닫기" onclick="self.close()" />
							</div> 
						</div>
					</div>
					
					<input type="hidden" name="id" id="id" value="${bookHeldItem.id}"/>
					
					<div class="makeAtc">
						<div class="form-inline mb-2">
							<label for="titleBook" class="col-3">도서 제목</label>
							<div class="col-9">
								<input name="title" id="title" class="form-control form-control-sm" 
									value="${fn:escapeXml(bookHeldItem.title)}" />
							</div>
						</div>
						
						<div class="form-inline mb-2">
							<label for="author" class="col-3">저자명</label>
							<div class="col-9">
								<input name="author" id="author" class="form-control form-control-sm" 
									value="${bookHeldItem.writer}" />
							</div>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="publisher" class="col-3">출판사</label>
						<div class="col-9">
							<input name="publisher" id="publisher" class="form-control form-control-sm" 
								value="${bookHeldItem.publisher}" />
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="pubDate" class="col-3">출판일</label>
						<div class="col-9">
							<fmt:parseDate var="parsePubDate" value="${bookHeldItem.pubDate}" pattern="yyyy-MM-dd"/>
							<fmt:formatDate var="pubDate" value="${parsePubDate}" pattern="yyyy-MM-dd" />
							<input name="pubDate" id="pubDate" class="form-control form-control-sm" 
								value="${pubDate}" />
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="localIdBarcode" class="col-3">도서등록번호</label>
						<div class="col-9">
							<input name="localIdBarcode" id="localIdBarcode" class="form-control form-control-sm"
								value="${bookHeldItem.localIdBarcode}"/>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="category" class="col-3">카테고리</label>
						<div class="col-9">
							<input name="category" id="category" class="form-control form-control-sm" 
								value="${bookHeldItem.category}" />
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="bookShelf" class="col-3">서가</label>
						<div class="col-9">
							<input name="bookShelf" id="bookShelf" class="form-control form-control-sm"
								value="${bookHeldItem.bookShelf}"/>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="additionalCode" class="col-3">별치기호</label>
						<div class="col-9">
							<input name="additionalCode" id="additionalCode" class="form-control form-control-sm"
								value="${bookHeldItem.additionalCode}"/>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="classificationCode" class="col-3">분류기호</label>
						<div class="col-9">
							<input name="classificationCode" id="classificationCode" class="form-control form-control-sm"
								value="${bookHeldItem.classificationCode}"/>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="authorCode" class="col-3">저자기호</label>
						<div class="col-9">
							<input name="authorCode" id="authorCode" class="form-control form-control-sm"
								value="${bookHeldItem.authorCode}"/>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="volumeCode" class="col-3">권차기호</label>
						<div class="col-9">
							<input name="volumeCode" id="volumeCode" class="form-control form-control-sm"
								value="${bookHeldItem.volumeCode}"/>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="copyCode" class="col-3">복본기호</label>
						<div class="col-9">
							<input name="copyCode" id="copyCode" class="form-control form-control-sm"
								value="${bookHeldItem.copyCode}"/>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="isbn13" class="col-3">ISBN 13</label>
						<div class="col-9">
							<input name="isbn13" id="isbn13" class="form-control form-control-sm" 
								value="${bookHeldItem.isbn13}" />
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="isbn10" class="col-3">ISBN 10</label>
						<div class="col-9">
							<input name="isbn10" id="isbn10" class="form-control form-control-sm" 
								value="${bookHeldItem.isbn10}" />
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="price" class="col-3">도서가격</label>
						<div class="col-9">
							<input name="price" id="price" class="form-control form-control-sm" 
								value="${bookHeldItem.price}" />
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="page" class="col-3">페이지</label>
						<div class="col-9">
							<input name="page" id="page" class="form-control form-control-sm"
								value="${bookHeldItem.page}"/>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="purchasedOrDonated" class="col-3">구입/기증</label>
						<div class="col-9">
							<select name="purchasedOrDonated" id="purchasedOrDonated"
								class="form-control form-control-sm">
								<option value="1" <c:if test="${bookHeldItem.purchasedOrDonated == 1}">selected</c:if>>구입</option>
								<option value="0"<c:if test="${bookHeldItem.purchasedOrDonated == 0}">selected</c:if>>기증</option>
								<option value="2" <c:if test="${bookHeldItem.purchasedOrDonated == 2}">selected</c:if>>자체발행</option>
							</select>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="bookOrNot" class="col-3">도서/비도서</label>
						<div class="col-9">
							<select name="bookOrNot" class="form-control form-control-sm">
								<option value="BOOK" <c:if test="${bookHeldItem.bookOrNot eq 'BOOK'}">selected</c:if>>
									국내도서</option>
								<option value="MUSIC" <c:if test="${bookHeldItem.bookOrNot eq 'MUSIC'}">selected</c:if>>
									음반</option>
								<option value="DVD" <c:if test="${bookHeldItem.bookOrNot eq 'DVD'}">selected</c:if>>
									DVD</option>
								<option value="FOREIGN" <c:if test="${bookHeldItem.bookOrNot eq 'FOREIGN'}">selected</c:if>>
									외국도서</option>
								<option value="EBOOK" <c:if test="${bookHeldItem.bookOrNot eq 'EBOOK'}">selected</c:if>>
									전자책</option>
							</select>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="rfId" class="col-3">도서크기</label>
						<div class="col-9">
							<input name="bookSize" id="bookSize" class="form-control form-control-sm"
								value="${bookHeldItem.bookSize}"/>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="rfId" class="col-3">RF ID</label>
						<div class="col-9">
							<input name="rfId" id="rfId" class="form-control form-control-sm"
								value="${bookHeldItem.rfId}"/>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="tag" class="col-3">태그(메모)</label>
						<div class="col-9">
							<input name="tag" id="tag" class="form-control form-control-sm"
								value="${bookHeldItem.tag}"/>
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="imageLink" class="col-3">이미지 경로</label>
						<div class="col-9">
							<input name="imageLink" id="imageLink" class="form-control form-control-sm"
								value="${bookHeldItem.imageLink}"/>
						</div>
					</div>
					
				</form>
				
				<ul class="nav nav-tabs" role="tablist">
					<li class="nav-item">
						<a class="nav-link" href="#bookInfo" data-toggle="tab">도서정보</a>
					</li>
					<li class="nav-item">
						<a class="nav-link active" href="#brwLog" data-toggle="tab">대출/반납 이력</a>
					</li>
				</ul>
				
				<div class="tab-content">
					<div class="tab-pane fade" id="bookInfo" role="tabpanel">
						<!-- 조회결과를 출력하기 위한 표 -->
						<table class="table table-sm table-bordered mt-2 data__table">
							<tbody>
								<tr>
									<th class="table-info text-center">도서상태</th>
									<c:choose>
										<c:when test="${bookHeldItem.available < 2 }">
											<td>이용가능</td>
										</c:when>
										<c:otherwise>
											<td style="color: red;">폐기됨</td>
										</c:otherwise>
									</c:choose>
								</tr>
								<tr>
									<th class="table-info text-center">번호</th>
									<td>${bookHeldItem.id}</td>
								</tr>
								<tr>
									<th class="table-info text-center">도서제목</th>
									<td>${bookHeldItem.title}</td>
								</tr>
								<tr>
									<th class="table-info text-center">저자명</th>
									<td>${bookHeldItem.writer}</td>
								</tr>
								<tr>
									<th class="table-info text-center">출판사</th>
									<td>${bookHeldItem.publisher}</td>
								</tr>
								<tr>
									<fmt:parseDate var="parsePubDate" value="${bookHeldItem.pubDate}" pattern="yyyy-MM-dd"/>
									<fmt:formatDate var="pubDate" value="${parsePubDate}" pattern="yyyy-MM-dd" />
									<th class="table-info text-center">출판일</th>
									<td>${pubDate}</td>
								</tr>
								<tr>
									<th class="table-info text-center">도서등록번호</th>
									<td>${bookHeldItem.localIdBarcode}</td>
								</tr>
								<tr>
									<th class="table-info text-center">카테고리</th>
									<td>${bookHeldItem.category}</td>
								</tr>
								<tr>
									<th class="table-info text-center">서가</th>
									<td>${bookHeldItem.bookShelf}</td>
								</tr>
								<tr>
									<th class="table-info text-center">별치기호</th>
									<td>${bookHeldItem.additionalCode}</td>
								</tr>
								<tr>
									<th class="table-info text-center">분류기호</th>
									<td>${bookHeldItem.classificationCode}</td>
								</tr>
								<tr>
									<th class="table-info text-center">저자기호</th>
									<td>${bookHeldItem.authorCode}</td>
								</tr>
								<tr>
									<th class="table-info text-center">권차기호</th>
									<td>${bookHeldItem.volumeCode}</td>
								</tr>
								<tr>
									<th class="table-info text-center">복본기호</th>
									<td>C${bookHeldItem.copyCode}</td>
								</tr>
								<tr>
									<th class="table-info text-center">ISBN 13</th>
									<td>${bookHeldItem.isbn13}</td>
								</tr>
								<tr>
									<th class="table-info text-center">ISBN 10</th>
									<td>${bookHeldItem.isbn10}</td>
								</tr>
								<tr>
									<fmt:parseDate var="parseRegDate" value="${bookHeldItem.regDate}" pattern="yyyy-MM-dd"/>
									<fmt:formatDate var="regDate" value="${parseRegDate}" pattern="yyyy-MM-dd" />
									<th class="table-info text-center">등록일</th>
									<td>${regDate}</td>
								</tr>
								<tr>
									<fmt:parseDate var="parseEditDate" value="${bookHeldItem.editDate}" pattern="yyyy-MM-dd"/>
									<fmt:formatDate var="editDate" value="${parseEditDate}" pattern="yyyy-MM-dd" />
									<th class="table-info text-center">수정일</th>
									<td>${editDate}</td>
								</tr>
								<tr>
									<th class="table-info text-center">가격</th>
									<fmt:formatNumber var="won" value="${bookHeldItem.price}" maxFractionDigits="3" />
									<td>${won}</td>
								</tr>
								<tr>
									<th class="table-info text-center">페이지</th>
									<td>${bookHeldItem.page}</td>
								</tr>
								<tr>
									<th class="table-info text-center">구입/기증</th>
									<td>
										<c:choose>
											<c:when test="${bookHeldItem.purchasedOrDonated eq 1}">
												구입
											</c:when>
											<c:otherwise>
												기증
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
								<tr>
									<th class="table-info text-center">도서/비도서</th>
									<c:choose>
										<c:when test="${bookHeldItem.bookOrNot eq 'BOOK'}">
											<td>국내도서</td>
										</c:when>
										<c:when test="${bookHeldItem.bookOrNot eq 'MUSIC'}">
											<td>음반</td>
										</c:when>
										<c:when test="${bookHeldItem.bookOrNot eq 'DVD'}">
											<td>DVD</td>
										</c:when>
										<c:when test="${bookHeldItem.bookOrNot eq 'FOREIGN'}">
											<td>외국도서</td>
										</c:when>
										<c:when test="${bookHeldItem.bookOrNot eq 'EBOOK'}">
											<td>전자책</td>
										</c:when>
										<c:otherwise>
											<td>기타</td>
										</c:otherwise>
									</c:choose>
								</tr>
								<tr>
									<th class="table-info text-center">도서크기</th>
									<td>${bookHeldItem.bookSize}</td>
								</tr>
								<tr>
									<th class="table-info text-center">RF ID</th>
									<td>${bookHeldItem.rfId}</td>
								</tr>
								<tr>
									<th class="table-info text-center">메모(태그)</th>
									<td>${bookHeldItem.tag}</td>
								</tr>
								<tr>
									<th class="table-info text-center">도서 국가</th>
									<td>${bookHeldItem.nameCountry}</td>
								</tr>
								<tr>
									<th class="table-info text-center">표지</th>
									<td><img name="bookCover" src="${bookHeldItem.imageLink}" /></td>
								</tr>
								<tr>
									<th class="table-info text-center">도서 설명</th>
									<td>${bookHeldItem.descriptionBook}</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="tab-pane fade show active" id="brwLog" role="tabpanel">
						<div class="div__brw__log">
							<table class="table table-sm table-bordered mt-2">
								<thead>
									<tr>
										<th class="table-warning text-center member__th__num">번호</th>
										<th class="table-warning text-center">대출회원</th>
										<th class="table-warning text-center">연락처</th>
										<th class="table-warning text-center">대출일</th>
										<th class="table-warning text-center">반납일</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${fn:length(brwLog) >0}">
											<c:forEach var="brwItem" items="${brwLog}" varStatus="status">
												<tr>
													<td class="text-center">${status.count}</td>
													<td class="text-center">
														<c:url var="memberUrl" value="/member/member_view.do">
															<c:param name="memberId" value="${brwItem.idMemberBrw}" />
														</c:url>
														<a href="${memberUrl}" onclick="window.open(this.href, '_blank','width=650,height=800,scrollbars=yes');return false;">
															${brwItem.name}
														</a>
													</td>
													<td class="text-center">${brwItem.phone}</td>
													
													<fmt:parseDate var="parseStartDate" value="${brwItem.startDateBrw}" pattern="yyyy-MM-dd"/>
													<fmt:formatDate var="startDate" value="${parseStartDate}" pattern="yyyy-MM-dd" />
													<td class="text-center">${startDate}</td>
													
													<fmt:parseDate var="parseEndDate" value="${brwItem.endDateBrw}" pattern="yyyy-MM-dd"/>
													<fmt:formatDate var="endDate" value="${parseEndDate}" pattern="yyyy-MM-dd" />
													<td class="text-center">${endDate}</td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td colspan="4" class="text-center">
													대출/반납 이력이 없습니다.
												</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
						</div>
					</div>
				</div> <!-- tab-content 끝  -->
			</div>
			<!-- container-fluid 끝 -->
		</div>
		<!-- content-wrapper 끝 -->
	</div>
	<!-- wrapper 끝 -->
	<div class="modal fade" id="delete_book_modal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">도서 정보를 삭제 또는 폐기하시겠습니까?</h5>
					<button class="close" type="button" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
					</button>
				</div>
				<form name="book_held_delete" method="post">
					<input type="hidden" name="localIdBarcode" value="${bookHeldItem.localIdBarcode}"/>
					<input type="hidden" name="bookHeldId" value="${bookHeldItem.id}"/>
					<div class="modal-body">
						<div>기록삭제는 도서정보를 모두 삭제합니다.(복구불가능)</div>
						<div>폐기는 해당 도서의 바코드번호, 복본기호 등만을 삭제하여 기록은 남기지만 이용가능한 도서에서 제외됩니다.</div>
					</div>
					<div class="modal-footer">
						<button class="btn btn-secondary" type="button"
							data-dismiss="modal">취소</button>
						<c:if test="${bookHeldItem.available < 2}">
						<input type="submit" class="btn btn-warning" value="폐기" formaction="${pageContext.request.contextPath}/book/book_held_discard_ok.do"/>
						</c:if>
						<c:if test="${(fn:length(brwLog) eq 0) and (bookCheckCount eq 0)}">
							<input type="submit" class="btn btn-danger" value="기록삭제" formaction="${pageContext.request.contextPath}/book/book_held_delete_ok.do"/>
						</c:if>
					</div>
				</form>
			</div>
		</div>
	</div>
<%@ include file="/WEB-INF/inc/script-common.jsp"%>
<script type="text/javascript">
		
		const makeAtc = document.querySelector('.makeAtc');
		makeAtc.addEventListener('change', function(){
			makeAuthorCode();
		});
		
		function makeAuthorCode() {
			var thisTitle = document.getElementById('title').value;
			var thisAuthor = document.getElementById('author').value;
			var atcout = null;
			
			if(thisTitle!=null&&thisAuthor!=null) {
				$.ajax({
					url: "${pageContext.request.contextPath}/book/author_code.do",
					type: 'POST',
					data: {
						thisTitle,
						thisAuthor
					},
					/* dataType: "json", */
					success: function(data) {
						if(data.rt != 'OK') {
							alert(data.rt);
						} else {
							document.getElementById('authorCode').value = data.result;
						}
					}
				});
			}
		};

		$(function() {
			$(".closeRefresh").click(function(){
				opener.location.href=opener.document.URL;
				window.close();
			});
			
			//아래 새로고침 기능을 사용하면, 자식창(도서변경창)이 떠있는 상태에서 부모창(도서목록)이
			//새로고침 되면서, 부모창에 잘못된 변수가 전달되는 것 같다.
			//도서 목록의 맨아래 LIMIT -10, 10 으로 쿼리 처리됨. 음수 -10은 도대체 어디서 오는 것이냐..
			//pagehelper에서 계산하면서 0-10 이런식으로 처리되서 음수가 나오는 것 같다.
			//아래 리프레쉬 기능을 사용하지 않음으로서 도서 변경시 뜨는 500 에러 메시지는
			//아직까지는 나오지 않는다.
			$(".Refresh").click(function(){
				opener.location.href=opener.document.URL;
			});
		});
	</script>
</body>
</html>