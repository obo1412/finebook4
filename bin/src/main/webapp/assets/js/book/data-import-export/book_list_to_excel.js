


function clickedBookHeldListToExcel() {
	var targetYear = document.getElementById('yearOptionBookHeldList').value;
	let bookShelf = document.querySelector('#selectBookShelf').value;
	
	
	$.ajax({
		url: "/book/book_held_list_to_excel.do",
		type: 'POST',
		data: {
			targetYear,
			bookShelf
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