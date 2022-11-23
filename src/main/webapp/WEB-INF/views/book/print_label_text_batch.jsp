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
						<h4>라벨출력 text File</h4>
					</div>
					<div class="card-body">
						<div class="table-responsive row">

							<!-- 회원정보, 도서정보 수집 시작 -->
							<form class="form-horizontal info-section" name="batchForm"
								enctype="multipart/form-data"
								method="post">
									
									<div class="form-group col-12">
										<label for='batchFile' class="col-12 control-label">txt File</label>
										<div class="col-10">
											<input type="file" name="batchFile" id="batchFile"
												class="form-control form-control-sm input-clear"
												placeholder="파일 로드" />
										</div>
									</div>

								<div class="form-group">
									<div class="col-12">
										<button type="submit" class="btn btn-primary btn-sm" formaction="${pageContext.request.contextPath}/book/print_label_text_batch_check.do">출력하기</button>
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
</body>
<script type="text/javascript">
$(function() {
	/* 페이지 호출시 회원검색에 포커싱 */
	document.getElementById('txtBox').focus();

});
</script>
</html>