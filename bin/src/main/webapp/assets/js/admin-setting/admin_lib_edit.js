const idLib = document.querySelector('#idLib');
let addUserIdMng = document.querySelector('#addUserIdMng');
let addUserPwMng = document.querySelector('#addUserPwMng');
let addNameMng = document.querySelector('#addNameMng');
let addEmailMng = document.querySelector('#addEmailMng');

let inputCurIdMng = document.getElementById('inputCurIdMng');

//관리자 추가처리
function clickedAddManager() {
	$.ajax({
		url: "/admin_setting/add_new_manager.do",
		type:'POST',
		async: false, //ajax결과값이 나오면 다음 함수가 진행된다.
		data: {
			idLib: idLib.value,
			addUserIdMng: addUserIdMng.value,
			addUserPwMng: addUserPwMng.value,
			addNameMng: addNameMng.value,
			addEmailMng: addEmailMng.value
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
				addUserIdMng.focus();
			} else {
				location.reload();
			}
		}
	});
}

//관리자 삭제 버튼 동작함수
function deleteManagerTargeting() {
	//현재 클릭한 타겟에서 원하는 값을 가져오기 위한
	let curTarget = event.target.previousElementSibling;
	
	while(curTarget) {
		//현타겟이 관리자 id 클래스를 포함하면 값을 가져오기.
		if(curTarget.classList.contains('cls-id-mng')) {
			inputCurIdMng.value = curTarget.value;
		}
		//유저아이디
		if(curTarget.classList.contains('cls-user-id-mng')) {
			document.getElementById('spanDeleteManagerUserId').textContent = curTarget.value;
		}
		//관리자명
		if(curTarget.classList.contains('cls-name-mng')) {
			document.getElementById('spanDeleteManagerName').textContent = curTarget.value;
		}
		curTarget = curTarget.previousElementSibling;
	}
	
}

//관리자 삭제 모달에서 실제 삭제 버튼 비동기처리
function clickedDeleteManager() {
	//현재 클릭한 타겟에서 원하는 값을 가져오기 위한
	
	$.ajax({
		url: "/admin_setting/admin_lib_delete_manager.do",
		type:'POST',
		async: false, //ajax결과값이 나오면 다음 함수가 진행된다.
		data: {
			idLib: idLib.value,
			//삭제할 관리자 id
			curIdMng: inputCurIdMng.value
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				location.reload();
			}
		}
	});
}

// 관리자 user id  중복체크 버튼 실행함수
function dupCheckMangerUserId() {
	if(addUserIdMng.value == null || addUserIdMng.value == '') {
		alert('사용하실 관리자 ID를 적어주세요.');
		return;
	}
	
	$.ajax({
		url: "/admin_setting/admin_lib_dup_check_userid_manager.do",
		type:'POST',
		async: false, //ajax결과값이 나오면 다음 함수가 진행된다.
		data: {
			addUserIdMng: addUserIdMng.value
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
				addUserIdMng.focus();
			} else {
				alert(data.msg);
				addUserPwMng.focus();
			}
		}
	});
}












