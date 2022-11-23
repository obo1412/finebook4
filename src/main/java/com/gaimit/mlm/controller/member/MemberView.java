package com.gaimit.mlm.controller.member;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
//import com.gaimit.helper.FileInfo;
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.WebHelper;

import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.model.Borrow;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class MemberView {
	/** log4j 객체 생성 및 사용할 객체 주입받기 */
	//private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
	// --> import study.spring.helper.WebHelper;
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
	BrwService brwService;
	
	/** 회원 상세 페이지 */
	@RequestMapping(value = "/member/member_view.do", method = RequestMethod.GET)
	public ModelAndView memberView(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		int memberId = web.getInt("memberId");
		
		// 파라미터를 저장할 Beans
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		member.setId(memberId);
		
		Borrow borrow = new Borrow();
		borrow.setIdLibBrw(loginInfo.getIdLibMng());
		borrow.setIdMemberBrw(memberId);
		
		List<Borrow> brwLog = null;
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		Member memberItem = null;
		String profileName = null;
		try {
			memberItem = memberService.selectMember(member);
			String profilePath = memberItem.getProfileImg();
			if(profilePath != null) {
				profileName = profilePath.substring(profilePath.lastIndexOf("/")+1);
			}
			
			brwLog = brwService.selectBorrowLogByMemberId(borrow);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("memberItem", memberItem);
		model.addAttribute("profileName", profileName);
		model.addAttribute("brwLog", brwLog);
		
		return new ModelAndView("member/member_view");
	}
	
	/* 회원 비활성화 ok 처리 */
	@RequestMapping(value = "/member/member_inactive_ok.do", method = RequestMethod.POST)
	public ModelAndView memberDelete(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			web.printJsonRt("로그인 상태가 아닙니다.");
		}
		
		int memberId = web.getInt("memberId");
		
		// 파라미터를 저장할 Beans
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		member.setId(memberId);
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		try {
			memberService.updateMemberInactive(member);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 *//*
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("memberItem", memberItem);
		
		return new ModelAndView("member/member_view");*/
		
		String urlTail = "/member/member_view.do?memberId=";
		//회원 번호
		urlTail = urlTail + memberId;
		//삭제 url 처리
		String deleteUrl = "&deleteMemberOk=true";
		urlTail = urlTail + deleteUrl;
		
		return web.redirect(web.getRootPath() + urlTail, null);
	}
	
	/** 회원 일괄 비활성화 비동기 */
	@ResponseBody
	@RequestMapping(value = "/member/member_inactive_ok_async.do", method = RequestMethod.POST)
	public void inactivateMemberBatchByCheckBox(Locale locale, Model model,
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
		
		int memberId = web.getInt("memberId");
		
		// 파라미터를 저장할 Beans
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		member.setId(memberId);
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		try {
			memberService.updateMemberInactive(member);
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
	
	
	/** 회원 수정 페이지 */
	@RequestMapping(value = "/member/member_edit.do", method = RequestMethod.GET)
	public ModelAndView memberEdit(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		int memberId = web.getInt("memberId");
		
		// 파라미터를 저장할 Beans
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		member.setId(memberId);
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		Member memberItem = null;
		String profileName = null;
		
		List<Member> gradeList = null;
		List<Member> classList = null;
		try {
			classList = memberService.selectMemberClassListByLib(member);
			gradeList = memberService.selectGrade(member);
			memberItem = memberService.selectMember(member);
			String profilePath = memberItem.getProfileImg();
			if(profilePath != null) {
				profileName = profilePath.substring(profilePath.lastIndexOf("/")+1);
			}
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("memberItem", memberItem);
		model.addAttribute("profileName", profileName);
		model.addAttribute("gradeList", gradeList);
		model.addAttribute("classList", classList);
		
		return new ModelAndView("member/member_edit");
	}
	
	@RequestMapping(value = "/member/member_edit_ok.do", method= RequestMethod.POST)
	public ModelAndView memberEditOk(Locale locale, Model model, HttpServletRequest request,
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

		// UploadHelper에서 텍스트 형식의 파라미터를 분류한 Map을 리턴받아서 값을 추출한다.
		Map<String, String> paramMap = upload.getParamMap();
		//join 페이지에서 전달받은 값
		String id = paramMap.get("memberId");
		String name = paramMap.get("name");
		String phone = paramMap.get("phone");
		String otherContact = paramMap.get("otherContact");
		String birthdate = paramMap.get("birthdate");
		String email = paramMap.get("email");
		String gradeStr = paramMap.get("grade");
		/*String postcode = paramMap.get("postcode");
		String addr1 = paramMap.get("addr1");
		String addr2 = paramMap.get("addr2");*/
		String rfuid = paramMap.get("rfuid");
		//String remarks = paramMap.get("remarks");
		String idMbrClassStr = paramMap.get("idMbrClass");
		Integer idMbrClass = null;
		if(idMbrClassStr!=null&&!"".equals(idMbrClassStr)) {
			idMbrClass = Integer.parseInt(idMbrClassStr);
		}

		// 전달받은 파라미터는 값의 정상여부 확인을 위해서 로그로 확인
		/*logger.debug("name=" + name);
		logger.debug("phone=" + phone);
		logger.debug("birthdate=" + birthdate);
		logger.debug("email=" + email);
		logger.debug("gradeStr=" + gradeStr);
		logger.debug("postcode=" + postcode);
		logger.debug("addr1=" + addr1);
		logger.debug("addr2=" + addr2);
		logger.debug("rfuid=" + rfuid);
		logger.debug("remarks=" + remarks);
		logger.debug("lastId=" + lastId);
		logger.debug("idLibString=" + idLibString);
		logger.debug("checkNum=" + checkNum);*/
		
		int memberId = Integer.parseInt(id);
		int gradeEdit = Integer.parseInt(gradeStr);

		/** (6) 업로드 된 파일 정보 추출 */
		/*List<FileInfo> fileList = upload.getFileList();
		// 업로드 된 프로필 사진 경로가 저장될 변수
		String profileImg = null;

		// 업로드 된 파일이 존재할 경우만 변수값을 할당한다.
		if (fileList.size() > 0) {
			// 단일 업로드이므로 0번째 항목만 가져온다.
			FileInfo info = fileList.get(0);
			profileImg = info.getFileDir() + "/" + info.getFileName();
		}

		// 파일경로를 로그로 기록
		logger.debug("profileImg=" + profileImg);*/

		/** (7) 전달받은 파라미터를 Beans 객체에 담는다. */
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		member.setId(memberId);
		member.setName(name);
		member.setPhone(phone);
		member.setOtherContact(otherContact);
		member.setBirthdate(birthdate);
		member.setEmail(email);
		/*member.setPostcode(postcode);
		member.setAddr1(addr1);
		member.setAddr2(addr2);*/
		/*member.setRemarks(remarks);*/
		/*member.setProfileImg(profileImg);*/
		member.setRfUid(rfuid);
		member.setGradeId(gradeEdit);
		member.setClassId(idMbrClass);

		Member memberItem = null;
		/** (8) Service를 통한 데이터베이스 저장 처리 */
		try {
			memberService.updateMember(member);
			memberItem = memberService.selectMember(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		model.addAttribute("memberItem", memberItem);

		/** (9) 가입이 완료되었으므로 메인페이지로 이동 */
		return web.redirect(web.getRootPath() + "/member/member_view.do?memberId="+memberId, "회원 정보가 수정되었습니다.");
	}
}
