package com.gaimit.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 기본적인 공통 기능들을 묶어 놓은 클래스
 */
public class Util {

	
	// ----------- 싱글톤 객체 생성 시작 -----------
	private static Util current = null;

	public static Util getInstance() {
		if (current == null) {
			current = new Util();
		}
		return current;
	}

	public static void freeInstance() {
		current = null;
	}

	private Util() {
		super();
	}
	// ----------- 싱글톤 객체 생성 끝 -----------

	/**
	 * 범위를 갖는 랜덤값을 생성하여 리턴하는 메서드 
	 * @param min - 범위 안에서의 최소값
	 * @param max - 범위 안에서의 최대값
	 * @return min~max 안에서의 랜덤값
	 */
	public int random(int min, int max) {
		int num = (int) ((Math.random() * (max - min + 1)) + min);
		return num;
	}

	/**
	 * 랜덤한 비밀번호를 생성하여 리턴한다.
	 * @return String
	 */
	public String getRandomPassword() {
		// 리턴할 문자열
		String password = "";

		// A~Z, a~z, 1~0 
		String words = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		// 글자길이
		int words_len = words.length();

		for (int i = 0; i < 8; i++) {
			// 랜덤한 위치에서 한 글자를 추출한다.
			int random = random(0, words_len - 1);
			String c = words.substring(random, random + 1);

			// 추출한 글자를 미리 준비한 변수에 추가한다.
			password += c;
		}

		return password;
	}
	
	public String getRandomPassword(int num) {
		// 리턴할 문자열
		String password = "";

		// A~Z, 1~0 소문자 뺀다. 소문자 넣고 싶으면 위처럼 다 적어넣으면 된다. 
		String words = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		// 글자길이
		int words_len = words.length();

		for (int i = 0; i < num; i++) {
			// 랜덤한 위치에서 한 글자를 추출한다.
			int random = random(0, words_len - 1);
			String c = words.substring(random, random + 1);

			// 추출한 글자를 미리 준비한 변수에 추가한다.
			password += c;
		}

		return password;
	}
	
	/**
	 * 값에 숫자가 포함되어 있는지 확인
	 * @param str
	 * @return
	 */
	public boolean checkNumContain(String str) {
		boolean result = false;
		if(str.matches(".*[0-9].*")) {
			result = true;
		}
		return result;
	}
	
	public boolean checkEngContain(String str) {
		boolean result = false;
		if(str.matches(".*[a-zA-Z].*")) {
			result = true;
		}
		return result;
	}
	
	/**
	 * 값이 숫자로만 이루어져있는지 판별하는 식
	 * @param str 판별하기 위한 문자열 값.
	 * @return
	 */
	public boolean checkNumberOnly(String str) {
		//문자열 양끝 공백 제거
		str = str.trim();
		if("".equals(str)) {
			//문자열 자체가 공백인지 검사
			return false;
		}
		
		for(int i=0; i<str.length(); i++) {
			char checkLetter = str.charAt(i);
			if(checkLetter<48||checkLetter>58) {
				//해당 checkLetter 값이 숫자가 아닐 경우
				return false;
			}
		}
		//위조건에 return false가 없으면 true 리턴
		return true;
	}
	
	/**
	 * 숫자만 추출해서 int로 변환
	 * @param str
	 * @return
	 */
	public int numExtract(String str) {
		int result = 0;
		if(!"".equals(str)&&str!=null) {
			String temp = str.replaceAll("[^0-9]", "");
			//숫자가 아예 아무것도 포함 안되어있는 경우를 상정 -> ""를 리턴
			if(temp!=null&&!"".equals(temp)) {
				result = Integer.parseInt(temp);
			}
		}
		return result;
	}
	
	/**
	 * 문자만 추출하기
	 * @param str
	 * @return
	 */
	public String strExtract(String str) {
		String result ="";
		if(!str.equals("")&&str!=null) {
			result = str.replaceAll("[0-9]", "");
		}
		return result;
	}
	
	
	public String makeStrLength(int len, String str, int i) {
		String result = null;
		String k = "";
		for(int j=0; j<len; j++) {
			result = str + k + i;
			if(result.length() == len) {
				break;
			}
			k += "0";
		}
		return result;
	}
	
	//권차기호에서 숫자만 뽑아내기.
	public String getNumVolumeCode(String vol) {
		//예전에 짠 코드
		/*String result = vol;
		vol = "";
		if(result != null && !"".equals(result)) {
			result = result.trim();
			if(result.indexOf(".")>=0) {
				result = result.substring(result.indexOf(".")+1);
			}
			
			for(int i=0; i<result.length(); i++) {
				int charNumCode = result.charAt(i);
				if((charNumCode>=48 && charNumCode<=57)||charNumCode==45) {
					vol += result.substring(i,i+1);
				}
			}
			
			while(!"".equals(vol)&&vol.substring(0,1).equals("0")) {
				vol = vol.substring(1);
			}
			result = vol;
		}
		return result;*/
		
		//정규식으로 짠 코드 숫자와 - 기호만 남기고 제거
		String result = vol;
		
		if(!"".equals(vol) && vol!=null) {
			result = vol.replaceAll("[^0-9,-]", "");
		}
		return result;
	}
	
	//복본기호에서 숫자만 뽑아내기
	public int getNumFromCopyCode(String copyCode) {
		int result = 0;
		if(copyCode.indexOf(".")>-1) {
			float floatCode = Float.parseFloat(copyCode);
			result = (int) floatCode;
		} else {
			result = numExtract(copyCode);
		}
		return result;
	}
	
	
	public String getFloatClsCode(String str) {
		String result = str;
		str = "";
		if(result!=null&&!"".equals(result)) {
			for(int i=0; i<result.length(); i++) {
				int charNumCode = result.charAt(i);
				if((charNumCode>=48&&charNumCode<=57)||charNumCode==46) {
					str += result.substring(i,i+1);
				}
			}
			result = str;
		}
		return result;
	}
	
	public String[][] stringTo2DArray(String dataListStr, int dataRow, int dataCol) {
		String[][] result = new String[dataRow][dataCol];
		
		StringBuffer dataListBuffer = new StringBuffer();
		int chkNum = 0;
		//"" 괄호 안에 , 콤마 체크를 위한 숫자.
		//하나하나 따서 ""이거 홀수개 일때, 콤마 제거
		String[] dataListStrComma = dataListStr.split("");
		for(int i=0; i<dataListStrComma.length; i++) {
			if(dataListStrComma[i].indexOf("\"")>-1) {
				chkNum++;
			}
			if(chkNum%2!=0) {
				dataListStrComma[i] = dataListStrComma[i].replace(",", " ");
			}
			dataListBuffer.append(dataListStrComma[i]);
		}
		dataListStr = dataListBuffer.toString();
		
		
		// 첫번째 가공 제일 바깥의 [] 괄호 떼기
		String dataList1 = dataListStr.substring(1, dataListStr.length()-1);
		// 두번째 쉼표 기준으로 값 다 나누기
		String[] dataList2 = dataList1.split(",");
		// 세번째 2차 배열에 넣어주기.
		for(int i=0; i<dataRow*dataCol; i++) {
			//값은 string이므로 ""안에 값이 존재한다. 아래와 같이 ""을 떼어준다.
			if(dataList2[i].indexOf("\"\"")<0) {
				dataList2[i] = dataList2[i].substring(dataList2[i].indexOf("\"")+1,dataList2[i].lastIndexOf("\""));
			} else {
				dataList2[i] = "";
			}
			result[i/dataCol][i%dataCol] = dataList2[i];
		}
		
		return result;
	}
	
	
	public String getUserIp() {
		String ip = null;
		HttpServletRequest request = 
		((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		ip = request.getHeader("X-Forwarded-For");
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("Proxy-Client-IP"); 
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("WL-Proxy-Client-IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("HTTP_CLIENT_IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("X-Real-IP"); 
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("X-RealIP"); 
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getRemoteAddr(); 
		}
		
		return ip;
	}
	
	//date 포멧이 어떤 형식인지 확인하는 함수, 아래에서 사용
	public boolean dateFormatChecker(String fromFormat, String FromStringDate) {
		boolean checker = false;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat);
			dateFormat.setLenient(false); // false 일 경우 엄격한 날짜 형식 검사
			if("EEE, d MMM yyyy HH:mm:ss Z".equals(fromFormat)) {
				//형식이 영문자를 포함하면, 로케일 넣어주어야한다.
				dateFormat = new SimpleDateFormat(fromFormat, Locale.ENGLISH);
				dateFormat.parse(FromStringDate);
				checker = true;
			} else {
				dateFormat.parse(FromStringDate);
				checker = true;
			}
			return checker;
		}catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * 날짜 형식 EEE, d MMM yyyy HH:mm:ss Z -> yyyy-MM-dd
	 * @param FromStringDate
	 * @return
	 */
	public String changeDateFormat1(String FromStringDate) {
		String result = null;
		
		if(FromStringDate != null) {
			SimpleDateFormat fromFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
			SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			try {
				Date itDate = fromFormat.parse(FromStringDate);
				result = toFormat.format(itDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/**
	 * 날짜 형식 yyyyMMdd -> yyyy-MM-dd
	 * @param FromStringDate
	 * @return
	 */
	public String changeDateFormat2(String FromStringDate) {
		String result = null;
		
		if(FromStringDate != null) {
			SimpleDateFormat fromFormat = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			try {
				Date itDate = fromFormat.parse(FromStringDate);
				result = toFormat.format(itDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * String 형태의 날짜를 넣으면 yyyy-MM-dd 형태로 날짜를 바꿔줌
	 * @param FromStringDate
	 * @return
	 */
	public String getDateValue(String FromStringDate) {
		String result = null;
		if(FromStringDate==null||"".equals(FromStringDate)||FromStringDate.length()<4) {
			//들어온 값이 null 이면 아무것도 하지 않음.
			//최소 4자리는되어야지
		} else {
			//값이 존재할 경우
			if(FromStringDate.matches(".*\\d.*")) {
				//입력값에 숫자가 포함된 경우에 진행. 반대경우엔 아무것도 하지 않는다.
				if(dateFormatChecker("EEE, d MMM yyyy HH:mm:ss Z", FromStringDate)) {
					//날짜 형식이 EEE, d MMM yyyy HH:mm:ss Z 이 형식일 경우
					result = changeDateFormat1(FromStringDate);
				} else if(dateFormatChecker("yyyyMMdd", FromStringDate)) {
					//날짜 형식이 yyyyMMdd 인 경우
					result = changeDateFormat2(FromStringDate);
				} else if(dateFormatChecker("yyyy-MM-dd HH:mm:ss", FromStringDate)) {
					// "yyyy-MM-dd hh:mm:ss"-> "yyyy-MM-dd HH:mm:ss"
					// hh 가 HH여아만 24시간제로 표시에 문제가 없다.
					result = getSqlDateToNormalDateStr(FromStringDate);
				} else if(dateFormatChecker("yyyy-MM-dd", FromStringDate)) {
					result = dateEveryTypeToNormalDateStr("yyyy-MM-dd", FromStringDate);
				}  else if(dateFormatChecker("dd-MMM-yyyy", FromStringDate)) {
					result = normalDateFormatter4(FromStringDate);
				} else if(FromStringDate.indexOf("-")>-1) {
					String tempDate = FromStringDate.substring(0,4);
					if(!isNumCheck(tempDate)) {
						return null;
					}
					//날짜 중간에 - 문자가 포함된 경우 예시) 2006-2007
					//앞에 날짜를 가져와서 2006으로 표기 날짜형식 맞춰주기 아래
					result = FromStringDate.substring(0,4)+"-01-01";
				} else {
					//위 조건 없이 이것저것이면
					result = String.valueOf(numExtract(FromStringDate));
					if(result.length()>3) {
						result = result.substring(0, 4)+"-01-01";
					} else {
						result = null;
					}
				}
			}
		}
		return result;
	}
	
	// 데이터 형식의 string 그대로 가져와서 한번 변환해서 내뱉기
	public String normalDateFormatter4(String fromDate) {
		String result = null;
		SimpleDateFormat fromFormat = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date itDate = fromFormat.parse(fromDate);
			result = toFormat.format(itDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 일반적인 mysql 날짜 형태(yyyy-MM-dd hh:mm:ss.SSS)를 yyyy-MM-dd로 바꿔주기
	 * @param sqlDate
	 * @return
	 */
	public String getSqlDateToNormalDateStr(String sqlDate) {
		String result = null;
		// 값이 없을 땐 그냥 null 리턴
		if(sqlDate == null || "".equals(sqlDate)) {
			return result;
		}
		
		try {
			SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
			SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date itDate = fromFormat.parse(sqlDate);
			result = toFormat.format(itDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String dateEveryTypeToNormalDateStr(String dateType, String dateString) {
		String result = null;
		// 값이 없을 땐 그냥 null 리턴
		if(dateString == null || "".equals(dateString)) {
			return result;
		}
		
		try {
			SimpleDateFormat fromFormat = new SimpleDateFormat(dateType);
			SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date itDate = fromFormat.parse(dateString);
			result = toFormat.format(itDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 도서인지 판별.
	 * @param str
	 * @return
	 */
	public String getBookOrNot(String str) {
		String result = "BOOK";
		if("도서".equals(str)) {
			result = "BOOK";
		} else if("음반".equals(str)) {
			result = "MUSIC";
		} else if("DVD".equals(str)) {
			result = "DVD";
		} else if("외국도서".equals(str)) {
			result = "FOREIGN";
		} else if("전자책".equals(str)) {
			result = "EBOOK";
		}
		return result;
	}
	
	/**
	 * print 컨트롤러에서 라벨코드에 따라서 url 연결해주기
	 * @param tagType
	 * @return
	 */
	public String getMovePageByLabel(int tagType) {
		String result = "book/print_tag/print_tag_a4_default";
		if(tagType == 1) {
			//사용안함.
			result = "opt1";
		} else if(tagType == 2) {
			//사용안함.
			result = "opt2";
		} else if(tagType == 3) {
			result = "book/print_tag/a4_barcode_32";
		} else if(tagType == 4) {
			result = "book/print_tag/a4_barcode_40";
		} else if(tagType == 5) {
			result = "book/print_tag/a4_code_30";
		} else if(tagType == 6) {
			result = "book/print_tag/a4_code_42";
		} else if(tagType == 7) {
			result = "book/print_tag/print_tag_a4_centerSpace_18";
		} else if(tagType == 10) {
			result = "book/print_tag/roll_default";
		} else if(tagType == 11) {
			result = "book/print_tag/roll_opt1";
		} else if(tagType ==12) {
			result = "book/print_tag/roll_opt2_blackAndWhite";
		} else if(tagType ==13) {
			result = "book/print_tag/roll_opt3";
		} else if(tagType ==14) {
			result = "book/print_tag/roll_opt4";
		} else if(tagType ==15) {
			result = "book/print_tag/roll_default_no_qrcode";
		} else if(tagType ==16) {
			result = "book/print_tag/roll_opt6_bookshelf_addicode";
		} else if(tagType ==17) {
			result = "book/print_tag/roll_opt7_blackAndWhite";
		} else if(tagType ==18) {
			result = "book/print_tag/roll_opt8_dsl_a";
		} else if(tagType ==19) {
			result = "book/print_tag/roll_opt9_dsl_b";
		} else if(tagType ==20) {
			result = "book/print_tag/roll_opt10";
		}
		return result;
	}
	
	/**
	 * qr코드가 필요한 라벨인지 체크하기
	 * 0, 10, 12번 라벨은 qr코드 호출
	 */
	public int qrCodeChecker(int tagType) {
		int result = 0;
		
		if(tagType==0 || tagType==7 || tagType==10 || tagType==12 || tagType == 17) {
			result = 1;
		}
		
		return result;
	}
	
	/**
	 * 도서상태 처리 함수 해당파라미터를 숫자로 변경.
	 * @param str 대출가능, 대출중, 폐기, 삭제예정
	 * @return
	 */
	public int getAvailableFromString(String str) {
		int result = 1;
		switch(str) {
		case "대출중":
			result = 0;
			break;
		case "폐기":
			result = 2;
			break;
		case "삭제예정":
			result = 3;
			break;
		}
		return result;
	}
	
	/**
	 * 문자열에서 숫자만 가져오는거. numExtract랑 완전 똑같음. 그냥 함수명만...
	 * @param str 1,123 이런식으로 콤마가 포함되어있는 경우가 많다.
	 * @return
	 */
	public int getPageFromString(String str) {
		int result = 0;
		//위에 숫자만 추출하는 함수 사용.
		result = numExtract(str);
		return result;
	}
	
	/**
	 * 도서수입구분, 구입/기증을 문자열 파라미터를 숫자로 바꿔주는 기능
	 * @param str 구입일 경우 1(default), 기증일 경우 0
	 * @return
	 */
	public int getPurOrDon(String str) {
		int result = 1;
		switch(str) {
		case "기증":
			result = 0;
			break;
		}
		return result;
	}
	
	/**
	 * 저자가 null 이나 공백일 경우
	 * @param str
	 * @return
	 */
	public String getAuthorNameFromBlank(String str) {
		str = str.trim();
		String result = str;
		if(str==null||"".equals(str)) {
			result = "저자 미상";
		}
		return result;
	}
	
	
	/**
	 * 하나의 셀로 합쳐져있는 청구기호에서 각 값 분리하기
	 * @param str
	 * @return
	 */
	public String[] getEachCodeFromCallCode(String str) {
		if(str==null||"".equals(str)) {
			return null;
		}
		//좌우공백 삭제
		str = str.trim();
		//공백으로 청구기호 값 나누기.
		String[] callCodeArr = str.split("\\s+");
		//최종 가공된 값 들어갈 배열 공간
		String[] result = new String[5];
		//복본기호는 기본값 0으로 초기화
		result[4] = "0";
		
		//별치기호를 위한 스위치
		int isAddiCode = 0;
		
		for(int j=0; j<callCodeArr.length; j++) {
			//내용이 null이거나 공백이면 바로위 for문 끝내기.
			if(callCodeArr[j]==null||"".equals(callCodeArr[j])) {
				break;
			}
			
			if(!isNumCheck(callCodeArr[j])&&isAddiCode==0) {
				//별치기호 처리 숫자가 아니면 우선 별치기호
				//별치기호가 맞다면 스위치 on 
				isAddiCode++;
				result[0] = callCodeArr[j];
			} else if(isNumCheck(callCodeArr[j])) {
				//분류기호처리
				result[1] = callCodeArr[j];
				//분류기호를 지났다면 별치기호가 아니다.
				isAddiCode++;
			} else if(volPatternCheck(callCodeArr[j])!=null) {
				//권차기호
				result[3] = volPatternCheck(callCodeArr[j]);
			} else if(copyPatternCheck(callCodeArr[j])!=null) {
				//복본기호
				result[4] = copyPatternCheck(callCodeArr[j]);
			} else if(!isNumCheck(callCodeArr[j])){
				//저자기호
				result[2] = callCodeArr[j];
			}
		}
		
		return result;
	}
	
	/**
	 * 권차기호 패턴인지 판별, 권차기호 패턴이면 값을 내보내고, 아니면 null 리턴
	 * @param str
	 * @return
	 */
	public String volPatternCheck(String str) {
		String result = null;
		
		if(str!=null&&!"".equals(str)) {
			String[] volInit = {"v.", "V."};
			
			for(int i=0; i<volInit.length; i++) {
				if(str.indexOf(volInit[i])==0) {
					result = str.replace(volInit[i], "");
					break;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 복본기호 패턴인지 판별, 복본기호 패턴이면 값리턴, 아니면 null리턴
	 * @param s
	 * @return
	 */
	public String copyPatternCheck(String s) {
		String result = null;
		
		if(s!=null&&!"".equals(s)) {
			String[] copyInit = {"c.", "C."};
			
			for(int i=0; i<copyInit.length; i++) {
				if(s.indexOf(copyInit[i])==0) {
					result = s.replace(copyInit[i], "");
					break;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 숫자이면 true(실수포함), 문자이면 false 리턴
	 * @param str
	 * @return
	 */
	public boolean isNumCheck(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	//txtExtractValue 함수는 종속성(Autowired) 사용 때문에
	//controller에 FrequentlyFunction로 함수 옮김.
	
	
	/**
	 * 엑셀 경로 들어오면 처리 함수
	 * @param filePath , checker =0 이면 결과값 10개까지만, 1이면 전체 다~
	 * @return
	 * @throws IOException 
	 */
	public String[][] excelExtractValues(String filePath, int checker) throws IOException {
		String[][] result = null;
		if(filePath == null || "".equals(filePath)) {
			return null;
		}
		
		FileInputStream fileStream = new FileInputStream(filePath);
		
		//초기에 데이터 몇개까지만 보일건지 정함.
		int seeRow = 2;
		
		//확장자 구분 xlsx xls
		int extPos = filePath.lastIndexOf(".");
		String ext = filePath.substring(extPos+1);
		
		if("xlsx".equals(ext)) {
			//엑셀 xlsx 일때
			XSSFWorkbook workbook = new XSSFWorkbook(fileStream);
			
			XSSFSheet curSheet = workbook.getSheetAt(0);
			int row_crt = curSheet.getPhysicalNumberOfRows();

			//최초 행의 마지막 셀 수 = 마지막 컬럼(수평) 번호
			int lastCellCount = curSheet.getRow(0).getPhysicalNumberOfCells();
			
			if(checker == 0) {
				//check 페이지, 모든 내용을 다 보여줄 필요는 없다.
				//최종 행의 개수가 10개가 넘는다면 보여지는 개수를 10개로
				if(row_crt > 30) {
					seeRow = 30;
				} else {
					seeRow = row_crt;
				}
			} else {
				//실제 등록이 필요할 때라면 모든 값 다 넣어야된다.
				seeRow = row_crt;
			}
			
			//비고처리를 위한 컬럼 하나 더 늘리기
			result = new String[seeRow][lastCellCount+1];
			
			for (int j=0; j<seeRow; j++) {
				//i번 행을 읽어온다.
				XSSFRow horizonRow = curSheet.getRow(j);
				if(horizonRow != null) {
					for(int k=0; k<lastCellCount; k++) {
						if("null".equals(String.valueOf(horizonRow.getCell(k)))||"".equals(String.valueOf(horizonRow.getCell(k)))) {
							//우선 배열값에 null처리하고
							result[j][k] = null;
							//합병된 셀인지 체크
							String isMerged = isMergedCell(curSheet,j,k);
							
							if(isMerged!=null) {
								if("col".equals(isMerged)) {
									//바로 위에 값 대입
									result[j][k] = result[j-1][k];
								}
							}
						} else {
							result[j][k] = String.valueOf(horizonRow.getCell(k));
						}
						/*logger.info("colArr["+j+"]["+k+"]의 값 = "+theArr[j][k]);*/
					}
				}
				//logger.info(theArr[j][9]);
			}
			//엑셀 처리 후엔 반드시 파일을 닫아줘야한다.
			workbook.close();
		} else if("xls".equals(ext)) {
			//엑셀 xls 일때
			HSSFWorkbook workbook = new HSSFWorkbook(fileStream);
			
			HSSFSheet curSheet = workbook.getSheetAt(0);
			int row_crt = curSheet.getPhysicalNumberOfRows();
			//최초 행의 마지막 셀 수 = 마지막 컬럼(수평) 번호
			int lastCellCount = curSheet.getRow(0).getPhysicalNumberOfCells();
			
			//최종 행의 개수가 10개가 넘는다면 보여지는 개수를 10개로
			if(row_crt > 10) {
				seeRow = 10;
			} else {
				seeRow = row_crt;
			}
			//비고처리를 위한 컬럼 하나 더 늘리기
			result = new String[seeRow][lastCellCount+1];
			
			for (int j=0; j<seeRow; j++) {
				//i번 행을 읽어온다.
				HSSFRow horizonRow = curSheet.getRow(j);
				if(horizonRow != null) {
					for(int k=0; k<lastCellCount; k++) {
						if("".equals(String.valueOf(horizonRow.getCell(k)))) {
//							String[] isMerged = isMergedXls(curSheet,j,k);
							result[j][k] = null;
						} else {
							result[j][k] = String.valueOf(horizonRow.getCell(k));
						}
						/*logger.info("colArr["+j+"]["+k+"]의 값 = "+theArr[j][k]);*/
					}
				}
				//logger.info(theArr[j][9]);
			}
			
			//엑셀 처리 후엔 반드시 파일을 닫아줘야한다.
			workbook.close();
		}
		
		return result;
	}
	
	/**
	 * excel 파일에서 컬럼(열) 개수 구하기 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public int[] getExcelLastCellCount(String filePath) throws IOException {
		int[] result = new int[2];
		if(filePath == null) {
			return null;
		}
		
		FileInputStream fileStream = new FileInputStream(filePath);
		
		//확장자 구분 xlsx xls
		int extPos = filePath.lastIndexOf(".");
		String ext = filePath.substring(extPos+1);
		
		if("xlsx".equals(ext)) {
			//엑셀 xlsx 일때
			XSSFWorkbook workbook = new XSSFWorkbook(fileStream);
			
			XSSFSheet curSheet = workbook.getSheetAt(0);
			//int row_crt = curSheet.getPhysicalNumberOfRows();

			//마지막 행의 개수 row
			int lastRowCount = curSheet.getPhysicalNumberOfRows();
			//최초 행의 마지막 셀 수 = 마지막 컬럼(수평) 번호
			int lastColCount = curSheet.getRow(0).getPhysicalNumberOfCells();
			
			result[0] = lastRowCount;
			result[1] = lastColCount;
			workbook.close();
		} else if("xls".equals(ext)) {
			//엑셀 xls 일때
			HSSFWorkbook workbook = new HSSFWorkbook(fileStream);
			
			HSSFSheet curSheet = workbook.getSheetAt(0);
			//int row_crt = curSheet.getPhysicalNumberOfRows();
			//마지막 행의 개수 row
			int lastRowCount = curSheet.getPhysicalNumberOfRows();
			//최초 행의 마지막 셀 수 = 마지막 컬럼(수평) 번호
			int lastColCount = curSheet.getRow(0).getPhysicalNumberOfCells();
			
			result[0] = lastRowCount;
			result[1] = lastColCount;
			//엑셀 처리 후엔 반드시 파일을 닫아줘야한다.
			workbook.close();
		}
		
		fileStream.close();
		
		return result;
	}
	
	/**
	 * isbn 숫자만 가져오기.
	 * @param str
	 * @return
	 */
	public String getISBNNumOnly(String str) {
		String result = null;
		if(str == null || "".equals(str)) {
			return null;
		}
		
		str = str.replaceAll(" ", "");
		
		if(str.contains("-")) {
			str = str.replaceAll("-", "");
		}
		
		if(str.contains(".")&&(str.contains("E")||str.contains("e"))) {
			str = str.trim();
			str = str.replace(".", "");
			int ePosition = 0;
			if(str.contains("E")) {
				ePosition = str.indexOf("E");
			} else {
				ePosition = str.indexOf("e");
			}
			str = str.substring(0,ePosition);
			while(str.length()<13) {
				str += 0;
			}
		}
		
		//숫자 제외 다 없애기.
		str = str.replaceAll("[^0-9]", "");
		
		result = str;
		
		return result;
	}
	
	/**
	 * arr를 엑셀파일로 변환
	 * @param arr, fileDir 파일 경로 파일명포함
	 * @return
	 * @throws FileNotFoundException 
	 */
	public void writerExcelFile(String[][] arr, String fileDir) throws Exception {
		if(arr == null) {
			return;
		}
		
		//파일명 수정
		int extPos = fileDir.lastIndexOf(".");
		String ext = fileDir.substring(extPos+1);
		fileDir = fileDir.substring(0,extPos);
		fileDir = fileDir+"_mod";
		fileDir = fileDir+"."+ext;
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet newSheet = workbook.createSheet("isbn처리");
		
		XSSFRow newRow = null;
		XSSFCell newCell = null;
		
		for(int i=0; i<arr.length; i++) {
			newRow = newSheet.createRow(i);
			for(int j=0; j<arr[i].length; j++) {
				newCell = newRow.createCell(j);
				newCell.setCellValue(arr[i][j]);
			}
		}
		
		FileOutputStream fos = new FileOutputStream(fileDir);
		workbook.write(fos);
		workbook.close();
		fos.close();
		
	}
	
	/**
	 * 해당 셀이 병합되어있는지 체크 하기.
	 * @param sheet
	 * @param row
	 * @param col
	 * @return
	 */
	public String isMergedCell(XSSFSheet sheet, int row, int col) {
		
		for(int i=0; i<sheet.getNumMergedRegions(); i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			if(row >= range.getFirstRow()&&row<=range.getLastRow()
				&& col>=range.getFirstColumn() && col <= range.getLastColumn()) {
				String result = null;
				if(range.getFirstRow()!=range.getLastRow()) {
					//다른 행번호가 있다면, 위아래로 합친거. 즉 세로로 병합. col이라고 하자.
					result = "col";
				} else if(range.getFirstColumn()!=range.getLastColumn()) {
					//다른 열 번호가 있다면, 좌우로 합친거. 가로로 병합 
					result = "row";
				}
				return result;
			}
		}
		return null;
	}
	
	/**
	 * 해당 셀이 병합되어있는지 체크 xls 버전
	 * @param sheet
	 * @param row
	 * @param col
	 * @return
	 */
	public String[] isMergedXls(HSSFSheet sheet, int row, int col) {
		//
		for(int i=0; i<sheet.getNumMergedRegions(); i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			if(row >= range.getFirstRow()&&row<=range.getLastRow()
				&& col>=range.getFirstColumn() && col <= range.getLastColumn()) {
				String[] result = new String[2];
				result[0]="true";
				if(range.getFirstRow()!=range.getLastRow()) {
					//다른 행번호가 있다면, 위아래로 합친거. 즉 세로로 병합. col이라고 하자.
					result[1] = "col";
				} else if(range.getFirstColumn()!=range.getLastColumn()) {
					//다른 열 번호가 있다면, 좌우로 합친거. 가로로 병합 
					result[1] = "row";
				}
				return result;
			}
		}
		return null;
	}
	
	
	public boolean hasTwoOrMoreDots(String str) {
		// true 면 점이 2개 이상.
		if("".equals(str) || str == null) {
			return false;
		}
		int firstIndex = str.indexOf(".");
		if(firstIndex < 0) {
			return false;
		}
		String nextStr = str.substring(firstIndex+1);
		int secondIndex = nextStr.indexOf(".");
		return secondIndex >= 0 ;
	}
	
	/**
	 * 날짜 오늘인지 판별하는 함수
	 * @param dateString
	 * @return
	 */
	public boolean isToday(String dateString, String targetDay) {
		if (dateString == null) {
            return false;
        }
		
		boolean result = false;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			Date date1 = dateFormatter.parse(targetDay);
			Date date2 = dateFormatter.parse(dateString);
			
			if(date1.equals(date2)) {
				result = true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return result;
	}
	
	public String isBlankToNull(String str) {
		String result = null;
		result = (str != null && str.trim().isEmpty()) ? null : str;
		return result;
	}
	
	public int isBlankToZero(String str) {
		int result = 0;
		result = (str != null && str.trim().isEmpty()) ? 0 : Integer.parseInt(str);
		return result;
	}
}
