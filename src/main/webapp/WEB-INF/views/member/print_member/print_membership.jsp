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
	</style>

	<link href="${pageContext.request.contextPath}/assets/css/member/print_membership/print_membership.css" rel="stylesheet" type="text/css" />
</head>
<body>
	

				
				<div class="prtNone">
					<span class="prtNone" style="font-size:30pt;">회원증 인쇄 A4용지</span>
					<span class="prtNone">
						<button class="btn btn-danger prtNone" id="btn-print">인쇄</button>
					</span>
				</div>
				
				
				<c:forEach var="item" items="${memberList}" varStatus="status">
					
					<!-- 20개를 담을 수 있는 a4 용지 크기 표시 -->
					<c:if test="${(status.count mod 20) eq 1}">
						<div class="a4_size">
						</div>
					</c:if>
					
					<!-- 이하 js(print_member.js)로 위 만든 a4 크기에 삽입 -->
					<div class="membershipSingleSpace">
						
						<!-- a4 가로 중간 매칭을 위한 box-->
						<c:if test="${(status.count mod 2) eq 1}">
							<div class="leftMarginBox">
							</div>
						</c:if>
						
						<!-- 실제 카드 크기 형태 -->
						<div class="membershipCard">
							<div class="membershipBorder">
							
								<div class="leftBox">
									<!-- 왼쪽 살짝 띄워주는 빈칸 -->
									<div style="float:left; width:2mm; height:100%;">
									</div>
									
									<div class="classNameDiv">
										<c:set var="className" value="" />
										<c:if test="${!empty item.className}">
											<c:set var="className" value="${item.className}" />
										</c:if>
										${className}
									</div>
									<div class="nameDiv">
										${item.name}
									</div>
								</div>
								
								<div class="rightBox">
									<!-- 왼쪽 살짝 띄워주는 빈칸 -->
									<div style="float:left; width:2mm; height:100%;">
									</div>
									
									<c:set var="barcodeSize" value="10mm" />
									<c:if test="${fn:length(item.barcodeMbr)gt 8}">
										<c:set var="barcodeSize" value="9mm" />
									</c:if>
									
									<div class="barcodeDiv" style="font-size:${barcodeSize};">
										*${item.barcodeMbr}*
									</div>
									<div class="memberCodeDiv">
										${item.barcodeMbr}
									</div>
								</div>
							</div> <!-- membershipBorder 실제 줄 치기  -->
						</div> <!-- membershipCard 너비 90mm -->
						
					</div> <!-- membershipSingleSpace 너비 104mm  -->
					
				</c:forEach>


</body>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/member-js/print_membership/print_member.js"></script>

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