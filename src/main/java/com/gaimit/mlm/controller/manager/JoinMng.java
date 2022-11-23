package com.gaimit.mlm.controller.manager;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.Library;
import com.gaimit.mlm.service.LibraryService;
import com.gaimit.mlm.service.ManagerService;

@Controller
public class JoinMng {

	/** (1) 사용하고자 하는 Helper 객체 선언 */
	// --> import study.jsp.helper.WebHelper;
	@Autowired
	WebHelper web;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	LibraryService libraryService;
	
	@RequestMapping(value="/managerFinebook/join_mng_xxx.do")
	public ModelAndView doRun(Locale locale, Model model, 
			HttpServletRequest request, HttpServletResponse response) {	
		
		/** (2) WebHelper 초기화 */
		web.init();
		
		/** (3) 로그인 여부 검사 */
		// 로그인 중이라면 이 페이지를 동작시켜서는 안된다.
		if (web.getSession("loginInfo") != null) {
			return web.redirect(web.getRootPath() + "/index.do", "이미 로그인 하셨습니다.");
		}
		
		Library library = new Library();
		
		List<Library> libList = null;
		try {
			libList = libraryService.selectLibraryList(library);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		model.addAttribute("libList", libList);
		
		return new ModelAndView("manager/join_mng");
	}
}
