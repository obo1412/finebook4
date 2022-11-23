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
		white-space: nowrap;
	}
	
	table { 
		table-layout: fixed;
	}
	
	tr > td {
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	
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
						<h4 style="float:left;">메일보내기 테스트</h4>
						<div style="float:right;">
							
						</div>
					</div>
					<div class="card-body">
						<div class="table-responsive row">

							<form class="form-horizontal search-box" name="search-mbr-form"
								id="search-mbr-form" method="get"
								action="${pageContext.request.contextPath}/test_lab/send_mail_test_ok.do">

								<button type="submit">테스트메일전송</button>

							</form>

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

</script>
</html>