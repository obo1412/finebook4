<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>

<div class="modal fade" id="enter_user_self_page_modal" tabindex="-1" role="dialog"
	aria-labelledby="enterUserSelfPageModal" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
		
			<div class="modal-header">
				<h5 class="modal-title" id="enterUserSelfPageModal">
					이용자 화면으로 전환하시겠습니까?
				</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			
			<div class="modal-body">
				<div>도서 조회, 대출/반납만 가능한 이용자 화면으로 전환합니다.</div>
				<div>관리자 모드로 돌아오기 위해서는 다시 로그인하셔야 합니다.</div>
			</div>
			
			<div class="modal-footer">
				<a class="btn btn-info"
					href="${pageContext.request.contextPath}/user_self_page/main_self.do">
					화면전환
				</a>
				
				<button class="btn btn-secondary" type="button"
					data-dismiss="modal">취소</button>
			</div>
			
		</div>
	</div>
</div>