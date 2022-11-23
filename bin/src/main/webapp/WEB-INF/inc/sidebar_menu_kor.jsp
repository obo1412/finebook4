<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true"%>

		<ul class="sidebar navbar-nav">
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#" id="brwRtnDropdown"
				role="button" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="false">
					<i class="fas fa-fw fa-book-reader"></i>
					<span>도서 대출/반납</span>
				</a>
				
				<div class="dropdown-menu menuBnRTop" aria-labelledby="brwRtnDropdown">
					<a class="dropdown-item menuBnR"
						href="${pageContext.request.contextPath}/book/brw_book.do">도서
						대출/반납</a>
					<a class="dropdown-item menuBnR"
						href="${pageContext.request.contextPath}/book/book_held_list_brwd.do">대출된
						도서 목록</a>
					<a class="dropdown-item menuBnR"
						href="${pageContext.request.contextPath}/stsc/statistics.do">통계</a>
				</div>
			</li>
			
			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle" href="#" id="bookDropdown"
				role="button" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="false"> <i class="fas fa-fw fa-book"></i> <span>도서
						관리</span>
			</a>
				<div class="dropdown-menu menuBookTop" aria-labelledby="bookDropdown">
					<a class="dropdown-item menuBook"
						href="${pageContext.request.contextPath}/book/book_held_list.do?chkBox_tag_search=checked">도서
						목록</a> <a class="dropdown-item menuBook"
						href="${pageContext.request.contextPath}/book/reg_book.do">도서
						등록하기</a> <a class="dropdown-item menuBook"
						href="${pageContext.request.contextPath}/book/reg_book_batch.do">도서
						일괄 등록하기</a>

					<div class="dropdown-divider"></div>
					
						<a class="dropdown-item menuBook"
						href="${pageContext.request.contextPath}/book/book_held_discard_list.do">폐기도서
						목록</a>
						
						<a class="dropdown-item menuBook"
						href="${pageContext.request.contextPath}/book/update_addi_code_batch.do">
						도서정보 일괄수정 </a>
						
					<div class="dropdown-divider"></div>
					
						<a class="dropdown-item menuBook"
							href="${pageContext.request.contextPath}/book_check/book_check_status.do">
							장서점검
						</a>
						
					<div class="dropdown-divider"></div>
					
					<a class="dropdown-item menuBook"
						href="${pageContext.request.contextPath}/book/print_tag_setup.do">라벨
						출력</a>
					<%-- <a class="dropdown-item menuBook"
						href="${pageContext.request.contextPath}/book/print_label_text_batch.do">
						라벨출력(textFile)</a> --%>
				</div>
			</li>
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#" id="memberDropdown"
					role="button" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="false"> <i class="fas fa-fw fa-address-book"></i>
						<span>회원 관리</span>
				</a>
				<div class="dropdown-menu menuMbrTop" aria-labelledby="memberDropdown">
					<a class="dropdown-item menuMbr"
						href="${pageContext.request.contextPath}/member/member_list.do">회원
						목록</a>
					<a class="dropdown-item menuMbr"
						href="${pageContext.request.contextPath}/member/join.do">회원
						등록하기</a>
					<a class="dropdown-item menuMbr"
						href="${pageContext.request.contextPath}/member/grade_list.do">회원등급
						관리</a>
					<a class="dropdown-item menuMbr"
						href="${pageContext.request.contextPath}/member/class_member_list.do">회원분류
						관리</a>
				</div>
			</li>

			<li class="nav-item dropdown">
				<a
					class="nav-link dropdown-toggle" href="#" id="settingDropdown"
					role="button" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="false"> <i class="fas fa-fw fa-cogs"></i> <span>환경
							설정</span>
				</a>
				<div class="dropdown-menu menuSetTop" aria-labelledby="settingDropdown">
					<a class="dropdown-item disabled"
						href="${pageContext.request.contextPath}/book/db_transfer.do">
						데이터베이스 이관 </a>
						
					<a class="dropdown-item menuSet"
						href="${pageContext.request.contextPath}/book/import_book_excel.do">
						도서정보가져오기(Excel)</a>
						
					<a class="dropdown-item menuSet"
						href="${pageContext.request.contextPath}/member/import_member_excel.do">
						회원정보가져오기(Excel)</a>
						
				<div class="dropdown-divider"></div>
					
					<a class="dropdown-item menuSet"
						href="${pageContext.request.contextPath}/book/country_list.do">
						국가 관리 </a>
					
					<a class="dropdown-item menuSet"
						href="${pageContext.request.contextPath}/setting/language.do">
						언어/Language </a>
						
					<a class="dropdown-item menuSet"
						href="${pageContext.request.contextPath}/setting/library_setting.do">
						도서관설정관리 </a>
						
				<!-- <div class="dropdown-divider"></div> -->
					
						<%-- <a class="dropdown-item menuSet"
						href="${pageContext.request.contextPath}/test_lab/ctrl_to_ctrl.do">
						테스트랩</a> --%>
				</div>
			</li>
				
			<!-- 도서관별로 나타내고 없애고 기능 마지막 /c:if는 추가해줘야됨. -->
			<%-- <c:if test="${loginInfo.idLibMng ne 1}"> --%>
			<li style="margin-top:5px; " class="nav-item dropdown">
				<a
					class="nav-link" href="#" id="calendarDropdown"
					role="button"> <i class="fas fa-calendar-alt"></i> <span>달력</span>
				</a>
				<div id="calendarDiv">
					<table id="calendar" border="1" align="center" style="width:220px; border-radius: 4%;">
						<tbody class="cal-body">
							<tr><!-- label은 마우스로 클릭을 편하게 해줌 -->
								<td class="arrow" onclick="prevCalendar()">
									<label><</label>
								</td>
								<td align="center" id="tbCalendarYM" colspan="5">
									yyyy년 m월
								</td>
								<td class="arrow"  onclick="nextCalendar()">
									<label>></label>
								</td>
							</tr>
							<tr>
								<td align="center"><font color ="red">일</font></td>
								<td align="center">월</td>
								<td align="center">화</td>
								<td align="center">수</td>
								<td align="center">목</td>
								<td align="center">금</td>
								<td align="center"><font color ="#0B58DE">토</font></td>
							</tr> 
						</tbody>
					</table>
				</div>
			</li>
		</ul>
