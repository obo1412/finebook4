package com.gaimit.mlm.controller.manager;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gaimit.helper.JwtHelper;
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.ManagerService;

@Controller
public class LoginOk {
	/** (1) 사용하고자 하는 Helper + Service 객체 선언 */
	// --> import org.apache.logging.log4j.Logger;
	private static Logger logger = LoggerFactory.getLogger(LoginOk.class);
	@Autowired
	SqlSession sqlSession;
	@Autowired
	WebHelper web;
	@Autowired
	UploadHelper upload;
	@Autowired
	ManagerService managerService;
	@Autowired
	JwtHelper jwtHelper;

	@RequestMapping(value = "/managerFinebook/login_ok.do")
	public ModelAndView doRun(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {

		/** (2) 사용하고자 하는 Helper+Service 객체 생성 */
		web.init();

		/** (3) 로그인 여부 검사 */
		// 로그인 중이라면 이 페이지를 동작시켜서는 안된다.
		if (web.getSession("loginInfo") != null) {
			return web.redirect(web.getRootPath() + "/index.do", "이미 로그인 하셨습니다.");
		}
		
		/** (4) 파라미터 처리 */
		// --> topbar.jsp에 배치된 폼으로부터 전송되는 입력값.
		String userId = web.getString("user_id");
		String userPw = web.getString("user_pw");

		// console창에 계속 뜸
		logger.debug("userId=" + userId);
		logger.debug("userPw=" + userPw);

		if (userId == null || userPw == null) {
			return web.redirect(null, "아이디나 비밀번호가 없습니다.");
		}

		
		
		/** (5) 전달받은 파라미터를 Beans에 설정한다. */
		Manager manager = new Manager();
		manager.setUserIdMng(userId);
		manager.setUserPwMng(userPw);

		/** (6) Service를 통한 회원 인증 */
		Manager loginInfo = null;
		//도서관 명을 확인하기 위한 변수 선언
		
		//jwt 토큰
		String jwt = null;
		//자동로그인 체크여부
		String autoLogin = web.getString("autoLogin");
		
		try {
			// 아이디와 비밀번호가 일치하는 회원 정보를 조회하여 리턴한다.
			loginInfo = managerService.selectLoginInfo(manager);
			if("true".equals(autoLogin)) {
				//자동로그인이 체크 되어 있다면, jwt 발급 처리.
				//기존에 존재하는 jwt가 있어도 앞에건 없애고 덮어쓰는듯.
				jwt = jwtHelper.createToken(loginInfo.getIdMng());
				web.setCookie("jwt", jwt, 60*60*24*7);
			}
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}

		/** (7) 프로필 이미지 처리 */
		// 프로필 이미지가 있을 경우 썸네일을 생성하여 쿠키에 별도로 저장
		/*String profileImg = loginInfo.getProfileImg();
		if (profileImg != null) {
			try {
				String profileThumbnail = upload.createThumbnail(profileImg, 40, 40, true);
				web.setCookie("profileThumbnail", profileThumbnail, -1);
			} catch (Exception e) {
				return web.redirect(null, e.getLocalizedMessage());
			}
		}*/

		/** (8) 조회된 회원 정보를 세션에 저장 */
		// 로그인 처리는 아이디와 비밀번호를 기반으로 조회된 정보를
		// 세션에 보관하는 과정을 말한다.
		// 로그인에 대한 판별은 저장된 세션정보의 존재 여부로 판별한다.
		web.setSession("loginInfo", loginInfo);

		/** (9) 페이지 이동 */
		// 이전 페이지 구하기 (javascript로 이동된 경우 조회 안됨)
		/*String movePage = request.getHeader("referer");
		if (movePage == null) {
			movePage = web.getRootPath() + "/index.do";
		}*/

		return web.redirect(web.getRootPath() + "/index_login.do", null);
	}

}
