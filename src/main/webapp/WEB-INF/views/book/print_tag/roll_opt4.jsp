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
	</style>
</head>
<body>
	<div class="prtNone">
		<span class="prtNone" style="font-size:30pt;">롤프린터 태그 - 옵션4 - 별치기호 색띠 구분</span>
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
						
						<!-- 아래 별치기호 색상 -->
						<c:set var="addiColor" value="white" />
						<c:choose>
							<c:when test="${item.additionalCode eq '아동'}">
								<c:set var="addiColor" value="yellow" />
							</c:when>
							<c:when test="${item.additionalCode eq '그림책'}">
								<c:set var="addiColor" value="red" />
							</c:when>
							<c:when test="${item.additionalCode eq '일반'}">
								<c:set var="addiColor" value="blue" />
							</c:when>
							<c:otherwise>
								<!-- 별치기호가 없는 경우 흰색에서 아무것도 하지 않음. -->
							</c:otherwise>
						</c:choose>
						<div class="addiCodeColorSection" style="float:right; width:8mm; height:100%; background-color:${addiColor};">
							
						</div>
						<!-- 별치기호 색상 -->
						<!-- 별치기호와 분류기호 구분선 -->
						<div class="codeDivisionSection" style="float:right; width:1mm; height:100%;">
						</div>
						<!-- 별치기호와 분류기호 구분선 -->
						
						<div class="kdc1Section" style="float:right; width:13mm; height:100%; background-color:${item.classCodeColor};">
							<div style="color:white; text-align:center; position:relative; top:50%; transform:translate(-0.5mm,-6.5mm); ">
								<div style="font-size: 24pt; font-weight:900; transform:rotate(90deg); font-family:HY견고딕; white-space:nowrap;">
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
									<%-- <div style="transform:scale(1.5,1.5);">${item.additionalCode}</div> --%>
								</div>
							</div>
						</div>
						
						<div class="callNoBox" style="float:right; width:28mm; height:100%;">
							<div class="callText" style="float:left; width:19mm; height:100%; font-size:3.5mm; line-height:5mm; transform:rotate(90deg);">
								<div style="transform:translate(2.5mm,-2mm); font-weight:400;">
									<c:choose>
										<c:when test="${not empty item.additionalCode}">
											<div style="height:15px;">${item.additionalCode}</div>
										</c:when>
										<c:otherwise>
											<div style="visibility:hidden; height:15px;">-</div>
										</c:otherwise>
									</c:choose>
									<!-- 분류기호 십진분류 -->
									<c:choose>
										<c:when test="${not empty item.classificationCode}">
											<div style="height:15px;">${item.classificationCode}</div>
										</c:when>
										<c:otherwise>
											<div style="visibility:hidden; height:15px;">-</div>
										</c:otherwise>
									</c:choose>
									<!-- 저자코드 저자기호 -->
									<c:choose>
										<c:when test="${not empty item.authorCode}">
											<div style="height:15px;">${item.authorCode}</div>
										</c:when>
										<c:otherwise>
											<div style="visibility:hidden; height:15px;">-</div>
										</c:otherwise>
									</c:choose>
									<!-- 볼륨코드 권차기호 -->
									<c:choose>
										<c:when test="${not empty item.volumeCode}">
											<div style="height:15px;">V${item.volumeCode}</div>
										</c:when>
										<c:otherwise>
											<div style="visibility:hidden; height:15px;">-</div>
										</c:otherwise>
									</c:choose>
									<!-- 카피코드 복본기호 -->
									<c:choose>
										<c:when test="${(item.copyCode gt 0) and (not empty item.copyCode)}">
											<div style="height:15px;">C${item.copyCode}</div>
										</c:when>
										<c:otherwise>
											<div style="visibility:hidden; height:15px;">-</div>
										</c:otherwise>
									</c:choose>
									<!-- 등록번호 -->
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