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