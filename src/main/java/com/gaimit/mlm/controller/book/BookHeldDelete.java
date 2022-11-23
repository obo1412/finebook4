package com.gaimit.mlm.controller.book;

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
import com.gaimit.helper.RegexHelper;
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BrwService;

@Controller
public class BookHeldDelete {
	/** (1) 사용하고자 하는 Helper + Service 객체 선언 */
	// --> import org.apache.logging.log4j.Logger;
	Logger logger = LoggerFactory.getLogger(BookHeldDelete.class);
	// --> import org.apache.ibatis.session.SqlSession;
	@Autowired
	SqlSession sqlSession;
	// --> import study.jsp.helper.WebHelper;
	@Autowired
	WebHelper web;
	
	@Autowired
	PageHelper page;
		// --> import study.jsp.helper.RegexHelper;
	@Autowired
	RegexHelper regex;
	
	@Autowired
	Util util;
	// --> import study.jsp.helper.UploadHelper;
	@Autowired
	UploadHelper upload;
	
	@Autowired
	BookHeldService bookHeldService;
	
	@Autowired
	BrwService brwService;

	@RequestMapping(value = "/book/book_held_discard_ok.do", method = RequestMethod.POST)
	public ModelAndView discardBook(Locale locale, Model model, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		/** (2) 사용하고자 하는 Helper+Service 객체 생성 */
		web.init();

		/** (3) 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		int idLib = 0;
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			/*return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");*/
			web.printJsonRt("로그인 상태가 아닙니다.");
		} else {
			idLib = loginInfo.getIdLibMng();
		}
		
		String localIdBarcode = web.getString("localIdBarcode");
		int bookHeldId = web.getInt("bookHeldId");
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		bookHeld.setLocalIdBarcode(localIdBarcode);
		bookHeld.setId(bookHeldId);
		
		try {
			bookHeldService.updateBookHeldDiscard(bookHeld);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}

		/** (9) 가입이 완료되었으므로 메인페이지로 이동 */
		/*return web.redirect(web.getRootPath() + "/book/book_held_list.do", "폐기되었습니다.");*/
		return web.redirect(null, "폐기되었습니다.");
	}
	
	@RequestMapping(value = "/book/book_held_delete_ok.do", method = RequestMethod.POST)
	public ModelAndView deleteBook(Locale locale, Model model, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		/** (2) 사용하고자 하는 Helper+Service 객체 생성 */
		web.init();

		/** (3) 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		int bookHeldId = web.getInt("bookHeldId", 0);
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		bookHeld.setId(bookHeldId);
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(loginInfo.getIdLibMng());
		borrow.setBookHeldId(bookHeldId);
		
		try {
			brwService.selectBorrowCountDeleteBookHeldId(borrow);
			bookHeldService.deleteBookHeldItem(bookHeld);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}

		/** (9) 완료되었으므로 이전 페이지로 이동 */
		return web.redirect(web.getRootPath()+"/index_login.do?deleteClose=true", null);
	}
	
	/** 도서 일괄 삭제 */
	@ResponseBody
	@RequestMapping(value = "/book/book_held_delete_batch_by_checkbox.do", method = RequestMethod.POST)
	public void deleteBookBatchByCheckBox(Locale locale, Model model,
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
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
		}
		
		int bookHeldId = web.getInt("bookHeldId", 0);
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		bookHeld.setId(bookHeldId);
		
		
		try {
			bookHeldService.deleteBookHeldItem(bookHeld);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	@RequestMapping(value = "/book/book_held_discard_list.do")
	public ModelAndView discardBookList(Locale locale, Model model, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		/** (2) 사용하고자 하는 Helper+Service 객체 생성 */
		web.init();

		/** (3) 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		int idLib = 0;
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			idLib = loginInfo.getIdLibMng();
		}
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		
		// 검색어 파라미터 받기 + Beans 설정
		int searchOpt = web.getInt("searchOpt");
		String keyword = web.getString("keyword", "");
		switch (searchOpt) {
		case 1:
			bookHeld.setTitleBook(keyword);
			break;
		case 2:
			bookHeld.setWriterBook(keyword);
			break;
		case 3:
			bookHeld.setPublisherBook(keyword);
			break;
		}
		
		// 현재 페이지 번호에 대한 파라미터 받기
		int nowPage = web.getInt("page", 1);
		
		/** 2) 페이지 번호 구현하기 */
		// 전체 데이터 수 조회하기
		int totalCount = 0;
		try {
			totalCount = bookHeldService.selectBookDiscardCountForPage(bookHeld);
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		// 페이지 번호에 대한 연산 수행 후 조회조건값 지정을 위한 Beans에 추가하기
		page.pageProcess(nowPage, totalCount, 10, 5);
		bookHeld.setLimitStart(page.getLimitStart());
		bookHeld.setListCount(page.getListCount());
		
		List<BookHeld> discardList = null;
		try {
			discardList = bookHeldService.getBookHeldDiscardList(bookHeld);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		model.addAttribute("discardList", discardList);
		model.addAttribute("searchOpt", searchOpt);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);

		/** (9) 가입이 완료되었으므로 메인페이지로 이동 */
		return new ModelAndView("book/book_held_discard_list");
	}
}
