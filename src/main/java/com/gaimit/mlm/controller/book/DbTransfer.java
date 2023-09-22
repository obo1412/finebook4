package com.gaimit.mlm.controller.book;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.gaimit.helper.FileInfo;
import com.gaimit.helper.RegexHelper;
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.controller.FrequentlyFunction;
import com.gaimit.mlm.model.BbsFile;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.BbsFileService;
import com.gaimit.mlm.service.BookHeldService;

@Controller
public class DbTransfer {
	/** (1) 사용하고자 하는 Helper 객체 선언 */
	// --> import org.apache.logging.log4j.Logger;
	private static Logger logger = LoggerFactory.getLogger(DbTransfer.class);
	@Autowired
	SqlSession sqlSession;
	@Autowired
	WebHelper web;
	@Autowired
	Util util;
	@Autowired
	UploadHelper upload;
	@Autowired
	RegexHelper regex;
	@Autowired
	BookHeldService bookHeldService;
	@Autowired
	BbsFileService bbsFileService;
	@Autowired
	FrequentlyFunction frequentlyFunction;
	
	static int dataTransStopper = 0;
	static List<String> libArr = new ArrayList<String>();
	
	//수원이 작성 예제
	@RequestMapping(value = "/book/db_transfer.do")
	public ModelAndView DbTransferV(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {		
		web.init();
		
		//Manager loginInfo = (Manager) web.getSession("loginInfo");
		if (web.getSession("loginInfo") == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인이 필요합니다.");
		}

		return new ModelAndView("book/db_transfer");
	}
	
	//수원이 작성 예제
	// mdb 데이터 MySQL에 저장 test.
	public void mdbConvertTest() {
		Connection conn;
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			
			conn = DriverManager.getConnection("jdbc:ucanaccess://E:\\downloads/Finebook.mdb"); // mdb 파일 위치 설정.
			Statement s = conn.createStatement();		
			ResultSet rs = s.executeQuery("SELECT * FROM 도서이동테스트");
			
			// 성능 때문에 반복문 안에서 new는 하는 것은 피해야함. 
			BookHeld bookHeld = new BookHeld();
			String title = "";
			String writer = "";
			
			while (rs.next()) {				
				title = rs.getString(1);
				writer = rs.getString(2);
				
				// console에서 mdb 값 확인.
			    logger.debug("mdb 1열="+title);
			    logger.debug("mdb 2열="+writer);
			    
			    // mdb 값 data로 설정.
				bookHeld.setTitleBook(title);
				bookHeld.setWriterBook(writer);
				
				// MySQL 삽입.
				bookHeldService.insertBook(bookHeld);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//수원 작성 예제
	@RequestMapping(value = "/book/db_transfer_ok.do")
	public ModelAndView DbTransferOk(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {		
		web.init();
		
		mdbConvertTest();

		String url = web.getRootPath() + "/book/reg_book_batch.do";
		return web.redirect(url, "도서 데이터베이스 이관 테스트 완료");
	}
	
	
	
	@RequestMapping(value= "/book/import_book_excel.do", method = RequestMethod.GET)
	public ModelAndView importBookExcel(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		
		return new ModelAndView("book/import_book_excel");
	}
	
	@RequestMapping(value = "/book/import_book_excel_check.do")
	public ModelAndView importBookExcelCheck(Locale locale, Model model, HttpServletRequest request,
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
			upload.multipartRequest();
		} catch (Exception e) {
			return web.redirect(null, "multipart 데이터가 아닙니다.");
		}
		
		//초기에 데이터 몇개까지만 보일건지 정함.
		int seeRow = 2;
		
		/** (6) 업로드 된 파일 정보 추출 */
		List<FileInfo> fileList = upload.getFileList();
		
		try {
			for (int i = 0; i < fileList.size(); i++) {
				FileInfo info = fileList.get(i);
				
				// DB에 저장하기 위한 항목 생성
				BbsFile file = new BbsFile();
				
				// 도서관 아이디 지정
				file.setIdLibFile(loginInfo.getIdLibMng());
				
				// 데이터 복사
				file.setOriginName(info.getOrginName());
				file.setFileDir(info.getFileDir());
				file.setFileName(info.getFileName());
				file.setContentType(info.getContentType());
				file.setFileSize(info.getFileSize());
				
				// DB에 파일 업로드 저장처리
				bbsFileService.insertRegBookFile(file);
				// DB에 저장된 경로, 추후 삭제처리를 위해 id 가져옴.
				int fileId = bbsFileService.selectFileIdByfilePath(file);
				model.addAttribute("fileId", fileId);
				
				
				int extPos = info.getFileName().lastIndexOf(".");
				String ext = info.getFileName().substring(extPos+1);
				logger.debug(ext);
				
				String loadFilePath = info.getFileDir() + "/" + info.getFileName();
				FileInputStream fis = new FileInputStream(loadFilePath);
				
				if(ext.equals("xlsx")) {
					XSSFWorkbook workbook = new XSSFWorkbook(fis);
					int sheetNum = workbook.getNumberOfSheets();
					logger.info("현재 시트 번호 = "+sheetNum);
					
					XSSFSheet curSheet = workbook.getSheetAt(0);
					int row_crt = curSheet.getPhysicalNumberOfRows();
					logger.info("최종 행 번호 = "+row_crt);
					//최초 행의 마지막 셀 수 = 마지막 컬럼(수평) 번호
					int lastCellCount = curSheet.getRow(0).getPhysicalNumberOfCells();
					logger.info("엑셀 db 목록의 컬럼 개수= "+lastCellCount);
					
					//최종 행의 개수가 10개가 넘는다면 보여지는 개수를 10개로
					if(row_crt > 10) {
						seeRow = 10;
					} else {
						seeRow = row_crt;
					}
					
					String[][] theArr = new String[seeRow][lastCellCount];
					
					
					//j < row_crt		k < lastCellCount 컬럼은 그대로 가져오기.
					for (int j=0; j<seeRow; j++) {
						//i번 행을 읽어온다.
						XSSFRow horizonRow = curSheet.getRow(j);
						if(horizonRow != null) {
							for(int k=0; k<lastCellCount; k++) {
								if("null".equals(String.valueOf(horizonRow.getCell(k)))) {
									theArr[j][k] = "";
								} else {
									theArr[j][k] = String.valueOf(horizonRow.getCell(k));
								}
//								logger.info("colArr["+j+"]["+k+"]의 값 = "+theArr[j][k]);
							}
						}
						//logger.info(theArr[j][9]);
					}
					model.addAttribute("theArr", theArr);
					model.addAttribute("row_crt", row_crt);
					model.addAttribute("lastCellCount", lastCellCount);
					workbook.close();
				} else {
					HSSFWorkbook workbook = new HSSFWorkbook(fis);
					int sheetNum = workbook.getNumberOfSheets();
					logger.info("현재 시트 번호 = "+sheetNum);
					
					HSSFSheet curSheet = workbook.getSheetAt(0);
					int row_crt = curSheet.getPhysicalNumberOfRows();
					logger.info("최종 행 번호 = "+row_crt);
					//최초 행의 마지막 셀 수 = 마지막 컬럼(수평) 번호
					int lastCellCount = curSheet.getRow(0).getPhysicalNumberOfCells();
					logger.info("엑셀 db 목록의 컬럼 개수= "+lastCellCount);
					
					// row_crt 는 10개까지만 가져와보자.
					String[][] theArr = new String[10][lastCellCount];
					
					// j < row_crt		k < lastCellCount 컬럼은 그대로 가져오기.
					for (int j=0; j<10; j++) {
						//i번 행을 읽어온다.
						HSSFRow horizonRow = curSheet.getRow(j);
						if(horizonRow != null) {
							for(int k=0; k<lastCellCount; k++) {
								theArr[j][k] = String.valueOf(horizonRow.getCell(k));
								/*logger.info("colArr["+j+"]["+k+"]의 값 = "+theArr[j][k]);*/
							}
						}
					}
					model.addAttribute("theArr", theArr);
					model.addAttribute("row_crt", row_crt);
					model.addAttribute("lastCellCount", lastCellCount);
					workbook.close();
				}
				//파일 호출 후, 반복 사용하지 않는 파일 삭제처리
				//upload.removeFile(loadFilePath);
				//파일 자체를 유지하며 파일 경로 표시
				model.addAttribute("loadFilePath", loadFilePath);
				logger.info("filePath"+loadFilePath);
				
				//시간 표시를 위한 로직
				SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar time = Calendar.getInstance();
				String timeStandard = dateForm.format(time.getTime());
				//시간 표시 로직 끝
				model.addAttribute("timeStandard", timeStandard);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

		/** (9) 가입이 완료되었으므로 메인페이지로 이동 */
		return new ModelAndView("book/import_book_excel_check");
	}
	
	@ResponseBody
	@RequestMapping(value= "/book/import_book_excel_ok.do", method = RequestMethod.POST)
	public void importBookExcelOkAsync(Locale locale, Model model,
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
		if (web.getSession("loginInfo") == null) {
			web.printJsonRt("로그인 후에 이용 가능합니다.");
		}
		
		//전역변수 스토퍼 초기화
		dataTransStopper = 0;
		//진행중인 등록진행중인지 도서관 번호 전역변수에 입력
		String idLibStr = String.valueOf(loginInfo.getIdLibMng()); 
		libArr.add(idLibStr);
		logger.info("도서관 번호 확인: "+libArr);
		
		//몇 번째에서 중단했는지 기록을 위한 변수
		//중단 되지 않으면, 중간처리에서 몇번째 행인지 값 넣어줌.
		String curStepMsg = null;
		
		//현재 페이지로부터 매번 번호를 가져온다.
		int curRow = web.getInt("curRow", 1);
		//행 개수
		int row_count = web.getInt("row_crt");
		//열 개수
		int col_count = web.getInt("lastCellCount");
		//파일 경로 받아오기.
		String loadFilePath = web.getString("loadFilePath");
		//logger.info("경로: "+loadFilePath);
		//파일 삭제를 위한 파일 id 미리 받아놔야함.
		int fileId = web.getInt("fileId");
		
		//프론트에서 배열 받아오기
		String[] arrColH = web.getStringArray("arrColH");
		//logger.info("arrColH:"+arrColH);
		//logger.info("arrColH길이:"+arrColH.length);
//		for(int i=0; i<arrColH.length; i++) {
//			System.out.println(i+"번째 arrColH값:"+arrColH[i]);
//		}
		
		try {
			//컬럼 제목이 담길 배열 생성
			String[] colList = null;
			// 셀렉트가 하나 이상있으면 colList에 셀렉트 값으로 가고,
			// 아예 없으면 자동으로 컬럼매칭
			int selectSwitch = 0;
			
			if(col_count > 0) {
				colList = new String[col_count];
				for(int i=0; i<col_count; i++) {
					colList[i] = arrColH[i];
					//logger.info("colList["+i+"]:"+colList[i]);

					//공백값인지 확인하기 위한 if문
//					if("".equals(arrColH[i])) {
//						System.out.println("공백값");
//					}
					if(colList[i]!=null&&!"".equals(colList[i])) {
						//셀렉트에서 선택된 값이 있으면, 스위치 +1
						selectSwitch++;
						//System.out.println("셀렉트 스위치 + 1");
					}
				}
			}
			
			//io 페이지에서 받은 파일 경로로 엑셀 읽어오기
			FileInputStream fis = new FileInputStream(loadFilePath);
			
			//확장자명을 가져오기 위한 변수
			int extPos = loadFilePath.lastIndexOf(".");
			String ext = loadFilePath.substring(extPos+1);
			
			if(ext.equals("xlsx")) {
				XSSFWorkbook workbook = new XSSFWorkbook(fis);
				//int sheetNum = workbook.getNumberOfSheets();
				//logger.info("현재 시트 번호 = "+sheetNum);
				
				XSSFSheet curSheet = workbook.getSheetAt(0);
				//int row_crt = curSheet.getPhysicalNumberOfRows();
				//logger.info("최종 행 번호 = "+row_crt);
				//최초 행의 마지막 셀 수 = 마지막 컬럼(수평) 번호
				
				//workbook 역할 끝났으면 바로 닫아주기.
				workbook.close();
				
				int lastCellCount = curSheet.getRow(0).getPhysicalNumberOfCells();
				//logger.info("엑셀 db 목록의 컬럼 개수= "+lastCellCount);
				
				//String[][] theArr = new String[row_crt][lastCellCount];
				
				XSSFRow horizonRow = curSheet.getRow(0);
				//엑셀 0번 행의 컬럼 제목 가져오기.
				if(selectSwitch==0) {
					//셀렉트된 값이 아예 없으면
					for(int k=0; k<lastCellCount; k++) {
						colList[k] = String.valueOf(horizonRow.getCell(k));
						//logger.info(k+" "+colList[k]);
					}
				}
				
				//초기화 선언 필요없음.
				for (; curRow<row_count; curRow++) {
					if(dataTransStopper>0&&!libArr.contains(idLibStr)) {
						//스토퍼가 있고, libArr에 도서관번호가 없으면 브레이크
						break;
					}
					//등록처리를 위한 오브젝트
					BookHeld bookHeld = new BookHeld();
					bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
					//j번 행을 읽어온다. 현재 페이지로 부터 값을 가져온다. curRow 선언은 위쪽에
					horizonRow = curSheet.getRow(curRow);
					if(horizonRow != null) {
						for(int k=0; k<lastCellCount; k++) {
							//theArr[j][k] = String.valueOf(horizonRow.getCell(k));
							//logger.info("colArr["+j+"]["+k+"]의 값 = "+theArr[j][k]);
							//k 번째 컬럼의 값을 아예 따서 일단 두고.
							String cellValue = String.valueOf(horizonRow.getCell(k));
							//엑셀에서 값이 없으면 문자로 "null" 이 된다. 값이 없음이 아니라 "null"이라는 문자로 적용됨.
							// k가 colList의 뭐시기면 이 값 넣기. colList 길이가 아예 없다. 그러면 자동선택으로.
							if(!"".equals(colList[k])&&colList[k]!=null&&cellValue!=null&&!"".equals(cellValue)&&!"null".equals(cellValue)) {
								//조건문이 null 이되면 아예 실행을 안한다. 따라서 위 if 조건 필요.
								//우선 도서 상태값 기본으로 1로 지정. 이후 switch 돌면서 값있으면 처리.
								bookHeld.setAvailable(1);
								switch(colList[k]) {
								case "도서바코드번호":
								case "도서등록번호":
								case "barcode":
								case "등록번호": //DLS용 임시이동용
									//등록번호 체크 안되어있을경우 처리
									bookHeld.setLocalIdBarcode(cellValue);
									bookHeld.setSortingIndex(util.numExtract(cellValue));
									break;
								case "도서명":
								case "title":
								case "자료명": //DLS용 임시이동용
									bookHeld.setTitle(cellValue);
									break;
								case "저자명":
								case "저자": //DLS용 임시이동용
								case "author":
									bookHeld.setWriter(util.getAuthorNameFromBlank(cellValue));
									break;
								case "출판사명":
								case "publisher":
								case "발행처": //DLS용 임시이동용
									bookHeld.setPublisher(cellValue);
									break;
								case "출판일시":
								case "pubDate":
									bookHeld.setPubDate(util.getDateValue(cellValue));
									break;
								case "pubDateYear":
									bookHeld.setPubDateYear(cellValue);
									break;
								case "등록일자":
								case "regDate":
									bookHeld.setRegDate(cellValue);
									break;
								case "callNumber":
								case "청구기호": //DLS용 임시이동용
									//청구기호가 뭉쳐있을때 분리하는 로직
									String[] eachCode = util.getEachCodeFromCallCode(cellValue);
									bookHeld.setAdditionalCode(eachCode[0]);
									bookHeld.setClassificationCode(eachCode[1]);
									bookHeld.setAuthorCode(eachCode[2]);
									bookHeld.setVolumeCode(eachCode[3]);
									bookHeld.setCopyCode(Integer.parseInt(eachCode[4]));
									break;
								case "도서정가":
								case "price":
									bookHeld.setPrice(cellValue);
									break;
								case "RF_id":
								case "rfId":
									bookHeld.setRfId(cellValue);
									break;
								case "ISBN10":
								case "isbn10":
									bookHeld.setIsbn10(cellValue);
									break;
								case "ISBN13":
								case "isbn13":
									bookHeld.setIsbn13(cellValue);
									break;
								case "카테고리":
								case "category":
									bookHeld.setCategory(cellValue);
									break;
								case "내용":
								case "description":
									bookHeld.setDescription(cellValue);
									break;
								case "수입구분":
								case "purOrDon":
								case "구입/기증":
								case "구매/기증":
									// 구입 1 / 기증 0
									bookHeld.setPurchasedOrDonated(util.getPurOrDon(cellValue));
									break;
								case "도서상태":
								case "available":
									//대출가능 1, 대출중 0, 폐기 2, 기록삭제예정 3
									bookHeld.setAvailable(util.getAvailableFromString(cellValue));
									break;
								case "별치기호":
								case "additionalCode":
									bookHeld.setAdditionalCode(cellValue);
									break;
								case "분류기호":
								case "classificationCode":
									bookHeld.setClassificationCode(cellValue);
									break;
								case "저자기호":
								case "authorCode":
									bookHeld.setAuthorCode(cellValue);
									break;
								case "권차":
								case "volumeCode":
									bookHeld.setVolumeCode(util.getNumVolumeCode(cellValue));
									break;
								case "복본기호":
								case "copyCode":
									//복본기호 함수처리 C2 요딴거 숫자로 다 바꿔주기
									bookHeld.setCopyCode(util.getNumFromCopyCode(cellValue));
									break;
								case "도서비도서":
								case "bookOrNot":
									bookHeld.setBookOrNot(util.getBookOrNot(cellValue));
									break;
								case "총페이지":
								case "pageItem":
									bookHeld.setPage(util.getPageFromString(cellValue));
									break;
								case "이미지URL":
								case "imageLink":
									bookHeld.setImageLink(cellValue);
									break;
								case "bookShelf":
									bookHeld.setBookShelf(cellValue);
									break;
								case "bookSize":
									bookHeld.setBookSize(cellValue);
									break;
								case "bookTag":
									bookHeld.setTag(cellValue);
									break;
								}
							} //스위치 null 조건 if문 끝
							
						} //for문 컬럼(열) k 셀 돌기 끝
						logger.info(curRow+"번째 :"+bookHeld.getTitle());
						//writer 즉 저자 값이 null 이면 저자 미상으로 변경 처리.
						if(bookHeld.getWriter()==null||"".equals(bookHeld.getWriter())) {
							bookHeld.setWriter("저자 미상");
						}
						//logger.info("bookHeld title: "+bookHeld.getTitle()+" writer: "+bookHeld.getWriter()+" publisher: "+bookHeld.getPublisher()+" classcode: "+bookHeld.getClassificationCode());
						//insert 문 명시
						bookHeldService.insertBookHeld(bookHeld);
						
						curStepMsg = curRow+"번째 행 등록됨: "+bookHeld.getTitle();
						
						//사용한 객체는 삭제처리.
						bookHeld = null;
					} //if문 row 가 존재하는지 끝
				
				} //row 행 for 문 끝
				//model.addAttribute("theArr", theArr);
				//효과가 없나?
				//workbook.close();
				
			} else {
				HSSFWorkbook workbook = new HSSFWorkbook(fis);
				//int sheetNum = workbook.getNumberOfSheets();
				//logger.info("현재 시트 번호 = "+sheetNum);
				
				HSSFSheet curSheet = workbook.getSheetAt(0);
				int row_crt = curSheet.getPhysicalNumberOfRows();
				logger.info("최종 행 번호 = "+row_crt);
				
				workbook.close();
				//최초 행의 마지막 셀 수 = 마지막 컬럼(수평) 번호
				int lastCellCount = curSheet.getRow(0).getPhysicalNumberOfCells();
				logger.info("엑셀 db 목록의 컬럼 개수= "+lastCellCount);
				
				String[][] theArr = new String[row_crt][lastCellCount];
				
				
				for (int j=0; j<row_crt; j++) {
					//i번 행을 읽어온다.
					HSSFRow horizonRow = curSheet.getRow(j);
					if(horizonRow != null) {
						for(int k=0; k<lastCellCount; k++) {
							theArr[j][k] = String.valueOf(horizonRow.getCell(k));
							/*logger.info("colArr["+j+"]["+k+"]의 값 = "+theArr[j][k]);*/
						}
					}
				}
				model.addAttribute("theArr", theArr);
			}
			
			//모든 등록이 완료되면 실행되는 최종 처리문.
			if(curRow >= row_count) {
				//중단/재시작을 위한 도서관 식별자 배열에서 식별자 삭제
				libArr.remove(idLibStr);
				//파일 호출 후, 반복 사용하지 않는 파일 삭제처리.
				upload.removeFile(loadFilePath);
				
				// db 삭제처리를 위한 파일 id는 클래스 상단에 미리 받아놔야함.
				BbsFile file = new BbsFile();
				file.setId(fileId);
				
				//db 삭제처리
				bbsFileService.deleteFile(file);
				//모든 데이터가 등록된 메시지
				curStepMsg = "모든 도서 데이터 등록이 완료되었습니다.";
			}
			
			
		} catch (Exception e) {
			libArr.remove(String.valueOf(loginInfo.getIdLibMng()));
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		//마지막 행까지 로직이 여기까지 왔을 경우엔 무조건 리턴.
		data.put("msg", curStepMsg);
		//현재 몇번째까지 등록되었는지
		data.put("curRow", curRow);
		
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping(value= "/book/import_book_excel_pause.do", method = RequestMethod.POST)
	public void importBookExcelPause(Locale locale, Model model,
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
		if (web.getSession("loginInfo") == null) {
			web.printJsonRt("로그인 후에 이용 가능합니다.");
		}
		
		//중단을 위한 idLib
		//해당 도서관 식별자 전역변수에서 삭제하면, 등록 for문이 종료됨.
		libArr.remove(String.valueOf(loginInfo.getIdLibMng()));
		logger.info("Stop 중단 클래스 동작 - 도서관 번호 확인: "+libArr);
		
		//데이터전송 스토퍼
		dataTransStopper = 1;
		
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		//마지막 행까지 다 돌았을 경우만 리턴
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping(value= "/book/import_book_excel_display_current_progress.do", method = RequestMethod.GET)
	public void importBookExcelDisplayCurData(Locale locale, Model model,
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
		if (web.getSession("loginInfo") == null) {
			web.printJsonRt("로그인 후에 이용 가능합니다.");
		}
		
		String idLibStr = String.valueOf(loginInfo.getIdLibMng());
		if(!libArr.contains(idLibStr)) {
			web.printJsonRt("데이터 전송이 중단되었습니다.");
		}
	
		String timeStandard = web.getString("timeStandard");
		
		BookHeld dcdBookHeld = new BookHeld();
		dcdBookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		dcdBookHeld.setEditDate(timeStandard);
		
		int regOkRow = 0;
		
		try {
			regOkRow = bookHeldService.selectCountImportExcelData(dcdBookHeld);
			logger.info(regOkRow+" 진행중 - 도서등록 진행중 몇번째인지 체크");
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("regOkRow", regOkRow);
		//마지막 행까지 다 돌았을 경우만 리턴
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	//사용 안하려는 방식. 이것은 데이터를 화면으로 다 불러와서
	//화면에 불러와진 데이터를 처리하는 방식으로 좀 불편 데이터 수가 많을 경우
	//불러올때까지 기다려야되는 불편함이 있음.
	@RequestMapping(value= "/book/import_book_excel_ok_xxxxxxxxxx.do", method = RequestMethod.POST)
	public ModelAndView importBookExcelOkXXXNoUse(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (web.getSession("loginInfo") == null) {
			web.printJsonRt("로그인 후에 이용 가능합니다.");
		}
		
		int colLast = web.getInt("colLast");
		
		//colH 머리값을 가져옴 컬럼의 주제
		//여기서 colH는 map colH 큰뭉치
		Map<String, String> colH = new HashMap<String, String>();
		for(int i=0; i<=colLast; i++) {
			colH.put("colH"+i, web.getString("colH"+i));
			logger.debug("colH"+i+"의 값 = "+colH.get("colH"+i));
		}
		
		/*System.out.println("title의 키값"+getKey(colH, "title"));
		System.out.println("author의 키값"+getKey(colH, "author"));
		System.out.println("publisher의 키값"+getKey(colH, "publisher"));
		System.out.println("classNo의 키값"+getKey(colH, "classNo"));*/
		
		//이하 colH의 키값은 colH+숫자 밸류는 title 등, getKey 함수로 colH+숫자값 빼오기
		String titleCol = getKey(colH, "title");
		String titleIdxStr = titleCol.substring(titleCol.indexOf("colH")+4);
		int titleIdx = Integer.parseInt(titleIdxStr);
		
		//저자명
		String authorCol = getKey(colH, "author");
		String authorIdxStr = authorCol.substring(authorCol.indexOf("colH")+4);
		int authorIdx = Integer.parseInt(authorIdxStr);
		
		//출판사
		String publisherCol = getKey(colH, "publisher");
		int publisherIdx = -1;
		if(publisherCol != null) {
			String publisherIdxStr = publisherCol.substring(publisherCol.indexOf("colH")+4);
			publisherIdx = Integer.parseInt(publisherIdxStr);
		}
		
		//출판일시
		String pubDateCol = getKey(colH, "pubDate");
		int pubDateIdx = -1;
		if(pubDateCol != null) {
			String pubDateIdxStr = pubDateCol.substring(pubDateCol.indexOf("colH")+4);
			pubDateIdx = Integer.parseInt(pubDateIdxStr);
		}
		
		//출판년도 출판일시랑 다름.
		String pubDateYearCol = getKey(colH, "pubDateYear");
		int pubDateYearIdx = -1;
		if(pubDateYearCol != null) {
			String pubDateYearIdxStr = pubDateYearCol.substring(pubDateYearCol.indexOf("colH")+4);
			pubDateYearIdx = Integer.parseInt(pubDateYearIdxStr);
		}
		
		String rfIdCol = getKey(colH, "rfId");
		int rfIdIdx = -1; //데이터 이관 선택되지 않았을 경우 음수
		if(rfIdCol != null) {
			String rfIdIdxStr = rfIdCol.substring(rfIdCol.indexOf("colH")+4);
			rfIdIdx = Integer.parseInt(rfIdIdxStr);
		}
		
		String regDateCol = getKey(colH, "regDate");
		int regDateIdx = -1; //임의의 값을 주고 사용하지 않을 경우를 상정
		if(regDateCol != null) {
			String regDateIdxStr = regDateCol.substring(regDateCol.indexOf("colH")+4);
			regDateIdx = Integer.parseInt(regDateIdxStr);
		}
		
		String priceCol = getKey(colH, "price");
		int priceIdx = -1;
		if(priceCol != null) {
			String priceIdxStr = priceCol.substring(priceCol.indexOf("colH")+4);
			priceIdx = Integer.parseInt(priceIdxStr);
		}
		
		String callNumberCol = getKey(colH, "callNumber");
		int callNumberIdx = -1;
		if(callNumberCol != null) {
			String callNumberIdxStr = callNumberCol.substring(callNumberCol.indexOf("colH")+4);
			callNumberIdx = Integer.parseInt(callNumberIdxStr);
		}
		
		String barcodeCol = getKey(colH, "barcode");
		int barcodeIdx = -1;
		if(barcodeCol != null) {
			String barcodeIdxStr = barcodeCol.substring(barcodeCol.indexOf("colH")+4);
			barcodeIdx = Integer.parseInt(barcodeIdxStr);
		}
		
		String isbn10Col = getKey(colH, "isbn10");
		int isbn10Idx = -1;
		if(isbn10Col != null) {
			String isbn10IdxStr = isbn10Col.substring(isbn10Col.indexOf("colH")+4);
			isbn10Idx = Integer.parseInt(isbn10IdxStr);
		}
		
		String isbn13Col = getKey(colH, "isbn13");
		int isbn13Idx = -1;
		if(isbn13Col != null) {
			String isbn13IdxStr = isbn13Col.substring(isbn13Col.indexOf("colH")+4);
			isbn13Idx = Integer.parseInt(isbn13IdxStr);
		}
		
		String categoryCol = getKey(colH, "category");
		int categoryIdx = -1;
		if(categoryCol != null) {
			String categoryIdxStr = categoryCol.substring(categoryCol.indexOf("colH")+4);
			categoryIdx = Integer.parseInt(categoryIdxStr);
		}
		
		String descriptionCol = getKey(colH, "description");
		int descriptionIdx = -1;
		if(descriptionCol != null) {
			String descriptionIdxStr = descriptionCol.substring(descriptionCol.indexOf("colH")+4);
			descriptionIdx = Integer.parseInt(descriptionIdxStr);
		}
		
		String purOrDonCol = getKey(colH, "purOrDon");
		int purOrDonIdx = -1;
		if(purOrDonCol != null) {
			String purOrDonIdxStr = purOrDonCol.substring(purOrDonCol.indexOf("colH")+4);
			purOrDonIdx = Integer.parseInt(purOrDonIdxStr);
		}
		
		String availableCol = getKey(colH, "available");
		int availableIdx = -1;
		if(availableCol != null) {
			String availableIdxStr = availableCol.substring(availableCol.indexOf("colH")+4);
			availableIdx = Integer.parseInt(availableIdxStr);
		}
		
		//기호류 시작
		String addiCodeCol = getKey(colH, "additionalCode");
		int addiCodeIdx = -1;
		if(addiCodeCol != null) {
			String addiCodeIdxStr = addiCodeCol.substring(addiCodeCol.indexOf("colH")+4);
			addiCodeIdx = Integer.parseInt(addiCodeIdxStr);
		}
		
		String classCodeCol = getKey(colH, "classificationCode");
		int classCodeIdx = -1;
		if(classCodeCol != null) {
			String classCodeIdxStr = classCodeCol.substring(classCodeCol.indexOf("colH")+4);
			classCodeIdx = Integer.parseInt(classCodeIdxStr);
		}
		
		String authorCodeCol = getKey(colH, "authorCode");
		int authorCodeIdx = -1;
		if(authorCodeCol != null) {
			String authorCodeIdxStr = authorCodeCol.substring(authorCodeCol.indexOf("colH")+4);
			authorCodeIdx = Integer.parseInt(authorCodeIdxStr);
		}
		
		String volumeCodeCol = getKey(colH, "volumeCode");
		int volumeCodeIdx = -1;
		if(volumeCodeCol != null) {
			String volumeCodeIdxStr = volumeCodeCol.substring(volumeCodeCol.indexOf("colH")+4);
			volumeCodeIdx = Integer.parseInt(volumeCodeIdxStr);
		}
		
		String copyCodeCol = getKey(colH, "copyCode");
		int copyCodeIdx = -1;
		if(copyCodeCol != null) {
			String copyCodeIdxStr = copyCodeCol.substring(copyCodeCol.indexOf("colH")+4);
			copyCodeIdx = Integer.parseInt(copyCodeIdxStr);
		}
		//기호류 끝
		
		String bookOrNotCol = getKey(colH, "bookOrNot");
		int bookOrNotIdx = -1;
		if(bookOrNotCol != null) {
			String bookOrNotIdxStr = bookOrNotCol.substring(bookOrNotCol.indexOf("colH")+4);
			bookOrNotIdx = Integer.parseInt(bookOrNotIdxStr);
		}
		
		String pageItemCol = getKey(colH, "pageItem");
		int pageItemIdx = -1;
		if(pageItemCol != null) {
			String pageItemIdxStr = pageItemCol.substring(pageItemCol.indexOf("colH")+4);
			pageItemIdx = Integer.parseInt(pageItemIdxStr);
		}
		
		String imageLinkCol = getKey(colH, "imageLink");
		int imageLinkIdx = -1;
		if(imageLinkCol != null) {
			String imageLinkIdxStr = imageLinkCol.substring(imageLinkCol.indexOf("colH")+4);
			imageLinkIdx = Integer.parseInt(imageLinkIdxStr);
		}
		
		
		//col 값을 받아옴
		Map<String, String[]> col = new HashMap<String, String[]>();
		for(int i=0; i<=colLast; i++) {
			col.put("col"+i, web.getStringArray("col"+i));
		}
		
		try {
			for(int i=0; i<col.get("col0").length; i++) {
				BookHeld bookHeld = new BookHeld();
				//id_book을 받아오기 위한 객체
				bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());

				//bookIdBook은 null 처리
				bookHeld.setBookIdBook(null);
				//bookHeld table
				bookHeld.setTitle(col.get("col"+titleIdx)[i]);
				bookHeld.setWriter(col.get("col"+authorIdx)[i]);
				//check페이지에서 select선택이 안되었을 경우, 선택지는 음수 값을 가진다.
				if(publisherIdx >= 0) {
					//bookHeld Table
					bookHeld.setPublisher(col.get("col"+publisherIdx)[i]);
				}
				if(pubDateIdx >= 0) {
					String pubDateVal = col.get("col"+pubDateIdx)[i];
					if(pubDateVal == null || "null".equals(pubDateVal) || "".equals(pubDateVal)) {
						//받은 값이 '문자열'로 'null' 이면 아무것도 하지 않음.
					} else {
						/*if(pubDateVal.length()>11) {
							//양식 중 데이터가 11자리 이하인 것은 숫자로만 이루어진것.
							pubDateVal = util.changeDateFormat1(pubDateVal);
						}
						//변환 과정에서 null 이 있을 수 있기 때문에 null이면 값을 아예 안넣음
						if(pubDateVal != null) {
							bookHeld.setPubDate(pubDateVal);
						}*/
						pubDateVal = util.getDateValue(pubDateVal);
						bookHeld.setPubDate(pubDateVal);
					}
				}
				if(pubDateYearIdx >= 0) {
					String pubDateYearVal = col.get("col"+pubDateYearIdx)[i];
					//북헬드 테이블
					bookHeld.setPubDateYear(pubDateYearVal);
				}
				if(rfIdIdx >= 0) {
					bookHeld.setRfId(col.get("col"+rfIdIdx)[i]);
				}
				if(regDateIdx >= 0) {
					bookHeld.setRegDate(col.get("col"+regDateIdx)[i]);
				}
				String price = "";
				if(priceIdx >= 0) {
					if(!(col.get("col"+priceIdx)[i]).equals("")) {
						price = col.get("col"+priceIdx)[i];
					}
				}
				
				bookHeld.setPrice(price);
				
				//청구기호 처리 시작
				if(callNumberIdx >= 0) {
					String callNumberOri = col.get("col"+callNumberIdx)[i];
					String[] callNumArray = callNumberOri.split("\\s+");
					for(int j=0; j<callNumArray.length; j++) {
						if(j==0) {
							bookHeld.setClassificationCode(callNumArray[j]);
						} else if(j==1) {
							bookHeld.setAuthorCode(callNumArray[j]);
						} else if(j>1) {
							String firstLetter = callNumArray[j].substring(0,1);
							int strDot = callNumArray[j].indexOf(".");
							if(firstLetter.equals("C")||firstLetter.equals("c")) {
								if(strDot > 0) {
									if(Integer.parseInt(callNumArray[j].substring(strDot+1))==1) {
										bookHeld.setCopyCode(0);
									} else {
										bookHeld.setCopyCode(Integer.parseInt(callNumArray[j].substring(strDot+1)));
									}
								} else {
									if(Integer.parseInt(callNumArray[j].substring(1))==1) {
										bookHeld.setCopyCode(0);
									} else {
										bookHeld.setCopyCode(Integer.parseInt(callNumArray[j].substring(1)));
									}
								}
							} else if(firstLetter.equals("V")||firstLetter.equals("v")||regex.isNum(firstLetter)) {
								if(regex.isNum(firstLetter)) {
									bookHeld.setVolumeCode(callNumArray[j]);
								} else if(strDot > 0) {
									bookHeld.setVolumeCode(callNumArray[j].substring(strDot+1));
								} else {
									bookHeld.setVolumeCode(callNumArray[j].substring(1));
								}
							}
						}
					}
				}
				//청구기호 처리 끝
				
				//등록번호 처리 시작
				if(barcodeIdx >= 0) {
					String excelBarcode = col.get("col"+barcodeIdx)[i];
					bookHeld.setLocalIdBarcode(excelBarcode);
					int existingBarcode = util.numExtract(excelBarcode);
					bookHeld.setSortingIndex(existingBarcode);
					logger.info(bookHeld.getLocalIdBarcode());
				} else {
					//등록번호 처리를 위한 코드
					String newBarcode = frequentlyFunction.getLastBarcode(1, bookHeld);
					//소문자 바코드를 대문자로 변환, 바코드 생성 및 bookHeld에 주입
					newBarcode = newBarcode.toUpperCase();
					bookHeld.setLocalIdBarcode(newBarcode);
					
					//위 비어있는 바코드 번호를 솔팅index에 주입
					bookHeld.setSortingIndex(util.numExtract(newBarcode));
					//등록번호 처리를 위한 코드 끝
				}
				
				if(isbn10Idx >= 0) {
					bookHeld.setIsbn10(col.get("col"+isbn10Idx)[i]);
				}
				
				if(isbn13Idx >= 0) {
					bookHeld.setIsbn13(col.get("col"+isbn13Idx)[i]);
				}
				
				if(categoryIdx >= 0) {
					bookHeld.setCategory(col.get("col"+categoryIdx)[i]);
				}
				
				if(descriptionIdx >= 0) {
					bookHeld.setDescription(col.get("col"+descriptionIdx)[i]);
				}
				
				if(purOrDonIdx >= 0) {
					int pod = 1;
					if("기증".equals(col.get("col"+purOrDonIdx)[i])) {
						pod = 0;
					}
					bookHeld.setPurchasedOrDonated(pod);
				}
				
				if(availableIdx >= 0) {
					int available = 1;
					if("대출중".equals(col.get("col"+availableIdx)[i])) {
						available = 0;
					}
					bookHeld.setAvailable(available);
				}
				
				//기호류 등록 처리
				if(addiCodeIdx >= 0) {
					bookHeld.setAdditionalCode(col.get("col"+addiCodeIdx)[i]);
				}
				if(classCodeIdx >= 0) {
					bookHeld.setClassificationCode(col.get("col"+classCodeIdx)[i]);
				}
				if(authorCodeIdx >= 0) {
					bookHeld.setAuthorCode(col.get("col"+authorCodeIdx)[i]);
				}
				if(volumeCodeIdx >= 0) {
					bookHeld.setVolumeCode(col.get("col"+volumeCodeIdx)[i]);
				}
				if(copyCodeIdx >= 0) {
					int copyCode = 0;
					if(!"".equals(col.get("col"+copyCodeIdx)[i])) {
						copyCode = util.getNumFromCopyCode(col.get("col"+copyCodeIdx)[i]);
					}
					bookHeld.setCopyCode(copyCode);
				}
				//기호류 등록 처리 끝
				
				if(bookOrNotIdx >= 0) {
					String bon = util.getBookOrNot(col.get("col"+bookOrNotIdx)[i]);
					bookHeld.setBookOrNot(bon);
				}
				
				if(pageItemIdx >= 0) {
					int pageItem = 0;
					if(!"".equals(col.get("col"+pageItemIdx)[i])) {
						pageItem = util.numExtract(col.get("col"+pageItemIdx)[i]);
					}
					bookHeld.setPage(pageItem);
				}
				
				if(imageLinkIdx >= 0) {
					bookHeld.setImageLink(col.get("col"+imageLinkIdx)[i]);
				}
				
				//도서 등록 처리
				bookHeldService.insertBookHeld(bookHeld);
				//사용한 객체는 삭제처리.
				bookHeld = null;
			}
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		return web.redirect(web.getRootPath() + "/book/book_held_list.do", "도서 엑셀데이터 등록이 완료되었습니다.");
	}

	//map에서 value 값으로 key 값을 가져옴
	public static <K, V> K getKey(Map<K, V> map, V value) {
		for (K key : map.keySet()) {
			if(value.equals(map.get(key))) {
				return key;
			}
		}
		return null;
	}
}

