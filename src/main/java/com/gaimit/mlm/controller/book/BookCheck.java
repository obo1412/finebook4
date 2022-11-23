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

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
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
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;

import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.BookCheckModel;
import com.gaimit.mlm.service.BookCheckService;
import com.gaimit.mlm.service.BookService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class BookCheck {
	/** log4j 객체 생성 및 사용할 객체 주입받기 */
	//private static Logger logger = LoggerFactory.getLogger(BookCheck.class);
	// --> import study.spring.helper.WebHelper;
	@Autowired
	WebHelper web;
	
	@Autowired
	PageHelper page;
	
	@Autowired
	Util util;
	
	@Autowired
	UploadHelper upload;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	BrwService brwService;
	
	@Autowired
	BookCheckService bookCheckService;
	
	/** 점검 현황 페이지 */
	@RequestMapping(value = "/book_check/book_check_status.do", method = RequestMethod.GET)
	public ModelAndView bookCheckStatus(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		BookCheckModel bookCheck = new BookCheckModel();
		bookCheck.setIdLib(loginInfo.getIdLibMng());
		
		List<BookCheckModel> bookCheckStatList = null;
		
		try {
			bookCheckStatList = bookCheckService.selectBcsList(bookCheck);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("bookCheckStatList", bookCheckStatList);

		return new ModelAndView("book_check/book_check_status");
	}
	
	/** 장서점검 생성 전체 status insertBcs */
	@ResponseBody
	@RequestMapping(value = "/book_check/new_book_check_ok.do", method = RequestMethod.POST)
	public void pickMember(Locale locale, Model model,
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
		
		BookCheckModel bookCheck = new BookCheckModel();
		bookCheck.setIdLib(loginInfo.getIdLibMng());
		
		
		try {
			int wholeCount = bookCheckService.selectWholeCount(bookCheck);
			bookCheck.setWholeCount(wholeCount);
			int brwedCount = bookCheckService.selectBrwedCount(bookCheck);
			bookCheck.setBrwedCount(brwedCount);
			//미점검도서수는, 전체도서에서, 이미 대출중으로 빠진 도서를 체크.
			bookCheck.setUncheckedCount(wholeCount-brwedCount);
			bookCheckService.insertBcs(bookCheck);
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
	
	/** 점검리스트 페이지 진입 */
	@RequestMapping(value = "/book_check/book_check_list.do", method = RequestMethod.GET)
	public ModelAndView bookCheckList(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		//book_check_status id
		int idBcs = web.getInt("id_bcs");
		String checker = web.getString("checker");
		
		BookCheckModel bookCheck = new BookCheckModel();
		bookCheck.setIdBcs(idBcs);
		
		try {
			
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("idBcs", idBcs);
		model.addAttribute("checker", checker);

		return new ModelAndView("book_check/book_check_list");
	}
	
	
	/** 도서 체크 낱권 하나씩 체크 insertBcl */
	@ResponseBody
	@RequestMapping(value = "/book_check/add_new_book_check.do", method = RequestMethod.POST)
	public void addNewBookCheck(Locale locale, Model model,
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
		
		//ip를 가져오는 기능 한개의 wifi에 연결하면 공인ip하나로 뜨기 때문에
		//그냥 각 페이지에 식별자를 주는 것으로 바꿈
		/*String checkIp = util.getUserIp();*/
		String checkIp = web.getString("checkIp");
		
		int idBcs = web.getInt("idBcs");
		String inputBarcode = web.getString("inputBarcodeVal");
		
		BookCheckModel bookCheckItem = new BookCheckModel();
		bookCheckItem.setIdBcs(idBcs);
		bookCheckItem.setInputBarcode(inputBarcode);
		bookCheckItem.setIdLib(loginInfo.getIdLibMng());
		//존재하는 도서인지 체크
		int existCount = 0;
		//도서의 id_book_held 값을 int로 가져옴.
		Integer idBookHeld = null;
		//중복 점검된 도서 체크 변수
		int redupCount = 0;
		//대출도서인지 체크 변수
		int brwedCheck = 0;
		
		//발견된 대출중 도서 매번 발견된 대출중 도서일 경우 ,매번 체크
		int rentedCheckedCount = 0;
		//발견된 대출중 도서는 bcl이 등록된 이 후에 개수를 정확하게 파악가능하므로,
		//스위치를 두어서, 카운트 체크 조건부 실행
		int rentedSwitch = 0;
		
		//해당 도서 점검을 실시한 식별자를 얻기 위함
		bookCheckItem.setCheckIp(checkIp);
		
		try {
			//해당 도서관에 번호가 있는지 없는지 체크
			existCount = bookCheckService.existBookCheckCount(bookCheckItem);
			if(existCount > 0) {
				//도서가 있을 경우
				//idBookHeld 가져오기
				idBookHeld = bookCheckService.selectIdBookHeldByInputBarcode(bookCheckItem);
				bookCheckItem.setIdBookHeld(idBookHeld);
				String barcode = bookCheckService.selectBarcodeByIdBookHeld(bookCheckItem);
				bookCheckItem.setLocalIdBarcode(barcode);
				//대출중인지 체크
				brwedCheck = bookCheckService.selectBrwedCheck(bookCheckItem);
				if(brwedCheck > 0) {
					//대출중이라는 뜻.
					bookCheckItem.setCheckResult("대출중도서");
					//발견된 대출중 도서는 카운트+1 하는게 조건이 더 복잡
					//그러므로, 그냥 매번 카운트를 구해서 대입.
					//발견된 대출중 스위치 온
					rentedSwitch++;
					
				} else {
					//bookCheckList에 이미 데이터가 있는지 체크
					redupCount = bookCheckService.selectRedupCountInBcl(bookCheckItem);
					if(redupCount == 0) {
						//중복이 없다는 뜻.
						bookCheckItem.setCheckResult("확인");
						//정상적인 점검 확인 카운트 + 1
						bookCheckService.updateConfirmCount(bookCheckItem);
						//미점검 도서 카운트 -1 정상확인 됐으면, 미점검 -1
						bookCheckService.updateUncheckedCountMinus(bookCheckItem);
					} else {
						//중복이 존재할 경우.
						bookCheckItem.setCheckResult("중복점검도서");
						//중복도서 카운트 +1
						bookCheckService.updateRedupCount(bookCheckItem);
					}
				}
			} else {
				//도서가 없을 경우
				bookCheckItem.setCheckResult("미등록도서");
				bookCheckService.updateUnregCount(bookCheckItem);
			}
			bookCheckService.insertBcl(bookCheckItem);
			//bookCheckItem 에 idBcl 값 들어간다.
			//도서 등록 후, 무조건 점검횟수 +1
			bookCheckService.updateCheckedCount(bookCheckItem);
			bookCheckItem = bookCheckService.selectBclItemByInputBarcode(bookCheckItem);
			
			//중간에 발견된 대출중 도서 스위치가 온되면 숫자 점검.
			if(rentedSwitch>0) {
				rentedCheckedCount = bookCheckService.selectRentedBookCount(bookCheckItem);
				bookCheckItem.setRentedCheckedCount(rentedCheckedCount);
				bookCheckService.updateRentedCheckedCount(bookCheckItem);
			}
			
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("bookCheckItem", bookCheckItem);
		data.put("rentedCheckedCount", rentedCheckedCount);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	/** 전체 도서 호출 */
	@ResponseBody
	@RequestMapping(value = "/book_check/book_held_list.do", method = RequestMethod.GET)
	public void bookHeldList(Locale locale, Model model,
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
		
		/*int idBcs = web.getInt("idBcs");
		String inputBarcode = web.getString("inputBarcode");*/
		
		BookCheckModel bookCheck = new BookCheckModel();
		bookCheck.setIdLib(loginInfo.getIdLibMng());
		
		List<BookCheckModel> bookHeldList = null;
		
		try {
			bookHeldList = bookCheckService.selectBookHeldList(bookCheck);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
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
	
	/** 이전에 체크된 도서 리스트 호출하기 */
	@ResponseBody
	@RequestMapping(value = "/book_check/checked_book_list.do", method = RequestMethod.GET)
	public void checkedBookList(Locale locale, Model model,
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
		
		int idBcs = web.getInt("idBcs");
		
		BookCheckModel bookCheck = new BookCheckModel();
		bookCheck.setIdLib(loginInfo.getIdLibMng());
		bookCheck.setIdBcs(idBcs);
		
		List<BookCheckModel> checkedBookList = null;
		
		try {
			checkedBookList = bookCheckService.selectBclList(bookCheck);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("checkedBookList", checkedBookList);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	/** 도서 점검 현황 정보 호출하기 */
	@ResponseBody
	@RequestMapping(value = "/book_check/current_checked_book_status.do", method = RequestMethod.GET)
	public void getCheckStatus(Locale locale, Model model,
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
		
		int idBcs = web.getInt("idBcs");
		
		BookCheckModel bookCheck = new BookCheckModel();
		bookCheck.setIdLib(loginInfo.getIdLibMng());
		bookCheck.setIdBcs(idBcs);
		
		try {
			bookCheck = bookCheckService.selectCurrentBookCheckStatus(bookCheck);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("bookCheck", bookCheck);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	
	
	/** 각 타입별 도서 목록 호출하기, 확인, 중복,대출중,미등록, 미점검 */
	@ResponseBody
	@RequestMapping(value = "/book_check/by_type_book_list.do", method = RequestMethod.GET)
	public void overallBookListByType(Locale locale, Model model,
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
		
		int idBcs = web.getInt("idBcs");
		//데이터 타입에 따라 어떤 목록을 불러올지 결정됨.
		String dataType = web.getString("type");
		
		BookCheckModel bookCheck = new BookCheckModel();
		bookCheck.setIdLib(loginInfo.getIdLibMng());
		bookCheck.setIdBcs(idBcs);
		
		List<BookCheckModel> dataList = null;
		
		try {
			switch(dataType) {
			case "tbodyNormalBook":
				bookCheck.setCheckResult("확인");
				dataList = bookCheckService.selectNormalBookList(bookCheck);
				break;
			case "tbodyUncheckedBook":
				//점검 아직 안한 도서 목록.
				dataList = bookCheckService.selectUncheckedBookExceptRentedBook(bookCheck);
				break;
			case "tbodyDoubleCheckedBook":
				bookCheck.setCheckResult("중복점검도서");
				dataList = bookCheckService.selectNormalBookList(bookCheck);
				break;
			case "tbodyRentedBook":
				bookCheck.setCheckResult("대출중도서");
				dataList = bookCheckService.selectNormalBookList(bookCheck);
				break;
			case "tbodyUnregisteredBook":
				bookCheck.setCheckResult("미등록도서");
				dataList = bookCheckService.selectNormalBookList(bookCheck);
				break;
			case "tbodyWholeBrwedBook":
				dataList = bookCheckService.selectWholeBrwedBookList(bookCheck);
				break;
			}
			
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("dataList", dataList);
		
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
	@RequestMapping(value = "/book_check/book_check_list_to_excel.do", method = RequestMethod.POST)
	public void excelExtractBookCheckList(Locale locale, Model model,
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
		
		int idBcs = web.getInt("idBcs");
		
		BookCheckModel bookCheck = new BookCheckModel();
		bookCheck.setIdLib(loginInfo.getIdLibMng());
		bookCheck.setIdBcs(idBcs);
		
		List<BookCheckModel> bookCheckList = null;
		
//		BookHeld bookHeld = new BookHeld();
//		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		//List<BookHeld> bookHeldList = null;
		
		
		String filePath = null;
		
		FileOutputStream fos = null;
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		//셀스타일을 위한 객체
		XSSFSheet sheetState = workbook.createSheet("점검현황");
		sheetState.setColumnWidth(0, 7500);
		//sheetState.autoSizeColumn(0);
		
		XSSFSheet sheetWhole = workbook.createSheet("점검전체목록");
		XSSFSheet sheetConfirm = workbook.createSheet("정상도서");
		XSSFSheet sheetDouble = workbook.createSheet("중복도서");
		XSSFSheet sheetRented = workbook.createSheet("발견된대출중도서");
		XSSFSheet sheetbrwed = workbook.createSheet("전체대출중도서");
		XSSFSheet sheetUnreg = workbook.createSheet("미등록도서");
		XSSFSheet sheetUnchecked = workbook.createSheet("미점검도서");
		
		//엑셀로 내보내기 위한 준비
		
		try {
			bookCheck = bookCheckService.selectCurrentBookCheckStatus(bookCheck);
			//제목에 들어갈 날짜 처리.
			String checkDate = bookCheck.getCheckDate();

			SimpleDateFormat nowForm = new SimpleDateFormat("yyyy-MM-dd");
			Date dateDate = nowForm.parse(checkDate);

			nowForm = new SimpleDateFormat("yyyyMMdd");
			String dateStr = nowForm.format(dateDate);

			String defaultPath = "/var/packages/Tomcat7/target/src/webapps/downloads/upload/finebook4/excel/libNo"+loginInfo.getIdLibMng();
			filePath = defaultPath+"/"+"장서점검_"+dateStr+".xlsx";
			
			//도서 점검 현황
			writeExcelDataBookCheckState(sheetState, bookCheck, workbook);
			
			//전체 점검 목록
			bookCheckList = bookCheckService.selectBclList(bookCheck);
			writeExcelDataByList(sheetWhole, bookCheckList);
			
			bookCheck.setCheckResult("확인");
			bookCheckList = bookCheckService.selectNormalBookList(bookCheck);
			writeExcelDataByList(sheetConfirm, bookCheckList);
			
			bookCheck.setCheckResult("중복점검도서");
			bookCheckList = bookCheckService.selectNormalBookList(bookCheck);
			writeExcelDataByList(sheetDouble, bookCheckList);
			
			bookCheck.setCheckResult("대출중도서");
			bookCheckList = bookCheckService.selectNormalBookList(bookCheck);
			writeExcelDataByList(sheetRented, bookCheckList);
			
			bookCheck.setCheckResult("미등록도서");
			bookCheckList = bookCheckService.selectNormalBookList(bookCheck);
			writeExcelDataByList(sheetUnreg, bookCheckList);
			
			//대출중인 전체 도서
			bookCheckList = bookCheckService.selectWholeBrwedBookList(bookCheck);
			writeExcelDataByList(sheetbrwed, bookCheckList);
			
			//대출중을 제외한 미점검 도서
			bookCheckList = bookCheckService.selectUncheckedBookExceptRentedBook(bookCheck);
			writeExcelDataByList(sheetUnchecked, bookCheckList);
			
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
	
	
	
	//총점검 현황 생성 함수
	public void writeExcelDataBookCheckState(XSSFSheet sheet, BookCheckModel bookCheckState, XSSFWorkbook workbook) {
		XSSFRow dataRow = null;
		Cell cell = null;
		int curCol = 0;
		
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		
		//기본 셀스타일(테투리선)에서 배경색 추가
		CellStyle anotherStyle = workbook.createCellStyle();
		anotherStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.index);
		anotherStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		anotherStyle.setBorderTop(BorderStyle.THIN);
		anotherStyle.setBorderBottom(BorderStyle.THIN);
		anotherStyle.setBorderLeft(BorderStyle.THIN);
		anotherStyle.setBorderRight(BorderStyle.THIN);
		System.out.println("another "+anotherStyle);
		System.out.println("default "+cellStyle);
		
		dataRow = sheet.createRow(0);
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(anotherStyle);
		cell.setCellValue("전체도서수");
		curCol++;
		cell = null;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("");
		curCol++;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(bookCheckState.getWholeCount());
		
		dataRow = sheet.createRow(1);
		curCol = 0;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(anotherStyle);
		cell.setCellValue("점검횟수");
		curCol++;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(bookCheckState.getCheckedCount());
		curCol++;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("");
		
		dataRow = sheet.createRow(2);
		curCol = 0;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(anotherStyle);
		cell.setCellValue("정상확인도서");
		curCol++;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("");
		curCol++;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(bookCheckState.getConfirmCount());
		
		dataRow = sheet.createRow(3);
		curCol = 0;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(anotherStyle);
		cell.setCellValue("미확인 도서");
		curCol++;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("");
		curCol++;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(bookCheckState.getUncheckedCount());
		
		dataRow = sheet.createRow(4);
		curCol = 0;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(anotherStyle);
		cell.setCellValue("발견된대출중/전체대출중");
		curCol++;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(bookCheckState.getRentedCheckedCount());
		curCol++;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(bookCheckState.getBrwedCount());
		
		dataRow = sheet.createRow(5);
		curCol = 0;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(anotherStyle);
		cell.setCellValue("발견된 중복바코드");
		curCol++;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(bookCheckState.getRedupCount());
		curCol++;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("");
		
		dataRow = sheet.createRow(6);
		curCol = 0;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(anotherStyle);
		cell.setCellValue("발견된 미등록도서");
		curCol++;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(bookCheckState.getUnregCount());
		curCol++;
		cell = dataRow.createCell(curCol);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("");
		
	}
	
	//각 리스트 도서 점검
	public void writeExcelDataByList(XSSFSheet sheet, List<BookCheckModel> bookCheckList) {
		XSSFRow titleRow = null;
		XSSFRow dataRow = null;
		
		int curCol = 0;
		titleRow = sheet.createRow(0);
		titleRow.createCell(curCol).setCellValue("번호");
		curCol++;
		titleRow.createCell(curCol).setCellValue("점검결과");
		curCol++;
		titleRow.createCell(curCol).setCellValue("점검번호");
		curCol++;
		titleRow.createCell(curCol).setCellValue("바코드번호");
		curCol++;
		titleRow.createCell(curCol).setCellValue("서가");
		curCol++;
		titleRow.createCell(curCol).setCellValue("도서명");
		curCol++;
		titleRow.createCell(curCol).setCellValue("저자명");
		curCol++;
		titleRow.createCell(curCol).setCellValue("출판사명");
		curCol++;
		titleRow.createCell(curCol).setCellValue("점검자");
		
		for(int i=1; i<=bookCheckList.size(); i++) {
			int j = i-1;
			int curValueCol = 0;
			dataRow = sheet.createRow(i);
			dataRow.createCell(curValueCol).setCellValue(i);
			curValueCol++;
			dataRow.createCell(curValueCol).setCellValue(bookCheckList.get(j).getCheckResult());
			curValueCol++;
			dataRow.createCell(curValueCol).setCellValue(bookCheckList.get(j).getInputBarcode());
			curValueCol++;
			dataRow.createCell(curValueCol).setCellValue(bookCheckList.get(j).getLocalIdBarcode());
			curValueCol++;
			dataRow.createCell(curValueCol).setCellValue(bookCheckList.get(j).getBookShelf());
			curValueCol++;
			dataRow.createCell(curValueCol).setCellValue(bookCheckList.get(j).getTitle());
			curValueCol++;
			dataRow.createCell(curValueCol).setCellValue(bookCheckList.get(j).getWriter());
			curValueCol++;
			dataRow.createCell(curValueCol).setCellValue(bookCheckList.get(j).getPublisher());
			curValueCol++;
			dataRow.createCell(curValueCol).setCellValue(bookCheckList.get(j).getCheckIp());
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/** bcs 삭제 호출하기 */
	@ResponseBody
	@RequestMapping(value = "/book_check/delete_book_check_status.do", method = RequestMethod.POST)
	public void deleteBookCheckStatus(Locale locale, Model model,
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
		
		int idBcs = web.getInt("receiveIdBcsVal");
		
		BookCheckModel bookCheck = new BookCheckModel();
		bookCheck.setIdLib(loginInfo.getIdLibMng());
		bookCheck.setIdBcs(idBcs);
		
		try {
			bookCheck = bookCheckService.selectCurrentBookCheckStatus(bookCheck);
			//제목에 들어갈 날짜 처리.
			String checkDate = bookCheck.getCheckDate();

			SimpleDateFormat nowForm = new SimpleDateFormat("yyyy-MM-dd");
			Date dateDate = nowForm.parse(checkDate);

			nowForm = new SimpleDateFormat("yyyyMMdd");
			String dateStr = nowForm.format(dateDate);
			
			String defaultPath = "/var/packages/Tomcat7/target/src/webapps/downloads/upload/finebook4/excel/libNo"+loginInfo.getIdLibMng();
			String filePath = defaultPath+"/"+"장서점검_"+dateStr+".xlsx";
			upload.removeFile(filePath);
			
			//bcs를 참조하는 book Check List를 삭제한다. 
			bookCheckService.deleteBclByBcs(bookCheck);
			//bcs 삭제
			bookCheckService.deleteBcs(bookCheck);
			
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
