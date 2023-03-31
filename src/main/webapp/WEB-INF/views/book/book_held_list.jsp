<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="currDate" class="java.util.Date" />
<!doctype html>
<html>
<head>
<meta charset='utf-8' />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>보유도서 목록</title>

<%@ include file="/WEB-INF/inc/head.jsp"%>
	<style>
		.card {
			max-width: 1500px;
		}
	
		table { 
			table-layout: fixed;
		}
		
		/* 한줄로 안보이게 처리였는데, 이제는 스크롤 처리로 바꿔서 필요 없어짐 */
		td {
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
		}
		
		::-webkit-scrollbar {
			width:2px;
			height: 2px;
			background-color:white;
		}
		::-webkit-scrollbar-thumb {
			background-color:#778899;
		}
		::-webkit-scrollbar-track {
			background-color:white;
		}
		
	</style>
	
	<link href="${pageContext.request.contextPath}/assets/css/book/book_held_list.css" rel="stylesheet" type="text/css" />
</head>

<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>

	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">

				<div class="card mb-3" style="height:800px;">
					<div class="card-header">
						
						<!-- 검색폼 + 추가버튼 -->
						<div>
							
							<div class="float-left div__print__search"> <!-- 인쇄바와 검색바 div -->
								
								<div class="searchbar__form">
									<form method='get' name="searchFormBookHeld"
										action='${pageContext.request.contextPath}/book/book_held_list.do'>
										
										<div class="input-group input-group-sm whole__searchbar">
											
											<span class="searchbar__book__shelf">
												<input type="hidden" id="searchShelf" value="${bookShelf}" />
												<select name="bookShelf" id="selectBookShelf" class="form-control form-control-sm">
													<option value="">서가(전체)</option>
													<c:choose>
														<c:when test="${fn:length(bookShelfList) > 0}">
															<c:forEach var="shelf" items="${bookShelfList}" varStatus="status">
																<!-- null 값 미분류처리 -->
																<c:choose>
																	<c:when test="${shelf eq null}">
																		<option value="uncateg">미분류</option>
																	</c:when>
																	<c:otherwise>
																		<option value="${shelf}">${shelf}</option>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</c:when>
														<c:otherwise>
															<option>서가 없음</option>
														</c:otherwise>
													</c:choose>
												</select>
											</span>
											
											<div class="searchbar__addi__code">
												<input type="hidden" id="searchAddiCode" value="${addiCode}" />
												<select name="addiCode" id="selectAddiCode" class="form-control form-control-sm">
													<option value="">별치기호(전체)</option>
													<c:choose>
														<c:when test="${fn:length(addiCodeList) > 0}">
															<c:forEach var="addiCodeItem" items="${addiCodeList}" varStatus="status">
																<!-- null 값 미분류처리 -->
																<c:choose>
																	<c:when test="${addiCodeItem eq null}">
																		<option value="emptyAddi">별치기호-없음</option>
																	</c:when>
																	<c:otherwise>
																		<option value="${addiCodeItem}">${addiCodeItem}</option>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</c:when>
														<c:otherwise>
															<option>서가 없음</option>
														</c:otherwise>
													</c:choose>
												</select>
											</div>
										
											<div class="input-group searchbar__input__group">
												<span class="input-group-prepend">
													<select name="searchOpt" class="form-control form-control-sm">
															<option value="1"
																<c:if test="${searchOpt == 1}">selected</c:if>>제목</option>
															<option value="2"
																<c:if test="${searchOpt == 2}">selected</c:if>>저자</option>
															<option value="3"
																<c:if test="${searchOpt == 3}">selected</c:if>>출판사</option>
													</select>
												</span>
												
												<input type="text" name='keyword'
													class="form-control form-control-sm search__input__keyword" placeholder="도서 검색"
													value="${keyword}" autofocus/>
												
												<input type="hidden" name="keywordHolder" value="${keywordHolder}" />
												
												<span class="input-group-append">
													<button class="btn btn-success btn-sm" type="submit">
														<i class='fas fa-search'></i>
													</button>
												</span>
												
												<span>
													<a class="btn btn-primary btn-sm ml-1" href="${pageContext.request.contextPath}/book/book_held_list.do?chkBox_tag_search=checked">검색초기화</a>
												</span>
											</div>
											<span class="ml-1">
												<label>
													<c:set var="tagSearchChkSetter" value="checked" />
													<c:if test="${tagSearchChecked eq null}">
														<!-- bookHeldList 컨트롤러에서,
															tagSearchChecked 값이 null이면 체크 안하게 -->
														<c:set var="tagSearchChkSetter" value="" />
													</c:if>
													<input id="chkBox_tag_search" name="chkBox_tag_search"
														type="checkbox" value="checked" ${tagSearchChkSetter} />
													태그 키워드
												</label>
											</span>
										</div>
									</form>
								</div>
								
								<div class="print__whole__bar">
									<button class="btn btn-sm btn-secondary float-left" onclick="checkAll()">전체선택</button>
									<button class="btn btn-sm btn-secondary float-left" onclick="uncheckAll()">전체해제</button>
									<div class="input-group print__select__bar float-left">
										<span class="input-group-prepend">
											<select class="form-control form-control-sm" id="a4LabelType">
												<option value="">-A4타입-</option>
												<option value="0">18칸_기본형</option>
												<option value="7">18칸_Ver2</option>
												<option value="3">바코드32</option>
												<option value="4">바코드40</option>
												<option value="5">청구기호30</option>
												<option value="6">청구기호42</option>
											</select>
										</span>
										<input class="form-control form-control-sm text-center input-group-prepend" type="text" id="startPoint" style="width:100px;"
											placeholder="인쇄위치"/>
										<button class="btn btn-sm btn-warning" onclick="clickedPrintChkList()">인쇄</button>
									</div>
								</div>
								
							</div><!-- 인쇄바와 검색바 감싼 div -->
							
							<div class="float-right form-inline">
								<select name="yearOptionBookHeldList" id="yearOptionBookHeldList" class="form-control form-control-sm">
									<option value="">전체목록</option>
									<fmt:formatDate value="${currDate}" pattern="yyyy" var="yearStart" />
									<c:forEach begin="0" end="10" var="pastYear" step="1">
										<option value="<c:out value='${yearStart-pastYear}'/>">
											<c:out value='${yearStart-pastYear}'/>
										</option>
									</c:forEach>
								</select>
								<button class="btn btn-sm btn-secondary" onclick="clickedBookHeldListToExcel()">도서목록 엑셀변환</button>
							</div>
							
						</div>
						
					</div> <!-- card-header 끝 -->
					
					<div class="card-body" style="overflow-y: scroll;">
						<!-- 조회결과를 출력하기 위한 표 -->
						<table class="table table-sm table-bordered">
							<thead>
								<tr>
									<th class="table-info text-center" style="width:20px; vertical-align: middle;" rowspan="2">-</th>
									<th class="table-info text-center" style="width:50px;">번호</th>
									<th class="table-info text-center" style="width:180px; vertical-align: middle;" rowspan="2">도서명</th>
									<th class="table-info text-center" style="width:70px;">저자명</th>
									<th class="table-info text-center" style="width:70px;">출판일</th>
									<th class="table-info text-center" style="width:90px;">ISBN13</th>
									<th class="table-info text-center" style="width:70px; vertical-align: middle;" rowspan="2">서가</th>
									<th class="table-info text-center" style="width:80px; vertical-align: middle;" rowspan="2">청구기호</th>
									<th class="table-info text-center" style="width:60px;">권차기호</th>
								</tr>
								<tr>
									<th class="table-info text-center" style="width:50px;">상태</th>
									<th class="table-info text-center" style="width:70px;">출판사</th>
									<th class="table-info text-center" style="width:80px;">등록일</th>
									<th class="table-info text-center" style="width:60px;">등록번호</th>
									<th class="table-info text-center" style="width:60px;">복본기호</th>
								</tr>
							</thead>
							<tbody class="searchCls">
								<c:choose>
									<c:when test="${fn:length(bookHeldList) > 0}">
										<c:forEach var="item" items="${bookHeldList}" varStatus="status">
											<tr style="border-top:3px solid #dee2e6;">
												<td class="cls-book-id" style="display:none;">${item.id}</td>
												<td class="text-center" rowspan="2" style="vertical-align:middle;">
													<input type="checkbox" class="chk-box-book-id"/>
												</td>
												<td class="text-center">${page.indexLast - status.index}</td>
												<td class="text-center" rowspan="2" style="vertical-align:middle; overflow-y: scroll;">
													<div style="height:50px;">
														<c:url var="viewUrl"
															value="/book/book_held_edit.do">
															<c:param name="localIdBarcode"
																value="${item.localIdBarcode}" />
																<c:param name="bookHeldId"
																value="${item.id}" />
														</c:url> <a href="${viewUrl}"
														onclick="window.open(this.href, '_blank','width=550,height=800,scrollbars=yes');return false;">${item.title}</a>
													</div>
												</td>
												<td class="text-center" style="overflow-y: scroll;">
													<div style="height:20px;">
														${item.writer}
													</div>
												</td>

												<fmt:parseDate var="parsePubDate" value="${item.pubDate}" pattern="yyyy-MM-dd"/>
												<fmt:formatDate var="pubDate" value="${parsePubDate}" pattern="yyyy-MM-dd" />
												<td class="text-center">${pubDate}</td>
												<td class="text-center" data-toggle="tooltip" data-placement="top" title="${item.isbn13}">${item.isbn13}</td>
												<td class="text-center" rowspan="2" style="vertical-align:middle;">${item.bookShelf}</td>
												<td class="text-center" rowspan="2" style="overflow-y:scroll;">
													<div style="height:50px;">
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
												<!-- 사용하지 않을 테이블 컬럼. 색상 표시 -->
												<%-- <td class="text-center labelColorCls">
													<div class="labelColorDivCls" style="margin:auto; width:30px; min-height:15px; background-color:${item.classCodeColor}"></div>
												</td> --%>
											</tr>
											
											<!-- 아래줄 시작 -->
											<tr>
												<td class="text-center text-danger">${item.brwStatus}</td>
												<td class="text-center" style="overflow-y: scroll;">
													<div style="height: 20px;">
														${item.publisher}
													</div>
												</td>
												<td class="text-center">
													<fmt:parseDate var="regDateStr" value="${item.regDate}" pattern="yyyy-MM-dd" />
													<!-- DB에서 가져온 String형 데이터를 Date로 변환 var 변수명, pattern 날짜형식, value 컨트롤러에서 받은값 var에 저장 -->
													<fmt:formatDate var="regDateFmt" value="${regDateStr}" pattern="yyyy-MM-dd" />
													<!-- Date형으로 저장된 값을 String으로 변환 var변수에 넣고 아래처럼 변수를 사용 -->
													${regDateFmt}
												</td>
												<td class="text-center targetBarcodeCls">${item.localIdBarcode}</td>
												
												<!-- 원래 td로 감싸줘야하는데, 제일 앞에 id값을 주기위한 td가 있음 그래서 한칸 밀림. td를 안써야함. -->
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
											<td colspan="8" class="text-center"
												style="line-height: 100px;">조회된 데이터가 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						
						<input type="hidden" id="labelType" value="${labelType.labelType}"/>
					</div>
					<!-- card body 끝 -->
					<div class="card-footer">
						<!-- 페이지 번호 -->
						<%@ include file="/WEB-INF/inc/common_pagination_bottom.jsp"%>
					</div>
				</div>
				<!-- card 끝 -->
			</div>
			<!-- container-fluid 끝 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
	</div>

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
	
	<!-- 도서 검색시, 한글 기입되면, 영어로 바꿔주는 함수 -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/book/barcode_transfer_kor_eng.js"></script>
	<!-- 체크박스 처리를 위한 js 호출 -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/book-side-list-check-box/book_check_box.js"></script>
	<!-- 도서목록 js 현재는 서가/분류 처리 js 포함 -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/book/book_list.js"></script>
	<!-- 엑셀 다운로드 처리에 필요한 js -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/book/data-import-export/book_list_to_excel.js"></script>
	
	<script type="text/javascript">
	
	</script>
</body>
</html>



