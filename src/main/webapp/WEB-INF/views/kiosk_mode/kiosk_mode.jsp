<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
	<%@ include file="/WEB-INF/inc/head.jsp"%>
	<link href="/assets/css/kiosk-mode/kiosk_main.css" rel="stylesheet" >
	<style type="text/css">
	</style>
</head>
<body>

	<!-- wrapper -->
	<div>
		<button class="btn__kiosk" onclick="btnclicked()">키오스크</button>
	</div>
	
	<div class="div__main__btn">
		<a class="a__btn" href="/kiosk_mode/kiosk_brw.do">
			<div class="btn__big__brw__rtn div__btn__brw">
				<span class="btn__text__big">
					대출
				</span>
			</div>
		</a>
		<a class="a__btn" href="/kiosk_mode/kiosk_return.do">
			<div class="btn__big__brw__rtn div__btn__return">
				<span class="btn__text__big">
					반납
				</span>
			</div>
		</a>
	</div>
	<!-- wrapper 끝 -->

</body>

<!-- <script type="text/javascript" src="/assets/js/date_handler.js"></script> -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/js/kiosk-mode/kiosk_main.js"></script>

</html>