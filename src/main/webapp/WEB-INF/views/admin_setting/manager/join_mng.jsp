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

				<h1>관리자 회원가입</h1>

				<!-- 가입폼 시작 -->
				<form class="form-horizontal" name="myform" method="post"
					enctype="multipart/form-data"
					action="${pageContext.request.contextPath}/managerFinebook/join_ok_mng.do">

					<div class="form-group">
						<label for='userid' class="col-md-2">아이디*</label>
						<div class="col-md-4">
							<input type="text" name="user_id" id="user_id"
								class="form-control" />
						</div>
					</div>

					<div class="form-group">
						<label for='"password"' class="col-md-2">비밀번호*</label>
						<div class="col-md-4">
							<input type="password" name="user_pw" id="user_pw"
								class="form-control" />
						</div>
					</div>

					<div class="form-group">
						<label for='password_re' class="col-md-2">비밀번호 확인*</label>
						<div class="col-md-4">
							<input type="password" name="user_pw_re" id="user_pw_re"
								class="form-control" />
						</div>
					</div>

					<div class="form-group">
						<label for='email' class="col-md-2">이메일*</label>
						<div class="col-md-4">
							<input type="email" name="email" id="email" class="form-control" />
						</div>
					</div>

					<div class="form-group">
						<label for='name' class="col-md-2">이름*</label>
						<div class="col-md-4">
							<input type="text" name="name" id="name" class="form-control" />
						</div>
					</div>

					<div class="form-group">
						<label for='library_name' class="col-md-2">도서관 이름*</label>
						<div class="col-md-4">
							<select name="idLib" class="form-control">
								<option value="">--도서관이름을 선택하세요--</option>
								<c:forEach var="l" items="${libList}">
									<option value="${l.idLib}">(${l.locLib}) ${l.nameLib}</option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div class="form-group">
						<div class="col-md-offset-2 col-md-10">
							<button type="submit" class="btn btn-primary">가입하기</button>
							<button type="reset" class="btn btn-danger">다시작성</button>
						</div>
					</div>
				</form>
				<!-- 가입폼 끝 -->
			</div>
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
	</div>

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>
</html>