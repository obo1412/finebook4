package com.gaimit.mlm.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.BbsComment;
import com.gaimit.mlm.model.BbsDocument;
import com.gaimit.mlm.model.BbsFile;
import com.gaimit.mlm.model.BookCheckModel;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.model.Library;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.model.TagSetting;
import com.gaimit.mlm.service.BbsDocumentService;
import com.gaimit.mlm.service.BbsCommentService;
import com.gaimit.mlm.service.BbsFileService;
import com.gaimit.mlm.service.BookCheckService;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.LibraryService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;
import com.gaimit.mlm.service.TagSettingService;

@Controller
public class AdminSetting {
	
	Logger logger = LoggerFactory.getLogger(AdminSetting.class);
	
	@Autowired
	SqlSession sqlSession;
	
	@Autowired
	WebHelper web;
	
	@Autowired
	PageHelper page;
	
	@Autowired
	BbsDocumentService bbsDocumentService;
	
	@Autowired
	BbsCommentService bbsCommentService;
	
	@Autowired
	BbsFileService bbsFileService;
	
	@Autowired
	BrwService brwService;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	LibraryService libraryService;
	
	@Autowired
	TagSettingService tagSettingService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	BookHeldService bookHeldService;
	
	@Autowired
	BookCheckService bookCheckService;
	
	/** 도서관 목록 페이지 */
	@RequestMapping(value = "/admin_setting/admin_lib_list.do", method = RequestMethod.GET)
	public ModelAndView adminSettingPage(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null || loginInfo.getIdLibMng() != 0) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			
		}
		
		// 검색어 파라미터 받기 + Beans 설정
		int searchOpt = web.getInt("searchOpt");
		String keyword = web.getString("keyword", "");
		String keywordHolder = web.getString("keywordHolder", "");
		
		//검색어를 담을 lib 객체
		Library library = new Library();
		
		int keySelector = 0;
		if((keyword==null||keyword=="")&&(keywordHolder==null||keywordHolder=="")) {
			//keyword 값 0, keywordHolder 값 0.
		} else if((keyword!=null&&keyword!="")&&(keywordHolder==null||keywordHolder=="")) {
			//keyword 값은 있고, keywordHolder 값 없을때.
			keySelector = 1;
		} else if((keyword==null||keyword=="")&&(keywordHolder!=null&&keywordHolder!="")) {
			//keyword 값은 없고, keywordHolder 값 있을때.
			keySelector = 2;
		} else if((keyword!=null&&keyword!="")&&(keywordHolder!=null&&keywordHolder!="")) {
			//값 둘다 있을 때.
			keySelector = 3;
		}
		
		switch(keySelector) {
		case 0:
			
			break;
			
		case 1: case 3:
			switch (searchOpt) {
			case 1:
				library.setNameLib(keyword);
				break;
			case 2:
				library.setLocLib(keyword);
				break;
			}
			
			// 키워드가 공백이 아니면, 키워드 홀더에 값 대입.
			keywordHolder = keyword;
			break;
			
		case 2:
			switch (searchOpt) {
			case 1:
				library.setNameLib(keywordHolder);
				break;
			case 2:
				library.setLocLib(keywordHolder);
				break;
			}
			break;
		}
		

		
		// 현재 페이지 번호에 대한 파라미터 받기
		int nowPage = web.getInt("page", 1);
		
		/** 2) 페이지 번호 구현하기 */
		// 전체 데이터 수 조회하기
		int totalCount = 0;
		try {
			totalCount = libraryService.selectLibraryCountForPage(library);
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		// 페이지 번호에 대한 연산 수행 후 조회조건값 지정을 위한 Beans에 추가하기
		//마지막 파라미터 페이지개수
		page.pageProcess(nowPage, totalCount, 20, 10);
		library.setLimitStart(page.getLimitStart());
		library.setListCount(page.getListCount());
		
		List<Library> libList = null;
		
		try {
			libList = libraryService.selectLibraryList(library);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		model.addAttribute("libList", libList);
		model.addAttribute("searchOpt", searchOpt);
		//키워드 홀더를 이용하여, 키워드가 keyword는 검색input 초기화를 위해 없어도됨.
		//model.addAttribute("keyword", keyword);
		model.addAttribute("keywordHolder", keywordHolder);
		model.addAttribute("page", page);
		model.addAttribute("pageDefUrl", "/admin_setting/admin_lib_list.do");
		
		return new ModelAndView("admin_setting/admin_lib_list");
	}
	
	
	/** updateExpDate 만료일 업데이트 비동기 처리 */
	@ResponseBody
	@RequestMapping(value = "/admin_setting/update_exp_date.do", method = RequestMethod.POST)
	public void updateExpDateAsync(Locale locale, Model model,
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
		
		int idLib = web.getInt("idLib");
		String expDate = web.getString("expDate");
		
		logger.info("만료일: "+expDate);
		
		Library lib = new Library();
		lib.setIdLib(idLib);
		lib.setExpDate(expDate);
		
		try {
			libraryService.updateExpDateLib(lib);
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
	
	
	/** 도서관 생성 페이지 */
	@RequestMapping(value = "/admin_setting/add_new_lib_ok.do", method = RequestMethod.POST)
	public ModelAndView addNewLib(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null || loginInfo.getIdLibMng() != 0) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			
		}
		
		// 검색어 파라미터 받기 + Beans 설정
		String nameLib = web.getString("nameLib");
		String locLib = web.getString("locLib", "");
		String stringKeyLib = web.getString("stringKeyLib");
		String expDate = web.getString("expDate");
		String purpose = web.getString("purpose");
		
		//검색어를 담을 lib 객체
		Library lib = new Library();
		lib.setNameLib(nameLib);
		lib.setLocLib(locLib);
		lib.setStringKeyLib(stringKeyLib);
		lib.setExpDate(expDate);
		lib.setPurpose(purpose);
		
		try {
			libraryService.insertNewLib(lib);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		return web.redirect(web.getRootPath() + "/admin_setting/admin_lib_list.do", "도서관 추가가 완료되었습니다.");
	}
	
	/** 도서관 정보 수정 */
	@RequestMapping(value = "/admin_setting/admin_lib_edit.do", method = RequestMethod.GET)
	public ModelAndView editLibItem(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null || loginInfo.getIdLibMng() != 0) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			
		}
		
		int idLib = web.getInt("idLib");
		
		Library lib = new Library();
		lib.setIdLib(idLib);
		
		Manager manager = new Manager();
		manager.setIdLibMng(idLib);
		
		List<Manager> managerList = null;
		
		try {
			lib = libraryService.selectLibraryItem(lib);
			managerList = managerService.selectManagerListByLib(manager);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("item", lib);
		model.addAttribute("managerList", managerList);
		
		return new ModelAndView("admin_setting/admin_lib_edit");
	}
	
	/** 도서관 수정 완료처리 페이지 */
	@RequestMapping(value = "/admin_setting/admin_lib_edit_ok.do", method = RequestMethod.POST)
	public ModelAndView updateLibOk(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null || loginInfo.getIdLibMng() != 0) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			
		}
		
		// 검색어 파라미터 받기 + Beans 설정
		int idLib = web.getInt("idLib");
		String nameLib = web.getString("nameLib");
		String locLib = web.getString("locLib", "");
		String stringKeyLib = web.getString("stringKeyLib");
		String purpose = web.getString("purpose");
		String expDate = web.getString("expDate");
		String regDate = web.getString("regDate");
		String statementDate = web.getString("statementDate");
		int kioskMode = web.getInt("kioskMode");
				
		//검색어를 담을 lib 객체
		Library lib = new Library();
		lib.setIdLib(idLib);
		lib.setNameLib(nameLib);
		lib.setLocLib(locLib);
		lib.setStringKeyLib(stringKeyLib);
		lib.setPurpose(purpose);
		lib.setExpDate(expDate);
		lib.setRegDateLib(regDate);
		lib.setStatementDate(statementDate);
		lib.setKioskMode(kioskMode);
		
		try {
			libraryService.updateLibItem(lib);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		return web.redirect(web.getRootPath() + "/admin_setting/admin_lib_edit.do?idLib="+idLib, "도서관 정보 수정이 완료되었습니다.");
	}
	
	
	/** 도서관 삭제 완료처리 */
	@RequestMapping(value = "/admin_setting/admin_lib_delete_ok.do", method = RequestMethod.POST)
	public ModelAndView deleteLibOk(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null || loginInfo.getIdLibMng() != 0) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			
		}
		
		// 검색어 파라미터 받기 + Beans 설정
		int idLib = web.getInt("idLib");
		
		//검색어를 담을 lib 객체
		Library lib = new Library();
		lib.setIdLib(idLib);
		
		TagSetting tag = new TagSetting();
		tag.setIdLib(idLib);
		//borrow 삭제용 객체
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(idLib);
		
		Member member = new Member();
		member.setIdLib(idLib);
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		
		BbsFile bbsFile = new BbsFile();
		bbsFile.setIdLibFile(idLib);
		
		//매니저 삭제를 위한 객체
		Manager manager = new Manager();
		manager.setIdLibMng(idLib);
		List<Manager> managerList = null;
		
		BbsComment bbsComment = new BbsComment();
		BbsDocument bbsDocument = new BbsDocument();
		
		BookCheckModel bookCheck = new BookCheckModel();
		bookCheck.setIdLib(idLib);
		List<BookCheckModel> bcsList = null;
		
		try {
			bcsList = bookCheckService.selectBcsList(bookCheck);
			for(int i=0; i<bcsList.size(); i++) {
				int idBcs = bcsList.get(i).getIdBcs();
				bookCheck.setIdBcs(idBcs);
				//bcs를 참조하는 book Check List를 삭제한다. 
				bookCheckService.deleteBclByBcs(bookCheck);
				//bcs 삭제
				bookCheckService.deleteBcs(bookCheck);
			}
			
			tagSettingService.deleteTagSettingByIdLib(tag);
			
			brwService.deleteBorrowsByIdLibBrw(borrow);
			
			memberService.deleteMemberAllDatas(member);
			
			bookHeldService.deleteBookHeldAllByLibraryIdLib(bookHeld);
			
			bbsFileService.deleteFileAllByIdLibFile(bbsFile);
			
			managerList = managerService.selectManagerListByLib(manager);
			for(int m=0; m<managerList.size(); m++) {
				int managerId = managerList.get(m).getIdMng();
				bbsComment.setManagerId(managerId);
				bbsCommentService.deleteCommentByManagerId(bbsComment);
				
				bbsDocument.setManagerId(managerId);
				bbsDocumentService.deleteDocumentByManagerId(bbsDocument);
				//매니저 삭제 처리.
				manager.setIdMng(managerId);
				managerService.deleteManager(manager);
			}
			
			libraryService.deleteLibItem(lib);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		return web.redirect(null, "도서관이 삭제되었습니다.");
	}
	
	/** 도서 전체(도서만) 삭제 완료처리 */
	@RequestMapping(value = "/admin_setting/admin_book_delete_ok.do", method = RequestMethod.POST)
	public ModelAndView deleteBookAllOk(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null || loginInfo.getIdLibMng() != 0) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			
		}
		
		// 검색어 파라미터 받기 + Beans 설정
		int idLib = web.getInt("idLib");
		
		//borrow 삭제용 객체
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(idLib);
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		
		BookCheckModel bookCheck = new BookCheckModel();
		bookCheck.setIdLib(idLib);
		List<BookCheckModel> bcsList = null;
		
		try {
			bcsList = bookCheckService.selectBcsList(bookCheck);
			for(int i=0; i<bcsList.size(); i++) {
				int idBcs = bcsList.get(i).getIdBcs();
				bookCheck.setIdBcs(idBcs);
				//bcs를 참조하는 book Check List를 삭제한다. 
				bookCheckService.deleteBclByBcs(bookCheck);
				//bcs 삭제
				bookCheckService.deleteBcs(bookCheck);
			}
			
			brwService.deleteBorrowsByIdLibBrw(borrow);
			
			bookHeldService.deleteBookHeldAllByLibraryIdLib(bookHeld);
			
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		return web.redirect(null, "도서 정보가 전부 삭제되었습니다.");
	}
	
	/** 관리자 manager 새로 추가 비동기 처리 */
	@ResponseBody
	@RequestMapping(value = "/admin_setting/add_new_manager.do", method = RequestMethod.POST)
	public void addNewManagerAsync(Locale locale, Model model,
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
		if (loginInfo == null || loginInfo.getIdLibMng() != 0) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
		}
		
		int idLib = web.getInt("idLib");
		String addUserIdMng = web.getString("addUserIdMng");
		String addUserPwMng = web.getString("addUserPwMng");
		String addNameMng = web.getString("addNameMng");
		String addEmailMng = web.getString("addEmailMng");
	
		
		Manager mng = new Manager();
		mng.setIdLibMng(idLib);
		mng.setUserIdMng(addUserIdMng);
		mng.setUserPwMng(addUserPwMng);
		mng.setNameMng(addNameMng);
		mng.setEmailMng(addEmailMng);
		
		
		try {
			//중복 아이디 체크
			managerService.selectCountForUserIdDuplicateCheck(mng);
			//아이디 생성
			managerService.insertManager(mng);
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
	
	/** 관리자 manager 삭제 비동기 처리 */
	@ResponseBody
	@RequestMapping(value = "/admin_setting/admin_lib_delete_manager.do", method = RequestMethod.POST)
	public void deleteManagerAsync(Locale locale, Model model,
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
		if (loginInfo == null || loginInfo.getIdLibMng() != 0) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
		}
		
		int idLib = web.getInt("idLib");
		int idMng = web.getInt("curIdMng");
	
		
		Manager mng = new Manager();
		mng.setIdLibMng(idLib);
		mng.setIdMng(idMng);
		
		
		try {
			
			managerService.deleteManager(mng);
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
	
	/** 관리자 manager user id 중복체크만 */
	@ResponseBody
	@RequestMapping(value = "/admin_setting/admin_lib_dup_check_userid_manager.do", method = RequestMethod.POST)
	public void dupCheckMangerUserId(Locale locale, Model model,
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
		if (loginInfo == null || loginInfo.getIdLibMng() != 0) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
		}
		
		String userIdMng = web.getString("addUserIdMng");
		
		Manager mng = new Manager();
		mng.setUserIdMng(userIdMng);
		
		
		try {
			managerService.selectCountForUserIdDuplicateCheck(mng);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("msg", "생성 가능한 관리자 ID 입니다.");
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	/** updateStatementDate 정산일 최신으로 업데이트 비동기 처리 */
	@ResponseBody
	@RequestMapping(value = "/admin_setting/update_statement_date_lastest.do", method = RequestMethod.POST)
	public void updateStatementDateAsync(Locale locale, Model model,
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
		
		int idLib = web.getInt("idLib");
		//String statementDate = web.getString("statementDate");
		
		Library lib = new Library();
		lib.setIdLib(idLib);
		//lib.setStatementDate(statementDate);
		
		String nowDate = null;
		
		try {
			libraryService.updateStatementDateLatest(lib);
			
			SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
			Date time = new Date();
			nowDate = dateForm.format(time);
			
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("nowDate", nowDate);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
