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

/* 화면에 띄우기만, 인쇄는 적용 안됨. */
@media screen {
	/* 이거 왜 안되냐? */
	/* .a4_size:first-child {
		border-top: 1px solid black;
	} */

	.a4_size {
		border-top: 1px solid black;
		border-left: 1px solid black;
		border-right: 1px solid black;
	}
}

@media all {
	@page {
		size: A4; /* A4 */
		margin: 0;
	}
	html, body {
		border: 0;
		margin: 0;
		padding: 0;
		font-family: '굴림';
		box-sizing: border-box;
	}
	
	.a4_size {
		width:210mm;
		height:297mm;
		padding-top:8.5mm; /* a4 세로 높이 맞추기 위한 값 */
		box-sizing: border-box;
	}
	
	.membershipSingleSpace {
		float:left;
		width:104mm; /* 원래 105mm */
		height:28mm;
		box-sizing: border-box;
	}
	
	.membershipCard {
		float:left;
		width:89mm; /* 원래 90mm */
		height:100%;
		box-sizing: border-box;
	}
	
	.leftMarginBox {
		float:left;
		width:15mm;
		/* 높이는 위 membershipSingleSpace와 동일하게 유지하기 */
		height:28mm;
	}
	
	.membershipBorder {
		width:90%;
		height:95%;
		margin: 0.7mm 4.5mm;
		border: 1px solid black;
		border-radius: 10px;
		box-sizing: border-box;
	}
	
	.leftBox {
		float:left;
		width: 45%;
		height: 100%;
	}
	
	.rightBox {
		float:left;
		width: 55%;
		height: 100%;
	}
	
	.nameDiv {
		height:35%;
		font-weight:900;
		font-size:3.5mm;
		line-height:12mm;
		white-space:nowrap;
	}
	
	/* 바코드 div */
	.barcodeDiv {
		height:35%;
		font-family:'Free 3 of 9';
		font-size:10mm;
		line-height:10mm;
		transform:scale(1,1.5);
	}
	
	/* left 박스 내에 처음 시작, 상부에서 조금 띄우기 */
	.classNameDiv {
		margin-top: 3mm;
	}
	
	/* right 박스 내에 처음 시작, 상부에서 조금 띄우기 */
	.barcodeDiv {
		margin-top: 5mm;
	}
	
	/* 바코드와 등록번호간 거리 띄우기 */
	.memberCodeDiv {
		margin-top: 2mm;
		text-align: center;
	}
	
}
</style>
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
									
									<div class="barcodeDiv">
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