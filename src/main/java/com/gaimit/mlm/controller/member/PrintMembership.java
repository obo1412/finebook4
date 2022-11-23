package com.gaimit.mlm.controller.member;

import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gaimit.helper.PageHelper;
import com.gaimit.helper.QRCodeHelper;
import com.gaimit.helper.RegexHelper;
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;

import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.Member;

import com.gaimit.mlm.controller.FrequentlyFunction;

import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;


@Controller
public class PrintMembership {
	/** log4j 객체 생성 및 사용할 객체 주입받기 */
	//private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
	// --> import study.spring.helper.WebHelper;
	@Autowired
	WebHelper web;
	// --> import study.jsp.helper.RegexHelper;
	@Autowired
	RegexHelper regex;
	
	@Autowired
	PageHelper page;
	
	@Autowired
	Util util;
	
	@Autowired
	UploadHelper upload;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	QRCodeHelper qrCode;
	
	@Autowired
	FrequentlyFunction frequentlyFunction;
	
	
	
	/** 라벨출력 페이지 */
	@RequestMapping(value = "/member/print_membership.do", method = RequestMethod.GET)
	public ModelAndView printMembership(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		//url get방식으로 string 하나로 가져옴.
		String MemberIdString = web.getString("chkBoxArr");
		String[] memberIdArr = null;
		
		if(MemberIdString!=null&&!"".equals(MemberIdString)) {
			//전체 인쇄
			memberIdArr = MemberIdString.split(",");
		}
		
		//System.out.println("memberidstr: ["+MemberIdString+"]");
		//여기서 arr 저런식으로 쓰면, 변수주소를 알려준다. 
		//java.lang.String;@12312 이딴식으로
		//문자그대로 'null' 표기가 되는건 아닌듯.
		//System.out.println("memberIdArr: ["+memberIdArr+"]");
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		Member member = new Member();
		member.setIdLib(loginInfo.getIdLibMng());
		
		//하나씩 조회한 도서 정보를 넣을 리스트
		List<Member> memberList = new ArrayList<Member>();
		
		try {
			//arr 길이가 0이상이면 시행.
			if(memberIdArr!=null) {
				//arr에 담긴 member id 만큼 회원 정보 도출
				for(int i=0; i<memberIdArr.length; i++) {
					member.setId(Integer.parseInt(memberIdArr[i]));
					Member item = new Member();
					item = memberService.selectMember(member);
					memberList.add(item);
					item = null;
				}
				
			} else {
				memberList = memberService.selectMemberListAllForPrint(member);
			}
			/** 4) View 처리하기 */
			// 조회 결과를 View에게 전달한다.
			model.addAttribute("memberList", memberList);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		
		return new ModelAndView("member/print_member/print_membership");
	}
	
}
