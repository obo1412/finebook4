<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/inc/head.jsp"%>
<style type="text/css">
	.card-body {
		font-size: 11pt;
		/* white-space: nowrap; */
	}
	
	table { 
		table-layout: fixed;
	}
	
	/* tr > td {
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	} */
	
	.cheongGuCode {
		height:40px;
		overflow:auto;
		-ms-overflow-style: none;
	}
	
	.cheongGuCode::-webkit-scrollbar {
		display:none;
	}
</style>

</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>
	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">
				<div class="card mb-3" style="float:left; min-width:700px; max-width:800px; min-height:780px;">
					<div class="card-header" style="height:50px;">
						<h4 style="float:left;">도서 등록하기</h4>
						<div style="float:right;">
							
						</div>
					</div>
					<div class="card-body">
						<div class="table-responsive row">

							
								<div class="form-group form-inline" style="margin:0px;">
									<div class="col-md-3">
										<label for='search-book-info'>도서 검색</label>
									</div>
									<div class="input-group input-group-sm col-md-6">
										<input type="text" name="search-book-info"
											id="search-book-info" class="form-control input-clear"
											placeholder="ISBN 검색" onKeyDown="searchBookEnterKeyDown()" value="${isbn}"/> <span
											class="input-group-append btn-group">
											<button class="btn btn-warning btn-sm" id="btn-search-bookinfo"
												onclick="clickedSearchBook()">
												<i class="fas fa-search"></i>
											</button>
											<button class="btn btn-info btn-sm" id="searchEx">
												<i class="fas fa-question"></i>
											</button>
										</span>
									</div>
									<c:url var="nlUrl" value="/book/search_nl_book.do">
										<c:param name="search-book-info" value="${isbn}" />
									</c:url>
									<input type="button" value="국중검색" class="btn btn-sm btn-secondary" 
										onclick="openNlPopup()" />
								</div>
								

						<!-- 회원정보, 도서정보 수집 시작 -->
						<form class="form-horizontal info-section" name="formRegBookOk" id="formRegBookOk"
							method="post" action="${pageContext.request.contextPath}/book/reg_book_ok.do">
							
							<div class="form-group form-inline" style="margin:0px 50px 10px 50px;">
								<div class="form-group form-inline classStraightReg">
									<input type="checkbox" class="classStraightReg form-control"
										id="chkBoxStraightReg" name="chkBoxRegOk" value="checked" ${chkBoxRegOk}/>
									<label for="chkBoxStraightReg">바로등록</label>
								</div>
								<div class="form-inline" style="margin-left:10px;">
									<input type="checkbox" id="chkBoxVolumeCodeAlarm" name="chkBoxVolumeCodeAlarm"
										value="checked" />
									<label for="chkBoxVolumeCodeAlarm">권차경고</label>
								</div>
								<div class="form-inline" style="margin-left:10px;">
									<input type="checkbox" id="chk_box_sound_eff" name="chk_box_sound_eff"
										value="checked" />
									<label for="chk_box_sound_eff">소리알림</label>
								</div>
								<div class="form-inline" style="margin-left:10px;">
									<input type="checkbox" id="chk_box_class_code_checker" name="chk_box_class_code_checker"
										value="checked" checked />
									<label for="chk_box_class_code_checker">십진분류</label>
								</div>
								<!-- <div class="form-inline" style="margin-left:10px;">
									<input type="checkbox" id="chk_box_collection_books_copy_code" name="chk_box_collection_books_copy_code"
										value="checked" />
									<label for="chk_box_collection_books_copy_code">전집류복본기호</label>
								</div> -->
							</div>
							<!-- search와 regBookOk 두가지 form 제출을 위한 분기 -->
							<!-- <div style="width:0; height:0; visibility:hidden">
								<input type="checkbox" name="chkBoxRegOk" value="checked" />
								<input type="checkbox" name="chkBoxVolumeCodeAlarm" value="checked" />
								<input type="checkbox" name="chk_box_sound_eff" value="checked" />
							</div> -->
							
							<div class="makeAtc">
								<div class="form-inline mb-1">
									<div class="form-group col-md-12">
										<label for='bookTitle' class="col-md-2 control-label">도서명</label>
										<div class="col-md-10">
											<input type="text" name="bookTitle" id="bookTitle"
												class="form-control form-control-sm input-clear" style="width: 97.5%;"
												placeholder="도서 제목" />
										</div>
									</div>
								</div>

								<div class="form-inline mb-1">
									<div class="form-group col-md-12">
										<label for='author' class="col-md-2 control-label">저자명</label>
										<div class="col-md-4">
											<input type="text" name="author" id="author"
												class="form-control form-control-sm input-clear" />
										</div>

										<label for='authorCode' class="col-md-2 control-label">저자기호</label>
										<div class="col-md-4">
											<input type="text" name="authorCode" id="authorCode"
												class="form-control form-control-sm input-clear" />
										</div>
									</div>
								</div>
							</div>

								<div class="form-inline mb-1">
									<div class="form-group col-md-12">
										<div class="col-md-2 control-label">
											<label for='classificationCode' class="control-label">
											분류기호</label>
											<!-- 아래 분류기호 출처 -->
											<div class="text-center" id="srcClassCode" style="font-size:10px;">
												(출처)
											</div>
										</div>
										<div class="col-md-4">
											<input type="text" name="classificationCode"
												id="classificationCode" class="form-control form-control-sm input-clear" />
										</div>

										<label for='additionalCode' class="col-md-2 control-label">별치기호</label>
										<div class="col-md-4">
											<c:choose>
												<c:when test="${loginInfo.idLibMng eq 4}">
													<select name="additionalCode" id="additionalCode"
														class="form-control form-control-sm">
														<option value="" selected>-선택-</option>
														<option value="재단">재단</option>
														<option value="현황">국외문화재 현황자료</option>
														<option value="연속">연속간행물</option>
														<option value="경매">경매도록</option>
														<option value="大">大</option>
													</select>
												</c:when>
												<c:otherwise>
													<input type="text" name="additionalCode" id="additionalCode"
														class="form-control form-control-sm input-clear" placeholder="" />
												</c:otherwise>
											</c:choose>
										</div>
									</div>
								</div>

								<div class="form-inline mb-1">
									<div class="form-group col-md-12">
										<label for='volumeCode' class="col-md-2 control-label">
											권차기호</label>
										<div class="col-md-4">
											<input type="text" name="volumeCode" id="volumeCode"
												class="form-control form-control-sm input-clear" placeholder="숫자만 기입하세요." />
										</div>
										
										<label for='copyCode' class="col-md-2 control-label">
											복본기호</label>
										<div class="col-md-4">
											<!-- <span style="">C</span> -->
											<input type="text" name="copyCode" id="copyCode"
												class="form-control form-control-sm input-clear" placeholder="숫자만 기입해주세요." />
										</div>
									</div>
								</div>

								<div class="form-inline mb-1">
									<div class="form-group col-12">
										<div class="col-md-2 ">
											<label for='bookCateg' class="control-label">도서분류</label>
										</div>
										<div class="col-md-10">
											<input type="text" name="bookCateg" id="bookCateg"
												class="form-control form-control-sm input-clear" style='width: 97.5%;'
												placeholder="도서 분류" />
										</div>
									</div>
								</div>

								<div class="form-inline mb-1">
									<div class="form-group col-md-12">
										<label for='publisher' class="col-md-2 control-label">출판사</label>
										<div class="col-md-4">
											<input type="text" name="publisher" id="publisher"
												class="form-control form-control-sm input-clear" />
										</div>

										<label for='pubDate'
											class="col-md-2 col-md-offset-1 control-label">출판일</label>
										<div class="col-md-4">
											<input type="text" name="pubDate" id="pubDate"
												class="form-control form-control-sm input-clear" />
										</div>
									</div>
								</div>

								<div class="form-inline mb-1">
									<div class="form-group col-md-12">
										<div class="col-md-2">
											<label for='itemPage' class="control-label">페이지</label>
										</div>
										<div class="col-md-4">
											<input type="text" name="itemPage" id="itemPage" class="form-control form-control-sm input-clear" />
										</div>
										<div class="col-md-2">
											<label for='price' class="control-label">가격</label>
										</div>
										<div class="col-md-4">
											<input type="text" name="price" id="price"
												class="form-control form-control-sm input-clear" />
										</div>
									</div>
								</div>

								<div class="form-inline mb-1">
									<div class="form-group col-md-12">
										<label for='bookOrNot' class="col-md-2 control-label"
											style="font-size:13px;">
											도서/비도서
										</label>
										<div class="col-md-4">
											<select name="bookOrNot" class="form-control form-control-sm">
												<option value="BOOK" selected>국내도서</option>
												<option value="MUSIC">음반</option>
												<option value="DVD">DVD</option>
												<option value="FOREIGN">외국도서</option>
												<option value="EBOOK">전자책</option>
											</select>
										</div>

										<label for='purOrDon' class="col-md-2 control-label">수입구분</label>
										<div class="col-md-4">
											<select name="purOrDon" class="form-control form-control-sm">
												<option value="1" selected>구입</option>
												<option value="0">기증</option>
											</select>
										</div>
									</div>
								</div>

								<div class="form-inline mb-1">
									<div class="form-group col-md-12">
										<div class="col-md-2">
											<label for='isbn13' class="control-label">ISBN13</label>
										</div>
										<div class="col-md-4">
											<input type="text" name="isbn13" id="isbn13"
												class="form-control form-control-sm input-clear" placeholder="ISBN 13자리" />
												<!-- 서가를 넣기 위해, isbn10은 히든처리 -->
											<input type="hidden" name="isbn10" id="isbn10"
												class="form-control form-control-sm input-clear" placeholder="ISBN 10자리" />
											<!-- 서가를 넣기 위해, isbn10은 히든처리 -->
										</div>
										<div class="col-md-2">
											<label for='bookShelf' class="control-label">서가</label>
										</div>
										<div class="col-md-4">
											<input type="text" name="bookShelf" id="bookShelf"
												class="form-control form-control-sm input-clear" placeholder="서가" />
										</div>
									</div>
								</div>
								
								<div class="form-inline mb-1">
									<div class="form-group col-md-12">
										
										<div class="col-md-2">
											<label for='bookSize' class="control-label">도서크기</label>
										</div>
										<div class="col-md-4">
											<input type="text" name="bookSize" id="bookSize"
												class="form-control form-control-sm input-clear" placeholder="Book Size" />
										</div>
									
										<div class="col-md-2">
											<label for='rfId' class="control-label">RF ID</label>
										</div>
										<div class="col-md-4">
											<input type="email" name="rfId" id="rfId"
												class="form-control form-control-sm input-clear" placeholder="RF ID"
												value="" />
										</div>
										
									</div>
								</div>

								<div class="form-inline mb-1">
									<div class="form-group col-md-12">
										<%-- <label for='idCountry' class="col-md-2 control-label">
											도서 국가</label>
										<div class="col-md-4">
											<select name="idCountry" class="form-control form-control-sm">
												<c:forEach var="country" items="${countryList}">
													<c:set var="choice_country" value="" />
													<c:if test="${country.nameCountry == '미지정'}">
														<c:set var="choice_country" value="selected" />
													</c:if>
													<option value="${country.idCountry}" ${choice_country}>
														${country.nameCountry}
													</option>
												</c:forEach>
											</select>
										</div> --%>
										
										<label for='newBarcode' class="col-md-2 control-label">
											도서등록번호</label>
										<div class="col-md-4">
											<input type="text" name="newBarcode" id="newBarcode"
												class="form-control form-control-sm" value="${newBarcode}"
												placeholder="공란일 경우 숫자로만 작성됨." />
										</div>
									</div>
								</div>
								
								<div class="form-inline mb-3">
									<div class="form-group col-12">
										<div class="col-md-2 ">
											<label for='tagBook' class="control-label">메모(태그)</label>
										</div>
										<div class="col-md-10">
											<input type="text" name="tagBook" id="tagBook"
												class="form-control form-control-sm input-clear" style='width: 97.5%;'
												placeholder="비고" />
										</div>
									</div>
								</div>

								<div class='form-horizontal'>
									<div class="form-group col-11" style="margin:auto; margin-bottom:14px;">
										<label for='bookDesc' class="col-md-4">도서 설명</label>

										<div class="form-inline">
											<div class="form-control form-control-sm col-md-2 input-clear"
												style="border: 1px solid black; width: 100px; height: 130px;">
												<img id="bookCover"
													src="" />
												<input type="hidden" name="bookCover" id="input__bookCover"/>
											</div>
											<textarea
												class="txt-box form-control form-control-sm custom-control col-md-7 input-clear"
												name='bookDesc' id="bookDesc"
												style="resize: none; height: 130px; width: 60%;"></textarea>
												
											<div class="ml-3 float-right">
												<ul class="list-group">
													<li class="py-0 list-group-item">
														<div id="summaryClassHeadCode" style="color:#D3D3D3;">
															대분류
														</div>
													</li>
													<li class="py-0 list-group-item">
														<div id="summaryAdditionalCode" style="color:#D3D3D3;">
															별치기호
														</div>
													</li>
													<li class="py-0 list-group-item">
														<div id="summaryClassCode" style="color:#D3D3D3;">
															십진분류
														</div>
													</li>
													<li class="py-0 list-group-item">
														<div id="summaryAuthorCode" style="color:#D3D3D3;">
															저자기호
														</div>
													</li>
													<li class="py-0 list-group-item">
														<div id="summaryVolumeCode" style="color:#D3D3D3;">
															권차기호
														</div>
													</li>
													<li class="py-0 list-group-item">
														<div id="summaryCopyCode" style="color:#D3D3D3;">
															복본기호
														</div>
													</li>
												</ul>
											</div>
										</div>
									</div>
								</div>


								<div class="form-group">
									<div class="text-center">
										<button type="submit" class="btn btn-primary" id="btnRegSbm">
											도서등록하기
										</button>
										<input type="button" class="btn btn-danger" onclick="clearInput()" value="다시작성" />
									</div>
								</div>
							</form>
							<!-- 회원정보, 도서정보 끝 -->
							
							<div>
								<%@ include file="/WEB-INF/inc/aladin_reference.jsp"%>
							</div>
						</div>
						<!-- table responsive 끝 -->
					</div>
					<!-- card body 끝 -->
				</div>
				<!-- card 끝 -->
				
				<!-- 등록된 도서 목록 -->
				<%@ include file="/WEB-INF/views/book/reg_side_status/reg_side_list.jsp"%>
				<!-- 등록된 도서 목록 -->
				
				<input type="hidden" id="dupNotice" value="${dupNotice}"/>
			</div>
			<!-- container fluid 끝 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
		<!-- content wrapper 끝 -->
	</div>
	<!-- wrapp 끝 -->
	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>

<!-- 소리재생 소스 -->
<audio id="checkSoundSuccess" src="/assets/sound_eff/check_success.mp3"></audio>
<audio id="checkSoundWarning" src="/assets/sound_eff/check_warning.mp3"></audio>
<audio id="checkSoundError" src="/assets/sound_eff/check_error.mp3"></audio>
<!-- 소리재생 소스 -->

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/book-reg/book_reg.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/book-reg/book_search.js"></script>

<script type="text/javascript">
	
	//ISBN 예시 불러오기.
	$("#searchEx").click(function(e) {
		e.preventDefault();
		$("#search-book-info").val("9788984314818");
	});
	
	//국중 검색 팝업창 띄우기
	function openNlPopup() {
		var isbn = $("#search-book-info").val();
		var url = '';
		if(isbn != ''){
			url = '${pageContext.request.contextPath}/book/search_nl_book.do?searchOpt=1&search-book-info='+isbn;
		} else{
			url = '${pageContext.request.contextPath}/book/search_nl_book.do';
		}
		window.open(url, '_blank', 'width=800,height=700,scrollbars=yes');
	};
	
	const thisAddiCode = document.getElementById('additionalCode');
	thisAddiCode.addEventListener('change', (e) => {
		var thisAddiCodeVal = thisAddiCode.value;
		document.getElementById('summaryAdditionalCode').style.color = 'black';
		document.getElementById('summaryAdditionalCode').innerHTML = thisAddiCodeVal
	});
	
	$("#classificationCode").change(function() {
		var thisCode = document.getElementById('classificationCode').value;
		document.getElementById('summaryClassCode').style.color = 'black';
		document.getElementById('summaryClassCode').innerHTML = thisCode;
		//대분류도 변경
		document.getElementById('summaryClassHeadCode').style.color = 'black';
		document.getElementById('summaryClassHeadCode').innerHTML = thisCode.substring(0,1)*100;
	});
	
	$("#authorCode").change(function() {
		var thisCode = document.getElementById('authorCode').value;
		document.getElementById('summaryAuthorCode').style.color = 'black';
		document.getElementById('summaryAuthorCode').innerHTML = thisCode;
	});
	
	$("#volumeCode").change(function() {
		var thisCode = document.getElementById('volumeCode').value;
		document.getElementById('summaryVolumeCode').style.color = 'black';
		document.getElementById('summaryVolumeCode').innerHTML = "V"+thisCode;
	});
	
	$("#copyCode").change(function() {
		var thisCode = document.getElementById('copyCode').value;
		document.getElementById('summaryCopyCode').style.color = 'black';
		document.getElementById('summaryCopyCode').innerHTML = "C"+thisCode;
	});
	
	//제목과 저자명 input 값이 바뀌면 저자기호 코드 호출
	const makeAtc = document.querySelector('.makeAtc');
	makeAtc.addEventListener('change', function(){
		makeAuthorCode();
	});
	
	//저자코드 생성 ajax 호출문
	function makeAuthorCode() {
		
		var thisIsbn = document.getElementById('search-book-info').value;
		var thisTitle = document.getElementById('bookTitle').value;
		var thisAuthor = document.getElementById('author').value;
		var atcout = null;
		
		if(thisTitle!=null&&thisAuthor!=null) {
			$.ajax({
				url: "${pageContext.request.contextPath}/book/author_code.do",
				type: 'POST',
				data: {
					thisIsbn,
					thisTitle,
					thisAuthor,
				},
				/* dataType: "json", */
				success: function(data) {
					if(data.rt != 'OK') {
						alert(data.rt);
					} else {
						document.getElementById('authorCode').value = data.result;
						document.getElementById('copyCode').value = data.copyCode;
					}
				}
			});
		}
	};
	
	$(".classStraightReg").on("click", function(e) {
		document.getElementById('search-book-info').focus();
	});
	
	//화면 로딩시 동작 함수들
	window.onload = function() {
		var dupNotice = document.getElementById('dupNotice').value;
		if(dupNotice != '' ){
			alert('중복 등록번호가 존재합니다. ('+dupNotice+')');
		}
		
		/* var thisClsInput = document.getElementById('classificationCode');
		if(!isNaN(thisClsInput.value)&&(thisClsInput.value!='')){
			thisClsInput.classList.remove('is-invalid');
		} else {
			thisClsInput.classList.add('is-invalid');
		} */
		
		document.getElementById('search-book-info').focus();
		
		//RF ID 포커싱
		/* var curBookTitle = document.getElementById('bookTitle').value;
		var curAuthor = document.getElementById('author').value;
		var curClsCode = document.getElementById('classificationCode').value;
		
		if(curBookTitle && curAuthor && curClsCode){
			/* document.getElementById('btnRegSbm').focus();
			document.getElementById('rfId').focus();
		} */
	};
	
	//rfId 영문 기입을 위해, 이메일 형식으로 넣어주고, 컨트롤러에서 @ 이후 값을 뺀다.
	var rfIdChk = document.getElementById('rfId');
	rfIdChk.addEventListener('change', (e)=>{
		rfIdChk.value = rfIdChk.value + '@1.com';
	});
	

</script>
</html>