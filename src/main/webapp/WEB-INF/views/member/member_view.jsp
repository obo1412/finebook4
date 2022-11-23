<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/inc/taglib_jstl.jsp"%>
<!doctype html>
<html>
<head>
<meta charset='utf-8' />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>회원 상세 보기</title>

<%@ include file="/WEB-INF/inc/head.jsp"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/member/member_view.css" />
</head>

<body>
	<div id="wrapper">
		<div id="content-wrapper">
			<div class="container-fluid">
				<h4 class='page-header'>회원 상세 보기</h4>
				
				<!-- 버튼 -->
				<div class="text-right">
					<a
						href="${pageContext.request.contextPath}/member/member_edit.do?memberId=${memberItem.id}"
						class="btn btn-warning">수정</a> <a
						href="${pageContext.request.contextPath}/member/member_delete.do?memberId=${memberItem.id}"
						class="btn btn-danger" data-toggle="modal" data-target="#inactive_member_modal">삭제</a> <input type="button"
						class="btn btn-primary closeRefresh" value="닫기" onclick="self.close()" />
				</div>
				
				<!-- 조회결과를 출력하기 위한 표 -->
				<table class="table table-sm table-bordered mt-2 table__member__info">
					<tbody>
						<tr>
							<th class="table-info text-center" width="130">번호</th>
							<td>${memberItem.id}</td>
						</tr>
						<tr>
							<th class="table-info text-center">이름</th>
							<td>${memberItem.name}</td>
						</tr>
						<tr>
							<th class="table-info text-center">연락처</th>
							<td>${memberItem.phone}</td>
						</tr>
						<tr>
							<th class="table-info text-center">추가 연락처</th>
							<td>${memberItem.otherContact}</td>
						</tr>
						<tr>
							<th class="table-info text-center">생년월일</th>
							<fmt:parseDate var="parsebirthDate" value="${memberItem.birthdate}" pattern="yyyy-MM-dd"/>
							<fmt:formatDate var="birthDate" value="${parsebirthDate}" pattern="yyyy-MM-dd" />
							<td>${birthDate}</td>
						</tr>
						<tr>
							<th class="table-info text-center">이메일</th>
							<td>${memberItem.email}</td>
						</tr>
						<tr>
							<th class="table-info text-center">우편번호</th>
							<td>${memberItem.postcode}</td>
						</tr>
						<tr>
							<th class="table-info text-center">주소</th>
							<td>${memberItem.addr1}</td>
						</tr>
						<tr>
							<th class="table-info text-center">상세주소</th>
							<td>${memberItem.addr2}</td>
						</tr>
						<tr>
							<th class="table-info text-center">가입일</th>
							<td>${memberItem.regDate}</td>
						</tr>
						<tr>
							<th class="table-info text-center">바코드</th>
							<td>${memberItem.barcodeMbr}</td>
						</tr>
						<tr>
							<th class="table-info text-center">프로필이미지</th>
								<td>
									<c:choose>
										<c:when test="${memberItem.profileImg ne null}">
											<img style="width:100px; height:133px;" 
												src="/filesMapping/upload/finebook4/memberImg/libNo${memberItem.idLib}/${profileName}" alt="profileImg"/>
										</c:when>
										<c:otherwise>
											프로필 사진이 없습니다.
										</c:otherwise>
									</c:choose>
								</td>
						</tr>
						<tr>
							<th class="table-info text-center">RF-UID</th>
							<td>${memberItem.rfUid}</td>
						</tr>
						<tr>
							<th class="table-info text-center">회원분류</th>
							<td>${memberItem.className}</td>
						</tr>
						<tr>
							<th class="table-info text-center">등급이름</th>
							<td>${memberItem.gradeName}</td>
						</tr>
						<tr>
							<th class="table-info text-center">대출가능권수</th>
							<td>${memberItem.brwLimit}</td>
						</tr>
						<tr>
							<th class="table-info text-center">대여기한</th>
							<td>${memberItem.dateLimit}</td>
						</tr>
					</tbody>
				</table>
				
				<div>
					<table class="table table-sm table-bordered mt-2 brw__log__member">
						<thead>
							<tr>
								<th class="table-warning text-center member__th__num">번호</th>
								<th class="table-warning text-center">도서명</th>
								<th class="table-warning text-center">저자명</th>
								<th class="table-warning text-center">등록번호</th>
								<th class="table-warning text-center">대출일</th>
								<th class="table-warning text-center">반납일</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${fn:length(brwLog) >0}">
									<c:forEach var="brwItem" items="${brwLog}" varStatus="status">
										<tr>
											<td class="text-center">${status.count}</td>
											<td class="text-center">
												<div class="td__title">
													<c:url var="bookUrl" value="/book/book_held_edit.do">
														<c:param name="bookHeldId" value="${brwItem.bookHeldId}" />
													</c:url>
													<a href="${bookUrl}" onclick="window.open(this.href, '_blank','width=550,height=800,scrollbars=yes');return false;">
														${brwItem.title}
													</a>
												</div>
											</td>
											<td class="text-center">
												<div class="td__writer">
													${brwItem.writer}
												</div>
											</td>
											<td class="text-center">${brwItem.localIdBarcode}</td>
											
											<fmt:parseDate var="parseStartDate" value="${brwItem.startDateBrw}" pattern="yyyy-MM-dd"/>
											<fmt:formatDate var="startDate" value="${parseStartDate}" pattern="yyyy-MM-dd" />
											<td class="text-center">${startDate}</td>
											
											<fmt:parseDate var="parseEndDate" value="${brwItem.endDateBrw}" pattern="yyyy-MM-dd"/>
											<fmt:formatDate var="endDate" value="${parseEndDate}" pattern="yyyy-MM-dd" />
											<td class="text-center">${endDate}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="6" class="text-center">
											대출/반납 이력이 없습니다.
										</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
			<!-- container-fluid 끝 -->
		</div>
		<!-- content-wrapper 끝 -->
	</div>
	<!-- wrapper 끝 -->
	<div class="modal fade" id="inactive_member_modal" tabindex="-1" role="dialog"
					aria-labelledby="exampleModalLabel" aria-hidden="true">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="exampleModalLabel">회원 정보 삭제</h5>
								<button class="close" type="button" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">×</span>
								</button>
							</div>
							<form name="member_inactive" method="post">
							<input type="hidden" name="memberId" value="${memberItem.id}"/>
							<div class="modal-body">
								<div>정말로 회원정보를 삭제하시겠습니까?</div>
							</div>
							<div class="modal-footer">
								<button class="btn btn-secondary" type="button"
									data-dismiss="modal">취소</button>
								<input type="submit" class="btn btn-warning" value="삭제" formaction="${pageContext.request.contextPath}/member/member_inactive_ok.do"/>
							</div>
							</form>
						</div>
					</div>
				</div>
<%@ include file="/WEB-INF/inc/script-common.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/member-js/member_view.js"></script>

</body>
</html>