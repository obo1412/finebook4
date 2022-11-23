package com.gaimit.mlm.controller;

import java.io.IOException;
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
import org.springframework.web.servlet.ModelAndView;

import com.gaimit.helper.JwtHelper;
import com.gaimit.helper.PageHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.BbsDocumentService;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.ManagerService;

@Controller
public class Index {
	
	Logger logger = LoggerFactory.getLogger(Index.class);
	
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
	FrequentlyFunction frequentlyFunction;
	
	@Autowired
	JwtHelper jwtHelper;
	
	@Autowired
	ManagerService managerService;
	
	@RequestMapping(value= {"/", "/index.do"})
	public ModelAndView doRun(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		web.init();
		
		String autoLoginJwt = web.getCookie("jwt");
		Map<String, Object> claims = null;
		
		if(autoLoginJwt!= null) {
			//쿠기가 있으면, 일단 비교처리.
			claims = jwtHelper.verifyJWT(autoLoginJwt);
			if(claims != null) {
				//System.out.println(claims);
				Manager manager = new Manager();
				manager.setIdMng((int) claims.get("id"));
				try {
					manager = managerService.selectManager(manager);
				} catch (Exception e) {
					web.removeCookie("jwt");
					return web.redirect(null, e.getLocalizedMessage());
				}
				//매니저 정상적으로 불러오면, 세션처리 세션처리되면 자동 로그인 처리.
				web.setSession("loginInfo", manager);
			}
		}
		
		// 로그인 중이라면 로그인된 화면으로 이동.
		if (web.getSession("loginInfo") != null) {
			return web.redirect(web.getRootPath() + "/index_login.do", null);
		}
		
		/** (3) 각 게시판 종류별로 최근 게시물을 조회한다. */
//		List<BbsDocument> noticeList = null;	// 공지사항 최신 게시물
//		List<BbsDocument> freeList = null;		// 자유게시판 최신 게시물
//		List<BbsDocument> qnaList = null;		// 질문답변 최신 게시물
		
		try {
//			noticeList = this.getDocumentList("notice", 5);
//			freeList = this.getDocumentList("free", 5);
//			qnaList = this.getDocumentList("qna", 5);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}

		
		/** (4) 최신 글 목록을 View에 전달 */
//		model.addAttribute("noticeList", noticeList);
//		model.addAttribute("freeList", freeList);
//		model.addAttribute("qnaList", qnaList);
		
		// "/WEB-INF/views/index.jsp"파일을 View로 사용한다.
		return new ModelAndView("index");
	}
	
	
	@RequestMapping(value= "/index_login.do")
	public ModelAndView logInIndex(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", null);
		}
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(loginInfo.getIdLibMng());
		
		int maxCountIdBrw = 5;
		
		List<Borrow> brwStatistics = null;
		List<Borrow> brwBookStatistics = null;
		
		try {
			brwStatistics = brwService.selectBorrowMemberCountThisMonth(borrow);
			brwBookStatistics = brwService.selectBorrowBookCountThisMonth(borrow);
			if(brwBookStatistics.size() >0) {
				maxCountIdBrw = brwBookStatistics.get(0).getCountIdBrw();
			}
		}catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		if(brwStatistics.size() > 0 && brwBookStatistics.size() > 0) {
			model.addAttribute("brwStatistics", brwStatistics);
			model.addAttribute("brwBookStatistics", brwBookStatistics);
			model.addAttribute("maxCountIdBrw", maxCountIdBrw);
		}
		
		// "/WEB-INF/views/index.jsp"파일을 View로 사용한다.
		return new ModelAndView("index_login");
	}
	
	//회원로그인부분 추후 완전 삭제
	/*@RequestMapping(value= "/index_login_member.do")
	public ModelAndView logInMemberIndex(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		web.init();
		
		Member loginInfo = (Member) web.getSession("loginInfo");
		
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", null);
		}
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLib());
		
		//라벨 색상을 가져오기 위한 객체
		TagSetting tag = new TagSetting();
		tag.setIdLib(loginInfo.getIdLibMng());
		
		// 현재 페이지 번호에 대한 파라미터 받기
		int nowPage = web.getInt("page", 1);
		
		*//** 2) 페이지 번호 구현하기 *//*
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
		
		*//** 3) Service를 통한 SQL 수행 *//*
		// 조회 결과를 저장하기 위한 객체
		List<BookHeld> bookHeldList = null;
		try {
			bookHeldList = bookHeldService.getBookHeldList(bookHeld);
			if(bookHeldList != null) {
				for(int i=0; i<bookHeldList.size(); i++) {
					String classCode = bookHeldList.get(i).getClassificationCode();
					classCode = util.getFloatClsCode(classCode);
					if(classCode != null&&!"".equals(classCode)) {
						float classCodeFloat = Float.parseFloat(classCode);
						int classCodeInt = (int) (classCodeFloat);
						String classCodeColor = "";
						classCodeColor = frequentlyFunction.getColorKDC(tag, classCodeInt);
						bookHeldList.get(i).setClassCodeColor(classCodeColor);
					}
				}
			}
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		*//** 4) View 처리하기 *//*
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("bookHeldList", bookHeldList);
		model.addAttribute("page", page);
		model.addAttribute("pageDefUrl", "/book/book_held_list_member.do");
		
		// "/WEB-INF/views/index.jsp"파일을 View로 사용한다.
		return new ModelAndView("index_login_member");
		return new ModelAndView("book/book_held_list_member");
	}*/
	
	
	
	//각 게시판 게시물 몇개씩 조회하는거
//	private List<BbsDocument> getDocumentList(String category, int listCount) 
//			throws Exception {
//		List<BbsDocument> list = null;
//
//		// 조회할 조건 생성하기
//		// --> 지정된 카테고리의 0번째부터 listCount개 만큼 조회
//		BbsDocument document = new BbsDocument();
//		document.setCategory(category);
//		document.setLimitStart(0);
//		document.setListCount(listCount);
//
//		list = bbsDocumentService.selectDocumentList(document);
//
//		return list;
//	}

}
