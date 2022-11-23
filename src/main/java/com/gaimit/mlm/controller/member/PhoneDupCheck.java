package com.gaimit.mlm.controller.member;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.gaimit.helper.RegexHelper;
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class PhoneDupCheck {

	/** (1) 사용하고자 하는 Helper 객체 선언 */
	// --> import org.apache.logging.log4j.Logger;
	Logger logger = LoggerFactory.getLogger(PhoneDupCheck.class);
	// --> import org.apache.ibatis.session.SqlSession;

	// --> import study.jsp.helper.WebHelper;
	@Autowired
	WebHelper web;

	@Autowired
	MemberService memberService;

	@Autowired
	ManagerService managerService;

	// --> import study.jsp.helper.UploadHelper;
	@Autowired
	UploadHelper upload;
	
	@Autowired
	RegexHelper regex;
	
	/** 장서점검 생성 전체 status insertBcs */
	@ResponseBody
	@RequestMapping(value = "/member/phone_dup_check.do", method = RequestMethod.GET)
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
			return;
		}
		
		String checkMsg = "사용 가능한 번호 입니다.";
		String phoneNum = web.getString("phone");
		int checkResult = 0;
		
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		member.setPhone(phoneNum);
		
		try {
			checkResult = memberService.selectPhoneCount(member);
			if(checkResult > 0) {
				checkMsg = "중복된 번호가 존재합니다.";
			}
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		// --> import java.util.HashMap;
		// --> import java.util.Map;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("checkResult", checkResult);
		data.put("checkMsg", checkMsg);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}

	@RequestMapping(value = "/member/phone_dup_check___________xxxxxxxxxxxxxxxx_not_use.do")
	public ModelAndView doRun(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {

		/** (2) WebHelper 초기화 */
		web.init();

		/** (3) 로그인 여부 검사 */
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		int idLib = 0;
		// 관리자 로그인 중이라면 관리자의 도서관 id를 가져온다.
		if (web.getSession("loginInfo") == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인이 필요합니다.");
		} else {
			idLib = loginInfo.getIdLibMng();
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

		// UploadHelper에서 텍스트 형식의 파라미터를 분류한 Map을 리턴받아서 값을 추출한다.
		Map<String, String> paramMap = upload.getParamMap();
		// join 페이지에서 전달받은 값
		String name = paramMap.get("name");
		String phone = paramMap.get("phone");
		String birthdate = paramMap.get("birthdate");
		String email = paramMap.get("email");
		String gradeStr = paramMap.get("grade");
		String postcode = paramMap.get("postcode");
		String addr1 = paramMap.get("addr1");
		String addr2 = paramMap.get("addr2");
		String rfuid = paramMap.get("rfuid");
		String remarks = paramMap.get("remarks");

		// 전달받은 파라미터는 값의 정상여부 확인을 위해서 로그로 확인
		logger.debug("name=" + name);
		logger.debug("phone=" + phone);
		logger.debug("birthdate=" + birthdate);
		logger.debug("email=" + email);
		logger.debug("gradeStr=" + gradeStr);
		logger.debug("postcode=" + postcode);
		logger.debug("addr1=" + addr1);
		logger.debug("addr2=" + addr2);
		logger.debug("rfuid=" + rfuid);
		logger.debug("remarks=" + remarks);

		// paramMap으로 받은 String grade를 int형으로 변환
		// int grade = Integer.parseInt(gradeStr);
		
		/** (5) 입력값의 유효성 검사 */
		if (!regex.isValue(phone)) {
			return web.redirect(null, "전화번호를 입력하세요.");
		}
		
		/*if (!regex.isCellPhone(phone) && !regex.isTel(phone)) {
			return web.redirect(null, "연락처의 형식이 잘못되었습니다.");
		}*/

		Member member = new Member();
		member.setIdLib(idLib);
		member.setPhone(phone);
		
		try {
			/*memberService.selectPhoneCount(member);*/
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		String checkMsg = "가입 가능한 전화 번호 입니다.";
		String checkNum = phone;

		/** (8) Service를 통한 데이터베이스 저장 처리 */
		List<Member> gradeList = null;
		// Integer lastId = new Integer(0);
		try {
			// lastId = memberService.selectLastJoinedId();
			gradeList = memberService.selectGrade(member);
		} catch (Exception e) {
			// lastId 초기값이 null 이라서 NPE 을 빼버림
			/* return web.redirect(null, e.getLocalizedMessage()); */
		}

		/*
		 * if (lastId.equals(null)) { lastId = 0; }
		 */

		// model.addAttribute("lastId", lastId);
		model.addAttribute("name", name);
		model.addAttribute("phone", phone);
		model.addAttribute("birthdate", birthdate);
		model.addAttribute("email", email);
		// model.addAttribute("grade", grade);
		model.addAttribute("postcode", postcode);
		model.addAttribute("addr1", addr1);
		model.addAttribute("addr2", addr2);
		model.addAttribute("rfuid", rfuid);
		model.addAttribute("remarks", remarks);

		model.addAttribute("idLib", idLib);
		model.addAttribute("gradeList", gradeList);
		model.addAttribute("checkMsg", checkMsg);
		model.addAttribute("checkNum", checkNum);

		return new ModelAndView("member/join");
	}
}
