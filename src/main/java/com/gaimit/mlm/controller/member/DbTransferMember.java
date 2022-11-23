package com.gaimit.mlm.controller.member;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.controller.FrequentlyFunction;
import com.gaimit.mlm.model.BbsFile;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.service.BbsFileService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class DbTransferMember {

	/** (1) 사용하고자 하는 Helper 객체 선언 */
	private static Logger logger = LoggerFactory.getLogger(DbTransferMember.class);
	// --> import study.jsp.helper.WebHelper;
	@Autowired
	WebHelper web;
	
	@Autowired
	UploadHelper upload;
	
	@Autowired
	Util util;

	@Autowired
	MemberService memberService;

	@Autowired
	ManagerService managerService;
	
	@Autowired
	BbsFileService bbsFileService;
	
	@Autowired
	FrequentlyFunction frequentlyFunction;
	
	static int dataTransStopper = 0;
	static List<String> libArr = new ArrayList<String>();

	@RequestMapping(value = "/member/import_member_excel.do")
	public ModelAndView importMemberExcelPage(Locale locale, Model model, 
			HttpServletRequest request, HttpServletResponse response) {

		/** (2) WebHelper 초기화 */
		web.init();

		/** (3) 로그인 여부 검사 */
		//Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 관리자 로그인 중이라면 관리자의 도서관 id를 가져온다.
		if (web.getSession("loginInfo") == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인이 필요합니다.");
		} 
		
		try {
			
		} catch (Exception e) {
			
		}

		return new ModelAndView("member/import_member_excel");
	}
	
	@RequestMapping(value = "/member/import_member_excel_check.do")
	public ModelAndView importMemberExcelCheck(Locale locale, Model model, HttpServletRequest request,
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
								/*logger.info("colArr["+j+"]["+k+"]의 값 = "+theArr[j][k]);*/
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
					String[][] theArr = new String[seeRow][lastCellCount];
					
					// j < row_crt		k < lastCellCount 컬럼은 그대로 가져오기.
					for (int j=0; j<seeRow; j++) {
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
		return new ModelAndView("member/import_member_excel_check");
	}
	
	@ResponseBody
	@RequestMapping(value= "/member/import_member_excel_ok.do", method = RequestMethod.POST)
	public void importMemberExcelOkAsync(Locale locale, Model model,
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
		
		try {
			//컬럼 제목이 담길 배열 생성
			String[] colList = null;
			// 셀렉트가 하나 이상있으면 colList에 셀렉트 값으로 가고,
			// 아예 없으면 자동으로 컬럼매칭
			int selectSwitch = 0;
			
			if(col_count > 0) {
				colList = new String[col_count];
				for(int i=0; i<col_count; i++) {
					colList[i] = web.getString("colH"+i);
					if(colList[i]!=null) {
						//셀렉트에서 선택된 값이 있으면, 스위치 +1
						selectSwitch++;
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
				
				//workbook 불러오기 끝
				workbook.close();
				
				//int row_crt = curSheet.getPhysicalNumberOfRows();
				//logger.info("최종 행 번호 = "+row_crt);
				//최초 행의 마지막 셀 수 = 마지막 컬럼(수평) 번호
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
					Member member = new Member();
					member.setIdLib(loginInfo.getIdLibMng());
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
							if(colList[k]!=null&&cellValue!=null&&!"".equals(cellValue)&&!"null".equals(cellValue)) {
								//조건문이 null 이되면 아예 실행을 안한다. 따라서 위 if 조건 필요.
								switch(colList[k]) {
								case "RF_UID":
								case "rfUid":
									member.setRfUid(cellValue);
									break;
								case "고객등급":
								case "gradeName":
									int gradeIdTemp = frequentlyFunction.getGradeIdByGradeName(cellValue, loginInfo.getIdLibMng());
									member.setGradeId(gradeIdTemp);
									break;
								case "고객등록일자":
								case "regDate":
									member.setRegDate(cellValue);
									break;
								case "고객바코드번호":
								case "barcodeMbr":
									member.setBarcodeMbr(cellValue);
									break;
								case "고객성명":
								case "name":
									member.setName(cellValue);
									break;
								case "핸드폰":
								case "phone":
									member.setPhone(cellValue);
									break;
								case "이메일주소":
								case "email":
									member.setEmail(cellValue);
									break;
								case "우편번호":
								case "postcode":
									member.setPostcode(cellValue);
									break;
								case "주소1":
								case "addr1":
									member.setAddr1(cellValue);
									break;
								case "주소2":
								case "addr2":
									member.setAddr2(cellValue);
									break;
								case "추가메모":
								case "remarks":
									member.setRemarks(cellValue);
									break;
								case "생년":
								case "birthdate":
									member.setBirthdate(util.getDateValue(cellValue));
									break;
								}
							} //스위치 null 조건 if문 끝
							
						} //for문 컬럼(열) k 셀 돌기 끝
						logger.info(curRow+"번째 :"+member.getName());
						//복붙으로 인한 안쓰는 로직
						//writer 즉 저자 값이 null 이면 저자 미상으로 변경 처리.
						/*if(bookHeld.getWriter()==null||"".equals(bookHeld.getWriter())) {
							bookHeld.setWriter("저자 미상");
						}*/
						//logger.info("bookHeld title: "+bookHeld.getTitle()+" writer: "+bookHeld.getWriter()+" publisher: "+bookHeld.getPublisher()+" classcode: "+bookHeld.getClassificationCode());
						//insert 문 명시
						memberService.insertMember(member);
						
						curStepMsg = curRow+"번째 행 등록됨: "+member.getName();
						
						//사용한 객체는 삭제처리.
						member = null;
					} //if문 row 가 존재하는지 끝
				
				} //row 행 for 문 끝
				//model.addAttribute("theArr", theArr);
				
				//위에서 닫아줌
				//workbook.close();
				
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
				workbook.close();
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
	@RequestMapping(value= "/member/import_member_excel_pause.do", method = RequestMethod.POST)
	public void importMemberExcelPause(Locale locale, Model model,
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
	@RequestMapping(value= "/member/import_member_excel_display_current_progress.do", method = RequestMethod.GET)
	public void importMemberExcelDisplayCurData(Locale locale, Model model,
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
		
		Member dcdMember = new Member();
		dcdMember.setIdLib(loginInfo.getIdLibMng());
		dcdMember.setEditDate(timeStandard);
		
		int regOkRow = 0;
		
		try {
			regOkRow = memberService.selectCountMemberImportExcelData(dcdMember);
			logger.info(regOkRow+" 진행중 - 회원등록 진행중 몇번째인지 체크");
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
	
	//웹페이지에 데이터를 다 가지고 들어와서 옮기는 방식 사용 x
	@RequestMapping(value = "/member/import_member_excel_check_xxxxxx.do")
	public ModelAndView importMemberExcelCheckXX(Locale locale, Model model, HttpServletRequest request,
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
					
					String[][] theArr = new String[row_crt][lastCellCount];
					
					
					for (int j=0; j<row_crt; j++) {
						//i번 행을 읽어온다.
						XSSFRow horizonRow = curSheet.getRow(j);
						if(horizonRow != null) {
							for(int k=0; k<lastCellCount; k++) {
								theArr[j][k] = String.valueOf(horizonRow.getCell(k));
								/*logger.info("colArr["+j+"]["+k+"]의 값 = "+theArr[j][k]);*/
							}
						}
					}
					model.addAttribute("theArr", theArr);
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
					workbook.close();
				}
				//파일 호출 후, 반복 사용하지 않는 파일 삭제처리.
				upload.removeFile(loadFilePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		/** (9) 가입이 완료되었으므로 메인페이지로 이동 */
		return new ModelAndView("member/import_member_excel_check");
	}
	
	/** 회원정보 가져오기 완료 안쓰는 방식 */
	@ResponseBody
	@RequestMapping(value = "/member/import_member_excel_ok_xxxxxx.do", method = RequestMethod.POST)
	public void importMemberExcelOkXXXX(Locale locale, Model model,
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
		
		//도서관 번호
		int idLib = loginInfo.getIdLibMng();
		
		String[] nameArr = web.getStringArray("nameArr");
		String[] phoneArr = web.getStringArray("phoneArr");
		String[] birthdateArr = web.getStringArray("birthdateArr");
		String[] emailArr = web.getStringArray("emailArr");
		String[] postcodeArr = web.getStringArray("postcodeArr");
		String[] addr1Arr = web.getStringArray("addr1Arr");
		String[] addr2Arr = web.getStringArray("addr2Arr");
		String[] remarksArr = web.getStringArray("remarksArr");
		String[] regDateArr = web.getStringArray("regDateArr");
		String[] barcodeMbrArr = web.getStringArray("barcodeMbrArr");
		String[] profileImgArr = web.getStringArray("profileImgArr");
		String[] rfUidArr = web.getStringArray("rfUidArr");
		//gradeId 라고 적혀있지만 받아오는 것은 gradeName
		String[] gradeNameArr = web.getStringArray("gradeNameArr");
		String[] statusArr = web.getStringArray("statusArr");
		
		if(nameArr != null) {
			for(int i=0; i<nameArr.length; i++) {
				System.out.println(nameArr[i]);
			}
		}
		
		if(nameArr == null) {
			web.printJsonRt("회원명 선택이 누락되었습니다.");
			return;
		}
		
		/*if(birthdateArr == null) {
			web.printJsonRt("회원의 생년월일 선택이 누락되었습니다.");
			return;
		}*/
		
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		
		try {
			for(int i=0; i<nameArr.length; i++) {
				member.setName(nameArr[i]);
				if(phoneArr != null) {
					member.setPhone(phoneArr[i]);
				}
				if(birthdateArr != null) {
					member.setBirthdate(birthdateArr[i]);
				}
				if(emailArr != null) {
					member.setEmail(emailArr[i]);
				}
				if(postcodeArr != null) {
					member.setPostcode(postcodeArr[i]);
				}
				if(addr1Arr != null) {
					member.setAddr1(addr1Arr[i]);
				}
				if(addr2Arr != null) {
					member.setAddr2(addr2Arr[i]);
				}
				if(remarksArr != null) {
					member.setRemarks(remarksArr[i]);
				}
				if(regDateArr != null) {
					member.setRegDate(regDateArr[i]);
				}
				
				if(barcodeMbrArr != null) {
					member.setBarcodeMbr(barcodeMbrArr[i]);
				} else {
					member.setBarcodeMbr(util.getRandomPassword(7));
				}
				
				if(profileImgArr != null) {
					member.setProfileImg(profileImgArr[i]);
				}
				if(rfUidArr != null) {
					member.setRfUid(rfUidArr[i]);
				}
				
				if(gradeNameArr != null) {
					int gradeIdTemp = frequentlyFunction.getGradeIdByGradeName(gradeNameArr[i], idLib);
					member.setGradeId(gradeIdTemp);
				} else {
					member.setGradeId(0);
				}
				
				if(statusArr != null) {
					member.setStatus(Integer.parseInt(statusArr[i]));
				} else {
					member.setStatus(1);
				}
				
				//DB 입력처리
				memberService.insertMember(member);
				
				//멤버 정보 초기화.
				member.setName(null);
				member.setPhone(null);
				member.setBirthdate(null);
				member.setEmail(null);
				member.setPostcode(null);
				member.setAddr1(null);
				member.setAddr2(null);
				member.setRemarks(null);
				member.setRegDate(null);
				member.setBarcodeMbr(null);
				member.setProfileImg(null);
				member.setRfUid(null);
				member.setGradeId(0);
				member.setStatus(0);
			}
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("nextPage", "/member/import_member_excel.do");
		data.put("msg", "회원정보 이관이 완료되었습니다.");
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
}
