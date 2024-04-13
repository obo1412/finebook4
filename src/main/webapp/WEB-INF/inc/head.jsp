<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<!-- http를 사용할때 발생하는 문제들 해결해주는 선언 -->
<meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
<!-- Twitter Bootstrap3 & jQuery -->
<!-- <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.5/css/bulma.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" />
<script src="http://code.jquery.com/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script> -->
<script src="http://code.jquery.com/jquery.min.js"></script>
<!-- QR Code -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/qrcode/jquery.qrcode.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/qrcode/qrcode.js"></script>

<!-- Bootstrap4 Local Page level plugin CSS-->
<link href="${pageContext.request.contextPath}/assets/vendor/datatables/dataTables.bootstrap4.css" rel="stylesheet">

<!-- Font Awesome CSS icon 온라인 링크, 아래 주석처리 여기선 불필요 -->
<!-- <script src="https://use.fontawesome.com/releases/v5.2.0/js/all.js"></script> -->
<!-- Custom fonts for this template-->
<link href="${pageContext.request.contextPath}/assets/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">

<!-- Custom styles for this template-->
<link href="${pageContext.request.contextPath}/assets/css/sb-admin-css/sb-admin.css" rel="stylesheet">

	
<!-- 공통 CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/common.css" />

<!-- 다음 우편번호 검색 스크립트 -->
<%-- <script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/daumPostCode.js"></script> --%>
<!-- 회원가입페이지에서만 사용. -->

<!-- CKEditor -->
<!-- <script src="http://cdn.ckeditor.com/4.4.7/standard/ckeditor.js"></script> -->

<!-- Multi-column -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/plugins/multi-column/ie-row-fix.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/plugins/multi-column/multi-columns-row.css" />

<!-- handlebars -->
<script src="${pageContext.request.contextPath}/assets/plugins/handlebars/handlebars-v4.0.5.js"></script>

<!-- ajax -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/plugins/ajax/ajax_helper.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/plugins/ajax/ajax_helper.js"></script>

<!-- ajaxForm -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/plugins/ajax-form/jquery.form.min.js"></script>
