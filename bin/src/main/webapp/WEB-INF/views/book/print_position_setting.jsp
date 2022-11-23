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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/book/print_label/print_position_setting.css" />

<style type="text/css">

</style>
</head>
<body>
	<form class="" name="tag-position-setting-form" method="get"
		action="${pageContext.request.contextPath}/book/print_position_setting_ok.do">
		
		<div class="div__btns">
			<button type="submit" class="btn btn-primary">설정저장</button>
			<button class="btn btn-warning" onclick="refreshParentWindow();">창닫기</button>
		</div>
		
		<div class="div__categ div__lib__name">
			<h6>바코드에 도서관명 포함 여부</h6>
			<c:set var="yesLibName" value=""/>
			<c:set var="noLibName" value=""/>
			<c:choose>
				<c:when test="${tag.libNameInbarcode eq 1}">
					<c:set var="yesLibName" value="checked"/>
				</c:when>
				<c:otherwise>
					<c:set var="noLibName" value="checked"/>
				</c:otherwise>
			</c:choose>
			<div>
				<div>
					<label for="yesLibName" class="label__lib__name">
						<span>
							<input type="radio" id="yesLibName" name="libNameInbarcode" value="1" ${yesLibName}/>
						</span>
						<img alt="barcode_libname" src="${pageContext.request.contextPath}/assets/img/label/barcode_label_libname.PNG">
					</label>
				</div>
				<div>
					<label for="noLibName" class="label__lib__name">
						<span>
							<input type="radio" id="noLibName" name="libNameInbarcode" value="0" ${noLibName}/>
						</span>
						<img alt="barcode_libname_empty" src="${pageContext.request.contextPath}/assets/img/label/barcode_label_libname_no.PNG">
					</label>
				</div>
			</div>
		</div>
		
		<div class="div__categ">
			<h4>롤 타입 태그 설정</h4>
			
			<div>
				<label class="fontColorRed">라벨타입</label>
				<input name="label-type" value="${tag.labelType}"/>
			</div>
			
			<div>
				<label class="fontColorRed">전체 marginLeft</label>
				<input name="margin-left" value="${tag.marginLeft}"/>
			</div>
			
			<div>
				<label class="fontColorRed">전체 태그너비</label>
				<input name="tag-width" value="${tag.tagWidth}" />
			</div>
			
			<div>
				<label class="fontColorRed">전체 태그높이</label>
				<input name="tag-height" value="${tag.tagHeight}" />
			</div>
			
			<div>
				<label>태그사이 간격</label>
				<input name="tag-gap" value="${tag.tagGap}" />
			</div>
			
			<div>
				<label>제목&amp;태그 사이 간격</label>
				<input name="title-tag-gap" value="${tag.titleTagGap}" />
			</div>
		</div>
		
		<div class="div__categ">
			<h4>A4용지 태그 설정</h4>
			
			<div class="mid__categ">
				<h6>폰트크기(mm)</h6>
				
				<div>
					<label>도서관명</label>
					<input name="fontsize-a4Namelib" value="${tag.a4FontSizeNamelib}"/>
				</div>
				<div>
					<label>바코드</label>
					<input name="fontsize-a4Barcode" value="${tag.a4FontSizeBarcode}"/>
				</div>
				<div>
					<label>바코드 숫자</label>
					<input name="fontsize-a4BarcodeNum" value="${tag.a4FontSizeBarcodeNum}"/>
				</div>
				
				<div>
					<label>청구기호의 바코드 숫자</label>
					<input name="fontsize-a4CodeBarcode" value="${tag.a4FontSizeCodeBarcode}"/>
				</div>
				<div>
					<label>청구기호</label>
					<input name="fontsize-a4Codes" value="${tag.a4FontSizeCodes}"/>
				</div>
				<div>
					<label>대분류기호</label>
					<input name="fontsize-a4ClassNum" value="${tag.a4FontSizeClassNum}"/>
				</div>
			</div>
		</div>
		
	</form>
	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>



<script type="text/javascript">
	function refreshParentWindow() {
		window.opener.location.reload();
		window.close();
	}
</script>
</html>