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
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/book/print_label/a4_default_18.css" />
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
		<span class="prtNone" style="font-size:30pt;">태그 - 기본</span>
		<span class="prtNone">
			<button class="btn btn-danger prtNone" id="btn-print">인쇄</button>
		</span>
	</div>
	
	<!-- 최초1회 margin top -->
	<div style="float:left; width:100%; margin-top:4mm;">
	</div>
	
	<!-- 페이지 넘기기 -->
	<c:if test="${startPoint gt 1}">
		<c:forEach var="i" begin="1" end="${startPoint-1}">
			<div style="width: 104mm; height: 31mm; float: left;"><!-- float left를 사용해서 나란히 놓기 -->
			</div>
		</c:forEach>
	</c:if>
	
	<c:forEach var="item" items="${bookHeldList}" varStatus="status">
	
		<c:choose>
			<c:when test="${(status.count ne 1) and (((status.count+startPoint-1) mod 18) eq 1)}">
				<div style="/* border:1px dashed red; */ width: 100%; height:18mm; float: left; ">
				<!-- 18로 나눈 나머지 간격 조정 -->
				</div>
			</c:when>
			<c:otherwise>
				<!-- 아무것도 만들지 않는다 -->
			</c:otherwise>
		</c:choose>
		
		<div style="width: 104mm; height: 31mm; float: left;"><!-- float left를 사용해서 나란히 놓기 -->
			<div class="bookTitleSection" style="padding-left:8mm; padding-top:0mm; width:90%; height:3mm; font-size:2mm; overflow:hidden;">
				${item.title}
			</div>
				<!-- 왼쪽 라인은 10mm 띄워주고 오른쪽 라인은 ?mm 띄워주는 -->
				<!-- 아래 원래조건 test="${(status.count mod 2) eq 1} -->
				<c:choose>
					<c:when test="${((status.count+startPoint-1) mod 2) eq 1}">
						<div style="width:10mm; height:100%; float:left;">
							<!-- 왼쪽라인 10mm 띄우기 -->
						</div>
					</c:when>
					<c:otherwise>
						<div style="width:6mm; height:100%; float:left;">
							<!-- 오른쪽 라인 6mm 띄우기 -->
						</div>
					</c:otherwise>
				</c:choose>
																		<!-- padding-top 음수는 동작 안함, margin-top음수는 위로 살짝이동 -->
				<div class="tagSection" style="float:left; margin-top:-0.5mm; width:92mm; height:29mm;">
					<div class="tagLeftSection" style="padding-left:0.5mm; width:45mm; height:100%; float:left; text-align:center;">
						<div class="left__nameLib" style="width:100%; height:35%; font-weight:900; line-height:12mm; white-space:nowrap;">
							<c:choose>
								<c:when test="${tag.libNameInbarcode eq 1}">
									${item.nameLib}
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
						</div>
						<c:choose>
							<c:when test="${fn:length(item.localIdBarcode) gt 8}">
								<div class="left__barcode" style="height:35%; font-family:'Free 3 of 9'; line-height:10mm; transform:scale(0.85,1.5);">
									*${item.localIdBarcode}*
								</div>
							</c:when>
							<c:otherwise>
								<div class="left__barcode" style="height:35%; font-family:'Free 3 of 9'; line-height:10mm; transform:scale(1,1.5);">
									*${item.localIdBarcode}*
								</div>
							</c:otherwise>
						</c:choose>
						<div class="left__barcode__num" style="padding-top:1mm; font-weight:bold; font-size:4mm;">
							${item.localIdBarcode}
						</div>
					</div>
					<div class="tagRightSection" style="width:45mm; height:100%; float:right;">
						<div class="qrBox" style="float:left; width:13mm; height:100%;">
							<div id="qrImg" style="float:left; width:10mm;">
								<%-- <img style="width:8mm; margin-top:5mm;" src="/files/upload/finebook4/qrcode/libNo${loginInfo.idLibMng}/${item.localIdBarcode}.png" /> --%>
								
								<img style="width:8mm; margin-top:5mm;" src="${pageContext.request.contextPath}/filesMapping/upload/finebook4/qrcode/libNo${loginInfo.idLibMng}/${item.localIdBarcode}.png" />
							</div>
							<div class="right__code__barcode" style="float:left; width:3mm; font-weight:bold; transform:rotate(90deg);">
								<div style="transform:translate(7mm, -1mm);">
									${item.localIdBarcode}
								</div>
							</div>
						</div>
						<div class="callNoBox" style="float:right; width:32mm; height:100%;">
							<div class="callText" style="float:left; width:18mm; height:100%; transform:rotate(90deg);">
								
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
							<div class="callNo" style="background-color:${item.classCodeColor}; float:right; width:14mm; height:100%;">
								<div class="right__class__num">
									<div>
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