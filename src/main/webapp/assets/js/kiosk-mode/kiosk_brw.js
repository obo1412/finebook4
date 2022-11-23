const timerValue = 120;

let timeCount = document.querySelector('#timer');
const userInput = document.querySelector('#userBarcode');
const bookInput = document.querySelector('#bookBarcode');
//회원 id 변수
let memberId = 0;
//도서 id 배열
let bookIdArr = new Array();

//도서 대출 권수 제한
let lastBrwCount = 0;

//창열리면 카운트 다운
window.addEventListener('load', function() {
	timeCount.innerHTML = timerValue;
	countDown;
	userInput.focus();
})

//화면 어디든 입력 감지시 타이머 초기화
window.addEventListener('click', () => {
	timeCount.innerHTML = timerValue;
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

const divBook = document.querySelector('.div__book');
divBook.addEventListener('click', ()=>{
	bookInput.focus();
})

//회원정보가 없음 알림 메시지
function toggleNoMemberMsg() {
	let msgNoMember = document.querySelector('.msg__no__member'); 
	msgNoMember.classList.remove('hidden');
	setTimeout(()=>{
		msgNoMember.classList.add('hidden');
	}, 3000)
}


//회원검색 엔터
const enterGetMemberInfo = () => {
	if(event.keyCode==13){
		getMemberInfo();
	}
}

//회원검색 정보 호출 함수
const getMemberInfo = () => {
	let memberBarcode = userInput.value;
	if(memberBarcode == '') {
		toggleNoMemberMsg();
		return;
	}
	
	$.ajax({
		url: "/kiosk_mode/search_member.do",
		type:'GET',
		data: {
			memberBarcode
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				//console.log(lastBrwCount);
				//인풋초기화.
				userInput.value = '';
				//console.log(data.memberItem);
				let divUserData = document.querySelector('.div__user__data');
				//사용할 멤버id변수에 id주입
				memberId = data.memberItem.id;
				
				divUserData.innerHTML = 
					data.memberItem.name +"/"+data.memberItem.phone
					+ "/" + data.lastBrwCount+"권 대출가능 /"
					+ data.memberItem.brwLimit+"총대출한도";
				
				//도서 대출 가능 권수 주입.
				lastBrwCount = data.lastBrwCount;
				//console.log(lastBrwCount);
				
				//회원 입력창 없애고, 도서 검색창 띄우기
				let divUser = document.querySelector(".div__user");
				divUser.classList.add('hidden');
				
				divBook.classList.remove('hidden');
				
				bookInput.focus();
			}
		}
	});
}


//도서검색 엔터
const enterGetBookInfo = () => {
	if(event.keyCode==13){
		getBookInfo();
	}
}
//도서 검색 정보 호출 함수
const getBookInfo = () => {
	let bookBarcode = bookInput.value;
	if(bookBarcode == '') {
		toggleBookScanMsg(".msg__no__book");
		return;
	}
	
	$.ajax({
		url: "/kiosk_mode/search_book.do",
		type:'GET',
		data: {
			bookBarcode
		},
		success: function(data) {
			if(data.rt != 'OK') {
				bookInput.value = '';
				alert(data.rt);
			} else {
				//인풋초기화.
				bookInput.value = '';
				//console.log(data.bookItem);
				
				//도서 배열에 도서 id 넣기.
				let checkId = dupCheckBookId(data.bookItem.id);
				if(checkId) {
					let newDiv = document.createElement('div');
					newDiv.classList.add('book__item__row');
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
					let newButton = document.createElement('button');
					newButton.classList.add('book__item__cancel');
					newButton.innerHTML = '<i class="fas fa-times-circle"></i>';
					newDiv.appendChild(newButton);
					
					let divBookData = document.querySelector('.div__book__data__list');
					divBookData.appendChild(newDiv);
				} else {
					toggleBookScanMsg(".msg__already__scan");
				}
				
				console.log(bookIdArr);
				
				bookInput.focus();
			}
		}
	});
}

//book list에서 x 표시 누르면 도서 삭제
const bookListBody = document.querySelector('.div__book__data__list');
bookListBody.addEventListener('click', (event)=>{
	if(event.target.closest('.book__item__cancel')) {
		let row = event.target.closest('.book__item__row');
		let removeId = row.querySelector('.book__id').textContent;
		let rmIdx = bookIdArr.indexOf(parseInt(removeId));
		if(rmIdx > -1) {
			bookIdArr.splice(rmIdx, 1);
		}
		row.remove();
		
		console.log(bookIdArr);
	}
})


//도서 배열에 같은 도서id값이 있는지 중복체크 함수
function dupCheckBookId(bookId) {
	if(!bookIdArr.includes(bookId)) {
		bookIdArr.push(bookId);
		return true;
	}
	return false;
}


//이미 입력된 도서 메시지 토글 함수
function toggleBookScanMsg(classText) {
	let msgAlreadyScan = document.querySelector(classText); 
	msgAlreadyScan.classList.remove('hidden');
	setTimeout(()=>{
		msgAlreadyScan.classList.add('hidden');
	}, 3000)
}



//대출하기 버튼 실행
function btnClickedBrw() {
	if(memberId <= 0) {
		toggleNoMemberMsg();
		return;
	}
	if(bookIdArr.length == 0) {
		toggleBookScanMsg(".msg__no__book");
		return;
	}
	
	console.log('회원번호: '+memberId);
	console.log(bookIdArr);
	console.log(bookIdArr.length);
	console.log(lastBrwCount);
	
	if(bookIdArr.length > lastBrwCount) {
		toggleBookScanMsg(".msg__over__book__brw");
		return;
	}
	
	$.ajax({
		url: "/kiosk_mode/brw_ok.do",
		type:'POST',
		traditional: true,
		data: {
			memberId,
			bookIdArr : JSON.stringify(bookIdArr)
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				divBook.classList.add('hidden');
				toggleBookScanMsg(".msg__success__brw");
				setTimeout(()=>{
					location.href = "/kiosk_mode.do";
				}, 3000)
			}
		}
	});
}






