package com.gaimit.mlm.controller.book;


import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class BookHeldListBrwd {
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
	BrwService brwService;
	
	/** 대출된 도서 목록 페이지 */
	@RequestMapping(value = "/book/book_held_list_brwd.do", method = RequestMethod.GET)
	public ModelAndView doRun(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		Borrow brw = new Borrow();
		brw.setIdLibBrw(loginInfo.getIdLibMng());
		
		// 검색어 파라미터 받기 + Beans 설정
		int searchOpt = web.getInt("searchOpt");
		String keyword = web.getString("keyword", "");
		if(keyword!=null||keyword!=""){
			switch (searchOpt) {
			case 1:
				brw.setName(keyword);
				break;
			case 2:
				brw.setTitleBook(keyword);
				break;
			case 3:
				brw.setLocalIdBarcode(keyword);
				break;
			}
		}
		
		String memberClassName = web.getString("memberClassName");
//		System.out.println(memberClassName);
		if(memberClassName != null) {
			brw.setClassName(memberClassName);
		}
		
		// 현재 페이지 번호에 대한 파라미터 받기
		int nowPage = web.getInt("page", 1);
		
		/** 2) 페이지 번호 구현하기 */
		// 전체 데이터 수 조회하기
		int totalCount = 0;
		try {
			totalCount = brwService.selectBorrowListCount(brw);
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		// 페이지 번호에 대한 연산 수행 후 조회조건값 지정을 위한 Beans에 추가하기
		page.pageProcess(nowPage, totalCount, 10, 5);
		brw.setLimitStart(page.getLimitStart());
		brw.setListCount(page.getListCount());
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		
		List<Borrow> brwList = null;
		List<Borrow> brwListRtnToday = null;
		
//		memberClassName을 가져오기 위한 리스트 객체
		List<Member> memberClassList = null;
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		
		try {
			brwList = brwService.getBorrowList(brw);
			brwListRtnToday = brwService.selectReturnListToday(brw);
			
			memberClassList = memberService.selectMemberClassListByLib(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("brwList", brwList);
		model.addAttribute("brwListRtnToday", brwListRtnToday);
		model.addAttribute("memberClassName", memberClassName);
		model.addAttribute("memberClassList", memberClassList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("searchOpt", searchOpt);
		model.addAttribute("page", page);
		
		return new ModelAndView("book/book_held_list_brwd");
	}	
	
	
	/** 대출된 도서 목록 엑셀화 페이지 */
	@ResponseBody
	@RequestMapping(value = "/book/brwed_book_held_list_to_excel.do", method = RequestMethod.POST)
	public void excelExtractBrwedBookHeldList(Locale locale, Model model,
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
			/*return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");*/
		}
		
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(loginInfo.getIdLibMng());
		
		List<Borrow> bookHeldList = null;
		

		String defaultPath = "/var/packages/Tomcat7/target/src/webapps/downloads/upload/finebook4/excel/libNo"+loginInfo.getIdLibMng();
		String filePath = defaultPath+"/"+"대출도서목록.xlsx";
		
		FileOutputStream fos = null;
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("대출도서목록");
		XSSFRow titleRow = null;
		XSSFRow row = null;
		
		//엑셀로 내보내기 위한 준비
		
		try {
			bookHeldList = brwService.getBorrowList(borrow);
			int curCol = 0;
			titleRow = sheet.createRow(0);
			titleRow.createCell(curCol).setCellValue("번호");
			curCol++;
			titleRow.createCell(curCol).setCellValue("회원명");
			curCol++;
			titleRow.createCell(curCol).setCellValue("회원연락처");
			curCol++;
			titleRow.createCell(curCol).setCellValue("회원분류");
			curCol++;
			titleRow.createCell(curCol).setCellValue("도서명");
			curCol++;
			titleRow.createCell(curCol).setCellValue("저자명");
			curCol++;
			titleRow.createCell(curCol).setCellValue("도서등록번호");
			curCol++;
			titleRow.createCell(curCol).setCellValue("대출일시");
			
			for(int i=1; i<=bookHeldList.size(); i++) {
				int j = i-1;
				int curValueCol = 0;
				row = sheet.createRow(i);
				row.createCell(curValueCol).setCellValue(i);
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getName());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getPhone());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getClassName());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getTitle());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getWriter());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getLocalIdBarcode());
				curValueCol++;
				if(bookHeldList.get(j).getStartDateBrw().length() > 9 ) {
					String tempDate = bookHeldList.get(j).getStartDateBrw();
					String startDate = tempDate.substring(0,10);
					row.createCell(curValueCol).setCellValue(startDate);
				} else {
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getStartDateBrw());
				}
			}
			
			File uploadDirFile = new File(defaultPath);
			if(!uploadDirFile.exists()) {
				uploadDirFile.mkdirs();
			}
			
			fos = new FileOutputStream(filePath);
			workbook.write(fos);
			workbook.close();
			fos.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("filePath", filePath);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());	
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
