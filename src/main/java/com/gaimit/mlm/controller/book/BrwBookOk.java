package com.gaimit.mlm.controller.book;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaimit.helper.RegexHelper;
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class BrwBookOk {
	/** (1) 사용하고자 하는 Helper + Service 객체 선언 */
	// --> import org.apache.logging.log4j.Logger;
	Logger logger = LoggerFactory.getLogger(BrwBookOk.class);
	// --> import org.apache.ibatis.session.SqlSession;
	@Autowired
	SqlSession sqlSession;
	// --> import study.jsp.helper.WebHelper;
	@Autowired
	WebHelper web;
	// --> import study.jsp.helper.RegexHelper;
	@Autowired
	RegexHelper regex;
	// --> import study.jsp.helper.UploadHelper;
	@Autowired
	UploadHelper upload;
	
	@Autowired
	Util util;
	
	// --> import study.jsp.mysite.service.MemberService;
	@Autowired
	MemberService memberService;
	
	@Autowired
	BookHeldService bookHeldService;
	
	@Autowired
	BrwService brwService;
	
	/** 도서대출 조회된 회원 선택 */
	@ResponseBody
	@RequestMapping(value = "/book/brw_book_ok.do", method = RequestMethod.POST)
	public void brwBookOk(Locale locale, Model model,
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
		
		int memberId = web.getInt("memberId", 0);
		String barcodeBook = web.getString("barcodeBook","");
		
		if(memberId == 0) {
			web.printJsonRt("존재하지 않는 회원입니다.");
		}
		
		// 관리자/일반회원 로그인 분기를 위한 처리
		int idLib = 0;
		if(loginInfo.getIdLibMng()!=0) {
			idLib = loginInfo.getIdLibMng();
		} else {
			idLib = loginInfo.getIdLib();
		}
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		
		Borrow borrow = new Borrow();
		// 이름에다가 그냥 검색 키워드 다 넣고, 이름으로 전화번호, 회원번호 검색
		borrow.setIdLibBrw(idLib);
		borrow.setIdMemberBrw(memberId);
		
		try {
			//바코드 번호가 숫자만 들어왔을 경우에 기타값들과 혼선이 있을 수 있으니
			//애시당초 sortingIndex로 처리
			//받아온 바코드 번호를 주입한다
			bookHeld.setLocalIdBarcode(barcodeBook);
			borrow.setLocalIdBarcode(barcodeBook);
			int bookHeldId = 0;
			
			if(util.checkNumberOnly(barcodeBook)) {
				//입력받은 등록번호가 숫자만일 경우
				bookHeld.setSortingIndex(Integer.parseInt(barcodeBook));
				borrow.setSortingIndex(Integer.parseInt(barcodeBook));
				//대출 도서인지 판별
				brwService.selectBorrowCountBySortingIndex(borrow);
				//도서 id 가져오기
				bookHeldId = bookHeldService.selectBookHeldItemByOnlyNumToSortingIndex(bookHeld);
				
			} else {
				//입력받은 등록번호가 숫자가 아닐 경우
				//이미 대출중인 도서가 있는지 판별
				//추후에 예약제? 를 시행하기 위해서 분기하는 조건부 분기하는거 염두하기
				//getBorrowCountByBarcodeBook 함수가 현재는 void인데, int result로
				brwService.getBorrowCountByBarcodeBook(borrow);
				//도서 id 가져오기
				bookHeld = bookHeldService.getBookHelditem(bookHeld);
				bookHeldId = bookHeld.getId();
			}
			//위 절차로 가져온 bookHeldId를 borrow에 주입
			borrow.setBookHeldId(bookHeldId);
			
			//도서 상태 컬럼 변경을 위해
			bookHeld.setAvailable(0);
			//혹시 모르니 다시한번 bookHeldId 주입
			bookHeld.setId(bookHeldId);
			bookHeldService.updateAvailableById(bookHeld);
			
			brwService.insertBorrow(borrow);
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
