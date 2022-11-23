package com.gaimit.mlm.controller.bbs;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.BbsDocument;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.BbsDocumentService;

@Controller
public class DocumentDelete {	
	/** (1) 사용하고자 하는 Helper 객체 선언 */
	// --> import org.apache.logging.log4j.Logger;
	//private static Logger logger = LoggerFactory.getLogger(DocumentDelete.class);
	@Autowired
	SqlSession sqlSession;
	@Autowired
	WebHelper web;
	@Autowired
	BBSCommon bbs;
	@Autowired
	BbsDocumentService bbsDocumentService;

	@RequestMapping(value = "/bbs/document_delete.do")
	public ModelAndView doRun(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {

		web.init();
		
		/** (3) 게시판 카테고리 값을 받아서 View에 전달 */
		String category = web.getString("category");
		model.addAttribute("category", category);
		
		/** (4) 존재하는 게시판인지 판별하기 */
		try {
			String bbsName = bbs.getBbsName(category);
			model.addAttribute("bbsName", bbsName);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** (5) 게시글 번호 받기 */
		int documentId = web.getInt("document_id");
		if (documentId == 0) {
			return web.redirect(null, "글 번호가 없습니다.");
		}
		
		// 파라미터를 Beans로 묶기
		BbsDocument document = new BbsDocument();
		document.setId(documentId);
		document.setCategory(category);
		
		// 로그인 한 경우 현재 회원의 일련번호를 추가한다. (비로그인 시 0으로 설정됨)
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		if (loginInfo != null) {
			document.setManagerId(loginInfo.getIdMng());
		}
		
		/** (6) 게시물 일련번호를 사용한 데이터 조회 */	
		// 회원번호가 일치하는 게시물 수 조회하기
		int documentCount = 0; 
		try {
			documentCount = bbsDocumentService.selectDocumentCountByManagerId(document);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** (7) 자신의 글에 대한 요청인지에 대한 여부를 view에 전달 */
		boolean myDocument = documentCount > 0;
		model.addAttribute("myDocument", myDocument);
		
		// 상태유지를 위하여 게시글 일련번호를 View에 전달한다.
		model.addAttribute("documentId", documentId);
		
		return new ModelAndView("bbs/document_delete");
	}
}
