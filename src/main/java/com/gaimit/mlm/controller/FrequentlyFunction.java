package com.gaimit.mlm.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.gaimit.helper.ApiHelper;
import com.gaimit.helper.AuthorCode;
import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Library;
import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.model.TagSetting;
import com.gaimit.mlm.service.BbsDocumentService;
import com.gaimit.mlm.service.BookHeldService;
import com.gaimit.mlm.service.BrwService;
import com.gaimit.mlm.service.LibraryService;
import com.gaimit.mlm.service.ManagerService;
import com.gaimit.mlm.service.MemberService;
import com.gaimit.mlm.service.TagSettingService;

@Controller
public class FrequentlyFunction {
	
	Logger logger = LoggerFactory.getLogger(FrequentlyFunction.class);
	
	@Autowired
	SqlSession sqlSession;
	
	@Autowired
	WebHelper web;
	
	@Autowired
	Util util;
	
	@Autowired
	BbsDocumentService bbsDocumentService;
	
	@Autowired
	BrwService brwService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	BookHeldService bookHeldService;
	
	@Autowired
	TagSettingService tagSettingService;
	
	@Autowired
	LibraryService libraryService;
	
	@Autowired
	ApiHelper apiHelper;
	
	@Autowired
	AuthorCode authorCode;
	
	public String testFunction(BookHeld bookHeld) throws Exception {
		String result = null;
		bookHeld = bookHeldService.getBookHelditem(bookHeld);
		result = String.valueOf(bookHeld.getCopyCode());
		return result;
	}
	
	public String getNameKDC(int code) {
		String result = null;
			if(code >= 0 && code <100) {
				result = "총류";
			} else if(code >= 100 && code < 200) {
				result = "철학";
			} else if(code >= 200 && code < 300) {
				result = "종교";
			} else if(code >= 300 && code < 400) {
				result = "사회과학";
			} else if(code >= 400 && code < 500) {
				result = "자연과학";
			} else if(code >= 500 && code < 600) {
				result = "기술과학";
			} else if(code >= 600 && code < 700) {
				result = "예술";
			} else if(code >= 700 && code < 800) {
				result = "언어";
			} else if(code >= 800 && code < 900) {
				result = "문학";
			} else if(code >= 900 && code < 1000) {
				result = "역사";
			} else {
				result = "미분류";
			}
		return result;
	}
	
	public String getColorKDC(TagSetting tag, int code) {
		String result = null;
		try {
			tag = tagSettingService.selectRollTagPositionValue(tag);
			if(code >= 0 && code <100) {
				result = tag.getColor0Kdc();
			} else if(code >= 100 && code < 200) {
				result = tag.getColor1Kdc();
			} else if(code >= 200 && code < 300) {
				result = tag.getColor2Kdc();
			} else if(code >= 300 && code < 400) {
				result = tag.getColor3Kdc();
			} else if(code >= 400 && code < 500) {
				result = tag.getColor4Kdc();
			} else if(code >= 500 && code < 600) {
				result = tag.getColor5Kdc();
			} else if(code >= 600 && code < 700) {
				result = tag.getColor6Kdc();
			} else if(code >= 700 && code < 800) {
				result = tag.getColor7Kdc();
			} else if(code >= 800 && code < 900) {
				result = tag.getColor8Kdc();
			} else if(code >= 900 && code < 1000) {
				result = tag.getColor9Kdc();
			} else {
				result = tag.getColorBlankKdc();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//RegBookOK에서 사용 복본기호 불러오기
	public int getCopyCode(BookHeld bookHeld) {
		int result = 0;
		
		try {
			//동일한 도서가 있는지 체크
			int copyCheckBookHeld = bookHeldService.selectBookHeldCount(bookHeld);
			//System.out.println("동일도서"+copyCheckBookHeld);
			//zeroCopyCode는 복본기호가 0인 것을 찾는 쿼리
			int zeroCopyCode = bookHeldService.selectZeroCopyCodeCount(bookHeld);
			//System.out.println("복본기호가 0인것 카운트"+zeroCopyCode);
			
			if(copyCheckBookHeld == 0) {
				//bookheld 테이블에 없으면 바로 등록
			} else if(copyCheckBookHeld > 1){
				//복본이 여러개이며,
				if(zeroCopyCode == 1) {
					//bookheld 테이블에 여러권 존재시 2번 시작기준 빠진 번호 검색
					int firstCopyCode = bookHeldService.selectFirstCopyCode(bookHeld);
					if(firstCopyCode != 2) {
						result = 2;
					} else {
						int lastEmptyCopyCode = bookHeldService.selectLastEmptyCopyCode(bookHeld);
						result = lastEmptyCopyCode;
					}
				} else {
					//zeroCopyCode가 1이 아니라는 의미는, 복본기호 0이 없다는 것.
					//result = 0;
				}
			} else if(copyCheckBookHeld == 1) {
				//위 경우 도서관에 책이 반드시 있는 경우다.
				 // 아래의 최초복본기호 체크가 copy_code != 0이 아닌 조건으로
				 // 검색했기 때문에, null이 나올 수 있다.
				 // 따라서 firstcopycode가 null이면 도서의 복본기호는 0이라는 뜻.
				 // 이 경우엔 복본기호를 2로 지정하고
				 // 0이 아닌 모든 경우엔 새로등록할 도서의 복본기호를 0으로 설정
				if(zeroCopyCode == 1) {
					result = 2;
				} else {
					//result = 0;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	//ISBN이 같으면 복본처리 진행, ISBN이 다르면 
	//이거 전집류 복본처리(제목과 저자가 같아도 isbn이 다르면 복본 x)를 위한건데
	//너무 getcopycode에서 zero copy code 함수등 충돌이 너무 심함.
	// 좀더 차분히 고려해서 로직 다시 짜야될듯.
	public int getCopyCodeByFirstCheckISBN(BookHeld bookHeld) throws Exception {
		int result = 0;
		
		int isbnChecker = bookHeldService.selectDupCountByOnlyISBN(bookHeld);
		if(isbnChecker > 0) {
			//isbn이 동일한 책이 존재할 경우, 복본처리
			result = getCopyCode(bookHeld);
		}
		//isbn만 동일한게 있는지 체크하는 함수 만들어서,
		//isbn 동일한게 없으면 그냥 무조건 복본기호 0
		
		return result;
	}
	
	/**
	 * 
	 * @param Type  1: 최종 등록번호+1로 등록, 2: 비어있는 등록번호 등록
	 * @param bookHeld
	 * @return
	 */
	public String getLastBarcode(int type, BookHeld bookHeld) {
		String result = null;
		String barcodeInit = "";
		int barcodeNum = 1;
		//0 전체, 1 바코드헤드, 2 바코드번호
		
		//등록번호 자리수
		Library library = new Library();
		library.setIdLib(bookHeld.getLibraryIdLib());
		
		
		try {
			//등록번호 자리수 가져오기.
			Integer numOfDigits = libraryService.selectNodBarcodeByIdLib(library);
			if(numOfDigits==null||numOfDigits==0) {
				numOfDigits = 8;
			}
			
			//마지막 바코드 번호 가져오기
			Integer lastSortingIndex = bookHeldService.selectLastSortingIndex(bookHeld);
			if(lastSortingIndex == null) {
				//max가 null이면 번호가 아예 없다는 것.
			} else {
				//마지막 값이 존재할 경우. 마지막 바코드헤드 가져오기
				String lastBarcode = bookHeldService.selectLastLocalBarcode(bookHeld);
				barcodeInit = util.strExtract(lastBarcode);
				//type에 따른 분기
				if(type==1) {
					//마지막 인덱스+1 사용할 바코드 번호
					barcodeNum = lastSortingIndex + 1;
				} else if(type==2) {
					int firstBarcode = bookHeldService.selectFirstLocalBarcode(bookHeld);
					if(firstBarcode ==1) {
						barcodeNum = bookHeldService.selectEmptyLocalBarcode(bookHeld);
					} else {
						barcodeNum = 1;
					}
				}
			}
			result = util.makeStrLength(numOfDigits, barcodeInit, barcodeNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 도서관별 등록번호 만들어주기
	 * @param idLib
	 * @param barcodeHead
	 * @param barcodeNum
	 * @return
	 */
	public String makeBarcodeByNumOfDigits(int idLib, String barcodeHead, int barcodeNum) {
		String result = null;
		
		//등록번호 자리수
		Library library = new Library();
		library.setIdLib(idLib);
		
		//등록번호 자리수 가져오기.
		Integer numOfDigits;
		try {
			numOfDigits = libraryService.selectNodBarcodeByIdLib(library);
			if(numOfDigits==null||numOfDigits==0) {
				numOfDigits = 8;
			}
			result = util.makeStrLength(numOfDigits, barcodeHead, barcodeNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
	
	
	
	//등급 id를 등급 이름으로 가져오기, 회원 엑셀 정보 가져오기 사용시.
	public Integer getGradeIdByGradeName(String gradeName, int idLib) {
		Integer gradeId = null;
		
		Member member = new Member();
		member.setIdLib(idLib);
		member.setGradeName(gradeName);
		
		try {
			gradeId = memberService.selectGradeIdByGradeNameForImportMember(member);
			if(gradeId == null) {
				//해당 등급이름으로, 기본 5,5 설정 후 등급 등록
				member.setBrwLimit(5);
				member.setDateLimit(5);
				memberService.insertGrade(member);
				gradeId = memberService.selectGradeIdByGradeNameForImportMember(member);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//객체 삭제
		member = null;
		
		return gradeId;
	}
	
	
	
	/**
	 * txt isbn 일고라 등록 처리 함수
	 * @param fileStream - txt 파일 한번에 여기서 담아서 2차원 배열로 리턴
	 * @return
	 * @throws IOException 
	 */
	public String[][] txtExtractValues(FileInputStream fileStream) throws IOException {
		
		/** (3) 로그인 여부 검사 */
		Manager loginInfo = (Manager) web.getSession("loginInfo");
			
		if(fileStream == null || "".equals(fileStream)) {
			return null;
		}
		
		InputStreamReader isr = new InputStreamReader(fileStream, "UTF-8");
		BufferedReader brFile = new BufferedReader(isr);
		String line = null;
		
		List<String> isbnArr = new ArrayList<>();
		
		//총 개수 구하기.
		int i=0;
		
		while((line = brFile.readLine()) != null) {
			isbnArr.add(line);
			i++;
		}
		//구해진 총개수 대로 배열 선언
		String[][] result = new String[i][15];
		
		try {
			for(int j=0; j<i; j++) {
				JSONObject jsonAladin = new JSONObject();
				jsonAladin = apiHelper.getJsonApiResult(isbnArr.get(j), 0);
				
				String isbn = isbnArr.get(j);
				
				//마지막 바코드번호 최초 1회 불러오기
				String barcode = null;
				
				BookHeld bookHeld = new BookHeld();
				bookHeld.setLibraryIdLib(loginInfo.getIdLibMng());
				//새로 생성한 바코드 번호 주입
				barcode = getLastBarcode(1, bookHeld);
				barcode = barcode.toUpperCase();
				
				if(j>0) {
					String barcodeHead = util.strExtract(barcode);
					//j번째 번호를 더해주기
					int barcodeNum = util.numExtract(barcode)+j;
					barcode = makeBarcodeByNumOfDigits(loginInfo.getIdLibMng(),barcodeHead,barcodeNum);
				}
				//바코드 처리 끝
				
				//알라딘 정보 호출
				if(jsonAladin.get("item") != null && !"".equals(jsonAladin.get("item"))) {
					
					String titleToCode = null;
					String authorToCode = null;
					String viewPublisher = null;
					
					String viewIsbn13 = null;
					String category = null;
					String pubDate = null;
					//String으로 받아온 int 형태의 값은 int로 파싱 page
					String itemPage = null;
//					int intPage = 0;
					String price = null;
//					String isbn10 = null;
					
					
					//저자기호
					String atcOut = null;
					String clsCode = null;
					String addiCode = null;
					//서지정보에서 볼륨코드를 담을 변수
					String volCode = null;
					String copyCode = null;
					
					//json타입의 값인 jsonAladin에서 특정값을 가지고옴.
					JSONArray itemArray = (JSONArray) jsonAladin.get("item");
					JSONObject itemObj = (JSONObject) itemArray.get(0);
					//item의 [0] <- 첫번째 값을 가져옴
					Object authorObj = itemObj.get("author");
					Object titleObj = itemObj.get("title");
					Object publisherObj = itemObj.get("publisher");
					Object isbn13Obj = itemObj.get("isbn13");
					
					//구매링크를 위한
//					Object itemIdObj = itemObj.get("itemId");
//					aladinItemId = String.valueOf(itemIdObj);
					
					//authorToCode = (String)authorObj;
					//위처럼도 casting만 바꾸는 방법도 있음.
					
//					System.out.println(itemObj);
					
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
//						intPage = 0;
//						if(!"".equals(itemPage)&&!"0".equals(itemPage)) {
//							intPage = Integer.parseInt(itemPage);
//						}
					}
					
					Object objPrice = itemObj.get("priceStandard");
					price = String.valueOf(objPrice);
					
//					Object objIsbn10 = itemObj.get("isbn");
//					isbn10 = String.valueOf(objIsbn10);
//					Object objCover = itemObj.get("cover");
//					cover = String.valueOf(objCover);
//					Object objDescription = itemObj.get("description");
//					description = String.valueOf(objDescription);
					
					if(titleToCode!=null&&authorToCode!=null) {
						atcOut = authorCode.authorCodeGen(authorToCode)
								+ authorCode.titleFirstLetter(titleToCode);
					}
					
					//국중에서 분류기호 가져오기
					JSONObject jsonNl = apiHelper.getJsonApiResult(isbn, 1);
					
					if(jsonNl.get("result")==null) {
						//System.out.println("국중내용 없음.");
					} else {
						//json타입의 값인 jsonAladin에서 특정값을 가지고옴.
						JSONArray itemArrayNL = (JSONArray) jsonNl.get("result");
						JSONObject itemObjNL = (JSONObject) itemArrayNL.get(0);
						
						Object obj = null;
						
						obj = itemObjNL.get("classNo");
						String classNo = String.valueOf(obj);
						if(!"".equals(classNo)) {
							clsCode = classNo;
						}
						
					}
					//국중에서 분류기호 가져오기 끝
					
					//서지에서 권차기호
					JSONObject jsonSeoji = apiHelper.getJsonApiResult(isbn, 2);
					
					if("0".equals(jsonSeoji.get("TOTAL_COUNT"))) {
						//System.out.println("서지내용 없음.");
					} else {
						//json타입의 값인 jsonAladin에서 특정값을 가지고옴.
						JSONArray itemArraySJ = (JSONArray) jsonSeoji.get("docs");
						JSONObject itemObjSJ = (JSONObject) itemArraySJ.get(0);
						
						Object obj = null;
						
						//분류기호 clsCode가 끝까지 공란일 경우 한번더 체크해서 넣기
						if("".equals(clsCode)||clsCode==null) {
							//부가기호
							obj = itemObjSJ.get("EA_ADD_CODE");
							String eaAddCode = String.valueOf(obj);
							if(!"".equals(eaAddCode)) {
								//앞 두자리 빼고 분류기호에 값 넣기.
								eaAddCode = eaAddCode.substring(2);
								clsCode = eaAddCode;
							}
							
							//분류기호
							obj = itemObjSJ.get("KDC");
							String kdc = String.valueOf(obj);
							if(!"".equals(kdc)) {
								clsCode = kdc;
							}
						}
						
						obj = itemObjSJ.get("VOL");
						String vol = String.valueOf(obj);
						if("".equals(vol)||vol==null) {
							volCode = "";
						} else {
							volCode = vol;
						}
					}
					//서지에서 권차기호 끝
					
					
					//도서등록번호0, 도서명1, 저자명2
					//저자기호3, 분류기호4, 별치기호5, 권차기호6, 복본기호7
					//도서분류8, 출판사9, 출판일10, 페이지11, 가격12
					//isbn13 13, 서가14, 
					
					result[j][0] = barcode;
					result[j][1] = titleToCode;
					result[j][2] = authorToCode;
					result[j][3] = atcOut;
					result[j][4] = clsCode;
					result[j][5] = addiCode;
					result[j][6] = volCode;
					result[j][7] = copyCode;
					result[j][8] = category;
					result[j][9] = viewPublisher;
					result[j][10] = pubDate;
					result[j][11] = itemPage;
					result[j][12] = price;
					result[j][13] = viewIsbn13;
					result[j][14] = "";
				} else {
					result[j][0] = barcode;
					result[j][1] = "";
					result[j][2] = "";
					result[j][3] = "";
					result[j][4] = "";
					result[j][5] = "";
					result[j][6] = "";
					result[j][7] = "";
					result[j][8] = "";
					result[j][9] = "";
					result[j][10] = "";
					result[j][11] = "";
					result[j][12] = "";
					result[j][13] = isbnArr.get(j);
					result[j][14] = "";
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//반드시 파일을 닫아줘야 삭제가 된다.
		brFile.close();
		
		
		
		return result;
	}
}
