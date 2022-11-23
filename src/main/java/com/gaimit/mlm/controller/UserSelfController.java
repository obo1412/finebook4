package com.gaimit.mlm.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaimit.helper.PageHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.model.Library;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.service.BbsDocumentService;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class UserSelfController {
	
	Logger logger = LoggerFactory.getLogger(UserSelfController.class);
	
	@Autowired
	SqlSession sqlSession;
	
	@Autowired
	WebHelper web;
	
	@Autowired
	PageHelper page;
	
	@Autowired
	Util util;
	
	@Autowired
	BbsDocumentService bbsDocumentService;
	
	@Autowired
	BrwService brwService;
	
	@Autowired
	BookHeldService bookHeldService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	FrequentlyFunction frequentlyFunction;
	
	@RequestMapping(value= {"/user_self_page/main_self.do"})
	public ModelAndView doRun(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		web.init();
		
		Manager loginInfoFrom = (Manager) web.getSession("loginInfo");
		logger.info("페이지 접속 웹세션 호출: "+web.getSession("loginInfo"));
		
		Member selfPageLogin = (Member) web.getSession("selfPageLogin");
		logger.info("페이지접속 웹세션 호출 selfPageLogin: "+web.getSession("selfPageLogin"));
		
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (web.getSession("loginInfo") == null && web.getSession("selfPageLogin") == null) {
			//로그인이 안되어 있다면
			return web.redirect(web.getRootPath() + "/index.do", "비정상적인 접근으로 로그인이 필요합니다.");
			
		}
		
		if(loginInfoFrom != null) {
			//변경할 세션 Member 형태 초기 수정시에
			selfPageLogin = new Member();
			selfPageLogin.setIdLib(loginInfoFrom.getIdLibMng());
			selfPageLogin.setNameLib(loginInfoFrom.getNameLib());
			
			//변수 주입 후 세션 생성
			web.setSession("selfPageLogin", selfPageLogin);
			
			//기존의 관리자 세션 삭제
			web.removeSession("loginInfo");
		}
		
		logger.info("웹세션 호출 loginInfo: "+web.getSession("loginInfo"));
		logger.info("웹세션 호출 selfPageLogin: "+web.getSession("selfPageLogin"));
		
		
		try {
			
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}

		/** (4) 최신 글 목록을 View에 전달 */
		model.addAttribute("idLib", selfPageLogin.getIdLib());
		model.addAttribute("nameLib", selfPageLogin.getNameLib());
		
		// "/WEB-INF/views/index.jsp"파일을 View로 사용한다.
		return new ModelAndView("/user_self_page/main_self");
	}
	
	
	
	@RequestMapping(value= {"/user_self_page/log_out_user.do"})
	public ModelAndView logOutUserSelf(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		web.init();
		
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (web.getSession("selfPageLogin") == null) {
			//로그인이 안되어 있다면
			return web.redirect(web.getRootPath() + "/index.do", "비정상적인 접근으로 로그인이 필요합니다.");
			
		}
		
		web.removeAllSession();
		
//		try {
//			
//		} catch (Exception e) {
//			return web.redirect(null, e.getLocalizedMessage());
//		}

		/** (4) 최신 글 목록을 View에 전달 */
//		model.addAttribute("idLib", selfPageLogin.getIdLib());

		
		// "/WEB-INF/views/index.jsp"파일을 View로 사용한다.
		return web.redirect(web.getRootPath() + "/index.do", "로그아웃 되었습니다.");
	}
	
	
	/** 도서목록 ajax 통신 
	 * @throws Exception */
	@ResponseBody
	@RequestMapping(value = "/user_self_page/search_book_list.do", method = RequestMethod.GET)
	public void searchBook(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		
		web.init();
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		//Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (web.getSession("selfPageLogin") == null) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
			return;
		}
		
		Member selfPageLogin = (Member) web.getSession("selfPageLogin");
		
		
		String keywordBook = web.getString("searchBookKeyword");
		//front에서 "" null처리 해주기.
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(selfPageLogin.getIdLib());
		bookHeld.setTitle(keywordBook);
		
		List<BookHeld> bookList = null;
		
		try {
			bookList = bookHeldService.selectBookListUserSelf(bookHeld);
		} catch(Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("bookList", bookList);
		
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
	}
	
	/** 회원 검색 ajax 통신
	 * @throws Exception */
	@ResponseBody
	@RequestMapping(value = "/user_self_page/search_member_list.do", method = RequestMethod.GET)
	public void searchMember(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		
		web.init();
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		//Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (web.getSession("selfPageLogin") == null) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
			return;
		}
		
		Member selfPageLogin = (Member) web.getSession("selfPageLogin");
		
		
		String keywordMember = web.getString("searchMemberKeyword");
		//front에서 "" null처리 해주기.
		
		Member member = new Member();
		member.setIdLib(selfPageLogin.getIdLib());
		member.setName(keywordMember);
		
		List<Member> memberList = null;
		
		try {
			memberList = memberService.selectMemberListUserSelf(member);
		} catch(Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("memberList", memberList);
		
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
	}
	
	
	/** 도서 등록번호, 회원등록번호,회원이름으로 도서 정보, 회원목록가져오기 ajax 통신
	 * @throws Exception */
	@ResponseBody
	@RequestMapping(value = "/user_self_page/search_brw_book_member.do", method = RequestMethod.GET)
	public void searchBookAndMember(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		
		web.init();
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		//Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (web.getSession("selfPageLogin") == null) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
			return;
		}
		
		Member selfPageLogin = (Member) web.getSession("selfPageLogin");
		
		
		String keyword = web.getString("searchKeyword");
		//front에서 "" null처리 해주기.
		
		Member member = new Member();
		member.setIdLib(selfPageLogin.getIdLib());
		member.setName(keyword);
		
		List<Member> memberList = null;
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(selfPageLogin.getIdLib());
		bookHeld.setLocalIdBarcode(keyword);
		
		
		
		try {
			memberList = memberService.selectMemberItemUserSelf(member);
			bookHeld = bookHeldService.selectBookHeldItemUserSelf(bookHeld);
			
		} catch(Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("memberList", memberList);
		data.put("bookHeld", bookHeld);
		
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
	}
	
	
	/** 회원번호로 대출 목록 가져오기. ajax 통신
	 * @throws Exception */
	@ResponseBody
	@RequestMapping(value = "/user_self_page/brw_list_member_id.do", method = RequestMethod.GET)
	public void BrwListByMemberId(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		
		web.init();
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		//Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (web.getSession("selfPageLogin") == null) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
			return;
		}
		
		Member selfPageLogin = (Member) web.getSession("selfPageLogin");
		
		
		int memberId = web.getInt("memberId");
		//front에서 "" null처리 해주기.
		
		Borrow borrow = new Borrow();
		// 이름에다가 그냥 검색 키워드 다 넣고, 이름으로 전화번호, 회원번호 검색
		borrow.setIdLibBrw(selfPageLogin.getIdLib());
		borrow.setIdMemberBrw(memberId);
		
		List<Borrow> brwRmnList = null;
		
		
		try {
			brwRmnList = brwService.getBorrowListByMbrId(borrow);
		} catch(Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("brwRmnList", brwRmnList);
		
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
	}
	
	
	/** 도서 대출 반납 처리 */
	@ResponseBody
	@RequestMapping(value = "/user_self_page/brw_rtn_book_member_ok.do", method = RequestMethod.POST)
	public void brwRtnBookOkUserSelf(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		
		web.init();
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		//Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (web.getSession("selfPageLogin") == null) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
			return;
		}
		
		Member selfPageLogin = (Member) web.getSession("selfPageLogin");
		
		int memberId = web.getInt("memberId", 0);
		int bookId = web.getInt("bookId", 0);
		//String barcodeBook = web.getString("barcodeBook","");
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(selfPageLogin.getIdLib());
		
		Borrow borrow = new Borrow();
		// 이름에다가 그냥 검색 키워드 다 넣고, 이름으로 전화번호, 회원번호 검색
		borrow.setIdLibBrw(selfPageLogin.getIdLib());
		borrow.setIdMemberBrw(memberId);
		borrow.setBookHeldId(bookId);
		
		List<Borrow> brwList = null;
		//도서 상태처리
		int bookState = 0;
		
		String resultMsg = null;
		
		try {
			//도서 id로 해서, 대출중인지 확인해야하고
			//내가 대출한 녀석이면, 반납처리. 그렇지 않으면 대출된 도서 입니다. 처리.
			//다른 사람이 대출했다면, exception 처리
			brwService.selectBorrowCountByBookHeldIdAndNotMy(borrow);
			
			//내가 대출한 것인지 count 판별.
			int myBrw = brwService.selectBorrowCountByBookHeldIdAndMemberId(borrow);
			
			if(myBrw>0) {
				//내가 대출을 한 도서이므로, 반납처리로
				//도서 상태 컬럼 변경을 위해 반납하면 1
				bookState = 1;
				//해당 idBrw를 가져오기 위함. 없으면 모든 bookHeldid로 된 녀석들이 다 업데이트됨.
				borrow = brwService.selectBorrowItemByBookHeldId(borrow);
				//반납 시간 업데이트
				brwService.updateBorrowEndDate(borrow);
				resultMsg = "정상적으로 반납 처리 되었습니다.";
			} else {
				//아무것도 대출이 안된 도서이므로, 대출 처리
				//도서 상태 컬럼 변경을 위해 대출하면 0
				bookState = 0;
				
				//대출권수 불러와서, 대출 권수가 초과 되었으면 대출 처리 하지 않기.
				Member curMember = new Member();
				curMember.setIdLib(selfPageLogin.getIdLib());
				curMember.setId(memberId);
				curMember = memberService.selectMember(curMember);
				int brwLimit = curMember.getBrwLimit();
				
				int lastBrwCount = brwService.selectBrwBookCountByMemberId(borrow);
				
				if(brwLimit-lastBrwCount <1) {
					web.printJsonRt("대출한도를 초과하였습니다.");
					return;
				}
				
				//도서 대출처리
				brwService.insertBorrow(borrow);
				//대출처리 되었을 경우에는 메시지 띄우지 않기.
//				resultMsg = "대출 처리 되었습니다.";
				resultMsg = null;
			}
			
			//혹시 모르니 다시한번 bookHeldId 주입
			bookHeld.setId(bookId);
			//도서의 상태 정보처리
			bookHeld.setAvailable(bookState);
			bookHeldService.updateAvailableById(bookHeld);
			
			brwList = brwService.getBorrowListByMbrId(borrow);
			
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("brwList", brwList);
		//도서상태를 가져와서 화면에 대출 상태 표현.
		data.put("bookStateNum", bookState);
		data.put("resultMsg", resultMsg);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	
	/** 도서 대출 반납 처리 회원정보 없이 도서만 반납했을 경우
	 * 	해당 도서가 대출중인지 판별하고, 대출중이면 반납처리. 대출중이 아니면 암 것도 안함. */
	@ResponseBody
	@RequestMapping(value = "/user_self_page/brw_rtn_book_check.do", method = RequestMethod.POST)
	public void brwRtnCheckBookUserSelf(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		
		web.init();
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		//Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (web.getSession("selfPageLogin") == null) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
			return;
		}
		
		Member selfPageLogin = (Member) web.getSession("selfPageLogin");
		
		//int memberId = web.getInt("memberId", 0);
		int bookId = web.getInt("bookId", 0);
		//String barcodeBook = web.getString("barcodeBook","");
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(selfPageLogin.getIdLib());
		
		Borrow borrow = new Borrow();
		// 이름에다가 그냥 검색 키워드 다 넣고, 이름으로 전화번호, 회원번호 검색
		borrow.setIdLibBrw(selfPageLogin.getIdLib());
		borrow.setBookHeldId(bookId);
		
		//도서 상태처리
		int bookState = 0;
		String resultMsg = null;
		
		try {
			//도서 id로 해서, 대출중인지 확인해야하고
			borrow = brwService.selectBorrowItemByBookHeldId(borrow);
			
			if(borrow!=null) {
				//내가 대출을 한 도서이므로, 반납처리로
				//도서 상태 컬럼 변경을 위해 반납하면 1
				bookState = 1;
				//반납 시간 업데이트
				brwService.updateBorrowEndDate(borrow);
				
				//혹시 모르니 다시한번 bookHeldId 주입
				bookHeld.setId(bookId);
				//도서의 상태 정보처리
				bookHeld.setAvailable(bookState);
				bookHeldService.updateAvailableById(bookHeld);
				
				resultMsg = "정상적으로 반납 처리 되었습니다.";
			}
			
			//borrow 값이 없으면 아무것도 하지 않음.
			
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		//도서상태를 가져와서 화면에 대출 상태 표현.
		data.put("resultMsg", resultMsg);
		//무언가 변경된 내용이 있으면 bookStateNum를 보내기.
		data.put("bookStateNum", bookState);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/** 회원 도서 조회용 페이지!!!!!!!!!!!! http 통신으로 처리 할거라 안씀. */
	@RequestMapping(value = "/user_self_page/book_list_xxxxxxxxxxxxxxx.do", method = RequestMethod.GET)
	public ModelAndView userSelfBookList(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (web.getSession("loginInfo") == null && web.getSession("selfPageLogin") == null) {
			//로그인이 안되어 있다면
			return web.redirect(web.getRootPath() + "/index.do", "비정상적인 접근으로 로그인이 필요합니다.");
			
		}
		
		//파라미터에서 값을 받아옴
		int idLib = web.getInt("lib");
		String stringKeyLib = web.getString("skl");
		
		/** 로그인 여부 검사 이 페이지는 로그인 개념 없이 진행*/
		// 로그인중인 회원 정보 가져오기
		/*Member loginInfo = (Member) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			idLib = loginInfo.getIdLib();
		}*/
		
		Library library = new Library();
		library.setIdLib(idLib);
		library.setStringKeyLib(stringKeyLib);
		
		String nameLib = null;
		
		try {
			
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(idLib);
		
		// 검색어 파라미터 받기 + Beans 설정
		int searchOpt = web.getInt("searchOpt");
		String keyword = web.getString("keyword", "");
		if(keyword!=null||keyword!=""){
			switch (searchOpt) {
			case 1:
				bookHeld.setTitle(keyword);
				break;
			case 2:
				bookHeld.setWriter(keyword);
				break;
			case 3:
				bookHeld.setPublisher(keyword);
				break;
			}
		}
		
		// 현재 페이지 번호에 대한 파라미터 받기
		int nowPage = web.getInt("page", 1);
		
		/** 2) 페이지 번호 구현하기 */
		// 전체 데이터 수 조회하기
		int totalCount = 0;
		try {
			totalCount = bookHeldService.selectBookCountForPage(bookHeld);
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		// 페이지 번호에 대한 연산 수행 후 조회조건값 지정을 위한 Beans에 추가하기
		page.pageProcess(nowPage, totalCount, 10, 5);
		bookHeld.setLimitStart(page.getLimitStart());
		bookHeld.setListCount(page.getListCount());
		//bookHeld.setCurrentListIndex(page.getIndexStart());
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		List<BookHeld> bookHeldList = null;
		try {
			bookHeldList = bookHeldService.getBookHeldList(bookHeld);
			if(bookHeldList != null) {
				for(int i=0; i<bookHeldList.size(); i++) {
					int bookHeldId = bookHeldList.get(i).getId();
					borrow.setBookHeldId(bookHeldId);
					if(brwService.selectBorrowItemByBookHeldId(borrow)!=null) {
						bookHeldList.get(i).setBrwStatus("대출중");
					}
				}
			}
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("bookHeldList", bookHeldList);
		model.addAttribute("searchOpt", searchOpt);
		model.addAttribute("keyword", keyword);
		model.addAttribute("idLib", idLib);
		model.addAttribute("nameLib", nameLib);
		model.addAttribute("stringKeyLib", stringKeyLib);
		model.addAttribute("page", page);
		model.addAttribute("pageDefUrl", "/blook_around.do");
		
		return new ModelAndView("/book/for_guest/book_list_guest");
	}
	
}
