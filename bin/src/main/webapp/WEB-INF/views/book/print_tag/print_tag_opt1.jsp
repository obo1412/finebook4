<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/inc/head.jsp"%>
<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/js/barcode/jquery-barcode.js"></script>
<style type="text/css">
@font-face {
	font-family: 'Free 3 of 9';
	src: url(${pageContext.request.contextPath}/assets/fonts/free3of9.ttf);
}

@media print {
	@page {
		size: 262.5mm 371.25mm; /* A4 */
		margin: 0;
	}
	html, body {
		border: 0;
		margin: 0;
		padding: 0;
		font-family: '굴림'
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
	
	<div id='wrapper'>
	
		<div id="content-wrapper">

			<div class="container-fluid" style="margin-left:0mm; margin-top:1.85mm;">
				
				<div class="prtNone">
					<span class="prtNone" style="font-size:30pt;">태그 - 옵션1</span>
					<span class="prtNone">
						<button class="btn btn-danger prtNone" id="btn-print">인쇄</button>
					</span>
				</div>

				<c:forEach var="item" items="${bookHeldList}" varStatus="status">
					<c:choose>
						<c:when test="${(status.count ne 1) and((status.count mod 18) eq 1)}">
							<div style="/* border:1px dashed red; */ width: 100%; height:22.125mm; float: left; ">
							<!-- 18로 나눈 나머지 간격 조정 -->
							</div>
						</c:when>
						<c:otherwise>
							<!-- 아무것도 만들지 않는다ㅇ -->
						</c:otherwise>
					</c:choose>
					<div style="/* border:1px dashed red; */ width: 125mm; float: left;"><!-- float left를 사용해서 나란히 놓기 -->
						<div id="titleRmk"
							style="line-height:3.75mm; padding-left: 6.25mm; font-size: 6pt; font-weight: bold; width:100%; overflow:hidden">${item.titleBook}</div>
						<div id="wholeBox"
							style="/* border:1px solid black; */ margin-left:6.2mm; width: 117.5mm; height: 35mm; float: left;">
							<div id="leftBox"
								style="padding-left:2.5mm; text-align: center; float: left; width: 58.75mm; height: 100%;">
								
								<c:choose>
									<c:when test="${item.classCodeHead lt 0}">
										<div id="kdcNoBox" style="display: table; background-color:#424242; float:left; width: 15.75mm; height: 100%; text-align: center;">
											<div style="width:100%; display: table-cell; font-size: 20pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;"> </div>
										</div>
									</c:when>
									<c:when test="${item.classCodeHead eq 0}">
										<div id="kdcNoBox" style="display: table; background-color:#848484; float:left; width: 15.75mm; height: 100%; text-align: center;">
											<div style="width:100%; display: table-cell; font-size: 20pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">총류</div>
										</div>
									</c:when>
									<c:when test="${item.classCodeHead eq 100}">
										<div id="kdcNoBox" style="display: table; background-color:#FA8258; float:left; width: 15.75mm; height: 100%; text-align: center;">
											<div style="width:100%; display: table-cell; font-size: 20pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">철학</div>
										</div>
									</c:when>
									<c:when test="${item.classCodeHead eq 200}">
										<div id="kdcNoBox" style="display: table; background-color:#F7D358; float:left; width: 15.75mm; height: 100%; text-align: center;">
											<div style="width:100%; display: table-cell; font-size: 20pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">종교</div>
										</div>
									</c:when>
									<c:when test="${item.classCodeHead eq 300}">
										<div id="kdcNoBox" style="display: table; background-color:#F5DA81; float:left; width: 15.75mm; height: 100%; text-align: center;">
											<div style="width:100%; display: table-cell; font-size: 10pt; color: white; transform-origin: 50%; transform:rotate(90deg) scale(1.8,1.8); vertical-align: middle;">사회과학</div>
										</div>
									</c:when>
									<c:when test="${item.classCodeHead eq 400}">
										<div id="kdcNoBox" style="display: table; background-color:#D358F7; float:left; width: 15.75mm; height: 100%; text-align: center;">
											<div style="width:100%; display: table-cell; font-size: 10pt; color: white; transform-origin: 50%; transform: rotate(90deg) scale(1.8,1.8); vertical-align: middle;">순수과학</div>
										</div>
									</c:when>
									<c:when test="${item.classCodeHead eq 500}">
										<div id="kdcNoBox" style="display: table; background-color:#F78181; float:left; width: 15.75mm; height: 100%; text-align: center;">
											<div style="width:100%; display: table-cell; font-size: 10pt; color: white; transform-origin: 50%; transform: rotate(90deg) scale(1.8,1.8); vertical-align: middle;">기술과학</div>
										</div>
									</c:when>
									<c:when test="${item.classCodeHead eq 600}">
										<div id="kdcNoBox" style="display: table; background-color:#7401DF; float:left; width: 15.75mm; height: 100%; text-align: center;">
											<div style="width:100%; display: table-cell; font-size: 20pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">예술</div>
										</div>
									</c:when>
									<c:when test="${item.classCodeHead eq 700}">
										<div id="kdcNoBox" style="display: table; background-color:#04B404; float:left; width: 15.75mm; height: 100%; text-align: center;">
											<div style="width:100%; display: table-cell; font-size: 20pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">언어</div>
										</div>
									</c:when>
									<c:when test="${item.classCodeHead eq 800}">
										<div id="kdcNoBox" style="display: table; background-color:#08088A; float:left; width: 15.75mm; height: 100%; text-align: center;">
											<div style="width:100%; display: table-cell; font-size: 20pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">문학</div>
										</div>
									</c:when>
									<c:when test="${item.classCodeHead eq 900}">
										<div id="kdcNoBox" style="display: table; background-color:#DBA901; float:left; width: 15.75mm; height: 100%; text-align: center;">
											<div style="width:100%; display: table-cell; font-size: 20pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">역사</div>
										</div>
									</c:when>
								</c:choose>
								
								<div id="barcodeBox" style="float:left; margin-left:20px;">
									<div
										style="width: 100%; height: 35%; font-weight: 900; font-size: 18pt; padding-top: 10pt;
										line-height:20pt;">
										${item.nameLib}</div>
									<div
										style="height: 35%; font-family: 'Free 3 of 9', 'barcode39', '바코드39'; font-size: 25pt; transform:scale(1,1.5);">
										*${item.localIdBarcode}*</div>
									<div style="font-weight: bold; margin-top:0mm; ">${item.localIdBarcode}</div>
								</div>
							</div>
							
							<div id="rightWhole" style="float:left; width:58.75mm; height:100%;">
							<div id="centerBox"
								style="float: left; width: 40mm; height: 100%;">
								<div id="qrBox" style="float: left; width: 20mm; height: 100%; /* border-right:1px solid red; border-left:1px solid red; */">
									<div
										style="float:left; line-height:12pt; margin-top: 10mm; margin-left: 2.5mm; transform: rotate(90deg);">
										<div style="font-weight:bold;">${item.localIdBarcode}</div>
										<div id="qrImg">
											<img style="width:50px;" 
												src="http://nuts.i234.me:7070/files/upload/finebook4/qrcode/libNo${loginInfo.idLibMng}/${item.localIdBarcode}.png" alt="profileImg"/>
										</div>
									</div>
								</div>
								<div id="callNoBox"
									style="float: left; width: 20mm; height: 100%;">
									<div
										style="float:left; font-size: 10pt; line-height:12pt; margin-top: 3.4mm; margin-left: 5.2mm; transform: rotate(90deg);">
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
							</div>
							<c:set var="classCode" value="${item.classCodeHead}" />
							<c:choose>
								<c:when test="${classCode lt 0}">
									<c:set var="classCode" value="" />
									<div id="rightBox"
										style="display: table; background-color:#424242; float: right; width: 18.75mm; height: 100%; text-align: center;">
									<div
										style="width:100%; display: table-cell; font-size: 30pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">${classCode}</div>
									</div>
								</c:when>
								<c:when test="${classCode eq 0}">
									<c:set var="classCode" value="000" />
									<div id="rightBox"
										style="display: table; background-color:#848484; float: right; width: 18.75mm; height: 100%; text-align: center;">
									<div
										style="width:100%; display: table-cell; font-size: 30pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">${classCode}</div>
										</div>
								</c:when>
								<c:when test="${classCode eq 100}">
									<div id="rightBox"
										style="display: table; background-color:#FA8258; float: right; width: 18.75mm; height: 100%; text-align: center;">
									<div
										style="width:100%; display: table-cell; font-size: 30pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">${classCode}</div>
										</div>
								</c:when>
								<c:when test="${classCode eq 200}">
									<div id="rightBox"
											style="display: table; background-color:#F7D358; float: right; width: 18.75mm; height: 100%; text-align: center;">
										<div style="width:100%; display: table-cell; font-size: 30pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">${classCode}</div>
									</div>
								</c:when>
								<c:when test="${classCode eq 300}">
									<div id="rightBox"
											style="display: table; background-color:#F5DA81; float: right; width: 18.75mm; height: 100%; text-align: center;">
										<div style="width:100%; display: table-cell; font-size: 30pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">${classCode}</div>
									</div>
								</c:when>
								<c:when test="${classCode eq 400}">
									<div id="rightBox"
											style="display: table; background-color:#D358F7; float: right; width: 18.75mm; height: 100%; text-align: center;">
										<div style="width:100%; display: table-cell; font-size: 30pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">${classCode}</div>
									</div>
								</c:when>
								<c:when test="${classCode eq 500}">
									<div id="rightBox"
											style="display: table; background-color:#F78181; float: right; width: 18.75mm; height: 100%; text-align: center;">
										<div style="width:100%; display: table-cell; font-size: 30pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">${classCode}</div>
									</div>
								</c:when>
								<c:when test="${classCode eq 600}">
									<div id="rightBox"
											style="display: table; background-color:#7401DF; float: right; width: 18.75mm; height: 100%; text-align: center;">
										<div style="width:100%; display: table-cell; font-size: 30pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">${classCode}</div>
									</div>
								</c:when>
								<c:when test="${classCode eq 700}">
									<div id="rightBox"
											style="display: table; background-color:#04B404; float: right; width: 18.75mm; height: 100%; text-align: center;">
										<div style="width:100%; display: table-cell; font-size: 30pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">${classCode}</div>
									</div>
								</c:when>
								<c:when test="${classCode eq 800}">
									<div id="rightBox"
											style="display: table; background-color:#08088A; float: right; width: 18.75mm; height: 100%; text-align: center;">
										<div style="width:100%; display: table-cell; font-size: 30pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">${classCode}</div>
									</div>
								</c:when>
								<c:when test="${classCode eq 900}">
									<div id="rightBox"
											style="display: table; background-color:#DBA901; float: right; width: 18.75mm; height: 100%; text-align: center;">
										<div style="width:100%; display: table-cell; font-size: 30pt; color: white; transform-origin: 50%; transform: rotate(90deg); vertical-align: middle;">${classCode}</div>
									</div>
								</c:when>
							</c:choose>
							</div><!-- rightWhole -->
						</div><!-- whole box 끝 -->
					</div>
				</c:forEach>

			</div>
			<!-- container-fluid 끝 -->
		</div>
		<!-- content-wrapper 끝 -->
	</div>
	<!-- wrapper 끝 -->

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>



<script type="text/javascript">
/* 	function printPage() {
		var initBody;
		window.onbeforeprint = function() {
			initBody = document.body.innerHTML;
			document.body.innerHTML = document.getElementByClassName('print').innerHTML;
		};
		window.onafterprint = function() {
			document.body.innerHTML = initBody;
		};
		window.print();
		return false;
	}; */

	$(function() {

		/* function PrintElem(elem) {
			Popup($(elem).html());
		}
		
		function Popup(data) {
			var mywindow = window.open('', 'my div', 'height=400,width=600');
			mywindow.document.write('<html><head><title>my div</title>');
			mywindow.document.write('</head><body >');
			mywindow.document.write(data);
			mywindow.document.write('</body></html>');
			mywindow.document.close(); // IE >= 10에 필요
			mywindow.focus(); // necessary for IE >= 10
			mywindow.print();
			mywindow.close();
			return true;	
		} */

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