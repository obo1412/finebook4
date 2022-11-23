package com.gaimit.mlm.controller.bbs;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.Manager;

@Controller
public class DocumentWrite {	
	/** (1) 사용하고자 하는 Helper 객체 선언 */
	@Autowired
	WebHelper web;
	@Autowired
	BBSCommon bbs;

	@RequestMapping(value = "/bbs/document_write.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView doRun(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {
	
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		
		if(loginInfo==null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		/** (3) 게시판 카테고리 값을 받아서 View에 전달 */
		String category = web.getString("category");
		request.setAttribute("category", category);
		
		String inputImageLink = web.getString("inputImageLink");
		String inputApiTitle = web.getString("inputApiTitle");
		String inputApiAuthor = web.getString("inputApiAuthor");
		String inputApiPublisher = web.getString("inputApiPublisher");
		String inputApiIsbn = web.getString("inputApiIsbn");
		
		
//		System.out.println(category);
//		System.out.println(inputImageLink);
//		System.out.println(inputApiTitle);
//		System.out.println(inputApiAuthor);
//		System.out.println(inputApiPublisher);
//		System.out.println(inputApiIsbn);
		
		if("notice".equals(category)&&loginInfo.getIdLibMng()!=0) {
			return web.redirect(null, "공지사항은 읽기만 가능합니다.");
		}
		
		/** (4) 존재하는 게시판인지 판별하기 */
		try {
			String bbsName = bbs.getBbsName(category);
			model.addAttribute("bbsName", bbsName);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
			
		}
		
		String viewPage = "bbs/document_write";
		
		if("request".equals(category)) {
			viewPage = "bbs/book_request/document_write_book_request";
		}
		
		model.addAttribute("inputImageLink", inputImageLink);
		model.addAttribute("inputApiTitle", inputApiTitle);
		model.addAttribute("inputApiAuthor", inputApiAuthor);
		model.addAttribute("inputApiPublisher", inputApiPublisher);
		model.addAttribute("inputApiIsbn", inputApiIsbn);
		
		return new ModelAndView(viewPage);
	}

}
