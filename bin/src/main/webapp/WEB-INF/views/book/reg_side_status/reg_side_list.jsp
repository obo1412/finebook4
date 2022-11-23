<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>

<style type="text/css">
	.commonWidth {
		width: 800px;
	}
	
	::-webkit-scrollbar {
		width:2px;
		background-color:white;
	}
	::-webkit-scrollbar-thumb {
		background-color:#778899;
	}
	::-webkit-scrollbar-track {
		background-color:white;
	}
</style>

<!-- 등록된 도서 card -->
<div class="btnGroup commonWidth" style="float:left;">
	<button style="float:left;" onclick="checkAll()">전체선택</button>
	<button style="float:left; margin-left:5px;" onclick="uncheckAll()">전체해제</button>
	<button style="float:left; margin-left:5px;" onclick="clickedPrintChkList()">인쇄</button>
	<button style="float:right;" onclick="clickedDeleteBatchBook()">
		삭제
	</button>
</div>
<div class="card-body commonWidth" style="float:left; max-width:800px; height:800px; overflow-y:auto;">
	<div class="divBookList">
		<table class="table table-sm table-bordered">
			<thead>
				<tr>
					<th class="table-secondary text-center" style="width:50px;">-</th>
					
					<th class="table-secondary text-center" style="width:200px; vertical-align:middle;" rowspan="2">도서명</th>
					<th class="table-secondary text-center" style="width:80px;">저자명</th>
					
					<th class="table-secondary text-center" style="width:50px; vertical-align:middle;" rowspan="2">청구기호</th>
					<th class="table-secondary text-center" style="width:60px; vertical-align:middle;" rowspan="2">등록번호</th>
					<th class="table-secondary text-center" style="width:40px;">권차</th>
					
					
				</tr>
				<tr>
					<th class="table-secondary text-center" style="width:50px;">번호</th>
					<th class="table-secondary text-center" style="width:40px;">출판사</th>
					<th class="table-secondary text-center" style="width:40px;">복본</th>
				</tr>
			</thead>
			<tbody class="searchCls">
				<c:choose>
					<c:when test="${fn:length(regTodayList) > 0}">
						<c:forEach var="item" items="${regTodayList}" varStatus="status">
							<tr style="border-top:3px solid #dee2e6;">
								<td class="text-center">
									<span class="cls-book-id" style="display:none;">${item.id}</span>
									<input type="checkbox" class="chk-box-book-id"/>
								</td>
								
								<td class="text-center" rowspan="2" style="vertical-align:middle; overflow-y:scroll;">
									<c:url var="viewUrl" value="/book/book_held_edit.do">
										<c:param name="localIdBarcode" value="${item.localIdBarcode}" />
										<c:param name="bookHeldId" value="${item.id}" />
									</c:url>
									<div style="height:50px;">
										<a href="${viewUrl}" onclick="window.open(this.href, '_blank','width=550,height=800,scrollbars=yes');return false;">
											${item.title}
										</a>
									</div>
								</td>
								
								<td class="text-center" style="overflow-y: scroll;">
									<div style="height:20px;">
										${item.writer}
									</div>
								</td>
								
								<td class="text-center" rowspan="2" style="overflow-y:scroll;">
									<div class="cheongGuCode" style="height:60px;">
										<div>
											<c:if test="${not empty item.additionalCode}">${item.additionalCode} </c:if>
										</div>
										<div style="background-color:${item.classCodeColor}; color:white;">
											<c:if test="${not empty item.classificationCode}">${item.classificationCode} </c:if>
										</div>
										<div>
											<c:if test="${not empty item.authorCode}">${item.authorCode} </c:if>
										</div>
										<div>
											<c:if test="${not empty item.volumeCode}">V${item.volumeCode} </c:if>
										</div>
										<div>
											<c:if test="${item.copyCode ne '0'}">C${item.copyCode} </c:if>
										</div>
									</div>
								</td>
								
								<td class="text-center barcodeNum" rowspan="2">${item.localIdBarcode}</td>
								
								<td class="text-center">
									<c:choose>
										<c:when test="${not empty item.volumeCode}">
											V${item.volumeCode}
										</c:when>
										<c:otherwise>
											-
										</c:otherwise>
									</c:choose>
								</td>
								
								
							</tr>
							<!-- 두번째줄 -->
							<tr>
								<td class="text-center">
									<span class="cls__copy__book__info" style="display:none;">${item.id}</span>
									<button class="btn btn-sm btn-info" onclick="copyBeforeBookInfo()">
										${item.sortingIndex}
									</button>
								</td>
								
								<td class="text-center" style="overflow-y:scroll;">
									<div style="height:20px;">
										${item.publisher}
									</div>
								</td>
								
								<c:choose>
									<c:when test="${item.copyCode eq 0}">
										<td class="text-center">-</td>
									</c:when>
									<c:otherwise>
										<td class="text-center">C${item.copyCode}</td>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="6" class="text-center"
								style="line-height: 100px;">조회된 데이터가 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<!-- 원래 페이지네이션 위치 -->
		
		<!-- 라벨타입을 위한 값 -->
		<input type="hidden" id="labelType" value="${labelType.labelType}"/>
	</div>
</div>
<div class="commonWidth" style="float:left; margin-top:10px;">
	<%@ include file="/WEB-INF/inc/common_pagination_bottom.jsp"%>
</div>
<!-- 등록된 도서 card 끝-->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/book-side-list-check-box/book_check_box_side_jsp_page.js"></script>

<script type="text/javascript">
	const labelType = document.getElementById('labelType');
	
	var searchCls = document.querySelector('.searchCls');
	searchCls.addEventListener('dblclick', (e)=>{
		if(e.target.classList.contains('labelColorCls')||e.target.classList.contains('labelColorDivCls')){
			var parentRow = null;
			if(e.target.classList.contains('labelColorCls')){
				parentRow = e.target.parentNode;
			} else if(e.target.classList.contains('labelColorDivCls')){
				parentRow = e.target.parentNode.parentNode;
			}
			let children = parentRow.childNodes;
			let barcodeNum = children[15].innerText;
			console.log(barcodeNum);
			var url = '${pageContext.request.contextPath}/book/print_tag_page.do?tagType='+labelType.value;
			url = url + '&targetSorting='+barcodeNum;
			window.open(url, '_blank', 'width=1080,height=800,scrollbars=yes');
		}
		/* for(var i=0; i<children.length; i++){
			console.log(children[i].innerText);
		} */
	});
	
	//아래는 번호 강제 지정으로 값을 찾아가는게 아니고,
	//클래스 코드를 가지고있으면 그 엘리먼트 값을 읽어들여오도록 하기 위한 테스트 코드
	/* searchCls.addEventListener('click', (e)=>{
		if(e.target.classList.contains('labelColorCls')){
			parentRow = e.target.parentNode;
		} else if(e.target.classList.contains('labelColorDivCls')){
			parentRow = e.target.parentNode.parentNode;
		}
		
		console.log(parentRow.childNodes);
		let chi = parentRow.childNodes;
		console.log('ㅇㅇ실행');
		console.log(chi[15].innerText);
		for(var j=0; j<chi.length; j++){
			
				console.log(document.querySelector('.barcodeNum'));
			
		}
	}); */
</script>

