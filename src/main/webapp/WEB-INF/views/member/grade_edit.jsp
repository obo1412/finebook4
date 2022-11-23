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
						<h4>등급 정보/수정</h4>
					</div>

					<div class="card-body">
						<!-- 가입폼 시작 -->
						<form class="form-horizontal" name="myform" method="post"
							action="${pageContext.request.contextPath}/member/grade_edit_ok.do">

							<div class="form-group">
								<label for='gradeId' class="col-md-2">번호</label>
								<div class="col-md-6">
									<input type="text" name="gradeId" id="gradeId"
										class="form-control" value="${item.gradeId}" readonly/>
								</div>
							</div>
							
							<div class="form-group">
								<c:set var="std" value=""/>
								<c:if test="${item.standard eq 1}">
									<c:set var="std" value="checked" />
								</c:if>
								<label class="col-md-2"><input type="checkbox" name="standard" value="1"
									${std}>
									기준등급
								</label>
							</div>

							<div class="form-group">
								<label for='gradeName' class="col-md-2">등급 이름</label>
								<div class="col-md-6">
									<input type="text" name="gradeName" id="gradeName"
										class="form-control" value="${item.gradeName}" />
								</div>
							</div>
							
							<div class="form-group">
								<label for='brwLimit' class="col-md-2">대출가능권수</label>
								<div class="col-md-6">
									<input type="text" name="brwLimit" id="brwLimit"
										class="form-control" value="${item.brwLimit}" />
								</div>
							</div>
							
							<div class="form-group">
								<label for='dateLimit' class="col-md-2">대출기한</label>
								<div class="col-md-6">
									<input type="text" name="dateLimit" id="dateLimit"
										class="form-control" value="${item.dateLimit}" />
								</div>
							</div>

							<div class="form-group">
								<div class="col-md-offset-2 col-md-6">
									<button type="submit" class="btn btn-primary">수정하기</button>
									<a class="btn btn-danger" href="#"
										data-toggle="modal" data-target="#delete_grade_modal">삭제하기</a>
									<a class="btn btn-warning" href="${pageContext.request.contextPath}/member/grade_list.do">목록으로</a>
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
	<div class="modal fade" id="delete_grade_modal" tabindex="-1" role="dialog"
					aria-labelledby="exampleModalLabel" aria-hidden="true">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="exampleModalLabel">등급 정보를 삭제 하시겠습니까?</h5>
								<button class="close" type="button" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">×</span>
								</button>
							</div>
							<form name="grade_delete" method="post">
							<input type="hidden" name="gradeId" value="${item.gradeId}"/>
							<div class="modal-body">
								<div>회원 등급 정보를 삭제합니다.</div>
								<div>복구는 불가능하며,</div>
								<div>삭제되는 등급의 회원은 기준등급으로 변경됩니다.</div>
							</div>
							<div class="modal-footer">
								<button class="btn btn-secondary" type="button"
									data-dismiss="modal">취소</button>
								<input type="submit" class="btn btn-danger closeRefresh" value="삭제" formaction="${pageContext.request.contextPath}/member/grade_delete_ok.do"/>
							</div>
							</form>
						</div>
					</div>
				</div>

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>
</html>