<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/inc/head.jsp"%>
<link href="/assets/css/book/update_5code_batch.css" rel="stylesheet" >
<style type="text/css">
.card {
	float:left;
}

.card-body {
	font-size: 11pt;
}
</style>

</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>
	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">
				
				<div class="card mb-3 mr-2" style="width:400px;">
					<div class="card-header">
						<h4>기호 일괄 수정하기</h4>
					</div>
					<div class="card-body">
						<div class="table-responsive row">

							<!-- 회원정보, 도서정보 수집 시작 -->
							<form class="form-horizontal info-section" name="batchForm"
								enctype="multipart/form-data" method="post">
									
									<select name="codeType" class="select__code__type">
										<option value="">--선택값없음--</option>
										<option value="bookShelf">서가</option>
										<option value="addiCode">별치기호</option>
										<option value="classCode">분류기호(숫자만)</option>
										<option value="authorCode">저자기호</option>
										<option value="volumeCode">권차기호</option>
										<option value="copyCode">복본기호(숫자만)</option>
										<option value="barcode">바코드자리수 변경</option>
										<option value="discard">도서 일괄 폐기</option>
										<option value="bookRfId">도서RFID수정(엑셀)</option>
										<option value="purOrDon">구입/기증</option>
									</select>
									
									<div class="div__same__value__control">
										<label for="chgValue">
											<span class="common__label">변경값(바코드자리수일 경우 자리수 숫자 기입)</span>
											<span class="purOrDon__label div__hidden">변경값(구입 : 1 / 기증 : 0)</span>
										</label>
										<input type="text" name="chgValue" id="chgValue" class="form-control form-control-sm"/>
										
										
										<div class="form-group col-12">
											<label for='batchFile' class="col-12 control-label">txt/xlsx File</label>
											<div class="col-10">
												<input type="file" name="batchFile" id="batchFile"
													class="form-control form-control-sm input-clear"
													placeholder="파일 로드" />
											</div>
										</div>
									</div>

									<div class="div__volume__code__control div__hidden">
										<label>
											시작 권차기호
											<input type="text" name="volumeCodeStart" value="1" />
										</label>
										<label>
											변경 시작 도서등록번호
											<input type="text" name="barcodeStart" />
										</label>
										<label>
											변경 마지막 도서등록번호
											<input type="text" name="barcodeEnd" />
										</label>
									</div>

								<div class="form-group">
									<div class="col-12">
										<button type="submit" class="btn btn-primary btn-sm" formaction="${pageContext.request.contextPath}/book/update_addi_code_batch_ok.do">변경하기</button>
										<input type="button" class="btn btn-danger btn-sm" onclick="clearInput()" value="다시작성" />
									</div>
								</div>
								
							</form>
							<!-- 회원정보, 도서정보 끝 -->
						</div>
						<!-- table responsive 끝 -->
					</div>
					<!-- card body 끝 -->
				</div>
				<!-- card 끝 -->
				
			</div>
			<!-- container fluid 끝 -->
			<%@ include file="/WEB-INF/inc/footer.jsp"%>
		</div>
		<!-- content wrapper 끝 -->
	</div>
	<!-- wrapp 끝 -->

	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
	<!-- 도서목록 js 현재는 서가/분류 처리 js 포함 -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/book/book_update/update_5code_batch.js"></script>
</body>

</html>