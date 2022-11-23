

//비동기 통신으로 엑셀 만들고, success 에서 파일 다운로드.
//저렇게 복잡하게 할 필요 없었는데. 돌아가고있던것.
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
			console.log('도서목록 엑셀 변환: '+data.rt);
			console.log('파일 경로: '+data.filePath);
			
			var uploadFolderPath = data.filePath.substring(data.filePath.lastIndexOf("upload"));
			console.log('업로드폴더까지 경로:'+uploadFolderPath);
			var downloadPath = "/files/"+uploadFolderPath;
			var fileName = data.filePath.substring(data.filePath.lastIndexOf("/")+1).split("?")[0];
			var xhr = new XMLHttpRequest();
				xhr.responseType = 'blob';
				xhr.onload = function() {
					var link = document.createElement('a');
					link.href = window.URL.createObjectURL(xhr.response); //xhr.response is a blob
					link.download = fileName;
					link.style.display = 'none';
					document.body.appendChild(link);
					link.click();
					delete link;
				};
				xhr.open('POST', downloadPath);
				xhr.setRequestHeader('Content-type', 'application/json');
				xhr.send();
		}
		,error:function(request,status,error){
			alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
};