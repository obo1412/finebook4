function deleteIdBcs() {
	var receiveIdBcsVal = document.getElementById('receiveIdBcs').value;
	$.ajax({
		url: "/book_check/delete_book_check_status.do",
		type:'POST',
		data: {
			receiveIdBcsVal
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				location.href = document.URL;
			}
		}
	});
}

function deliverIdBcs() {
	var receiveIdBcs = document.getElementById('receiveIdBcs');
	// 값 초기화
	receiveIdBcs.value = 0;
	var idBcs = 0;
	var dateValue = '';
	if(event.target.classList.contains('btnDeliverIdBcs')) {
		idBcs = event.target.getAttribute('value');
		dateValue = event.target.getAttribute('dateValue');
	} else if(event.target.classList.contains('iconDeliverIdBcs')) {
		idBcs = event.target.parentNode.getAttribute('value');
		dateValue = event.target.parentNode.getAttribute('dateValue');
	}
	receiveIdBcs.value = idBcs;
	document.getElementById('dateKey').innerText = dateValue;
}
		
function enterTheChecking() {
	console.log(event.target);     
	console.log(document.getElementsByName('id_bcs')[0].value);
}

function clickedNewChecking() {
	$.ajax({
		url: "/book_check/new_book_check_ok.do",
		type:'POST',
		data: {
			
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				location.href = document.URL;
			}
		}
	});
}



//엑셀파일만들기
function makeBookCheckExcelFile() {
	let eTarget = event.target;
	//closest는 '부모요소'인 선택자를 받아옴
	let tdTarget = eTarget.closest('.td__id__bcs');
	let idBcs = tdTarget.querySelector('.cls__id__bcs').value;
	
	
	$.ajax({
		url: "/book_check/book_check_list_to_excel.do",
		type:'POST',
		data: {
			idBcs
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				let downUrl = "/download.do?file="+data.filePath;
				location.href = downUrl;
			}
		}
	});
}



//점검페이지 진입버튼
function enterBookCheckList() {
	event.preventDefault();
	
	let eTarget = event.target;
	let trClass = eTarget.closest('.tr__class');
	let checker = trClass.querySelector('.class__checker').value;
	let id_bcs = trClass.querySelector('.cls__id__bcs').value; 
	
	let url = "/book_check/book_check_list.do?id_bcs="+id_bcs
				+"&checker="+checker;
	
	location.href= url;
}




















