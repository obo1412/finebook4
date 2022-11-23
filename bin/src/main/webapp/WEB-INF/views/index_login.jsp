<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/inc/head.jsp" %>

<style type="text/css">
.red {
	color: red;
}

</style>

</head>
<body>
<%@ include file="/WEB-INF/inc/topbar.jsp" %>

<!-- 최신 게시물 목록 영역 -->
<div id="wrapper">
<%@ include file="/WEB-INF/inc/sidebar_left.jsp" %>
	<div id="content-wrapper">
		<div class="container-fluid">
		
		<div class="row my-2 ml-1">
			<div class="card" style="float:left;">
				<div class="card-body">
					<canvas id="brwChart"></canvas>
				</div>
				<div class="card card-body text-center bg-primary">
					<h4>이번달 회원 대출 현황</h4>
				</div>
			</div>
			
			<div class="card ml-2" style="float:left;">
				<div class="card-body">
					<canvas id="brwBookChart"></canvas>
				</div>
				<div class="card card-body text-center bg-primary">
					<h4>이번달 도서 대출 현황</h4>
				</div>
			</div>
		</div>
		
		</div> <!-- container-fluid 종료 -->
		<%@ include file="/WEB-INF/inc/footer.jsp" %>
	</div><!-- content wrapper 끝 -->
</div>
<%@ include file="/WEB-INF/inc/script-common.jsp" %>
<%@ include file="/WEB-INF/inc/script-common-chart.jsp" %>
<script>
	//Set new default font family and font color to mimic Bootstrap's default styling
	Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
	Chart.defaults.global.defaultFontColor = '#292b2c';
	
	<c:if test="${fn:length(brwStatistics)>0}">
		// Pie Chart Example
		brwData = [];
		brwLabel = [];
	
		<c:forEach var="item" items="${brwStatistics}" varStatus="status">
			brwData.push(${item.countIdBrw});
			brwLabel.push("${item.name}");
		</c:forEach>
		
		var ctx = document.getElementById("brwChart");
		var myPieChart = new Chart(ctx, {
		  type: 'doughnut',
		  data: {
		    labels: brwLabel,
		    datasets: [{
		      data: brwData,
		      backgroundColor: ['#007bff', '#dc3545', '#ffc107', '#28a745'],
		    }],
		  },
		});
	</c:if>;
	
	// Bar Chart Example
	<c:if test="${fn:length(brwBookStatistics)>0}">
		brwBookData = [];
		brwBookLabel = [];
	
		<c:forEach var="item" items="${brwBookStatistics}" varStatus="status">
			brwBookData.push(${item.countIdBrw});
			brwBookLabel.push("${item.titleBook}");
		</c:forEach>
		
		var ctxBar = document.getElementById("brwBookChart");
		var myLineChart = new Chart(ctxBar, {
		  type: 'bar',
		  data: {
		    labels: brwBookLabel,
		    datasets: [{
		      label: "대출횟수",
		      backgroundColor: "rgba(2,117,216,1)",
		      borderColor: "rgba(2,117,216,1)",
		      data: brwBookData,
		    }],
		  },
		  options: {
		    scales: {
		      xAxes: [{
		    	  display: false,
		        time: {
		          unit: 'month'
		        },
		        gridLines: {
		          display: false
		        },
		        ticks: {
		          maxTicksLimit: 10
		        }
		      }],
		      yAxes: [{
		        ticks: {
		          min: 0,
		          max: ${maxCountIdBrw},
		          maxTicksLimit: 5
		        },
		        gridLines: {
		          display: true
		        }
		      }],
		    },
		    legend: {
		      display: false
		    }
		  }
		});
	</c:if>
</script>
</body>
</html>