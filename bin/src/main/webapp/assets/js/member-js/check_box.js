//도서마다 체크박스 두고 일괄 삭제 기능 하기 위함.
const searchTbody = document.querySelector('.searchCls');
const chkBoxArr = new Array();
const clsChkBox = document.querySelectorAll('.chk-box-member-id');
//checkAll 스위치
let checkAllswitch = 0;


window.addEventListener('load', function() {
	
})


//체크박스 인쇄버튼 클릭
function printMembershipA4() {
	event.preventDefault();
	
	console.log('체크박스배열: '+chkBoxArr);

	var url = '/member/print_membership.do';
	if(chkBoxArr.length > 0){
		url = url + '?chkBoxArr='+chkBoxArr;
	}
	window.open(url, '_blank', 'width=1080,height=800,scrollbars=yes');

}

//체크박스 전체 체크
function checkAll() {
	//배열 초기화
	chkBoxArr.length = 0;
	if(checkAllswitch == 0) {
		for(var i=0; i<clsChkBox.length; i++){
			//체크박스 체크하기
			clsChkBox[i].checked = true;
			let targetMemberId = clsChkBox[i].previousElementSibling.value;
			//console.log(targetMemberId);
			if(clsChkBox[i].checked) {
				//target의 부모의 부모, 그 첫번째 자식 엘리먼트가 도서 id
				chkBoxArr.push(targetMemberId);
			}
		}
		//스위치 값 +1
		checkAllswitch++;
	} else {
		for(let i=0; i<clsChkBox.length; i++){
			//체크박스 체크하기
			clsChkBox[i].checked = false;
		}
		//해제 후 0으로 다시 만들어주기
		checkAllswitch = 0;
	}
	
	console.log('체크박스배열: '+chkBoxArr);
}

//체크 박스 전체 해제
function uncheckAll() {
	//배열 초기화
	chkBoxArr.length = 0;
	for(let i=0; i<clsChkBox.length; i++){
		//체크박스 체크하기
		clsChkBox[i].checked = false;
	}
	console.log('체크박스배열: '+chkBoxArr);
}

//체크박스 변화 감지 함수 체크박스 체크/해제
searchTbody.addEventListener('change', (e)=> {
	if(e.target.classList.contains('chk-box-member-id')) {
		//let targetBookId = e.target.parentNode.parentNode.children[0].innerText;
		//형제 엘리먼트 sibling
		let tempMemberId = e.target.previousElementSibling.value;
		if(e.target.checked) {
			//target의 부모의 부모, 그 첫번째 자식 엘리먼트가 도서 id
			chkBoxArr.push(tempMemberId);
		} else {
			let removeIdx = chkBoxArr.indexOf(tempMemberId);
			if(removeIdx > -1) {
				chkBoxArr.splice(removeIdx, 1);
			}
		}
		console.log('체크박스배열: '+chkBoxArr);
	}
})

//삭제버튼은 book check box 와 동일 수정 안했음.
/*//일괄 삭제 버튼
function clickedDeleteBatchBook() {
	if(chkBoxArr.length>0) {
		for(var i=0; i<chkBoxArr.length; i++){
			console.log('id값은: '+chkBoxArr[i]);
			deleteBookBatch(chkBoxArr[i]);
		}
		location.reload();
	} else {
		alert('체크된 도서가 없습니다.');
	}
}

//일괄 삭제 함수
function deleteBookBatch(bookHeldId) {
	$.ajax({
		url: "/book/book_held_delete_batch_by_checkbox.do",
		type:'POST',
		async: false, //ajax결과값이 나오면 다음 함수가 진행된다.
		data: {
			bookHeldId
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				
			}
		}
	});
}*/