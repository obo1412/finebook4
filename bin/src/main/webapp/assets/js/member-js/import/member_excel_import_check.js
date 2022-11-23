
//form 가져오기
//const formImportExcelCheck = document.getElementById('formImportExcelCheck');
//행 (row) 개수
const row_crt = document.getElementById('row_crt');
//열(컬럼) 개수
const lastCellCount = document.getElementById('lastCellCount');
//서버에올라간 파일 경로
const loadFilePath = document.getElementById('loadFilePath');
//db에 등록된 파일 id
const fileId = document.getElementById('fileId');
let importProcess = null;
//현재 번호 input
const curRow = document.getElementById('curRow');
//진행율 progress
const progBar = document.getElementById('progBar');
//등록프로세스 기준 시간
const timeStandard = document.getElementById('timeStandard').value;
//인터벌 중단 재시작을 위한 변수
let dInterval = null;

//버튼 눌렸을 경우.
function clickedBtnSubmit() {
	/*formImportExcelCheck.action = "/book/import_book_excel_ok.do";
	//서브밋
	formImportExcelCheck.submit();*/
	
	let curRowNum = curRow.value;
	
	console.log("["+curRowNum+"] 등록처리");
	if(curRowNum==""||curRowNum==null||curRowNum==0) {
		curRowNum = 1;
	}
//	console.log('curRowNum 값: '+curRowNum);
//	console.log('row_crt 값: '+row_crt.value);
	
	importProcess = $.ajax({
		url: "/member/import_member_excel_ok.do",
		type: 'POST',
		timeout: 0, //timeout 기본은 30000으로 되어있음. 여기선 타임아웃없애기위해 명시.
		data: {
			fileId: fileId.value,
			row_crt: row_crt.value,
			lastCellCount: lastCellCount.value,
			loadFilePath: loadFilePath.value,
			curRow: curRowNum
		},
		/* dataType: "json", */
		/*beforeSend: (e) => {
		alert('데이터 통신을 시작합니다.');
		},*/
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				//서버로부터 받은 현재 단계 input에 넣어주기.
				let thisCurRow = data.curRow;
				curRow.value = thisCurRow;
				progBar.value = thisCurRow;
				alert(data.msg);
				//전부다 등록되면 페이지 이동처리.
				if(Number(data.curRow)>=row_crt.value) {
					//중간 확인 row 인터벌 중단
					clearInterval(dInterval);
					location.href = '/member/import_member_excel.do?menuHolder=setting';
				}
			}
		}
	}); //ajax 문 종료
	
	//인터벌 함수 실행
	displayInterval();
}

//ajax 중단
function cancelProcess() {
	console.log();
	
	//중간 확인 함수 끊기 전에 실행
	displayProgressing();
	//중간 확인 row 인터벌 중단
	clearInterval(dInterval);
	
	$.ajax({
		url: "/member/import_member_excel_pause.do",
		type: 'POST',
		data: {
			//서버 단에서 전역변수로 선언한 스토퍼를 조작하기 때문에
			//여기선 그냥 컨트롤러 클래스만 호출해주면 된다.
			//stopper: 1
		},
		/* dataType: "json", */
		/*beforeSend: (e) => {
		alert('데이터 통신을 시작합니다.');
		},*/
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				
			}
		}
	}); //ajax 문 종료
}

//현재 몇건 등록됬는지 db에 값 가져오는 비동기함수
function displayProgressing() {
	$.ajax({
		url: "/member/import_member_excel_display_current_progress.do",
		type: 'GET',
		data: {
			//기준시간 넣어주고
			timeStandard
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				progBar.value = data.regOkRow;
			}
		}
	}); //ajax 문 종료
}

//위 함수를 인터벌로 실행
function displayInterval() {
	dInterval = setInterval(displayProgressing, 5000);
}


//이하 안쓰는 코드

/*
window.addEventListener('load', function() {
	
})

const theadCls = document.querySelector('.theadCls');
	
const nameArr = [];
const phoneArr = [];
const birthdateArr = [];
const emailArr = [];
const postcodeArr = [];
const addr1Arr = [];
const addr2Arr = [];
const remarksArr = [];
const regDateArr = [];
const barcodeMbrArr = [];
const profileImgArr = [];
const rfUidArr = [];
const gradeNameArr = [];
const statusArr = [];


theadCls.addEventListener('change', (e)=> {
	if(e.target.classList.contains('selectCls')) {
		var idNum = e.target.id;
		var Num = idNum.substring(idNum.lastIndexOf('H')+1);
		var colNum = 'col'+Num;
		var thisCol = document.getElementsByName(colNum);
		console.log(e.target.value);
		console.log(colNum);
		
		if(e.target.value != '') {
			//선택한 select값이 공백이 아니면, 그 해당 값으로 선택한것.
			if(e.target.value == 'name') {
				for(var j=0; j<thisCol.length; j++) {
					console.log('j값은 '+j);
					nameArr[j] = thisCol[j].value;
					thisCol[j].setAttribute('name', 'nameArr');
					console.log(thisCol[j]);
				}
			} else if(e.target.value == 'phone') {
				for(var i=0; i<thisCol.length; i++) {
					phoneArr[i] = thisCol[i].value;
				}
			} else if(e.target.value == 'birthdate') {
				for(var i=0; i<thisCol.length; i++) {
					birthdateArr[i] = thisCol[i].value;
				}
			} else if(e.target.value == 'email') {
				for(var i=0; i<thisCol.length; i++) {
					emailArr[i] = thisCol[i].value;
				}
			} else if(e.target.value == 'postcode') {
				for(var i=0; i<thisCol.length; i++) {
					postcodeArr[i] = thisCol[i].value;
				}
			} else if(e.target.value == 'addr1') {
				for(var i=0; i<thisCol.length; i++) {
					addr1Arr[i] = thisCol[i].value;
				}
			} else if(e.target.value == 'addr2') {
				for(var i=0; i<thisCol.length; i++) {
					addr2Arr[i] = thisCol[i].value;
				}
			} else if(e.target.value == 'remarks') {
				for(var i=0; i<thisCol.length; i++) {
					remarksArr[i] = thisCol[i].value;
				}
			} else if(e.target.value == 'regDate') {
				for(var i=0; i<thisCol.length; i++) {
					regDateArr[i] = thisCol[i].value;
				}
			} else if(e.target.value == 'barcodeMbr') {
				for(var i=0; i<thisCol.length; i++) {
					barcodeMbrArr[i] = thisCol[i].value;
				}
			} else if(e.target.value == 'profileImg') {
				for(var i=0; i<thisCol.length; i++) {
					profileImgArr[i] = thisCol[i].value;
				}
			} else if(e.target.value == 'rfUid') {
				for(var i=0; i<thisCol.length; i++) {
					rfUidArr[i] = thisCol[i].value;
				}
			} else if(e.target.value == 'gradeName') {
				for(var i=0; i<thisCol.length; i++) {
					gradeNameArr[i] = thisCol[i].value;
				}
			} else if(e.target.value == 'status') {
				for(var i=0; i<thisCol.length; i++) {
					statusArr[i] = thisCol[i].value;
				}
			}
			//공백상태인 name attribute에 e.target.value 주입.
			e.target.setAttribute('name', e.target.value);
		} else {
			//기존에 존재하는 값인 경우 선택한 값이 e.target.value='' 공백 값일 경우
			if(e.target.getAttribute('name')=='name') {
				if(document.getElementsByName('name').length>1){
					console.log("nameArr 초기화 되지 않음.");
				} else {
					nameArr.length = 0;
					console.log("nameArr 초기화됨.");
				}
			} else if(e.target.getAttribute('name')=='phone') {
				if(document.getElementsByName('phone').length>1){
					console.log("phoneArr 초기화 되지 않음.");
				} else {
					phoneArr.length = 0;
					console.log("phoneArr 초기화됨.");
				}
			} else if(e.target.getAttribute('name')=='birthdate') {
				if(document.getElementsByName('birthdate').length>1){
					console.log("birthdateArr 초기화 되지 않음.");
				} else {
					birthdateArr.length = 0;
					console.log("birthdateArr 초기화됨.");
				}
			} else if(e.target.getAttribute('name')=='email') {
				if(document.getElementsByName('email').length>1){
					console.log("emailArr 초기화 되지 않음.");
				} else {
					emailArr.length = 0;
					console.log("emailArr 초기화됨.");
				}
			} else if(e.target.getAttribute('name')=='postcode') {
				if(document.getElementsByName('postcode').length>1){
					console.log("postcodeArr 초기화 되지 않음.");
				} else {
					postcodeArr.length = 0;
					console.log("postcodeArr 초기화됨.");
				}
			} else if(e.target.getAttribute('name')=='addr1') {
				if(document.getElementsByName('addr1').length>1){
					console.log("addr1Arr 초기화 되지 않음.");
				} else {
					addr1Arr.length = 0;
					console.log("addr1Arr 초기화됨.");
				}
			} else if(e.target.getAttribute('name')=='addr2') {
				if(document.getElementsByName('addr2').length>1){
					console.log("addr2Arr 초기화 되지 않음.");
				} else {
					addr2Arr.length = 0;
					console.log("addr2Arr 초기화됨.");
				}
			} else if(e.target.getAttribute('name')=='remarks') {
				if(document.getElementsByName('remarks').length>1){
					console.log("remarksArr 초기화 되지 않음.");
				} else {
					remarksArr.length = 0;
					console.log("remarksArr 초기화됨.");
				}
			} else if(e.target.getAttribute('name')=='regDate') {
				if(document.getElementsByName('regDate').length>1){
					console.log("regDateArr 초기화 되지 않음.");
				} else {
					regDateArr.length = 0;
					console.log("regDateArr 초기화됨.");
				}
			} else if(e.target.getAttribute('name')=='barcodeMbr') {
				if(document.getElementsByName('barcodeMbr').length>1){
					console.log("barcodeMbrArr 초기화 되지 않음.");
				} else {
					barcodeMbrArr.length = 0;
					console.log("barcodeMbrArr 초기화됨.");
				}
			} else if(e.target.getAttribute('name')=='profileImg') {
				if(document.getElementsByName('profileImg').length>1){
					console.log("profileImgArr 초기화 되지 않음.");
				} else {
					profileImgArr.length = 0;
					console.log("profileImgArr 초기화됨.");
				}
			} else if(e.target.getAttribute('name')=='rfUid') {
				if(document.getElementsByName('rfUid').length>1){
					console.log("rfUidArr 초기화 되지 않음.");
				} else {
					rfUidArr.length = 0;
					console.log("rfUidArr 초기화됨.");
				}
			} else if(e.target.getAttribute('name')=='gradeName') {
				if(document.getElementsByName('gradeName').length>1){
					console.log("gradeNameArr 초기화 되지 않음.");
				} else {
					gradeNameArr.length = 0;
					console.log("gradeNameArr 초기화됨.");
				}
			} else if(e.target.getAttribute('name')=='status') {
				if(document.getElementsByName('status').length>1){
					console.log("statusArr 초기화 되지 않음.");
				} else {
					statusArr.length = 0;
					console.log("statusArr 초기화됨."+ statusArr.length);
				}
			}
			//name attribute 공백값으로 초기화.
			e.target.setAttribute('name', '');
		}
		
	}
});

function clickedImportMemberOk() {
	//기존 동작 기능 막기.
	event.preventDefault();
	
	var a = document.getElementsByName('name');
	console.log(a.length);
	
	$.ajax({
		url: "/member/import_member_excel_ok.do",
		type:'POST',
		traditional: true, //배열 및 리스트 값을 넘기기 위해서 선언되어야함.
		data: {
			nameArr,
			phoneArr,
			birthdateArr,
			emailArr,
			postcodeArr,
			addr1Arr,
			addr2Arr,
			remarksArr,
			regDateArr,
			barcodeMbrArr,
			profileImgArr,
			rfUidArr,
			gradeNameArr,
			statusArr
		},
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				alert(data.msg);
				location.href = data.nextPage;
			}
		}
	});
}*/