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
	
	<link href="${pageContext.request.contextPath}/assets/css/book/print_label/print_label_color_setting.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<form class="" name="tag-color-setting-form" method="post"
		action="${pageContext.request.contextPath}/book/print_color_setting_ok.do">
		<div class="form-group">
			<button type="submit" class="btn btn-primary">설정저장</button>
			<button class="btn btn-warning" onclick="window.close();">창닫기</button>
		</div>
		
		<div style="float:left; width:300px;">
			<table class="table table-bordered ref__color__table">
				<thead>
					<tr>
						<th></th>
						<c:forEach var="itemHead" items="${tagPack}" varStatus="headStatus">
							<th>${itemHead.tagName}</th>
						</c:forEach>
					</tr>
				</thead>
				
					<c:choose>
						<c:when test="${fn:length(tagPack) > 0}">
								<tbody>
									<tr>
										<td>000</td>
										<c:forEach var="item" items="${tagPack}" varStatus="status">
											<td><input type="color" name="refKdc0" value="${item.color0Kdc}"/></td>
										</c:forEach>
									</tr>
									<tr>
										<td>100</td>
										<c:forEach var="item" items="${tagPack}" varStatus="status">
											<td><input type="color" name="refKdc1" value="${item.color1Kdc}"/></td>
										</c:forEach>
									</tr>
									<tr>
										<td>200</td>
										<c:forEach var="item" items="${tagPack}" varStatus="status">
											<td><input type="color" name="refKdc2" value="${item.color2Kdc}"/></td>
										</c:forEach>
									</tr>
									<tr>
										<td>300</td>
										<c:forEach var="item" items="${tagPack}" varStatus="status">
											<td><input type="color" name="refKdc3" value="${item.color3Kdc}"/></td>
										</c:forEach>
									</tr>
									<tr>
										<td>400</td>
										<c:forEach var="item" items="${tagPack}" varStatus="status">
											<td><input type="color" name="refKdc4" value="${item.color4Kdc}"/></td>
										</c:forEach>
									</tr>
									<tr>
										<td>500</td>
										<c:forEach var="item" items="${tagPack}" varStatus="status">
											<td><input type="color" name="refKdc5" value="${item.color5Kdc}"/></td>
										</c:forEach>
									</tr>
									<tr>
										<td>600</td>
										<c:forEach var="item" items="${tagPack}" varStatus="status">
											<td><input type="color" name="kdc6" value="${item.color6Kdc}"/></td>
										</c:forEach>
									</tr>
									<tr>
										<td>700</td>
										<c:forEach var="item" items="${tagPack}" varStatus="status">
											<td><input type="color" name="refKdc7" value="${item.color7Kdc}"/></td>
										</c:forEach>
									</tr>
									<tr>
										<td>800</td>
										<c:forEach var="item" items="${tagPack}" varStatus="status">
											<td><input type="color" name="refKdc8" value="${item.color8Kdc}"/></td>
										</c:forEach>
									</tr>
									<tr>
										<td>900</td>
										<c:forEach var="item" items="${tagPack}" varStatus="status">
											<td><input type="color" name="refKdc9" value="${item.color9Kdc}"/></td>
										</c:forEach>
									</tr>
									<tr>
										<td>미지정</td>
										<c:forEach var="item" items="${tagPack}" varStatus="status">
											<td><input type="color" name="refKdcBlank" value="${item.colorBlankKdc}"/></td>
										</c:forEach>
									</tr>
								</tbody>
						</c:when>
						<c:otherwise>
							<div>
								기본 색상 테이블이 존재하지 않습니다.
							</div>
						</c:otherwise>
					</c:choose>
			</table>
		</div>
		
		<div style="float:left; margin-left:50px; max-width:400px;">
			<table class="table table-bordered clickCls cur__color__table">
				<thead>
					<tr>
						<td class="td__width">현재</td>
						<td class="td__width">변경</td>
						<td class="td__code__text">코드</td>
					</tr>
				</thead>
				<tbody class="chooseColor">
					<tr>
						<td><input type="color" value="${tag.color0Kdc}"/></td>
						<td><input type="color" name="kdc0" id="kdc0" value="${tag.color0Kdc}"/></td>
						<td><input type="text" name="kdc0Code" id="kdc0Code" class="input__text" /></td>
					</tr>
					<tr>
						<td><input type="color" value="${tag.color1Kdc}"/></td>
						<td><input type="color" name="kdc1" id="kdc1" value="${tag.color1Kdc}"/></td>
						<td><input type="text" name="kdc1Code" id="kdc1Code" class="input__text" /></td>
					</tr>
					<tr>
						<td><input type="color" value="${tag.color2Kdc}"/></td>
						<td><input type="color" name="kdc2" id="kdc2" value="${tag.color2Kdc}"/></td>
						<td><input type="text" name="kdc2Code" id="kdc2Code" class="input__text" /></td>
					</tr>
					<tr>
						<td><input type="color" value="${tag.color3Kdc}"/></td>
						<td><input type="color" name="kdc3" id="kdc3" value="${tag.color3Kdc}"/></td>
						<td><input type="text" name="kdc3Code" id="kdc3Code" class="input__text" /></td>
					</tr>
					<tr>
						<td><input type="color" value="${tag.color4Kdc}"/></td>
						<td><input type="color" name="kdc4" id="kdc4" value="${tag.color4Kdc}"/></td>
						<td><input type="text" name="kdc4Code" id="kdc4Code" class="input__text" /></td>
					</tr>
					<tr>
						<td><input type="color" value="${tag.color5Kdc}"/></td>
						<td><input type="color" name="kdc5" id="kdc5" value="${tag.color5Kdc}"/></td>
						<td><input type="text" name="kdc5Code" id="kdc5Code" class="input__text" /></td>
					</tr>
					<tr>
						<td><input type="color" value="${tag.color6Kdc}"/></td>
						<td><input type="color" name="kdc6" id="kdc6" value="${tag.color6Kdc}"/></td>
						<td><input type="text" name="kdc6Code" id="kdc6Code" class="input__text" /></td>
					</tr>
					<tr>
						<td><input type="color" value="${tag.color7Kdc}"/></td>
						<td><input type="color" name="kdc7" id="kdc7" value="${tag.color7Kdc}"/></td>
						<td><input type="text" name="kdc7Code" id="kdc7Code" class="input__text" /></td>
					</tr>
					<tr>
					<td><input type="color" value="${tag.color8Kdc}"/></td>
						<td><input type="color" name="kdc8" id="kdc8" value="${tag.color8Kdc}"/></td>
						<td><input type="text" name="kdc8Code" id="kdc8Code" class="input__text" /></td>
					</tr>
					<tr>
						<td><input type="color" value="${tag.color9Kdc}"/></td>
						<td><input type="color" name="kdc9" id="kdc9" value="${tag.color9Kdc}"/></td>
						<td><input type="text" name="kdc9Code" id="kdc9Code" class="input__text" /></td>
					</tr>
					<tr>
						<td><input type="color" value="${tag.colorBlankKdc}"/></td>
						<td><input type="color" name="kdcBlank" id="kdcBlank" value="${tag.colorBlankKdc}"/></td>
						<td><input type="text" name="kdcBlankCode" id="kdcBlankCode" class="input__text" /></td>
					</tr>
				</tbody>
			</table>
		</div>
		
		
	</form>
	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>



<script type="text/javascript">
	window.onload = function() {
		//초기 호출시 색상 코드 값 불러오기
		bringColorToCode();
	}
	
	//class로 change 체크하는 조건 생성해야됨.
	const crtTable = document.querySelector('.clickCls');
	crtTable.addEventListener('change', (e)=> {
		console.log(e.target.getAttribute('id'));
		var clickedNode = e.target.getAttribute('id');
		if(clickedNode != null) {
			var thisId = '';
			var thisCode = '';
			if(clickedNode.indexOf("Code")>-1){
				thisCode = e.target.getAttribute('id');
				thisId = thisCode.substring(0,clickedNode.indexOf('Code'));
				console.log(thisId);
				document.getElementById(thisId).value = document.getElementById(thisCode).value;
			} else {
				thisId = e.target.getAttribute('id');
				thisCode = thisId+'Code';
				document.getElementById(thisCode).value = document.getElementById(thisId).value;
			}
		}
	})
	
	//전역 변수 선언
	const kdcBlank = document.getElementById('kdcBlank');
	const kdcBlankCode = document.getElementById('kdcBlankCode');
	
	//blank값 앞뒤가 바뀌면, 수정해주는 코드
	kdcBlank.addEventListener('change', (e) => {
		kdcBlankCode.value = kdcBlank.value;
	})
	kdcBlankCode.addEventListener('change', (e)=> {
		kdcBlank.value = kdcBlankCode.value;
	})
	//blank값 앞뒤가 바뀌면, 수정해주는 코드
	
	
	//색상 코드 값 불러오기
	function bringColorToCode() {
		for(var i=0; i<10; i++){
			var kdcNumVar = 'kdc'+i;
			var kdcNumCodeVar = 'kdc'+i+'Code';
			var kdcNumColor = document.getElementById(kdcNumVar).value;
			var kdcNumCode = document.getElementById(kdcNumCodeVar);
			kdcNumCode.value = kdcNumColor;
		}
		//kdcBlankCode는 이미 const로 선언했기 때문에, 생략
		//전역변수로 선언된, 블랭크의 값 대입.
		kdcBlankCode.value = kdcBlank.value;
		
	}
</script>
</html>