window.addEventListener('load', function() {
	var curUrl = window.location.search;
	console.log(curUrl);
	let params = curUrl.split("&");
	for(var i=0; i<params.length; i++) {
		if(params[i].indexOf('deleteMemberOk') > -1) {
			console.log('삭제성공');
			opener.location.reload();
			window.close();
		}
	}
})

