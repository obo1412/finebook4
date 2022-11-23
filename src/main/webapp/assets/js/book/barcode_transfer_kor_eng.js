

window.addEventListener('load', function() {
	
})

//항상 쌍을 이루는 키워드, 앞으로 여기다가 기록하자.
let transKey = [
	"ㅡㅐ", "MO",
	"ㅓㅛ", "JY"
];

//한글을 영어로 바꿔주는 함수
transferKorToEng = (barcode) => {
	if(barcode == null || barcode == '') {
		alert('등록번호를 입력하세요');
		return;
	}
	for(let i=0; i<transKey.length; i++){
		if(barcode.indexOf(transKey[i])>-1) {
			barcode = barcode.replace(transKey[i], transKey[i+1]);
			break;
		}
	}
	return barcode;
}

