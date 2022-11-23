<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>

<style>
	label {
		margin-top : 10px;
	}
</style>

<div class="modal fade" id="clicked_add_new_lib_modal" tabindex="-1" role="dialog"
	aria-labelledby="addNewLibModal" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered" role="document">
		<div class="modal-content">
		
			<div class="modal-header">
				<h5 class="modal-title" id="addNewLibModal">
					새로운 도서관 생성
				</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			
			<form class="form-horizontal" id="formAddNewLib" method="post">
				
				<div class="modal-body">
					<label>도서관명</label>
					<input class="form-control" type="text" name="nameLib" />
					<label>위치</label>
					<input class="form-control" type="text" name="locLib" />
					<label>URL조회용 KEY(영문만 가능)</label>
					<input class="form-control" type="text" name="stringKeyLib" />
					<label>계정용도</label>
					<input class="form-control" type="text" name="purpose" />
					<label>만료일</label>
					<input class="form-control" type="date" name="expDate" />
				</div>
				
				<div class="modal-footer">
					<button class="btn btn-info" onclick="submitFormAddNewLib()">
						생성하기
					</button>
					<button class="btn btn-secondary" type="button"
						data-dismiss="modal">취소</button>
				</div>
				
			</form>
			
		</div>
	</div>
</div>


<script type="text/javascript">
	let formAddNewLib = document.querySelector('#formAddNewLib');
	formAddNewLib.action = '/admin_setting/add_new_lib_ok.do';
	
	function submitFormAddNewLib() {
		formAddNewLib.submit();
	}
</script>