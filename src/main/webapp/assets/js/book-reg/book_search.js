
let inputBookTitle = document.getElementById('bookTitle');
let inputAuthor = document.getElementById('author');
let inputAuthorCode = document.getElementById('authorCode');
let srcClassCode = document.getElementById('srcClassCode');
let inputClassCode = document.getElementById('classificationCode');
let inputVolumeCode = document.getElementById('volumeCode');
let inputCopyCode = document.getElementById('copyCode');
let inputBookCateg = document.getElementById('bookCateg');
let inputPublisher = document.getElementById('publisher');
let inputPubDate = document.getElementById('pubDate');
let inputPage = document.getElementById('itemPage');
let inputPrice = document.getElementById('price');
let inputIsbn13 = document.getElementById('isbn13');
let inputIsbn10 = document.getElementById('isbn10');
let inputBookSize = document.getElementById('bookSize');
let imgBookCover = document.getElementById('bookCover');
let inputBookCover = document.getElementsByName('bookCover');
let textBookDesc = document.getElementById('bookDesc');

//청구기호 서머리
let smryClassCodeHead = document.getElementById('summaryClassHeadCode');
let smryClassCode = document.getElementById('summaryClassCode');
let smryAuthorCode = document.getElementById('summaryAuthorCode');
let smryVolCode = document.getElementById('summaryVolumeCode');
let smryCopyCode = document.getElementById('summaryCopyCode');


window.addEventListener('load', function() {
	
})

//값이 없을땐, 십진분류 빨간색, 있을땐 검은색
inputClassCode.addEventListener('change', (e)=> {
	if(inputClassCode.value==null||inputClassCode.value==''){
		inputClassCode.style.borderColor = 'red';
	} else {
		inputClassCode.style.borderColor = 'black';
	}
})


//도서 검색 엔터키
function searchBookEnterKeyDown() {
	if(event.keyCode==13){
		clickedSearchBook();
	}
}

//도서 검색
function clickedSearchBook() {
	let searchKey = document.getElementById('search-book-info').value;
	if(searchKey.indexOf('-')>-1){
		searchKey = searchKey.replaceAll('-','');
	}
	if(searchKey==null||searchKey==''){
		alert('검색 키워드를 입력하세요.');
	} else {
		
		//전집류 복본기호 따로 체크할 수 있도록
//		let chkBoxColleBooks = document.getElementById('chk_box_collection_books_copy_code');
//		let colleBooksSwitch = chkBoxColleBooks.checked;
		//검색 키워드가 null이 아닐 경우 검색 실행
		$.ajax({
			url: "/book/search_book.do",
			type:'GET',
			async: false,
			data: {
				searchKey,
			},
			success: function(data) {
				if(data.rt != 'OK') {
					alert(data.rt);
				} else {
					inputBookTitle.value = data.bookTitle;
					if(data.bookTitle==null||data.bookTitle==""){
						alert('결과값이 없습니다.');
					}
					inputAuthor.value = data.author;
					inputAuthorCode.value = data.atcOut;
					srcClassCode.innerText = data.srcClassCode;
					inputClassCode.value = data.clsCode;
					inputVolumeCode.value = data.volCode;
					inputCopyCode.value = data.copyCode;
					inputBookCateg.value = data.category;
					inputPublisher.value = data.publisher;
					inputPubDate.value = data.pubDate;
					inputPage.value = data.intPage;
					inputPrice.value = data.price;
					inputIsbn13.value = data.isbn13;
					inputIsbn10.value = data.isbn10;
					inputBookSize.value = data.bookSize;
					//화면에 보여줄 src 루트와 값 전달을 위한 cover 루트
					imgBookCover.src = data.bookCover;
					inputBookCover[0].value = data.bookCover;
					textBookDesc.innerHTML = data.bookDesc;
					//서머리 채우기
					if(data.clsCode != null&&data.clsCode!=""){
						smryClassCode.style.color = "black";
						smryClassCode.innerText = data.clsCode;
						
						smryClassCodeHead.style.color = "black";
						smryClassCodeHead.innerText = (data.clsCode.substring(0,1))*100;
					} else {
						smryClassCode.innerText = '십진분류';
						smryClassCodeHead.innerText = '대분류';
					}
					if(data.atcOut != null&&data.atcOut!=""){
						smryAuthorCode.style.color = "black";
						smryAuthorCode.innerText = data.atcOut;
					} else {
						smryAuthorCode.innerText = '저자기호';
					}
					if(data.volCode != null&&data.volCode!=""){
						smryVolCode.style.color = "black";
						smryVolCode.innerText = "V"+data.volCode;
					} else {
						smryVolCode.style.color = "#D3D3D3";
						smryVolCode.innerText = '권차기호';
					}
					if(data.copyCode != null&&data.copyCode!=""){
						smryCopyCode.style.color = "black";
						smryCopyCode.innerText = "C"+data.copyCode;
					} else {
						smryCopyCode.style.color = "#D3D3D3";
						smryCopyCode.innerText = '복본기호';
					}
					
					//알라딘 구매링크를 위한 알라딘 아이템아이디
					//aladinItemId 선언은 aladin_reference.jsp에 되어있음.
					aladinItemId = data.aladinItemId;
				}
			}
		}); //ajax 끝
		
		//결과 값이 나왔으면, 제목과 권차기호 체크
		checkTitleContainNumAndRegOk(inputBookTitle.value, inputVolumeCode.value);
	}
}

//제목과 권차기호 체크 함수
function checkTitleContainNumAndRegOk(chkValue, chkVolume) {
	//등록 스위치 변수
	var switchRegOk = 0;
	var switchVolAlert = 0;

	if(chkValue!=null&&chkValue!=''){
		console.log('제목: '+chkValue);
		//체크하려는 값(도서명)이 null이거나 공백이 아닐 경우, 권차기호 체크
		if(chkVolume==null||chkVolume==''){
			console.log('권차기호가 없습니다.');
			//권차기호의 값이 null이거나 공백이 아닐 경우
			//정규식
			var regExp = /[0-9]/g;
			if(regExp.test(chkValue)) {
				//경고 해야되는 상황
				switchVolAlert++;
				switchRegOk++;
			} else {
				//제목에 숫자 미포함, 권차기호 없는 경우
				switchRegOk++;
			}
		} else {
			//제목 값이 있고 제목에 숫자 미포함, 권차기호 값이 있을 경우는 정상 진행
			switchRegOk++;
		}
	}
	
	//권차 알림 되어 있고, 알림 상황이면 알럿 띄우기
	if(chkBoxVolumeCodeAlarm.checked&&(switchVolAlert>0)) {
		clickedSound(3);
		alert('제목에 권차기호를 포함하고 있을 수 있습니다.');
	} else if(chkBoxStraightReg.checked&&(switchRegOk>0)) {
		//바로등록 체크되어있고, 등록스위치가 0 이상이면 등록
		document.getElementById('formRegBookOk').submit();
	}
}

