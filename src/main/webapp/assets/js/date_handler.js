	//날짜 포멧 변경(yyyy-MM-dd)을 위한 함수
	function dateFormChange(d) {
		var result = null;
		if(d != null && d != '') {
			var date = new Date(d);
			var year = date.getFullYear();
			var month = date.getMonth()+1;
			month = month > 9 ? month : '0'+month;
			var day = date.getDate();
			day = day > 9 ? day : '0'+day;
			result = year+'-'+month+'-'+day;
		}
		return result;
	};