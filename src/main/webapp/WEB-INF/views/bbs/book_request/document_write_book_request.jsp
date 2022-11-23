<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/inc/head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>
	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">

				<h1 class="page-header">${bbsName}
					- <small>도서 요청 새글</small>
				</h1>
				
				<div>
					<form class="form-horizontal" method="post"
						enctype="multipart/form-data"
						action="${pageContext.request.contextPath}/bbs/document_write_ok.do">
	
						<!-- 게시판 카테고리에 대한 상태유지 -->
						<input type="hidden" name="category" value="${category}" />
						
						<div class="form-group">
							<label for="subject" class="col-sm-2 control-label">제목</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="subject"
									name="subject" value="[요청] ${inputApiTitle}" />
							</div>
						</div>
						<div class="form-group">
							<label for="teamDoc" class="col-sm-2 control-label">신청부서</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="teamDoc"
									name="teamDoc" value="" />
							</div>
						</div>
						<div class="form-group">
							<label for="personDoc" class="col-sm-2 control-label">신청자</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="personDoc"
									name="personDoc" value="" />
							</div>
						</div>
						<!-- 내용 -->
						<div class="form-group">
							<label for="content" class="col-sm-2 control-label">내용</label>
							<div class="col-sm-10">
								<textarea id="content" name="content" class="ckeditor">
									제목: <strong>${inputApiTitle}</strong>
									<br/>
									저자: <strong>${inputApiAuthor}</strong>
									<br>
									출판사 : <strong>${inputApiPublisher}</strong>
									<br>
									ISBN : <strong>${inputApiIsbn}</strong>
									<br>
									<br />
									도서 등록 요청드립니다.
																	<br />
								</textarea>
							</div>
						</div>
						<!-- 파일업로드 -->
						<div class="form-group">
							<label for="file" class="col-sm-2 control-label">파일첨부</label>
							<div class="col-sm-10">
								<input type="file" class="form-control" id="file" name="file"
									multiple />
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
			
			</div>
			<!-- container-fluid 종료 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
	</div>
	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>
</html>