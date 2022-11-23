<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/inc/head.jsp"%>

<link href="/assets/css/user-self/user_self.css" rel="stylesheet" >
<style type="text/css">
</style>

</head>
<body>

	<!-- wrapper -->
	<div>
		<!-- image section -->
		<div class="wrap">
			<div class="img_wrap">
				<div>
					<a href="#" onclick="backToManagerPage()">돌아가기</a>
					<img src="/assets/img/user_self_page/user_self_main_img.jpg" alt="img_user_self_main">
				</div>
			</div>

			<!-- image section -->
			<!-- button section -->
			<div class="list">
				<div>
					<button class="menu btn btn-secondary">목록</button>
					<button class="return btn btn-secondary">대출/반납</button>
					<button class="inquiry btn btn-secondary">조회</button>
				</div>
			</div>
			<!-- button section -->
		</div>
	</div>
	<!-- wrapper 끝 -->

</body>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/js/user-self/user_self.js"></script>

</html>