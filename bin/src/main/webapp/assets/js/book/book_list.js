let searchShelf = document.getElementById('searchShelf');
let selectBookShelf = document.getElementById('selectBookShelf');





window.addEventListener('load', function() {
	for(var i=0; i<selectBookShelf.options.length; i++){
		if(selectBookShelf.options[i].value == searchShelf.value){
			selectBookShelf.options[i].selected = true;
		}
	}
})

selectBookShelf.addEventListener('change', (e)=> {
	//document에서 아래와 같이 .name으로만 써도 객체를 담을 수 있다.
	let searchForm = document.searchFormBookHeld;
	searchForm.submit();
});