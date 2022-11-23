<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fonts/fonts.css" />
<%@ include file="/WEB-INF/inc/head.jsp"%>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/barcode/jquery-barcode.js"></script>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/book/print_label/print_tag_setup.css" />
<style type="text/css">
	@font-face {
		font-family: 'Free 3 of 9';
		src: url(${pageContext.request.contextPath}/assets/fonts/free3of9.ttf) format('truetype');
	}
	
	.barCode {
		/* barcode font free 3 of 9*/
		font-family: 'barcode font';
		font-size: 40pt;
	}
	
	.divPrintType {
		font-weight:bold;
	}
	
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
</style>
</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>
	<div id='wrapper'>
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">

			<div class="container-fluid">

				<h1 class="prtNone">인쇄 설정 페이지</h1>
					<a class="btn btn-secondary"
						href="${pageContext.request.contextPath}/assets/fonts/free3of9.ttf" download>폰트다운로드</a>
					<a class="btn btn-warning"
						href="${pageContext.request.contextPath}/book/print_position_setting.do"
						onclick="window.open(this.href, '_black', 'width=600,height=800,scrollbars=yes'); return false;" >태그치수
					</a>
					<a class="btn btn-info"
						href="${pageContext.request.contextPath}/book/print_color_setting.do"
						onclick="window.open(this.href, '_black', 'width=1000,height=800,scrollbars=yes'); return false;" >
							태그색상
					</a>

				<%-- <form class="form-horizontal search-box prtNone"
					name="search-mbr-form" id="search-mbr-form" method="get"
					action="${pageContext.request.contextPath}/book/print_tag_page.do"> --%>
				<div>
					<a class="btn btn-primary text-white" style="margin-top:10px;"
						id="btnLabelCollapse" onclick="modalLabelsOpenClose()">
						라벨 타입 선택
					</a>
					<div id="divPickedLabel" style="height:120px;">
						<img id="imgPickedLabel" style="width:300px;" src=""/>
						<a href="#" class="btn btn-danger btn__color__set__16 modal_hidden">16라벨색상관리</a>
						<div class="div__color__set__16 modal__hidden">
							<div class="div__16__btn">
								<a href="#" class="btn btn-warning btn__change__color__set">변경적용</a>
								<a href="#" class="btn btn-danger btn__init__color">초기화</a>
							</div>
							<div>
								<label for="">임시marginTop</label>
								<input type="number" id="tempTitleMt" />
							</div>
							<div class="div__columns">
								<div class="div__keyword__column"></div>
								<div class="div__color__column"></div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="tagTypeClass modal__hidden" id="divLabels">
					
					<div class="div__a4">
						<div class="divPrintType">A4용지 타입</div>
						<label class="label__item">
							<img class="tagImg" src="${pageContext.request.contextPath}/assets/img/tagDefault.PNG" />
								<span>
									<c:set var="labelSelect0" value="" />
									<c:if test="${tag.labelType eq 0}">
										<c:set var="labelSelect0" value="checked" />
									</c:if>
									<input type="radio" name="tagType"
										id="tag_0" value="0" ${labelSelect0}/>기본형
								</span>
						</label>
						<%-- <label>
							<img class="tagImg" src="${pageContext.request.contextPath}/assets/img/tagOpt1.PNG"
								style="display:block; width:300px;"/>
							<span style="padding-left:100px;">
								<c:set var="labelSelect1" value="" />
								<c:if test="${tag.labelType eq 1}">
									<c:set var="labelSelect1" value="checked" />
								</c:if>
								<input type="radio" name="tagType"
									id="tag_1" value="1" ${labelSelect1}/>OPTION1
							</span>
						</label> --%>
						<%-- <label>
							<img class="tagImg" src="${pageContext.request.contextPath}/assets/img/tagC.jpg"
								style="display:block;"/>
							<input type="radio" name="tagType" id="tag_opt2" value="2"/>바코드(중)
						</label> --%>
						<label class="label__item">
							<img class="tagImg" src="" />
								<span>
									<input type="radio" name="tagType" id="tag_2" value="3" />
									바코드 32
								</span>
						</label>
						<label class="label__item">
							<img class="tagImg" src="" />
								<span>
									<input type="radio" name="tagType" id="tag_3" value="4" />
									바코드 40
								</span>
						</label>
						<label class="label__item">
							<img class="tagImg" src="" />
								<span>
									<input type="radio" name="tagType" id="tag_4" value="5" />
									청구기호 30
								</span>
						</label>
						<label class="label__item">
							<img class="tagImg" src="" />
								<span>
									<input type="radio" name="tagType" id="tag_5" value="6" />
									청구기호 42
								</span>
						</label>
							<label class="label__item">
								<img class="tagImg" src="${pageContext.request.contextPath}/assets/img/tagA4CenterSpace.PNG" />
									<span>
										<c:set var="labelSelect7" value="" />
										<c:if test="${tag.labelType eq 7}">
											<c:set var="labelSelect7" value="checked" />
										</c:if>
										<input type="radio" name="tagType"
											id="tag_7" value="7" ${labelSelect7}/>Ver.2(중앙공간)
									</span>
							</label>
					</div>
					
					<div class="mt-1 div__roll">
						<div class="divPrintType">Roll용지 타입</div>
						<label class="label__item">
							<img class="tagImg" src="${pageContext.request.contextPath}/assets/img/tagRollDefault.PNG" />
							<span>
								<c:set var="labelSelect10" value="" />
								<c:if test="${tag.labelType eq 10}">
									<c:set var="labelSelect10" value="checked" />
								</c:if>
								<input type="radio" name="tagType"
									id="tag_10" value="10" ${labelSelect10}/>Roll_Printer(10)
							</span>
						</label>
						<label class="label__item">
							<img class="tagImg" src="${pageContext.request.contextPath}/assets/img/tagRollOpt1.PNG" />
							<span>
								<c:set var="labelSelect11" value="" />
								<c:if test="${tag.labelType eq 11}">
									<c:set var="labelSelect11" value="checked" />
								</c:if>
								<input type="radio" name="tagType"
									id="tag_11" value="11" ${labelSelect11}/>Roll_opt1
							</span>
						</label>
						<label class="label__item">
							<img class="tagImg" src="${pageContext.request.contextPath}/assets/img/tagRollOpt2.PNG" />
							<span>
								<c:set var="labelSelect12" value="" />
								<c:if test="${tag.labelType eq 12}">
									<c:set var="labelSelect12" value="checked" />
								</c:if>
								<input type="radio" name="tagType"
									id="tag_12" value="12" ${labelSelect12}/>Roll_opt2
							</span>
						</label>
						<label class="label__item">
							<img class="tagImg" src="${pageContext.request.contextPath}/assets/img/tagRollOpt3.PNG" />
							<span>
								<c:set var="labelSelect13" value="" />
								<c:if test="${tag.labelType eq 13}">
									<c:set var="labelSelect13" value="checked" />
								</c:if>
								<input type="radio" name="tagType"
									id="tag_13" value="13" ${labelSelect13}/>Roll_opt3
							</span>
						</label>
						<label class="label__item">
							<img class="tagImg" src="${pageContext.request.contextPath}/assets/img/tagRollOpt3.PNG" />
							<span>
								<c:set var="labelSelect14" value="" />
								<c:if test="${tag.labelType eq 14}">
									<c:set var="labelSelect14" value="checked" />
								</c:if>
								<input type="radio" name="tagType"
									id="tag_14" value="14" ${labelSelect14}/>Roll_opt4_별치기호색띠
							</span>
						</label>
						<label class="label__item">
							<img class="tagImg" src="${pageContext.request.contextPath}/assets/img/tagRollNoQrcode.PNG" />
							<span>
								<c:set var="labelSelect15" value="" />
								<c:if test="${tag.labelType eq 15}">
									<c:set var="labelSelect15" value="checked" />
								</c:if>
								<input type="radio" name="tagType"
									id="tag_15" value="15" ${labelSelect15}/>Roll_default_QRCode없음(15)
							</span>
						</label>
						<label class="label__item">
							<img class="tagImg" src="${pageContext.request.contextPath}/assets/img/tagRollOpt6.PNG" />
							<span>
								<c:set var="labelSelect16" value="" />
								<c:if test="${tag.labelType eq 16}">
									<c:set var="labelSelect16" value="checked" />
								</c:if>
								<input type="radio" name="tagType"
									id="tag_16" value="16" ${labelSelect16}/>Roll_서가_별치기호(16)
							</span>
						</label>
					</div>
				</div>
				
				<div class="form-group mb-3" style="width:600px;">
					<label for="rangeSorting">범위 출력(등록번호)</label>
					<div class="col-12">
					
						<div class="col-12 input__range">
							<select name="rangeStart" id="rangeStart" class="form-control">
								<c:forEach var="start" items="${bookHeldList}" varStatus="startStatus">
									<option value="${startStatus.count}">
										${start.localIdBarcode} / (${start.title})
									</option>
								</c:forEach>
							</select>
						</div>
						
						<div class="input-group col-12 mt-1  input__range">
							<select name="rangeEnd" id="rangeEnd" class="form-control">
								<c:forEach var="end" items="${bookHeldList}" varStatus="endStatus">
									<option value="${endStatus.count}">
										${end.localIdBarcode} / (${end.title})
									</option>
								</c:forEach>
							</select>
							<span class="input-group-append">
								<input type="button" class="btn btn-secondary" value="출력" id="sbmBtn"
									<%-- formaction="${pageContext.request.contextPath}/book/print_tag_page.do" --%>
									onclick="openPopup()" />
							</span>
						</div>
						
					</div>
				</div>
				
				<div class="form-group">
					<label for="rangeInputSorting">등록번호 범위입력</label>
					<div class="form-inline">
						<span>바코드:&nbsp;</span>
						<input type="text" class="form-control" id="rangeInputBarcode" style="width:60px;" />
						<span>저자기호:&nbsp;</span>
						<input type="text" class="form-control" id="inputAuthorCode" style="width:60px;" />
						<span>&nbsp;번호:&nbsp;</span>
						<input type="number" class="form-control" id="ribStartNum" style="width:110px;" />
						<span>&nbsp;~&nbsp;</span>
						<input type="number" class="form-control" id="ribEndNum" style="width:110px;" />
						<span>&nbsp;</span>
						<input type="button" class="btn btn-secondary" value="출력" onclick="openRangeInputBarcode()" />
					</div>
				</div>
				
				<div class="form-group">
					<label for="sheetSorting">자동 묶음 출력</label>
					<div class="form-inline">
						<input type="text" class="form-control col-1"
							name="printingEa" id="printingEa" value="${tag.printingEa}" />권씩
						<input type="text" class="form-control col-1"
							name="printingSheetCount" id="printingSheetCount" value="${tag.printingSheetCount}" />번째
						<input type="button" class="btn btn-secondary" value="출력" id="btnPrintSheetSorting"
							onclick="autoSheetSortingPopup()" />
					</div>
				</div>
				
				<div class="form-group">
					<label for="dateSorting">날짜별 출력</label>
					<div class="col-sm-12 col-md-4 mb-3 input-group">
						<input type="date" class="form-control" max="9999-12-31"
							name="dateSorting" id="dateSorting" value="" placeholder="날짜별 출력"/>
							<span class="input-group-append">
								<input type="button" class="btn btn-secondary" value="출력" id="sbmBtn"
									<%-- formaction="${pageContext.request.contextPath}/book/print_tag_page.do" --%>
									onclick="openPopupDate()" />
							</span>
					</div>
				</div>
				
				<div class="form-group">
					<label for="titleSorting">단일 출력(도서명)</label>
					<div class="col-sm-12 col-md-4 mb-3 input-group">
						<input type="text" class="form-control"
							name="titleSorting" id="titleSorting" value="" 
							placeholder="도서명으로 출력" onKeyDown="enterTitle()"/>
						<span class="input-group-append">
								<input type="button" class="btn btn-secondary" value="출력" id="sbmBtn"
									<%-- formaction="${pageContext.request.contextPath}/book/print_tag_page.do" --%>
									onclick="openPopupTitle()" />
						</span>
					</div>
				</div>
				
				<div class="form-group">
					<label for="targetSorting">단일 출력(등록번호)</label>
					<div class="form-inline">
						<div class="col-sm-12 col-md-4 input-group">
							<input type="text" class="form-control"
								name="targetSorting" id="targetSorting" value=""
								placeholder="도서등록번호로 출력" onKeyDown="enterTarget()"/>
							<span class="input-group-append">
									<input type="button" class="btn btn-secondary" value="출력"
										<%-- formaction="${pageContext.request.contextPath}/book/print_tag_page.do" --%>
										onclick="openPopupTarget()" />
							</span>
						</div>
						<label for="startPoint">A4용지 시작위치:</label>
						<input type="text" class="form-control"
								name="startPoint" id="startPoint" value=""
								placeholder="기본 값:1" />
					</div>
				</div>
				
				<!-- </form> -->
				
				<!-- 라벨 txt파일로 출력 -->
				<div style="width:400px;">
					<label for="batchFile">txt파일 일괄 출력</label>
					<form class="form-horizontal info-section" name="batchForm"
						enctype="multipart/form-data"
						method="post">
							
						<div class="form-group">
							<div class="input-group">
								<input type="file" name="batchFile" id="batchFile"
									class="form-control form-control-sm input-clear"
									placeholder="파일 로드" />
								<input type="hidden" name="tagTypeTxtBatch" id="tagTypeTxtBatch" value="${tag.labelType}"/>
								<input type="hidden" name="startPointTxtBatch" id="startPointTxtBatch" value="1" />
								<span class="input-group-append">
									<button class="btn btn-primary btn-sm" formaction="${pageContext.request.contextPath}/book/print_label_text_batch_check.do">출력하기</button>
									<input type="button" class="btn btn-danger btn-sm" onclick="clearInput()" value="다시작성" />
								</span>
							</div>
						</div>
						
					</form>
				</div>
				
			</div>
			<!-- container-fluid 끝 -->
		</div>
		<!-- content-wrapper 끝 -->
	</div>
	<!-- wrapper 끝 -->

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>

<script type="text/javascript" src="/assets/js/book/label_print/print_tag_setup.js"></script>

</html>