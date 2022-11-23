package com.gaimit.mlm.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.Library;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.BbsDocumentService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.LibraryService;
import com.gaimit.mlm.service.ManagerService;

@Controller
public class Setting {
	
	Logger logger = LoggerFactory.getLogger(Setting.class);
	
	@Autowired
	SqlSession sqlSession;
	
	@Autowired
	WebHelper web;
	
	@Autowired
	BbsDocumentService bbsDocumentService;
	
	@Autowired
	BrwService brwService;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	LibraryService libraryService;
	
	
	@RequestMapping(value= "/setting/language.do")
	public ModelAndView doRun(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", null);
		}
		
		try {
			
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}

		/** (4) 최신 글 목록을 View에 전달 */
		//model.addAttribute("noticeList", noticeList);
		
		// "/WEB-INF/views/index.jsp"파일을 View로 사용한다.
		return new ModelAndView("setting/language");
	}
	
	@RequestMapping(value= "/setting/language_change_ok.do")
	public ModelAndView changeLangOk(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", null);
		}
		
		int langNo = web.getInt("language");
		
		Manager manager = new Manager();
		manager.setIdMng(loginInfo.getIdMng());
		manager.setLangMng(langNo);
		
		Manager editInfo = null;
		
		try {
			managerService.updateManagerLanguage(manager);
			editInfo = managerService.selectManager(manager);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		web.removeSession("loginInfo");
		web.setSession("loginInfo", editInfo);

		/** (4) 최신 글 목록을 View에 전달 */
		//model.addAttribute("noticeList", noticeList);
		
		// "/WEB-INF/views/index.jsp"파일을 View로 사용한다.
		//return new ModelAndView("setting/language");
		return web.redirect(web.getRootPath() + "/setting/language.do", "언어 정보가 수정되었습니다.");
	}
	
	//도서관 설정 페이지 도서관명, 도서관위치, 조회키, 만료일, 등록번호자리수 확인가능.
	@RequestMapping(value= "/setting/library_setting.do")
	public ModelAndView librarySettingPage(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", null);
		}
		
		Library library = new Library();
		library.setIdLib(loginInfo.getIdLibMng());
		
		try {
			library = libraryService.selectLibraryItem(library);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}

		/** (4) 최신 글 목록을 View에 전달 */
		model.addAttribute("libInfo", library);
		
		// "/WEB-INF/views/index.jsp"파일을 View로 사용한다.
		return new ModelAndView("setting/library_setting");
	}
	
	//등록번호 자리수 변경 비동기 쿼리
	@ResponseBody
	@RequestMapping(value= "/setting/change_nod_barcode_ok.do", method = RequestMethod.POST)
	public void editNodLib(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			web.printJsonRt("로그인 후에 이용 가능합니다.");
		}
		
		int nodBarcode = web.getInt("nodBarcode", 8);
		
		Library library = new Library();
		library.setIdLib(loginInfo.getIdLibMng());
		library.setNumOfDigitBarcode(nodBarcode);
		
		try {
			libraryService.updateNodBarcodeByIdLib(library);
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
	
	
	
	
	
	
	
	
	
	
	
	
}
