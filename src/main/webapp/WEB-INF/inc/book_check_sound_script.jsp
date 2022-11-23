<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true"%>

<audio id="checkSoundSuccess" src="/assets/sound_eff/check_success.mp3"></audio>
<audio id="checkSoundWarning" src="/assets/sound_eff/check_warning.mp3"></audio>
<audio id="checkSoundError" src="/assets/sound_eff/check_error.mp3"></audio>

<script type="text/javascript">
	const checkSoundSuccess = document.getElementById('checkSoundSuccess');
	const checkSoundWarning = document.getElementById('checkSoundWarning');
	const checkSoundError = document.getElementById('checkSoundError');
	
	//페이지 시작되면 location_chkBox_soundEff class 위치 찾아서
	//checkbox 생성
	const locationChkBoxSoundEff = document.querySelector('.location_chkBox_soundEff');
	
	window.addEventListener('load', function() {
		if(locationChkBoxSoundEff) {
			locationChkBoxSoundEff.innerHTML = "<input type='checkbox' id='chk_box_sound_eff'/>";
			locationChkBoxSoundEff.innerHTML += "<label for='chk_box_sound_eff'>소리효과</label>"
		}
	});
	
	function clickedSound(soundCode) {
		let chkBoxSoundEff = document.getElementById('chk_box_sound_eff');
		//1 성공, 2 에러, 3 경고
		if(soundCode == '확인') {
			if(chkBoxSoundEff.checked) {
				checkSoundSuccess.play();
				setTimeout(function() {
					checkSoundSuccess.pause();
					checkSoundSuccess.currentTime = 0;
				}, 500);
			}
		} else if(soundCode == '미등록도서') {
			checkSoundError.play();
		} else {
			checkSoundWarning.play();
		}
		/* console.log(checkSoundSuccess.duration);
		console.log(checkSoundWarning.duration);
		console.log(checkSoundError.duration); */
	}
</script>
