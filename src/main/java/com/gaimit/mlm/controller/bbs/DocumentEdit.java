package com.gaimit.mlm.controller.bbs;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gaimit.helper.RegexHelper;
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.BbsDocument;
import com.gaimit.mlm.model.BbsFile;
import com.gaimit.mlm.service.BbsDocumentService;
import com.gaimit.mlm.service.BbsFileService;

@Controller
public class DocumentEdit {
	/** (1) 사용하고자 하는 Helper 객체 선언 */
	// --> import org.apache.logging.log4j.Logger;
	//private static Logger logger = LoggerFactory.getLogger(Download.class);
	@Autowired
	SqlSession sqlSession;
	@Autowired
	WebHelper web;
	@Autowired
	BBSCommon bbs;
	@Autowired
	UploadHelper upload;
	@Autowired
	RegexHelper regex;
	@Autowired
	BbsDocumentService bbsDocumentService;
	@Autowired
	BbsFileService bbsFileService;

	@RequestMapping(value = "/bbs/document_edit.do")
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

		/** (5) 글 번호 파라미터 받기 */
		int documentId = web.getInt("document_id");
		if (documentId == 0) {
			return web.redirect(null, "글 번호가 지정되지 않았습니다.");
		}

		// 파라미터를 Beans로 묶기
		BbsDocument document = new BbsDocument();
		document.setId(documentId);
		document.setCategory(category);

		BbsFile file = new BbsFile();
		file.setBbsDocumentId(documentId);

		/** (6) 게시물 일련번호를 사용한 데이터 조회 */
		// 지금 읽고 있는 게시물이 저장될 객체
		BbsDocument readDocument = null;
		// 첨부파일 정보가 저장될 객체
		List<BbsFile> fileList = null;

		try {
			readDocument = bbsDocumentService.selectDocument(document);
			fileList = bbsFileService.selectFileList(file);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}

		/** (7) 읽은 데이터를 View에게 전달한다. */
		model.addAttribute("readDocument", readDocument);
		model.addAttribute("fileList", fileList);

		return new ModelAndView("bbs/document_edit");
	}

}
