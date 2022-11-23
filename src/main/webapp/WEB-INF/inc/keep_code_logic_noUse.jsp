<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true"%>

<!-- 아래기능은, ajax 기본 호출-->
	var name = null;
	var urlName = null;
	name = $("#search-name").val();
	urlName = '${pageContext.request.contextPath}/member/member_list.do?name='+name;
	if(CountMember > 1){
		window.open(urlName, 'window', 'width=300', 'height=300');
		
		$.ajax({
			url: "${pageContext.request.contextPath}/member/member_list.do?name="+name,
			type: 'get',
			data: {
			},
			dataType: 'html',
			success: function() {
				pop.location;
			}
		});
		
	} else if(CountMember == 0) {
		$('#search-state').html("회원 정보를 찾을 수 없습니다.");
	}
	console.log(name);



<!-- ajax를 이용하여 openapi로 데이터를 보내고 json 형태로 값을 받아옴 -->
		$("#btn-search-bookinfo").click(function(e) {
			e.preventDefault();
		
		var bKeyword = $("#search-book-info").val();
		var Parms = '&ttbkey=${ttbKey}';
		var ttb;
		
		
		$.get("http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?itemIdType=ISBN13&output=js&Version=20131101"
				+"&ItemId="+bKeyword +'&ttbkey=[승인key]',
			 function(json) {
			$("#isbn13").val(json.item[0].isbn13);
			$("#isbn10").val(json.item[0].isbn);
			$("#bookCateg").val(json.item[0].categoryName);
			$("#categCode").val(json.item[0].categoryId);
			$("#bookName").val(json.item[0].title);
			$("#author").val(json.item[0].author);
			$("#authorCode").val();
			$("#publisher").val(json.item[0].publisher);
			$("#pubDate").val(json.item[0].pubDate);
			$("#bookDesc").val(json.item[0].description);
			$("#bookCover").attr("src", jsonAladin.item[0].cover);
			
		});
		
		});
		
		
		<!-- list로 받은 데이터가 순차적으로 등록되는데,
					이 데이터 값 하나하나를 우리가 인덱싱을 못해서 사용을 못함.
					그래서 강제로 하나하나에 인덱스를 주는 역할. -->
		 var CountMember = ${CountMember};
		 console.log(CountMember);
		 if (CountMember == 0) {
		 $('#search-state').html("회원 정보를 찾을 수 없습니다.");
		 }
		
		 $(".pick-user").on("click", function(e){
		 var x = $(this).attr('id');
		 console.log(x);
		
		 var PIdList = [];
		 var PnameList = [];
		 var PidCodeList = [];
		 var PphoneList = [];
		 var PlevelList = [];

		 <c:forEach var="item" items='${list}'>
		 PIdList.push("${item.id}");
		 PnameList.push("${item.name}");
		 PidCodeList.push("${item.idCode}");
		 PphoneList.push("${item.phone}");
		 PlevelList.push("${item.level}");
		 </c:forEach>
		
		 $("#memberId").val(PIdList[x]);
		 $("#name").val(PnameList[x]);
		 $("#idCode").val(PidCodeList[x]);
		 $("#phone").val(PphoneList[x]);
		 $("#level").val(PlevelList[x]);
		 e.preventDefault();
		 });
		 
		 <!-- 위와 동 -->
		 /*
		 * 아래기능은 회원검색했을때, 회원선택버튼 누르면, input칸에 채우기
		 */
		$(".pick-user").on("click", function(e) {
			var x = $(this).attr('id');
			console.log(x);

			var PIdList = [];
			var PnameList = [];
			var PphoneList = [];
			var PbrwLimitList = [];
			var PdateLimitList = [];

			<c:forEach var="item" items='${list}'>
			PIdList.push("${item.id}");
			PnameList.push("${item.name}");
			PphoneList.push("${item.phone}");
			PbrwLimitList.push("${item.brwLimit}");
			PdateLimitList.push("${item.dateLimit}");
			</c:forEach>

			$("#memberId").val(PIdList[x]);
			$("#name").val(PnameList[x]);
			$("#phone").val(PphoneList[x]);
			$("#brwLimit").val(PbrwLimitList[x]);
			$("#dateLimit").val(PdateLimitList[x]);
			e.preventDefault();
			
			/* pick-user 버튼을 누르면 도서바코드로 포커싱 */
			document.getElementById('barcodeBook').focus();
		});
		
		<!-- 버튼 클릭시, 항목에 인덱싱해주고, 그 입력된 버튼에 자리를
					찾아가게 하는 ajax -->
			$(".return-book").on("click", function(e){
	    var x = $(this).attr('id');
	    /* console.log(x); */
	    
	    var PIdCodeBookList = [];
	    var PIdBrwList = [];
	    var curEndDateBrw = [];

	    <c:forEach var="item" items='${brwList}'>
	    	PIdCodeBookList.push("${item.idCodeBook}");
	    	PIdBrwList.push("${item.idBrw}")
	    </c:forEach>
	    
	    var curIdCodeBook = PIdCodeBookList[x];
	    var curIdBrw = PIdBrwList[x];
	    
	    $.get("${pageContext.request.contextPath}/book/return_book_ok.do",{
	    	id_code_book : curIdCodeBook,
	    	id_brw : curIdBrw
	    }, function(json) {
	 		curEndDateBrw.push(json.rtndItem.endDateBrw); 
	 		console.log(curEndDateBrw[x]);
	    	$(".endDateBox").append("<p>"+curEndDateBrw[x]+"</p>");
	    });
	    
	    
	    /* console.log(PIdCodeBookList[x]) */
	    e.preventDefault();
	});
		
		
				<!-- return-book 버튼이 입력되면, 해당 barcode의 도서가 반납처리
							ajax로 구현 시도 아직 이해 못함. -->
		$(document).on('click', '.return-book', function(){
			var rtnBarcode = $(this).val();
			$.post("${pageContext.request.contextPath}/book/return_book_ok.do",
					{barcodeBookRtn: rtnBarcode},
					function(req){
						$("#calltest").val(req.a);
					});
		});
		
		<!-- 반납결과 내용 출력 템플릿 -->
	<script id="return_book_tmpl" type="text/x-handlebars-template">
	</script>
	
	<!-- ajax 호출하여 handlebars 템플릿에 넣는 대략적인 과정 실행해보지는 않았음. -->
	$(document).on('click', '.testbtn', function(){
			var rtnBarcode = 3;
			$.post("${pageContext.request.contextPath}/book/return_book_ok_ajax.do",
					{
						barcodeBookRtn: 'BZ000002',
					},
					function(req){
						$('#calltest').html(req.jsonBrwRmnList);
						<!-- return_book_tmpl을 로드하여 변수 template에 주입 -->
						var template = Handlebars.compile($("#return_book_tmpl").html());
						<!-- 결과값 jsonBrwRmnList와 template 매칭,변수 html에 주입. -->
						var html = template(req.jsonBrwRmnList);
						<!-- 결합된 결과를 #result 박스나 뭐 어딘가의 공간에 주입. -->
						$("#result").append(html);
					});
		});



<!-- xml데이터를 json으로 Map을 이용해서 파싱해보려고 시도했으나,
	몇가지 문제에 부딪혀 성공하지는 못했음. hm과 nlLists 에 데이터를 넣기는 했으나,
	Map 특성상 Map<key, value>에서 key 중복이 안되므로 여러 key 값의 put이 안된다.-->
	<!-- 	String isbn = web.getString("search-book-info");
		
		String NLKcertKey = "6debf14330e5866f7c50d47a9c84ae8f";
		//국립중앙도서관 아래 api검색
		//http://www.nl.go.kr/app/nl/search/openApi/search.jsp?key=6debf14330e5866f7c50d47a9c84ae8f&category=dan&detailSearch=true&isbnOp=isbn&isbnCode=8984993727
		// 국중은 openapi가 xml 형태밖에 없는 듯하여 xml 호출 구조
		ArrayList<Object> xmlArray = new ArrayList<Object>();
		ArrayList<String> classNoArray = new ArrayList<String>();
		ArrayList<String> titleArray = new ArrayList<String>();
		ArrayList<String> authorArray = new ArrayList<String>();
		ArrayList<String> pubArray = new ArrayList<String>();
		
		JSONArray nlList = new JSONArray();
		JSONObject nlJson = new JSONObject();
		
		Map<String, String> hm = new HashMap<String, String>();
		Map<String, Object> nlLists = new HashMap<String, Object>();
		
		try {
			String apiUrl = "http://www.nl.go.kr/app/nl/search/openApi/search.jsp?key="+NLKcertKey+"&category=dan&detailSearch=true&isbnOp=isbn";
			String apiUrlFull = null;
			if(isbn.length() == 13) {
				apiUrlFull = apiUrl + "&isbnCode="+ isbn;
			} else if(isbn.length() == 10) {
				apiUrlFull = apiUrl +"&isbnCode="+ isbn;
			}
			URL url = new URL(apiUrlFull);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.getResponseCode(); // 응답코드 리턴 200번대 404 등등
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc =builder.parse(con.getInputStream());
			
			// xml 데이터를 string으로 
			String jsonNl = null;
			org.json.JSONObject jsonob = null;
			
			//xml 데이터를 string 으로
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString();
			
			jsonob = XML.toJSONObject(output);
			jsonNl = jsonob.toString(4);
			// xml 데이터를 string으로
			
			NodeList nodeList = doc.getElementsByTagName("item");
			for(int i =0; i<nodeList.getLength(); i++) {
				for(Node node = nodeList.item(i).getFirstChild(); node!=null;
					node=node.getNextSibling()) {
					if(node.getNodeName().equals("class_no")) {
						classNoArray.add(node.getTextContent());
						hm.put("class_no", node.getTextContent());
					}
					if(node.getNodeName().equals("title_info")) {
						titleArray.add(node.getTextContent());
						hm.put("title_info", node.getTextContent());
					}
					if(node.getNodeName().equals("author_info")) {
						authorArray.add(node.getTextContent());
						hm.put("author_info", node.getTextContent());
					}
					if(node.getNodeName().equals("pub_info")) {
						pubArray.add(node.getTextContent());
						hm.put("pub_info", node.getTextContent());
					}
					nlLists.put("item"+i, hm);
					xmlArray.add(hm);
				}
			}
			
			
			xmlArray.add(classNoArray);
			xmlArray.add(titleArray);
			xmlArray.add(authorArray);
			xmlArray.add(pubArray); -->

	.input[type=text] {
		-webkit-ime-mode: active;
		-moz-ime-mode: active;
		-ms-ime-mode: active;
		ime-mode: active;
	}
	
	.korean-first {
		-webkit-ime-mode: active;
		-moz-ime-mode: active;
		-ms-ime-mode: active;
		ime-mode: active;
	}

	.english-first {
		-webkit-ime-mode: inactive;
		-moz-ime-mode: inactive;
		-ms-ime-mode: inactive;
		ime-mode: inactive;
	}

	.only-english {
		/* 아이디와 비밀번호에는 이것을 적용시켜서 혼란이 없도록. */
		-webkit-ime-mode: disabled;
		-moz-ime-mode: disabled;
		-ms-ime-mode: disabled;
		ime-mode: disabled;
	}
	
	
	url을 받아와서 url의 파라미터를 추출하는 메서드
	<!-- function getUrlParam(sname) {
			var url = location.search;
			var params = url.substr(url.indexOf("?")+1);
			var sval = "";
			params = params.split("&");
			for(var i=0; i<params.length; i++){
				temp=params[i].split("=");
				if(temp[0]==sname) {
					sval=temp[1];
				}
				return sval;
			}
		}; -->
		

											<c:if test="${firStat.index gt 0}">
												<tr>
													<td style="text-align:center;">${dataList[firStat.index][0]}</td>
													<td style="text-align:center;">${dataList[firStat.index][1]}</td>
													<td>
														${dataList[firStat.index][2]}/${dataList[firStat.index][3]}<br>
														${dataList[firStat.index][4]}/${dataList[firStat.index][5]}
													</td>
													<td>
														${dataList[firStat.index][6]}/${dataList[firStat.index][7]}<br>
														${dataList[firStat.index][8]}
													</td>
														<!-- 아래 대출/반납날짜 글자->날짜 다시 날짜->글자로 파싱 -->
														<fmt:parseDate var="brwDateStr" value="${dataList[firStat.index][9]}" pattern="yyyy-MM-dd" />
														<!-- DB에서 가져온 String형 데이터를 Date로 변환 var 변수명, pattern 날짜형식, value 컨트롤러에서 받은값 var에 저장 -->
														<fmt:formatDate var="brwDateFmt" value="${brwDateStr}" pattern="yyyy-MM-dd" />
														<!-- Date형으로 저장된 값을 String으로 변환 var변수에 넣고 아래처럼 변수를 사용 -->
														<fmt:parseDate var="rtnDateStr" value="${dataList[firStat.index][10]}" pattern="yyyy-MM-dd" />
														<!-- DB에서 가져온 String형 데이터를 Date로 변환 var 변수명, pattern 날짜형식, value 컨트롤러에서 받은값 var에 저장 -->
														<fmt:formatDate var="rtnDateFmt" value="${rtnDateStr}" pattern="yyyy-MM-dd" />
														<!-- Date형으로 저장된 값을 String으로 변환 var변수에 넣고 아래처럼 변수를 사용 -->
													<td style="text-align:center;">
														${brwDateFmt}<br>
														<c:choose>
															<c:when test="${empty rtnDateFmt}">
																-
															</c:when>
															<c:otherwise>
																${rtnDateFmt}
															</c:otherwise>
														</c:choose>
													</td>
													<td>${dataList[firStat.index][11]}/${firStat.end}/${firStat.last}</td>
												</tr>
											</c:if>





<!-- async await 사용 간단 예제 두 함수 연결되어있는거랑
	async await 위치 확인하자 -->

makeExcelBrwAndMemberByDateDownload = async () => {
	try {
		let aaa = await makeExcelBrwAndMemberByDate();
		alert(aaa);
	} catch (err) {
		console.error(err);
	}
}

async function makeExcelBrwAndMemberByDate() {
	let staDateStart = document.querySelector('#staDateStart').value;
	let staDateEnd = document.querySelector('#staDateEnd').value;
	
	let downloadLink = '';
	
	await $.ajax({
		url: "/stsc/brw_and_member_by_date.do",
		type: 'GET',
		data: {
			staDateStart,
			staDateEnd
		},
		//dataType: "json",
		success: function(data) {
			if(data.rt != 'OK') {
				alert(data.rt);
			} else {
				downloadLink = data.downloadPath;
			}
		}
	})
	
	return downloadLink;
};
