<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true"%>


		<ul class="sidebar navbar-nav">
			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle" href="#" id="brwRtnDropdown"
				role="button" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="false"> <i class="fas fa-fw fa-book-reader"></i>
					<span>Borrow/Return</span>
			</a>
				<div class="dropdown-menu" aria-labelledby="brwRtnDropdown">
					<a class="dropdown-item"
						href="${pageContext.request.contextPath}/book/brw_book.do">도서
						대출/반납</a> <a class="dropdown-item"
						href="${pageContext.request.contextPath}/book/book_held_list_brwd.do">대출된
						도서 목록</a>
				</div></li>
			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle" href="#" id="bookDropdown"
				role="button" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="false"> <i class="fas fa-fw fa-book"></i> <span>Book</span>
			</a>
				<div class="dropdown-menu" aria-labelledby="bookDropdown">
					<a class="dropdown-item"
						href="${pageContext.request.contextPath}/book/book_held_list.do">도서
						목록</a> <a class="dropdown-item"
						href="${pageContext.request.contextPath}/book/reg_book.do">도서
						등록하기</a> <a class="dropdown-item"
						href="${pageContext.request.contextPath}/book/reg_book_batch.do">도서
						일괄 등록하기</a>

					<div class="dropdown-divider"></div>
					<a class="dropdown-item"
						href="${pageContext.request.contextPath}/book/book_held_discard_list.do">폐기도서
						목록</a> <a class="dropdown-item"
						href="${pageContext.request.contextPath}/book/print_tag_setup.do">라벨
						출력</a>
				</div></li>
			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle" href="#" id="memberDropdown"
				role="button" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="false"> <i class="fas fa-fw fa-address-book"></i>
					<span>Member</span>
			</a>
				<div class="dropdown-menu" aria-labelledby="memberDropdown">
					<a class="dropdown-item"
						href="${pageContext.request.contextPath}/member/member_list.do">회원
						목록</a> <a class="dropdown-item"
						href="${pageContext.request.contextPath}/member/join.do">회원
						등록하기</a> <a class="dropdown-item"
						href="${pageContext.request.contextPath}/member/grade_list.do">회원등급
						관리</a>
					<!-- <div class="dropdown-divider"></div>
		          <h6 class="dropdown-header">Other Pages:</h6>
		          <a class="dropdown-item" href="#">404 Page</a>
		          <a class="dropdown-item" href="#">Blank Page</a> -->
				</div></li>

			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle" href="#" id="settingDropdown"
				role="button" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="false"> <i class="fas fa-fw fa-cogs"></i> <span>Setting</span>
			</a>
				<div class="dropdown-menu" aria-labelledby="settingDropdown">
					<a class="dropdown-item disabled"
						href="${pageContext.request.contextPath}/book/db_transfer.do">
						데이터베이스 이관 </a> <a class="dropdown-item disabled" id="btn_export_excel">
						Export Excel </a> <a class="dropdown-item"
						href="${pageContext.request.contextPath}/book/import_book_excel.do">
						도서 정보 가져오기 </a> <a class="dropdown-item"
						href="${pageContext.request.contextPath}/book/country_list.do">
						국가 관리 </a>
					<div class="dropdown-divider"></div>
					<a class="dropdown-item"
						href="${pageContext.request.contextPath}/setting/language.do">
						언어/Language </a>
				</div></li>
				
			
				 <li style="margin-top:5px; ">
          <div style="">
            <table id="calendar" border="1" align="center" style="border-color:black; ">
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
                  <td align="center"><font color ="#F79DC2">일</font></td>
                  <td align="center">월</td>
                  <td align="center">화</td>
                  <td align="center">수</td>
                  <td align="center">목</td>
                  <td align="center">금</td>
                  <td align="center"><font color ="skyblue">토</font></td>
                </tr> 
              </tbody>
            </table>
          </div>
        </li>
       
		</ul>
