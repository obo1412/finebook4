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
	<style type="text/css">
	:root {
		--a4-fontsize-namelib: ${tag.a4FontSizeNamelib}mm;
		--a4-fontsize-barcode: ${tag.a4FontSizeBarcode}mm;
		--a4-fontsize-barcode-num: ${tag.a4FontSizeBarcodeNum}mm;
		--a4-fontsize-code-barcode: ${tag.a4FontSizeCodeBarcode}mm;
		--a4-fontsize-codes: ${tag.a4FontSizeCodes}mm;
		--a4-fontsize-class-num: ${tag.a4FontSizeClassNum}mm;
	}
	
	@media all {
		@page {
			/* size: 98mm; */ /* roll size */
			margin: 0;
		}
		html, body {
			border: 0;
			margin: 0;
			padding: 0;
			font-family: '굴림';
			font-weight: 800;
			box-sizing: border-box;
		}
		
		.font-weight-normal {
			font-weight: normal;
		}
		
		img {
			image-rendering: pixelated;
		}
	}
	
	@media print {
	
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
		<span class="prtNone" style="font-size:30pt;">롤프린터 옵션2 - 흑백프린터</span>
		<span class="prtNone">
			<button class="btn btn-danger prtNone" id="btn-print">인쇄</button>
		</span>
	</div>

	<c:forEach var="item" items="${bookHeldList}" varStatus="status">
		<div style="width:95mm; height:36.3mm;">
			<div style="padding-left:3mm; padding-top:0mm; width:100%; height:100%;">
				<div class="tagSection" style="padding-top:0mm; width:100%; height:30mm;">
					<div class="tagLeftSection" style="width:50%; height:100%; float:left; text-align:center;">
						<div class="left__nameLib" style="width:100%; height:35%; margin-top:2mm; font-weight:900;
							line-height:12mm; white-space:nowrap;  letter-spacing:-2px;">
							<c:choose>
								<c:when test="${tag.libNameInbarcode eq 1}">
									${item.nameLib}
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
						</div>
						<div style="height:35%; font-family:'Free 3 of 9'; font-size:10mm; line-height:10mm; transform:scale(1,1.5);"
							class="font-weight-normal">
							*${item.localIdBarcode}*
						</div>
						<div style="padding-top:0mm; font-weight:bold; font-size:3mm;">
							${item.localIdBarcode}
						</div>
					</div>
					<div class="tagRightSection" style="width:47mm; height:100%; float:right;">
						<div class="qrBox" style="float:left; width:14mm; height:100%;">
							<div id="qrImg" style="float:left; width:11mm;">
								<img style="width:8mm; margin-top:5mm;" src="${pageContext.request.contextPath}/filesMapping/upload/finebook4/qrcode/libNo${loginInfo.idLibMng}/${item.localIdBarcode}.png" />
							</div>
							<div style="float:left; width:3mm; font-weight:bold; font-size:2mm; transform:rotate(90deg);">
								<div style="transform:translate(7mm, -1mm);">
									${item.localIdBarcode}
								</div>
							</div>
						</div>
						<div class="callNoBox" style="float:right; width:33mm; height:100%;">
							<div class="callText" style="float:left; width:19mm; height:100%; font-size:3mm; line-height:3mm; transform:rotate(90deg);">
								<div class="code__box" style="transform:translate(1mm,7mm);">
									
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
							</div>
							<div class="callNo" style="float:right; width:14mm; height:100%;">
								<div style="font-size:8mm; font-weight:bold; color:black; transform:translate(-1mm,9mm);">
									<div style="transform:rotate(90deg);">
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
									</div>
								</div>
							</div>
						</div>
					</div>
				</div><!-- tagSection 끝 -->
				
				<div class="bookTitleSection" style="float:left; padding-left:2.5mm; padding-top:1mm; width:90%; height:3mm; font-size:2mm; overflow:hidden;">
					${item.title}
				</div>
				
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
	
</script>
</html>