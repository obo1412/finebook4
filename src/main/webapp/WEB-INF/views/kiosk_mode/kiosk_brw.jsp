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
	<link href="/assets/css/kiosk-mode/kiosk_brw.css" rel="stylesheet" >
	<style type="text/css">
	</style>
</head>
<body>

    <div class="div__brw__wrapper">
      <div class="btn__return__kiosk__main">
        <a class="a__btn__return__main" href="/kiosk_mode.do">
          <i class="far fa-arrow-alt-circle-left text-white icon__back"></i>
        </a>
        <span id="timer" class="timer__num"></span>
        <div class="div__user__data"></div>
      </div>

      <div class="div__brw">
        <div class="div__user">
          <div class="div__user__inputs">
            <label for="userBarcode">회원번호</label>
            <input
              type="text"
              id="userBarcode"
              onkeydown="enterGetMemberInfo()"
            />
            <button onclick="getMemberInfo()">입력</button>
          </div>
        </div>
        <span class="msg__no__member hidden">회원정보를 입력해주세요.</span>

        <div class="div__book hidden">
          <div class="div__book__input">
            <label for="bookBarcode">도서등록번호</label>
            <input
              type="text"
              id="bookBarcode"
              onkeydown="enterGetBookInfo()"
            />
            <button onclick="getBookInfo()">도서입력</button>
          </div>
          <span class="msg__already__scan hidden">이미 입력된 도서입니다.</span>
          <span class="msg__no__book hidden">대출할 도서를 입력해주세요.</span>
          <span class="msg__over__book__brw hidden">대출 가능권수 한도를 초과했습니다.</span>
          <div class="div__book__data__list"></div>

          <div>
            <button class="btn__brw__ok" onclick="btnClickedBrw()">
              대출하기
            </button>
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
	src="${pageContext.request.contextPath}/assets/js/kiosk-mode/kiosk_brw.js"></script>

</html>