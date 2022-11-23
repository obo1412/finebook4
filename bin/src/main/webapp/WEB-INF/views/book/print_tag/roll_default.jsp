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

<style type="text/css">



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
		<span class="prtNone" style="font-size:30pt;">롤프린터 태그 - 기본</span>
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
						<div style="width:100%; height:35%; font-weight:900; font-size:3.5mm; line-height:12mm; white-space:nowrap;">
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
						<div style="padding-top:1mm; font-weight:bold; font-size:4mm;">
							${item.localIdBarcode}
						</div>
					</div>
					<div class="tagRightSection" style="width:47mm; height:100%; float:right;">
						<div class="qrBox" style="float:left; width:13mm; height:100%;">
							<div id="qrImg" style="float:left; width:10mm;">
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
								<div style="transform:translate(1mm,7mm);">
									<c:if test="${not empty item.additionalCode}">
										<div>${item.additionalCode}</div>
									</c:if>
									<div>${item.classificationCode}</div>
									<div>${item.authorCode}</div>
									<c:if test="${not empty item.volumeCode}">
										<div>V${item.volumeCode}</div>
									</c:if>
									<c:if test="${(item.copyCode gt 0) and (not empty item.copyCode)}">
										<div>C${item.copyCode}</div>
									</c:if>
								</div>
							</div>
							<div class="callNo" style="background-color:${item.classCodeColor}; float:right; width:14mm; height:100%;">
								<div style="height:100%; font-size:8mm; font-weight:bold; color:white; transform:translate(-1mm,9mm);">
									<div style="transform:rotate(90deg); font-family:HY견고딕;">
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