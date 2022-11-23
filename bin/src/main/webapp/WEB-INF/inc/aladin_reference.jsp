<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true"%>


<div style="float:right; /* color:#c0c0c0; */">
	<div>
		도서 DB제공 : 알라딘 인터넷서점(<a href="http://www.aladin.co.kr" target="_blank">www.aladin.co.kr</a>)
	</div>
	<div style="float:right;">
		알라딘에서 <a href="#" onclick="openAladinPopup()">구매하기</a>
	</div>
</div>

<script type="text/javascript">
let linkBase = "https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=";
let aladinItemId = "";
//aladinItemId는 book_search.js에서 값을 전달 받음.

function openAladinPopup() {
	event.preventDefault();
	
	var url = linkBase;
	if(aladinItemId != ""){
		url = linkBase + aladinItemId;
	}
	console.log(url);
	window.open(url, '_blank', 'width=800,height=600,scrollbars=yes');
};
</script>