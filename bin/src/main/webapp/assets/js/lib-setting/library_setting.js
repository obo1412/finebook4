


window.addEventListener('load', function() {
	
})

//등록번호 자리수 수정 함수
function clickedChangeNod() {
	let nodBarcode = document.getElementById('nodBarcode').value;
	
	if(nodBarcode < 6 || nodBarcode > 12) {
		alert('최소 6자리 이상 또는 12자리 이하여야 합니다.');
		return;
	}
	
	
	$.ajax({
		url: "/setting/change_nod_barcode_ok.do",
		type:'POST',
		async: false,
		data: {
			nodBarcode
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				alert('등록번호 자리수가 수정되었습니다.');
			}
		}
	}); //ajax 끝
}




