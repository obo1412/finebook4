<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
	
	.callNoBox__opt8 {
		float:right;
		width:33mm;
		height:100%;
		white-space:nowrap;
	}
	
	.callText__opt8 {
		float:left;
		width:19mm;
		height:100%;
		font-size:3.5mm;
		font-weight:600;
		line-height:7mm;
		transform:rotate(90deg);
		font-family: HY견고딕;
	}
	
	.callText__opt8__translate {
		transform:translate(-0.7mm,-2mm);
		/* 바코드 번호 8자리일 경우엔 x: 1.5mm 정도 주는게 딱 좋다.
		나중에 자리수에 맞추어 변경 필요할듯. */
	}
	
	.callText__opt8__lineHeight {
		line-height:4mm;
	}
	
	</style>
</head>
<body>
	<div class="prtNone">
		<span class="prtNone" style="font-size:30pt;">롤프린터 태그 - 옵션8 - DSL전용 라벨A</span>
		<span class="prtNone">
			<button class="btn btn-danger prtNone" id="btn-print">인쇄</button>
		</span>
	</div>

	<c:forEach var="item" items="${bookHeldList}" varStatus="status">
		<div style="width:103mm; height:34mm;">
			<div style="padding-left:1mm; padding-top:1mm; width:100%; height:100%;">
				<div class="bookTitleSection" style="padding-left:2.5mm; padding-top:0mm; width:90%; height:3mm; font-size:2mm; overflow:hidden;">
					${item.title}
				</div>
				<div class="tagSection" style="padding-top:${tag.titleTagGap}mm; width:100%; height:30mm;">
					<div class="tagLeftSection" style="width:48%; height:100%; float:left; text-align:center;">
						<div class="left__nameLib" style="width:100%; height:35%; font-weight:900; line-height:12mm; white-space:nowrap; letter-spacing:-2px;">
							<c:choose>
								<c:when test="${tag.libNameInbarcode eq 1}">
									${item.nameLib}
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
						</div>
						<div style="height:35%; font-family:'Free 3 of 9'; font-size:10mm; line-height:10mm; transform:scale(1,1.5);">
							*${item.localIdBarcode}*
						</div>
						<div style="padding-top:0mm; font-weight:bold; font-size:3mm;">
							${item.localIdBarcode}
						</div>
					</div>
					<div class="tagRightSection" style="width:50%; height:100%; float:right;">
						<!-- QR 박스는 사용하지 않으니 일단 삭제 -->
						<%-- <div class="qrBox" style="float:left; width:13mm; height:100%;">
							<div id="qrImg" style="float:left; width:10mm;"> //아래 주소부분 http://nuts.i234.me:7070 이부분 필요없을지도
								<img style="width:8mm; margin-top:5mm;" src="http://nuts.i234.me:7070/files/upload/finebook4/qrcode/libNo${loginInfo.idLibMng}/${item.localIdBarcode}.png" />
							</div>
							<div style="float:left; width:3mm; font-weight:bold; font-size:2mm; transform:rotate(90deg);">
								<div style="transform:translate(7mm, -1mm);">
									${item.localIdBarcode}
								</div>
							</div>
						</div> --%>
						<div class="kdc1Section" style="float:left; width:16mm; height:100%; background-color:${item.classCodeColor};">
							<div style="color:white; text-align:center; position:relative; top:50%; transform:translate(1mm,-50%);">
								<div style="font-size: 24pt; font-weight:900; transform:rotate(90deg); font-family:HY견고딕;">
									<c:choose>
										<c:when test="${item.classCodeHead gt 0}">
											<fmt:parseNumber var="clsCodeHead" integerOnly="true" value="${item.classCodeHead}" />
											${clsCodeHead}
										</c:when>
										<c:when test="${item.classCodeHead eq 0}">
											<c:set var="classCode" value="000" />
											${classCode}
										</c:when>
										<c:otherwise>
											
										</c:otherwise>
									</c:choose>
									
								</div>
							</div>
						</div>
						
						<div class="callNoBox callNoBox__opt8">
							<div class="callText callText__opt8">
								<div class="callText__opt8__translate">
									
									<div class="callText__opt8__lineHeight">
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
												<div class="volume__code">v.${item.volumeCode}</div>
											</c:when>
											<c:otherwise>
												<div class="div__empty__data">-</div>
											</c:otherwise>
										</c:choose>
										
										<c:choose>
											<c:when test="${(item.copyCode gt 0) and (not empty item.copyCode)}">
												<div class="copy__code">c.${item.copyCode}</div>
											</c:when>
											<c:otherwise>
												<div class="div__empty__data">-</div>
											</c:otherwise>
										</c:choose>
									</div>
									
									<div>${item.localIdBarcode}</div>
									
								</div>
							</div>
						</div>
						
					</div>
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