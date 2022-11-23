<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/inc/head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>

	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">
				<div class="card mb-3">
					<div class="card-header">
						<h4>도서관 정보 관리</h4>
					</div>

					<div class="card-body">
						<!-- 가입폼 시작 -->
						<form class="form-horizontal" name="myform" method="post"
							action="${pageContext.request.contextPath}/setting/library_setting_change_ok.do">

							<div class="form-group">
								<div class="col-md-6">
									<div class="form-group row">
										<label class="col-form-label">도서관명</label>
										<input class="form-control" name="nameLib" value="${libInfo.nameLib}" readonly/>
									</div>
									<div class="form-group row">
										<label class="col-form-label">도서관위치</label>
										<input class="form-control" name="locLib" value="${libInfo.locLib}"/>
									</div>
									<div class="form-group row">
										<label class="col-form-label">조회Key</label>
										<input class="form-control" name="skl" value="${libInfo.stringKeyLib}" readonly/>
									</div>
									<div class="form-group row">
										<label class="col-form-label">만료일</label>
										<input class="form-control" name="expDate" value="${libInfo.expDate}" readonly/>
									</div>
									<div class="form-group row">
										<label class="col-form-label">등록번호자리수</label>
										<div class="input-group">
											<input class="form-control" name="nodBarcode" id="nodBarcode" value="${libInfo.numOfDigitBarcode}" />
											<div class="input-group-append">
												<input type="button" class="btn btn-secondary" value="수정" onclick="clickedChangeNod()"/>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<div class="col-md-offset-2 col-md-6">
									<button type="submit" class="btn btn-primary">수정하기</button>
									<a class="btn btn-warning" href="${pageContext.request.contextPath}/index.do">기본화면으로</a>
								</div>
							</div>

						</form>
						<!-- 가입폼 끝 -->
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
	<!-- wrapper 끝 -->
	
	<!-- 도서관 관리 페이지 -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/lib-setting/library_setting.js"></script>
	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>
</html>