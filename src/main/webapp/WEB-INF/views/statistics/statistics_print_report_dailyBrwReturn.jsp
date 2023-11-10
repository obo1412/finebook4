<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!-- 오늘날짜 만들고, 날짜패턴 아래와같이 바꾸기 -->
<jsp:useBean id="currDate" class="java.util.Date" />
<fmt:parseDate var="pickDateParse" value="${pickDate}" pattern="yyyy-MM-dd" />
<fmt:formatDate var="pickDatePattern" value="${pickDateParse}" pattern="yyyy년MM월dd일" />
<!-- 오늘날짜 관련 끝 -->
<!doctype html>
<html>
<head>

<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.2.1.min.js"></script>

<style type="text/css">
	@media print {
		.sidebar {
			display: none;
		}
		.sticky-footer {
			display: none;
		}
		.prtNone {
			display: none;
		}
	}
	
	@media all {
		@page {
			size: A4;
			margin: 0;
		}
		html, body {
			border: 0;
			margin: 0;
			padding: 0;
			font-family: '굴림';
			box-sizing: border-box;
		}
		
		.background-grey {
			background-color: #CFCFCF;
		}
		
		table {
			/* 보더 사이사이에 간격이 생기는데, 아래 기능쓰면 한줄로 됨. */
			border-collapse: collapse;
			/* 글자에 따라서 자동으로 늘어나는데, 그것을 막으면서 overflow nowrap 조건주면된다. */
			table-layout: fixed;
		}
		
		th, td {
			border: 1px solid black;
			white-space: nowrap;
			overflow: hidden;
		}
		
		tr {
			height: 36px;
		}
		
		table.no-border > tbody > tr {
			height: 6mm;
		}
		
		table.no-border > tbody > tr > td {
			border: 0 none;
			font-size: 12px;
			font-weight: bold;
		}
		
	}
</style>
</head>
<body>
	<div class="prtNone">
		<span class="prtNone" style="font-size:30pt;">통계 인쇄</span>
		<span class="prtNone">
			<button class="btn btn-danger prtNone" id="btn-print">인쇄</button>
		</span>
	</div>

	<div class="A4" style="width:210mm; height:297mm;">
		<!-- 비어있는 공간 -->
		<div style="float:left; width:100%; height:20mm;">
		</div>
		<!-- 비어있는 공간 -->
		
		<!-- 내용 시작 -->
		<div style="float:left; width:100%; height:277mm;">
			<!-- 일보 - 도서관명 -->
			<div style="float:left; width:100%;">
				<span style="float:left; margin-left:15mm; font-size:30px;">日 報</span>
				<span style="float:right; margin-right:10mm; font-size:14px;">${nameLib}</span>
			</div>
			<!-- 일보 - 도서관명 -->
			
			<!-- border 공간 세로로 가득 시작 -->
			<div style="float:left; width:100%; height:260mm;">
				<!-- table border 시작 -->
				<div style="width:94%; height:260mm; box-sizing:border-box; margin:auto;">
					<!-- 상단 업무보고 칸 -->
					<div class="background-grey" style="float:left; width:100%; height:10mm; box-sizing:border-box; border:1px solid black;">
						${pickDatePattern} 업무 보고
					</div>
					<!-- 상단 업무보고 칸 -->
					<!-- 통계 현황 칸 -->
					<div style="float:left; width:100%; height:20mm; box-sizing:border-box; border-left:1px solid black; border-right:1px solid black;">
						<table class="no-border" style="margin-top:1mm; width:100%; text-align:center;">
							<tbody>
								<tr>
									<td>대출</td>
									<td>${brwCount} 권</td>
									<td>대출중도서</td>
									<td>${totalBrw} 권</td>
									<td>신규등록</td>
									<td>- 권</td>
									<td colspan="2"></td>
								</tr>
								<tr>
									<td>반납</td>
									<td>${rtnCount} 권</td>
									<td>연체중도서</td>
									<td>${totalOverDue} 권</td>
									<td>폐기도서</td>
									<td>- 권</td>
									<td>현재등록도서</td>
									<td>0000 권</td>
								</tr>
								<tr>
									<td>이용자현황</td>
									<td colspan="4">(신규등록 --명, 제적 --명, 현재 등록자 00명)</td>
									<td colspan="3"></td>
								</tr>
							</tbody>
						</table>
					</div>
					<!-- 통계 현황 칸 -->
					<div style="float:left; width:100%; height:230mm; box-sizing:border-box;">
						<table style="width:100%;">
							<thead>
								<tr class="background-grey">
									<th style="width:40px;">번호</th>
									<th style="width:40px;">구분</th>
									<th style="width:300px;">
											도서명/저자명<br>
											출판사/등록번호
									</th>
									<th style="width:200px;">고객성명/연락처<br>고객고유번호</th>
									<th style="width:90px;">대출일자<br>반납기일</th>
									<th>비고</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${fn:length(dataList) > 0}">
										<c:forEach var="row" items="${dataList}" varStatus="firStat">
											<!-- index21 면 22번째칸의 공간 띄기-->
											<c:if test="${(firStat.index ne 1) and ((firStat.index mod 21) eq 1)}">
												<div style="width:100%; height:30mm;"></div>
											</c:if>
											<!-- 이부분 검증 제대로 안했음. 다시 체크하자. -->
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
													<td>${dataList[firStat.index][11]}</td>
												</tr>
											</c:if>
											
											<!-- 빈칸 강제로 생성되며, 마지막일 경우에만 작동 -->
											<c:choose>
												<c:when test="${(firStat.last)and(fn:length(dataList) < 24)}">
													<c:forEach begin="0" end="${23-fn:length(dataList)}" step="1" varStatus="under">
														<tr>
															<td></td>
															<td></td>
															<td></td>
															<td></td>
															<td></td>
															<td></td>
														</tr>
													</c:forEach>
												</c:when>
												<c:when test="${(firStat.last)and(fn:length(dataList)-1)%22 > 0}">
													<c:forEach begin="0" end="${23-((fn:length(dataList)-1)%22)}" step="1">
														<tr>
															<td></td>
															<td></td>
															<td></td>
															<td></td>
															<td></td>
															<td></td>
														</tr>
													</c:forEach>
												</c:when>
												<c:otherwise>
												</c:otherwise>
											</c:choose>
											<!-- 빈칸 강제로 생성 끝-->
										</c:forEach>
										
									</c:when>
									<c:otherwise>
										<c:forEach begin="0" end="22" step="1" varStatus="blankStat">
											<tr>
												<td>${blankStat.count}</td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
								
							</tbody>
						</table>
					</div>
				</div>
				<!-- table border 끝 -->
			</div>
			<!-- border 공간 세로로 가득 끝 -->
		</div>
	</div>
	

</body>



<script type="text/javascript">

	$(function() {
		$("#btn-print").click(function() {
			if (navigator.userAgent.indexOf("MSIE") > 0) {
				printPage();
			} else if (navigator.userAgent.indexOf("Chrome") > 0) {
				window.print();
			}
		});
	});
	
</script>
</html>