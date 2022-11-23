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
<title>회원 정보 수정</title>

<%@ include file="/WEB-INF/inc/head.jsp"%>
</head>

<body>
	<div id="wrapper">
		<div id="content-wrapper">
			<div class="container-fluid">
				<h4 class='page-header'>회원 정보 수정</h4>
				
				<form class="horizontal" name="edit_member" method="post"
					enctype="multipart/form-data"
					action="${pageContext.request.contextPath}/member/member_edit_ok.do">
					
					<input type="hidden" name="memberId" id="memberId" value="${memberItem.id}"/>
					
					<div class="form-group mb-2">
						<label for="name" class="col-12">회원 이름</label>
						<div class="col-8">
							<input name="name" id="name" class="form-control form-control-sm"
								value="${memberItem.name}"/>
						</div>
					</div>
					
					<div class="form-group mb-2">
						<label for="phone" class="col-12">연락처</label>
						<div class="col-8">
							<input name="phone" id="phone" class="form-control form-control-sm"
								value="${memberItem.phone}"/>
						</div>
					</div>
					
					<div class="form-group mb-2">
						<label for="otherContact" class="col-12">추가 연락처</label>
						<div class="col-8">
							<input name="otherContact" id="otherContact" class="form-control form-control-sm"
								value="${memberItem.otherContact}"/>
						</div>
					</div>
					
					<div class="form-group mb-2">
						<label for="birthdate" class="col-12">생년월일</label>
						<div class="col-8">
							<fmt:parseDate var="parsebirthDate" value="${memberItem.birthdate}" pattern="yyyy-MM-dd"/>
							<fmt:formatDate var="birthDate" value="${parsebirthDate}" pattern="yyyy-MM-dd" />
							<input name="birthdate" id="birthdate" class="form-control form-control-sm"
								value="${birthDate}"/>
						</div>
					</div>
					
					<div class="form-group mb-2">
						<label for="email" class="col-12">이메일</label>
						<div class="col-8">
							<input type="email" name="email" id="email" class="form-control form-control-sm"
								value="${memberItem.email}"/>
						</div>
					</div>
					
					<div class="form-group mb-2">
						<label for="barcodeMbr" class="col-12">회원등록번호</label>
						<div class="col-8">
							<p class="form-control-static form-control-sm">${memberItem.barcodeMbr}</p>
						</div>
					</div>
					
					<div class="form-group mb-2">
						<label for='idMbrClass' class="col-12">회원분류</label>
						<div class="col-8">
							<select name="idMbrClass" class="form-control form-control-sm">
								<option value="">---분류 없음---</option>
								<c:forEach var="classMemberList" items="${classList}">
									<c:set var="choice_class" value="" />
									<c:if test="${classMemberList.idMbrClass == memberItem.classId}">
										<c:set var="choice_class" value="selected" />
									</c:if>
									<option value="${classMemberList.idMbrClass}" ${choice_class}>
										${classMemberList.className}
									</option>
								</c:forEach>
							</select>
						</div>
					</div>
					
					<%-- <div class="form-group mb-2">
						<label for="profileImg" class="col-12">프로필이미지</label>
						<div class="col-8">
							<input type="file" name="profileImg" id="profileImg" class="form-control form-control-sm"
								value="${memberItem.profileImg}"/>
						</div>
					</div> --%>
					
					<div class="form-group mb-2">
						<label for="rfuid" class="col-12">RF-UID</label>
						<div class="col-8">
							<input type="text" name="rfuid" id="rfuid" class="form-control form-control-sm"
								value="${memberItem.rfUid}"/>
						</div>
					</div>
					
					<div class="form-group mb-2">
						<label for='grade' class="col-12">회원등급</label>
						<div class="col-8">
							<select name="grade" class="form-control form-control-sm">
								<c:forEach var="l" items="${gradeList}">
									<c:set var="choice_grade" value="" />
									<c:if test="${l.gradeId == memberItem.gradeId}">
										<c:set var="choice_grade" value="selected" />
									</c:if>
									<option value="${l.gradeId}" ${choice_grade}>${l.gradeName}(대여가능권수:${l.brwLimit}권
										/ 대여기간:${l.dateLimit}일)</option>
								</c:forEach>
							</select>
						</div>
					</div>
					
					<div class="form-group mt-2">
						<div class="col-md-offset-2 col-md-6">
							<button type="submit" class="btn btn-primary btn-sm">수정하기</button>
							<button type="reset" class="btn btn-danger btn-sm">다시작성</button>
						</div>
					</div>
				</form>
				
				<!-- 조회결과를 출력하기 위한 표 -->
				<table class="table table-sm table-bordered mt-2">
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
							<th class="table-info text-center">생년월일</th>
							<td>${memberItem.birthdate}</td>
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
												src="http://nuts.i234.me:7070/files/upload/${profileName}" alt="profileImg"/>
										</c:when>
										<c:otherwise>
											프로필 사진이 없습니다.
										</c:otherwise>
									</c:choose>
								</td>
						</tr>
						<tr>
							<th class="table-info text-center">RF-UID</th>
							<td>C${memberItem.rfUid}</td>
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
								<input type="submit" class="btn btn-warning closeRefresh" value="삭제" formaction="${pageContext.request.contextPath}/member/member_inactive_ok.do"/>
							</div>
							</form>
						</div>
					</div>
				</div>
<%@ include file="/WEB-INF/inc/script-common.jsp"%>
<script type="text/javascript">
		$(function() {
			$(".closeRefresh").click(function(){
				opener.location.href=opener.document.URL;
				window.close();
			});
		});
	</script>
</body>
</html>