package com.gaimit.mlm.controller.member;


import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gaimit.helper.PageHelper;
import com.gaimit.helper.WebHelper;

import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;

@Controller
public class Grade {
	/** log4j 객체 생성 및 사용할 객체 주입받기 */
	// --> import org.apache.logging.log4j.Logger;
	Logger logger = LoggerFactory.getLogger(Grade.class);
	// --> import study.spring.helper.WebHelper;
	@Autowired
	WebHelper web;
	
	@Autowired
	PageHelper page;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	ManagerService managerService;
	
	/** 등급 목록 페이지 */
	@RequestMapping(value = "/member/grade_list.do", method = {RequestMethod.GET, RequestMethod.POST})
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
		
		//String MemberName = web.getString("name", "");
		
		// 파라미터를 저장할 Beans
		Member member = new Member();
		member.setIdLib(idLib);
		
		// 검색어 파라미터 받기 + Beans 설정
		String keyword = web.getString("keyword", "");
		member.setGradeName(keyword);
		
		// 현재 페이지 번호에 대한 파라미터 받기
		int nowPage = web.getInt("page", 1);
		
		/** 2) 페이지 번호 구현하기 */
		// 전체 데이터 수 조회하기
		int totalCount = 0;
		try {
			totalCount = memberService.selectGradeCountForPage(member);
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		// 페이지 번호에 대한 연산 수행 후 조회조건값 지정을 위한 Beans에 추가하기
		page.pageProcess(nowPage, totalCount, 10, 5);
		member.setLimitStart(page.getLimitStart());
		member.setListCount(page.getListCount());
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		List<Member> gradeList = null;
		try {
			/*if(MemberName.equals("")) {*/
				gradeList = memberService.selectGrade(member);
			/*} else {
				list = memberService.getMemberListByLibAndName(member);
			}*/
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("gradeList", gradeList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		
		return new ModelAndView("member/grade_list");
	}
	
	/** 등급 정보 상세보기 페이지 */
	@RequestMapping(value = "/member/grade_edit.do", method = RequestMethod.GET)
	public ModelAndView GradeEdit(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		int idLib = 0;
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			idLib = loginInfo.getIdLibMng();
		}
		
		int gradeId = web.getInt("gradeId");
		logger.debug("gradeId=" + gradeId);
		
		if (gradeId == 0) {
			return web.redirect(null, "해당하는 등급이 없습니다.");
		}
		
		// 전달된 파라미터를 Beans에 저장한다.
		Member member = new Member();
		member.setGradeId(gradeId);
		member.setIdLib(idLib);
		
		/** 2) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		Member item = null;
		try {
			item = memberService.getGradeItem(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 3) View 처리하기 */
		model.addAttribute("item", item);
		
		return new ModelAndView("member/grade_edit");
	}
	
	/** 등급 정보 수정 처리 */
	@RequestMapping(value = "/member/grade_edit_ok.do", method = RequestMethod.POST)
	public ModelAndView GradeEditOk(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		int idLib = 0;
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			idLib = loginInfo.getIdLibMng();
		}
		
		int gradeId = web.getInt("gradeId");
		String gradeName = web.getString("gradeName");
		int brwLimit = web.getInt("brwLimit");
		int dateLimit = web.getInt("dateLimit");
		int standard = web.getInt("standard", 0);
		
		logger.debug("gradeId=" + gradeId);
		logger.debug("gradeName=" + gradeName);
		logger.debug("brwLimit=" + brwLimit);
		logger.debug("dateLimit=" + dateLimit);
		logger.debug("standard=" + standard);
		
		if (gradeId == 0) {
			return web.redirect(null, "해당하는 등급이 없습니다.");
		}
		if (gradeName == null) {
			return web.redirect(null, "등급이름을 입력하세요.");
		}
		if (brwLimit == 0) {
			return web.redirect(null, "대여가능권수를 입력하세요.");
		}
		if (dateLimit == 0) {
			return web.redirect(null, "대여기한을 입력하세요.");
		}
		
		// 전달된 파라미터를 Beans에 저장한다.
		Member member = new Member();
		member.setGradeId(gradeId);
		member.setGradeName(gradeName);
		member.setBrwLimit(brwLimit);
		member.setDateLimit(dateLimit);
		member.setIdLib(idLib);
		member.setStandard(standard);
		
		/** 2) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		Member item = null;
		int changeStd = 0;
		try {
			/*기준등급인지 조회를 위한 호출*/
			item = memberService.getGradeItem(member);
			if(item.getStandard() == 1 && standard == 0) {
				return web.redirect(null, "기준 등급은 반드시 하나가 존재해야합니다.");
			} else if(item.getStandard()==0 && standard==1) {
				memberService.updateMemberGradeStandardToNormal(member);
				changeStd = 1;
			}
			memberService.updateGrade(member);
			item = memberService.getGradeItem(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 3) View 처리하기 */
		model.addAttribute("item", item);
		
		/** 5) 결과를 확인하기 위한 페이지로 이동하기 */
		String url = web.getRootPath() + "/member/grade_edit.do?gradeId=" + member.getGradeId();
		String msg = "등급정보가 수정되었습니다.";
		if(changeStd == 1) {
			msg = "기준 등급과 등급 정보가 수정되었습니다.";
		}
		return web.redirect(url, msg);
	}
	
	
	
	/** 등급 추가 페이지 */
	@RequestMapping(value = "/member/grade_add.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView GradeAdd(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		/** 5)등급 추가 페이지로 이동하기 */
		return new ModelAndView("member/grade_add");
	}
	
	
	/** 등급 정보 수정 처리 */
	@RequestMapping(value = "/member/grade_add_ok.do", method = RequestMethod.POST)
	public ModelAndView GradeAddOk(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		String gradeName = web.getString("gradeName");
		int brwLimit = web.getInt("brwLimit");
		int dateLimit = web.getInt("dateLimit");
		
		logger.debug("gradeName=" + gradeName);
		logger.debug("brwLimit=" + brwLimit);
		logger.debug("dateLimit=" + dateLimit);
		
		if (gradeName == null) {
			return web.redirect(null, "등급이름을 입력하세요.");
		}
		if (brwLimit == 0) {
			return web.redirect(null, "대여가능권수를 입력하세요.");
		}
		if (dateLimit == 0) {
			return web.redirect(null, "대여기한을 입력하세요.");
		}
		
		// 전달된 파라미터를 Beans에 저장한다.
		Member member = new Member();
		member.setGradeName(gradeName);
		member.setBrwLimit(brwLimit);
		member.setDateLimit(dateLimit);
		member.setStandard(0);
		member.setIdLib(loginInfo.getIdLibMng());
		
		/** 2) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
	
		try {
			int std = memberService.selectGradeStandardCount(member);
			if(std < 1) {
				member.setStandard(1);
			}
			memberService.selectGradeNameCount(member);
			memberService.insertGrade(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 3) View 처리하기 */
		
		/** 5) 결과를 확인하기 위한 페이지로 이동하기 */
		String url = web.getRootPath() + "/member/grade_list.do";
		return web.redirect(url, "등급정보가 추가되었습니다.");
	}
	
	/** 등급 정보 수정 처리 */
	@RequestMapping(value = "/member/grade_delete_ok.do", method = RequestMethod.POST)
	public ModelAndView GradeDeleteOk(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		int gradeId = web.getInt("gradeId");
		
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		member.setGradeId(gradeId);
		
		logger.debug("gradeId=" + gradeId);
	
		try {
			memberService.updateMemberGradeStandardToDelete(member);
			memberService.deleteMemberGrade(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 3) View 처리하기 */
		
		/** 5) 결과를 확인하기 위한 페이지로 이동하기 */
		String url = web.getRootPath() + "/member/grade_list.do";
		return web.redirect(url, "등급 정보가 삭제되었습니다.");
	}
}
