
//버튼

let btnStateRequest = document.querySelector('#btn__state__request');
let btnStateProcessing = document.querySelector('#btn__state__processing');
let btnStateDone = document.querySelector('#btn__state__done');

//제목
let documentSubject = document.querySelector('#document__subject');


btnStateRequest.addEventListener('click', ()=> {
	event.preventDefault();
	
	changeSubjectView('요청');
})

btnStateProcessing.addEventListener('click', ()=> {
	event.preventDefault();
	
	changeSubjectView('진행중');
})

btnStateDone.addEventListener('click', ()=> {
	event.preventDefault();
	
	changeSubjectView('완료');
})

//제목만 바꿔주는 함수
function changeSubjectView(subject) {
	let beforeChange = documentSubject.textContent;
	beforeChange = beforeChange.trim();
	
	if(beforeChange.indexOf("[요청]")==0) {
		beforeChange = beforeChange.replace("[요청]","["+subject+"]");
	} else if(beforeChange.indexOf("[진행중]")==0) {
		beforeChange= beforeChange.replace("[진행중]","["+subject+"]");
	} else if(beforeChange.indexOf("[완료]")==0) {
		beforeChange= beforeChange.replace("[완료]","["+subject+"]");
	}
	
	let document_id = document.querySelector('#readDocumentId').value;
	
	$.ajax({
		url: "/book/update_document_subject_state.do",
		type:'GET',
		data: {
			document_id,
			stateTo: beforeChange
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				//통신에 성공하면, 화면 변환 처리.
				console.log('성공공공공공공공');
				documentSubject.textContent = beforeChange;
			}
		}
	})
}


















