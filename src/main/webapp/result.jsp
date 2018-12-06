<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.jeanhwea.ds.MyTask"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="/mpp/js/jquery/jquery-1.9.1.js"></script>
<script src="/mpp/js/highcharts/highcharts.js"></script>
<script src="/mpp/js/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="/mpp/js/d3/d3.js"></script>
<script type="text/javascript" src="/mpp/js/d3gantt.js"></script>
<script type="text/javascript" src="/mpp/js/helper.js"></script>
<script type="text/javascript">
<%
out.write("$.getJSON(\"/mpp/PerformServlet?action=1&filename="+request.getAttribute("xml_filename")+"\", function(data) {plot_generation(data);});\n");
//out.write("$.getJSON(\"/mpp/SolutionServlet?action=2&filename="+request.getAttribute("xml_filename")+"\", function(data) {gantt_plot.plot(data);});\n");
%>
</script>
<title>Result</title>
</head>
<body>
	<h1>Result</h1>
	<h2>1. Uploaded Status</h2>
	<div> <p>${requestScope["message"]}</p> </div>
	<h2>2. Brief Report</h2>
	<p>Uploaded filename: ${requestScope["upload_filename"]}</p>
	<p>Size after pre-processing: <strong>${requestScope["ntask"]} tasks, ${requestScope["nreso"]} resources, ${requestScope["ndepd"]} dependencies.</strong></p>
	<%
		String d_str = (String)request.getAttribute("depd_img");
		out.write("<img src=\"" + d_str + "\" onload='AutoResizeImage(1200, 400, this)'/>");
	%>
	</br>
	<h2>3. Processing</h2>
	<div id="ctn-generation" style="min-width: 310px; height: 400px; margin: 0 auto"></div>	
	
	<h3>4. Solutions</h3>
	<p>Three solutions:</p>
	<%
	String xml_filename = (String) request.getAttribute("xml_filename");
	%>
	<table>
	<tr> <td>Solution 1:</td><td><a target="_blank" href="/mpp/solution.jsp?sid=1&filename=<%=xml_filename%>"/>View Gantt Chart</a></td> </tr>
	<tr> <td>Solution 2:</td><td><a target="_blank" href="/mpp/solution.jsp?sid=2&filename=<%=xml_filename%>"/>View Gantt Chart</a></td> </tr>
	<tr> <td>Solution 3:</td><td><a target="_blank" href="/mpp/solution.jsp?sid=3&filename=<%=xml_filename%>"/>View Gantt Chart</a></td> </tr>
	</table>	
</body>
</html>