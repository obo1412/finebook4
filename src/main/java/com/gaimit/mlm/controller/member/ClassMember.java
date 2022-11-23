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
public class ClassMember {
	/** log4j 객체 생성 및 사용할 객체 주입받기 */
	// --> import org.apache.logging.log4j.Logger;
	Logger logger = LoggerFactory.getLogger(ClassMember.class);
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
	@RequestMapping(value = "/member/class_member_list.do", method = RequestMethod.GET)
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
		member.setClassName(keyword);
		
		// 현재 페이지 번호에 대한 파라미터 받기
		int nowPage = web.getInt("page", 1);
		
		/** 2) 페이지 번호 구현하기 */
		// 전체 데이터 수 조회하기
		int totalCount = 0;
		try {
			totalCount = memberService.selectMemberClassCountForPage(member);
		}  catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		// 페이지 번호에 대한 연산 수행 후 조회조건값 지정을 위한 Beans에 추가하기
		page.pageProcess(nowPage, totalCount, 10, 5);
		member.setLimitStart(page.getLimitStart());
		member.setListCount(page.getListCount());
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		List<Member> classList = null;
		try {
			//serviceImpl 에서 null처리 NPE를 하긴했으나,
			//결과 값이 아예 없어도 classList는 [] 이와 같이 괄호 값이 생김
			//그러므로 ""공백처럼 비어있긴하지만 null은 아님
			//NPE 처리는 의미가 없긴함.
			classList = memberService.selectMemberClassListByLib(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("classList", classList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		
		return new ModelAndView("member/class_member_list");
	}
	
	/** 등급 정보 상세보기 페이지 */
	@RequestMapping(value = "/member/class_member_edit.do", method = RequestMethod.GET)
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
		
		int idMbrClass = web.getInt("idMbrClass");
		logger.debug("idMbrClass=" + idMbrClass);
		
		if (idMbrClass == 0) {
			return web.redirect(null, "해당하는 분류가 없습니다.");
		}
		
		// 전달된 파라미터를 Beans에 저장한다.
		Member member = new Member();
		member.setIdMbrClass(idMbrClass);
		member.setIdLib(idLib);
		
		/** 2) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		try {
			member = memberService.selectClassMemberItem(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 3) View 처리하기 */
		model.addAttribute("item", member);
		
		return new ModelAndView("member/class_member_edit");
	}
	
	/** 등급 정보 수정 처리 */
	@RequestMapping(value = "/member/class_member_edit_ok.do", method = RequestMethod.POST)
	public ModelAndView GradeEditOk(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		int idMbrClass = web.getInt("idMbrClass");
		String className = web.getString("className");
		
		logger.debug("idMbrClass=" + idMbrClass);
		logger.debug("className=" + className);
		

		if (className == null) {
			return web.redirect(null, "분류명을 입력하세요.");
		}
		
		// 전달된 파라미터를 Beans에 저장한다.
		Member member = new Member();
		member.setIdMbrClass(idMbrClass);
		member.setClassName(className);
		member.setIdLib(loginInfo.getIdLibMng());
		
		/** 2) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		Member item = null;
		try {
			//분류명 중복체크
			memberService.selectClassNameCountForDup(member);
			memberService.updateClassMemberItem(member);
			item = memberService.selectClassMemberItem(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 3) View 처리하기 */
		model.addAttribute("item", item);
		
		/** 5) 결과를 확인하기 위한 페이지로 이동하기 */
		String url = web.getRootPath() + "/member/class_member_edit.do?idMbrClass=" + member.getIdMbrClass();
		
		return web.redirect(url, "회원 분류 정보가 수정되었습니다.");
	}
	
	
	
	/** 등급 추가 페이지 */
	@RequestMapping(value = "/member/class_member_add.do", method = RequestMethod.GET)
	public ModelAndView GradeAdd(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		/** 5)등급 추가 페이지로 이동하기 */
		return new ModelAndView("member/class_member_add");
	}
	
	
	/** 등급 정보 수정 처리 */
	@RequestMapping(value = "/member/class_member_add_ok.do", method = RequestMethod.POST)
	public ModelAndView GradeAddOk(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		String className = web.getString("className");
		
		logger.debug("className=" + className);
		
		if (className == null) {
			return web.redirect(null, "등급이름을 입력하세요.");
		}
		
		// 전달된 파라미터를 Beans에 저장한다.
		Member member = new Member();
		member.setClassName(className);
		member.setIdLib(loginInfo.getIdLibMng());
		
		/** 2) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
	
		try {
			memberService.selectClassNameCountForDup(member);
			memberService.insertClassMember(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 3) View 처리하기 */
		
		/** 5) 결과를 확인하기 위한 페이지로 이동하기 */
		String url = web.getRootPath() + "/member/class_member_list.do";
		return web.redirect(url, "분류정보가 추가되었습니다.");
	}
	
	/** 등급 정보 수정 처리 */
	@RequestMapping(value = "/member/class_member_delete_ok.do", method = RequestMethod.POST)
	public ModelAndView GradeDeleteOk(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		int idMbrClass = web.getInt("idMbrClass");
		
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		member.setIdMbrClass(idMbrClass);
		//삭제를 위한 회원테이블의 class_id
		member.setClassId(idMbrClass);
		logger.debug("idMbrClass=" + idMbrClass);
	
		try {
			//회원분류를 포함한 회원의 회원분류 업데이트
			memberService.updateMemberClassIdToNull(member);
			
			//회원분류삭제
			memberService.deleteClassMemberItem(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 3) View 처리하기 */
		
		/** 5) 결과를 확인하기 위한 페이지로 이동하기 */
		String url = web.getRootPath() + "/member/class_member_list.do";
		return web.redirect(url, "분류 정보가 삭제되었습니다.");
	}
}
