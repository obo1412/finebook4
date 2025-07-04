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

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.controller.FrequentlyFunction;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class Statistics {
	/** log4j 객체 생성 및 사용할 객체 주입받기 */
	private static final Logger logger = LoggerFactory.getLogger(Statistics.class);
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
	MemberService memberService;
	
	@Autowired
	BrwService brwService;
	
	@Autowired
	FrequentlyFunction frequentlyFunction;
	
	/** 도서 목록 페이지 */
	@RequestMapping(value = "/stsc/statistics.do", method = RequestMethod.GET)
	public ModelAndView statistics(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		
		int wholeCount = 0;
		List<BookHeld> dataList = null;
		
		try {
			wholeCount = bookHeldService.selectBookCountForPage(bookHeld);
			dataList = bookHeldService.selectOrganizationTableByClassCode(bookHeld);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		model.addAttribute("wholeCount", wholeCount);
		model.addAttribute("dataList", dataList);
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		
		return new ModelAndView("statistics/statistics");
	}
	
	@ResponseBody
	@RequestMapping(value="/stsc/select_brw_rtn_list", method = RequestMethod.POST)
	public void selectBrwRtnList(Locale locale, Model model,
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
		
		String pickDate = web.getString("pickDate","");
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(loginInfo.getIdLibMng());
		
		List<Borrow> brwRtnList = null;
		
		try {
			//pickDate가 있다면, Date형식에 지정날짜를 넣고 borrow에 set
			if(pickDate!=null&&!"".equals(pickDate)) {
				borrow.setPickDateBrw(pickDate);
			}
			
			brwRtnList = brwService.selectBrwRtnList(borrow);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("brwRtnList", brwRtnList);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	/* 엑셀 다운로드 */
	@ResponseBody
	@RequestMapping(value = "/stsc/statistics_to_excel.do", method = RequestMethod.POST)
	public void excelExtractStatistics(Locale locale, Model model,
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
		
		String targetYear = web.getString("targetYear");
		String tCatpion = web.getString("tcaption");
		int dataRow = web.getInt("dataRowVal");
		int dataCol = web.getInt("dataColVal");
		
		String dataListStr = web.getString("dataListStr");
		
		String[][] dataList = util.stringTo2DArray(dataListStr, dataRow, dataCol);
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		bookHeld.setRegDate(targetYear);
		
//		List<BookHeld> bookHeldList = null;
		
		Date now = new Date();
		SimpleDateFormat nowForm = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowStr = nowForm.format(now);
		String defaultPath = "/var/packages/Tomcat7/target/src/webapps/downloads/upload/finebook4/excel/libNo"+loginInfo.getIdLibMng();
		String filePath = defaultPath+"/"+tCatpion+"_"+nowStr+".xlsx";
		
		FileOutputStream fos = null;
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow row = null;
		
		//엑셀로 내보내기 위한 준비
		
		try {
			
			for(int i=0; i<dataRow; i++) {
				row = sheet.createRow(i);
				for(int j=0; j<dataCol; j++) {
					row.createCell(j).setCellValue(dataList[i][j]);
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
	
	/** 라벨출력 페이지 */
	@RequestMapping(value = "/statistics_print_report.do", method = RequestMethod.POST)
	public ModelAndView printStatistics(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		String nameLib = loginInfo.getNameLib();
		
		String pickDate = web.getString("pickDate");
		
		String targetYear = web.getString("targetYear");
		String tCaption = web.getString("tcaption");
		if(tCaption == null) {
			return web.redirect(null, "보고서 양식이 선택되지 않았습니다.");
		}
		int dataRow = web.getInt("dataRowVal");
		int dataCol = web.getInt("dataColVal");
		
		String dataListStr = web.getString("dataListStr");
		
		String[][] dataList = null;
		
		// 데이터 값이 넘어왔을 경우.
		if(dataRow > 1) {
			dataList = util.stringTo2DArray(dataListStr, dataRow, dataCol);
		}
		
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		bookHeld.setRegDate(pickDate);
		bookHeld.setEditDate(pickDate);
		
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		member.setRegDate(pickDate);
		member.setEditDate(pickDate);
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(loginInfo.getIdLibMng());
		borrow.setPickDateBrw(pickDate);
		List<Borrow> brwRtnList = null;
		
		// 오늘 대출 권수
		int brwCount = 0;
		// 오늘 반납 권수
		int rtnCount = 0;
		// 대출 총 권수
		int totalBrw = 0;
		// 연체 총 권수
		int totalOverDue = 0;
		// 신규등록 도서
		int newBookCount = 0;
		// 폐기 도서수
		int discardBookCount = 0;
		// 현재 도서 총수
		int totalBookCount = 0;
		// 신규등록회원
		int newMemberCount = 0;
		// 제적회원
		int discardMemberCount = 0;
		// 총회원수
		int totalMemberCount = 0;
		
		try {
			brwRtnList = brwService.selectBrwRtnList(borrow);
			for(int i=0; i<brwRtnList.size(); i++) {
				// 오늘 빌린도서라면 대출도서 카운팅
				String thisStartDate = brwRtnList.get(i).getStartDateBrw();
				String thisEndDate = brwRtnList.get(i).getEndDateBrw();
				
				// 타겟날짜의 대출도서 카운팅
				if(util.isToday(thisStartDate, pickDate)) {
					brwCount = brwCount+1;
				}
				// 다켓날짜의 반납도서 카운팅
				if(util.isToday(thisEndDate, pickDate)) {
					rtnCount = rtnCount+1;
				}
			}
			
			// 대출여부에 관계없는 데이터 불러오기
			// 대출 총권수 불러오기
			totalBrw = brwService.selectBorrowListCount(borrow);
			// 연체 총권수 불러오기
			totalOverDue = brwService.selectOverDueCountByLib(borrow);
			// 도서 총권수 불러오기
			totalBookCount = bookHeldService.selectBookCountForPage(bookHeld);
			// 타겟 날짜에 등록된 도서수
			newBookCount = bookHeldService.selectBookCountByRegDatePick(bookHeld);
			// 타겟 날짜에 폐기된 도서수
			discardBookCount = bookHeldService.selectBookDiscardCountByEditDatePick(bookHeld);
			
			// 회원 총수
			totalMemberCount = memberService.getMemberCount(member);
			// 타겟날짜 신규등록 회원수
			newMemberCount = memberService.selectNewMemberCountByRegDatePick(member);
			// 타겟날짜 제적 회원수
			discardMemberCount = memberService.selectInactiveMemberCountByRegDatePick(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		model.addAttribute("nameLib", nameLib);
		model.addAttribute("targetYear", targetYear);
		model.addAttribute("tCaption", tCaption);
		model.addAttribute("dataRow", dataRow);
		model.addAttribute("dataCol", dataCol);
		model.addAttribute("dataList", dataList);
		model.addAttribute("brwCount", brwCount);
		model.addAttribute("rtnCount", rtnCount);
		model.addAttribute("pickDate", pickDate);
		model.addAttribute("totalBrw", totalBrw);
		model.addAttribute("totalOverDue", totalOverDue);
		model.addAttribute("totalBookCount", totalBookCount);
		model.addAttribute("newBookCount", newBookCount);
		model.addAttribute("discardBookCount", discardBookCount);
		model.addAttribute("totalMemberCount", totalMemberCount);
		model.addAttribute("newMemberCount", newMemberCount);
		model.addAttribute("discardMemberCount", discardMemberCount);
		
		String urlTail = "";
		if(tCaption.equals("일일대출반납보고서")) {
			urlTail = "dailyBrwReturn";
		}
		
		return new ModelAndView("statistics/statistics_print_report_"+urlTail);
	}
	
	
	/* 기간별 대출권수 + 회원수 + 도서목록 엑셀화 후 다운로드까지 */
	@ResponseBody
	@RequestMapping(value = "/stsc/brw_and_member_by_date.do", method = RequestMethod.GET)
	public void BrwAndMemberStatisticsByDate(Locale locale, Model model,
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
		
		String staDateStart = web.getString("staDateStart");
		String staDateEnd = web.getString("staDateEnd");
		
		logger.debug(staDateEnd);
		
		String defaultPath =
			"/var/packages/Tomcat7/target/src/webapps/downloads/upload/finebook4/excel/";
		String fileName = loginInfo.getNameLib()+"_기간별대출통계.xlsx";
		
//		String downloadPath = "filesMapping/upload/finebook4/excel/"+fileName;
		String downloadPath = defaultPath+fileName;
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(loginInfo.getIdLibMng());
		borrow.setStartDateBrw(staDateStart);
		borrow.setEndDateBrw(staDateEnd);
		
		List<Borrow> dataList = null;
		
		/*
		 * 
		 * Date now = new Date(); SimpleDateFormat nowForm = new
		 * SimpleDateFormat("yyyyMMddHHmmss"); String nowStr = nowForm.format(now);
		 * 
		 * String filePath =
		 * defaultPath+"/"+tCatpion+"_"+nowStr+".xlsx";
		 * 
		 * 
		 */
		
		//엑셀로 내보내기 위한 준비
		
		try {
			FileOutputStream fos = null;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("기간별대출내역");
			XSSFRow row = null;
			
			dataList = brwService.selectStatisticsBrwAndMemberByDate(borrow);
			
			int memberCount = brwService.selectStatisticsMemberCountByDate(borrow);
			
			if(dataList.size()==0) {
				web.printJsonRt("통계 데이터 값이 없습니다.");
			} else {
				File uploadDirFile = new File(defaultPath);
				
				if(!uploadDirFile.exists()) {
					uploadDirFile.mkdirs(); 
				}
				
				for(int s=0; s<3; s++) {
					if(s==0 ) {
						row = sheet.createRow(0);
						row.createCell(0).setCellValue(staDateStart+" ~ "+staDateEnd);
						row.createCell(2).setCellValue("대출권수:");
						row.createCell(3).setCellValue(String.valueOf(dataList.size()));
						row.createCell(5).setCellValue("이용자수:");
						row.createCell(6).setCellValue(String.valueOf(memberCount));
						
						//아래 별도 함수선언, list를 2차원 배열 값으로 변경
						String[][] dataArr = dataToArray(dataList);
						//dataArr는 순서대로 들어가면 됨.
						for(int i=0; i<dataArr.length; i++) {
							row = sheet.createRow(i+2);
							for(int j=0; j<dataArr[0].length; j++) {
								row.createCell(j).setCellValue(dataArr[i][j]);
							}
						}
					} else if(s==1) {
						dataList = brwService.selectStatisticsBrwMemberRanking10ByDate(borrow);
						sheet = workbook.createSheet("다독자 순위");
						row = sheet.createRow(0);
						row.createCell(0).setCellValue(staDateStart+" ~ "+staDateEnd);
						
						if(dataList.size()==0) {
							// 자료가 없을 땐 아무것도 하지 않는다.
						} else {
							//아래 별도 함수선언, list를 2차원 배열 값으로 변경
							String[][] dataArr = memberRankingToArray(dataList);
							//dataArr는 순서대로 들어가면 됨.
							for(int i=0; i<dataArr.length; i++) {
								row = sheet.createRow(i+2);
								for(int j=0; j<dataArr[0].length; j++) {
									row.createCell(j).setCellValue(dataArr[i][j]);
								}
							}
						}
					} else if(s==2) {
						dataList = brwService.selectStatisticsBrwBookRanking10ByDate(borrow);
						sheet = workbook.createSheet("다대출도서 순위");
						row = sheet.createRow(0);
						row.createCell(0).setCellValue(staDateStart+" ~ "+staDateEnd);
						
						if(dataList.size()==0) {
							// 자료가 없을땐 아무것도 하지 않는다.
						} else {
							//아래 별도 함수선언, list를 2차원 배열 값으로 변경
							String[][] dataArr = bookRankingToArray(dataList);
							//dataArr는 순서대로 들어가면 됨.
							for(int i=0; i<dataArr.length; i++) {
								row = sheet.createRow(i+2);
								for(int j=0; j<dataArr[0].length; j++) {
									row.createCell(j).setCellValue(dataArr[i][j]);
								}
							}
						}
					}
				}
				
				fos = new FileOutputStream(defaultPath+"/"+fileName);
				workbook.write(fos);
				workbook.close();
				fos.close();
			}
			
			
			/*
			 * for(int i=0; i<dataRow; i++) { row = sheet.createRow(i); for(int j=0;
			 * j<dataCol; j++) { row.createCell(j).setCellValue(dataList[i][j]); } }
			 * 
			 *
			 */
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("downloadPath", downloadPath);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	
	/**
	 * 기간별 대출권수, 이용자수 배열로 만들어주는 함수
	 * @param dataList
	 * @return
	 */
	public String[][] dataToArray(List<Borrow> list) {
		if(list.size()==0) {
			return null;
		}
		
		String[][] result = new String[list.size()+1][8];
		
		result[0][0] = "번호";
		result[0][1] = "도서명";
		result[0][2] = "저자명";
		result[0][3] = "도서등록번호";
		result[0][4] = "회원분류";
		result[0][5] = "회원명";
		result[0][6] = "대출일";
		result[0][7] = "반납일";
		
		for(int i=0; i<list.size(); i++) {
			result[i+1][0] = String.valueOf(i+1);
			result[i+1][1] = list.get(i).getTitle();
			result[i+1][2] = list.get(i).getWriter();
			result[i+1][3] = list.get(i).getLocalIdBarcode();
			result[i+1][4] = list.get(i).getClassName();
			result[i+1][5] = list.get(i).getName();
			result[i+1][6] = util.getSqlDateToNormalDateStr(list.get(i).getStartDateBrw());
			result[i+1][7] = util.getSqlDateToNormalDateStr(list.get(i).getEndDateBrw());
		}
		
		return result;
	}
	
	/**
	 * 기간별 다독자 배열로 만들어주는 함수
	 * @param dataList
	 * @return
	 */
	public String[][] memberRankingToArray(List<Borrow> list) {
		if(list.size()==0) {
			return null;
		}
		
		String[][] result = new String[list.size()+1][5];
		
		result[0][0] = "순위";
		result[0][1] = "대출권수";
		result[0][2] = "회원명";
		result[0][3] = "연락처";
		result[0][4] = "회원등록번호";
		
		for(int i=0; i<list.size(); i++) {
			result[i+1][0] = String.valueOf(i+1);
			result[i+1][1] = String.valueOf(list.get(i).getCount());
			result[i+1][2] = list.get(i).getName();
			result[i+1][3] = list.get(i).getPhone();
			result[i+1][4] = list.get(i).getBarcodeMbr();
		}
		
		return result;
	}
	
	/**
	 * 기간별 다대출 도서 배열로 만들어주는 함수
	 * @param dataList
	 * @return
	 */
	public String[][] bookRankingToArray(List<Borrow> list) {
		if(list.size()==0) {
			return null;
		}
		
		String[][] result = new String[list.size()+1][5];
		
		result[0][0] = "순위";
		result[0][1] = "대출회수";
		result[0][2] = "도서명";
		result[0][3] = "저자명";
		result[0][4] = "도서등록번호";
		
		for(int i=0; i<list.size(); i++) {
			result[i+1][0] = String.valueOf(i+1);
			result[i+1][1] = String.valueOf(list.get(i).getCount());
			result[i+1][2] = list.get(i).getTitle();
			result[i+1][3] = list.get(i).getWriter();
			result[i+1][4] = list.get(i).getLocalIdBarcode();
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
