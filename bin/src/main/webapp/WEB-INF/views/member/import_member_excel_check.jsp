<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/inc/head.jsp"%>
<style type="text/css">
.card {
	float:left;
	width: 100%;
	height:800px;
}

.card-body {
	font-size: 11pt;
	overflow: auto;
}

.cur-th, .cur-td {
	width: 100px;
	white-space: nowrap;
	overflow: hidden;
}

.div-btns {
	margin-bottom:0;
}

label {
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
				
				<div class="card mb-3 mr-2">
				
					<div class="card-header">
						<div style="float:left;">
							<h4>회원 데이터 엑셀 등록</h4>
						</div>
						
						<div style="float:left; margin-left:10px;">
							<progress id="progBar" max="${row_crt}" value="0"></progress>
						</div>
						
						<div class="form-group float-right div-btns">
							<input id="curRow" value="" />
							<button class="btn btn-primary btn-sm" onclick="clickedBtnSubmit()">등록하기</button>
							<button class="btn btn-danger btn-sm" onclick="cancelProcess()">전송중단</button>
						</div>
					</div>
					
					<div class="card-body">
						<div class="table-responsive row">

							<!-- 회원정보, 도서정보 수집 시작 -->
							
							<form class="form-horizontal info-section" name="batchForm"
								method="post">
								
								<div>
									<input type="hidden" id="timeStandard" name="timeStandard" value="${timeStandard}"/>
									<input type="hidden" id="row_crt" name="row_crt" value="${row_crt}"/>
									<input type="hidden" id="lastCellCount" name="lastCellCount" value="${lastCellCount}"/>
									<input type="hidden" id="loadFilePath" name="loadFilePath" value="${loadFilePath}"/>
									<input type="hidden" id="fileId" name="fileId" value="${fileId}"/>
								</div>
								
								<table class="table table-sm">

									<thead class="theadCls">
										<c:forEach var="row" items="${theArr}" varStatus="firStat">
											<c:if test="${firStat.index eq 0}">
												<tr>
													<c:forEach var="col" items="${row}" varStatus="seStat">
														<th class="text-center cur-th">
															<c:set var="opt" value="" />
															<c:if test="">
																<c:set var="opt" value="selected" />
															</c:if>
															<select id="colH${seStat.index}" class="selectCls">
																<option value="">목록제외</option>
																<option value="name">회원명</option>
																<option value="phone">연락처</option>
																<option value="birthdate">생년월일</option>
																<option value="email">이메일</option>
																<option value="postcode">우편번호</option>
																<option value="addr1">주소1</option>
																<option value="addr2">주소2</option>
																<option value="remarks">비고</option>
																<option value="regDate">등록일</option>
																<option value="barcodeMbr">회원등록번호</option>
																<option value="profileImg">회원사진</option>
																<option value="rfUid">RF UID</option>
																<option value="gradeName">회원등급</option>
																<option value="status">회원상태</option>
															</select>
														</th>
													</c:forEach>
												</tr>
												<tr>
													<c:forEach var="col" items="${row}" varStatus="seStat">
														<th class="table-info text-center cur-th">${col}</th>
														<c:if test="${(firStat.index eq 0) and (seStat.last eq true)}" >
															<!-- 마지막 col값을 주기 위한 변수로 -->
															<input type="hidden" name="colLast" value="${seStat.index}" />
														</c:if>
													</c:forEach>
												</tr>
											</c:if>
										</c:forEach>
									</thead>

									<tbody>
										<c:forEach var="row" items="${theArr}" varStatus="firStat">
											<c:if test="${firStat.index gt 0}">
											<tr>
												<c:forEach var="col" items="${row}" varStatus="seStat">
													<td name="col${seStat.index}" class="text-center cur-td">
														${col}
														<input type="hidden" name="col${seStat.index}" value="${col}" />
													</td>
												</c:forEach>
											</tr>
											</c:if>
										</c:forEach>
									</tbody>

								</table>
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
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/member-js/import/member_excel_import_check.js"></script>
</html>