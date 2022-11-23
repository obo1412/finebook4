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
	<link href="/assets/css/kiosk-mode/kiosk_return.css" rel="stylesheet" >
	<style type="text/css">
	</style>
</head>
<body>

	<div class="div__brw__wrapper">
		<div>
			<a class="btn__return__kiosk__main" href="/kiosk_mode.do">
				<i class="far fa-arrow-alt-circle-left text-white icon__back"></i>
			</a>
			<span id="timer" class="timer__num"></span>
		</div>
		
		<div class="div__brw">
			
			<div class="div__book">
				<div class="div__book__input">
					<label for="">반납 도서</label>
					<input type="text" id="bookBarcode" onkeydown="enterRetBookInfo()" />
					<button onclick="returnBookInfo()">반납</button>
				</div>
				<span class="msg__already__scan hidden">반납 처리된 도서입니다.</span>
				<span class="msg__no__book hidden">반납할 도서를 입력해주세요.</span>
				<div class="div__book__data__list">
					
				</div>
			</div>
			
			<div class="msg__success__brw hidden">
				<div>정상 대출처리 되었습니다.</div>
			</div>
			
		</div>
	</div>

</body>

<!-- <script type="text/javascript" src="/assets/js/date_handler.js"></script> -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/js/kiosk-mode/kiosk_return.js"></script>

</html>