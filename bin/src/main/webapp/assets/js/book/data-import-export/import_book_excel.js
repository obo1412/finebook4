
//form 가져오기
const formImportExcelCheck = document.getElementById('formImportExcelCheck');
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

//select가 하나이상 선택된다면, 그것을 담을 배열 생성.
let arrColH = [];



//버튼 눌렸을 경우.
function clickedBtnSubmit() {
	/*formImportExcelCheck.action = "/book/import_book_excel_ok.do";
	//서브밋
	formImportExcelCheck.submit();*/
	
	//현재 몇번째인지 번호
	let curRowNum = curRow.value;
	
	console.log("["+curRowNum+"] 등록처리");
	if(curRowNum==""||curRowNum==null||curRowNum==0) {
		curRowNum = 1;
	}
//	console.log('curRowNum 값: '+curRowNum);
//	console.log('row_crt 값: '+row_crt.value);
	
	//현재 페이지의 셀렉트값 가져와서 배열에 넣기.
	for(let i=0; i<lastCellCount.value; i++){
		//배열 순서대로 값 넣기.
		let tempVal = document.getElementById('colH'+i).value;
		arrColH[i] = tempVal;
		//console.log(i+'번째 값:'+arrColH[i]);
		
	}
	console.log('arrColH값: '+arrColH);
	
	importProcess = $.ajax({
		url: "/book/import_book_excel_ok.do",
		type: 'POST',
		timeout: 0, //timeout 기본은 30000으로 되어있음. 여기선 타임아웃없애기위해 명시.
		traditional:true, //arr 데이터방식 수신하기 위해 명시
		data: {
			fileId: fileId.value,
			row_crt: row_crt.value,
			lastCellCount: lastCellCount.value,
			loadFilePath: loadFilePath.value,
			curRow: curRowNum,
			arrColH
		},
		/* dataType: "json", */
		/*beforeSend: (e) => {
		alert('데이터 통신을 시작합니다.');
		},*/
		success: function(data) {
			if(data.rt != 'OK') {
				//중간 확인 row 인터벌 중단 함수 실행
				cancelProcess();
				
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
					location.href = '/book/import_book_excel.do?menuHolder=setting';
				}
			}
		}
	}); //ajax 문 종료
	
	//인터벌 함수 실행
	displayInterval();
}

//ajax 중단
function cancelProcess() {
	//console.log();
	
	//중간 확인 row 인터벌 중단
	clearInterval(dInterval);
	
	$.ajax({
		url: "/book/import_book_excel_pause.do",
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
		url: "/book/import_book_excel_display_current_progress.do",
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