let timeCount = document.querySelector('#timer');
const bookInput = document.querySelector('#bookBarcode');

//창열리면 카운트 다운
window.addEventListener('load', function() {
	timeCount.innerHTML = 60;
	countDown;
	bookInput.focus();
})

//화면 어디든 입력 감지시 타이머 초기화
window.addEventListener('click', () => {
	timeCount.innerHTML = 60;
})

//카운트다운 셋인터벌 함수
const countDown = setInterval(function() {
	let timeSec = timeCount.textContent;
	timeSec--;
	timeCount.innerHTML = timeSec;
	if(timeSec < 0) {
		location.href = "/kiosk_mode.do";
	}
}, 1000);

//대충 가운데 터치하면 인풋포커스
const divBook = document.querySelector('.div__book');
divBook.addEventListener('click', ()=>{
	bookInput.focus();
});

//도서검색 엔터
const enterRetBookInfo = () => {
	if(event.keyCode==13){
		returnBookInfo();
	}
}
//도서 검색 정보 호출 함수
const returnBookInfo = () => {
	let bookBarcode = bookInput.value;
	if(bookBarcode == '') {
		toggleBookScanMsg(".msg__no__book");
		return;
	}
	console.log(bookBarcode);
	
	$.ajax({
		url: "/kiosk_mode/return_book_ok.do",
		type:'POST',
		data: {
			bookBarcode
		},
		success: function(data) {
			if(data.rt != 'OK') {
				bookInput.value = '';
//			alert(data.rt);
				//반납처리 메세지.
				toggleBookScanMsg('.msg__already__scan');
			} else {
				//인풋초기화.
				bookInput.value = '';
				//console.log(data.bookItem);
				
				let newDiv = document.createElement('div');
				newDiv.classList.add('book__item__row');
				let newSpanMsg = document.createElement('span');
				newSpanMsg.innerHTML = "반납되었습니다.";
				newDiv.appendChild(newSpanMsg);
				
				let newSpanId = document.createElement('span');
				newSpanId.classList.add('book__id')
				newSpanId.innerHTML = data.bookItem.id;
				newDiv.appendChild(newSpanId);
				
				let newSpan1 = document.createElement('span');
				newSpan1.innerHTML = data.bookItem.title;
				newDiv.appendChild(newSpan1);
				let newSpan2 = document.createElement('span');
				newSpan2.innerHTML = data.bookItem.localIdBarcode;
				newDiv.appendChild(newSpan2);
				
				let divBookData = document.querySelector('.div__book__data__list');
				divBookData.appendChild(newDiv);
				
				bookInput.focus();
			}
		}
	});
}



//이미 입력된 도서 메시지 토글 함수
function toggleBookScanMsg(classText) {
	let msgAlreadyScan = document.querySelector(classText); 
	msgAlreadyScan.classList.remove('hidden');
	setTimeout(()=>{
		msgAlreadyScan.classList.add('hidden');
	}, 3000)
}






