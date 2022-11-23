package com.gaimit.mlm.controller;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.gaimit.helper.Util;
import com.gaimit.helper.WebHelper;
import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.model.Library;
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
}
