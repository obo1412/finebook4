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
import com.gaimit.helper.PageHelper;
import com.gaimit.helper.WebHelper;

import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.service.BookService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class BrwBook {
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
	
	/** 도서 대출/반납 페이지 */
	@RequestMapping(value = "/book/brw_book.do", method = RequestMethod.GET)
	public ModelAndView doRun(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		/*List<Borrow> brwListToday = null;
		
		Borrow brw = new Borrow();
		brw.setIdLibBrw(loginInfo.getIdLibMng());*/
		
		try {
			/*brwListToday = brwService.selectBorrowListToday(brw);*/
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		/*model.addAttribute("brwListToday", brwListToday);*/

		return new ModelAndView("book/brw_book");
	}
	
	/** 도서 대출/반납 페이지 */
	@RequestMapping(value = "/book/brw_book_member.do", method = RequestMethod.GET)
	public ModelAndView brwBookMember(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Member loginInfo = (Member) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(loginInfo.getIdLib());
		borrow.setIdMemberBrw(loginInfo.getId());
		
		int brwNow = 0;
		
		Member member = new Member();
		member.setId(loginInfo.getId());
		member.setIdLib(loginInfo.getIdLib());
		
		try {
			member = memberService.selectMember(member);
			brwNow = brwService.selectBrwBookCountByMemberId(borrow);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("member", member);
		model.addAttribute("brwNow", brwNow);

		return new ModelAndView("book/brw_book_member");
	}
	
	/** 도서대출 회원검색 */
	@ResponseBody
	@RequestMapping(value = "/book/brw_search_member.do", method = RequestMethod.POST)
	public void brwSearchMember(Locale locale, Model model,
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
			/*return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");*/
		}
		
		//관리자/일반회원 로그인 분기를 위한 처리
		int idLib = 0;
		if(loginInfo.getIdLibMng()!=0) {
			idLib = loginInfo.getIdLibMng();
		} else {
			idLib = loginInfo.getIdLib();
		}
		
		String searchKeyword = web.getString("searchKeywordValue", "");
		
		Borrow member = new Borrow();
		// 이름에다가 그냥 검색 키워드 다 넣고, 이름으로 전화번호, 회원번호 검색
		member.setIdLibBrw(idLib);
		member.setName(searchKeyword);
		
		List<Borrow> memberList = null;
		
		try {
			memberList = brwService.selectMemberListForBorrow(member);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
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
	
	/** 도서대출 조회된 회원 선택 */
	@ResponseBody
	@RequestMapping(value = "/book/brw_pick_member.do", method = RequestMethod.POST)
	public void pickMember(Locale locale, Model model,
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
			/*return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");*/
		}
		
		int memberId = web.getInt("memberId", 0);
		
		Borrow borrow = new Borrow();
		// 이름에다가 그냥 검색 키워드 다 넣고, 이름으로 전화번호, 회원번호 검색
		//관리자/일반회원 로그인 분기를 위한 처리
		int idLib = 0;
		if(loginInfo.getIdLibMng()!=0) {
			idLib = loginInfo.getIdLibMng();
		} else {
			idLib = loginInfo.getIdLib();
		}
		borrow.setIdLibBrw(idLib);
		borrow.setIdMemberBrw(memberId);
		
		int brwNow = 0;
		
		int overDueCount = 0;
		Borrow overDueDate = new Borrow();
		String restrictDate = null;
		
		try {
			//해당 회원이 현재 몇권 대출중인지 확인 업데이트
			brwNow = brwService.selectBrwBookCountByMemberId(borrow);
			
			overDueCount = brwService.selectOverDueCountByMemberId(borrow);
			overDueDate =brwService.selectRestrictDate(borrow);
			if(overDueDate != null ) {
				restrictDate = overDueDate.getRestrictDateBrw();
			}
			
			borrow = brwService.selectMemberItemByMemberId(borrow);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("brwNow", brwNow);
		data.put("overDueCount", overDueCount);
		data.put("overDueDate", overDueDate);
		data.put("restrictDate", restrictDate);
		data.put("memberItem", borrow);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());	
		}
	}
	
	/** 오늘 대출/반납된 도서 목록 */
	@ResponseBody
	@RequestMapping(value = "/book/brw_list_D_day.do", method = RequestMethod.GET)
	public void brwListDday(Locale locale, Model model,
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
			/*return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");*/
		}
		
		// 관리자/일반회원 로그인 분기를 위한 처리
		int idLib = 0;
		if(loginInfo.getIdLibMng()!=0) {
			idLib = loginInfo.getIdLibMng();
		} else {
			idLib = loginInfo.getIdLib();
		}
		
		String pickDate = web.getString("pickDate","");
		//날짜 형식을 yyyy-MM-dd로 맞춰서 자바스크립트로 보냄.
		//날짜 형식이 맞지 않으면, 쿼리에서 비교할 수가 없어서 데이터 안나옴.
		
		Borrow borrow = new Borrow();
		// 이름에다가 그냥 검색 키워드 다 넣고, 이름으로 전화번호, 회원번호 검색
		borrow.setIdLibBrw(idLib);
		
		List<Borrow> brwListDday = null;
		
		try {
			//pickDate가 있다면, Date형식에 지정날짜를 넣고 borrow에 set
			if(pickDate!=null&&!"".equals(pickDate)) {
				borrow.setPickDateBrw(pickDate);
			}
			
			brwListDday = brwService.selectBorrowListToday(borrow);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("brwListDday", brwListDday);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	
	/** 도서대출 조회된 회원 선택 */
	@ResponseBody
	@RequestMapping(value = "/book/brw_remain_list_member.do", method = RequestMethod.GET)
	public void brwRemainList(Locale locale, Model model,
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
			/*return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");*/
		}
		
		// 관리자/일반회원 로그인 분기를 위한 처리
		int idLib = 0;
		if(loginInfo.getIdLibMng()!=0) {
			idLib = loginInfo.getIdLibMng();
		} else {
			idLib = loginInfo.getIdLib();
		}
		
		int memberId = web.getInt("memberId", 0);
		
		Borrow borrow = new Borrow();
		// 이름에다가 그냥 검색 키워드 다 넣고, 이름으로 전화번호, 회원번호 검색
		borrow.setIdLibBrw(idLib);
		borrow.setIdMemberBrw(memberId);
		
		List<Borrow> brwRmnList = null;
		
		try {
			brwRmnList = brwService.getBorrowListByMbrId(borrow);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
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
}
