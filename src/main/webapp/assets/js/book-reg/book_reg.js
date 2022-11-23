const vBookTitle = document.getElementById('bookTitle');
const vVolumeCode = document.getElementById('volumeCode');
//권차기호 알림 체크 박스
const chkBoxVolumeCodeAlarm = document.getElementById('chkBoxVolumeCodeAlarm');

//바로등록 체크박스
const chkBoxStraightReg = document.getElementById('chkBoxStraightReg');
//const chkBoxRegOkByName = document.getElementsByName('chkBoxRegOk');

//십진분류 체크박스
const chkBoxClassCodeChecker = document.getElementById('chk_box_class_code_checker');

//소리 체크박스
const chkBoxSoundEff = document.getElementById('chk_box_sound_eff');
//const chkBoxSoundEffByName = document.getElementsByName('chk_box_sound_eff');

//전집류 복본기호 체크하는 체크박스 안씀.
//const chkBoxColleBooksCopyCode = document.getElementById('chk_box_collection_books_copy_code');

//소리재생 관련 소리파일
const checkSoundSuccess = document.getElementById('checkSoundSuccess');
const checkSoundWarning = document.getElementById('checkSoundWarning');
const checkSoundError = document.getElementById('checkSoundError');
//url파라미터를 for문 돌려서 체크할 수가 없어서, 소리 임시저장매체
let soundType = null;


window.addEventListener('load', function() {
	var curUrl = window.location.search;
	// ?물음표 이후 파라미터 추출
	curUrl = curUrl.substring(1);
	//console.log('파라미터 '+curUrl+' 길이 '+curUrl.length);
	if(curUrl.length<16||curUrl.length==0) {
		console.log('파라미터가 없는 상태');
		//파라미터가 없는 것은 최초 접속 아무것도 하지 않는다.
		//체크되어 있어야하는 것들 체크
		chkBoxVolumeCodeAlarm.checked = true;
		chkBoxSoundEff.checked = true;
	} else {
		var params = curUrl.split('&');
		//스위치 값이 있으면 숫자 더하기.
		var switchRegOk = 0;
		var switchVolChk = 0;
		var switchSoundChk = 0;
		var switchColleBooks = 0;
		//아래 분류기호 체크는 값이 unchecked를 찾는 변수
		var switchClsCodeChk = 0;
		//파라미터가 여러개일 경우 각 키워드 찾기.
		for(var i=0; i<params.length; i++) {
			console.log(params[i]);
			
			//권차기호 알림 체크 유지하기
			if((params[i].indexOf('volumeCodeAlarm'))>-1) {
				var volKeys = params[i].split('=');
				if(volKeys[1]=='checked') {
					switchVolChk++;
				}
			}
			
			//바로등록 체크 유지하기
			if(params[i].indexOf('regChk')>-1){
				var regStraight = params[i].split('=');
				if(regStraight[1]=='checked') {
					switchRegOk++;
				}
			}
			
			//알림 상태 유지하기
			if(params[i].indexOf('SoundChk')>-1){
				var SoundEff = params[i].split('=');
				if(SoundEff[0]!=null) {
					//체크가 되어있으면, 체크 상태만 유지.
					switchSoundChk++;
				}
			}
			
			//소리재생 파라미터 가져와서 체크
			if(params[i].indexOf('soundEff')>-1) {
				var soundEff = params[i].split('=');
				soundType = soundEff[1];
			}
			
			//clsCodeChk unchecked 상태 유지하기
			if(params[i].indexOf('clsCodeChker')>-1){
				var clsCodeChkParams = params[i].split('=');
				if(clsCodeChkParams[1]=='unchecked') {
					switchClsCodeChk++;
				}
			}
			
			//별치기호 값 가져와 유지하기
			if(params[i].indexOf('addiCode')>-1){
				var addiCodeParams = params[i].split('=');
				if(addiCodeParams[1]!='') {
					let decodeUriParam = decodeURI(decodeURIComponent(addiCodeParams[1]))
					document.getElementById('additionalCode').value = decodeUriParam;
				}
			}
			
			//전집류 복본기호 url 처리 함수 어려워서 안되겠음.
			/*if(params[i].indexOf('colleBooksCopy')>-1){
				var colleBooksChk = params[i].split('=');
				if(colleBooksChk[1]=='checked') {
					switchColleBooks++;
				}
			}*/
			
		}
		//스위치 값이 존재하면 체크 값 유지
		if(switchRegOk>0){
			chkBoxStraightReg.checked = true;
		}
		if(switchVolChk>0){
			chkBoxVolumeCodeAlarm.checked = true;
		}
		if(switchSoundChk>0){
			chkBoxSoundEff.checked = true;
		}
		/*if(switchColleBooks) {
			chkBoxColleBooksCopyCode.checked = true;
		}*/
		//스위치 값이 존재하면 체크 '안된' 값 유지
		if(switchClsCodeChk>0) {
			chkBoxClassCodeChecker.checked = false;
		}
		
	}
	
	
	//알림소리 제외 체크 관련
	if(chkBoxSoundEff.checked) {
		//체크 되어있으면 알림 재생
		clickedSound(soundType);
	} else {
		//체크 안되어있으면 알리지 않음.
	}
	
	
})


//별치기호 직접입력 체크박스
let checkBoxAddiCode = document.getElementById('checkBoxAddiCode');
//인풋 또는 셀렉트가 담길 div
let divAddiSelectOrInput = document.querySelector('.div__addi__select__or__input');
//임시 노드에 셀렉트 목록 고대로 담기.
let tempDivAddi = divAddiSelectOrInput.cloneNode(true);
let tempSelectAddiCode = tempDivAddi.querySelector('select');

checkBoxAddiCode.addEventListener('change', ()=>{
		divAddiSelectOrInput.innerHTML = '';
		
	if(checkBoxAddiCode.checked) {
		//현재 셀렉트 상태를 그대로 temp에 담기
		let newInput = document.createElement('input');
		newInput.type = "text";
		newInput.setAttribute('name',"additionalCode");
		newInput.setAttribute('id',"additionalCode");
		newInput.classList.add('form-control','form-control-sm','input-clear');
		divAddiSelectOrInput.append(newInput);
	} else {
		divAddiSelectOrInput.append(tempSelectAddiCode);
	}
	
});


//서가 셀렉트, 직접입력 체크박스
let checkBoxBookShelf = document.querySelector('#checkBoxBookShelf');
//서가 인풋과 셀렉트가 임시로 담길 div
let divBookShelfSelectOrInput = document.querySelector('.div__bookshelf__select__or__input');
//임시 노드에 서가 셀렉트 목록 담기
let tempDivBookShelf = divBookShelfSelectOrInput.cloneNode(true);
let tempSelectBookShelf = tempDivBookShelf.querySelector('select'); 

checkBoxBookShelf.addEventListener('change', ()=>{
	divBookShelfSelectOrInput.innerHTML = '';
	
	if(checkBoxBookShelf.checked) {
		let newInputBookShelf = document.createElement('input');
		newInputBookShelf.type = "text";
		newInputBookShelf.setAttribute('name',"bookShelf");
		newInputBookShelf.setAttribute('id',"bookShelf");
		newInputBookShelf.classList.add('form-control','form-control-sm','input-clear');
		divBookShelfSelectOrInput.append(newInputBookShelf);
	} else {
		divBookShelfSelectOrInput.append(tempSelectBookShelf);
	}
})


//체크박스가 1개만 남으면서 이거 필요 없어짐
//바로등록 체크박스 2개 동기화
/*chkBoxRegOkByName[0].addEventListener('change', (e)=> {
	if(chkBoxRegOkByName[0].checked == false) {
		chkBoxRegOkByName[1].checked = false;
	} else {
		chkBoxRegOkByName[1].checked = true;
	}
})*/


//체크박스가 1개만 남으면서 이거 필요 없어짐
//권차기호 체크박스 2개 동기화
/*chkBoxVolumeCodeAlarm[0].addEventListener('change', (e)=> {
	if(chkBoxVolumeCodeAlarm[0].checked == false) {
		chkBoxVolumeCodeAlarm[1].checked = false;
	} else {
		chkBoxVolumeCodeAlarm[1].checked = true;
	}
})*/

//소리 체크박스 2개 동기화
//체크박스가 1개만 남으면서 이거 필요 없어짐
/*chkBoxSoundEffByName[0].addEventListener('change', (e)=> {
	if(chkBoxSoundEffByName[0].checked == false) {
		chkBoxSoundEffByName[1].checked = false;
	} else {
		chkBoxSoundEffByName[1].checked = true;
	}
})*/


//도서명에 숫자를 포함했는지 판별 함수
//여기서 사용안하고 book_search에서 사용
/*function checkTitleContainNum(chkValue, chkVolume) {
	if(chkValue!=null&&chkValue!=''){
		console.log('제목이 있습니다.');
		//체크하려는 값(도서명)이 null이거나 공백이 아닐 경우
		if(chkVolume==null||chkVolume==''){
			console.log('권차기호가 없습니다.');
			//권차기호의 값이 null이거나 공백이 아닐 경우
			//정규식
			var regExp = /[0-9]/g;
			if(regExp.test(chkValue)) {
				//경고 알림시 소리 재생
				clickedSound(3);
				alert('제목에 권차기호를 포함하고 있을 수 있습니다.');
			}
		}
	}
}*/

function clickedSound(soundCode) {
	//1 성공, 2 에러, 3 경고
	if(soundCode == '1') {
		checkSoundSuccess.play();
		setTimeout(function() {
			checkSoundSuccess.pause();
			checkSoundSuccess.currentTime = 0;
		}, 500);
	} else if(soundCode == '2') {
		checkSoundError.play();
	} else if(soundCode == null) {
		//null 이면 아무것도 재생하지 않는다.
	} else {
		checkSoundWarning.play();
	}
	/* console.log(checkSoundSuccess.duration);
	console.log(checkSoundWarning.duration);
	console.log(checkSoundError.duration); */
} 