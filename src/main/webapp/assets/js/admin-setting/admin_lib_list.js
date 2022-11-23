

window.addEventListener('load', function() {
	//console.log('날짜비교 함수 실행');
	
	const spanStatementDate = document.querySelectorAll('.spanStatementDate');
	const todayDate = new Date();
	
	//console.log(todayDate);
	
	for(let i=0; i<spanStatementDate.length; i++){
		//console.log('정산일 돌기시작');
		//console.log('['+spanStatementDate[i].innerText+']');
		if(spanStatementDate[i].innerText!=null&&spanStatementDate[i].innerText!='') {
			let eachDate = new Date(spanStatementDate[i].innerHTML);
			if(todayDate.getMonth() > eachDate.getMonth()) {
				//달을 비교해서 이번달 숫자가 더크면.
				spanStatementDate[i].style.color = 'red';
			}
		}
	}
})



function clickedUpdateExpDate() {
//	console.log(event.target.parentNode.parentNode);
//	console.log(event.target.parentNode.previousElementSibling.firstElementChild);
	let expDate = null;
	let idLib = null;
	//현재 타겟의 부모노드
	let targetChildren = event.target.parentNode.parentNode.children;
	for(var i=0; i<targetChildren.length; i++) {
		//클릭한 노드와 엘리먼트 전체를 가져와서 포문으로 클래스명 찾기.
		if(targetChildren[i].classList.contains('spanExpDate')){
			//spanExpDate 클래스 그 아래로 for문 또 돌리기
			//원하는 클래스 알아서 찾게 하고 싶어서 이 구성 해봄.
			let spanChildren = targetChildren[i].children;
			for(var j=0; j<spanChildren.length; j++){
				if(spanChildren[j].classList.contains('clsExpDate')){
					//console.log(spanChildren[j].value);
					expDate = spanChildren[j].value;
				}
				if(spanChildren[j].classList.contains('clsIdLib')){
					//console.log(spanChildren[j].value);
					idLib = spanChildren[j].value;
				}
			}
		}
	}
	
	//업데이트 함수 실행 파라미터 넣어서
	updateExpDateAsync(idLib, expDate);
}



//만료일 업데이트 비동기 처리 함수
function updateExpDateAsync(idLib, expDate) {
	$.ajax({
		url: "/admin_setting/update_exp_date.do",
		type:'POST',
		async: false, //ajax결과값이 나오면 다음 함수가 진행된다.
		data: {
			idLib,
			expDate
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				alert('변경됨');
			}
		}
	});
}


function updateStatementDateLastestAsync() {
	let eventTarget = event.target;
	let idLib = event.target.parentNode.querySelector('.clsIdLib').innerHTML;
	
	$.ajax({
		url: "/admin_setting/update_statement_date_lastest.do",
		type:'POST',
		async: false, //ajax결과값이 나오면 다음 함수가 진행된다.
		data: {
			idLib
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				let nowDate = data.nowDate;
				let spanDate = eventTarget.parentNode.parentNode.querySelector('.spanStatementDate');
				console.log(spanDate);
				spanDate.innerHTML = nowDate;
			}
		}
	});
}



















