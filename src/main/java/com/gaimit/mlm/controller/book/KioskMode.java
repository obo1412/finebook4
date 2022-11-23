package com.gaimit.mlm.controller.book;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
import com.gaimit.helper.JwtHelper;

import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BookService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class KioskMode {
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
	BookHeldService bookHeldService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	BrwService brwService;
	
	@Autowired
	JwtHelper jwtHelper;
	
	/** 키오스크모드 대출/반납 버튼 페이지 */
	@RequestMapping(value = "/kiosk_mode.do", method = RequestMethod.GET)
	public ModelAndView doRun(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
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
					return web.redirect(null, e.getLocalizedMessage());
				}
				//매니저 정상적으로 불러오면, 세션처리 세션처리되면 자동 로그인 처리.
				web.setSession("loginInfo", manager);
			}
		}
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		

		
		try {
			
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		/*model.addAttribute("brwListToday", brwListToday);*/

		return new ModelAndView("kiosk_mode/kiosk_mode");
	}
	
	
	
	/** 도서 대출/반납 페이지 */
	@RequestMapping(value = "/kiosk_mode/kiosk_brw.do", method = RequestMethod.GET)
	public ModelAndView kioskBrwPage(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		

		
		try {
			
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		/*model.addAttribute("brwListToday", brwListToday);*/

		return new ModelAndView("kiosk_mode/kiosk_brw");
	}
	
	/** 도서 대출/반납 페이지 */
	@RequestMapping(value = "/kiosk_mode/kiosk_return.do", method = RequestMethod.GET)
	public ModelAndView kioskReturnPage(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		

		
		try {
			
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		/*model.addAttribute("brwListToday", brwListToday);*/

		return new ModelAndView("kiosk_mode/kiosk_return");
	}
	
	
	/** 회원 검색 ajax 통신
	 * @throws Exception */
	@ResponseBody
	@RequestMapping(value = "/kiosk_mode/search_member.do", method = RequestMethod.GET)
	public void kioskSearchMember(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
			return;
		}
		
		String memberBarcode = web.getString("memberBarcode");
		//front에서 "" null처리 해주기.
		
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		member.setBarcodeMbr(memberBarcode);
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(loginInfo.getIdLibMng());
		
		int lastBrwCount = 0;
		try {
			member = memberService.selectMemberByBarcodeMbr(member);
			//대출중인 개수 구하기 위함.
			borrow.setIdMemberBrw(member.getId());
			int curBrwCount = brwService.selectBrwBookCountByMemberId(borrow);
			lastBrwCount = member.getBrwLimit() - curBrwCount;
		} catch(Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("memberItem", member);
		data.put("lastBrwCount", lastBrwCount);
		
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
	}
	
	
	/** 도서 검색 ajax 통신
	 * @throws Exception */
	@ResponseBody
	@RequestMapping(value = "/kiosk_mode/search_book.do", method = RequestMethod.GET)
	public void kioskSearchBook(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
			return;
		}
		
		String bookBarcode = web.getString("bookBarcode");
		//front에서 "" null처리 해주기.
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		bookHeld.setLocalIdBarcode(bookBarcode);
		
		try {
			bookHeld = bookHeldService.getBookHelditem(bookHeld);
		} catch(Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("bookItem", bookHeld);
		
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
	}
	
	
	/** 도서 대출 완료 처리 */
	@ResponseBody
	@RequestMapping(value = "/kiosk_mode/brw_ok.do", method = RequestMethod.POST)
	public void brwRtnBookOkUserSelf(Locale locale, Model model,
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
			return;
		}
		
		int memberId = web.getInt("memberId", 0);
		String strBookIdArr = web.getString("bookIdArr");
		//System.out.println(strBookIdArr);
		strBookIdArr = strBookIdArr.substring(1, strBookIdArr.length()-1);
		//System.out.println(strBookIdArr);
		String[] bookIdArr = strBookIdArr.split(",");
		
		//도서 대출중인지 확인하기 위한 빈즈
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		
		Borrow borrow = new Borrow();
		// 이름에다가 그냥 검색 키워드 다 넣고, 이름으로 전화번호, 회원번호 검색
		borrow.setIdLibBrw(loginInfo.getIdLibMng());
		borrow.setIdMemberBrw(memberId);
		
		//도서 대출중이면 쌓이는 스택
		int brwStack = 0;
		
		String resultMsg = "";
		
		try {
			
			for(int i=0; i<bookIdArr.length; i++) {
				//도서 id로 해서, 대출중인지 확인해야하고
				//대출중이라면 그 도서가 대출중인 도서 알림, 예외처리.
//				System.out.println(bookIdArr[i]);
				borrow.setBookHeldId(Integer.parseInt(bookIdArr[i]));
				Borrow brwLog = brwService.selectBorrowItemByBookHeldId(borrow);
				//System.out.println(brwLog);
				if(brwLog != null) {
					brwStack++;
					resultMsg += brwLog.getLocalIdBarcode()+" ";
					//System.out.println("대출중 도서");
				}
			}
			//brw 스택이 있다면, 대출 처리까지 넘어가지 않고 메시지 리턴
			if(brwStack>0) {
				web.printJsonRt(resultMsg+"도서가 대출중입니다. 반납처리 후 다시 시도해주세요.");
				return;
			}
			
			//정상 대출 처리.
			for(int i=0; i<bookIdArr.length; i++) {
				borrow.setBookHeldId(Integer.parseInt(bookIdArr[i]));
				//대출처리
				brwService.insertBorrow(borrow);
				
				//혹시 모르니 다시한번 bookHeldId 주입
				bookHeld.setId(Integer.parseInt(bookIdArr[i]));
				//도서의 상태 정보처리
				bookHeld.setAvailable(0);
				bookHeldService.updateAvailableById(bookHeld);
			}
			
			
			
			
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		//data.put("brwList", brwList);
		data.put("resultMsg", resultMsg);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	
	/** 도서 반납 완료 처리 ajax 통신
	 * @throws Exception */
	@ResponseBody
	@RequestMapping(value = "/kiosk_mode/return_book_ok.do", method = RequestMethod.POST)
	public void kioskReturnBook(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
			return;
		}
		
		String bookBarcode = web.getString("bookBarcode");
		//front에서 "" null처리 해주기.
		//System.out.println(bookBarcode);
		
		Borrow borrow = new Borrow();
		// 이름에다가 그냥 검색 키워드 다 넣고, 이름으로 전화번호, 회원번호 검색
		borrow.setIdLibBrw(loginInfo.getIdLibMng());
		borrow.setLocalIdBarcode(bookBarcode);
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		bookHeld.setLocalIdBarcode(bookBarcode);
		
		try {
			//해당 idBrw를 가져오기 위함. 없으면 모든 bookHeldid로 된 녀석들이 다 업데이트됨.
			borrow = brwService.selectBorrowIdBrwByBarcodeBook(borrow);
			//System.out.println(borrow);
			if(borrow == null) {
				web.printJsonRt("대출 중 도서가 아닙니다.");
				return;
			}
			bookHeld = bookHeldService.getBookHelditem(bookHeld);
			
			//반납 시간 업데이트
			brwService.updateBorrowEndDate(borrow);
			
			//도서의 상태 정보처리
			bookHeld.setAvailable(1);
			bookHeldService.updateAvailableById(bookHeld);
			
		} catch(Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("bookItem", bookHeld);
		
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
	}
	
	
	
	

}
