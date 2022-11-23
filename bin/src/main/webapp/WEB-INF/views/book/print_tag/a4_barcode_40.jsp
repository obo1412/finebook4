<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/book/print_label/a4_barcode_40.css" />
</head>
<body>

	<div class="prtNone">
		<span class="prtNone" style="font-size:30pt;">바코드 40칸</span>
		<span class="prtNone">
			<button class="btn btn-danger prtNone" id="btn-print">인쇄</button>
		</span>
	</div>
	
	
	<div class="whole__box">
		<!-- 최초1회 a4 시작 위치 조정 -->
		<span class="start__gap__box">
		</span>
		<!-- 시작위치 이동용 더미 -->
		<c:if test="${startPoint gt 1}">
			<c:forEach var="i" begin="1" end="${startPoint-1}">
				<div class="item__box"><!-- float left를 사용해서 나란히 놓기 -->
				</div>
			</c:forEach>
		</c:if>
		<!-- 라벨 출력 시작 -->
		<c:forEach var="item" items="${bookHeldList}" varStatus="status">
			<div class="item__box">
				<div class="name__lib">
					<c:choose>
						<c:when test="${tag.libNameInbarcode eq 1}">
							${item.nameLib}
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
				</div>
				<div class="barcode__on">*${item.localIdBarcode}*</div>
				<div class="barcode__num">${item.localIdBarcode}</div>
			</div>
			<c:if test="${(((status.count+startPoint-1) mod 40) eq 0) and ((fn:length(bookHeldList)+startPoint-1) gt 40)}">
				<span class="over__page__sheet__gap"></span>
			</c:if>
		</c:forEach>
	</div>

</body>



<script type="text/javascript">

	$(function() {
		$("#btn-print").click(function() {
			if (navigator.userAgent.indexOf("MSIE") > 0) {
				printPage();
			} else if (navigator.userAgent.indexOf("Chrome") > 0) {
				window.print();
			}
		});
	});
	
</script>
</html>