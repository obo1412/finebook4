package com.gaimit.mlm.controller.book;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaimit.helper.PageHelper;
import com.gaimit.helper.WebHelper;

import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BookService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class ReturnBookOk {
	/** log4j 객체 생성 및 사용할 객체 주입받기 */
	//private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
	// --> import study.spring.helper.WebHelper;
	@Autowired
	WebHelper web;
	
	@Autowired
	PageHelper page;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	BrwService brwService;
	
	@Autowired
	BookHeldService bookHeldService;
	
	/** 도서 반납 페이지 */
	@RequestMapping(value = "/book/return_book_ok_sync.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView RtnBook(Locale locale, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		
		/*response.setContentType("application/json");*/
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		int idLib = 0;
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			/*web.printJsonRt("로그인 정보가 명확하지 않습니다. 다시 로그인 해주세요.");*/
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			idLib = loginInfo.getIdLibMng();
		}
		
		// web으로부터 책 코드 번호 수신
		String barcodeBook = web.getString("barcodeBookRtn", "");
		int idMemberBrw = web.getInt("idMemberBrw", 0);
		String name = web.getString("name", "");
		String phone = web.getString("phone", "");
		int brwLimit = web.getInt("brwLimit", 0);
		
		//멤버가 현재 대여중인 도서 권수
		int brwNow = 0;
		//앞으로 대여가능한 도서 권수
		int brwPsb = 0;
		
		if(barcodeBook.equals("")) {
			return web.redirect(web.getRootPath() + "/book/brw_book.do", "도서바코드를 입력하세요.");
		}
		
		// 파라미터를 저장할 Beans
		/*Member member = new Member();
		member.setIdLib(idLib);*/
		
		Borrow brw = new Borrow();
		// 아래 brw로 idBrw 호출을 위한 객체
		Borrow brwSe = new Borrow();
		// 아래 멤버id(회원id)로 검색에 사용할 객체 생성 아직미구현
		List<Borrow> brwRmnList = null;
		List<Borrow> brwListToday = null;
		
		brw.setIdLibBrw(idLib);
		brw.setLocalIdBarcode(barcodeBook);
		
		//미반납 연체
		int overDueCount = 0;
		//반납된 연체 기간이 남아있을 경우
		Borrow overDueDate = new Borrow();
		String restrictDate = null;
		
		if(!(barcodeBook.equals(""))) {
			try {
				brwSe = brwService.getBorrowItemByBarcodeBook(brw);
				idMemberBrw = brwSe.getIdMemberBrw();
				name = brwSe.getName();
				phone = brwSe.getPhone();
				brw.setIdBrw(brwSe.getIdBrw());
				brw.setIdMemberBrw(brwSe.getIdMemberBrw());
				
				brwService.updateBorrowEndDate(brw);
				brwNow = brwService.selectBrwBookCountByMemberId(brw);
				//책으로 검색 시작 => 그 책을 빌린 회원id로 더 빌려간 책이 없는지 확인.
				brwRmnList = brwService.getBorrowListByMbrId(brw);
				//오늘 대출/반납 도서 조회
				brwListToday = brwService.selectBorrowListToday(brw);
				
				//연체 제한 정보 호출 도서 숫자 반영을 위하여
				//update보다 이후에 실행문 호출
				overDueCount = brwService.selectOverDueCountByMemberId(brw);
				overDueDate = brwService.selectRestrictDate(brw);
				if(overDueDate != null) {
					restrictDate = overDueDate.getRestrictDateBrw();
				}
			}  catch (Exception e) {
				return web.redirect(null, e.getLocalizedMessage());
			}
		}
		
		// 대출가능 권수 계산
		brwPsb = brwLimit - brwNow;
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("memberId", idMemberBrw);
		model.addAttribute("name", name);
		model.addAttribute("phone", phone);
		model.addAttribute("brwLimit", brwLimit);
		model.addAttribute("brwNow", brwNow);
		model.addAttribute("brwPsb", brwPsb);
		model.addAttribute("brwListToday", brwListToday);
		model.addAttribute("brwRmnList", brwRmnList);
		model.addAttribute("overDueCount", overDueCount);
		model.addAttribute("restrictDate", restrictDate);
		
		return new ModelAndView("book/brw_book");
		/*return web.redirect(web.getRootPath() + "/book/brw_book.do", "도서 반납이 완료되었습니다.");*/
	}
	
	/** 도서 반납 처리 비동기 */
	@ResponseBody
	@RequestMapping(value = "/book/return_book_ok.do", method = RequestMethod.POST)
	public void rtnBookOk(Locale locale, Model model,
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
		
		// 관리자/일반회원 로그인 분기를 위한 처리
		int idLib = 0;
		if(loginInfo.getIdLibMng()!=0) {
			idLib = loginInfo.getIdLibMng();
		} else {
			idLib = loginInfo.getIdLib();
		}
		
		String barcodeBookRtn = web.getString("barcodeBookRtn","");
		
		if("".equals(barcodeBookRtn)) {
			web.printJsonRt("반납하시려는 도서등록번호를 입력하세요.");
		}
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(idLib);
		borrow.setLocalIdBarcode(barcodeBookRtn);
		
		int memberId = 0;
		
		try {
			borrow = brwService.getBorrowItemByBarcodeBook(borrow);
			brwService.updateBorrowEndDate(borrow);
			memberId = borrow.getIdMemberBrw();
			
			//도서 상태 다시 대출가능으로 변경
			bookHeld.setId(borrow.getBookHeldId());
			bookHeld.setAvailable(1);
			bookHeldService.updateAvailableById(bookHeld);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("memberId", memberId);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	/** 도서 반납 취소 처리 비동기 */
	@ResponseBody
	@RequestMapping(value = "/book/return_book_cancel.do", method = RequestMethod.POST)
	public void rtnBookCancel(Locale locale, Model model,
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
		
		// 관리자/일반회원 로그인 분기를 위한 처리
		int idLib = 0;
		if(loginInfo.getIdLibMng()!=0) {
			idLib = loginInfo.getIdLibMng();
		} else {
			idLib = loginInfo.getIdLib();
		}
		
		String barcodeBookRtn = web.getString("barcodeBookRtn","");
		
		if("".equals(barcodeBookRtn)) {
			web.printJsonRt("반납하시려는 도서등록번호를 입력하세요.");
		}
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(idLib);
		borrow.setLocalIdBarcode(barcodeBookRtn);
		
		int memberId = 0;
		
		try {
			brwService.getBorrowCountByBarcodeBook(borrow);
			borrow = brwService.getBorrowItemByBarcodeBook(borrow);
			brwService.updateCancelBorrowEndDate(borrow);
			memberId = borrow.getIdMemberBrw();
			
			//도서 상태 다시 대출중으로 변경
			bookHeld.setId(borrow.getBookHeldId());
			bookHeld.setAvailable(1);
			bookHeldService.updateAvailableById(bookHeld);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("memberId", memberId);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	
	/** 대출중 도서 리스트 비동기 */
	@ResponseBody
	@RequestMapping(value = "/borrow/brw_list_by_title.do", method = RequestMethod.GET)
	public void brwBookListByTitle(Locale locale, Model model,
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
		
		// 관리자/일반회원 로그인 분기를 위한 처리
		int idLib = 0;
		if(loginInfo.getIdLibMng()!=0) {
			idLib = loginInfo.getIdLibMng();
		} else {
			idLib = loginInfo.getIdLib();
		}
		
		String searchBookTitle = web.getString("searchBookTitle","");
		
		if("".equals(searchBookTitle)) {
			web.printJsonRt("검색하실 도서제목을 입력하세요.");
		}
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(idLib);
		borrow.setTitle(searchBookTitle);
		
		List<Borrow> brwListByTitle = null;
		
		try {
			brwListByTitle = brwService.selectBorrowListByTitle(borrow);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("brwListByTitle", brwListByTitle);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	/** 도서 반납 처리 비동기 */
	@ResponseBody
	@RequestMapping(value = "/book/extend_book_due_date.do", method = RequestMethod.POST)
	public void extendBookDueDate(Locale locale, Model model,
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
		
		// 관리자/일반회원 로그인 분기를 위한 처리
		int idLib = 0;
		if(loginInfo.getIdLibMng()!=0) {
			idLib = loginInfo.getIdLibMng();
		} else {
			idLib = loginInfo.getIdLib();
		}
		
		String barcodeBook = web.getString("barcodeBook","");
		int extendDay = web.getInt("extendDay",3);
		
		if("".equals(barcodeBook)) {
			web.printJsonRt("반납하시려는 도서등록번호를 입력하세요.");
		}
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(idLib);
		borrow.setLocalIdBarcode(barcodeBook);
		
		int memberId = 0;
		
		try {
			borrow = brwService.getBorrowItemByBarcodeBook(borrow);
			borrow.setExtendDay(extendDay);
			
			// 기한 연장 기능 실행
			brwService.updateExtendBorrowDueDate(borrow);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("memberId", memberId);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	
	
	
	
	
}
