//서가 관련 컨트롤
let searchMemberClass = document.getElementById('searchMemberClass');
let selectMemberClass = document.getElementById('selectMemberClass');

window.addEventListener('load', function() {
	for(var i=0; i<selectMemberClass.options.length; i++){
		if(selectMemberClass.options[i].value == searchMemberClass.value){
			selectMemberClass.options[i].selected = true;
		}
	}
	
})

selectMemberClass.addEventListener('change', (e)=> {
	//document에서 아래와 같이 .name으로만 써도 객체를 담을 수 있다.
	let searchForm = document.searchFormBookHeldBrwed;
	searchForm.submit();
});
