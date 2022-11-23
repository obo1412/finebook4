//서가 관련 컨트롤
let searchShelf = document.getElementById('searchShelf');
let selectBookShelf = document.getElementById('selectBookShelf');

//별치기호 관련 컨트롤
let searchAddiCode = document.getElementById('searchAddiCode');
let selectAddiCode = document.getElementById('selectAddiCode');

window.addEventListener('load', function() {
	for(var i=0; i<selectBookShelf.options.length; i++){
		if(selectBookShelf.options[i].value == searchShelf.value){
			selectBookShelf.options[i].selected = true;
		}
	}
	
	if(searchAddiCode != null) {
		for(var i=0; i<selectAddiCode.options.length; i++){
		if(selectAddiCode.options[i].value == searchAddiCode.value){
			selectAddiCode.options[i].selected = true;
		}
	}
	}
})

selectBookShelf.addEventListener('change', (e)=> {
	//document에서 아래와 같이 .name으로만 써도 객체를 담을 수 있다.
	let searchForm = document.searchFormBookHeld;
	searchForm.submit();
});

selectAddiCode.addEventListener('change', (e)=> {
	//document에서 아래와 같이 .name으로만 써도 객체를 담을 수 있다.
	let searchForm = document.searchFormBookHeld;
	searchForm.submit();
});