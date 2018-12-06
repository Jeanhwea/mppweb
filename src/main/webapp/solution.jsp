<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="/mpp/js/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="/mpp/js/d3/d3.js"></script>
<script type="text/javascript" src="/mpp/js/d3gantt.js"></script>
<script type="text/javascript" src="/mpp/js/helper.js"></script>
<script type="text/javascript">
<%
out.write("$.getJSON(\"/mpp/PerformServlet?action=2&solution_id="+request.getParameter("sid")+"&filename=" + request.getParameter("filename") + "\", function(data) {gantt_plot.plot(data);});\n");
%>
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View Gantt Chart</title>
</head>
<body>
 <h1>Solution <%=request.getParameter("sid") %>:</h1>
	<div id="cnt-gantt"></div>
	<div style="padding-left:50px">
	<button type="button" onclick="gantt_plot.addTask()">Add a task</button>
	<button type="button" onclick="gantt_plot.removeTask()">Remove a task</button>
	<button type="button" onclick="gantt_plot.addAllTasks()">Add all tasks</button>
	<button type="button" onclick="gantt_plot.removeAllTasks()">Remove all tasks</button>
	</div>
</body>
</html>