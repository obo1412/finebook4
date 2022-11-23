<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true"%>
<!-- 공통 스크립트 영역 -->

   <!-- Bootstrap core JavaScript-->
  <script src="${pageContext.request.contextPath}/assets/vendor/jquery/jquery.min.js"></script>
  <script src="${pageContext.request.contextPath}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

  <!-- Core plugin JavaScript-->
  <script src="${pageContext.request.contextPath}/assets/vendor/jquery-easing/jquery.easing.min.js"></script>

  <!-- Page level plugin JavaScript-->
  <script src="${pageContext.request.contextPath}/assets/vendor/datatables/jquery.dataTables.js"></script>
  <script src="${pageContext.request.contextPath}/assets/vendor/datatables/dataTables.bootstrap4.js"></script>

  <!-- Custom scripts for all pages-->
  <script src="${pageContext.request.contextPath}/assets/js/sb-admin-js/sb-admin.min.js"></script>

<script type="text/javascript">

function clearInput() {
	//텍스트박스 지우기
	/* var el = document.getElementsByClassName("input-clear");

	for(var i=0; i<el.length; i++){
		el[i].value = '';
	} /*
	
	//체크박스 지우기
	/* var el = document.getElementsByClassName("input-radio");
	for(var i=0; i<el.length; i++){
		el[i].checked = false;
	}
	
	$("#bookCover").attr("src", ''); */
	
	window.location.reload();
}


function goBackPage() {
	window.history.back();
}

//member view또는 book_held_view 등에서 쓰이는 버튼 닫으면서 새로고침
$(".closeRefresh").click(function(){
	opener.location.href=opener.document.URL;
	window.close();
});

//페이지 호출되면 url 체크해서 popup창 닫기
window.addEventListener('load', function() {
	var curUrl = window.location.search;
	//console.log(curUrl);
	let params = curUrl.split("&");
	for(var i=0; i<params.length; i++) {
		if(params[i].indexOf('deleteClose') > -1) {
			console.log('삭제성공');
			opener.location.reload();
			window.close();
		}
	}
});


</script>
<!--// 공통 스크립트 영역 -->