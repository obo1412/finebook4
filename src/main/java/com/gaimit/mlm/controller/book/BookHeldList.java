package com.gaimit.mlm.controller.book;


import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaimit.helper.PageHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.controller.FrequentlyFunction;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.model.Library;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.TagSetting;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.LibraryService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.TagSettingService;

@Controller
public class BookHeldList {
	/** log4j 객체 생성 및 사용할 객체 주입받기 */
	//private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
	// --> import study.spring.helper.WebHelper;
	@Autowired
	WebHelper web;
	
	@Autowired
	PageHelper page;
	
	@Autowired
	Util util;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	BookHeldService bookHeldService;
	
	@Autowired
	BrwService brwService;
	
	@Autowired
	FrequentlyFunction frequentlyFunction;
	
	@Autowired
	TagSettingService tagSettingService;
	
	@Autowired
	LibraryService libraryService;
	
	/** 도서 목록 페이지 */
	@RequestMapping(value = "/book/book_held_list.do", method = RequestMethod.GET)
	public ModelAndView doRun(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		int idLib = 0;
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			idLib = loginInfo.getIdLibMng();
		}
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		
		// 검색어 파라미터 받기 + Beans 설정
		int searchOpt = web.getInt("searchOpt");
		String keyword = web.getString("keyword", "");
		String keywordHolder = web.getString("keywordHolder", "");
		
		//태그 키워드까지 검색할건지 체크박스
		String tagSearchChecked = web.getString("chkBox_tag_search");
		
		
		
		int keySelector = 0;
		if((keyword==null||keyword=="")&&(keywordHolder==null||keywordHolder=="")) {
			//keyword 값 0, keywordHolder 값 0.
		} else if((keyword!=null&&keyword!="")&&(keywordHolder==null||keywordHolder=="")) {
			//keyword 값은 있고, keywordHolder 값 없을때.
			keySelector = 1;
		} else if((keyword==null||keyword=="")&&(keywordHolder!=null&&keywordHolder!="")) {
			//keyword 값은 없고, keywordHolder 값 있을때.
			keySelector = 2;
			//키워드에 키워드 홀더값을 대입.
			keyword = keywordHolder;
		} else if((keyword!=null&&keyword!="")&&(keywordHolder!=null&&keywordHolder!="")) {
			//값 둘다 있을 때.
			keySelector = 3;
		}
		
		if(tagSearchChecked!=null) {
			//태그 검색 체크박스가 되어있다면, 태그에도 검색을 포함하여야 함.
			bookHeld.setTag(keyword);
			//bookHeld tag 빈즈에 값을 넣어놓고, mapper에서,
			//tag 값이 없으면 title만 검색하고, tag 값이 있으면,
			//tag like {title} 이런식으로 처리함. 결국 맵퍼에서 처리.
		}
		
		switch(keySelector) {
		case 0:
			
			break;
			
		case 1:
		case 3:
		case 2: //keywordHolder를 keyword로 대입함.
			switch (searchOpt) {
			case 1:
				bookHeld.setTitle(keyword);
				break;
			case 2:
				bookHeld.setWriter(keyword);
				break;
			case 3:
				bookHeld.setPublisher(keyword);
				break;
			}
			
			// 키워드가 공백이 아니면, 키워드 홀더에 값 대입.
			keywordHolder = keyword;
			break;
		}
	
		
		// 현재 페이지 번호에 대한 파라미터 받기
		int nowPage = web.getInt("page", 1);
		
		//bookShelf 검색어 받기
		String bookShelf = web.getString("bookShelf");
		//System.out.println("서가 공백인지 null인지판단 ["+bookShelf+"]");
		if(bookShelf != null) {
			//값이 null이 아니면 검색값 넣기
			//System.out.println("서가null아님["+bookShelf+"]");
			bookHeld.setBookShelf(bookShelf);
		}
		
		String addiCode = web.getString("addiCode");
		//System.out.println("["+addiCode+"]");
		if(addiCode != null) {
			bookHeld.setAdditionalCode(addiCode);
		}
		
		/** 2) 페이지 번호 구현하기 */
		// 전체 데이터 수 조회하기
		int totalCount = 0;
		try {
			totalCount = bookHeldService.selectBookCountForPage(bookHeld);
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		// 페이지 번호에 대한 연산 수행 후 조회조건값 지정을 위한 Beans에 추가하기
		//마지막 파라미터 페이지개수
		page.pageProcess(nowPage, totalCount, 18, 10);
		bookHeld.setLimitStart(page.getLimitStart());
		bookHeld.setListCount(page.getListCount());
		//bookHeld.setCurrentListIndex(page.getIndexStart());
		
		/** 3) Service를 통한 SQL 수행 */
		//서가를 담을 리스트
		List<String> bookShelfList = null;
		
		//별치기호 목록 담을 리스트
		List<String> addiCodeList = null;
		
		// 조회 결과를 저장하기 위한 객체
		List<BookHeld> bookHeldList = null;
		
		//라벨 색상을 가져오기 위한 객체
		TagSetting tag = new TagSetting();
		tag.setIdLib(idLib);
		
		TagSetting labelType = new TagSetting();
		labelType.setIdLib(loginInfo.getIdLibMng());
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(idLib);
		
		try {
			//서가 리스트 처리.
			bookShelfList = bookHeldService.selectBookShelfGroup(bookHeld);
			
			//별치기호 리스트
			addiCodeList = bookHeldService.selectAddiCodeGroup(bookHeld);
			
			//태그타입 확인을 위한 tag 조회
			labelType = tagSettingService.selectRollTagPositionValue(labelType);
			
			bookHeldList = bookHeldService.getBookHeldList(bookHeld);
			if(bookHeldList != null) {
				for(int i=0; i<bookHeldList.size(); i++) {
					//도서 상태를 확인하기 위한 쿼리
					int bookHeldId = bookHeldList.get(i).getId();
					borrow.setBookHeldId(bookHeldId);
					if(brwService.selectBorrowItemByBookHeldId(borrow)!=null) {
						bookHeldList.get(i).setBrwStatus("대출중");
					}
					//도서상태 확인 쿼리 끝
					
					//도서 분류기호 색상 확인
					String classCode = bookHeldList.get(i).getClassificationCode();
					classCode = util.getFloatClsCode(classCode);
					if(classCode != null&&!"".equals(classCode)) {
						float classCodeFloat = Float.parseFloat(classCode);
						int classCodeInt = (int) (classCodeFloat);
						//원본
						/*String classCodeColor = util.getColorKDC(classCodeInt);
						bookHeldList.get(i).setClassCodeColor(classCodeColor);*/
						String classCodeColor = "";
						classCodeColor = frequentlyFunction.getColorKDC(tag, classCodeInt);
						bookHeldList.get(i).setClassCodeColor(classCodeColor);
					}
				}
			}
			
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("labelType", labelType);
		model.addAttribute("bookHeldList", bookHeldList);
		model.addAttribute("searchOpt", searchOpt);
		//서가 전달
		model.addAttribute("bookShelf", bookShelf);
		//서가 리스트 결과값 전달
		model.addAttribute("bookShelfList", bookShelfList);
		
		//별치기호 값 전달
		model.addAttribute("addiCode", addiCode);
		//별치기호 리스트 전달 addiCodeList
		model.addAttribute("addiCodeList", addiCodeList);
		
		//키워드 홀더를 이용하여, 키워드가
		//model.addAttribute("keyword", keyword);
		model.addAttribute("keywordHolder", keywordHolder);
		model.addAttribute("tagSearchChecked", tagSearchChecked);
		model.addAttribute("page", page);
		model.addAttribute("pageDefUrl", "/book/book_held_list.do");
		
		return new ModelAndView("book/book_held_list");
	}
	
	/** 회원 도서 조회용 페이지!!!!!!!!!!!! */
	@RequestMapping(value = "/blook_around.do", method = RequestMethod.GET)
	public ModelAndView bookHeldListMember(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		//파라미터에서 값을 받아옴
		int idLib = web.getInt("lib");
		String stringKeyLib = web.getString("skl");
		
		/** 로그인 여부 검사 이 페이지는 로그인 개념 없이 진행*/
		// 로그인중인 회원 정보 가져오기
		/*Member loginInfo = (Member) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			idLib = loginInfo.getIdLib();
		}*/
		
		Library library = new Library();
		library.setIdLib(idLib);
		library.setStringKeyLib(stringKeyLib);
		
		String nameLib = null;
		
		try {
			library = libraryService.selectLibItemByKeys(library);
			idLib = library.getIdLib();
			nameLib = library.getNameLib();
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(idLib);
		
		// 검색어 파라미터 받기 + Beans 설정
		int searchOpt = web.getInt("searchOpt");
		String keyword = web.getString("keyword", "");
		if(keyword!=null||keyword!=""){
			switch (searchOpt) {
			case 1:
				bookHeld.setTitle(keyword);
				break;
			case 2:
				bookHeld.setWriter(keyword);
				break;
			case 3:
				bookHeld.setPublisher(keyword);
				break;
			}
		}
		
		// 현재 페이지 번호에 대한 파라미터 받기
		int nowPage = web.getInt("page", 1);
		
		/** 2) 페이지 번호 구현하기 */
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
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		List<BookHeld> bookHeldList = null;
		try {
			bookHeldList = bookHeldService.getBookHeldList(bookHeld);
			if(bookHeldList != null) {
				for(int i=0; i<bookHeldList.size(); i++) {
					int bookHeldId = bookHeldList.get(i).getId();
					borrow.setBookHeldId(bookHeldId);
					if(brwService.selectBorrowItemByBookHeldId(borrow)!=null) {
						bookHeldList.get(i).setBrwStatus("대출중");
					}
				}
			}
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("bookHeldList", bookHeldList);
		model.addAttribute("searchOpt", searchOpt);
		model.addAttribute("keyword", keyword);
		model.addAttribute("idLib", idLib);
		model.addAttribute("nameLib", nameLib);
		model.addAttribute("stringKeyLib", stringKeyLib);
		model.addAttribute("page", page);
		model.addAttribute("pageDefUrl", "/blook_around.do");
		
		return new ModelAndView("/book/for_guest/book_list_guest");
	}
	
	/** 도서 목록 엑셀화 페이지 */
	@ResponseBody
	@RequestMapping(value = "/book/book_held_list_to_excel.do", method = RequestMethod.POST)
	public void excelExtractBookHeldList(Locale locale, Model model,
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
		
		String targetYear = web.getString("targetYear");
		// 아래 연도표기만 하기 위함.
		int onlyYear = Integer.parseInt(targetYear);
		SimpleDateFormat dformat = new SimpleDateFormat("yyyy-01-01");
		if(targetYear == null || "".equals(targetYear)) {
			Date nowDate = new Date();
			targetYear = dformat.format(nowDate);
		} else {
			targetYear = targetYear+"-01-01";
		}
		
		String bookShelf = web.getString("bookShelf");
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		bookHeld.setBookShelf(bookShelf);
		
		List<BookHeld> bookHeldList = null;
		
//		Date now = new Date();
//		SimpleDateFormat nowForm = new SimpleDateFormat("yyyyMMddHHmmss");
//		String nowStr = nowForm.format(now);
		String nowStr = bookShelf;
		if(bookShelf==null) {
			nowStr = "전체목록";
		} else if("uncateg".equals(bookShelf)) {
			nowStr = "서가미분류";
		}
		String defaultPath = "/var/packages/Tomcat7/target/src/webapps/downloads/upload/finebook4/excel/libNo"+loginInfo.getIdLibMng();
		String filePath = defaultPath+"/"+"도서목록_"+nowStr+".xlsx";
		
		FileOutputStream fos = null;
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("전체도서목록");
//		XSSFSheet sheet2 = workbook.createSheet("등록폐기요약");
		
		XSSFRow titleRow = null;
		Cell titleCell = null;
		XSSFRow row = null;
//		XSSFRow summaryRow = null;
		
		XSSFCellStyle topRowStyle = workbook.createCellStyle();
		topRowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		topRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		// 색상 참조용.
		IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
		//엑셀로 내보내기 위한 준비
		
		try {
			// 도서 전체 목록
			for(int b=0; b<4; b++) {
				if(b==0) {
					// 2023년 기준 2022년 자료
					bookHeld.setRegDate(Integer.toString(onlyYear-1));
					bookHeldList = bookHeldService.selectBookHeldListToExcel(bookHeld);
					// 전체 시트 이름 변경
					sheet = workbook.getSheetAt(0);
					workbook.setSheetName(0, (onlyYear-1)+"년기준보유량_"+bookHeldList.size()+"권");
					// 빨간색
					sheet.setTabColor(new XSSFColor(new java.awt.Color(220,20,60), colorMap));
				} else if(b == 1) {
					// 2023년이면 
					bookHeld.setRegDate(Integer.toString(onlyYear));
					bookHeldList = bookHeldService.selectBookHeldListToExcel(bookHeld);
					// 전체 시트 이름 변경
					workbook.createSheet("당해 보유량");
					sheet = workbook.getSheetAt(1);
					workbook.setSheetName(1, onlyYear+"년기준보유량_"+bookHeldList.size()+"권");
					sheet.setTabColor(new XSSFColor(new java.awt.Color(175,3,253), colorMap));
//					summaryRow = sheet2.createRow(0);
//					summaryRow.createCell(0).setCellValue("기준연도");
//					summaryRow.createCell(1).setCellValue(targetYear);
//					summaryRow = sheet2.createRow(1);
//					summaryRow.createCell(0).setCellValue("전체 정상도서수");
//					summaryRow.createCell(1).setCellValue(bookHeldList.size());
				} else if(b == 2) {
					// sheet4를 객체 sheet로 옮기며 폐기 도서 처리
					bookHeld.setRegDate(targetYear);
					bookHeldList = bookHeldService.selectDiscardBookHeldListToExcelByYear(bookHeld);
					// 목록을 표기할 시트 옮겨주기
					workbook.createSheet("폐기도서");
					sheet = workbook.getSheetAt(2);
					workbook.setSheetName(2, onlyYear+"년폐기수량_"+bookHeldList.size()+"권");
					sheet.setTabColor(new XSSFColor(new java.awt.Color(250,243,72), colorMap));
					// 요약 시트에 데이터 표기
//					summaryRow = sheet2.createRow(3);
//					summaryRow.createCell(0).setCellValue("폐기 도서수");
//					summaryRow.createCell(1).setCellValue(bookHeldList.size());
				} else if(b ==3) {
					// sheet3을 객체 sheet로 옮기며 새로 등록 도서 처리
					bookHeld.setRegDate(targetYear);
					bookHeldList = bookHeldService.selectNewBookHeldListToExcelByYear(bookHeld);
					// 목록을 표기할 시트 옮겨주기
					workbook.createSheet("등록도서");
					sheet = workbook.getSheetAt(3);
					workbook.setSheetName(3, "장서증가량_"+bookHeldList.size()+"권");
					sheet.setTabColor(new XSSFColor(new java.awt.Color(120,229,52), colorMap));
					// 요약 시트에 데이터 표기
//					summaryRow = sheet2.createRow(2);
//					summaryRow.createCell(0).setCellValue("등록 도서수");
//					summaryRow.createCell(1).setCellValue(bookHeldList.size());
				}
				titleRow = sheet.createRow(0);
				int curCol = 0;
				titleRow.createCell(curCol).setCellValue("번호");
				curCol++;
				titleRow.createCell(curCol).setCellValue("도서명");
				curCol++;
				titleRow.createCell(curCol).setCellValue("저자명");
				curCol++;
				titleRow.createCell(curCol).setCellValue("출판사");
				curCol++;
				titleRow.createCell(curCol).setCellValue("출판일");
				curCol++;
				titleRow.createCell(curCol).setCellValue("출판년도");
				curCol++;
				titleRow.createCell(curCol).setCellValue("ISBN13");
				curCol++;
				titleRow.createCell(curCol).setCellValue("서가");
				curCol++;
				titleRow.createCell(curCol).setCellValue("등록일");
				curCol++;
				titleRow.createCell(curCol).setCellValue("도서등록번호");
				curCol++;
				titleRow.createCell(curCol).setCellValue("구매/기증");
				curCol++;
				titleRow.createCell(curCol).setCellValue("부가기호");
				curCol++;
				titleRow.createCell(curCol).setCellValue("십진분류");
				curCol++;
				titleRow.createCell(curCol).setCellValue("저자기호");
				curCol++;
				titleRow.createCell(curCol).setCellValue("권차기호");
				curCol++;
				titleRow.createCell(curCol).setCellValue("복본기호");
				curCol++;
				titleRow.createCell(curCol).setCellValue("상품구분");
				curCol++;
				if(b==2) {
					titleRow.createCell(curCol).setCellValue("폐기일");
				} else {
					titleRow.createCell(curCol).setCellValue("수정일");
				}
				curCol++;
				titleRow.createCell(curCol).setCellValue("가격");
				curCol++;
				titleRow.createCell(curCol).setCellValue("ISBN10");
				curCol++;
				titleRow.createCell(curCol).setCellValue("카테고리");
				curCol++;
				titleRow.createCell(curCol).setCellValue("태그");
				curCol++;
				titleRow.createCell(curCol).setCellValue("RF ID");
				curCol++;
				titleRow.createCell(curCol).setCellValue("페이지");
				curCol++;
				titleRow.createCell(curCol).setCellValue("도서크기");
				curCol++;
				titleRow.createCell(curCol).setCellValue("이미지링크");
				curCol++;
				titleRow.createCell(curCol).setCellValue("국가");
				curCol++;
				// 최상단 제목 셀의 배경색 회색으로 바꿔주기.
				for(int cs=0; cs<curCol; cs++) {
					titleCell = titleRow.getCell(cs);
					titleCell.setCellStyle(topRowStyle);
				}
				
				for(int i=1; i<=bookHeldList.size(); i++) {
					int j = i-1;
					int curValueCol = 0;
					row = sheet.createRow(i);
					row.createCell(curValueCol).setCellValue(i);
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getTitle());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getWriter());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getPublisher());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(util.getSqlDateToNormalDateStr(bookHeldList.get(j).getPubDate()));
					curValueCol++;
					if(bookHeldList.get(j).getPubDate() != null ) {
						String tempDate = bookHeldList.get(j).getPubDate();
						String pubYear = tempDate.substring(0,4);
						row.createCell(curValueCol).setCellValue(pubYear);
					}
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getIsbn13());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getBookShelf());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(util.getSqlDateToNormalDateStr(bookHeldList.get(j).getRegDate()));
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getLocalIdBarcode());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getPurchasedOrDonated());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getAdditionalCode());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getClassificationCode());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getAuthorCode());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getVolumeCode());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getCopyCode());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getBookOrNot());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(util.getSqlDateToNormalDateStr(bookHeldList.get(j).getEditDate()));
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getPrice());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getIsbn10());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getCategory());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getTag());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getRfId());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getPage());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getBookSize());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getImageLink());
					curValueCol++;
					row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getNameCountry());
					curValueCol++;
				}
			}
			// 도서 전체 목록 끝.
			
			// 등록 도서 목록
			// 등록 도서 목록 끝.
			
			// 폐기 도서 목록
			// 폐기 도서 목록 끝.
			
			// 파일 처리 작업
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
	
	
	
	/** 폐기 도서 목록 엑셀화 페이지 */
	@ResponseBody
	@RequestMapping(value = "/book/discard_book_held_list_to_excel.do", method = RequestMethod.POST)
	public void excelExtractDiscardBookHeldList(Locale locale, Model model,
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
		
		List<BookHeld> bookHeldList = null;
		

		String defaultPath = "/var/packages/Tomcat7/target/src/webapps/downloads/upload/finebook4/excel/libNo"+loginInfo.getIdLibMng();
		String filePath = defaultPath+"/"+"폐기도서목록.xlsx";
		
		FileOutputStream fos = null;
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("폐기도서목록");
		XSSFRow titleRow = null;
		XSSFRow row = null;
		
		//엑셀로 내보내기 위한 준비
		
		try {
			bookHeldList = bookHeldService.getBookHeldDiscardList(bookHeld);
			int curCol = 0;
			titleRow = sheet.createRow(0);
			titleRow.createCell(curCol).setCellValue("번호");
			curCol++;
			titleRow.createCell(curCol).setCellValue("도서명");
			curCol++;
			titleRow.createCell(curCol).setCellValue("저자명");
			curCol++;
			titleRow.createCell(curCol).setCellValue("출판사");
			curCol++;
			titleRow.createCell(curCol).setCellValue("출판일");
			curCol++;
			titleRow.createCell(curCol).setCellValue("출판년도");
			curCol++;
			titleRow.createCell(curCol).setCellValue("가격");
			curCol++;
			titleRow.createCell(curCol).setCellValue("ISBN10");
			curCol++;
			titleRow.createCell(curCol).setCellValue("ISBN13");
			curCol++;
			titleRow.createCell(curCol).setCellValue("카테고리");
			curCol++;
			titleRow.createCell(curCol).setCellValue("서가");
			curCol++;
			titleRow.createCell(curCol).setCellValue("등록일");
			curCol++;
			titleRow.createCell(curCol).setCellValue("폐기일");
			curCol++;
			titleRow.createCell(curCol).setCellValue("도서등록번호");
			curCol++;
			titleRow.createCell(curCol).setCellValue("구매/기증");
			curCol++;
			titleRow.createCell(curCol).setCellValue("부가기호");
			curCol++;
			titleRow.createCell(curCol).setCellValue("십진분류");
			curCol++;
			titleRow.createCell(curCol).setCellValue("저자기호");
			curCol++;
			titleRow.createCell(curCol).setCellValue("권차기호");
			curCol++;
			titleRow.createCell(curCol).setCellValue("복본기호");
			curCol++;
			titleRow.createCell(curCol).setCellValue("태그");
			curCol++;
			titleRow.createCell(curCol).setCellValue("RF ID");
			curCol++;
			titleRow.createCell(curCol).setCellValue("상품구분");
			curCol++;
			titleRow.createCell(curCol).setCellValue("페이지");
			curCol++;
			titleRow.createCell(curCol).setCellValue("도서크기");
			curCol++;
			titleRow.createCell(curCol).setCellValue("이미지링크");
			curCol++;
			titleRow.createCell(curCol).setCellValue("국가");
			curCol++;
			
			for(int i=1; i<=bookHeldList.size(); i++) {
				int j = i-1;
				int curValueCol = 0;
				row = sheet.createRow(i);
				row.createCell(curValueCol).setCellValue(i);
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getTitle());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getWriter());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getPublisher());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getPubDate());
				curValueCol++;
				if(bookHeldList.get(j).getPubDate() != null ) {
					String tempDate = bookHeldList.get(j).getPubDate();
					String pubYear = tempDate.substring(0,4);
					row.createCell(curValueCol).setCellValue(pubYear);
				}
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getPrice());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getIsbn10());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getIsbn13());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getCategory());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getBookShelf());
				curValueCol++;
				String tempRegDate = bookHeldList.get(j).getRegDate();
				if(tempRegDate != null && !"".equals(tempRegDate)) {
					tempRegDate = util.getDateValue(tempRegDate);
				}
				row.createCell(curValueCol).setCellValue(tempRegDate);
				curValueCol++;
				String tempEditDate = bookHeldList.get(j).getEditDate();
				if(tempEditDate != null && !"".equals(tempEditDate)) {
					tempEditDate = util.getDateValue(tempEditDate);
				}
				row.createCell(curValueCol).setCellValue(tempEditDate);
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getLocalIdBarcode());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getPurchasedOrDonated());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getAdditionalCode());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getClassificationCode());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getAuthorCode());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getVolumeCode());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getCopyCode());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getTag());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getRfId());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getBookOrNot());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getPage());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getBookSize());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getImageLink());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(bookHeldList.get(j).getNameCountry());
				curValueCol++;
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
	
	
	/** 도서 목록 엑셀화 페이지 */
	@ResponseBody
	@RequestMapping(value = "/book/external/book_held_list.do/{idLib}/{fromNum}/{count}", method = RequestMethod.GET)
	@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
	public void selectBookHeldListByOtherUrl(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response, @PathVariable("idLib") int idLib,@PathVariable("fromNum") int fromNumb, @PathVariable("count") int count) {
		
		//위 origins 위치에 사용할 도메인을 넣고,
		// http://localhost:8080/book/external/book_held_list.do/${idLib}/${fromNum}/${count}
		// 와 같이 사용하면 된다. idLib 도서관 번호, fromNum 번호 몇번부터, count 몇권 보여줄건지
		
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
//		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
//		if (loginInfo == null) {
//			web.printJsonRt("로그인 후 이용 가능합니다.");
//		}
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		//fromNum 와 count 를 사용하여 가져올 책 개수 구하기.
		bookHeld.setLimitStart(0);
		bookHeld.setListCount(5);
		
		
		List<BookHeld> bookHeldList = null;
		
		
		try {
			bookHeldList= bookHeldService.getBookHeldList(bookHeld);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("bookHeldList", bookHeldList);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());	
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
