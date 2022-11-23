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
						<h4>회원 추가</h4>
					</div>

					<div class="card-body">
						<!-- 가입폼 시작 -->
						<form class="form-horizontal" name="myform" method="post"
							enctype="multipart/form-data"
							action="${pageContext.request.contextPath}/member/join_ok.do">

							<div class="form-group row">
								<label for='name' class="col-2 col-xs-12">이름*</label>
								<div class="col-6 col-xs-12">
									<input type="text" name="name" id="name" class="form-control form-control-sm"
									value="${name}" />
								</div>
							</div>

							<div class="form-group row">
								<label for='phone' class="col-2 col-xs-12">연락처</label>
								<div class="col-6 col-xs-12 input-group">
									<input type="tel" name="phone" id="phone" class="form-control form-control-sm"
										placeholder="'-'없이 입력" value="${phone}" />
									<div class="input-group-append">
										<input value="중복체크" type="submit" class="btn btn-danger btn-sm" style='color: white;'
											onclick="javascript: form.action='${pageContext.request.contextPath}/member/phone_dup_check.do';" />
									</div>
								</div>
								<p class="col-6 col-xs-12">${checkMsg}</p>
								<input type="hidden" name="checkNum" value="${checkNum}" readonly/>
							</div>

							<div class="form-group row">
								<label for='birthdate' class="col-2 col-xs-12">생년월일*</label>
								<div class="col-6 col-xs-12 form-inline">
									<input type="date" name="birthdate" id="birthdate" max="9999-12-31"
										class="form-control form-control-sm" placeholder="yyyy-mm-dd" value="${birthdate}"/>
									<p class="" style="text-align:center;">YYYY-mm-dd</p>
								</div>
							</div>

							<div class="form-group row">
								<label for='email' class="col-2 col-xs-12">이메일</label>
								<div class="col-6 col-xs-12">
									<input type="email" name="email" id="email"
										class="form-control form-control-sm" placeholder="" value="${email}"/>
								</div>
							</div>
							
							<div class="form-group row">
								<label for='idMbrClass' class="col-2 col-xs-12">회원분류</label>
								<div class="col-6 col-xs-12">
									<select name="idMbrClass" class="form-control form-control-sm">
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
								<label for='grade' class="col-2 col-xs-12">등급*</label>
								<div class="col-6 col-xs-12">
									<select name="grade" class="form-control form-control-sm">
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
								<label for='postcode' class="col-2 col-xs-12">우편번호</label>
								<div class="col-6 col-xs-12 input-group clearfix">
									<input type="text" name="postcode" id="postcode"
										class="form-control form-control-sm pull-left" style='' value="${postcode}"/>
									<!-- 클릭 시, Javascript 함수 호출 : openDaumPostcode() -->
									<div class="input-group-append">
										<input type='button' value='주소 찾기' class='btn btn-warning btn-sm'
											style="color: white;"
											onclick='execDaumPostcode("postcode", "addr1", "addr2")' />
									</div>
								</div>
							</div>

							<div class="form-group row">
								<label for='addr1' class="col-2 col-xs-12">주소</label>
								<div class="col-6 col-xs-12">
									<input type="text" name="addr1" id="addr1" class="form-control form-control-sm"
									value="${addr1}" />
								</div>
							</div>

							<div class="form-group row">
								<label for='addr2' class="col-2 col-xs-12">상세주소</label>
								<div class="col-6 col-xs-12">
									<input type="text" name="addr2" id="addr2" class="form-control form-control-sm"
									value="${addr2}" />
								</div>
							</div>

							<div class="form-group row">
								<label for='profile_img' class="col-2 col-xs-12">프로필 사진</label>
								<div class="col-6 col-xs-12">
									<input type="file" name="profile_img_path"
										id="profile_img_path" class="form-control-file form-control-sm" />
								</div>
							</div>

							<div class="form-group row">
								<label for='rfuid' class="col-2 col-xs-12">RF-UID</label>
								<div class="col-6 col-xs-12">
									<input type="text" name="rfuid" id="rfuid"
										class="form-control form-control-sm" value="${rfuid}"/>
								</div>
							</div>

							<div class="form-group row">
								<label for='remarks' class="col-2 col-xs-12">비고</label>
								<div class="col-6 col-xs-12">
									<input type="text" name="remarks" id="remarks"
										class="form-control form-control-sm" value="${remarks}" />
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
</body>
</html>