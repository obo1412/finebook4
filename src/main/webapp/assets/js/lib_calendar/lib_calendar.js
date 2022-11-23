


	var today = new Date();//오늘 날짜//내 컴퓨터 로컬을 기준으로 today에 Date 객체를 넣어줌
	var date = new Date();//today의 Date를 세어주는 역할
	
	function prevCalendar() {//이전 달
		// 이전 달을 today에 값을 저장하고 달력에 today를 넣어줌
		//today.getFullYear() 현재 년도//today.getMonth() 월  //today.getDate() 일 
		//getMonth()는 현재 달을 받아 오므로 이전달을 출력하려면 -1을 해줘야함
		today = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
		buildCalendar(); //달력 cell 만들어 출력 
	}
 
	function nextCalendar() {//다음 달
		// 다음 달을 today에 값을 저장하고 달력에 today 넣어줌
		//today.getFullYear() 현재 년도//today.getMonth() 월  //today.getDate() 일 
		//getMonth()는 현재 달을 받아 오므로 다음달을 출력하려면 +1을 해줘야함
		today = new Date(today.getFullYear(), today.getMonth() + 1, today.getDate());
		buildCalendar();//달력 cell 만들어 출력
	}
	
	function buildCalendar() {//현재 달 달력 만들기
		var doMonth = new Date(today.getFullYear(),today.getMonth(),1);
		//이번 달의 첫째 날,
		//new를 쓰는 이유 : new를 쓰면 이번달의 로컬 월을 정확하게 받아온다.     
		//new를 쓰지 않았을때 이번달을 받아오려면 +1을 해줘야한다. 
		//왜냐면 getMonth()는 0~11을 반환하기 때문
		var lastDate = new Date(today.getFullYear(),today.getMonth()+1,0);
		//이번 달의 마지막 날
		//new를 써주면 정확한 월을 가져옴, getMonth()+1을 해주면 다음달로 넘어가는데
		//day를 1부터 시작하는게 아니라 0부터 시작하기 때문에 
		//대로 된 다음달 시작일(1일)은 못가져오고 1 전인 0, 즉 전달 마지막일 을 가져오게 된다
		var tbCalendar = document.getElementById("calendar");
		//날짜를 찍을 테이블 변수 만듬, 일 까지 다 찍힘
		var tbCalendarYM = document.getElementById("tbCalendarYM");
		//테이블에 정확한 날짜 찍는 변수
		//innerHTML : js 언어를 HTML의 권장 표준 언어로 바꾼다
		//new를 찍지 않아서 month는 +1을 더해줘야 한다. 
		tbCalendarYM.innerHTML = "<span class='pickYear'>"+today.getFullYear()+"</span>" + "년 " + "<span class='pickMonth'>"+(today.getMonth()+ + 1)+"</span>" + "월";
		
		/*while은 이번달이 끝나면 다음달로 넘겨주는 역할*/
		while (tbCalendar.rows.length > 2) {
			//열을 지워줌
			//기본 열 크기는 body 부분에서 2로 고정되어 있다.
			tbCalendar.deleteRow(tbCalendar.rows.length-1);
			//테이블의 tr 갯수 만큼의 열 묶음은 -1칸 해줘야지 
			//30일 이후로 담을달에 순서대로 열이 계속 이어진다.
		}
		var row = null;
		row = tbCalendar.insertRow();
		//테이블에 새로운 열 삽입//즉, 초기화
		var cnt = 0;// count, 셀의 갯수를 세어주는 역할
		// 1일이 시작되는 칸을 맞추어 줌
		for (i=0; i<doMonth.getDay(); i++) {
			//getDay는 요일을 숫자로 반환함.
			/*이번달의 day만큼 돌림*/
			cell = row.insertCell();//열 한칸한칸 계속 만들어주는 역할
			cnt = cnt + 1;//열의 갯수를 계속 다음으로 위치하게 해주는 역할
		}
		
		/*달력 출력*/
		for (i=1; i<=lastDate.getDate(); i++) { 
			//1일부터 마지막 일까지 돌림
			//tr row에 class trDays클래스 추가하여, css 제어
			
			cell = row.insertCell();//열 한칸한칸 계속 만들어주는 역할
			cell.innerHTML = i;//셀을 1부터 마지막 day까지 HTML 문법에 넣어줌
			cell.classList.add('pickDay');
			/*cell.id = "day"+i;*/
			cnt = cnt + 1;//열의 갯수를 계속 다음으로 위치하게 해주는 역할
			if (cnt % 7 == 1) {/*일요일 계산*/
				//1주일이 7일 이므로 일요일 구하기
				//월화수목금토일을 7로 나눴을때 나머지가 1이면 cnt가 1번째에 위치함을 의미한다
				cell.innerHTML = i
				//1번째의 cell에만 색칠
				cell.classList.add('pickDay');
				cell.style.color = 'red';
			}
			
			if (cnt%7 == 0){/* 1주일이 7일 이므로 토요일 구하기*/
				//월화수목금토일을 7로 나눴을때 나머지가 0이면 cnt가 7번째에 위치함을 의미한다
				cell.innerHTML = i;
				//7번째의 cell에만 색칠
				row = calendar.insertRow();
				//토요일 다음에 올 셀을 추가
				cell.classList.add('pickDay');
				cell.style.color = '#0B58DE';
			}
			
			/*오늘의 날짜에 노란색 칠하기*/
			if (today.getFullYear() == date.getFullYear()
					&& today.getMonth() == date.getMonth()
					&& i == date.getDate()) {
				//달력에 있는 년,달과 내 컴퓨터의 로컬 년,달이 같고, 일이 오늘의 일과 같으면
				cell.bgColor = "#FAF58C";//셀의 배경색을 노랑으로
				cell.style.borderRadius = "0%";
			}
		}//이번달 달력 일수 출력 끝
		
		//해당 월의 일수가 끝난 후 빈칸 채워주기
		if(cnt%7 >0) {
			for(var i=0; i<(7-(cnt%7)); i++) {
				cell = row.insertCell();
				//나머지 개수만큼 셀 생성
			}
		}
		
	}//달력 펑션 끝
	
	//달력 클릭시, 활성화 날짜에 배경색상 표시
	const curPickDay = {
		activeDTag: null
	}



//이하 달력 생성 후 달력 클릭시 이벤트 처리
buildCalendar();//달력 생성
	
	
	//창 크기 작아지면 달력 숨기기
	//달력을 품은 div
	let calendarDiv = document.getElementById('calendarDiv');
	//달력 아이콘 버튼
	let calendarDropdown = document.getElementById('calendarDropdown');
	
	//아래 load 함수처럼 처리하면, 밀접하게 실시간으로 크기 체크 못하는듯.
	window.addEventListener('resize', (e)=> {
		if(window.innerWidth > 800) {
			//800보다 클때 달력 생성
			calendarDiv.classList.remove('d-none');
			calendarDropdown.classList.add('d-none');
		} else {
			//800보다 작을때 달력 숨기기
			calendarDiv.classList.add('d-none');
			calendarDropdown.classList.remove('d-none');
		}
	});
	
	window.addEventListener('load', visibleCalendarControl());
	
	function visibleCalendarControl() {
		if(window.innerWidth > 800) {
			//800보다 클때 달력 생성
			calendarDiv.classList.remove('d-none');
			calendarDropdown.classList.add('d-none');
		} else {
			//800보다 작을때 달력 숨기기
			calendarDiv.classList.add('d-none');
			calendarDropdown.classList.remove('d-none');
		}
	}
	
	calendarDropdown.addEventListener('click', (e)=> {
		//a 동작 막기
		e.preventDefault();
		/*let newCalendarSpace = document.createElement("div");
		newCalendarSpace.style.zIndex = 99;
		newCalendarSpace.style.width = "150px";
		newCalendarSpace.style.height = "200px";
		let xPos = e.clientX;
		let yPos = e.clientY;
		
		console.log(xPos);
		console.log(yPos);*/
		
		if(calendarDiv.classList.contains('d-none')) {
			//보이게 하기.
			calendarDiv.classList.remove('d-none');
			calendarDiv.style.position = 'absolute';
			calendarDiv.style.zIndex = 99;
		} else {
			//안보이게 하기.
			calendarDiv.classList.add('d-none');
			calendarDiv.style.zIndex = 0;
		}
		
	})
	
	

const calBody = document.querySelector('.cal-body');
let dDay = document.querySelector('.dDay');

calBody.addEventListener('click', (e) => {

	if(e.target.classList.contains('pickDay')) {

		const pickYear = document.querySelector('.pickYear').textContent;
		const pickMonth = document.querySelector('.pickMonth').textContent;
		if(curPickDay.activeDTag) {
			curPickDay.activeDTag.classList.remove('dDay');
		}
		e.target.classList.add('dDay');
		curPickDay.activeDTag = e.target;
			
		var pickDay = pickYear+'-'+pickMonth+'-'+e.target.textContent;
		var pickDate = new Date(pickDay);
		console.log(pickDay);
		console.log(pickDate);
		
		var bannerPickDay = document.getElementById('banner-pick-day-id');
		if(bannerPickDay!=null){
			bannerPickDay.innerText = dateFormChange(pickDay);
		}
    
		//대출 반납 페이지에 대출 반납 현황 호출
		if(document.querySelector('.brwListDdayClass')!=null){
			var pickDateForm = dateFormChange(pickDate);
			console.log(pickDateForm);
			selectBrwListDday(pickDateForm);
		}
	}
});

