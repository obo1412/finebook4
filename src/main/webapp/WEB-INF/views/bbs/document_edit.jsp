<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/inc/head.jsp"%>
<%@ include file="/WEB-INF/inc/board_editor.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>
	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">
				<h1 class="page-header">${bbsName}
					- <small>수정하기</small>
				</h1>

				<form class="form-horizontal" method="post"
					enctype="multipart/form-data"
					action="${pageContext.request.contextPath}/bbs/document_edit_ok.do">

					<!-- 게시판 카테고리에 대한 상태유지 -->
					<input type="hidden" name="category" value="${category}" />
					<!-- 수정 대상에 대한 상태유지 -->
					<input type="hidden" name="document_id" value="${readDocument.id}" />

					<!-- 작성자,비밀번호,이메일은 자신의 글을 수정하는 경우만 생략한다. -->
					<c:if
						test="${loginInfo == null || loginInfo.idMng != readDocument.managerId}">
						<!-- 작성자 -->
						<div class="form-group">
							<label for="writer_name" class="col-sm-2 control-label">작성자</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="writer_name"
									name="writer_name" value="${readDocument.writerName}">
							</div>
						</div>
						<!-- 비밀번호 -->
						<div class="form-group">
							<label for="writer_pw" class="col-sm-2 control-label">비밀번호</label>
							<div class="col-sm-10">
								<input type="password" class="form-control" id="writer_pw"
									name="writer_pw" placeholder="글 작성시 설정하신 비밀번호를 입력하세요." />
							</div>
						</div>
						<!-- 이메일 -->
						<div class="form-group">
							<label for="email" class="col-sm-2 control-label"> 이메일</label>
							<div class="col-sm-10">
								<input type="email" class="form-control" id="email" name="email"
									value="${readDocument.email}" />
							</div>
						</div>
					</c:if>
					<!-- 제목 -->
					<div class="form-group">
						<label for="subject" class="col-sm-2 control-label">제목</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="subject"
								name="subject" value="${readDocument.subject}" />
						</div>
					</div>
					<!-- 내용 -->
					<div class="form-group">
						<label for="content" class="col-sm-2 control-label">내용</label>
						<div class="col-sm-10">
							<textarea id="content" name="content" class="ckeditor">${readDocument.content}</textarea>
						</div>
					</div>
					<!-- 파일업로드 -->
					<div class="form-group">
						<label for="file" class="col-sm-2 control-label">파일첨부</label>
						<div class="col-sm-10">
							<input type="file" class="form-control" id="file" name="file"
								multiple />

							<!-- 첨부파일 리스트가 존재할 경우만 삭제할 항목 선택 가능 -->
							<c:if test="${fileList != null}">
								<c:forEach var="file" items="${fileList}">
									<!-- 이미지를 다운받기 위한 URL 구성 -->
									<c:url value="/download.do" var="downloadUrl">
										<c:param name="file" value="${file.fileDir}/${file.fileName}" />
									</c:url>

									<div class="checkbox">
										<label> <input type="checkbox" name="del_file"
											id="img_del" value="${file.id}" /> ${file.originName} 삭제하기 <a
											href="${downloadUrl}">[다운받기]</a>
										</label>
									</div>
								</c:forEach>
							</c:if>
						</div>
					</div>

					<!-- 버튼들 -->
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-primary">저장하기</button>
							<button type="button" class="btn btn-danger"
								onclick="history.back();">작성취소</button>
						</div>
					</div>

				</form>
			</div>
			<!-- container-fluid 종료 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
	</div>
	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>
</html>