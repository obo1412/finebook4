<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true"%>

<nav class="navbar navbar-expand navbar-dark bg-dark static-top">
	<c:url var="homeUrl" value="/blook_around.do">
		<c:param name="lib" value="${idLib}" />
		<c:param name="skl" value="${stringKeyLib}" />
	</c:url>
	<a class="navbar-brand mr-3"
		href="${homeUrl}">FineBook v4</a>
		
	<div class="navbar pull-right text-white">${nameLib}</div>

	<!-- <button class="btn btn-link btn-sm text-white order-1 order-sm-1"
		id="sidebarToggle">
		<i class="fas fa-bars"></i>
	</button> -->





	<!-- Navbar -->
	<%-- <c:choose>
		<c:when test="${loginInfo==null}">
			<!-- 로그인 안된 경우 -->
			<ul class="navbar-nav ml-auto mr-0 mr-md-3 my-2 my-md-0">
				<li class="nav-item dropdown no-arrow mx-1"><a
					class="nav-link dropdown-toggle" href="#" id="alertsDropdown"
					role="button" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="false"> <i class="fas fa-user fa-fw"></i> <!-- <span class="badge badge-danger">9+</span> -->
				</a>
					<div class="dropdown-menu dropdown-menu-right"
						aria-labelledby="alertsDropdown">
						<form class="navbar-form navbar-right mr-3 ml-3" method="post"
							action="${pageContext.request.contextPath}/manager/login_ok.do">
							<div class="form-group">
								<input type="text" name="user_id" placeholder="User Id"
									class="form-control">
							</div>
							<div class="form-group">
								<input type="password" name="user_pw" placeholder="Password"
									class="form-control">
							</div>
							<div class="dropdown-divider"></div>
							<button type="submit" class="btn btn-success btn-block mr-2">
								Login</button>
						</form>
					</div></li>
				<li class="nav-item dropdown no-arrow"><a
					class="nav-link dropdown-toggle" href="#" id="userDropdown"
					role="button" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="false"> <!-- <i class="fas fa-user-circle fa-fw"></i> -->
						<i class="fas fa-search fa-fw"></i>
				</a>
					<div class="dropdown-menu dropdown-menu-right"
						aria-labelledby="userDropdown">
						<a class="dropdown-item" href="#">아이디 찾기 기능 추가 예정</a>
						<!-- <a class="dropdown-item" href="#">Activity Log</a>
		          <div class="dropdown-divider"></div>
		          <a class="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">Logout</a> -->
					</div></li>
			</ul>
			<!-- 로그인 안된 경우 -->
		</c:when>
		<c:otherwise>
			<!-- 로그인 된 경우 -->
			<ul class="navbar-nav ml-auto mr-0 mr-md-3 my-2 my-md-0">
				<li class="nav-item dropdown no-arrow mx-1"><a
					class="nav-link dropdown-toggle" href="#" id="alertsDropdown"
					role="button" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="false">
					<!-- 쿠키값에 따른 프로필 이미지 표시 --> <c:if
							test="${cookie.profileThumbnail!=null}">
							<img
								src="${pageContext.request.contextPath}/
            			download.do?file=${cookie.profileThumbnail.value}"
								class="img-circle" />
						</c:if> <!-- 쿠키값에 따른 프로필 이미지 표시 끝 -->
						${loginInfo.nameLib}/${loginInfo.name}님 <span
						class="caret"></span> <!-- <i class="fas fa-user fa-fw"></i>
		          <span class="badge badge-danger">9+</span> -->
				</a>
					<div class="dropdown-menu dropdown-menu-right"
						aria-labelledby="alertsDropdown">
						<a class="dropdown-item"
							href="${pageContext.request.contextPath}/member/logout_member.do">로그
							아웃</a> <a class="dropdown-item"
							href="${pageContext.request.contextPath}/member/edit.do">회원정보
							수정</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item"
							href="${pageContext.request.contextPath}/member/out.do">회원
							탈퇴</a>
					</div></li>
				
			</ul>
			<!-- 로그인 된 경우 -->
		</c:otherwise>
	</c:choose> --%>
</nav>





<!-- 메뉴바 -->
<%-- <div class="navbar navbar-inverse topbar">
	<div class="container">
		<!-- 로고 영역 -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span>	
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<!-- //반응형 메뉴 버튼 -->
			<!-- 로고 -->
			<a class="navbar-brand" href="${pageContext.request.contextPath}/index.do">My Library Manager</a>
		</div>
		<!-- //로고 영역 -->
		<!-- 메뉴 영역 -->
		<div class="navbar-collapse collapse">
			<!-- 사이트 메뉴 -->
			<ul class="nav navbar-nav">
				<li><a href="${pageContext.request.contextPath}/bbs/document_list.do?category=notice">공지사항</a></li>
				<li><a href="${pageContext.request.contextPath}/bbs/document_list.do?category=qna">질문/답변</a></li>
			</ul>
			<!-- //사이트 메뉴 -->
			<!-- 로그인 메뉴 -->
			<c:choose>
				<c:when test="${loginInfo==null}">
					<form class="navbar-form navbar-right" method="post" 
					action="${pageContext.request.contextPath}/manager/login_ok.do">
						<div class="form-group">
							<input type="text" name="user_id" placeholder="User Id" 
							class="form-control">			
						</div>
						<div class="form-group">
							<input type="password" name="user_pw" placeholder="Password" 
							class="form-control">			
						</div>
						<button type="submit" class="btn btn-success">
							<i class="glyphicon glyphicon-user"></i>
						</button>
						<a href="${pageContext.request.contextPath}/manager/join_mng.do" class="btn btn-warning">
						<i class="glyphicon glyphicon-plus"></i></a>
						<a href="${pageContext.request.contextPath}/manager/find_pw.do" class="btn btn-info">
						<i class="glyphicon glyphicon-search"></i></a>
					</form>
				</c:when>
				<c:otherwise>
					<!-- 로그인 된 경우 -->
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown"
							style="padding:5px;">
							<!-- 쿠키값에 따른 프로필 이미지 표시 -->
							<c:if test="${cookie.profileThumbnail!=null}">
								<img src="${pageContext.request.contextPath}/
								download.do?file=${cookie.profileThumbnail.value}" class="img-circle"/>
							</c:if>
							<!-- 쿠키값에 따른 프로필 이미지 표시 끝 -->
							${loginInfo.nameMng}님 <span class="caret"></span>
							</a>
							<!-- 로그인 한 경우 표시될 메뉴 -->
							<ul class="dropdown-menu">
								<li><a href="${pageContext.request.contextPath}/manager/logout_mng.do">로그아웃</a></li>
								<li><a href="${pageContext.request.contextPath}/manager/edit.do">회원정보 수정</a></li>
								<li><a href="${pageContext.request.contextPath}/manager/out.do">회원탈퇴</a></li>
							</ul>
						</li>
					</ul>
					<!-- //로그인 된 경우 -->
				</c:otherwise>
			</c:choose>
		</div>
		<!-- //메뉴 영역 -->
	</div>
</div> --%>
<!-- //메뉴바 -->