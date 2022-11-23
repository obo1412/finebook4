<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true"%>

<head>

<style type="text/css">
.notyet {
	color: red;
}

	td {
		/* 달력 크기에도 영향을 미치므로, 주석처리 */
		/* width: 10px; */
		height: 10px;
		text-align: center;
		font-size: 15px;
		font-family: 나눔고딕;
		border: 1px border-color:black;
		border-radius: 4px; /*모서리 둥글게*/
	}
	
	td.arrow:hover {
		background-color: orange;
	}
	
	.pickDay:hover {
		background-color: grey;
		cursor: pointer;
	}
	
	.dDay {
		background-color: orange;
	}
	
	table {
		background-color: white;
	}
</style>

</head>

<!-- Sidebar -->
<c:choose>
	<c:when test="${loginInfo.langMng eq 0}">
		<%@ include file="/WEB-INF/inc/sidebar_menu_kor.jsp"%>
	</c:when>
	<c:otherwise>
		<%@ include file="/WEB-INF/inc/sidebar_menu_eng.jsp"%>
	</c:otherwise>
</c:choose>

<script type="text/javascript">
	
	/* 사이드바 더블클릭시, 열려있는 메뉴 닫히게 */
	window.addEventListener('dblclick', (e)=> {
		if(e.target.closest('.nav-item')) {
			console.log(e.target);
			e.target.closest('.nav-item').querySelector('.dropdown-menu').classList.toggle('show');
		}
	});
	
	window.addEventListener('resize', (e) => {
		//파라미터 판별하여서 메뉴홀더 가져오기
		var urlParams = location.search;
		urlParams = urlParams.substring(1);
		
		var params = urlParams.split('&');
		var menuHolder = null;
		for(var i=0; i<params.length; i++) {
			if((params[i].indexOf('menuHolder'))>-1) {
				var key = params[i].split('=');
				menuHolder = key[1];
			}
		}
		
		if(window.innerWidth > 800) {
			if(urlParams.length==0){
				
			} else {
				
				if(menuHolder == 'bnr'){
					document.querySelector('.menuBnRTop').classList.add('show');
				} else if(menuHolder == 'book') {
					document.querySelector('.menuBookTop').classList.add('show');
				} else if(menuHolder == 'member') {
					document.querySelector('.menuMbrTop').classList.add('show');
				} else if(menuHolder == 'setting') {
					document.querySelector('.menuSetTop').classList.add('show');
				}
			}
		} else {
			if(urlParams.length==0){
				
			} else {
				
				if(menuHolder == 'bnr'){
					document.querySelector('.menuBnRTop').classList.remove('show');
				} else if(menuHolder == 'book') {
					document.querySelector('.menuBookTop').classList.remove('show');
				} else if(menuHolder == 'member') {
					document.querySelector('.menuMbrTop').classList.remove('show');
				} else if(menuHolder == 'setting') {
					document.querySelector('.menuSetTop').classList.remove('show');
				}
			}
			
		}
	})

	window.addEventListener('load', function() {
		
		//파라미터 판별하여서 메뉴홀더 가져오기
		var urlParams = location.search;
		urlParams = urlParams.substring(1);
		
		if(window.innerWidth > 800) {
			if(urlParams.length==0){
				
			} else {
				var params = urlParams.split('&');
				var menuHolder = null;
				for(var i=0; i<params.length; i++) {
					if((params[i].indexOf('menuHolder'))>-1) {
						var key = params[i].split('=');
						menuHolder = key[1];
					}
				}
				
				if(menuHolder == 'bnr'){
					document.querySelector('.menuBnRTop').classList.toggle('show');
				} else if(menuHolder == 'book') {
					document.querySelector('.menuBookTop').classList.toggle('show');
				} else if(menuHolder == 'member') {
					document.querySelector('.menuMbrTop').classList.toggle('show');
				} else if(menuHolder == 'setting') {
					document.querySelector('.menuSetTop').classList.toggle('show');
				}
			}
		} else {
			if(urlParams.length==0){
				
			} else {
				var params = urlParams.split('&');
				var menuHolder = null;
				for(var i=0; i<params.length; i++) {
					if((params[i].indexOf('menuHolder'))>-1) {
						var key = params[i].split('=');
						menuHolder = key[1];
					}
				}
				
				if(menuHolder == 'bnr'){
					document.querySelector('.menuBnRTop').classList.remove('show');
				} else if(menuHolder == 'book') {
					document.querySelector('.menuBookTop').classList.remove('show');
				} else if(menuHolder == 'member') {
					document.querySelector('.menuMbrTop').classList.remove('show');
				} else if(menuHolder == 'setting') {
					document.querySelector('.menuSetTop').classList.remove('show');
				}
			}
			
		}
	})

	//클릭 감지를 위한 전체 클래스
	const sidebarCls = document.querySelector('.sidebar');
	//클릭 이벤트
	sidebarCls.addEventListener('click', (e)=> {
		//클릭 타겟이 아래 클래스를 포함하면, url에 파라미터 추가
		if(e.target.classList.contains('menuBnR')){
			e.target.href += '?menuHolder=bnr';
		} else if(e.target.classList.contains('menuBook')) {
			e.target.href += '?menuHolder=book';
		} else if(e.target.classList.contains('menuMbr')) {
			e.target.href += '?menuHolder=member';
		} else if(e.target.classList.contains('menuSet')) {
			e.target.href += '?menuHolder=setting';
		}
		
	});
	
	//도서 정보 엑셀 내보내기
	/* const btnExportExcel = document.getElementById('btn_export_excel');
	btnExportExcel.addEventListener("click", exportExcelOk);
	
	function exportExcelOk() {
		$.ajax({
			url: "${pageContext.request.contextPath}/export_bookheld_excel_ok.do",
			type: 'POST',
			dataType: "text",
			data: {},
			success: function(data) {
				//location.href=document.URL;
				alert("도서등록정보를 엑셀 파일로 저장하였습니다.");
			}
		});
	}; */
</script>

<!-- 달력 js -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/lib_calendar/lib_calendar.js"></script>
