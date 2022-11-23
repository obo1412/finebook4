<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true"%>

<nav class="navbar navbar-expand navbar-dark bg-dark static-top">

	<a class="navbar-brand mr-3"
		href="${pageContext.request.contextPath}/index_login.do">FineBook v4</a>

	<button class="btn btn-link btn-sm text-white order-1 order-sm-1"
		id="sidebarToggle">
		<i class="fas fa-bars"></i>
	</button>

	<ul class="navbar-nav ml-3">
		<li class="nav-item mr-3">
			<a class="text-white"
				href="${pageContext.request.contextPath}/bbs/document_list.do?category=notice">
					<span class="d-inline-block mr-0"> <i
						class="fas fa-clipboard-list fa-fw"></i>
				</span> <span class="d-none d-md-inline-block"> 공지사항 </span>
			</a>
		</li>
		
		<li class="nav-item mr-3">
			<a class="text-white"
			href="${pageContext.request.contextPath}/bbs/document_list.do?category=qna">
				<span class="d-inline-block mr-auto">
					<i class="fas fa-question-circle fa-fw"></i>
				</span>
				<span class="d-none d-md-inline-block"> 질문/답변 </span>
			</a>
		</li>
		
		<li class="nav-item mr-3">
			<a class="text-white"
				href="#" data-toggle="modal" data-target="#enter_user_self_page_modal">
				<span class="d-inline-block mr-auto">
					<i class="fas fa-chalkboard-teacher fa-fw"></i>
				</span> 
				<span class="d-none d-md-inline-block">이용자화면</span>
			</a>
		</li>
		
		<c:if test="${loginInfo.kioskMode eq 1}">
			<li class="nav-item mr-3">
				<a class="text-white"
					href="${pageContext.request.contextPath}/kiosk_mode.do">
					<span class="d-inline-block mr-auto">
						<i class="fas fa-chalkboard-teacher fa-fw"></i>
					</span> 
					<span class="d-none d-md-inline-block">무인대출반납</span>
				</a>
			</li>
		</c:if>
		
		<c:if test="${loginInfo.idLibMng eq 4 or loginInfo.idLibMng eq 1}">
			<li class="nav-item mr-3">
				<a class="text-white"
				href="${pageContext.request.contextPath}/bbs/document_list.do?category=request">
					<span class="d-inline-block mr-auto">
						<i class="fas fa-shopping-basket fa-fw"></i>
					</span>
					<span class="d-none d-md-inline-block">신규도서등록</span>
				</a>
			</li>
		</c:if>
		<!-- 이용자화면 전환 모달 -->
		<%@ include file="/WEB-INF/views/user_self_page/modal/modal_user_self_enter.jsp" %>
		
		<!-- 총괄관리자만 볼수있는 메뉴 -->
		<c:if test="${loginInfo.idLibMng == 0}">
			<li class="nav-item mr-3">
				<a class="text-white"
				href="${pageContext.request.contextPath}/admin_setting/admin_lib_list.do">
					<span class="d-inline-block mr-auto">
						<i class="fas fa-tasks fa-fw"></i>
					</span>
					<span class="d-none d-md-inline-block"> 도서관 관리 </span>
				</a>
			</li>
			
			<li class="nav-item mr-3">
				<a class="text-white"
				href="${pageContext.request.contextPath}/bbs/document_list.do?category=development">
					<span class="d-inline-block mr-auto">
						<i class="fab fa-dev fa-fw"></i>
					</span>
					<span class="d-none d-md-inline-block">개발 게시판</span>
				</a>
			</li>
			
			<li class="nav-item mr-3">
				<a class="text-white"
				href="${pageContext.request.contextPath}/test_lab/send_mail_test.do">
					<span class="d-inline-block mr-auto">
						<i class="far fa-envelope"></i>
					</span>
					<span class="d-none d-md-inline-block">메일서버테스트</span>
				</a>
			</li>
		</c:if>
		
	</ul>


	<!-- Navbar Search -->
	<!-- <form class="d-none d-md-inline-block form-inline ml-auto mr-0 mr-md-3 my-2 my-md-0">
      <div class="input-group">
        <input type="text" class="form-control" placeholder="Search for..." aria-label="Search" aria-describedby="basic-addon2">
        <div class="input-group-append">
          <button class="btn btn-primary" type="button">
            <i class="fas fa-search"></i>
          </button>
        </div>
      </div>
    </form> -->

	<!-- Navbar -->
	<c:choose>
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
							action="${pageContext.request.contextPath}/managerFinebook/login_ok.do">
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
				<li class="nav-item dropdown no-arrow mx-1"><a
					class="nav-link dropdown-toggle" href="#" id="messagesDropdown"
					role="button" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="false"> <!-- <span class="badge badge-danger">7</span> -->
						<i class="fas fa-plus fa-fw"></i>
				</a>
					<div class="dropdown-menu dropdown-menu-right"
						aria-labelledby="messagesDropdown">
						<a class="dropdown-item"
							href="${pageContext.request.contextPath}/manager/join_mng.do">관리자
							회원가입</a>
						<!-- <a class="dropdown-item" href="#">Another action</a>
		          <div class="dropdown-divider"></div>
		          <a class="dropdown-item" href="#">Something else here</a> -->
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
				<li class="nav-item dropdown no-arrow mx-1">
					<a class="nav-link dropdown-toggle" href="#" id="alertsDropdown" role="button" data-toggle="dropdown" aria-haspopup="true"
							aria-expanded="false">
						<!-- 쿠키값에 따른 프로필 이미지 표시 -->
						<c:if test="${cookie.profileThumbnail!=null}">
							<img src="${pageContext.request.contextPath}/download.do?file=${cookie.profileThumbnail.value}"
								class="img-circle" />
						</c:if><!-- 쿠키값에 따른 프로필 이미지 표시 끝 -->${loginInfo.nameLib}/${loginInfo.nameMng}님
						<span class="caret"></span>
								<!-- <i class="fas fa-user fa-fw"></i>
			          <span class="badge badge-danger">9+</span> -->
					</a>
					<div class="dropdown-menu dropdown-menu-right" aria-labelledby="alertsDropdown">
						<a class="dropdown-item" href="${pageContext.request.contextPath}/managerFinebook/logout_mng.do">
							로그아웃
						</a> 
						<a class="dropdown-item" href="${pageContext.request.contextPath}/managerFinebook/edit.do">
							회원정보 수정
						</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/managerFinebook/out.do">
							회원탈퇴
						</a>
					</div>
				</li>
				
				<!-- 사용안하는 버튼 숨기기 -->
				
				<!-- <li class="nav-item dropdown no-arrow mx-1"><a
					class="nav-link dropdown-toggle" href="#" id="messagesDropdown"
					role="button" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="false"> <span class="badge badge-danger">7+</span>
						<i class="fas fa-bell fa-fw"></i>
				</a>
					<div class="dropdown-menu dropdown-menu-right"
						aria-labelledby="messagesDropdown">
						<a class="dropdown-item" href="#">Action 추가예정</a> <a
							class="dropdown-item" href="#">Another action</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="#">Something else here</a>
					</div>
				</li> -->
				
				
				
				<!-- <li class="nav-item dropdown no-arrow"><a
					class="nav-link dropdown-toggle" href="#" id="userDropdown"
					role="button" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="false"> <i class="fas fa-user-circle fa-fw"></i>
						<i class="fas fa-user-cog fa-fw"></i>
				</a>
					<div class="dropdown-menu dropdown-menu-right"
						aria-labelledby="userDropdown">
						<a class="dropdown-item" href="#">Settings 추가예정</a> <a
							class="dropdown-item" href="#">Activity Log</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="#" data-toggle="modal"
							data-target="#logoutModal">Logout</a>
					</div>
				</li> -->
			</ul>
			<!-- 로그인 된 경우 -->
		</c:otherwise>
	</c:choose>
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