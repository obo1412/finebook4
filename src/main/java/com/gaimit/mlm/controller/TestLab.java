package com.gaimit.mlm.controller;



import java.io.IOException;

/*import java.io.PrintWriter;*/

import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gaimit.helper.ApiHelper;
import com.gaimit.helper.AuthorCode;
import com.gaimit.helper.MailHelper;
import com.gaimit.helper.PageHelper;
/*import com.gaimit.helper.FileInfo;*/
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.BbsFileService;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BookService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.TagSettingService;

@Controller
public class TestLab {
	/** log4j 객체 생성 및 사용할 객체 주입받기 */
	//private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
	// --> import study.spring.helper.WebHelper;
	// --> import org.apache.logging.log4j.Logger;
	Logger logger = LoggerFactory.getLogger(TestLab.class);
	
	@Autowired
	WebHelper web;
	
	@Autowired
	PageHelper page;
	
	@Autowired
	Util util;
	
	@Autowired
	MailHelper mail;
	
	@Autowired
	FrequentlyFunction frequentlyFunction;
	
	@Autowired
	AuthorCode authorCode;
	
	// --> import study.jsp.helper.UploadHelper;
	@Autowired
	UploadHelper upload;
	
	@Autowired
	ApiHelper apiHelper;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	BookHeldService bookHeldService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	BbsFileService bbsFileService;
	
	@Autowired
	TagSettingService tagSettingService;
	
	/**
	 * 메일 보내기 화면
	 * @param locale
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/test_lab/send_mail_test.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView mailServerTest(Locale locale, Model model) throws Exception {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		
		
		

		return new ModelAndView("test_lab/send_mail_test");
	}
	
	/**
	 * 메일보내기 ok 컨트롤러
	 * @param locale
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	
	@RequestMapping(value = "/test_lab/send_mail_test_ok.do")
	public ModelAndView sendMailOk(Locale locale, Model model, HttpServletRequest request,
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
		String sender = "admin@finebook.co.kr";
//		String receiver = web.getString("receiver");
//		String subject = web.getString("subject");
//		String content = web.getString("content");
		
		String receiver = "g.aim.it.kr@gmail.com";
		String subject = "테스트메일 송부중 메일테스트스트트";
		String content = "메일 테스트 송부 된당?";
		
		
		
		try {
			mail.sendMail(sender, receiver, subject, content);
		} catch (MessagingException e) {
			web.redirect(null, "메일 발송에 실패했습니다. 관리자에게 문의 바랍니다.");
		}
		
		return web.redirect(web.getRootPath() + "/test_lab/send_mail_test.do" , null);
	}
	
	
	
	/** 도서 등록 페이지 
	 * @throws Exception */
	@RequestMapping(value = "/test_lab/ctrl_to_ctrl.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView regBook(Locale locale, Model model) throws Exception {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		
		model.addAttribute("test", "dd");
		

		return new ModelAndView("test_lab/ctrl_to_ctrl");
	}
	
	
	@RequestMapping(value = "/test_lab/ctrl1.do")
	public ModelAndView searchPage(Locale locale, Model model, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/** (2) 사용하고자 하는 Helper+Service 객체 생성 */
		web.init();

		/** (3) 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			/*return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");*/
		}
		
		String keyword = web.getString("test_key");
		String chkReg = web.getString("straightReg");
		
		System.out.println("ctrl1 접근");
		System.out.println("keyword는 "+keyword);
		System.out.println("check여부는 "+chkReg);
		
		model.addAttribute("testtest", keyword);
		
		ModelAndView mv = new ModelAndView("test");
		System.out.println("getViewName "+mv.getViewName());
		
		if(chkReg != null) {
			//forward는 위 web.getString으로 받은거 그대로 전달
			//ctrl2로 포워딩되는 변수는 ctrl1과 같은 test_key
			//따라서 ctrl2에서도 web.getString("test_key")로 받으면 됨.
			//mv.setViewName("forward:/test_lab/ctrl2.do");
			
			//redirect는 model.addAttribute로 받은 것을 전달.
			//ctrl2 리다이렉팅되는 변수는 여기서 model로 전달한 testtest
			//따라서 ctrl2에서는 web.getString("testtest")로 받으면 됨.
			//mv.setViewName("redirect:/test_lab/ctrl2.do");
			return new ModelAndView("forward:/test_lab/ctrl3.do");
		} else {
			//mv.setViewName("test_lab/ctrl_to_ctrl");
		}
		
		return new ModelAndView("test_lab/ctrl_to_ctrl");
	}
	
	@RequestMapping(value = "/test_lab/ctrl2.do")
	public ModelAndView ctrl2(Locale locale, Model model, HttpServletRequest request,
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
		
		String test_key = web.getString("test_key");
		String testtest = web.getString("testtest");
		
		System.out.println("ctrl2 접근");
		System.out.println("test_key값 "+test_key);
		System.out.println("testtest값 "+testtest);
		
		return web.redirect(web.getRootPath() + "/test_lab/ctrl_to_ctrl.do" , null);
	}
	
	@RequestMapping(value = "/test_lab/ctrl3.do")
	public ModelAndView ctrl3(Locale locale, Model model, HttpServletRequest request,
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
		
		String test_key = web.getString("test_key");
		String testtest = web.getString("testtest");
		
		System.out.println("ctrl3 접근");
		System.out.println("test_key값 "+test_key);
		System.out.println("testtest값 "+testtest);
		
		return web.redirect(web.getRootPath() + "/test_lab/ctrl_to_ctrl.do" , null);
	}
	
}
