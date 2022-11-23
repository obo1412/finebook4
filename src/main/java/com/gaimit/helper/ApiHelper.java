package com.gaimit.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ApiHelper {
	//알라딘 과거 인증키
	/*String apiKey = "?ttbkey=ttbanfyanfy991303001";*/
	
	//2020.06.10 latest version key
	private String aladinTtbKey = "ttblib1207001";
	private String aladinSearchVersion = "20131101";
	//다른 클래스에서도 사용하기 위해 private에서 public으로 바꿈.
	public String NLKcertKey = "6debf14330e5866f7c50d47a9c84ae8f";

	//알라딘 api, isbn 검색
	public String getAladinJsonIsbnResult(String isbn) {
		String result = null;
		
		String apiUrl = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey="
				+ aladinTtbKey +"&output=js&Version=20131101&OptResult=ebookList,usedList,reviewList";
		if(isbn.length() == 13) {
			result = apiUrl+"&itemIdType=ISBN13"+"&ItemId="+ isbn;
		} else if(isbn.length() == 10) {
			result = apiUrl+"&itemIdType=ISBN"+"&ItemId="+ isbn;
		}
		
		return result;
	}
	
	//알라딘 api, 도서명과 저자로 검색.
	public JSONObject getAladinJsonResultTitleAndAuthor(String title, String author) throws Exception {
		JSONObject result = null;
		
		String apiUrl = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey="
				+ aladinTtbKey +"&QueryType=keyword&MaxResults=10&start=1&SearchTarget=Book&output=js"
				+ "&Version="+ aladinSearchVersion
				+ "&Query="+title+author;
		
		
		URL url = new URL(apiUrl);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		//System.out.println("["+searchEngine+"] 응답코드: "+con.getResponseCode()); // 응답코드 리턴 200번대 404 등등
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
		String brResult = br.readLine();
		br.close();

		JSONParser jsonParser = new JSONParser();
		result = (JSONObject) jsonParser.parse(brResult);
		
		return result;
	}
	
	//국중 api, 도서명과 저자로 검색.
	public JSONObject getNLJsonResultTitleAndAuthor(String title, String author) throws Exception {
		JSONObject result = null;
		
		String apiUrl = "https://www.nl.go.kr/NL/search/openApi/search.do?key="
		+ NLKcertKey + "&apiType=xml&srchTarget=total&pageSize=10&pageNum=1&sort=&category=%EB%8F%84%EC%84%9C"
		+ "&detailSearch=true&f1=title&f2=author&v1="+ title+"&v2="+author;
		
		
		URL url = new URL(apiUrl);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		//System.out.println("["+searchEngine+"] 응답코드: "+con.getResponseCode()); // 응답코드 리턴 200번대 404 등등
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
		String brResult = br.readLine();
		br.close();

		JSONParser jsonParser = new JSONParser();
		result = (JSONObject) jsonParser.parse(brResult);
		
		return result;
	}
	
	
	public String getSeojiJsonIsbnResult(String isbn) {
		String result = null;
		
		//숫자부분만 추출하기 위함.
		if(Util.getInstance().checkEngContain(isbn)) {
			isbn = isbn.substring(1);
			//System.out.println(isbn);
		}
		
		String apiUrl = "http://seoji.nl.go.kr/landingPage/SearchApi.do?cert_key="
				+ NLKcertKey + "&result_style=json&page_no=1&page_size=1";
		if(isbn.length() == 13) {
			result = apiUrl + "&isbn="+ isbn;
		} else {
			result = apiUrl +"&isbn="+ isbn;
		}
		
		return result;
	}
	
	//xml 리턴 타입
	public String getNlXmlIsbnResult(String isbn) {
		String result = null;
		//숫자부분만 추출하기 위함.
		if(Util.getInstance().checkEngContain(isbn)) {
			isbn = isbn.substring(1);
			//System.out.println(isbn);
		}
		
		String apiUrl = "https://www.nl.go.kr/NL/search/openApi/search.do?key="
		+ NLKcertKey + "&apiType=xml&pageSize=10&pageNum=1&detailSearch=true&isbnOp=isbn";
		if(isbn.length() == 13) {
			result = apiUrl + "&isbnCode="+ isbn;
		} else {
			result = apiUrl +"&isbnCode="+ isbn;
		}
		
		return result;
	}
	
	//국중 json 리턴 타입
	public String getNlJsonIsbnResult(String isbn) {
		String result = null;
		//숫자부분만 추출하기 위함.
		if(Util.getInstance().checkEngContain(isbn)) {
			isbn = isbn.substring(1);
			//System.out.println(isbn);
		}
		
		String apiUrl = "https://www.nl.go.kr/NL/search/openApi/search.do?key="
		+ NLKcertKey + "&apiType=json&pageSize=10&pageNum=1&detailSearch=true&isbnOp=isbn";
		if(isbn.length() == 13) {
			result = apiUrl + "&isbnCode="+ isbn;
		} else {
			result = apiUrl +"&isbnCode="+ isbn;
		}
		
		return result;
	}
	
	//SearchBook에 /book/search_nl_book.do 부분에 아직 수정이 안되었다.
	//현재 이 기능은 브라우저에선 결과값을 주지만,
	//코드 상태로는 서버 응답값이 502 에러가 발생한다. 나중에 처리하자
	public String getNlXmlTypeResult(int type, String keyword1, String keyword2, int nowPage) {
		String resultApi = null;
		String encode_result = null;
		String encode_result2 = null;
		try {
			encode_result = URLEncoder.encode(keyword1,"utf-8");
			if(keyword2 != null) {
				encode_result2 = URLEncoder.encode(keyword2,"utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		switch(type) {
		case 1:
			//type 1 isbn 검색
			resultApi ="https://www.nl.go.kr/NL/search/openApi/search.do?key="
				+ NLKcertKey + "&apiType=xml&srchTarget=total&pageSize=10&pageNum="+nowPage+"&sort=&category=%EB%8F%84%EC%84%9C"
				+ "&detailSearch=true&isbnOp=isbn&isbnCode="+ encode_result;
			break;
		case 2:
			//type 2 title 단일 검색
			resultApi ="https://www.nl.go.kr/NL/search/openApi/search.do?key="
				+ NLKcertKey + "&apiType=xml&srchTarget=total&pageSize=10&pageNum="+nowPage+"&sort="
				+ "&detailSearch=true&f1=title&v1="+ encode_result;
			break;
		case 3:
			//type 3 author 저자 단일 검색
			resultApi ="https://www.nl.go.kr/NL/search/openApi/search.do?key="
				+ NLKcertKey + "&apiType=xml&srchTarget=total&pageSize=10&pageNum="+nowPage+"&sort=&category=%EB%8F%84%EC%84%9C"
				+ "&detailSearch=true&f1=author&v1="+ encode_result;
			break;
		case 4:
			//type 4 title author 키워드 두가지 검색
			resultApi ="https://www.nl.go.kr/NL/search/openApi/search.do?key="
				+ NLKcertKey + "&apiType=xml&srchTarget=total&pageSize=10&pageNum="+nowPage+"&sort=&category=%EB%8F%84%EC%84%9C"
				+ "&detailSearch=true&f1=title&f2=author&v1="+ encode_result+"&v2="+encode_result2;
			break;
		}
		
		
		return resultApi;
	}
	
	/**
	 * url 대입하면 json 오브젝트로 결과값 리턴해주도록.
	 * @param isbnUrl, type 0알라딘 1국중 2서지
	 * @return
	 * @throws Exception
	 */
	public JSONObject getJsonApiResult(String isbn, int type) throws Exception {
		JSONObject result = null;
		if(isbn == null) {
			return null;
		}
		
		String isbnUrl = null;
		
		switch(type) {
			case 0:
				isbnUrl = getAladinJsonIsbnResult(isbn);
				break;
			case 1:
				isbnUrl = getNlJsonIsbnResult(isbn);
				break;
			case 2:
				isbnUrl = getSeojiJsonIsbnResult(isbn);
				break;
			
		}
		
		URL url = new URL(isbnUrl);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		/*String searchEngine = "알라딘";
		if(type==1) {
			searchEngine = "국중";
		} else if(type==2) {
			searchEngine = "서지";
		}*/
		//System.out.println("["+searchEngine+"] 응답코드: "+con.getResponseCode()); // 응답코드 리턴 200번대 404 등등
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
		String brResult = br.readLine();
		br.close();

		JSONParser jsonParser = new JSONParser();
		result = (JSONObject) jsonParser.parse(brResult);
		
		return result;
	}
	
	
}
