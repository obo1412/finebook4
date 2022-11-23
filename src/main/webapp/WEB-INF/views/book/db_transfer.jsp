<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/inc/head.jsp" %>
</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>
	<div id="wrapper">
		<%@include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="card card-body">
				<form class="form-horizontal" method="post" enctype="multipart/form-data"
					action="${pageContext.request.contextPath}/book/db_transfer_ok.do">
					<!-- 버튼들 -->
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10" align="center">
							<br/>
							<button type="submit" class="btn btn-primary">실행 !!</button>
							<br/>
						</div>
					</div>
				</form>
			</div><!-- card card-body 끝 -->

<%@ include file="/WEB-INF/inc/footer.jsp" %>
		</div>
	</div>
</body>
</html>