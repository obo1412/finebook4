package com.gaimit.mlm.controller.book;

import java.io.IOException;
import java.util.Locale;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gaimit.helper.RegexHelper;
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.controller.FrequentlyFunction;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.BookHeldService;

@Controller
public class RegBookOk {
	/** (1) 사용하고자 하는 Helper + Service 객체 선언 */
	// --> import org.apache.logging.log4j.Logger;
	Logger logger = LoggerFactory.getLogger(RegBookOk.class);
	// --> import org.apache.ibatis.session.SqlSession;
	@Autowired
	SqlSession sqlSession;
	// --> import study.jsp.helper.WebHelper;
	@Autowired
	WebHelper web;
	// --> import study.jsp.helper.RegexHelper;
	@Autowired
	RegexHelper regex;
	
	@Autowired
	Util util;
	// --> import study.jsp.helper.UploadHelper;
	@Autowired
	UploadHelper upload;
	
	@Autowired
	BookHeldService bookHeldService;
	
	@Autowired
	FrequentlyFunction frequentlyFunction;

	@RequestMapping(value = "/book/reg_book_ok.do", method = RequestMethod.POST)
	public ModelAndView doRun(Locale locale, Model model, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		/** (2) 사용하고자 하는 Helper+Service 객체 생성 */
		web.init();

		/** (3) 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		int idLib = 0;
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		} else {
			idLib = loginInfo.getIdLibMng();
		}

		logger.info("reg_book_ok 처리 시작합니다.");
		/** reg_book에서 전달받은 book, bookheld 파라미터를 Beans 객체에 담는다. */
		String isbn13 = web.getString("isbn13");
		String isbn10 = web.getString("isbn10");
		String bookTitle = web.getString("bookTitle");
		String author = web.getString("author");
		String authorCode = web.getString("authorCode");
		String publisher = web.getString("publisher");
		String pubDate = web.getString("pubDate");
		if(pubDate.length() == 4) {
			pubDate = pubDate+"-01-01";
		}
		String bookCateg = web.getString("bookCateg");
		String bookShelf = web.getString("bookShelf");
		int page = web.getInt("itemPage");
		String price = web.getString("price");
		String bookOrNot = web.getString("bookOrNot");
		int purOrDon = web.getInt("purOrDon");
		String rfId = web.getString("rfId");
		String bookSize = web.getString("bookSize");
		String classificationCode = web.getString("classificationCode");
		if(util.hasTwoOrMoreDots(classificationCode)) {
			return web.redirect(null, "분류기호에 소수점이 2개 이상입니다.");
		}
		String additionalCode = web.getString("additionalCode");
		
		String volumeCode = web.getString("volumeCode");
		
		String newBarcode = web.getString("newBarcode");
		
		String tagBook = web.getString("tagBook");
		
		String bookCover = web.getString("bookCover");
		String bookDesc = web.getString("bookDesc");
		
		int idCountry = web.getInt("idCountry", 0);
		
		//바로 등록 체크박스
		String chkBoxRegOk = web.getString("chkBoxRegOk");
		logger.info("바로 등록 체크 상태 "+chkBoxRegOk);
		
		//권차기호 알림을 위한 체크박스
		String chkBoxVolumeCodeAlarm = web.getString("chkBoxVolumeCodeAlarm");
		logger.info("현재 권차기호 체크 상태 "+chkBoxVolumeCodeAlarm);
		
		//알림소리를 위한 체크박스, 체크시 알리지 않음.
		String chkBoxSoundEff = web.getString("chk_box_sound_eff");
		logger.info("알림소리 체크박스 상태 "+chkBoxSoundEff);
		
		//십진분류 체킹을 위한 체크박스
		String chkBoxClassCodeChecker = web.getString("chk_box_class_code_checker");
		logger.info("십진분류 체크박스 상태 "+chkBoxClassCodeChecker);
		
		
		// 전달받은 파라미터는 값의 정상여부 확인을 위해서 로그로 확인
		/*logger.info("isbn13=" + isbn13);
		logger.info("isbn10=" + isbn10);
		logger.info("bookTitle=" + bookTitle);
		logger.info("author=" + author);
		logger.info("authorCode=" + authorCode);
		logger.info("publisher=" + publisher);
		logger.info("pubDate=" + pubDate);
		logger.info("bookCateg=" + bookCateg);
		logger.info("bookShelf=" + bookShelf);
		logger.info("page=" + page);
		logger.info("price=" + price);
		logger.info("bookOrNot=" + bookOrNot);
		logger.info("purOrDon=" + purOrDon);
		logger.info("rfId=" + rfId);
		logger.info("bookSize=" + bookSize);
		logger.info("classificationCode=" + classificationCode);
		logger.info("additionalCode=" + additionalCode);
		logger.info("volumeCode=" + volumeCode);
		
		logger.info("tagBook=" + tagBook);
		logger.info("bookCover=" + bookCover);
		logger.info("bookDesc=" + bookDesc);
		logger.info("newBarcode=" + newBarcode);
		
		logger.info("idCountry=" + idCountry);*/
		
		//분류기호 점검 체크 되어있을 때, 누락되면 안되는 분류기호 정규표현식으로 검사
		if("checked".equals(chkBoxClassCodeChecker)) {
			if(!regex.isValue(classificationCode)) {
				return web.redirect(null, "십진분류 기호가 누락되었습니다.");
			}
		}
		
		//rfId @이후 제거 작업
//		System.out.println(rfId+" rfid 값은 왼쪽");
		/*System.out.println(rfId.substring(0,rfId.indexOf('@')));*/
		if(rfId!=null&&!"".equals(rfId)) {
			if(rfId.indexOf('@')>-1) {
				rfId = rfId.substring(0,rfId.indexOf('@'));
			}
		}
		
		//book, bookHeld insert 위한 정보 수집
		BookHeld bookHeld = new BookHeld();
		
		//bookIdBook 없애기 위한 1차 변수
		bookHeld.setBookIdBook(null);
		
		//manager로부터 도서관번호 부여.
		bookHeld.setLibraryIdLib(idLib);
		bookHeld.setIsbn13(isbn13);
		bookHeld.setIsbn10(isbn10);
		bookHeld.setTitle(bookTitle);
		bookHeld.setWriter(author);
		bookHeld.setAuthorCode(authorCode);
		bookHeld.setPublisher(publisher);
		bookHeld.setPubDate(pubDate);
		bookHeld.setCategory(bookCateg);
		bookHeld.setBookShelf(bookShelf);
		bookHeld.setPage(page);
		bookHeld.setPrice(price);
		bookHeld.setBookOrNot(bookOrNot);
		bookHeld.setPurchasedOrDonated(purOrDon);
		bookHeld.setRfId(rfId);
		bookHeld.setBookSize(bookSize);
		bookHeld.setClassificationCode(classificationCode);
		bookHeld.setAdditionalCode(additionalCode);
		//권차기호에 영문 섞인거 예외처리
		volumeCode = util.getNumVolumeCode(volumeCode);
		bookHeld.setVolumeCode(volumeCode);
		
		bookHeld.setTag(tagBook);
		
		bookHeld.setImageLink(bookCover);
		bookHeld.setDescription(bookDesc);
		bookHeld.setAvailable(1);
		
		bookHeld.setIdCountry(idCountry);
		
		//바코드 번호 생성을 위한 변수 선언
		int newBarcodeNum = 0; //바코드 번호 빈자리
		
		//바코드 헤드를 바꾸는 경우,
		String beforeBarcodeHead = null;
		String afterBarcodeHead = null;
		
		//barcode 호출
		try {
			//바코드 직접 기입시, 직접 기입한 바코드로 등록하기 위해서.
			newBarcodeNum = util.numExtract(newBarcode);
			//위 새로운 바코드 번호를 솔팅index에 주입
			bookHeld.setSortingIndex(newBarcodeNum);
			//소문자 바코드를 대문자로 변환
			newBarcode = newBarcode.toUpperCase();
			bookHeld.setLocalIdBarcode(newBarcode);
			
			afterBarcodeHead = util.strExtract(newBarcode);
			//바코드 번호 지정 직접 등록시에 사용
			/*bookHeld.setSortingIndex(viewBarNum);*/
			
			/*if(newBarcode.length() != 8) {
				return web.redirect(null, "바코드를 8자리로 맞추어 주세요.");
			} else if(viewBarcodeInit.length() > 3 ) {
				return web.redirect(null, "바코드 머리 글자수는 3자리 이하여야 합니다.");
			}*/
			
			/*뷰페이지에서 넘어온 바코드 숫자와 ok컨트롤러에서 조사한 바코드 뒤숫자가
			 * 같지 않으면, 콜백 발생. */
			/*if(viewBarNum != lastEmptyLocalBarcode) {
				return web.redirect(null, "최신 바코드 번호가 일치하지 않습니다.");
			}*/
			//* 바코드 호출 끝
			//바코드 뒤 숫자 중복검사를 위하여 값 주입
			bookHeld.setNewBarcodeForDupCheck(newBarcodeNum);
			//위 viewBarNum를 중복검사 변수로 사용.
			//중복되는 번호가 있다면 impl 단계에서 예외처리
			
			//아래 기본 중복검사, 중복있으면 그냥 메시지 띄워주기
			/**
			 * 옵션 1 **************************************
			 */
			//bookHeldService.selectDupCheckLocalBarcode(bookHeld);
			
			/**
			 * 옵션 2 **************************************
			 */
			//아래 중복검사는 중복 아닐때까지 등록번호(sortingIndex) +1 돌리기
			int dupChecker = 1;
			while(dupChecker > 0) {
				dupChecker = bookHeldService.selectDupCheckLocalBarcodeReturnNum(bookHeld);
				if(dupChecker == 0) {
					//중복이 없을 경우,
					newBarcode = frequentlyFunction.getLastBarcode(1, bookHeld);
					if(afterBarcodeHead != beforeBarcodeHead) {
						//바코드 헤드가 같지 않으면,
						newBarcode = frequentlyFunction.makeBarcodeByNumOfDigits(idLib, afterBarcodeHead, util.numExtract(newBarcode));
					}
					bookHeld.setSortingIndex(util.numExtract(newBarcode));
					bookHeld.setLocalIdBarcode(newBarcode);
					break;
				}
				//System.out.println("sortingIndex 대입전"+newBarcodeNum);
				bookHeld.setNewBarcodeForDupCheck(++newBarcodeNum);
				//System.out.println("sortingIndex 대입후"+bookHeld.getNewBarcodeForDupCheck());
			}
			
			int copyCode = 0;
			//복본 처리 함수 통합관리.
			
			//전집류 복본기호 체크를 위한 체크박스
			/*String chkBoxCollectionBooksCopyCode = web.getString("chk_box_collection_books_copy_code");
			logger.info("전집류 복본기호 체크박스 상태 "+chkBoxCollectionBooksCopyCode);
			
			if("checked".equals(chkBoxCollectionBooksCopyCode)) {
				copyCode = frequentlyFunction.getCopyCodeByFirstCheckISBN(bookHeld);
			} else {
				copyCode = frequentlyFunction.getCopyCode(bookHeld);
			}*/
			
			copyCode = frequentlyFunction.getCopyCode(bookHeld);
			bookHeld.setCopyCode(copyCode);
			
			//등록처리
			bookHeldService.insertBookHeld(bookHeld);
			
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}

		/** (9) 가입이 완료되었으므로 메인페이지로 이동 */
		//"도서 등록이 완료되었습니다."
		//메시지 띄우려면 두번째 매개변수에 string
		//음향효과 띄우려면 화면에서 js로 컨트롤
		String viewPage = "/book/reg_book.do";
		
		//음향효과 1 성공 2 에러 3 경고
		viewPage += "?soundEff=1";
		
		if(chkBoxRegOk!=null) {
			if(viewPage.indexOf("?")>-1) {
				viewPage += "&regChk=checked";
			} else {
				viewPage += "?regChk=checked";
			}
		}
		
		if("checked".equals(chkBoxVolumeCodeAlarm)) {
			if(viewPage.indexOf("?")>-1) {
				viewPage += "&volumeCodeAlarm=checked";
			} else {
				viewPage += "?volumeCodeAlarm=checked";
			}
		}
		
		//noSoundChk
		if("checked".equals(chkBoxSoundEff)) {
			if(viewPage.indexOf("?")>-1) {
				viewPage += "&SoundChk=checked";
			} else {
				viewPage += "?SoundChk=checked";
			}
		}
		
		//십진분류 체크, 체크가 안되어 있을 경우
		if(chkBoxClassCodeChecker==null) {
			if(viewPage.indexOf("?")>-1) {
				viewPage += "&clsCodeChker=unchecked";
			} else {
				viewPage += "?clsCodeChker=unchecked";
			}
		}
		
		if(additionalCode!=null) {
			if(viewPage.indexOf("?")>-1) {
				viewPage += "&addiCode="+additionalCode;
			} else {
				viewPage += "?addiCode="+additionalCode;
			}
		}
		
		//전집류 복본기호 따로 체크 너무 복잡해서 안될 것 같음.
		/*if("checked".equals(chkBoxCollectionBooksCopyCode)) {
			if(viewPage.indexOf("?")>-1) {
				viewPage += "&colleBooksCopy=checked";
			} else {
				viewPage += "?colleBooksCopy=checked";
			}
		}*/
		
		
		return web.redirect(web.getRootPath() + viewPage , null);
		
	}
}
