//book_check_box js가 두개가 있는데
//그 이유는, side_jsp_page는 도서등록페이지에 사이드 페이지의
//체크박스를 제어하고
//그냥 book_check_box.js는 도서목록의 체크박스를 제어한다.
//통일해야하는데 귀찮아서 그냥 둠..



//도서마다 체크박스 두고 일괄 삭제 기능 하기 위함.

const searchTbody = document.querySelector('.searchCls');

const chkBoxArr = new Array();
const clsChkBox = document.querySelectorAll('.chk-box-book-id');
//reg_side_list.jsp 페이지에 이미 const로 선언되어있는 변수라 let으로 이름 바꿔서 선언
let labelType2 = document.getElementById('labelType').value;

window.addEventListener('load', function() {
	
})


//체크박스 인쇄버튼 클릭
function clickedPrintChkList() {
	console.log('체크박스배열: '+chkBoxArr);
	console.log('도서관 기본 라벨타입코드: '+labelType2);
	
	let a4LabelType = document.querySelector('#a4LabelType').value;
	
	if(a4LabelType=="") {
		a4LabelType = labelType2
	}
	
	let startPoint = document.querySelector('#startPoint').value;
	
	if(chkBoxArr.length>0) {
		var url = '/book/print_tag_page_by_book_id.do?tagType='+a4LabelType
			+"&startPoint="+startPoint;
		url = url + '&chkBoxArr='+chkBoxArr;
		window.open(url, '_blank', 'width=900,height=800,scrollbars=yes');
	} else {
		alert('체크된 도서가 없습니다.');
	}
}

//체크박스 전체 체크
function checkAll() {
	//배열 초기화
	chkBoxArr.length = 0;
	for(var i=0; i<clsChkBox.length; i++){
		//체크박스 체크하기
		clsChkBox[i].checked = true;
		let targetBookId = clsChkBox[i].parentNode.parentNode.children[0].innerText;
		console.log(targetBookId);
		if(clsChkBox[i].checked) {
			//target의 부모의 부모, 그 첫번째 자식 엘리먼트가 도서 id
			chkBoxArr.push(targetBookId);
		}
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
	if(e.target.classList.contains('chk-box-book-id')) {
		//let targetBookId = e.target.parentNode.parentNode.children[0].innerText; 
		let targetChildren = e.target.parentNode.parentNode.children;
		let tempBookId = null;
		for(var i=0; i<targetChildren.length; i++) {
			//클릭한 노드와 엘리먼트 전체를 가져와서 포문으로 클래스명 찾기.
			if(targetChildren[i].classList.contains('cls-book-id')){
				//클래스명으로 cls-book-id가 포함되어있으면 그 값을 arr에 추가
				//console.log(targetChildren[i].innerText);
				tempBookId = targetChildren[i].innerText;
			}
		}
		if(e.target.checked) {
			//target의 부모의 부모, 그 첫번째 자식 엘리먼트가 도서 id
			chkBoxArr.push(tempBookId);
		} else {
			let removeIdx = chkBoxArr.indexOf(tempBookId);
			if(removeIdx > -1) {
				chkBoxArr.splice(removeIdx, 1);
			}
		}
		console.log('체크박스배열: '+chkBoxArr);
	}
})

//일괄 삭제 버튼
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
}