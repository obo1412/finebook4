<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!doctype html>
<html>
<head>
<meta charset='utf-8' />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>회원 목록</title>

<%@ include file="/WEB-INF/inc/head.jsp"%>

<style>
	table { 
		table-layout: fixed;
	}
	
	tr > td {
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
</style>
</head>

<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>

	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">
				<div class="card mb-3">
					<div class="card-header">
						<h4 class='pull-left'>회원 목록</h4>
					</div>
					<div class="card-body">
						<!-- 검색폼 + 추가버튼 -->
						<div class="float-left mr-2">
							<button class="btn btn-sm btn-secondary" onclick="checkAll()">전체선택/해제</button>
						</div>
						
						<div style="" class="float-left">
							<form method='get' name="searchFormMember"
								action='${pageContext.request.contextPath}/member/member_list.do'
								style="width: 350px;">
								<div class="input-group input-group-sm">
									<span class="input-group-prepend">
										<input type="hidden" id="classIdFromCntl" value="${searchClassId}"/>
										<select name="searchClassId" id="idSearchClassId" class="form-control form-control-sm">
												<option value="">전체</option>
												<option value="-1">미분류</option>
												<c:forEach var="classMemberList" items="${classList}">
													<c:set var="choice_class" value="" />
													<c:if test="${classMemberList.idMbrClass == searchClassId}">
														<c:set var="choice_class" value="selected" />
													</c:if>
													<option value="${classMemberList.idMbrClass}" ${choice_class}>
														${classMemberList.className}
													</option>
												</c:forEach>
										</select>
									</span>
									<input type="text" name='keyword' class="form-control"
										placeholder="회원 이름 검색" value="${keyword}" autofocus/> <span
										class="input-group-append">
											<button class="btn btn-success btn-sm" type="submit">
												<i class='fas fa-search'></i>
											</button>
										<a href="${pageContext.request.contextPath}/member/join.do"
										class="btn btn-primary btn-sm">회원 추가</a>
									</span>
								</div>
							</form>
							
						</div>
						
						<div class="float-right">
							<button class="btn btn-sm btn-danger float-left mr-1"
								data-toggle="modal" data-target="#inactivate_member_batch">
								체크회원일괄삭제
							</button>
							<div class="dropdown float-left">
								<button class="btn btn-sm btn-info dropdown-toggle"
									id="btnDropdownPrintMembership" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
									회원증 출력
								</button>
								<div class="dropdown-menu dropdown-menu-right" aria-labelledby="btnDropdownPrintMembership">
									<a class="dropdown-item" href="#" onclick="memberListToExcel()">엑셀변환</a>
									<hr />
									<a class="dropdown-item" href="#" onclick="printMembershipA4()">A4용지</a>
									<!-- <a class="dropdown-item" href="#">카드(현재 미지원)</a> -->
								</div>
							</div>
						</div>
						
						<!-- 일괄삭제를 위한 모달 -->
						<div class="modal fade" id="inactivate_member_batch" tabindex="-1" role="dialog"
							aria-labelledby="inactivateMemberBatchModal" aria-hidden="true">
							<div class="modal-dialog" role="document">
								<div class="modal-content">
								
									<div class="modal-header">
										<h5 class="modal-title">
											체크된 회원 일괄 삭제
										</h5>
										<button class="close" type="button" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">×</span>
										</button>
									</div>
									
									<div class="modal-body">
										<div>체크된 회원을 일괄 삭제 하시겠습니까?</div>
										<div>삭제 후에는 돌이킬 수 없습니다.</div>
									</div>
									
									<div class="modal-footer">
										<button class="btn btn-danger" onclick="inactivateMemberList()">
											삭제하기
										</button>
										
										<button class="btn btn-secondary" type="button"
											data-dismiss="modal">취소</button>
									</div>
									
								</div>
							</div>
						</div>
						<!-- 일괄삭제를 위한 모달 끝 -->

						<!-- 조회결과를 출력하기 위한 표 -->

						<table class="table table-sm">
							<thead>
								<tr>
									<th class="table-info text-center" style="width:20px;">-</th>
									<th class="table-info text-center" style="width:30px;">번호</th>
									<th class="table-info text-center" style="width:50px;">이름</th>
									<th class="table-info text-center" style="width:60px;">생년월일</th>
									<th class="table-info text-center" style="width:70px;">연락처</th>
									<th class="table-info text-center" style="width:70px;">추가연락처</th>
									<th class="table-info text-center" style="width:100px;">이메일</th>
									<th class="table-info text-center" style="width:50px;">회원분류</th>
									<th class="table-info text-center" style="width:50px;">회원등급</th>
									<th class="table-info text-center" style="width:50px;">대출제한(권)</th>
									<th class="table-info text-center" style="width:50px;">대출기한(일)</th>
								</tr>
							</thead>
							<tbody class="searchCls">
								<c:choose>
									<c:when test="${fn:length(list) > 0}">
										<c:forEach var="item" items="${list}" varStatus="status">
											<tr>
												<td class="text-center">
													<input type="hidden" value="${item.id}"/>
													<input type="checkbox" class="chk-box-member-id"/>
												</td>
												<td class="text-center">${page.indexLast - status.index}</td>
												<td class="text-center"><c:url var="readUrl" value="/member/member_view.do">
														<c:param name="memberId" value="${item.id}" />
													</c:url>
													<a href="${readUrl}" onclick="window.open(this.href, '_blank','width=650,height=800,scrollbars=yes');return false;">
														${item.name}
													</a>
												</td>
												<td class="text-center" id="userCode"><fmt:parseDate
														var="birthdateStr" value="${item.birthdate}"
														pattern="yyyy-MM-dd" /> <!-- DB에서 가져온 String형 데이터를 Date로 변환
											var 변수명, pattern 날짜형식, value 컨트롤러에서 받은값 var에 저장 --> <fmt:formatDate
														var="birthdateFmt" value="${birthdateStr}"
														pattern="yyyy-MM-dd" /> <!-- Date형으로 저장된 값을 String으로 변환 var변수에 넣고 아래처럼 변수를 사용 -->
													${birthdateFmt}</td>
												<td class="text-center">${item.phone}</td>
												<td class="text-center">${item.otherContact}</td>
												<td class="text-center">${item.email}</td>
												<td class="text-center">${item.className}</td>
												<td class="text-center">${item.gradeName}</td>
												<td class="text-center">${item.brwLimit}</td>
												<td class="text-center">${item.dateLimit}</td>
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

						<!-- 페이지 번호 -->
						<%@ include file="/WEB-INF/inc/common_pagination_bottom.jsp" %>
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
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/member-js/check_box.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/member-js/member_list.js"></script>
</body>
</html>



