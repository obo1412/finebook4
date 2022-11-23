let btnSearchAndCheckDup = document.getElementById('btnSearchAndCheckDup');
//보유도서목록 박스
let divBookInLibList = document.querySelector('.book__in__lib__list');

window.addEventListener('load', function() {
	
})


//보유도서 상세 보기
/*divBookInLibList.addEventListener('click', (e)=> {
	let inLibBook = e.target.closest('.inlib__book');
	if(!inLibBook) {
		//console.log('!inLibBook 실행');
		return;
	}
	//여기 수정중
	let inLibBookData = inLibBook.querySelector('.inlib__book__data').innerHTML;
	let bDataJson = JSON.parse(inLibBookData);
	//console.log(bDataJson);
	//상세보기 함수로 데이터 전달
	detailInLibBook(bDataJson);
});*/

//상세보기 div 박스 엘리먼트
let detailBookInLib = document.querySelector('.detail__book__in__lib');
//보유도서 상세보기
//현재는 안씀.
function detailInLibBook(jsonData) {
	//초기화
	clearDetailInLibBook();
	
	if(jsonData.imageLink != null){
		detailBookInLib.querySelector('#img__book__inlib').src = jsonData.imageLink;
	}
	detailBookInLib.querySelector('.detail__inlib__title').textContent = jsonData.title;
	detailBookInLib.querySelector('.detail__inlib__author').textContent = jsonData.writer;
	detailBookInLib.querySelector('.detail__inlib__barcode').textContent = jsonData.localIdBarcode;
	detailBookInLib.querySelector('.detail__inlib__publisher').textContent = jsonData.publisher;
	detailBookInLib.querySelector('.detail__inlib__isbn13').textContent = jsonData.isbn13;
}

//보유도서 상세보기 초기화 함수
//현재는 안씀.
function clearDetailInLibBook() {
	detailBookInLib.querySelector('#img__book__inlib').src = "";
	detailBookInLib.querySelector('.detail__inlib__title').textContent = "";
	detailBookInLib.querySelector('.detail__inlib__author').textContent = "";
	detailBookInLib.querySelector('.detail__inlib__barcode').textContent = "";
	detailBookInLib.querySelector('.detail__inlib__publisher').textContent = "";
	detailBookInLib.querySelector('.detail__inlib__isbn13').textContent = "";
}


//검색 버튼 클릭시 동작
btnSearchAndCheckDup.addEventListener('click', doSearchDupAndApi);

//도서 종합 검색 버튼 엔터
function doSearchEnterKeyDown() {
	if(event.keyCode==13){
		doSearchDupAndApi();
	}
};

//비동기통신 종합 검색 SearchBook.java 에 있음.
function doSearchDupAndApi() {
		
	let isbnReq = document.getElementById('isbn_req').value;
	let titleReq = document.getElementById('title_req').value;
	let authorReq = document.getElementById('author_req').value;
	
	console.log('isbn'+isbnReq);
	console.log('titleReq'+titleReq);
	console.log('authorReq'+authorReq);
	
	isbnReq = isbnReq.trim();
	titleReq = titleReq.trim();
	authorReq = authorReq.trim();
	
	//검색 형태를 판별할 스위치 0 이상이면, 에러
	let inputType = 0;
	
	
	//값 받는건 공백으로 받아짐,
	//언제 null이고 언제 공백인거야? 몰라...
	if(isbnReq==''&&titleReq==''&&authorReq=='') {
		inputType++;
		alert('검색어를 입력해주세요');
		return;
	} else if(isbnReq==''&&(titleReq==''||authorReq=='')) {
		inputType++;
		alert('제목과 저자는 모두 입력해야 검색이 가능합니다.');
		return;
	}
	
	
	if(isbnReq.length!=10&&isbnReq.length!=13&&isbnReq!=''){
		alert('ISBN은 10자리 또는 13자리여야 합니다.');
		return;
	}
	
	
	//도서명 적을 때, isbn 적은거 없애기
	document.getElementById('title_req').addEventListener('change', ()=> {
		document.getElementById('isbn_req').value = '';
	});
	
	$.ajax({
		url: "/book/search_book_dup_check_lib.do",
		type:'GET',
		data: {
			isbnReq,
			titleReq,
			authorReq
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				new Promise((resolve)=>{
					
					//리스트 tbody 초기화
					let tbodyLib = document.querySelector('.tb__lib__body');
					tbodyLib.innerHTML = "";
					
					//도서관 내의 데이터 받아오기.
					let bookList = data.bookList;
					//템플릿
					let tmplBookInLib = document.querySelector('#tmpl__book__in__lib').content;
					
					if(bookList.length>0){
						//console.log('도서관에 '+bookList.length+'권의 같은 도서가 존재합니다.');
						for(let i=0; i<bookList.length; i++){
							let tmplClone = tmplBookInLib.cloneNode(true);
							tmplClone.querySelector('.tb__lib__num').textContent = i+1;
							tmplClone.querySelector('.inlib__title').textContent = bookList[i].title;
							tmplClone.querySelector('.inlib__author').textContent = bookList[i].writer;
							tmplClone.querySelector('.inlib__publisher').textContent = bookList[i].publisher;
							tmplClone.querySelector('.inlib__isbn13').textContent = bookList[i].isbn13;
							tmplClone.querySelector('.inlib__barcode').textContent = bookList[i].localIdBarcode;
							tmplClone.querySelector('.inlib__book__data').innerHTML = JSON.stringify(bookList[i]);
							tbodyLib.appendChild(tmplClone);
						}
					} else {
						//도서관 내에 보유한 도서가 없으면 도서 없음 표시.
						let noDataMsg = noDataList('inlib');
						tbodyLib.appendChild(noDataMsg);
					}
					
					resolve();
				}).then(()=>{
					
					//도서요청 게시판에 이미 동일한 도서가 신청되어있는지 체크
					if(isbnReq != '') {
						checkReqBookInDocumentList(isbnReq, null, null);
					} else if(titleReq != '' && authorReq != '') {
						checkReqBookInDocumentList(null, titleReq, authorReq);
					}
					
				}).then(()=>{
					//extData 외부 external Data api 데이터
					let extData = data.extData;
					if(extData != null){
						//데이터 형태 상관없이 detailApiData 안에 데이터 형태 바꿔주는 함수 포함
						detailApiData(extData);
					} else {
						console.log('외부 결과값 없음');
					}
				}).then(()=>{
					
					//api 목록 초기화
					let divApiList = document.querySelector('.tb__api__body');
					divApiList.innerHTML = "";
					
					//api 상세보기 화면 초기화
					clearDetailApi();
					
					let apiList = data.apiList;
					//api 목록 담을 템플릿
					let tmplApi = document.querySelector('#tmpl__book__api').content;
					
					
					if(apiList != null){
						console.log('****************************');
						console.log(apiList);
						if(apiList.item.length>0){
							console.log('알라딘 결과값 가져옮');
							for(let j=0; j<apiList.item.length; j++){
								//console.log(apiList.item[j]);
								//아래는 목록에 값을 만들어주는 것.
								let tmplClone = tmplApi.cloneNode(true);
								tmplClone.querySelector('.tb__api__num').textContent = j+1;
								tmplClone.querySelector('.api__title').textContent = apiList.item[j].title;
								tmplClone.querySelector('.api__author').textContent = apiList.item[j].author;
								tmplClone.querySelector('.api__publisher').textContent = apiList.item[j].publisher;
								tmplClone.querySelector('.api__isbn13').textContent = apiList.item[j].isbn13;
								tmplClone.querySelector('.api__book__data').innerHTML =  JSON.stringify(apiList.item[j]);
								divApiList.appendChild(tmplClone);
							}
							
							//상세보기 값으로 첫번째 데이터 넘기기
							detailApiData(apiList.item[0]);
							
						} else if(apiList.result != null) {
							//console.log('국중 결과 목록 가져옴');
							if(apiList.result.length >0) {
								for(let j=0; j<apiList.result.length; j++){
									//console.log(apiList.result[j]);
									let tmplClone = tmplApi.cloneNode(true);
									tmplClone.querySelector('.tb__api__num').textContent = j+1;
									tmplClone.querySelector('.api__title').textContent = apiList.result[j].titleInfo;
									tmplClone.querySelector('.api__author').textContent = apiList.result[j].authorInfo;
									tmplClone.querySelector('.api__publisher').textContent = apiList.result[j].pubInfo;
									tmplClone.querySelector('.api__isbn13').textContent = apiList.result[j].isbn;
									tmplClone.querySelector('.api__book__data').innerHTML = JSON.stringify(apiList.result[j]);
									divApiList.appendChild(tmplClone);
								}
								
								//상세보기 값으로 첫번째 데이터 넘기기
								detailApiData(apiList.result[0]);
							}
						} else {
							//api 검색 결과값 없을 경우 메시지 처리
							clearDetailApi();
							let noDataMsg = noDataList('api');
							divApiList.appendChild(noDataMsg);
						}
						//console.log('****************************');
					} else {
						clearDetailApi();
						let noDataMsg = noDataList('api');
						divApiList.appendChild(noDataMsg);
						console.log('api 결과 목록 없음.');
					}
				})
				
				
				
			}//success 처리 끝
		}
	})
}

//도서관 내 또는 API 결과 값이 없을 경우 화면 표시 처리
function noDataList(type) {
	let tr = document.createElement('tr');
	let td = document.createElement('td');
	//api 일 경우 컬럼 3개, 도서관목록 6개
	let colCount = 3;
	if(type=='inlib') {
		colCount = 6;
	}
	td.setAttribute('colspan', colCount);
	td.style.color = 'red';
	td.style.height = '55px';
	
	td.innerHTML = "검색 결과 값이 없습니다. 다른 검색을 이용하세요."
		+"<br/>1, 2차 검색 모두 결과가 없는 경우, 우측 \"구매신청하기\"버튼을 눌러 직접 입력 바랍니다.";
	
	tr.appendChild(td);
	
	return tr;
}

//도서요청 목록에 있는지 없는지 체크
function checkReqBookInDocumentList(isbnApi, titleApi, authorApi) {
	
	$.ajax({
		url: "/book/search_book_dup_check_document.do",
		type:'GET',
		data: {
			isbnApi,
			titleApi,
			authorApi
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				new Promise((resolve)=>{
					removeNewTr();
					resolve();
				}).then(()=> {
					let documentList = data.documentList;
					//존재하는 도서요청게시글 목록 alert 삭제
					let tbodyLib = document.querySelector('.tb__lib__body');
					//console.log(documentList);
					for(let k=0; k<documentList.length; k++){
						let newTr = document.createElement("tr");
						newTr.classList.add("alert__already__document");
						
						//css에서 tr 두줄에 한번씩 border 적용하는 것을 위해
						//아래와같이 tr 처리를 하나 더 하였으나,
						//tr 2줄이 필요없게 되었으므로 주석처리
//						let newTr2 = document.createElement("tr");
//						newTr2.classList.add("alert__already__document");
						
						let newTd = document.createElement("td");
						newTd.setAttribute("colspan","6");
						
						let aTag = document.createElement("a");
						aTag.href = "/bbs/document_read.do?category=request&document_id="
							+documentList[k].id;
						
						aTag.textContent = documentList[k].subject;
						
						newTd.innerHTML = "총"+documentList.length
						+"건의 같은 도서 요청이 존재합니다. <br />";
						
						newTd.appendChild(aTag);
						
						newTr.appendChild(newTd);
						tbodyLib.prepend(newTr);
						//tbodyLib.prepend(newTr2);
					}
				})
				
				
				
			}
		}
	})
}

//게시글 목록에 중복요청 알림인 녀석들 지우기.
function removeNewTr() {
	let classToRemove = document.querySelectorAll('.alert__already__document');
	if(classToRemove != null) {
		//console.log(classToRemove.length);
		for(let l=0; l<classToRemove.length; l++){
			document.querySelector('.tb__lib__body').removeChild(classToRemove[l]);
		}
	}
}


//json 데이터를 일정한 형태로 뿌려주는 함수
function jsonDataToViewData(jsonData) {
	if(jsonData == null){
		return;
	}
	
	//console.log('ToviewData 동작');
	//국중결과값 형태 -> 알라딘 형태로 씁시다.
	if(jsonData.titleInfo != null){
		//console.log('titleInfo null 아님 '+jsonData.titleInfo);
		jsonData.title = jsonData.titleInfo;
		//isbn은 알라딘에서 isbn과 isbn13으로 나뉘어있고
		//국중에선 isbn 하나로 관리되므로, 제목에 제어를 묶는다.
		jsonData.isbn13 = jsonData.isbn;
	}
	if(jsonData.authorInfo != null){
		//console.log('authorInfo null 아님 '+jsonData.authorInfo);
		jsonData.author = jsonData.authorInfo;
	}
	if(jsonData.pubInfo != null){
		jsonData.publisher = jsonData.pubInfo;
	}
	
	return jsonData;
}

//api 도서 리스트 클릭 대기 객체
let bookApiList= document.querySelector('.book__api__list');
//api 검색 도서 클릭시 상세 보기로 전환
bookApiList.addEventListener('click', (e)=> {
	//console.log(e.target);
	let apiBook = e.target.closest('.api__book');
	//console.log(apiBook);
	if(!apiBook) {
		//console.log('!apiBook 실행');
		return;
	}
	let apiBookData = apiBook.querySelector('.api__book__data').innerHTML;
	//console.log(apiBookData);
	let apiDataJson = JSON.parse(apiBookData);
	//console.log(apiDataJson);
	//상세보기 함수로 데이터 전달
	detailApiData(apiDataJson);
});

//상세보기 div
let detailBookApi = document.querySelector('.detail__book__api');
//정보가 들어오면 api 상세보기로.
function detailApiData(jsonData) {
	if(jsonData == null){
		return;
	}
	
	new Promise((resolve)=> {
		//데이터 형태 변환
		jsonData = jsonDataToViewData(jsonData);
		resolve();
	}).then(()=> {
		//api 상세 정보 초기화
		clearDetailApi();
	}).then(()=> {
		if(jsonData.cover != null){
			document.querySelector('#img__book__api').src = jsonData.cover;
			document.querySelector('#imageLink').value = jsonData.cover;
		}
		document.querySelector('#inputApiTitle').value = jsonData.title;
		document.querySelector('#inputApiAuthor').value = jsonData.author;
		document.querySelector('#inputApiPublisher').value = jsonData.publisher;
		document.querySelector('#inputApiIsbn').value = jsonData.isbn13;
	}).then(()=>{
		//도서 요청 목록에 있는지 체크 함수
		checkReqBookInDocumentList(jsonData.isbn13, jsonData.title, jsonData.author);
	})
	

}

//api 정보 상세 보기 초기화
function clearDetailApi() {
	document.querySelector('#img__book__api').src = "";
	document.querySelector('#imageLink').value = "";
	
	document.querySelector('#inputApiTitle').value = "";
	document.querySelector('#inputApiAuthor').value = "";
	document.querySelector('#inputApiPublisher').value = "";
	document.querySelector('#inputApiIsbn').value = "";
}


//글쓰기 버튼 구현
const btnWriteBookReq = document.querySelector('#btnWriteBookReq');

btnWriteBookReq.addEventListener('click', (e)=> {
	e.preventDefault();
	
	let inputImageLink = document.querySelector('#imageLink');
	let inputApiTitle = document.querySelector('#inputApiTitle');
	let inputApiAuthor = document.querySelector('#inputApiAuthor');
	let inputApiPublisher = document.querySelector('#inputApiPublisher');
	let inputApiIsbn = document.querySelector('#inputApiIsbn');
	
//	console.log(inputImageLink);
//	console.log(inputApiTitle);
//	console.log(inputApiAuthor);
//	console.log(inputApiPublisher);
//	console.log(inputApiIsbn);
	
	let formBookReq = document.createElement("form");
	formBookReq.setAttribute("charset", "UTF-8");
	formBookReq.setAttribute("method","get");
	formBookReq.setAttribute("action","/bbs/document_write.do?category=request");
	
	let inputCategory = document.createElement("input");
	inputCategory.setAttribute("type", "text");
	inputCategory.setAttribute("name", "category");
	inputCategory.value = "request";
	
	let inputTitle = document.createElement("input");
	inputTitle.setAttribute("type", "text");
	inputTitle.setAttribute("name", "inputApiTitle");
	inputTitle.value = inputApiTitle.value;
	
	formBookReq.appendChild(inputCategory);
	formBookReq.appendChild(inputImageLink);
	formBookReq.appendChild(inputApiTitle);
	formBookReq.appendChild(inputApiAuthor);
	formBookReq.appendChild(inputApiPublisher);
	formBookReq.appendChild(inputApiIsbn);

	document.body.appendChild(formBookReq);
	formBookReq.submit();
});





















