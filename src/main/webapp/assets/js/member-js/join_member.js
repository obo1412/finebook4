window.addEventListener('load', function() {
	
})

let dupCheckMsg = document.querySelector('.dup__check__msg');


function dupCheckPhoneNumClicked() {
	let phone = document.querySelector('#phone').value;
	
	if(phone=='') {
		alert('번호를 입력해주세요.');
		return;
	}
	
	$.ajax({
		url: "/member/phone_dup_check.do",
		type:'GET',
		data: {
			phone
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				let checkMsg = data.checkMsg;

				if(data.checkResult >0) {
					dupCheckMsg.style.color = 'red';
				} else {
					dupCheckMsg.style.color = 'blue';
				}
				
				dupCheckMsg.innerText = checkMsg;
			}
		}
	});
}