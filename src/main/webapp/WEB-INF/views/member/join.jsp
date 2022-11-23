<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
	<%@ include file="/WEB-INF/inc/head.jsp"%>
	<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/daumPostCode.js"></script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/member/join_member.css" />
</head>
<body>
	<%@ include file="/WEB-INF/inc/topbar.jsp"%>

	<div id="wrapper">
		<%@ include file="/WEB-INF/inc/sidebar_left.jsp"%>
		<div id="content-wrapper">
			<div class="container-fluid">
				<div class="card mb-3 join__whole__card">
					<div class="card-header">
						<h4>회원 추가</h4>
					</div>

					<div class="card-body">
						<!-- 가입폼 시작 -->
						<form class="form-horizontal" name="myform" method="post"
							enctype="multipart/form-data"
							action="${pageContext.request.contextPath}/member/join_ok.do">

							<div class="form-group row">
								<label for='name' class="col-2 col-xs-12 m_label">이름*</label>
								<div class="col-6 col-xs-12">
									<input type="text" name="name" id="name" class="form-control form-control-sm m__input"
									value="${name}" />
								</div>
							</div>

							<div class="form-group row">
								<label for='phone' class="col-2 col-xs-12 m_label">연락처</label>
								<div>
									<div class="col-xs-12 input-group m__group__input">
										<input type="tel" name="phone" id="phone" class="form-control form-control-sm"
											placeholder="'-'없이 입력" value="${phone}" />
										<div class="input-group-append">
											<input type="button" value="중복체크" class="btn btn-danger btn-sm m__btn__size" style='color: white;' onclick="dupCheckPhoneNumClicked()" />
											<!-- onclick="javascript: form.action='${pageContext.request.contextPath}/member/phone_dup_check.do';" -->
										</div>
									</div>
									<div class="dup__check__msg">연락처 중복검사를 해주세요.</div>
								</div>
								<%-- <input type="hidden" name="checkNum" value="${checkNum}" readonly/> --%>
							</div>
							
							<div class="form-group row">
								<label for='phone' class="col-2 col-xs-12 m_label">추가 연락처</label>
								<div class="col-6 col-xs-12">
									<input type="text" name="otherContact" id="otherContact" class="form-control form-control-sm m__input"
									placeholder="선택사항 입니다." />
								</div>
							</div>

							<div class="form-group row">
								<label for='birthdate' class="col-2 col-xs-12 m_label">생년월일*</label>
								<div class="col-6 col-xs-12 form-inline">
									<input type="date" name="birthdate" id="birthdate" max="9999-12-31"
										class="form-control form-control-sm m__date__input" placeholder="yyyy-mm-dd" value="${birthdate}"/>
									<p class="date__remark">(YYYY-mm-dd)</p>
								</div>
							</div>

							<div class="form-group row">
								<label for='email' class="col-2 col-xs-12 m_label">이메일</label>
								<div class="col-6 col-xs-12">
									<input type="email" name="email" id="email"
										class="form-control form-control-sm m__input" placeholder="" value="${email}"/>
								</div>
							</div>
							
							<div class="form-group row">
								<label for='idMbrClass' class="col-2 col-xs-12 m_label">회원분류</label>
								<div class="col-6 col-xs-12">
									<select name="idMbrClass" class="form-control form-control-sm m__input">
										<option value="">---분류 없음---</option>
										<c:forEach var="classMemberList" items="${classList}">
											<option value="${classMemberList.idMbrClass}">
												${classMemberList.className}
											</option>
										</c:forEach>
									</select>
								</div>
							</div>

							<div class="form-group row">
								<label for='grade' class="col-2 col-xs-12 m_label">등급*</label>
								<div class="col-6 col-xs-12">
									<select name="grade" class="form-control form-control-sm m__input">
										<option value="">--고객등급을 선택하세요--</option>
										<c:forEach var="l" items="${gradeList}">
											<c:set var="choice_grade" value="" />
											<c:if test="${l.standard == 1}">
												<c:set var="choice_grade" value="selected" />
											</c:if>
											<option value="${l.gradeId}" ${choice_grade}>${l.gradeName}(대여가능권수:${l.brwLimit}권
												/ 대여기간:${l.dateLimit}일)</option>
										</c:forEach>
									</select>
								</div>
							</div>

							<div class="form-group row">
								<label for='postcode' class="col-2 col-xs-12 m_label">우편번호</label>
								<div class="col-xs-12 input-group m__group__input">
									<input type="text" name="postcode" id="postcode"
										class="form-control form-control-sm" style='' value="${postcode}"/>
									<!-- 클릭 시, Javascript 함수 호출 : openDaumPostcode() -->
									<div class="input-group-append">
										<input type='button' value='주소 찾기' class='btn btn-warning btn-sm'
											style="color: white;"
											onclick='execDaumPostcode("postcode", "addr1", "addr2")' />
									</div>
								</div>
							</div>

							<div class="form-group row">
								<label for='addr1' class="col-2 col-xs-12 m_label">주소</label>
								<div class="col-6 col-xs-12">
									<input type="text" name="addr1" id="addr1" class="form-control form-control-sm m__input"
									value="${addr1}" />
								</div>
							</div>

							<div class="form-group row">
								<label for='addr2' class="col-2 col-xs-12 m_label">상세주소</label>
								<div class="col-6 col-xs-12">
									<input type="text" name="addr2" id="addr2" class="form-control form-control-sm m__input"
									value="${addr2}" />
								</div>
							</div>

							<div class="form-group row">
								<label for='profile_img' class="col-2 col-xs-12 m_label">프로필 사진</label>
								<div class="col-6 col-xs-12">
									<input type="file" name="profile_img_path"
										id="profile_img_path" class="form-control-file form-control-sm m__input" />
								</div>
							</div>

							<div class="form-group row">
								<label for='rfuid' class="col-2 col-xs-12 m_label">RF-UID</label>
								<div class="col-6 col-xs-12">
									<input type="text" name="rfuid" id="rfuid"
										class="form-control form-control-sm m__input" value="${rfuid}"/>
								</div>
							</div>

							<div class="form-group row">
								<label for='remarks' class="col-2 col-xs-12 m_label">비고</label>
								<div class="col-6 col-xs-12">
									<input type="text" name="remarks" id="remarks"
										class="form-control form-control-sm m__input" value="${remarks}" />
								</div>
							</div>

							<div class="form-group row">
								<div class="col-md-offset-2 col-6">
									<button type="submit" class="btn btn-primary">추가하기</button>
									<button type="reset" class="btn btn-danger">다시작성</button>
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
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/member-js/join_member.js"></script>
</body>
</html>