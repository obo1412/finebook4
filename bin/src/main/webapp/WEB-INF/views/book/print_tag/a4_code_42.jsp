<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/book/print_label/a4_code_42.css" />
</head>
<body>

	<div class="prtNone">
		<span class="prtNone" style="font-size:30pt;">청구기호 42칸</span>
		<span class="prtNone">
			<button class="btn btn-danger prtNone" id="btn-print">인쇄</button>
		</span>
	</div>
	
	
	<div class="whole__box">
		<!-- 최초1회 a4 시작 위치 조정 -->
		<span class="start__gap__box">
		</span>
		<!-- 시작위치 이동용 더미 -->
		<c:if test="${startPoint gt 1}">
			<c:forEach var="i" begin="1" end="${startPoint-1}">
				<div class="item__box"><!-- float left를 사용해서 나란히 놓기 -->
				</div>
			</c:forEach>
		</c:if>
		<!-- 라벨 출력 시작 -->
		<c:forEach var="item" items="${bookHeldList}" varStatus="status">
			<div class="item__box">
			
				<span class="div__title">
					<span>
						${item.title}
					</span>
				</span>
				
				<div class="data__box">
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
				</div>
				
			</div>
			<c:if test="${(((status.count+startPoint-1) mod 42) eq 0) and ((fn:length(bookHeldList)+startPoint-1) gt 42)}">
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