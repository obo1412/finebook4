

	/*
	 *	print tag setup js 시작
	 *	 
	 */
	const tagTypeClass = document.querySelector('.tagTypeClass');
	let tagTypeByName = document.getElementsByName('tagType');
	//txt 일괄 라벨 인쇄를 위한 변수
	let tagTypeTxtBatch = document.getElementById('tagTypeTxtBatch');
	//라벨 선택시 선택한 라벨만 보이도록.
	const divPickedLabel = document.getElementById('divPickedLabel');
	const imgPickedLabel = document.getElementById('imgPickedLabel');
	const btnLabelCollapse = document.getElementById('btnLabelCollapse');
	
	
	window.addEventListener('load', function() {
		for(var i=0; i<tagTypeByName.length; i++){
			if(tagTypeByName[i].checked) {
				let pickedLabel = tagTypeByName[i].parentNode.parentNode.childNodes;
				let pickedImg = pickedLabel[1];
//				console.log(pickedLabel[1].getAttribute('src'));
				let pickedSrc = pickedLabel[1].getAttribute('src');
				imgPickedLabel.setAttribute('src', pickedSrc);
			}
		}
	})
	
	
	
	tagTypeClass.addEventListener('change', (e)=> {
		for(var i=0; i<tagTypeByName.length; i++){
			if(tagTypeByName[i].checked) {
				//안쓰는 박스 지우기.
				deleteAnotherBox();
				
				tagTypeSepA4(tagTypeByName[i].value, tagTypeByName[i].parentNode.innerText);
				//console.log(tagTypeByName[i]);
				
				//일괄 출력부분 태그 타입 변경
				tagTypeTxtBatch.value = tagTypeByName[i].value;
				
				//선택한 태그 이미지 보이기
				let pickedLabel = tagTypeByName[i].parentNode.parentNode.childNodes;
				let pickedImg = pickedLabel[1];
				let pickedSrc = pickedLabel[1].getAttribute('src');
				imgPickedLabel.setAttribute('src', pickedSrc);
				//상단 모습 바꾸고 다시 컬랩스 닫기.
				btnLabelCollapse.click();
			}
		}
	});
	
	//별도(separate) 타입인 경우, 화면에 이미지 전달이 아니라, 텍스트만 전달하기.
	function tagTypeSepA4(TypeSep, typeText) {
		if(TypeSep==3||TypeSep==4||TypeSep==5||TypeSep==6) {
			
			//안쓰는 박스 지우기.
			deleteAnotherBox();
			
			const divText = document.createElement('div');
			divText.classList.add('sep__type__div');
			divText.innerText = typeText;
			
			divPickedLabel.appendChild(divText);
		}
	}
	
	//이미지와 박스 통일하기 귀찮아서 이런식으로 붙여씀.
	//박스 지우기.
	function deleteAnotherBox() {
		if(divPickedLabel.querySelector('.sep__type__div') != null) {
			divPickedLabel.querySelector('.sep__type__div').remove();
		}
	}
	
	//클릭했을 경우 클릭된 라벨 보이기
//	tagTypeClass.addEventListener('click', (e)=> {
//		if(e.target.classList.contains('labelSet')) {
//			console.log(e.target);
//			divPickedLabel.innerHTML = '';
//			let curCheckedLabel = e.target.parentNode.cloneNode();
//			let divShowLabel = document.importNode(curCheckedLabel);
//			divPickedLabel.appendChild(divShowLabel);
			//appendChild 기능은 복사해서 붙여넣는 것이 아니라 이동시키는 것.
//		}
//	});
	
	function autoSheetSortingPopup() {
		var tagT = 0;
		tagT = $('input:radio[name=tagType]:checked').val();
		
		var printingEa = document.getElementById('printingEa').value;
		var printingSheetCount = document.getElementById('printingSheetCount').value;
		
		var rangeS = ((printingSheetCount - 1) * printingEa) + 1;
		var rangeE = printingEa * printingSheetCount ;
		
		var url = '/book/print_tag_page.do?tagType='+tagT;
		url = url + '&rangeStart='+rangeS +'&rangeEnd='+rangeE;
		window.open(url, '_blank', 'width=1080,height=800,scrollbars=yes');
		
		$.ajax({
			url: "/book/autoSheetCountUp.do",
			type: 'POST',
			data: {
				printingEa,
				printingSheetCount
			},
			/* dataType: "json", */
			success: function(data) {
				document.getElementById('printingSheetCount').value = data.result;
			}
			,error:function(request,status,error){
				alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	};
	
	//제목 titleSort 엔터
	function enterTitle() {
		if(event.keyCode==13){
			openPopupTitle();
		}
	}
	
	function openPopupTitle() {
		var tagT = 0;
		tagT = $('input:radio[name=tagType]:checked').val();
		var titleBook = document.getElementById('titleSorting').value;
		
		var url = '/book/print_tag_page.do?tagType='+tagT;
		url = url + '&titleSorting='+titleBook;
		window.open(url, '_blank', 'width=1080,height=800,scrollbars=yes');
	}
	
	//등록번호 targetsort 엔터
	function enterTarget() {
		if(event.keyCode==13){
			openPopupTarget();
		}
	}
	
	//등록번호 target 출력 
	function openPopupTarget() {
		var tagT = 0;
		tagT = $('input:radio[name=tagType]:checked').val();
		var targetBarcode = document.getElementById('targetSorting').value;
		//시작위치 변수 그때그때 객체선언으로 값가져와야한다.
		let startPoint = document.querySelector('#startPoint').value;
		
		var url = '/book/print_tag_page.do?tagType='+tagT;
		url = url + '&targetSorting='+targetBarcode+"&startPoint="+startPoint;
		window.open(url, '_blank', 'width=1080,height=800,scrollbars=yes');
	}
	
	function openPopup() {
		var tagT = 0;
		tagT = $('input:radio[name=tagType]:checked').val();
		
		var rangeS = $("#rangeStart").val();
		var rangeE = $("#rangeEnd").val();
		
		let startPoint = document.querySelector('#startPoint').value;
		
		var url = '/book/print_tag_page.do?tagType='+tagT;
		url = url + '&rangeStart='+rangeS +'&rangeEnd='+rangeE;
		url = url + "&&startPoint="+startPoint;
		window.open(url, '_blank', 'width=1080,height=800,scrollbars=yes');
	};
