<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/inc/head.jsp"%>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<!-- Popper JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

<!-- Twitter Bootstrap3 & jQuery -->
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" />
<script src="http://code.jquery.com/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script> -->

<!-- ajax -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/plugins/ajax/ajax_helper.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/plugins/ajax/ajax_helper.js"></script>

<!-- ajaxForm -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/plugins/ajax-form/jquery.form.min.js"></script>

</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>
	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">

				<h1 class="page-header">${bbsName}
					- <small>글 읽기</small>
				</h1>

				<!-- 제목, 작성자, 작성일시, 조회수 -->
				<div class="alert alert-info">
					<h3 style="margin: 0">
						<span id="document__subject">
							${readDocument.subject}
						</span>
							<br />
							
							<c:if test="${category eq 'request'}">
								<small>
									신청부서: ${readDocument.teamDoc} / 신청자: ${readDocument.personDoc}
								</small>
								<br />
							</c:if>
							<small>
								${readDocument.writerName} <a href="mailto:${readDocument.email}">
										<i class="glyphicon glyphicon-envelope"></i>
								</a> / 조회수 : ${readDocument.hit} / 작성일시 : ${readDocument.regDate}
							</small>
					</h3>
				</div>

				<!-- 첨부파일 목록 표시하기 -->
				<c:if test="${fileList != null}">
					<!-- 첨부파일 목록 -->
					<table class="table table-bordered">
						<tbody>
							<tr>
								<th class="warning" style="width: 100px">첨부파일</th>
								<td><c:forEach var="file" items="${fileList}">
										<!-- 다운로드를 위한 URL만들기 -->
										<c:url var="downloadUrl" value="/download.do">
											<c:param name="file" value="${file.fileDir}/${file.fileName}" />
											<c:param name="orgin" value="${file.originName}" />
										</c:url>
										<!-- 다운로드 링크 -->
										<a href="${downloadUrl}" class="btn btn-link btn-xs">${file.originName}</a>
									</c:forEach></td>
							</tr>
						</tbody>
					</table>

					<!-- 이미지만 별도로 화면에 출력하기 -->
					<c:forEach var="file" items="${fileList}">
						<c:if
							test="${fn:substringBefore(file.contentType, '/') == 'image'}">
							<c:url var="downloadUrl" value="/download.do">
								<c:param name="file" value="${file.fileDir}/${file.fileName}" />
							</c:url>
							<p>
								<img src="${downloadUrl}" class="img-responsive"
									style="margin: auto" />
							</p>
						</c:if>
					</c:forEach>
				</c:if>

				<!-- 내용 -->
				<section style="padding: 0 10px 20px 10px">
					${readDocument.content}</section>

				<!-- 다음글/이전글 -->
				<table class="table table-bordered">
					<tbody>
						<tr>
							<th class="success" style="width: 100px">다음글</th>
							<td><c:choose>
									<c:when test="${nextDocument != null}">
										<c:url var="nextUrl" value="/bbs/document_read.do">
											<c:param name="category" value="${category}" />
											<c:param name="document_id" value="${nextDocument.id}" />
										</c:url>
										<a href="${nextUrl}">${nextDocument.subject}</a>
									</c:when>
									<c:otherwise>
							다음글이 없습니다.
						</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<th class="success" style="width: 100px">이전글</th>
							<td><c:choose>
									<c:when test="${prevDocument != null}">
										<c:url var="prevUrl" value="/bbs/document_read.do">
											<c:param name="category" value="${category}" />
											<c:param name="document_id" value="${prevDocument.id}" />
										</c:url>
										<a href="${prevUrl}">${prevDocument.subject}</a>
									</c:when>
									<c:otherwise>
							이전글이 없습니다.
						</c:otherwise>
								</c:choose></td>
						</tr>
					</tbody>
				</table>

				<!-- 버튼들 : category값에 대한 상태유지 필요함 -->
				<div class="clearfix">
					<c:if test="${category eq 'request'}">
						<div class="float-left">
							<input type='hidden' id="readDocumentId" value='${readDocument.id}' />
							<a href="#" class="btn btn-danger" id="btn__state__request">요청</a>
							<a href="#" class="btn btn-warning" id="btn__state__processing">처리중</a>
							<a href="#" class="btn btn-primary" id="btn__state__done">완료</a>
						</div>
					</c:if>
					<div class="float-right">
						<a
							href="${pageContext.request.contextPath}/bbs/document_list.do?category=${category}"
							class="btn btn-info">목록보기</a> <a
							href="${pageContext.request.contextPath}/bbs/document_write.do?category=${category}"
							class="btn btn-primary">글쓰기</a>
						<!-- 수정,삭제 대상을 지정하기 위해서 글 번호가 전달되어야 한다. -->
						<a
							href="${pageContext.request.contextPath}/bbs/document_edit.do?category=${category}&document_id=${readDocument.id}"
							class="btn btn-warning">수정하기</a> <a
							href="${pageContext.request.contextPath}/bbs/document_delete.do?category=${category}&document_id=${readDocument.id}"
							class="btn btn-danger">삭제하기</a>
					</div>
				</div>

				<!-- 덧글 -->
				<hr />
				<form id="comment_form" method="post"
					action="${pageContext.request.contextPath}/bbs/comment_insert.do">
					<!-- 글 번호 상태 유지 -->
					<input type='hidden' name='document_id' value='${readDocument.id}' />
					<!-- 작성자,비밀번호,이메일은 로그인하지 않은 경우만 입력한다. -->
					<c:if test="${loginInfo == null}">
						<div class='form-group form-inline'>
							<!-- 이름, 비밀번호, 이메일 -->
							<div class="form-group">
								<input type="text" name="writer_name" class="form-control"
									placeholder='작성자' />
							</div>
							<div class="form-group">
								<input type="password" name="writer_pw" class="form-control"
									placeholder='비밀번호' />
							</div>
							<div class="form-group">
								<input type="email" name="email" class="form-control"
									placeholder='이메일' />
							</div>
						</div>
					</c:if>
					<!-- 내용입력, 저장버튼 -->
					<div class='form-group'>
						<div class="input-group">
							<textarea class="form-control custom-control" name='content'
								style="resize: none; height: 80px"></textarea>
							<span class="input-group-btn">
								<button type="submit" class="btn btn-success"
									style="height: 80px">저장</button>
							</span>
						</div>
					</div>
				</form>

				<!-- 덧글 리스트 -->
				<ul class="media-list" id="comment_list">
				</ul>
			</div>
			<!-- container-fluid 종료 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
	</div>

	<!-- 덧글 항목에 대한 템플릿 참조 -->
	<script id="tmpl_comment_item" type="text/x-handlebars-template">
    <li class="media" style='border-top: 1px dotted #ccc; padding-top: 15px' 
    	id="comment_{{id}}">
        <div class="media-body" style='display: block;'>
            <h5 class="heading clearfix">
                <!-- 작성자,작성일시 -->
                <div class='float-left'>
                    {{writerName}}
                    <small>
                        <a href='mailto:{{email}}'>
                            <i class='fas fa-envelope fa-sm'></i></a>
                            / {{regDate}}
                    </small>
                </div>
                <!-- 수정,삭제 버튼 -->
                <div class='float-right'>
                    <a href='${pageContext.request.contextPath}/bbs/comment_edit.do?comment_id={{id}}' data-toggle="modal" data-target="#comment_edit_modal" class='btn btn-warning btn-xs'>
                        <i class='far fa-edit fa-sm'></i>
                    </a>
                    <a href='${pageContext.request.contextPath}/bbs/comment_delete.do?comment_id={{id}}' data-toggle="modal" data-target="#comment_delete_modal" class='btn btn-danger btn-xs'>
                        <i class='fas fa-eraser fa-sm'></i>
                    </a>
                </div>
            </h5>
            <!-- 내용 -->
            <p>{{{content}}}</p>
        </div>
    </li>
</script>

	<!-- 덧글 삭제시 사용될 Modal -->
	<div class="modal fade" id="comment_delete_modal" tabindex="-1" role="dialog"
					aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm" role="document">
			<div class="modal-content">
				<form id="comment_delete_form" method="post" 
					action="${pageContext.request.contextPath}/bbs/comment_delete_ok.do">
					<input type="hidden" name="comment_id" value="${commentId}" />
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" 
							aria-label="Close"><span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">덧글 삭제</h4>
					</div>
					<div class="modal-body">
						<c:choose>
							<c:when test="${myComment == true}">
								<!-- 자신의 글에 대한 삭제 -->
								<p>정말 이 덧글을 삭제하시겠습니까?</p>
							</c:when>
							<c:otherwise>
								<!-- 자신의 글이 아닌 경우 -->
								<p>삭제하시려면 비밀번호를 입력하세요</p>
								<div class="form-group">
									<input type="password" name="writer_pw" id="writer_pw" 
										class="form-control"/>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
						<button type="submit" class="btn btn-danger">삭제</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- 덧글 수정시 사용될 Modal -->
	<div class="modal fade" id="comment_edit_modal">
		<div class="modal-dialog">
			<div class="modal-content">
			</div>
		</div>
	</div>

	<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/js/book/book_prevent_redundancy/document_read_book_req.js"></script>

	<script type="text/javascript">
		$(function() {
			/** 페이지가 열리면서 동작하도록 이벤트 정의 없이 Ajax요청 */
			$.get("${pageContext.request.contextPath}/bbs/comment_list.do", {
				document_id : "${readDocument.id}"
			}, function(json) {
				if (json.rt != "OK") {
					alert(json.rt);
					return false;
				}

				// 템플릿 HTML을 로드한다.
				var template = Handlebars.compile($("#tmpl_comment_item").html());

				// JSON에 포함된 '&lt;br/&gt;'을 검색에서 <br/>로 변경함.
				// --> 정규표현식 /~~~/g는 문자열 전체의 의미.
				for (var i = 0; i < json.item.length; i++) {
					json.item[i].content = json.item[i].content.replace(
							/&lt;br\/&gt;/g, "<br/>");

					// 덧글 아이템 항목 하나를 템플릿과 결합한다.
					var html = template(json.item[i]);
					// 결합된 결과를 덧글 목록에 추가한다.
					$("#comment_list").append(html);
				}
			});

			/** 덧글 작성 폼의 submit 이벤트 Ajax 구현 */
			// <form>요소의 method, action속성과 <input>태그를
			// Ajax요청으로 자동 구성한다.
			$("#comment_form").ajaxForm(
					function(json) {
						// json은 API에서 표시하는 전체 데이터
						if (json.rt != "OK") {
							alert(json.rt);
							return false;
						}

						// 줄 바꿈에 대한 처리
						// --> 정규표현식 /~~~/g는 문자열 전체의 의미.
						// --> JSON에 포함된 '&lt;br/&gt;'을 검색에서 <br/>로 변경함.
						json.item.content = json.item.content.replace(
								/&lt;br\/&gt;/g, "<br/>");

						// 템플릿 HTML을 로드한다.
						var template = Handlebars.compile($(
								"#tmpl_comment_item").html());
						// JSON에 포함된 작성 결과 데이터를 템플릿에 결합한다.
						var html = template(json.item);
						// 결합된 결과를 덧글 목록에 추가한다.
						$("#comment_list").append(html);
						// 폼 태그의 입력값을 초기화 하기 위해서 reset이벤트를 강제로 호출
						$("#comment_form").trigger('reset');
					});

			/** 모든 모달창의 캐시 방지 처리 */
			$('.modal').on('hidden.bs.modal', function(e) {
				// 모달창 내의 내용을 강제로 지움.
				$(this).removeData('bs.modal');
			});

			/** 동적으로 로드된 폼 안에서의 submit 이벤트 */
			$(document).on('submit', "#comment_delete_form", function(e) {
				e.preventDefault();

				// AjaxForm 플러그인의 강제 호출
				$(this).ajaxSubmit(function(json) {
					if (json.rt != "OK") {
						alert(json.rt);
						return false;
					}

					alert("삭제되었습니다.");
					// modal 강제로 닫기
					$("#comment_delete_modal").modal('hide');

					// JSON 결과에 포함된 덧글일련번호를 사용하여 삭제할 <li>의 id값을 찾는다.
					var comment_id = json.commentId;
					$("#comment_" + comment_id).remove();
				});
			});

			/** 동적으로 로드된 폼 안에서의 submit 이벤트 */
			$(document).on(
					'submit',
					"#comment_edit_form",
					function(e) {
						e.preventDefault();

						// AjaxForm 플러그인의 강제 호출
						$(this).ajaxSubmit(
								function(json) {
									if (json.rt != "OK") {
										alert(json.rt);
										return false;
									}

									// 줄 바꿈에 대한 처리
									// --> 정규표현식 /~~~/g는 문자열 전체의 의미.
									// --> JSON에 포함된 '&lt;br/&gt;'을 검색에서 <br/>로 변경함.
									json.item.content = json.item.content
											.replace(/&lt;br\/&gt;/g, "<br/>");

									// 템플릿 HTML을 로드한다.
									var template = Handlebars.compile($(
											"#tmpl_comment_item").html());
									// JSON에 포함된 작성 결과 데이터를 템플릿에 결합한다.
									var html = template(json.item);
									// 결합된 결과를 기존의 덧글 항목과 교체한다.
									$("#comment_" + json.item.id).replaceWith(
											html);

									// 덧글 수정 모달 강제로 닫기
									$("#comment_edit_modal").modal('hide');
								});
					});
		});
	</script>
	<!-- 아래 template 충돌로 인해, 댓글 작성 등 작동 안됐음. -->
	<%-- <%@ include file="/WEB-INF/inc/script-common.jsp" %> --%>
</body>
</html>