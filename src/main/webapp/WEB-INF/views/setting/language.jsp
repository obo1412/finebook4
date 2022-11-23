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
						<h4>언어 / Language</h4>
					</div>

					<div class="card-body">
						<!-- 가입폼 시작 -->
						<form class="form-horizontal" name="myform" method="post"
							action="${pageContext.request.contextPath}/setting/language_change_ok.do">

							<div class="form-group">
								<div class="col-md-6">
									<select name="language" class="form-control form-control-sm">
										<c:set var="kor" value="" />
										<c:set var="eng" value="" />
										<c:choose>
											<c:when test="${loginInfo.langMng eq 0}" >
												<c:set var="kor" value="selected" />
											</c:when>
											<c:otherwise>
												<c:set var="eng" value="selected" />
											</c:otherwise>
										</c:choose>
										<option value="0" ${kor}>한글 / Korean</option>
										<option value="1" ${eng}>영어 / English</option>
									</select>
									
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
	


	<%@ include file="/WEB-INF/inc/script-common.jsp"%>
</body>
</html>