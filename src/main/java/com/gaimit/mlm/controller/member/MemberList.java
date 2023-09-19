package com.gaimit.mlm.controller.member;


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

import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class MemberList {
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
	
	/** 교수 목록 페이지 */
	@RequestMapping(value = "/member/member_list.do", method = {RequestMethod.GET, RequestMethod.POST})
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
		
		// 파라미터를 저장할 Beans
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		/*member.setName(MemberName);*/
		
		// 검색어 파라미터 받기 + Beans 설정
		int searchClassId = web.getInt("searchClassId");
		//System.out.println("분류id ["+searchClassId+"]");
		//반분류 검색 키워드
		member.setClassId(searchClassId);
		
		String keyword = web.getString("keyword", "");
		String keywordHolder = web.getString("keywordHolder", "");
		
		int keySelector = 0;
		if((keyword==null||keyword=="")&&(keywordHolder==null||keywordHolder=="")) {
			//keyword 값 0, keywordHolder 값 0.
		} else if((keyword!=null&&keyword!="")&&(keywordHolder==null||keywordHolder=="")) {
			//keyword 값은 있고, keywordHolder 값 없을때.
			keySelector = 1;
		} else if((keyword==null||keyword=="")&&(keywordHolder!=null&&keywordHolder!="")) {
			//keyword 값은 없고, keywordHolder 값 있을때.
			keySelector = 2;
		} else if((keyword!=null&&keyword!="")&&(keywordHolder!=null&&keywordHolder!="")) {
			//값 둘다 있을 때.
			keySelector = 3;
		}
		
		switch(keySelector) {
		case 0:
			break;
			
		case 1: case 3:
			member.setName(keyword);
			// 키워드가 공백이 아니면, 키워드 홀더에 값 대입.
			keywordHolder = keyword;
			break;

		case 2:
			member.setName(keywordHolder);
			break;
		}
		
		

		
		// 현재 페이지 번호에 대한 파라미터 받기
		int nowPage = web.getInt("page", 1);
		
		/** 2) 페이지 번호 구현하기 */
		// 전체 데이터 수 조회하기
		int totalCount = 0;
		try {
			totalCount = memberService.getMemberCount(member);
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		// 페이지 번호에 대한 연산 수행 후 조회조건값 지정을 위한 Beans에 추가하기
		page.pageProcess(nowPage, totalCount, 36, 5);
		member.setLimitStart(page.getLimitStart());
		member.setListCount(page.getListCount());
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		List<Member> list = null;
		List<Member> classList = null;
		try {
			list = memberService.getMemberListByLib(member);
			classList = memberService.selectMemberClassListByLib(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("list", list);
		model.addAttribute("classList", classList);
		model.addAttribute("searchClassId", searchClassId);
		model.addAttribute("keyword", keyword);
		model.addAttribute("keywordHolder", keywordHolder);
		model.addAttribute("page", page);
		model.addAttribute("pageDefUrl", "/member/member_list.do");
		
		return new ModelAndView("member/member_list");
	}
	
	
	/** 회원 목록 엑셀화 페이지 */
	@ResponseBody
	@RequestMapping(value = "/member/member_list_to_excel.do", method = RequestMethod.POST)
	public void excelExtractMemberList(Locale locale, Model model,
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
		
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		
		List<Member> memberList = null;
		
		String defaultPath = "/var/packages/Tomcat7/target/src/webapps/downloads/upload/finebook4/excel/libNo"+loginInfo.getIdLibMng();
		String filePath = defaultPath+"/"+"회원목록.xlsx";
		
		FileOutputStream fos = null;
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("회원목록");
		XSSFRow titleRow = null;
		XSSFRow row = null;
		
		//엑셀로 내보내기 위한 준비
		
		try {
			memberList = memberService.selectMemberListToExcel(member);
			int curCol = 0;
			titleRow = sheet.createRow(0);
			titleRow.createCell(curCol).setCellValue("번호");
			curCol++;
			titleRow.createCell(curCol).setCellValue("회원명");
			curCol++;
			titleRow.createCell(curCol).setCellValue("생년월일");
			curCol++;
			titleRow.createCell(curCol).setCellValue("연락처");
			curCol++;
			titleRow.createCell(curCol).setCellValue("추가연락처");
			curCol++;
			titleRow.createCell(curCol).setCellValue("이메일");
			curCol++;
			titleRow.createCell(curCol).setCellValue("회원분류");
			curCol++;
			titleRow.createCell(curCol).setCellValue("회원등급");
			curCol++;
			titleRow.createCell(curCol).setCellValue("대출권수");
			curCol++;
			titleRow.createCell(curCol).setCellValue("대출기한");
			curCol++;
			titleRow.createCell(curCol).setCellValue("주소1");
			curCol++;
			titleRow.createCell(curCol).setCellValue("주소2(상세)");
			curCol++;
			titleRow.createCell(curCol).setCellValue("우편번호");
			curCol++;
			titleRow.createCell(curCol).setCellValue("가입일");
			curCol++;
			titleRow.createCell(curCol).setCellValue("회원등록번호");
			curCol++;
			titleRow.createCell(curCol).setCellValue("RF-UID");
			
			for(int i=1; i<=memberList.size(); i++) {
				int j = i-1;
				int curValueCol = 0;
				row = sheet.createRow(i);
				row.createCell(curValueCol).setCellValue(i);
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getName());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getBirthdate());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getPhone());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getOtherContact());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getEmail());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getClassName());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getGradeName());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getBrwLimit());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getDateLimit());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getAddr1());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getAddr2());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getPostcode());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getRegDate());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getBarcodeMbr());
				curValueCol++;
				row.createCell(curValueCol).setCellValue(memberList.get(j).getRfUid());

//				if(bookHeldList.get(j).getPubDate() != null ) {
//					String tempDate = bookHeldList.get(j).getPubDate();
//					String pubYear = tempDate.substring(0,4);
//					row.createCell(curValueCol).setCellValue(pubYear);
//				}

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
