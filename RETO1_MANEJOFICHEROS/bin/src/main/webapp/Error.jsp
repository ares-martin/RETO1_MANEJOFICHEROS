<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>
</head>
<body>

	<h1 style="color: red" align="center">TIPO DE ERROR</h1>
	
	<%
	if (exception != null) {
		exception.printStackTrace(new PrintWriter(out));
	} %>
	
</body>
</html>
