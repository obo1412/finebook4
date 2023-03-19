package com.gaimit.mlm.controller.member;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
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

import com.gaimit.helper.FileInfo;
import com.gaimit.helper.RegexHelper;
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.WebHelper;
import com.gaimit.helper.Util;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.service.MemberService;

@Controller
public class JoinOk {
	/** (1) 사용하고자 하는 Helper + Service 객체 선언 */
	// --> import org.apache.logging.log4j.Logger;
	Logger logger = LoggerFactory.getLogger(JoinOk.class);
	// --> import org.apache.ibatis.session.SqlSession;
	@Autowired
	SqlSession sqlSession;
	// --> import study.jsp.helper.WebHelper;
	@Autowired
	WebHelper web;
	// --> import study.jsp.helper.RegexHelper;
	@Autowired
	RegexHelper regex;
	// --> import study.jsp.helper.UploadHelper;
	@Autowired
	UploadHelper upload;
	// --> import study.jsp.mysite.service.MemberService;
	@Autowired
	Util util;
	
	@Autowired
	MemberService memberService;

	@RequestMapping(value = "/member/join_ok.do")
	public ModelAndView doRun(Locale locale, Model model, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		/** (2) 사용하고자 하는 Helper+Service 객체 생성 */
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
		
		String memberImgDir = "/memberImg/libNo"+idLib;
		
		/** (4) 파일이 포함된 POST 파라미터 받기 */
		// <form>태그 안에 <input type="file">요소가 포함되어 있고,
		// <form>태그에 enctype="multipart/form-data"가 정의되어 있는 경우
		// WebHelper의 getString()|getInt() 메서드는 더 이상 사용할 수 없게 된다.
		try {
			upload.multipartRequest(memberImgDir);
		} catch (Exception e) {
			return web.redirect(null, "multipart 데이터가 아닙니다.");
		}

		// UploadHelper에서 텍스트 형식의 파라미터를 분류한 Map을 리턴받아서 값을 추출한다.
		Map<String, String> paramMap = upload.getParamMap();
		//join 페이지에서 전달받은 값
		String name = paramMap.get("name");
		String phone = paramMap.get("phone");
		String otherContact = paramMap.get("otherContact");
		String birthdate = paramMap.get("birthdate");
		String email = paramMap.get("email");
		String gradeStr = paramMap.get("grade");
		String postcode = paramMap.get("postcode");
		String addr1 = paramMap.get("addr1");
		String addr2 = paramMap.get("addr2");
		String rfuid = paramMap.get("rfuid");
		String remarks = paramMap.get("remarks");
		String lastId = paramMap.get("lastId");
		String idLibString = paramMap.get("idLib");
		
		String checkNum = paramMap.get("checkNum");
		//회원 분류
		String idMbrClassStr = paramMap.get("idMbrClass");
		Integer idMbrClass = null;
		if(idMbrClassStr!=null&&!"".equals(idMbrClassStr)) {
			idMbrClass = Integer.parseInt(idMbrClassStr);
		} 

		// 전달받은 파라미터는 값의 정상여부 확인을 위해서 로그로 확인
		logger.debug("name=" + name);
		logger.debug("phone=" + phone);
		logger.debug("otherContact=" + otherContact);
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
		
		logger.debug("checkNum=" + checkNum);
		
		if(birthdate==null||"".equals(birthdate)||"0000-00-00".equals(birthdate)) {
			birthdate = null;
		}
		
		if(gradeStr==null||gradeStr.equals("")) {
			return web.redirect(null, "회원 등급을 선택해주세요.");
		}
		
		int grade = 0;
		if(gradeStr!=null&&!gradeStr.equals("")) {
			//paramMap으로 받은 String grade를 int형으로 변환
			grade = Integer.parseInt(gradeStr);
		}
		
		
		// 중복검사를 실행한 번호와 가입완료로 넘기는 번호가 다를경우 검사.
		/*if (!phone.equals(checkNum)) {
			return web.redirect(null, "연락처 중복 검사를 실행하세요.");
		}*/
		
		//시간으로 난수 생성
		//int intLastId = Integer.parseInt(lastId);
		//int useId = intLastId + 1;
		// 가장 마지막 생성된 id를 조합한 id_code
		//현재시간 생성 Calendar 클래스 활용
		
		/*int yy = cal.get(Calendar.YEAR);
		int mm = cal.get(Calendar.MONTH);
		int dd = cal.get(Calendar.DAY_OF_MONTH);
		int hh = cal.get(Calendar.HOUR_OF_DAY);
		int mi = cal.get(Calendar.MINUTE);
		int ss = cal.get(Calendar.SECOND);*/
		
		/*Calendar cal = Calendar.getInstance();
		Date curr = cal.getTime();
		String timeCurr = (new SimpleDateFormat("yyyyMMddHHmmss").format(curr));
		String idCode = timeCurr + useId;
		
		logger.debug("idCode= "+ idCode);*/

		/** (5) 입력값의 유효성 검사 */
		// 아이디 검사
		/*if (!regex.isValue(userId)) {
			return web.redirect(null, "아이디를 입력하세요.");
		}

		if (!regex.isEngNum(userId)) {
			return web.redirect(null, "아이디는 숫자와 영문의 조합으로 20자까지만 가능합니다.");
		}

		if (userId.length() > 20) {
			return web.redirect(null, "아이디는 숫자와 영문의 조합으로 20자까지만 가능합니다.");
		}*/

		// 비밀번호 검사
		/*if (!regex.isValue(userPw)) {
			return web.redirect(null, "비밀번호를 입력하세요.");
		}

		if (!regex.isEngNum(userPw)) {
			return web.redirect(null, "비밀번호는 숫자와 영문의 조합으로 20자까지만 가능합니다.");
		}

		if (userPw.length() > 20) {
			return web.redirect(null, "비밀번호는 숫자와 영문의 조합으로 20자까지만 가능합니다.");
		}

		// 비밀번호 확인
		if (!userPw.equals(userPwRe)) {
			return web.redirect(null, "비밀번호 확인이 잘못되었습니다.");
		}*/

		// 이름 검사
		if (!regex.isValue(name)) {
			return web.redirect(null, "이름을 입력하세요.");
		}

//		if (!regex.isKor(name)) {
//			return web.redirect(null, "이름은 한글만 입력 가능합니다.");
//		}

//		if (name.length() < 2 || name.length() > 5) {
//			return web.redirect(null, "이름은 2~5글자 까지만 가능합니다.");
//		}

		// 이메일 검사
		/*if (!regex.isValue(email)) {
			return web.redirect(null, "이메일을 입력하세요.");
		}

		if (!regex.isEmail(email)) {
			return web.redirect(null, "이메일의 형식이 잘못되었습니다.");
		}*/

		// 연락처 검사
		/*if (!regex.isValue(phone)) {
			return web.redirect(null, "연락처를 입력하세요.");
		}*/

		/*if (!regex.isCellPhone(phone) && !regex.isTel(phone)) {
			return web.redirect(null, "연락처의 형식이 잘못되었습니다.");
		}*/

		// 생년월일 검사
		/*if (!regex.isValue(birthdate)) {
			return web.redirect(null, "생년월일을 입력하세요.");
		}*/

		// 성별검사
		/*if (!regex.isValue(gender)) {
			return web.redirect(null, "성별을 입력하세요.");
		}

		if (!gender.equals("M") && !gender.equals("F")) {
			return web.redirect(null, "성별이 잘못되었습니다.");
		}*/
		
		/**
		 * 바코드 값 난수 생성
		 */
		String barcodeMbr = util.getRandomPassword(7);
		logger.debug("barcodeMbr=" + barcodeMbr);
		//util의 난수 생성 코드 사용
		barcodeMbr = barcodeMbr.toUpperCase();

		/** (6) 업로드 된 파일 정보 추출 */
		List<FileInfo> fileList = upload.getFileList();
		// 업로드 된 프로필 사진 경로가 저장될 변수
		String profileImg = null;

		// 업로드 된 파일이 존재할 경우만 변수값을 할당한다.
		if (fileList.size() > 0) {
			// 단일 업로드이므로 0번째 항목만 가져온다.
			FileInfo info = fileList.get(0);
			profileImg = info.getFileDir() + "/" + info.getFileName();
		}

		// 파일경로를 로그로 기록
		logger.debug("profileImg=" + profileImg);

		/** (7) 전달받은 파라미터를 Beans 객체에 담는다. */
		Member member = new Member();
		member.setIdLib(idLib);
		member.setName(name);
		member.setPhone(phone);
		member.setOtherContact(otherContact);
		member.setBirthdate(birthdate);
		member.setEmail(email);
		member.setPostcode(postcode);
		member.setAddr1(addr1);
		member.setAddr2(addr2);
		member.setRemarks(remarks);
		member.setProfileImg(profileImg);
		member.setRfUid(rfuid);
		member.setGradeId(grade);
		member.setBarcodeMbr(barcodeMbr);
		member.setClassId(idMbrClass);


		/** (8) Service를 통한 데이터베이스 저장 처리 */
		try {
			//전화번호 중복검사인데, 쌍둥이는 부모님 전화번호를 사용할경우 문제잇음.
			//memberService.selectPhoneCount(member);
			memberService.insertMember(member);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}

		/** (9) 가입이 완료되었으므로 메인페이지로 이동 */
		return web.redirect(web.getRootPath() + "/member/member_list.do", "회원추가가 완료되었습니다.");
	}
}
