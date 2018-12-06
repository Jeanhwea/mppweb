<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Index</title>
</head>
<body>
<h1>SB-PPMT</h1>
<p>Search Based Paralleled Project Management Tool</p>
<div>
	<form action="/mpp/UploadServlet" method="post" enctype="multipart/form-data" >
		<label for="file">Please choose a mpp file to upload:</label><br/>
		<input type="file" name="file" id="file" /><br/>
		<label for="file">Processing will task several minitues - please wait!!!</label><br/>
		<input type="submit" name="submit" value="Submit" /><br/>
	</form>
</div>
</body>
</html>