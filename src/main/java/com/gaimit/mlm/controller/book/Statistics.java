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
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.ManagerService;

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
		
		
		try {
			
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
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
		
		String targetYear = web.getString("targetYear");
		String tCaption = web.getString("tcaption");
		if(tCaption == null) {
			return web.redirect(null, "보고서 양식이 선택되지 않았습니다.");
		}
		int dataRow = web.getInt("dataRowVal");
		int dataCol = web.getInt("dataColVal");
		
		String dataListStr = web.getString("dataListStr");
		
		String[][] dataList = util.stringTo2DArray(dataListStr, dataRow, dataCol);
		
		
		model.addAttribute("nameLib", nameLib);
		model.addAttribute("targetYear", targetYear);
		model.addAttribute("tCaption", tCaption);
		model.addAttribute("dataRow", dataRow);
		model.addAttribute("dataCol", dataCol);
		model.addAttribute("dataList", dataList);
		
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
			XSSFSheet sheet = workbook.createSheet();
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
				
				row = sheet.createRow(0);
				row.createCell(0).setCellValue(staDateStart+"~"+staDateEnd);
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
		
		String[][] result = new String[list.size()+1][7];
		
		result[0][0] = "번호";
		result[0][1] = "도서명";
		result[0][2] = "저자명";
		result[0][3] = "도서등록번호";
		result[0][4] = "회원명";
		result[0][5] = "대출일";
		result[0][6] = "반납일";
		
		for(int i=0; i<list.size(); i++) {
			result[i+1][0] = String.valueOf(i+1);
			result[i+1][1] = list.get(i).getTitle();
			result[i+1][2] = list.get(i).getWriter();
			result[i+1][3] = list.get(i).getLocalIdBarcode();
			result[i+1][4] = list.get(i).getName();
			result[i+1][5] = list.get(i).getStartDateBrw();
			result[i+1][6] = list.get(i).getEndDateBrw();
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
