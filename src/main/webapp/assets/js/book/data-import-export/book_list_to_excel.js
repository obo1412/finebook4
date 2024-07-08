


function clickedBookHeldListToExcel() {
	var targetYear = document.getElementById('yearOptionBookHeldList').value;
	let bookShelf = document.querySelector('#selectBookShelf').value;
	
	
	$.ajax({
		url: "/book/book_held_list_to_excel.do",
		type: 'POST',
		timeout:60*2*1000,
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

//폐기 도서목록 엑셀로 다운로드
function clickedDiscardBookHeldListToExcel() {
	$.ajax({
		url: "/book/discard_book_held_list_to_excel.do",
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



//대출 도서 목록 엑셀로 다운로드
function clickedBrwedBookHeldListToExcel() {
	$.ajax({
		url: "/book/brwed_book_held_list_to_excel.do",
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