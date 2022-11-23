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
<title>도서관 정보 수정하기</title>

<%@ include file="/WEB-INF/inc/head.jsp"%>

<style type="text/css">
	label {
		font-size: 14px;
	}
	
	p {
		margin: 5px 0px;
	}
</style>
</head>

<body>
	<div id="wrapper">
		<div id="content-wrapper">
			<div class="container-fluid">
				<h4 class='page-header'>도서관 정보 수정</h4>

				<form class="horizontal" name="edit_admin_lib" method="post" action="${pageContext.request.contextPath}/admin_setting/admin_lib_edit_ok.do">
					
					<div class="form-group mt-2">
						<div class="col-md-offset-2 col-md-6">
							<button type="submit" class="btn btn-primary btn-sm">저장하기</button>
							
							<button type="reset" class="btn btn-secondary btn-sm">다시작성</button>
							<div class="float-right">
								<a href="#" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#delete_lib_modal">삭제</a>
								<input type="button" class="btn btn-warning btn-sm closeRefresh" value="닫기" onclick="self.close()" />
							</div> 
						</div>
					</div>
					
					<input type="hidden" name="idLib" id="idLib" value="${item.idLib}"/>
					
					<div class="form-inline mb-2">
						<label for="nameLib" class="col-3">도서관명</label>
						<div class="col-9">
							<input name="nameLib" id="nameLib" class="form-control form-control-sm" 
								value="${item.nameLib}" />
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="locLib" class="col-3">도서관위치</label>
						<div class="col-9">
							<input name="locLib" id="locLib" class="form-control form-control-sm" 
								value="${item.locLib}" />
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="skl" class="col-3">조회용URL KEY</label>
						<div class="col-9">
							<input name="stringKeyLib" id="stringKeyLib" class="form-control form-control-sm" 
								value="${item.stringKeyLib}" />
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="purpose" class="col-3">계정용도</label>
						<div class="col-9">
							<input name="purpose" id="purpose" class="form-control form-control-sm" 
								value="${item.purpose}" />
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="statementDate" class="col-3">정산일</label>
						<div class="col-9">
							<fmt:parseDate var="parseStatementDate" value="${item.statementDate}" pattern="yyyy-MM-dd"/>
							<fmt:formatDate var="statementDate" value="${parseStatementDate}" pattern="yyyy-MM-dd" />
							<input name="statementDate" id="statementDate" class="form-control form-control-sm" 
								type="text" value="${statementDate}" />
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="regDate" class="col-3">등록일</label>
						<div class="col-9">
							<fmt:parseDate var="parseRegDate" value="${item.regDateLib}" pattern="yyyy-MM-dd"/>
							<fmt:formatDate var="regDate" value="${parseRegDate}" pattern="yyyy-MM-dd" />
							<input name="regDate" id="regDate" class="form-control form-control-sm" 
								type="date" value="${regDate}" />
						</div>
					</div>
					
					<div class="form-inline mb-2">
						<label for="expDate" class="col-3">만료일</label>
						<div class="col-9">
							<fmt:parseDate var="parseExpDate" value="${item.expDate}" pattern="yyyy-MM-dd"/>
							<fmt:formatDate var="expDate" value="${parseExpDate}" pattern="yyyy-MM-dd" />
							<input name="expDate" id="expDate" class="form-control form-control-sm" 
								type="date" value="${expDate}" />
						</div>
					</div>
					
				</form>
				
				<table class="table table-sm table-bordered">
					<thead>
						<tr>
							<th class="table-warning text-center">
								<button class="btn btn-sm btn-warning" onclick="dupCheckMangerUserId()">
									<span style="font-size:9pt; font-weight: 800;">
										ID(중복체크)
									</span>
								</button>
							</th>
							<th class="table-warning text-center">PASSWORD</th>
							<th class="table-warning text-center">관리자명</th>
							<th class="table-warning text-center">관리자이메일</th>
							<th class="table-warning text-center">추가버튼</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="text-center">
								<input type="text" id="addUserIdMng" class="form-control form-control-sm" />
							</td>
							<td class="text-center">
								<input type="text" id="addUserPwMng" class="form-control form-control-sm" />
							</td>
							<td class="text-center">
								<input type="text" id="addNameMng" class="form-control form-control-sm" />
							</td>
							<td class="text-center">
								<input type="text" id="addEmailMng" class="form-control form-control-sm" />
							</td>
							<td class="text-center">
								<button class="btn btn-primary btn-sm" style="width:70px;" onclick="clickedAddManager()">+</button>
							</td>
						</tr>
					</tbody>
				</table>
				
				<table class="table table-sm table-bordered">
					<thead>
						<tr>
							<th class="table-info text-center" style="width:50px;">번호</th>
							<th class="table-info text-center">관리자 ID</th>
							<th class="table-info text-center">관리자명</th>
							<th class="table-info text-center">관리자이메일</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(managerList) > 0}">
								<c:forEach var="managerItem" items="${managerList}" varStatus="status">
									<tr>
										<td class="cls-id-mng" style="display:none;">${managerItem.idMng}</td>
										<td class="text-center">${status.count}</td>
										<td class="text-center">
											<div class="dropdown">
												<a href="#" class="dropdown-toggle" data-toggle="dropdown">
													${managerItem.userIdMng}
												</a>
												<div class="dropdown-menu">
													<input type="hidden" class="cls-id-mng" value="${managerItem.idMng}" />
													<input type="hidden" class="cls-user-id-mng" value="${managerItem.userIdMng}" />
													<input type="hidden" class="cls-name-mng" value="${managerItem.nameMng}" />
													<a class="dropdown-item" href="#">관리자아이디 수정</a>
													<a class="dropdown-item text-danger" href="#"
														data-toggle="modal" data-target="#delete_manager_modal"
														onclick="deleteManagerTargeting()">관리자 삭제</a>
												</div>
											</div>
										</td>
										<td class="text-center">${managerItem.nameMng}</td>
										<td class="text-center">${managerItem.emailMng}</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="4" class="text-center">
										조회된 관리자 정보가 없습니다.
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
				
			</div>
			<!-- container-fluid 끝 -->
		</div>
		<!-- content-wrapper 끝 -->
	</div>
	<!-- wrapper 끝 -->
	
	
	<!-- 매니저(관리자) 삭제 모달 -->
	<div class="modal fade" id="delete_manager_modal" tabindex="-1" role="dialog"
		aria-labelledby="deleteManagerModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="deleteManagerModalLabel">관리자 삭제</h5>
					<button class="close" type="button" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
					</button>
				</div>
				
					<input type="hidden" name="idLib" value="${item.idLib}"/>
					<input type="hidden" id="inputCurIdMng" />
					<div class="modal-body">
						<div>아이디: <span id="spanDeleteManagerUserId"></span></div>
						<div>관리자명: <span id="spanDeleteManagerName"></span></div>
						<div>해당 관리자를 정말로 삭제하시겠습니까?</div>
					</div>
					<div class="modal-footer">
						<button class="btn btn-secondary" type="button"
							data-dismiss="modal">취소</button>
						<input type="submit" class="btn btn-danger" value="삭제" onclick="clickedDeleteManager()"/>
					</div>
					
			</div>
		</div>
	</div>
	
	
	<!-- 도서관 삭제 모달 -->
	<div class="modal fade" id="delete_lib_modal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">도서관을 삭제하시겠습니까?</h5>
					<button class="close" type="button" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
					</button>
				</div>
				<form name="form_admin_lib_delete" method="post">
					<input type="hidden" name="idLib" value="${item.idLib}"/>
					<div class="modal-body">
						<div>도서관이 삭제가 됩니다.</div>
						<div>이미 이용중인(도서등록 등) 도서관은 삭제되지 않습니다.</div>
					</div>
					<div class="modal-footer">
						<button class="btn btn-secondary" type="button"
							data-dismiss="modal">취소</button>
						<input type="submit" class="btn btn-danger" value="삭제" formaction="${pageContext.request.contextPath}/admin_setting/admin_lib_delete_ok.do"/>
					</div>
				</form>
			</div>
		</div>
	</div>
	
	
<%@ include file="/WEB-INF/inc/script-common.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/admin-setting/admin_lib_edit.js"></script>
<script type="text/javascript">
		$(function() {
			$(".closeRefresh").click(function(){
				opener.location.href=opener.document.URL;
				window.close();
			});
			
			//아래 새로고침 기능을 사용하면, 자식창(도서변경창)이 떠있는 상태에서 부모창(도서목록)이
			//새로고침 되면서, 부모창에 잘못된 변수가 전달되는 것 같다.
			//도서 목록의 맨아래 LIMIT -10, 10 으로 쿼리 처리됨. 음수 -10은 도대체 어디서 오는 것이냐..
			//pagehelper에서 계산하면서 0-10 이런식으로 처리되서 음수가 나오는 것 같다.
			//아래 리프레쉬 기능을 사용하지 않음으로서 도서 변경시 뜨는 500 에러 메시지는
			//아직까지는 나오지 않는다.
			$(".Refresh").click(function(){
				opener.location.href=opener.document.URL;
			});
		});
	</script>
</body>
</html>