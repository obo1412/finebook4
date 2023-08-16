<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/fonts.css" />
	<link rel="preload" href="${pageContext.request.contextPath}/assets/fonts/free3of9.ttf" as="font" crossorigin > --%>
<%-- <%@ include file="/WEB-INF/inc/head.jsp"%> --%>
<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<%-- <script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/js/barcode/jquery-barcode.js"></script> --%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/member/print_membership/print_membership_ver2.css" />
<style type="text/css">
	:root {
		--a4-fontsize-namelib: ${tag.a4FontSizeNamelib}mm;
		--a4-fontsize-barcode: ${tag.a4FontSizeBarcode}mm;
		--a4-fontsize-barcode-num: ${tag.a4FontSizeBarcodeNum}mm;
		--a4-fontsize-code-barcode: ${tag.a4FontSizeCodeBarcode}mm;
		--a4-fontsize-codes: ${tag.a4FontSizeCodes}mm;
		--a4-fontsize-class-num: ${tag.a4FontSizeClassNum}mm;
	}
</style>
</head>
<body>
	
	<div class="prtNone">
		<span class="prtNone" style="font-size:30pt;">회원증 - 18칸통합라벨 ver.2</span>
		<span class="prtNone">
			<button class="btn btn-danger prtNone" id="btn-print">인쇄</button>
		</span>
	</div>
	
	<!-- 최초1회 margin top -->
	<div class="content__box">
		<span class="start__box">
		</span>
		
		<c:if test="${startPoint gt 1}">
			<c:forEach var="i" begin="1" end="${startPoint-1}">
				<div class="item__box"><!-- float left를 사용해서 나란히 놓기 -->
				</div>
			</c:forEach>
		</c:if>
		
		<c:forEach var="item" items="${memberList}" varStatus="status">
		
			<c:choose>
				<c:when test="${(status.count mod 4) eq 1}">
					<div style="float:left; width:4mm; height:31mm;"></div>
				</c:when>
				<c:when test="${(status.count mod 4) eq 2}">
					<div style="float:left; width:1mm; height:31mm;"></div>
				</c:when>
				<c:when test="${(status.count mod 4) eq 3}">
					<div style="float:left; width:6mm; height:31mm;"></div>
				</c:when>
				<c:otherwise>
					<div style="float:left; width:1mm; height:31mm;"></div>
				</c:otherwise>
			</c:choose>
			
			<div class="item__box">
				<span class="div__title">
					<!-- 초기 빈공간 맞추기 용 -->
				</span>
				
				<div class="data__box">
					<div class="left__box">
						<div class="name__lib">
							${item.name}
						</div>
						<div class="barcode__on">*${item.barcodeMbr}*</div>
						<div class="barcode__num">${item.barcodeMbr}</div>
					</div>
				</div>
			</div>
			
			<c:if test="${(status.count mod 4) eq 0}">
		    <div style="clear:both;"></div>
		  </c:if>
			
			<%-- <c:if test="${(((status.count+startPoint-1) mod 36) eq 0) and ((fn:length(memberList)+startPoint-1) gt 36)}">
				<span class="over__page__sheet__gap"></span>
			</c:if> --%>
			<c:if test="${(((status.count) mod 36) eq 0) and ((fn:length(memberList)) gt 36)}">
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