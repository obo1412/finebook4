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
						<h4>등급 추가</h4>
					</div>

					<div class="card-body">
						<!-- 가입폼 시작 -->
						<form class="form-horizontal" name="myform" method="post"
							action="${pageContext.request.contextPath}/member/grade_add_ok.do">

							<div class="form-group">
								<label for='gradeName' class="col-md-2">등급 이름</label>
								<div class="col-md-6 input-group">
									<input type="text" name="gradeName" id="gradeName"
										class="form-control" value="${item.gradeName}" />
								</div>
							</div>

							<div class="form-group">
								<label for='brwLimit' class="col-md-2">대여가능권수</label>
								<div class="col-md-6">
									<input type="text" name="brwLimit" id="brwLimit"
										class="form-control" value="${item.brwLimit}" />
								</div>
							</div>

							<div class="form-group">
								<label for='dateLimit' class="col-md-2">대여기한</label>
								<div class="col-md-6">
									<input type="text" name="dateLimit" id="dateLimit"
										class="form-control" value="${item.dateLimit}" />
								</div>
							</div>

							<div class="form-group">
								<div class="col-md-offset-2 col-md-6">
									<button type="submit" class="btn btn-primary">추가하기</button>
									<button type="reset" class="btn btn-danger">다시작성</button>
									<a class="btn btn-warning"
										href="${pageContext.request.contextPath}/member/grade_list.do">목록으로</a>
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

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>
</html>