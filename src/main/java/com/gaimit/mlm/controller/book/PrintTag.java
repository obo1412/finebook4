package com.gaimit.mlm.controller.book;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaimit.helper.FileInfo;
import com.gaimit.helper.PageHelper;
import com.gaimit.helper.QRCodeHelper;
import com.gaimit.helper.RegexHelper;
import com.gaimit.helper.UploadHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;

import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.TagSetting;
import com.gaimit.mlm.controller.FrequentlyFunction;
import com.gaimit.mlm.model.BbsFile;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.TagSettingService;

@Controller
public class PrintTag {
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
	BookHeldService bookHeldService;
	
	@Autowired
	TagSettingService tagSettingService;
	
	@Autowired
	QRCodeHelper qrCode;
	
	@Autowired
	FrequentlyFunction frequentlyFunction;
	
	/** 교수 목록 페이지 */
	@RequestMapping(value = "/book/print_tag_setup.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView printTagSetup(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		TagSetting tag = new TagSetting();
		tag.setIdLib(loginInfo.getIdLibMng());
		
		int chkTagInfo = 0;
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		
		List<BookHeld> bookHeldList = null;
		try {
			chkTagInfo = tagSettingService.selectTagInfoCountByLib(tag);
			if(chkTagInfo == 0) {
				tagSettingService.insertDefaultTagSetting(tag);
			}
			
			tag = tagSettingService.selectRollTagPositionValue(tag);
			bookHeldList = bookHeldService.getPrintBookHeldList(bookHeld);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		/** 4) View 처리하기 */
		// 조회 결과를 View에게 전달한다.
		model.addAttribute("tag", tag);
		model.addAttribute("bookHeldList", bookHeldList);
		
		return new ModelAndView("book/print_tag_setup");
	}
	
	/** 라벨출력 페이지 */
	@RequestMapping(value = "/book/print_tag_page.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView printTag(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		TagSetting tag = new TagSetting();
		tag.setIdLib(loginInfo.getIdLibMng());
		
		//태그타입
		int tagType = web.getInt("tagType", 0);
		//태그 a4용지에서 시작위치.
		int startPoint = web.getInt("startPoint", 1);
		
		String dateSorting = web.getString("dateSorting");
		String targetSorting = web.getString("targetSorting");
		String titleSorting = web.getString("titleSorting");
		
		//바코드 범위 직접 입력 출력방식 1이면)
		int sortingType = web.getInt("sortingType", 0);
		String rangeInputBarcode = web.getString("rangeInputBarcode");
		int ribStartNum = web.getInt("ribStartNum", 0);
		int ribEndNum = web.getInt("ribEndNum", 0);
		//바코드범위 임시 사용 저자기호
		String inputAuthorCode = web.getString("inputAuthorCode");
		
		//tagType 16번에서만 적용하는 객체들
		String[] colorArr = new String[0];
		String[] addiKeyArr = new String[0];
		if(tagType==16) {
			String colorArrStr = web.getString("colorArr");
			colorArr = colorArrStr.split(",");
			String keyArrStr = web.getString("keyArr");
			addiKeyArr = keyArrStr.split(",");
//			for(int i=0; i<addiKeyArr.length; i++) {
//				System.out.println(addiKeyArr[i]);
//			}
		}
		//tagType 16번에서 임시로 사용되는 객체. 그대로 전달해서 margintop 값
		int tempTitleMt = web.getInt("tempTitleMt", 0);
		
		if(targetSorting!=null) {
			targetSorting = targetSorting.toUpperCase();
		}
		
		int rangeStart = 0;
			rangeStart = web.getInt("rangeStart", 0);
		int rangeEnd = 0;
			rangeEnd = web.getInt("rangeEnd", 0);

		//rangeStart는 0부터 시작하지만, 실제 숫자는 1
			rangeStart = rangeStart -1;
		//range는 최종에서 +1 해줘야된다. 안그러면 숫자가 생겨버려서 실행조건이됨.
		//rangeStart에서 -1 해줬기 때문에 안해도 됨.
		int rangeLength = rangeEnd - rangeStart;
		
		if(dateSorting!= null || targetSorting!=null || titleSorting!=null || rangeLength!=0 
			|| sortingType==1) {
			/** 3) Service를 통한 SQL 수행 */
			// 조회 결과를 저장하기 위한 객체
			BookHeld bookHeld = new BookHeld();
			bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
			
			if(dateSorting!=null) {
				bookHeld.setRegDate(dateSorting);
			}
			
			if(titleSorting!=null) {
				bookHeld.setTitleBook(titleSorting);
			}
			
			if(targetSorting!=null) {
				targetSorting = targetSorting.toUpperCase();
				bookHeld.setLocalIdBarcode(targetSorting);
			}
			
			if(rangeEnd!=0 && rangeLength != 0) {
				bookHeld.setLimitStart(rangeStart);
				bookHeld.setListCount(rangeLength);
			}
			
			//바코드 직접 입력 방식
			if(sortingType==1) {
				bookHeld.setLocalIdBarcode(rangeInputBarcode);
				int ribRange = ribEndNum - ribStartNum + 1;
				bookHeld.setSortingIndex(ribEndNum);
				bookHeld.setListCount(ribRange);
				if(inputAuthorCode!=null) {
					bookHeld.setAuthorCode(inputAuthorCode);
				}
			}
			
			List<BookHeld> bookHeldList = null;
			try {
				tag = tagSettingService.selectRollTagPositionValue(tag);
				if(sortingType == 1) {
					bookHeldList = bookHeldService.selectPrintListByBarcodeRange(bookHeld);
				} else {
					bookHeldList = bookHeldService.getPrintBookHeldList(bookHeld);
				}
			} catch (Exception e) {
				return web.redirect(null, e.getLocalizedMessage());
			}
			
			for(int i=0; i<bookHeldList.size(); i++) {
				//3자리 분류기호 100자리 숫자만 남기고 편집.
				String classCode = bookHeldList.get(i).getClassificationCode();
				//System.out.println(classCode);
				//System.out.println(util.isNumCheck(classCode));
				if(classCode == null || "".equals(classCode) || !util.isNumCheck(classCode)) {
					classCode = "-1";
					int classCodeInt = (int)Float.parseFloat(classCode);
					//음수인 경우는 인쇄페이지에서 무표시 처리
					bookHeldList.get(i).setClassCodeHead(classCodeInt);
					bookHeldList.get(i).setClassCodeColor(frequentlyFunction.getColorKDC(tag, classCodeInt));
					bookHeldList.get(i).setClassCodeSection(frequentlyFunction.getNameKDC(classCodeInt));
				} else {
					int classCodeInt = (int)Float.parseFloat(classCode);
					int classHead1 = classCodeInt/100;
					int classCodeHead = classHead1 * 100;
					bookHeldList.get(i).setClassCodeHead(classCodeHead);
					bookHeldList.get(i).setClassCodeColor(frequentlyFunction.getColorKDC(tag, classCodeHead));
					bookHeldList.get(i).setClassCodeSection(frequentlyFunction.getNameKDC(classCodeHead));
				}
				
				//태그타입 16번 롤 옵션6 특수경우 경기도청 뭐시기에서만 사용.
				if(tagType==16&&colorArr.length>0&&addiKeyArr.length>0) {
					//여기다가, 이제 별치기호 비교해가지고 색상 넣어주기
					String addiCode = bookHeldList.get(i).getAdditionalCode();
					String color = getColorForType16(addiKeyArr, colorArr, addiCode);
					bookHeldList.get(i).setClassCodeColor(color);
				}
				
				//qrCodeChecker 가 0보다 크면 qr 코드 필요.
				int qrCodeChecker = util.qrCodeChecker(tagType);
				if(qrCodeChecker > 0) {
					//System.out.println("qr코드 생성");
					//QR코드 생성
					qrCode.makeQR(bookHeldList.get(i).getLocalIdBarcode(), 60, 60
							,"/var/packages/Tomcat7/target/src/webapps/downloads/upload/finebook4/qrcode/libNo"+loginInfo.getIdLibMng()+"/"
							,bookHeldList.get(i).getLocalIdBarcode()+".png" );
				}
				
			}
			/** 4) View 처리하기 */
			// 조회 결과를 View에게 전달한다.
			model.addAttribute("bookHeldList", bookHeldList);
			model.addAttribute("tag", tag);
		} else {
			
			if (!regex.isValue(dateSorting)) {
				return web.redirect(null, "원하시는 날짜 또는 도서명, 등록번호를 입력하세요.");
			}
			
			if (!regex.isValue(targetSorting)) {
				return web.redirect(null, "원하시는 날짜 또는 도서명, 등록번호를 입력하세요.");
			}
			
			if (!regex.isValue(titleSorting)) {
				return web.redirect(null, "원하시는 날짜 또는 도서명, 등록번호를 입력하세요.");
			}
			
		}
		
		model.addAttribute("startPoint", startPoint);
		model.addAttribute("tempTitleMt", tempTitleMt);
		
		//태그 타입 받아온 것으로 페이지 url 변경
		String movePage = util.getMovePageByLabel(tagType);
		
		return new ModelAndView(movePage);
	}
	
	//keyArr와 colorArr를 받아서 addiCode(별치기호)와 비교해서 색상 도출하기
	public String getColorForType16(String[] keyArr, String[] colorArr, String addiCode) {
		String result = "#8b4b93";
		if(addiCode == null) return result;
		addiCode = addiCode.trim();
		addiCode = addiCode.replaceAll("\\s+", "");
		for(int i=0; i<keyArr.length; i++) {
			if(keyArr[i].equals(addiCode)) {
				result = colorArr[i];
				break;
			}
		}
		return result;
	}
	
	/** 라벨출력 페이지 */
	@RequestMapping(value = "/book/print_tag_page_by_book_id.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView printTagByBookId(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		TagSetting tag = new TagSetting();
		tag.setIdLib(loginInfo.getIdLibMng());
		
		int tagType = web.getInt("tagType", 0);
		//인쇄 시작 위치
		int startPoint = web.getInt("startPoint", 1);
		
		//url get방식으로 string 하나로 가져옴.
		String bookIdString = web.getString("chkBoxArr");
		
		if(bookIdString==null||"".equals(bookIdString)) {
			return web.redirect(null, "라벨 프린트를 위한 bookIdArr가 없습니다.");
		}
		
		String[] bookIdArr = bookIdString.split(",");
		
//		for(int i=0; i<bookIdArr.length; i++) {
//			System.out.println(bookIdArr[i]);
//		}
		
		/** 3) Service를 통한 SQL 수행 */
		// 조회 결과를 저장하기 위한 객체
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		
		//하나씩 조회한 도서 정보를 넣을 리스트
		List<BookHeld> bookHeldList = new ArrayList<BookHeld>();
		
		try {
			tag = tagSettingService.selectRollTagPositionValue(tag);
			//arr 길이가 0이상이면 시행.
			if(bookIdArr.length>0) {
				//arr에 담긴 book id 만큼 도서정보 도출
				for(int i=0; i<bookIdArr.length; i++) {
					bookHeld.setId(Integer.parseInt(bookIdArr[i]));
					BookHeld item = new BookHeld();
					item = bookHeldService.selectPrintBookByBookId(bookHeld);
					bookHeldList.add(item);
					item = null;
				}
				
				for(int i=0; i<bookHeldList.size(); i++) {
					//3자리 분류기호 100자리 숫자만 남기고 편집.
					String classCode = bookHeldList.get(i).getClassificationCode();
					if(classCode == null || "".equals(classCode) || !util.isNumCheck(classCode)) {
						classCode = "-1";
						int classCodeInt = (int)Float.parseFloat(classCode);
						//음수인 경우는 인쇄페이지에서 무표시 처리
						bookHeldList.get(i).setClassCodeHead(classCodeInt);
						bookHeldList.get(i).setClassCodeColor(frequentlyFunction.getColorKDC(tag, classCodeInt));
						bookHeldList.get(i).setClassCodeSection(frequentlyFunction.getNameKDC(classCodeInt));
					} else {
						int classCodeInt = (int)Float.parseFloat(classCode);
						int classHead1 = classCodeInt/100;
						int classCodeHead = classHead1 * 100;
						bookHeldList.get(i).setClassCodeHead(classCodeHead);
						bookHeldList.get(i).setClassCodeColor(frequentlyFunction.getColorKDC(tag, classCodeHead));
						bookHeldList.get(i).setClassCodeSection(frequentlyFunction.getNameKDC(classCodeHead));
					}
					
					//qrCodeChecker 가 0보다 크면 qr 코드 필요.
					int qrCodeChecker = util.qrCodeChecker(tagType);
					if(qrCodeChecker > 0) {
						//QR코드 생성
						qrCode.makeQR(bookHeldList.get(i).getLocalIdBarcode(), 60, 60
								,"/var/packages/Tomcat7/target/src/webapps/downloads/upload/finebook4/qrcode/libNo"+loginInfo.getIdLibMng()+"/"
								,bookHeldList.get(i).getLocalIdBarcode()+".png" );
					}
				}
			}
			/** 4) View 처리하기 */
			// 조회 결과를 View에게 전달한다.
			model.addAttribute("bookHeldList", bookHeldList);
			model.addAttribute("tag", tag);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		model.addAttribute("startPoint", startPoint);
		
		//태그 타입 받아온 것으로 페이지 url 변경
		String movePage = util.getMovePageByLabel(tagType);
		
		return new ModelAndView(movePage);
	}
	
	
	/* 라벨 상세 위치 수정 페이지 */
	@RequestMapping(value = "/book/print_position_setting.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView printTagPosition(Locale locale, Model model) {
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		TagSetting tag = new TagSetting();
		tag.setIdLib(loginInfo.getIdLibMng());
		
		try {
			tag = tagSettingService.selectRollTagPositionValue(tag);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		model.addAttribute("tag", tag);
		
		return new ModelAndView("book/print_position_setting");
	}
	
	/*라벨 위치 수정 완료 컨트롤러*/
	@RequestMapping(value = "/book/print_position_setting_ok.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView printTagPositionOk(Locale locale, Model model) {
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		int labelType = web.getInt("label-type");
		float marginLeft = web.getFloat("margin-left");
		float tagWidth = web.getFloat("tag-width");
		float tagHeight = web.getFloat("tag-height");
		float tagGap = web.getFloat("tag-gap");
		float titleTagGap = web.getFloat("title-tag-gap");
		
		String fontsizeA4Namelib = web.getString("fontsize-a4Namelib");
		String fontsizeA4Barcode = web.getString("fontsize-a4Barcode");
		String fontsizeA4BarcodeNum = web.getString("fontsize-a4BarcodeNum");
		String fontsizeA4CodeBarcode = web.getString("fontsize-a4CodeBarcode");
		String fontsizeA4Codes = web.getString("fontsize-a4Codes");
		String fontsizeA4ClassNum = web.getString("fontsize-a4ClassNum");
		
		int libNameInbarcode = web.getInt("libNameInbarcode",0);
		
		TagSetting tag = new TagSetting();
		tag.setLabelType(labelType);
		tag.setIdLib(loginInfo.getIdLibMng());
		tag.setMarginLeft(marginLeft);
		tag.setTagWidth(tagWidth);
		tag.setTagHeight(tagHeight);
		tag.setTagGap(tagGap);
		tag.setTitleTagGap(titleTagGap);
		
		tag.setA4FontSizeNamelib(fontsizeA4Namelib);
		tag.setA4FontSizeBarcode(fontsizeA4Barcode);
		tag.setA4FontSizeBarcodeNum(fontsizeA4BarcodeNum);
		tag.setA4FontSizeCodeBarcode(fontsizeA4CodeBarcode);
		tag.setA4FontSizeCodes(fontsizeA4Codes);
		tag.setA4FontSizeClassNum(fontsizeA4ClassNum);
		
		//바코드 라벨에 도서관명 포함/미포함
		tag.setLibNameInbarcode(libNameInbarcode);
		
		try {
			tagSettingService.updateTagPosition(tag);
			tag = tagSettingService.selectRollTagPositionValue(tag);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		model.addAttribute("tag", tag);
		
		return web.redirect(web.getRootPath() + "/book/print_position_setting.do", "태그 위치가 수정되었습니다.");
		/*return new ModelAndView("book/print_position_setting");*/
	}
	
	/*라벨 색상 페이지*/
	@RequestMapping(value = "/book/print_color_setting.do", method = RequestMethod.GET)
	public ModelAndView printTagColor(Locale locale, Model model) {
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		TagSetting tag = new TagSetting();
		tag.setIdLib(loginInfo.getIdLibMng());
		
		List<TagSetting> tagPack = null;
		
		try {
			tagPack = tagSettingService.selectTagColorsBy0(tag);
			tag = tagSettingService.selectRollTagPositionValue(tag);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		model.addAttribute("tagPack", tagPack);
		model.addAttribute("tag", tag);
		
		return new ModelAndView("book/print_color_setting");
	}
	
	/*라벨 색상 수정 완료 컨트롤러*/
	@RequestMapping(value = "/book/print_color_setting_ok.do", method = RequestMethod.POST)
	public ModelAndView printTagColorOk(Locale locale, Model model) {
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		String kdc0Code = web.getString("kdc0Code");
		String kdc1Code = web.getString("kdc1Code");
		String kdc2Code = web.getString("kdc2Code");
		String kdc3Code = web.getString("kdc3Code");
		String kdc4Code = web.getString("kdc4Code");
		String kdc5Code = web.getString("kdc5Code");
		String kdc6Code = web.getString("kdc6Code");
		String kdc7Code = web.getString("kdc7Code");
		String kdc8Code = web.getString("kdc8Code");
		String kdc9Code = web.getString("kdc9Code");
		String kdcBlankCode = web.getString("kdcBlankCode");
		
		TagSetting tag = new TagSetting();
		tag.setIdLib(loginInfo.getIdLibMng());
		tag.setColor0Kdc(kdc0Code);
		tag.setColor1Kdc(kdc1Code);
		tag.setColor2Kdc(kdc2Code);
		tag.setColor3Kdc(kdc3Code);
		tag.setColor4Kdc(kdc4Code);
		tag.setColor5Kdc(kdc5Code);
		tag.setColor6Kdc(kdc6Code);
		tag.setColor7Kdc(kdc7Code);
		tag.setColor8Kdc(kdc8Code);
		tag.setColor9Kdc(kdc9Code);
		tag.setColorBlankKdc(kdcBlankCode);
		
		
		try {
			tagSettingService.updateTagColor(tag);
			tag = tagSettingService.selectRollTagPositionValue(tag);
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		model.addAttribute("tag", tag);
		
		return web.redirect(web.getRootPath() + "/book/print_color_setting.do", "태그 색상이 수정되었습니다.");
		/*return new ModelAndView("book/print_position_setting");*/
	}
	
	/*인쇄시 자동 순서로딩기 잘 안씀.*/
	@RequestMapping(value="/book/autoSheetCountUp.do", method = RequestMethod.POST)
	public void updatePrintEaSheetCount(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
		web.init();
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
			/*return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");*/
		}
		
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		int printingEa = web.getInt("printingEa");
		int printingSheetCount = web.getInt("printingSheetCount");
		int currentSheetCount = 0;
		
		TagSetting tag = new TagSetting();
		tag.setIdLib(loginInfo.getIdLibMng());
		tag.setPrintingEa(printingEa);
		tag.setPrintingSheetCount(printingSheetCount);
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		//전체 데이터 수 구하기
		int totalCount = 0;
		
		try {
			tagSettingService.updatePrintingEaAndSheetCount(tag);
			totalCount = bookHeldService.selectBookCountForPage(bookHeld);
			currentSheetCount = tagSettingService.selectCurrentSheetCount(tag);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		page.pageProcess(currentSheetCount, totalCount, printingEa, 1);
		int totalPage = page.getTotalPage();
		
		int nextSheet = currentSheetCount + 1;
		
		try {
			if(nextSheet > totalPage) {
				nextSheet = 1;
			} else {
				tagSettingService.updatePrintingSheetCountUp(tag);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("result", nextSheet);
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
	}
	
	/** txt파일 일괄 출력 페이지 */
	@RequestMapping(value = "/book/print_label_text_batch.do", method = RequestMethod.GET)
	public ModelAndView printLabelTextBatch(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}

		return new ModelAndView("book/print_label_text_batch");
	}
	
	/*일괄출력 확인 페이지*/
	@RequestMapping(value = "/book/print_label_text_batch_check.do")
	public ModelAndView printLabelTextBatchCheck(Locale locale, Model model, HttpServletRequest request,
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
			upload.multipartRequest("/addiCodeTemp");
		} catch (Exception e) {
			return web.redirect(null, "multipart 데이터가 아닙니다.");
		}

		// UploadHelper에서 텍스트 형식의 파라미터를 분류한 Map을 리턴받아서 값을 추출한다.
		Map<String, String> paramMap = upload.getParamMap();
		String tagTypeWebGet = paramMap.get("tagTypeTxtBatch");
		// 태그 타입에 따른 분기를 해야하는데, 우선은 임시로 그냥 11번타입의
		//태그만 출력으로 세팅
		
		int tagType = Integer.parseInt(tagTypeWebGet);
		
		String startPointWebGet = paramMap.get("startPointTxtBatch");
		int startPoint = Integer.parseInt(startPointWebGet);
		
		
		TagSetting tag = new TagSetting();
		tag.setIdLib(loginInfo.getIdLibMng());
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		/*bookHeld.setAdditionalCode(addiCode);*/
		
		
		/** (6) 업로드 된 파일 정보 추출 */
		List<FileInfo> fileList = upload.getFileList();
		List<BookHeld> bookHeldList = new ArrayList<BookHeld>();
		
		

		try {
			
			tag = tagSettingService.selectRollTagPositionValue(tag);
			// 업로드 된 파일의 수 만큼 반복 처리 한다.
			for (int i = 0; i < fileList.size(); i++) {
				// 업로드 된 정보 하나 추출하여 데이터베이스에 저장하기 위한 형태로 가공해야 한다.
				FileInfo info = fileList.get(i);

				// DB에 저장하기 위한 항목 생성
				BbsFile file = new BbsFile();

				// 도서관 아이디 지정
				file.setIdLibFile(loginInfo.getIdLibMng());
				
				// 데이터 복사
				file.setOriginName(info.getOrginName());
				file.setFileDir(info.getFileDir());
				file.setFileName(info.getFileName());
				file.setContentType(info.getContentType());
				file.setFileSize(info.getFileSize());
				
				// 저장처리 할필요가 없다
				/*bbsFileService.insertRegBookFile(file);*/
				
				String loadFilePath = info.getFileDir() + "/" + info.getFileName();
				FileInputStream fileStream = new FileInputStream(loadFilePath);
				InputStreamReader isr = new InputStreamReader(fileStream, "UTF-8");
				BufferedReader brFile = new BufferedReader(isr);
				String line = null;
				
				
				while((line = brFile.readLine()) != null) {
					bookHeld.setLocalIdBarcode(line);
					int bookExistCheck = bookHeldService.selectBookHeldCountByBarcode(bookHeld);
					if(bookExistCheck == 0) continue;
					
					BookHeld item = new BookHeld();
					item = bookHeldService.getBookHelditem(bookHeld);
					//System.out.println(item);
					
					String classCode = item.getClassificationCode();
					if(classCode == null) {
						classCode = "-1";
					}
					int classCodeInt = (int)Float.parseFloat(classCode);
					if(classCodeInt < 0) {
						item.setClassCodeHead(classCodeInt);
					} else {
						int classHead1 = classCodeInt/100;
						int classCodeHead = classHead1 * 100;
						item.setClassCodeHead(classCodeHead);
						item.setClassCodeColor(frequentlyFunction.getColorKDC(tag, classCodeHead));
						item.setClassCodeSection(frequentlyFunction.getNameKDC(classCodeHead));
					}
					
					//qrCodeChecker 가 0보다 크면 qr 코드 필요.
					int qrCodeChecker = util.qrCodeChecker(tagType);
					if(qrCodeChecker > 0) {
						//System.out.println("qr코드 생성");
						//QR코드 생성
						qrCode.makeQR(item.getLocalIdBarcode(), 60, 60
								,"/var/packages/Tomcat7/target/src/webapps/downloads/upload/finebook4/qrcode/libNo"+loginInfo.getIdLibMng()+"/"
								,item.getLocalIdBarcode()+".png" );
					}
					
					bookHeldList.add(item);
					item = null;
				} //while 문 끝
				
				brFile.close();
				//업로드된 파일 다시 사용할 일 없을 듯 하니 삭제
				upload.removeFile(loadFilePath);
			}
		} catch (Exception e) {
			return web.redirect(null, e.getLocalizedMessage());
		}
		
		model.addAttribute("bookHeldList", bookHeldList);
		model.addAttribute("tag", tag);
		model.addAttribute("startPoint", startPoint);
		

		//태그 타입 받아온 것으로 페이지 url 변경
		String movePage = util.getMovePageByLabel(tagType);
		
		return new ModelAndView(movePage);
		
	}
}
