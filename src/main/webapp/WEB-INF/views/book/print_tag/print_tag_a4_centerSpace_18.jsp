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

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/book/print_label/a4_centerSpace_18.css" />
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
		<span class="prtNone" style="font-size:30pt;">A4 - CenterSpace</span>
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
		
		<c:forEach var="item" items="${bookHeldList}" varStatus="status">
			
			<div class="item__box">
				<span class="div__title">
					<span>
						${item.title}
					</span>
				</span>
				<div class="data__box">
					<div class="left__box">
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
					<div class="right__box">
						<div class="r__in__left">
							<img src="${pageContext.request.contextPath}/filesMapping/upload/finebook4/qrcode/libNo${loginInfo.idLibMng}/${item.localIdBarcode}.png" alt="">
						</div>
						<div class="r__in__center">
							<span>
								<div class="code__box">
								
									<c:choose>
										<c:when test="${not empty item.additionalCode}">
											<div class="addi__code">${item.additionalCode}</div>
										</c:when>
										<c:otherwise>
											<div class="div__empty__data">-</div>
										</c:otherwise>
									</c:choose>
									
									<c:choose>
										<c:when test="${not empty item.classificationCode}">
											<div class="class__code">${item.classificationCode}</div>
										</c:when>
										<c:otherwise>
											<div class="div__empty__data">-</div>
										</c:otherwise>
									</c:choose>
									
									<c:choose>
										<c:when test="${not empty item.authorCode}">
											<div class="author__code">${item.authorCode}</div>
										</c:when>
										<c:otherwise>
											<div class="div__empty__data">-</div>
										</c:otherwise>
									</c:choose>
									
									<c:choose>
										<c:when test="${not empty item.volumeCode}">
											<div class="volume__code">V${item.volumeCode}</div>
										</c:when>
										<c:otherwise>
											<div class="div__empty__data">-</div>
										</c:otherwise>
									</c:choose>
									
									<c:choose>
										<c:when test="${(item.copyCode gt 0) and (not empty item.copyCode)}">
											<div class="copy__code">C${item.copyCode}</div>
										</c:when>
										<c:otherwise>
											<div class="div__empty__data">-</div>
										</c:otherwise>
									</c:choose>
								</div>
								
								<div class="barcode">${item.localIdBarcode}</div>
							</span>
						</div>
						<div class="r__in__right" style="background-color:${item.classCodeColor};">
							<span>
								<c:set var="classCode" value="${item.classCodeHead}" />
									<c:choose>
										<c:when test="${classCode lt 0}">
											<c:set var="classCode" value="" />
											${classCode}
										</c:when>
										<c:when test="${classCode eq 0}">
											<c:set var="classCode" value="000" />
											${classCode}
										</c:when>
										<c:otherwise>
											${classCode}
										</c:otherwise>
									</c:choose>
							</span>
						</div>
					</div>
				</div>
			</div>
			
			<c:if test="${(((status.count+startPoint-1) mod 18) eq 0) and ((fn:length(bookHeldList)+startPoint-1) gt 18)}">
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