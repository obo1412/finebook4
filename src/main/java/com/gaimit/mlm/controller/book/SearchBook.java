package com.gaimit.mlm.controller.book;


import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaimit.helper.ApiHelper;
import com.gaimit.helper.AuthorCode;
import com.gaimit.helper.PageHelper;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.controller.FrequentlyFunction;
import com.gaimit.mlm.model.BbsDocument;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.BbsDocumentService;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BookService;
import com.gaimit.mlm.service.ManagerService;


@Controller
public class SearchBook {
	/** log4j 객체 생성 및 사용할 객체 주입받기 */
	Logger logger = LoggerFactory.getLogger(SearchBook.class);
	// --> import study.spring.helper.WebHelper;
	
	@Autowired
	WebHelper web;
	
	@Autowired
	PageHelper page;
	
	@Autowired
	Util util;
	
	@Autowired
	AuthorCode authorCode;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	BookHeldService bookHeldService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	BbsDocumentService bbsDocumentService;
	
	@Autowired
	ApiHelper apiHelper;
	
	@Autowired
	FrequentlyFunction frequentlyFunction;
	
	/** 도서 검색 페이지 
	 * @throws Exception */
	@ResponseBody
	@RequestMapping(value = "/book/search_book.do", method = RequestMethod.GET)
	public void searchBook(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
		
		String isbn = web.getString("searchKey", "");
		logger.info("도서검색 키 : "+isbn);
		
		
		//copyCode를 가져오기 위한 객체
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		
		//분류기호 출처를 위한 변수
		String srcClassCode = "No Result";
		int copyCode = 0;
		String atcOut = null;
		
		//aladin json 변수
		String category = null;
		String pubDate = null;
		//가격은 달러나 등등 숫자가 아닐 수도 있어서, 그냥 문자로
		String price = null;
		//String으로 받아온 int 형태의 값은 int로 파싱 page
		String itemPage = null;
		int intPage = 0;
		
		String isbn10 = null;
		String description = null;
		String cover = null;
		
		//구매링크를 위한 알라딘 itemId
		String aladinItemId = null;
		//aladin json 변수 끝
		
		//try catch 문 하나로 묶기위하여, 지역변수들 다 try문 밖으로 이동
		//try catch문이 하나로 유지되어야 트랜젝션 유지가능
		//트랜젝션은 하나의 try문에서 쿼리 오류 발생시,
		//동작했던 쿼리문 전체 롤백을 해야하므로.
		JSONObject jsonAladin = new JSONObject();
		JSONObject jsonSeoji = new JSONObject();
		ArrayList<String> xmlClassNoArray = new ArrayList<String>();
		
		String clsCode = null;	// view전달 변수
		
		//저자코드 생성을 위한 제목, 저자명 변수 1순위:알라딘 2순위: 국중
		String titleToCode = null;
		String authorToCode = null;
		String viewPublisher = null;
		
		String viewIsbn13 = null;
		
		//서지정보에서 볼륨코드를 담을 변수
		String volCode = null;
		//jsonSeoji.docs[0].VOL 왼쪽 코드로 뷰페이지에 구현됨.
		String bookSize = null;
		//그러나 바로등록을 위해 변수 담기
		
		try {
			//분류기호 3단계 절차 아래 순서대로
			String kdcStr = null;	// 서지 KDC
			String clsNo = null;	// 국중 class_no
			String eac3 = null;		// 서지 EA_ADD_CODE
			
			// 알라딘 api 에서 json 수신
			//apiHelper로 OpenApi호출 중앙관리
			
			jsonAladin = apiHelper.getJsonApiResult(isbn, 0);
			
			//isbn10자리일 경우, 알라딘에서 isbn13자리를 불러와서 값 넣어주기.
			if(isbn.length()==10) {
				//isbn에 영문을 포함하고 있는 경우,
				if(util.checkEngContain(isbn)) {
					//isbn13를 검색 isbn으로 대체
					if(jsonAladin.get("item") != null && !"".equals(jsonAladin.get("item"))) {
						
						//json타입의 값인 jsonAladin에서 특정값을 가지고옴.
						JSONArray itemArray = (JSONArray) jsonAladin.get("item");
						JSONObject itemObj = (JSONObject) itemArray.get(0);
						//item의 [0] <- 첫번째 값을 가져옴
						Object isbn13Obj = itemObj.get("isbn13");
						//검색을 위한 isbn 13자리로 대체
						String strIsbnObj = String.valueOf(isbn13Obj);
						if(strIsbnObj!=null&&!"".equals(strIsbnObj)) {
							isbn = strIsbnObj;
						}
						//아래 isbn은 공백처리가 된다. 공백처리 될 경우 분류기호 한문으로 나옴.
						//isbn = String.valueOf(isbn13Obj);
						logger.info("영문포함 10자리 ISBN 알라딘값으로 대체: "+isbn);
					}
				}
			}
			//영문을 포함한 isbn10자리일 경우 isbn13로 변경 끝
			
			// 알라딘 api 호출 준비 끝
			
			// 서지정보 api 호출
			
			jsonSeoji = apiHelper.getJsonApiResult(isbn, 2);
			
			if(!"0".equals(jsonSeoji.get("TOTAL_COUNT"))) {
				JSONArray itemSeojiArray = (JSONArray) jsonSeoji.get("docs");
				JSONObject itemSeojiObj = (JSONObject) itemSeojiArray.get(0);
				
				Object kdc = itemSeojiObj.get("KDC");
				Object eac = itemSeojiObj.get("EA_ADD_CODE");
				kdcStr = String.valueOf(kdc);
				
				String eacStr = String.valueOf(eac);
				//5자리중 뒤에 3자리만
				if(eacStr!=null&& !"".equals(eacStr)) {
					eac3 = eacStr.substring(2);
				}
				
				Object titleSeo = itemSeojiObj.get("TITLE");
				Object authorSeo = itemSeojiObj.get("AUTHOR");
				
				authorToCode = String.valueOf(authorSeo);
				titleToCode = String.valueOf(titleSeo);
				
				Object volCodeObj = itemSeojiObj.get("VOL");
				volCode = String.valueOf(volCodeObj);
				
				Object bookSizeObj = itemSeojiObj.get("BOOK_SIZE");
				bookSize = String.valueOf(bookSizeObj);
				
				Object publisherObj = itemSeojiObj.get("PUBLISHER");
				viewPublisher = String.valueOf(publisherObj);
				
				Object isbnObj = itemSeojiObj.get("EA_ISBN");
				viewIsbn13 = String.valueOf(isbnObj);
			}
			logger.info("서지 서칭 완료");
			//서지정보 호출 끝
			
			//국립중앙도서관 아래 api검색
			//http://www.nl.go.kr/app/nl/search/openApi/search.jsp?key=6debf14330e5866f7c50d47a9c84ae8f&category=dan&detailSearch=true&isbnOp=isbn&isbnCode=8984993727
			// 국중은 openapi가 xml 형태밖에 없는 듯하여 xml 호출 구조
			ArrayList<String> titleAndAuthor = new ArrayList<String>();
				
			String apiUrlFullNl = apiHelper.getNlXmlIsbnResult(isbn);
			logger.info("국중 api");
			URL urlNl = new URL(apiUrlFullNl);
			HttpURLConnection conNl = (HttpURLConnection)urlNl.openConnection();
			conNl.setRequestMethod("GET");
			conNl.getResponseCode(); // 응답코드 리턴 200번대 404 등등
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc =builder.parse(conNl.getInputStream());
			
			//xml 결과값 total 값을 가져오기 위함.
			NodeList nodeTotalVal = doc.getElementsByTagName("total");
			int nodeTotalCount = Integer.parseInt(nodeTotalVal.item(0).getTextContent());
			//total 결과 값이 0 보다 크면 item 값을 가져오기.
			/*if(nodeTotalCount>0) {
				//이 방식으로 하는게 문제가 덜 할 것 같긴 한데...
			}*/
			//item 값이 있건 없건 nodeList는 호출됨.
			//아래 nodeTotalCount>0 과 동시만족시 값을 불러옴.
			NodeList nodeList = doc.getElementsByTagName("item");
			
			if(nodeList.getLength()>0 && nodeTotalCount>0) {
				for(int i =0; i<nodeList.getLength(); i++) {
					for(Node node = nodeList.item(i).getFirstChild(); node!=null;
						node=node.getNextSibling()) {
						if(node.getNodeName().equals("title_info")) {
							titleAndAuthor.add(node.getTextContent());
						}
						if(node.getNodeName().equals("author_info")) {
							titleAndAuthor.add(node.getTextContent());
						}
						if(node.getNodeName().equals("pub_info")) {
							//위에 먼저 서지에 정보를 넣고,
							//국중에 정보가 있으면 국중 정보를 대입.
							viewPublisher = node.getTextContent();
						}
						if(node.getNodeName().equals("isbn")) {
							String nlIsbn = node.getTextContent();
							//isbn이 한개만 뜨는게 아니라 여러개 뜰수가 있어서 처리
							if(nlIsbn!=null&&!"".equals(nlIsbn)) {
								if(nlIsbn.length()>13) {
									if(nlIsbn.indexOf(isbn)>-1) {
										viewIsbn13 = nlIsbn.substring(nlIsbn.indexOf(isbn), nlIsbn.indexOf(isbn)+13);
									}
								} else {
									viewIsbn13 = node.getTextContent();
								}
							}
						}
						if(node.getNodeName().equals("class_no")) {
							xmlClassNoArray.add(node.getTextContent());
						}
					}
				}
				if(xmlClassNoArray.size()>0&&xmlClassNoArray.get(0)!=null) {
					for(int x=0; x<xmlClassNoArray.size(); x++) {
						if(xmlClassNoArray.get(x)!=null&&!"".equals(xmlClassNoArray.get(x))) {
							clsNo = xmlClassNoArray.get(x);
						}
					}
					//System.out.println("clsNo값"+clsNo+" isbn:"+isbn);
				}
				if(titleAndAuthor.get(0).length()!=0&&titleAndAuthor.get(0)!=null) {
					titleToCode = titleAndAuthor.get(0);
				}
				if(titleAndAuthor.get(1).length()!=0&&titleAndAuthor.get(1)!=null) {
					authorToCode = titleAndAuthor.get(1);
				}
			}
			logger.info("국중 검색 완료");
			//국중 정보 호출 끝
			
			//알라딘 정보 호출
			if(jsonAladin.get("item") != null && !"".equals(jsonAladin.get("item"))) {
				
				//json타입의 값인 jsonAladin에서 특정값을 가지고옴.
				JSONArray itemArray = (JSONArray) jsonAladin.get("item");
				JSONObject itemObj = (JSONObject) itemArray.get(0);
				//item의 [0] <- 첫번째 값을 가져옴
				Object authorObj = itemObj.get("author");
				Object titleObj = itemObj.get("title");
				Object publisherObj = itemObj.get("publisher");
				Object isbn13Obj = itemObj.get("isbn13");
				//구매링크를 위한
				Object itemIdObj = itemObj.get("itemId");
				aladinItemId = String.valueOf(itemIdObj);
				
				//authorToCode = (String)authorObj;
				//위처럼도 casting만 바꾸는 방법도 있음.
				if(!"".equals(titleObj)) {
					titleToCode = String.valueOf(titleObj);
				}
				
				if(!"".equals(authorObj)) {
					authorToCode = String.valueOf(authorObj);
				}
				
				if(!"".equals(publisherObj)) {
					viewPublisher = String.valueOf(publisherObj);
				}
				
				if(!"".equals(isbn13Obj)) {
					viewIsbn13 = String.valueOf(isbn13Obj);
				}
				
				Object objCategory = itemObj.get("categoryName");
				if(!"".equals(objCategory)) {
					category = String.valueOf(objCategory);
				}
				
				Object objPubDate = itemObj.get("pubDate");
				pubDate = String.valueOf(objPubDate);
				JSONObject objSubInfo = (JSONObject) itemObj.get("subInfo");
				Object objPage = objSubInfo.get("itemPage");
				if(objPage != null) {
					itemPage = String.valueOf(objPage);
					intPage = 0;
					if(!"".equals(itemPage)&&!"0".equals(itemPage)) {
						intPage = Integer.parseInt(itemPage);
					}
				}
				
				Object objPrice = itemObj.get("priceStandard");
				price = String.valueOf(objPrice);
				
				Object objIsbn10 = itemObj.get("isbn");
				isbn10 = String.valueOf(objIsbn10);
				Object objCover = itemObj.get("cover");
				cover = String.valueOf(objCover);
				Object objDescription = itemObj.get("description");
				description = String.valueOf(objDescription);
				
			}
			logger.info("알라딘 검색 완료");
			//알라딘 정보 호출 끝
			
			//우선순위에 따라서 최종에 남은 도서명과 저자명으로
			//저자코드 생성
			if(titleToCode!=null&&authorToCode!=null) {
				atcOut = authorCode.authorCodeGen(authorToCode)
						+ authorCode.titleFirstLetter(titleToCode);
			}
			
			//kdcStr!=null&&kdcStr.length()!=0 이 조건으로 했을 경우에 length() 부분에서
			// NPE 에러가 발생한다.
			if(kdcStr!=null&&(!"".equals(kdcStr))&&!"null".equals(kdcStr)) {
				srcClassCode = "서지";
				clsCode = kdcStr;
			} else if(clsNo!=null&&(!"".equals(clsNo))&&!"null".equals(clsNo)) {
				srcClassCode = "국중";
				clsCode = clsNo;
			} else if(eac3!=null&&(!"".equals(eac3))&&!"null".equals(eac3)) {
				srcClassCode = "부가기호";
				clsCode = eac3;
			}
			
			//복본기호 따로 빼는거 너무 기능이 복잡해져서 안될듯.
			/*
			String chkBoxCollectionBooksCopyCode = web.getString("colleBooksSwitch");
			logger.info("전집류 복본기호 체크박스 상태 "+chkBoxCollectionBooksCopyCode);
			
			if("true".equals(chkBoxCollectionBooksCopyCode)) {
				bookHeld.setIsbn13(isbn);
				copyCode = frequentlyFunction.getCopyCodeByFirstCheckISBN(bookHeld);
			} else {
				//복본기호
				if(titleToCode!=null&&authorToCode!=null) {
					bookHeld.setIsbn13(isbn);
					bookHeld.setTitle(titleToCode);
					bookHeld.setWriter(authorToCode);
					copyCode = frequentlyFunction.getCopyCode(bookHeld);

				}
			}*/
			
			//복본기호
			if(titleToCode!=null&&authorToCode!=null) {
				bookHeld.setIsbn13(isbn);
				bookHeld.setTitle(titleToCode);
				bookHeld.setWriter(authorToCode);
				copyCode = frequentlyFunction.getCopyCode(bookHeld);
			}
			
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("isbn", isbn);
		data.put("atcOut", atcOut);
		data.put("copyCode", copyCode);
		data.put("bookTitle", titleToCode);
		data.put("author", authorToCode);
		data.put("publisher", viewPublisher);
		data.put("pubDate", pubDate);
		data.put("isbn13", viewIsbn13);
		data.put("isbn10", isbn10);
		data.put("jsonAladin", jsonAladin);
		data.put("jsonSeoji", jsonSeoji);
		data.put("xmlClassNoArray", xmlClassNoArray);
		data.put("clsCode", clsCode);
		data.put("volCode", volCode);
		data.put("bookSize", bookSize);
		data.put("srcClassCode", srcClassCode);
		data.put("category", category);
		data.put("price", price);
		data.put("intPage", intPage);
		data.put("bookCover", cover);
		data.put("bookDesc", description);
		data.put("aladinItemId", aladinItemId);
		
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
	}
	
	
	/** 국중에서 도서 검색 */
	@RequestMapping(value = "/book/search_nl_book.do", method = RequestMethod.GET)
	public ModelAndView nlSearch(Locale locale, Model model) {
		
		/** 1) WebHelper 초기화 및 파라미터 처리 */
		web.init();
		
		/** 로그인 여부 검사 */
		// 로그인중인 회원 정보 가져오기
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		if (loginInfo == null) {
			return web.redirect(web.getRootPath() + "/index.do", "로그인 후에 이용 가능합니다.");
		}
		
		int searchOpt = web.getInt("searchOpt");
		String keyword = web.getString("search-book-info", "");
		if("".equals(keyword)) {
			keyword = web.getString("keyword");
		}
		
		//전체페이지를 얻기 위한 값.
		int totalCount = 0;
		int nowPage = web.getInt("page", 1);
		
		if(keyword != null && !"".equals(keyword)) {
			String apiUrl = null;
			
			//현재 이 기능은 브라우저에선 결과값을 주지만,
			//코드 상태로는 서버 응답값이 502 에러가 발생한다. 나중에 처리하자
			//처리 되었음.
			if(searchOpt==4) {
				String[] array = keyword.split(" ");
				apiUrl = apiHelper.getNlXmlTypeResult(searchOpt, array[0], array[1], nowPage);
			} else {
				apiUrl = apiHelper.getNlXmlTypeResult(searchOpt, keyword, null, nowPage);
			}
			
			//아래는 스위치문 중복으로 사용하므로 주석 처리.
			/*switch (searchOpt) {
			case 1:
				apiUrl = apiHelper.getNlXmlTypeResult(1, keyword, null);
				break;
			case 2:
				apiUrl = apiHelper.getNlXmlTypeResult(2, keyword, null);
				break;
			case 3:
				apiUrl = apiHelper.getNlXmlTypeResult(3, keyword, null);
				break;
			case 4:
				String[] array = keyword.split(" ");
				apiUrl = apiHelper.getNlXmlTypeResult(searchOpt, array[0], array[1]);
				break;
			}*/
			
			//국립중앙도서관 아래 api검색
			//http://www.nl.go.kr/app/nl/search/openApi/search.jsp?key=6debf14330e5866f7c50d47a9c84ae8f&category=dan&detailSearch=true&isbnOp=isbn&isbnCode=8984993727
			// 국중은 openapi가 xml 형태밖에 없는 듯하여 xml 호출 구조
			ArrayList<Object> xmlArray = new ArrayList<Object>();
			try {
				
				URL url = new URL(apiUrl);
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				con.setRequestMethod("GET");
				con.getResponseCode(); // 응답코드 리턴 200번대 404 등등
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc =builder.parse(con.getInputStream());
				
				//xml 결과값 total 값을 가져오기 위함.
				NodeList nodeTotalVal = doc.getElementsByTagName("total");
				totalCount = Integer.parseInt(nodeTotalVal.item(0).getTextContent());
				
				page.pageProcess(nowPage, totalCount, 10, 5);
				//페이징 처리
				
				NodeList nodeList = doc.getElementsByTagName("item");
				for(int i =0; i< nodeList.getLength(); i++) {
					Map<String, String> tempMap = new HashMap<String, String>();
					for(Node node = nodeList.item(i).getFirstChild(); node!=null; node=node.getNextSibling()) {
						if(node.getNodeName().equals("title_info")) {
							tempMap.put("title_info", node.getTextContent());
						}
						if(node.getNodeName().equals("author_info")) {
							tempMap.put("author_info", node.getTextContent());
						}
						if(node.getNodeName().equals("pub_info")) {
							tempMap.put("pub_info", node.getTextContent());
						}
						if(node.getNodeName().equals("isbn")) {
							tempMap.put("isbn", node.getTextContent());
						}
						if(node.getNodeName().equals("class_no")) {
							tempMap.put("class_no", node.getTextContent());
							xmlArray.add(tempMap);
						}
					}
				}
				model.addAttribute("xmlArray", xmlArray);
			} catch(Exception e) {
				String exceptionMsg = e.getLocalizedMessage();
				if(e.getLocalizedMessage().indexOf(apiHelper.NLKcertKey)>-1) {
					exceptionMsg = e.getLocalizedMessage().replace(apiHelper.NLKcertKey, "*****");
				}
				return web.redirect(null, exceptionMsg);
			}
		}
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("searchOpt", searchOpt);
		model.addAttribute("page", page);
		model.addAttribute("pageDefUrl", "/book/search_nl_book.do");
		
		
		return new ModelAndView("book/search_nl_book");
	}
	
	
	/** 중복도서 구매 방지를 위하여, 중복도서 체크 및 도서 검색 ajax 통신 
	 * @throws Exception */
	@ResponseBody
	@RequestMapping(value = "/book/search_book_dup_check_lib.do", method = RequestMethod.GET)
	public void searchBookAndCheckDupInLib(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
		//Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		
		if (loginInfo == null) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
			return;
		}
		
		String isbn = web.getString("isbnReq");
		String title = web.getString("titleReq");
		String writer = web.getString("authorReq");
		
		if(isbn != null) {
			isbn = isbn.trim();
			isbn = isbn.replaceAll(" ", "");
		}
		
		if(title != null) {
			title = title.trim();
			title = title.replaceAll(" ", "");
		}
		
		if(writer != null) {
			writer = writer.trim();
			writer = writer.replaceAll(" ", "");
		}
		
		BookHeld bookHeld = new BookHeld();
		bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
		bookHeld.setIsbn13(isbn);
		bookHeld.setTitle(title);
		bookHeld.setWriter(writer);
		
		List<BookHeld> bookList = null;
		//위 도서관 내부 검색
		
		//외부 정보 검색
		JSONObject jsonAladin = new JSONObject();
		JSONObject jsonNl = new JSONObject();
		//외부데이터 담을 객체
		JSONObject extData = null;
		//국중과 알라딘의 json key 값이 다르므로... 여기서 하나하나 뽑아서 주는게 나을듯.
		//뷰페이지에서 js로 처리함.
		
		//외부 api 데이터 목록
		JSONObject apiList = null;
		
		if(isbn != null) {
			//isbn값이 있을 경우,
			jsonAladin = apiHelper.getJsonApiResult(isbn, 0);
			if(jsonAladin.get("item")==null) {
				//System.out.println("알라딘 내용 없음.");
				//알라딘에 내용 없으면 국중에 한번더 체크, 국중에도 없으면 그냥 빠져나오기
				jsonNl = apiHelper.getJsonApiResult(isbn, 1);
				
				if(jsonNl.get("result")==null) {
					//System.out.println("국중내용 없음.");
					//국중에도 내용 없음. 아무것도 하지 않음.
				} else {
					//json타입의 값을 국중에서 가져오기.
					JSONArray itemArray = (JSONArray) jsonNl.get("result");
					extData = (JSONObject) itemArray.get(0);
				}
				
			} else {
				//json타입의 값인 jsonAladin에서 특정값을 가지고옴.
				JSONArray itemArray = (JSONArray) jsonAladin.get("item");
				extData = (JSONObject) itemArray.get(0);
			}
		}
		
		//프론트에서 도서명, 저자명 둘다 있어야하지만. 둘중하나라도 입력된다면.
		if(title!=null||writer!=null) {
			apiList = apiHelper.getAladinJsonResultTitleAndAuthor(title, writer);
			if(apiList.get("item")==null) {
				//알라딘 결과가 없으면,
				apiList = apiHelper.getNLJsonResultTitleAndAuthor(title, writer);
			}
		}
		
		try {
			//도서관 내 도서 정보
			bookList = bookHeldService.selectBookListCheckDupNoBlank(bookHeld);
			
			
			
		} catch(Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("bookList", bookList);
		data.put("extData", extData);
		data.put("apiList", apiList);
		
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
	}
	
	/** 중복도서 구매 방지를 위하여, 중복도서 체크 및 도서 검색 ajax 통신 
	 * 게시글이 이미 있는지 체크
	 * @throws Exception */
	@ResponseBody
	@RequestMapping(value = "/book/search_book_dup_check_document.do", method = RequestMethod.GET)
	public void searchBookAndCheckDupDocumentList(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
		//Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		
		if (loginInfo == null) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
			return;
		}
		
		String isbn = web.getString("isbnApi");
		String title = web.getString("titleApi");
		String writer = web.getString("authorApi");
		
		if(isbn != null) {
			isbn = isbn.trim();
		}
		
		if(title != null) {
			title = title.trim();
		}
		
		if(writer != null) {
			writer = writer.trim();
		}
		
		BbsDocument document = new BbsDocument();
		document.setIdLibMng(loginInfo.getIdLibMng());
		document.setReqBookIsbn(isbn);
		document.setReqBookTitle(title);
		document.setReqBookAuthor(writer);
		
		List<BbsDocument> documentList = null;
		//위 도서관 내부 검색
		
		try {
			//도서관 내 도서 정보
			documentList = bbsDocumentService.selectCheckRequestBook(document);
		} catch(Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rt", "OK");
		data.put("documentList", documentList);
		
		
		// --> import com.fasterxml.jackson.databind.ObjectMapper;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}
		
	}
	
	/** 중복도서 구매 방지를 위하여, 중복도서 체크 및 도서 검색 ajax 통신
	 * 게시글 제목 변경하기, 처리중 구매 완료 등
	 * @throws Exception */
	@ResponseBody
	@RequestMapping(value = "/book/update_document_subject_state.do", method = RequestMethod.GET)
	public void updateDocumentSubjectState(Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
		//Manager loginInfo = (Manager) web.getSession("loginInfo");
		// 로그인 중이 아니라면 이 페이지를 동작시켜서는 안된다.
		Manager loginInfo = (Manager) web.getSession("loginInfo");
		
		if (loginInfo == null) {
			web.printJsonRt("로그인 후 이용 가능합니다.");
			return;
		}
		
		int documentId = 0;
		documentId = web.getInt("document_id");
		
		String stateTo = web.getString("stateTo");
		
//		if (!"요청".equals(stateTo)||!"진행중".equals(stateTo)||!"완료".equals(stateTo)) {
//			web.printJsonRt("올바른 상태 전달이 아닙니다.");
//			return;
//		}
		
		BbsDocument document = new BbsDocument();
		document.setId(documentId);
		document.setSubject(stateTo);
	
		
		try {
			bbsDocumentService.updateDocumentSubjectOnly(document);
		} catch(Exception e) {
			web.printJsonRt(e.getLocalizedMessage());
		}

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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

