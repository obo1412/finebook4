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
		width: 10px;
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
		<ul class="sidebar navbar-nav toggled">
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#" id="bookDropdown"
				role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					<i class="fas fa-fw fa-book"></i>
					<span>도서</span>
				</a>
				<div class="dropdown-menu" aria-labelledby="bookDropdown">
					<a class="dropdown-item"
						href="${pageContext.request.contextPath}/book/brw_book_member.do">도서
						대출/반납</a>
					<a class="dropdown-item"
						href="${pageContext.request.contextPath}/book/book_held_list_member.do">도서
						목록
					</a>
				</div>
			</li>
		</ul>

