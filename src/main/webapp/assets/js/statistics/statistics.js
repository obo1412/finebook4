	const dataRow = document.getElementById('dataRow');
	const dataCol = document.getElementById('dataCol');
	const tcaption = document.getElementById('tcaption');
	const thead = document.getElementById('thead');
	const tbody = document.getElementById('tbody');
	
	function clickedDataListPrint() {
		var targetYear = document.getElementById('yearOptionBookHeldList').value;
		var tcaption = document.getElementById('tcaption').innerText;
		var dataRowVal = dataRow.value;
		var dataColVal = dataCol.value;
		//화면의 데이터들을 dataListStr로 넘기기
		var dataList = null;
		dataList = dataListArray();
		console.log(dataList);
		var dataListStr = JSON.stringify(dataList);
		
		var form = document.createElement('form');
		form.setAttribute("method","post");
		form.setAttribute("action","/statistics_print_report.do");
		document.body.appendChild(form);
		
		//아래 tcaption 제목으로 페이지 분기.
		var insert1 = document.createElement('input');
		insert1.setAttribute("type","hidden");
		insert1.setAttribute("name","tcaption");
		insert1.setAttribute("value", tcaption);
		form.appendChild(insert1);
		
		var insert2 = document.createElement('input');
		insert2.setAttribute("type","hidden");
		insert2.setAttribute("name","targetYear");
		insert2.setAttribute("value", targetYear);
		form.appendChild(insert2);
		
		var insert3 = document.createElement('input');
		insert3.setAttribute("type","hidden");
		insert3.setAttribute("name","dataRowVal");
		insert3.setAttribute("value", dataRowVal);
		form.appendChild(insert3);
		
		var insert4 = document.createElement('input');
		insert4.setAttribute("type","hidden");
		insert4.setAttribute("name","dataColVal");
		insert4.setAttribute("value", dataColVal);
		form.appendChild(insert4);
		
		var insert5 = document.createElement('input');
		insert5.setAttribute("type","hidden");
		insert5.setAttribute("name","dataListStr");
		insert5.setAttribute("value", dataListStr);
		form.appendChild(insert5);
		
		window.open('', 'printReportPopup', 'width=1080,height=800,scrollbars=yes');
		form.target = 'printReportPopup';
		
		form.submit();
		
		
		/* var url = '${pageContext.request.contextPath}/statistics_print_report.do'
		window.open(url, '_black', 'width=1080,height=800,scrollbars=yes'); */
	}
	
	//화면에 떠있는 데이터를 2차 배열로 만들기.
	function dataListArray() {
		/* console.log(tbody); */
		var tr = tbody.childNodes;
		//td length를 위하여 선언.
		var td = tr[0].childNodes;
		/* console.log('tr데이터'+tr);
		console.log('tr의 길이'+tr.length);
		console.log('td데이터'+td[0].innerText);
		console.log('td의 길이'+td.length); */
		
		var arr = new Array(tr.length);
		for(var i=0; i<tr.length; i++) {
			arr[i] = new Array(td.length);
			for(var j=0; j<td.length; j++) {
				var td = tr[i].childNodes;
				/* console.log('i: '+i+' j: '+j+' 값: '+td[j].innerText); */
				arr[i][j] = td[j].innerText;
			} 
		}
		
		return arr;
	}
	
	//일일 대출/반납 정보 호출
	function todayBrwRtnList(pickDate) {
		$.ajax({
			url: "/stsc/select_brw_rtn_list.do",
			type:'POST',
			data: {
				pickDate
			},
			success: function(data) {
				if(data.rt != 'OK') {
					alert(data.rt);
				} else {
					console.log(data.brwRtnList);
					//불러온 제목 넣어주기. 초기화
					tcaption.innerHTML = '';
					/* var tcaption = document.getElementById('tcaption'); */
					tcaption.innerHTML = '일일대출반납보고서';
					/* var thead = document.getElementById('thead'); */
					/* var tbody = document.getElementById('tbody'); */
					//테이블 값 초기화
					thead.innerHTML = '';
					tbody.innerHTML = '';
					var rowHead = null;
					var rowBody = null;
					var brwRtnList = data.brwRtnList;
					//데이터 행의 길이 input에 기록.+1 이유는 제목라인을 포함하기 때문에.
					dataRow.value = brwRtnList.length+1;
					//데이터 열의 길이 input에 기록 0~11번까지니까, 이런건 직접 기록.
					dataCol.value = 12;
					if(brwRtnList.length > 0) {
						rowBody = tbody.insertRow();
						var cell0 = rowBody.insertCell(0); 
						cell0.innerText = '번호';
						cell0.style.width = '40px';
						var cell1 = rowBody.insertCell(1); 
						cell1.innerText = '구분';
						cell1.style.width = '50px';
						rowBody.insertCell(2).innerText = '도서명';
						var cell3 = rowBody.insertCell(3);
						cell3.innerText = '저자명';
						cell3.style.width = '150px';
						var cell4 = rowBody.insertCell(4);
						cell4.innerText = '출판사';
						cell4.style.width = '80px';
						var cell5 = rowBody.insertCell(5);
						cell5.innerText = '등록번호';
						cell5.style.width = '90px';
						var cell6 = rowBody.insertCell(6); 
						cell6.innerText = '고객성명';
						cell6.style.width = '70px';
						var cell7 = rowBody.insertCell(7);
						cell7.innerText = '연락처';
						cell7.style.width = '120px';
						var cell8 = rowBody.insertCell(8);
						cell8.innerText = '고객번호';
						cell8.style.width = '90px';
						var cell9 = rowBody.insertCell(9);
						cell9.innerText = '대출일';
						cell9.style.width = '100px';
						var cell10 = rowBody.insertCell(10);
						cell10.innerText = '반납일';
						cell10.style.width = '100px';
						var cell11 = rowBody.insertCell(11);
						cell11.innerText = '비고';
						cell11.style.width = '40px';
						for(var i=0; i<brwRtnList.length; i++) {
							rowBody =  tbody.insertRow();
							rowBody.insertCell(0).innerText = i+1;
							//대출/반납 구분을 위한 if문
							var stateBrw = '';
							if(brwRtnList[i].endDateBrw == null || brwRtnList[i].endDateBrw == ''){
								stateBrw = '대출';
							} else {
								stateBrw = '반납';
							}
							rowBody.insertCell(1).innerText = stateBrw;
							rowBody.insertCell(2).innerText = brwRtnList[i].title;
							rowBody.insertCell(3).innerText = brwRtnList[i].writer;
							rowBody.insertCell(4).innerText = brwRtnList[i].publisher;
							rowBody.insertCell(5).innerText = brwRtnList[i].localIdBarcode;
							rowBody.insertCell(6).innerText = brwRtnList[i].name;
							rowBody.insertCell(7).innerText = brwRtnList[i].phone;
							rowBody.insertCell(8).innerText = brwRtnList[i].barcodeMbr;
							rowBody.insertCell(9).innerText = dateFormChange(brwRtnList[i].startDateBrw);
							rowBody.insertCell(10).innerText = dateFormChange(brwRtnList[i].endDateBrw);
							rowBody.insertCell(11).innerText = '';
						} //for문 끝
					} //if문 끝
				} //성공처리 끝
			} //통신처리 끝
		});
	}
	
		//화면에 떠있는 데이터를 excel로 
		function clickedDataListToExcel() {
			var targetYear = document.getElementById('yearOptionBookHeldList').value;
			var tcaption = document.getElementById('tcaption').innerText;
			var dataRowVal = dataRow.value;
			var dataColVal = dataCol.value;
			
			var dataList = null;
			dataList = dataListArray();
			console.log(dataList);
			var dataListStr = JSON.stringify(dataList);
			console.log('---------------------------');
			console.log(dataListStr);
			
			$.ajax({
				url: "/stsc/statistics_to_excel.do",
				type: 'POST',
				data: {
					targetYear,
					tcaption,
					dataRowVal,
					dataColVal,
					dataListStr
				},
				//dataType: "json",
				success: function(data) {
					console.log('도서목록 엑셀 변환: '+data.rt);
					console.log('파일 경로: '+data.filePath);
					
					var uploadFolderPath = data.filePath.substring(data.filePath.lastIndexOf("upload"));
					console.log('업로드폴더까지 경로:'+uploadFolderPath);
					var downloadPath = "/filesMapping/"+uploadFolderPath;
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
						xhr.open('GET', downloadPath);
						xhr.setRequestHeader('Content-type', 'application/json');
						xhr.send();
				}
			});
		};
		
		
		
		

//기간별대출통계 기간별대출권수 + 이용자수 + 대출도서 목록

/*makeExcelBrwAndMemberByDateDownload = () => {
	let downloadPath = null;
	new Promise((resolve)=>{
		downloadPath = makeExcelBrwAndMemberByDate();
		resolve();
	}).then(()=>{
		alert(downloadPath);
	})
}*/

function makeExcelBrwAndMemberByDateDownload() {
	let staDateStart = document.querySelector('#staDateStart').value;
	let staDateEnd = document.querySelector('#staDateEnd').value;
	
	let downloadLink = '';
	
	new Promise((resolve)=>{
		$.ajax({
			url: "/stsc/brw_and_member_by_date.do",
			type: 'GET',
			data: {
				staDateStart,
				staDateEnd
			},
			//dataType: "json",
			success: function(data) {
				if(data.rt != 'OK') {
					alert(data.rt);
				} else {
					downloadLink = data.downloadPath;
					//받은 결과값을 리졸브에 넣고, 리졸브를 받으면 다음 함수 실행
					resolve(data.downloadPath);
				}
			}
		})
	}).then((res)=>{
		let downUrl = "/download.do?file="+res;
		location.href = downUrl;
	})
	
};

































