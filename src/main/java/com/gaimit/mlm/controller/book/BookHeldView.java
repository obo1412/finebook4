package com.gaimit.mlm.controller.book;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
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
import com.gaimit.helper.FileInfo;
import com.gaimit.helper.PageHelper;
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;

import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.controller.FrequentlyFunction;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.model.Library;
import com.gaimit.mlm.service.BbsFileService;
import com.gaimit.mlm.service.BookCheckService;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.LibraryService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class BookHeldView {
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
	UploadHelper upload;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	BookHeldService bookHeldService;
	
	@Autowired
	BbsFileService bbsFileService;
	
	@Autowired
	LibraryService libraryService;
	
	@Autowired
	FrequentlyFunction frequentlyFunction;
	
	@Autowired
	BrwService brwService;
	
	@Autowired
	BookCheckService bookCheckService;
	
	/** 도서 상세 정보 */
	@RequestMapping(value = "/book/book_held_view.do", method = RequestMethod.GET)
	public ModelAndView viewBook(Locale locale, Model model) {
		
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
		
		String barcodeBook = web.getString("localIdBarcode");
		int bookHeldId = web.getInt("bookHeldId");
		
		// 파라미터를 저장할 Beans
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		bookHeld.setLocalIdBarcode(barcodeBook);
		bookHeld.setId(bookHeldId);
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		BookHeld bookHeldItem = new BookHeld();
		try {
			bookHeldItem = bookHeldService.getBookHelditem(bookHeld);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("bookHeldItem", bookHeldItem);
		
		return new ModelAndView("book/book_held_view");
	}
	
	/** 회원 도서 조회만 가능 상세 정보 */
	@RequestMapping(value = "/blook_details.do", method = RequestMethod.GET)
	public ModelAndView viewBookMember(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		//파라미터에서 값을 받아옴
		int idLib = web.getInt("lib");
		String stringKeyLib = web.getString("skl");
		
		//lib와 skl키로 도서관이 맞는지 판별
		Library library = new Library();
		library.setIdLib(idLib);
		library.setStringKeyLib(stringKeyLib);
		
		/*String nameLib = null;*/
		
		try {
			library = libraryService.selectLibItemByKeys(library);
			idLib = library.getIdLib();
			/*nameLib = library.getNameLib();*/
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		//lib와 skl키로 도서관이 맞는지 판별
		
		String barcodeBook = web.getString("localIdBarcode");
		int bookHeldId = web.getInt("bookHeldId");
		
		// 파라미터를 저장할 Beans
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		bookHeld.setLocalIdBarcode(barcodeBook);
		bookHeld.setId(bookHeldId);
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		BookHeld bookHeldItem = new BookHeld();
		try {
			bookHeldItem = bookHeldService.getBookHelditem(bookHeld);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("bookHeldItem", bookHeldItem);
		
		return new ModelAndView("book/for_guest/book_view_guest");
	}
	
	/** 도서 정보 수정 */
	@RequestMapping(value = "/book/book_held_edit.do", method = RequestMethod.GET)
	public ModelAndView editBook(Locale locale, Model model) {
		
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
		
		String barcodeBook = web.getString("localIdBarcode");
		int bookHeldId = web.getInt("bookHeldId");
		
		// 파라미터를 저장할 Beans
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		bookHeld.setId(bookHeldId);
		bookHeld.setLocalIdBarcode(barcodeBook);
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(idLib);
		borrow.setBookHeldId(bookHeldId);
		
		List<Borrow> brwLog = null;
		
		//장서점검 기록이 있는지 체크
		int bookCheckCount = 0;
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		BookHeld bookHeldItem = new BookHeld();
		try {
			bookHeldItem = bookHeldService.getBookHelditem(bookHeld);
			brwLog = brwService.selectBookBorrowLogByBookHeldId(borrow);
			bookCheckCount = bookCheckService.selectBookCheckRecordByBookId(bookHeldId);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("bookHeldItem", bookHeldItem);
		model.addAttribute("brwLog", brwLog);
		model.addAttribute("bookCheckCount", bookCheckCount);
		
		return new ModelAndView("book/book_held_edit");
	}
	
	@RequestMapping(value = "/book/book_held_edit_ok.do", method = RequestMethod.POST)
	public ModelAndView editBookOk(Locale locale, Model model) {
		
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
		
		int idBookHeld = web.getInt("id");
		String title = web.getString("title");
		String author = web.getString("author");
		String publisher = web.getString("publisher");
		String pubDate = web.getString("pubDate");
		String price = web.getString("price");
		String isbn13 = web.getString("isbn13");
		String isbn10 = web.getString("isbn10");
		String category = web.getString("category");
		String bookShelf = web.getString("bookShelf");
		String barcodeBook = web.getString("localIdBarcode");
		
		//등록번호에서 숫자만 추출.
		int sortingIndex = util.numExtract(barcodeBook);
		
		int purchasedOrDonated = web.getInt("purchasedOrDonated");
		String additionalCode = web.getString("additionalCode");
		String classificationCode = web.getString("classificationCode");
		if(util.hasTwoOrMoreDots(classificationCode)) {
			return web.redirect(null, "분류기호에 소수점이 2개 이상입니다.");
		}
		String authorCode = web.getString("authorCode");
		String volumeCode = web.getString("volumeCode");
		String tag = web.getString("tag");
		String rfId = web.getString("rfId");
		int page = web.getInt("page");
		String bookOrNot = web.getString("bookOrNot");
		String imageLink = web.getString("imageLink");
		int copyCode = web.getInt("copyCode");
		
		
		// 파라미터를 저장할 Beans
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(idLib);
		bookHeld.setId(idBookHeld);
		bookHeld.setTitle(title);
		bookHeld.setWriter(author);
		bookHeld.setPublisher(publisher);
		bookHeld.setPubDate(pubDate);
		bookHeld.setPrice(price);
		bookHeld.setIsbn10(isbn10);
		bookHeld.setIsbn13(isbn13);
		bookHeld.setCategory(category);
		bookHeld.setBookShelf(bookShelf);
		bookHeld.setLocalIdBarcode(barcodeBook);
		//등록번호에서 숫자만 추출한 값을 솔팅인덱스에 주입.
		bookHeld.setSortingIndex(sortingIndex);
		bookHeld.setPurchasedOrDonated(purchasedOrDonated);
		bookHeld.setAdditionalCode(additionalCode);
		bookHeld.setClassificationCode(classificationCode);
		bookHeld.setAuthorCode(authorCode);
		bookHeld.setVolumeCode(volumeCode);
		bookHeld.setTag(tag);
		bookHeld.setRfId(rfId);
		bookHeld.setPage(page);
		bookHeld.setBookOrNot(bookOrNot);
		bookHeld.setImageLink(imageLink);
		bookHeld.setCopyCode(copyCode);
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		BookHeld bookHeldItem = new BookHeld();
		try {
			bookHeldService.updateBookHeldItem(bookHeld);
			bookHeldItem = bookHeldService.getBookHelditem(bookHeld);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("bookHeldItem", bookHeldItem);
		
		return new ModelAndView("book/book_held_view");
	}
	
	@ResponseBody
	@RequestMapping(value= "book/edit_book_held_tag_ok.do", method = RequestMethod.POST)
	public void editBookHeldTag(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			web.printJsonRt("로그인 후에 이용 가능합니다.");
		}
		
		int id = web.getInt("book_held_id");
		String tag = web.getString("tag");
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		bookHeld.setId(id);
		bookHeld.setTag(tag);
		
		
		try {
			bookHeldService.updateBookHeldTag(bookHeld);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}

		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "TAG EDIT OK");
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	/** 별치기호 일괄수정 페이지 */
	@RequestMapping(value = "/book/update_addi_code_batch.do", method = RequestMethod.GET)
	public ModelAndView updateAddiCodeBatch(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}

		return new ModelAndView("book/update_addi_code_batch");
	}
	
	@RequestMapping(value = "/book/update_addi_code_batch_ok.do")
	public ModelAndView regBookBatchCheck(Locale locale, Model model, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		/** (2) 사용하고자 하는 Helper+Service 객체 생성 */
		web.init();

		/** (3) 로그인 여부 검사 */
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		
		// 관리자 로그인 중이라면 관리자의 도서관 id를 가져온다.
		if (web.getSession("loginInfo") == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인이 필요합니다.");
		}

		/** (4) 파일이 포함된 POST 파라미터 받기 */
		// <form>태그 안에 <input type="file">요소가 포함되어 있고,
		// <form>태그에 enctype="multipart/form-data"가 정의되어 있는 경우
		// WebHelper의 getString()|getInt() 메서드는 더 이상 사용할 수 없게 된다.
		try {
			upload.multipartRequest("/addiCodeTemp");
		} catch (Exception e) {
			return web.redirect(null, "multipart 데이터가 아닙니다.");
		}

		// UploadHelper에서 텍스트 형식의 파라미터를 분류한 Map을 리턴받아서 값을 추출한다.
		Map<String, String> paramMap = upload.getParamMap();
		String codeType = paramMap.get("codeType");
		String codeValue = paramMap.get("chgValue");
		
		//권차기호 일 경우 사용하는 항목
		String volumeCodeStart = paramMap.get("volumeCodeStart");
		String barcodeStart = paramMap.get("barcodeStart");
		String barcodeEnd = paramMap.get("barcodeEnd");
		
		if(codeType == null || "".equals(codeType)) {
			return web.redirect(null, "변경 항목을 선택해주세요.");
		}
		
		int idLib = loginInfo.getIdLibMng();
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		
		//System.out.println(codeType+"코드타입");
		switch(codeType) {
		case "bookShelf":
			bookHeld.setBookShelf(codeValue);
			break;
		case "addiCode" :
			bookHeld.setAdditionalCode(codeValue);
			break;
		case "classCode":
			bookHeld.setClassificationCode(codeValue);
			break;
		case "authorCode" :
			bookHeld.setAuthorCode(codeValue);
			break;
		case "volumeCode" :
			
			break;
		case "copyCode" :
			int tempNum = 0;
			if("".equals(codeValue)||codeValue==null) {
				tempNum = 0;
			} else {
				tempNum = Integer.parseInt(codeValue);
			}
			bookHeld.setCopyCode(tempNum);
			break;
		}
		
		String resultMsg = "도서정보 일괄 수정이 완료되었습니다.";
		
		/** (6) 업로드 된 파일 정보 추출 */
		List<FileInfo> fileList = upload.getFileList();
		
		try {
			// 권차기호 수정시, 화면에 등록번호를 지정하는 것으로 수정 파일을 읽지 않음.
			if("volumeCode".equals(codeType)) {
				int curVolumeCode = Integer.parseInt(volumeCodeStart);
				String curVolumeCodeStr = null;
				barcodeStart = barcodeStart.toUpperCase();
				//바코드 헤드 영문 이니셜
				String barcodeHead = util.strExtract(barcodeStart);
				//바코드 숫자 시작번호
				int barcodeNum = util.numExtract(barcodeStart);
				//바코드 마지막번호
				int endBarcodeNum = util.numExtract(barcodeEnd);
				
				while(barcodeNum<=endBarcodeNum) {
					String curBarcode = frequentlyFunction.makeBarcodeByNumOfDigits(idLib, barcodeHead, barcodeNum);
					bookHeld.setLocalIdBarcode(curBarcode);
					curVolumeCodeStr = String.valueOf(curVolumeCode);
					bookHeld.setVolumeCode(curVolumeCodeStr);
					bookHeldService.updateVolumeCodeBatchByBarcode(bookHeld);
					barcodeNum++;
					curVolumeCode++;
				}
				
			} else if("bookRfId".equals(codeType)) {
				// 도서등록번호(바코드)로 rf id 수정
				for (int i = 0; i < fileList.size(); i++) {
					// 업로드 된 정보 하나 추출하여 데이터베이스에 저장하기 위한 형태로 가공해야 한다.
					FileInfo info = fileList.get(i);
					String loadFilePath = info.getFileDir() + "/" + info.getFileName();
					//io 페이지에서 받은 파일 경로로 엑셀 읽어오기
					FileInputStream fis = new FileInputStream(loadFilePath);
					
					XSSFWorkbook workbook = new XSSFWorkbook(fis);
					//int sheetNum = workbook.getNumberOfSheets();
					//logger.info("현재 시트 번호 = "+sheetNum);
					
					XSSFSheet curSheet = workbook.getSheetAt(0);
					//int row_crt = curSheet.getPhysicalNumberOfRows();
					//logger.info("최종 행 번호 = "+row_crt);
					//최초 행의 마지막 셀 수 = 마지막 컬럼(수평) 번호
					// 마지막 로우 넘버 0부터 시작, 5개면 번호는 4
					int lastRowCount = curSheet.getLastRowNum();
					
					for(int j=0; j<=lastRowCount; j++) {
						XSSFRow curRow = curSheet.getRow(j);
						String localIdBarcode = String.valueOf(curRow.getCell(0));
						String rfId = String.valueOf(curRow.getCell(1));
						if(rfId.indexOf(".") > -1) {
							rfId = rfId.substring(0,rfId.indexOf("."));
						}
						bookHeld.setLocalIdBarcode(localIdBarcode);
						bookHeld.setRfId(rfId);
						bookHeldService.updateBookRfIdByBarcode(bookHeld);
					}
					
					//workbook 역할 끝났으면 바로 닫아주기.
					workbook.close();
					
					// 파일 처리 후 파일 삭제
					upload.removeFile(loadFilePath);
				}
			} else {
				//권차기호 volumecode 가 아닌경우 파일 처리.
				// 업로드 된 파일의 수 만큼 반복 처리 한다.
				for (int i = 0; i < fileList.size(); i++) {
					
					// 업로드 된 정보 하나 추출하여 데이터베이스에 저장하기 위한 형태로 가공해야 한다.
					FileInfo info = fileList.get(i);

					// DB에 저장하기 위한 항목 생성
//					BbsFile file = new BbsFile();

					//아래는 db에 저장하기 위한 처리이므로 필요없다.
					// 도서관 아이디 지정
//					file.setIdLibFile(loginInfo.getIdLibMng());
					
					// 데이터 복사
//					file.setOriginName(info.getOrginName());
//					file.setFileDir(info.getFileDir());
//					file.setFileName(info.getFileName());
//					file.setContentType(info.getContentType());
//					file.setFileSize(info.getFileSize());
					
					// 저장처리 할필요가 없다
					/*bbsFileService.insertRegBookFile(file);*/
					
					String loadFilePath = info.getFileDir() + "/" + info.getFileName();
					FileInputStream fileStream = new FileInputStream(loadFilePath);
					InputStreamReader isr = new InputStreamReader(fileStream, "UTF-8");
					BufferedReader brFile = new BufferedReader(isr);
					String line = null;
					
					if("barcode".equals(codeType)) {
						while((line = brFile.readLine()) != null) {
							bookHeld.setLocalIdBarcode(line);
							String tempHead = util.strExtract(line);
							int tempNum = util.numExtract(line);
							String modiBarcode = util.makeStrLength(Integer.parseInt(codeValue), tempHead, tempNum);
							bookHeld.setModiBarcode(modiBarcode);
							//함수만든게 별치기호로 만든거라, 이름은 따로 수정안하겠음.
							//모든기호 다 수정할수있도록 맵퍼만 변경.
							bookHeldService.updateBookBarcodeByBarcode(bookHeld);
							System.out.println(line+"->"+modiBarcode+" 수정됨.");
							bookHeld.setLocalIdBarcode("");
						} //while 문 끝
					} else if("discard".equals(codeType)) {
						int nullChecker = 0;
						//도서 폐기 처리
						while((line = brFile.readLine()) != null) {
							//System.out.println(line);
							BookHeld deleteBook = new BookHeld();
							deleteBook.setLibraryIdLib(idLib);
							deleteBook.setLocalIdBarcode(line);
							deleteBook = bookHeldService.selectBookHeldItemEvenNull(deleteBook);
							if(deleteBook == null) {
								nullChecker++;
								if(nullChecker==1) {
									resultMsg += "("+line;
								} else {
									resultMsg += "/"+line;
								}
								continue;
							}
							bookHeldService.updateBookHeldDiscard(deleteBook);
						}
						if(nullChecker>0) {
							resultMsg += " 도서정보를 찾을 수 없습니다.)";
						}
					} else {
						//바코드 자리수 수정아닐 경우
						while((line = brFile.readLine()) != null) {
							bookHeld.setLocalIdBarcode(line);
							//함수만든게 별치기호로 만든거라, 이름은 따로 수정안하겠음.
							//모든기호 다 수정할수있도록 맵퍼만 변경.
							bookHeldService.updateAddiCodeByBarcodeNum(bookHeld);
							bookHeld.setLocalIdBarcode("");
						} //while 문 끝
					}
					brFile.close();
					upload.removeFile(loadFilePath);
				}
			}
			
			

		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}

		/** (9) 가입이 완료되었으므로 메인페이지로 이동 */
		return web.redirect(web.getRootPath() + "/book/update_addi_code_batch.do", resultMsg);
	}
	
	
	//바코드 자리수 바꾸기 
	@RequestMapping(value = "/book/update_barcode_batch_ok_xxxxxxxxxxxxxxxxxxx.do")
	public ModelAndView barcodeBatchModify(Locale locale, Model model, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		/** (2) 사용하고자 하는 Helper+Service 객체 생성 */
		web.init();

		/** (3) 로그인 여부 검사 */
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		
		// 관리자 로그인 중이라면 관리자의 도서관 id를 가져온다.
		if (web.getSession("loginInfo") == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인이 필요합니다.");
		}

		/** (4) 파일이 포함된 POST 파라미터 받기 */
		// <form>태그 안에 <input type="file">요소가 포함되어 있고,
		// <form>태그에 enctype="multipart/form-data"가 정의되어 있는 경우
		// WebHelper의 getString()|getInt() 메서드는 더 이상 사용할 수 없게 된다.
		try {
			upload.multipartRequest("/addiCodeTemp");
		} catch (Exception e) {
			return web.redirect(null, "multipart 데이터가 아닙니다.");
		}

		// UploadHelper에서 텍스트 형식의 파라미터를 분류한 Map을 리턴받아서 값을 추출한다.
		Map<String, String> paramMap = upload.getParamMap();
		//바코드 수정으로 체크하면,
		String codeType = paramMap.get("codeType");
		//바코드 자리수
		String codeValue = paramMap.get("chgValue");
		
		if(codeType == null) {
			return web.redirect(null, "변경 항목을 선택해주세요.");
		}
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		
		System.out.println(codeType+"코드타입");
		
		
		
		/** (6) 업로드 된 파일 정보 추출 */
		List<FileInfo> fileList = upload.getFileList();
		
		try {
			// 업로드 된 파일의 수 만큼 반복 처리 한다.
			for (int i = 0; i < fileList.size(); i++) {
				System.out.println("파일 처리 됨.");
				// 업로드 된 정보 하나 추출하여 데이터베이스에 저장하기 위한 형태로 가공해야 한다.
				FileInfo info = fileList.get(i);

				// DB에 저장하기 위한 항목 생성
//				BbsFile file = new BbsFile();

				//아래는 db에 저장하기 위한 처리이므로 필요없다.
				// 도서관 아이디 지정
//				file.setIdLibFile(loginInfo.getIdLibMng());
				
				// 데이터 복사
//				file.setOriginName(info.getOrginName());
//				file.setFileDir(info.getFileDir());
//				file.setFileName(info.getFileName());
//				file.setContentType(info.getContentType());
//				file.setFileSize(info.getFileSize());
				
				// 저장처리 할필요가 없다
				/*bbsFileService.insertRegBookFile(file);*/
				
				String loadFilePath = info.getFileDir() + "/" + info.getFileName();
				FileInputStream fileStream = new FileInputStream(loadFilePath);
				InputStreamReader isr = new InputStreamReader(fileStream, "UTF-8");
				BufferedReader brFile = new BufferedReader(isr);
				String line = null;
				
				while((line = brFile.readLine()) != null) {
					bookHeld.setLocalIdBarcode(line);
					String tempHead = util.strExtract(line);
					int tempNum = util.numExtract(line);
					String modiBarcode = util.makeStrLength(Integer.parseInt(codeValue), tempHead, tempNum);
					bookHeld.setModiBarcode(modiBarcode);
					//함수만든게 별치기호로 만든거라, 이름은 따로 수정안하겠음.
					//모든기호 다 수정할수있도록 맵퍼만 변경.
					bookHeldService.updateBookBarcodeByBarcode(bookHeld);
					bookHeld.setLocalIdBarcode("");
				} //while 문 끝
				brFile.close();
				upload.removeFile(loadFilePath);
			}
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}

		/** (9) 가입이 완료되었으므로 메인페이지로 이동 */
		return web.redirect(web.getRootPath() + "/book/update_addi_code_batch.do", "도서정보 일괄 수정이 완료되었습니다.");
	}
	
	
	
	
	
}
