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
				<div class="card mb-3">
					<div class="card-header">
						<h4>분류 수정</h4>
					</div>

					<div class="card-body">
						<!-- 가입폼 시작 -->
						<form class="form-horizontal" name="myform" method="post"
							action="${pageContext.request.contextPath}/member/class_member_edit_ok.do">

							<div class="form-group">
								<label for='gradeId' class="col-md-2">분류key(수정불가)</label>
								<div class="col-md-6">
									<input type="text" name="idMbrClass" id="idMbrClass"
										class="form-control" value="${item.idMbrClass}" readonly/>
								</div>
							</div>

							<div class="form-group">
								<label for='className' class="col-md-2">분류명</label>
								<div class="col-md-6">
									<input type="text" name="className" id="className"
										class="form-control" value="${item.className}" />
								</div>
							</div>

							<div class="form-group">
								<div class="col-md-offset-2 col-md-6">
									<button type="submit" class="btn btn-primary">수정하기</button>
									<a class="btn btn-danger" href="#"
										data-toggle="modal" data-target="#delete_class_member_modal">삭제하기</a>
									<a class="btn btn-warning" href="${pageContext.request.contextPath}/member/class_member_list.do">목록으로</a>
								</div>
							</div>
						</form>
						<!-- 가입폼 끝 -->
					</div>
					<!-- card body 끝 -->
				</div>
				<!-- card 끝 -->
			</div>
			<!-- container fluid 끝 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
		<!-- content wrapper 끝 -->
	</div>
	<!-- wrapper 끝 -->
	
	<!-- 삭제 모달 -->
	<div class="modal fade" id="delete_class_member_modal" tabindex="-1" role="dialog"
					aria-labelledby="exampleModalLabel" aria-hidden="true">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="exampleModalLabel">분류 정보를 삭제 하시겠습니까?</h5>
								<button class="close" type="button" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">×</span>
								</button>
							</div>
							<form name="grade_delete" method="post">
							<input type="hidden" name="idMbrClass" value="${item.idMbrClass}"/>
							<div class="modal-body">
								<div>회원 분류 정보를 삭제합니다.</div>
								<div>삭제 후 복구 불가능합니다.</div>
							</div>
							<div class="modal-footer">
								<button class="btn btn-secondary" type="button"
									data-dismiss="modal">취소</button>
								<input type="submit" class="btn btn-danger" value="삭제" formaction="${pageContext.request.contextPath}/member/class_member_delete_ok.do"/>
							</div>
							</form>
						</div>
					</div>
				</div>

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>
</html>