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
import com.gaimit.helper.RegexHelper;
import com.gaimit.helper.WebHelper;

import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.Book;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BookService;
import com.gaimit.mlm.service.ManagerService;

@Controller
public class CountryController {
	/** log4j 객체 생성 및 사용할 객체 주입받기 */
	//private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
	// --> import study.spring.helper.WebHelper;
	@Autowired
	WebHelper web;
	
	@Autowired
	PageHelper page;
	
	@Autowired
	RegexHelper regexHelper;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	BookHeldService bookHeldService;
	
	@Autowired
	BookService bookService;
	
	/** 국가 목록 페이지 */
	@RequestMapping(value = "/book/country_list.do", method = RequestMethod.GET)
	public ModelAndView countryList(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		Book country = new Book();
		
		// 검색어 파라미터 받기 + Beans 설정
		String keyword = web.getString("keyword", "");
		country.setNameCountry(keyword);
		
		// 현재 페이지 번호에 대한 파라미터 받기
		int nowPage = web.getInt("page", 1);
		
		/** 2) 페이지 번호 구현하기 */
		// 전체 데이터 수 조회하기
		int totalCount = 0;
		try {
			totalCount = bookService.selectCountryCount(country);
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		// 페이지 번호에 대한 연산 수행 후 조회조건값 지정을 위한 Beans에 추가하기
		page.pageProcess(nowPage, totalCount, 10, 5);
		country.setLimitStart(page.getLimitStart());
		country.setListCount(page.getListCount());
		//bookHeld.setCurrentListIndex(page.getIndexStart());
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		List<Book> countryList = null;
		try {
			countryList = bookService.selectCountryList(country);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("countryList", countryList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("pageDefUrl", "/book/country_list.do");
		
		return new ModelAndView("book/country_list");
	}
	
	/** 국가 목록 페이지 */
	@RequestMapping(value = "/book/insert_country.do", method = RequestMethod.POST)
	public ModelAndView insertCountry(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		Book country = new Book();
		Book insertCountry = new Book();
		
		String keyword = web.getString("keyword", "");
		country.setNameCountry(keyword);
		
		// 검색어 파라미터 받기 + Beans 설정
		String nameCountry = web.getString("newCountry", "");
		if(!regexHelper.isValue(nameCountry)) {
			return web.redirect(null, "국가명을 입력해주세요.");
		}
		insertCountry.setNameCountry(nameCountry);
		
		// 현재 페이지 번호에 대한 파라미터 받기
		int nowPage = web.getInt("page", 1);
		
		/** 2) 페이지 번호 구현하기 */
		// 전체 데이터 수 조회하기
		int totalCount = 0;
		try {
			totalCount = bookService.selectCountryCount(country);
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		// 페이지 번호에 대한 연산 수행 후 조회조건값 지정을 위한 Beans에 추가하기
		page.pageProcess(nowPage, totalCount, 10, 5);
		country.setLimitStart(page.getLimitStart());
		country.setListCount(page.getListCount());
		//bookHeld.setCurrentListIndex(page.getIndexStart());
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		/*List<Book> countryList = null;*/
		try {
			bookService.insertCountry(insertCountry);
			/*countryList = bookService.selectCountryList(country);*/
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		/*model.addAttribute("countryList", countryList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("pageDefUrl", "/book/country_list.do");*/
		
		/*return new ModelAndView("book/country_list");*/
		return web.redirect(web.getRootPath() + "/book/country_list.do", "국가 등록이 완료되었습니다.");
	}
}
