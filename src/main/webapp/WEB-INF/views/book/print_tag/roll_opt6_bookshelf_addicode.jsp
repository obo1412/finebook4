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
	
	<link href="${pageContext.request.contextPath}/assets/css/book/print_label/roll_common.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/assets/css/book/print_label/roll_opt6_bookshelf_addicode.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
	:root {
		--a4-fontsize-namelib: ${tag.a4FontSizeNamelib}mm;
		--a4-fontsize-barcode: ${tag.a4FontSizeBarcode}mm;
		--a4-fontsize-barcode-num: ${tag.a4FontSizeBarcodeNum}mm;
		--a4-fontsize-code-barcode: ${tag.a4FontSizeCodeBarcode}mm;
		--a4-fontsize-codes: ${tag.a4FontSizeCodes}mm;
		--a4-fontsize-class-num: ${tag.a4FontSizeClassNum}mm;
	}
	
	@media print {
		@page {
			/* size: 98mm; */ /* roll size */
			margin: 0;
		}
		html, body {
			border: 0;
			margin: 0;
			padding: 0;
			font-family: '굴림';
			box-sizing: border-box;
		}
		.sidebar {
			display: none;
		}
		.sticky-footer {
			display: none;
		}
		.prtNone {
			display: none;
		}
		
	}
	</style>
</head>
<body>
	<div class="prtNone">
		<span class="prtNone" style="font-size:30pt;">롤프린터 태그 - 옵션6 - 서가&별치기호</span>
		<span class="prtNone">${fn:length(bookHeldList)}</span>
		<span class="prtNone">
			<button class="btn btn-danger prtNone" id="btn-print">인쇄</button>
		</span>
	</div>

	<c:forEach var="item" items="${bookHeldList}" varStatus="status">
		<div style="width:103mm; height:34mm;">
			<div style="padding-left:1mm; padding-top:1mm; width:100%; height:100%;">
				<div class="bookTitleSection" style="padding-left:2.5mm; padding-top:${tempTitleMt}mm; width:90%; height:3mm; font-size:2mm; overflow:hidden;">
					${item.title}
				</div>
				<div class="tagSection" style="padding-top:${tag.titleTagGap}mm; width:100%; height:30mm;">
					<div class="tagLeftSection" style="width:48%; height:100%; float:left; text-align:center;">
						<div class="left__nameLib" style="width:100%; height:35%; font-weight:900; line-height:12mm; white-space:nowrap;">
							<c:choose>
								<c:when test="${tag.libNameInbarcode eq 1}">
									${item.nameLib}
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
						</div>
						
						<%-- <div style="height:35%; font-family:'Free 3 of 9'; font-size:10mm; line-height:10mm; transform:scale(1,1.5);">
							*${item.localIdBarcode}*
						</div> --%>
						<c:choose>
							<c:when test="${fn:length(item.localIdBarcode) gt 8}">
								<div class="barcode__on" style="transform:scale(0.85,1.5);">*${item.localIdBarcode}*</div>
							</c:when>
							<c:otherwise>
								<div class="barcode__on">*${item.localIdBarcode}*</div>
							</c:otherwise>
						</c:choose>
						
						<div style="padding-top:1mm; font-weight:bold; font-size:4mm;">
							${item.localIdBarcode}
						</div>
					</div>
					<div class="tagRightSection" style="width:50mm; height:100%; float:right;">
						<div class="qrBox" style="background-color:${item.classCodeColor}; float:left; width:16mm; height:100%;">
							<%-- <div id="qrImg" style="float:left; width:10mm;">
								<img style="width:8mm; margin-top:5mm;" src="${pageContext.request.contextPath}/filesMapping/upload/finebook4/qrcode/libNo${loginInfo.idLibMng}/${item.localIdBarcode}.png" />
							</div> --%>
							<div class="lib__name__fixed" style="transform:translate(0mm, 11mm) rotate(90deg);">
								소풍마루
							</div>
						</div>
						<div class="callNoBox" style="float:right; width:33mm; height:100%;">
							<div class="callText" style="float:left; width:17mm; height:100%; font-size:3mm; line-height:3mm; transform:rotate(90deg);">
								
								<div class="code__box" style="transform:translate(0mm,12.5mm);">
									
									<c:choose>
										<c:when test="${not empty item.authorCode}">
											<div class="author__code">${item.authorCode}</div>
										</c:when>
										<c:otherwise>
											<div class="div__empty__data">-</div>
										</c:otherwise>
									</c:choose>
									
									<%-- <c:choose>
										<c:when test="${not empty item.additionalCode}">
											<div class="addi__code">${item.additionalCode}</div>
										</c:when>
										<c:otherwise>
											<div class="div__empty__data">-</div>
										</c:otherwise>
									</c:choose> --%>
									
								</div>
								
							</div>
							
							<div class="callNo " style="background-color:${item.classCodeColor}; float:right; width:16mm; height:100%;">
								<c:choose>
									<c:when test="${fn:length(item.additionalCode) gt 4}">
										<div class="color__addi__code text__digit__5">
											${item.additionalCode}
										</div>
									</c:when>
									<c:when test="${fn:length(item.additionalCode) gt 3}">
										<div class="color__addi__code text__digit__4">
											${item.additionalCode}
										</div>
									</c:when>
									<c:when test="${fn:length(item.additionalCode) gt 1}">
										<div class="color__addi__code text__digit__2">
											${item.additionalCode}
										</div>
									</c:when>
									<c:otherwise>
										<div class="color__addi__code text__digit__1">
											${item.additionalCode}
										</div>
									</c:otherwise>
								</c:choose>
							</div>
							
						</div>
					</div>
				</div><!-- tagSection 끝 -->
			</div>
		</div>
		<div style="height:${tag.tagGap}mm;">
		</div>
	</c:forEach>
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
	
	window.addEventListener('load', ()=>{
		console.log(localStorage.getItem("colorArr"));
	});
	
</script>
</html>