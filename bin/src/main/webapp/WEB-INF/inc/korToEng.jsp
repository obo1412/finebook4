<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true"%>

<script type="text/javascript">

function convertKorToEng(str) {
	var fromKor = ['ㅑ','ㅓ','ㄳ','뜨','드','ㄸ','ㄷ','ㅡ'];
	var toEng = ['I','J','RT','EM','EM','E','E','M'];
	
	for(var i=0; i<str.length; i++){
		for(var j=0; j<fromKor.length; j++) {
			if(str.indexOf(fromKor[j])>-1) {
				str = str.replace(fromKor[j],toEng[j]);
			}
		}
	}
	return str;
}

</script>
<!--// 공통 스크립트 영역 -->