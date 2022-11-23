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
						<h4 style="float:left;">도서 등록하기</h4>
						<div style="float:right;">
							
						</div>
					</div>
					<div class="card-body">
						<div class="table-responsive row">

							<form class="form-horizontal search-box" name="search-mbr-form"
								id="search-mbr-form" method="get"
								action="${pageContext.request.contextPath}/test_lab/ctrl1.do">
								<div class="form-group form-inline" style="margin:0px;">
									<div class="col-md-3">
										<label for='search-book-info'>도서 검색</label>
									</div>
									<div class="input-group input-group-sm col-md-6">
										<input type="text" name="test_key"
											id="test_key" class="form-control input-clear"
											placeholder="ISBN 검색" value="${isbn}"/> <span
											class="input-group-append btn-group">
											<button class="btn btn-warning btn-sm" id="btn-search-bookinfo"
												type="submit">
												<i class="fas fa-search"></i>
											</button>
											<button class="btn btn-info btn-sm" id="searchEx">
												<i class="fas fa-question"></i>
											</button>
										</span>
									</div>
								</div>
								
								<div class="form-group form-inline" style="margin:0px 50px 10px 50px;">
									<div class="form-group form-inline classStraightReg">
										<input type="checkbox" class="classStraightReg form-control"
											id="chkBoxStraightReg" name="straightReg" value="checked" ${regCheckBox}/>
										<label for="chkBoxStraightReg">바로등록</label>
									</div>
									<div class="form-inline" style="margin-left:10px;">
										<input type="checkbox" id="chkBoxVolumeCodeAlarm" name="chkBoxVolumeCodeAlarm"
											value="checked" ${chkBoxVolumeCodeAlarm}/>
										<label for="chkBoxVolumeCodeAlarm">권차경고제외</label>
									</div>
									<div class="form-inline" style="margin-left:10px;">
										<input type="checkbox" id="chk_box_sound_eff" name="chk_box_sound_eff"
											value="checked" ${chkBoxSoundEff}/>
										<label for="chk_box_sound_eff">소리알림제외</label>
									</div>
								</div>
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