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
<title>도서 상세 보기</title>

<%@ include file="/WEB-INF/inc/head.jsp"%>
</head>

<body>
	<div id="wrapper">
		<div id="content-wrapper">
			<div class="container-fluid">
				<h4 class='page-header'>도서 상세 보기</h4>
				
				<!-- 버튼 -->
				<div class="text-right">
					<a
						href="${pageContext.request.contextPath}/book/book_held_edit.do?localIdBarcode=${bookHeldItem.localIdBarcode}"
						class="btn btn-warning">수정</a> <a
						href="#"
						class="btn btn-danger" data-toggle="modal" data-target="#delete_book_modal">삭제</a> <input type="button"
						class="btn btn-primary closeRefresh" value="닫기" onclick="self.close()" />
				</div>
				
				<!-- 조회결과를 출력하기 위한 표 -->
				<table class="table table-sm table-bordered mt-2">
					<tbody>
						<tr>
							<th class="table-info text-center" width="130">
								<button id="btn_edit_tag" class="btn btn-sm btn-info">태그/수정</button>
							</th>
							<td>
								<input type="hidden" id="book_held_id" value="${bookHeldItem.id}" />
								<input type="text" class="form-control form-control-sm" name="tag" id="tag" value="${bookHeldItem.tag}" />
							</td>
						</tr>
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
							<th class="table-info text-center" width="130">번호</th>
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
							<c:choose>
								<c:when test="${not empty bookHeldItem.volumeCode}">
									<td>V${bookHeldItem.volumeCode}</td>
								</c:when>
								<c:otherwise>
									<td>-</td>
								</c:otherwise>
							</c:choose>
						</tr>
						<tr>
							<th class="table-info text-center">복본기호</th>
							<c:choose>
								<c:when test="${bookHeldItem.copyCode eq 0}">
									<td>원본</td>
								</c:when>
								<c:otherwise>
									<td>C${bookHeldItem.copyCode}</td>
								</c:otherwise>
							</c:choose>
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
							<td>
								${won}
							</td>
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
							<th class="table-info text-center">도서 크기</th>
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
						<c:if test="${bookHeldItem.available < 2 }">
							<input type="submit" class="btn btn-warning" value="폐기" formaction="${pageContext.request.contextPath}/book/book_held_discard_ok.do"/>
						</c:if>
						<input type="submit" class="btn btn-danger" value="기록삭제" formaction="${pageContext.request.contextPath}/book/book_held_delete_ok.do"/>
					</div>
				</form>
			</div>
		</div>
	</div>
<%@ include file="/WEB-INF/inc/script-common.jsp"%>
<script type="text/javascript">
		const btnEditTag = document.getElementById('btn_edit_tag');
		btnEditTag.addEventListener('click', function() {
			var editData = {
				"book_held_id" : document.getElementById('book_held_id').value,
				"tag" : document.getElementById('tag').value
			};
			$.ajax({
				url: "${pageContext.request.contextPath}/book/edit_book_held_tag_ok.do",
				type: 'POST',
				data: editData,
				success: function(data) {
					location.href=document.URL;
				}
			});
		});
		
	</script>
</body>
</html>