let searchClassId = document.getElementById('classIdFromCntl');
let selectMenuClass = document.getElementById('idSearchClassId');

window.addEventListener('load', function() {
	for(var i=0; i<selectMenuClass.options.length; i++){
		if(selectMenuClass.options[i].value == searchClassId.value){
			selectMenuClass.options[i].selected = true;
		}
	}
})

selectMenuClass.addEventListener('change', (e)=> {
	//document에서 아래와 같이 .name으로만 써도 객체를 담을 수 있다.
	let searchForm = document.searchFormMember;
	searchForm.submit();
});



//회원 목록 엑셀 변환 함수
function memberListToExcel() {
		$.ajax({
		url: "/member/member_list_to_excel.do",
		type: 'POST',
		data: {
			
		},
		/* dataType: "json", */
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				let downUrl = "/download.do?file="+data.filePath;
				location.href = downUrl;
			}
		}
	});
};