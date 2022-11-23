package com.gaimit.mlm.controller.book;


import java.util.List;
import java.util.Locale;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gaimit.helper.PageHelper;
import com.gaimit.helper.WebHelper;

import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.ManagerService;

@Controller
public class BookHeldListPopup {
	/** log4j 객체 생성 및 사용할 객체 주입받기 */
	//private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
	// --> import study.spring.helper.WebHelper;
	@Autowired
	WebHelper web;
	
	@Autowired
	PageHelper page;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	BrwService brwService;
	
	/** 교수 목록 페이지 */
	@RequestMapping(value = "/book/book_held_list_popup.do", method = RequestMethod.GET)
	public ModelAndView doRun(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		int idLib = 0;
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			idLib = loginInfo.getIdLibMng();
		}
		
		Borrow borrow = new Borrow();
		borrow.setLibraryIdLib(idLib);
		
		// 검색어 파라미터 받기 + Beans 설정
		int searchOpt = web.getInt("searchOpt");
		String keyword = web.getString("keyword", "");
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		List<Borrow> rmnBookHeldList = null;
		
		switch (searchOpt) {
		case 1:
			borrow.setTitle(keyword);
			break;
		case 2:
			borrow.setWriter(keyword);
			break;
		case 3:
			borrow.setPublisher(keyword);
			break;
		}
		
		// 현재 페이지 번호에 대한 파라미터 받기
		int nowPage = web.getInt("page", 1);
		
		/** 2) 페이지 번호 구현하기 */
		// 전체 데이터 수 조회하기
		int totalCount = 0;
		try {
			totalCount = brwService.selectRemainedBookCountOnLibrary(borrow);
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		// 페이지 번호에 대한 연산 수행 후 조회조건값 지정을 위한 Beans에 추가하기
		page.pageProcess(nowPage, totalCount, 10, 5);
		borrow.setLimitStart(page.getLimitStart());
		borrow.setListCount(page.getListCount());
		
		if(!keyword.equals("")) {
			try {
				rmnBookHeldList = brwService.selectRemainedBookOnLibrary(borrow);
			} catch (Exception e) {
				return web.redirect(null, e.getLocalizedMessage());
			}
		}
		
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("bookHeldList", rmnBookHeldList);
		model.addAttribute("searchOpt", searchOpt);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		
		return new ModelAndView("book/book_held_list_popup");
	}
}
