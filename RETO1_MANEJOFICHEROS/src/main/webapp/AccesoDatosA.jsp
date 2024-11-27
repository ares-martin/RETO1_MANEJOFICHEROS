<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- Lo más óptimo sería que cada tipo de fichero enviase un array o similar con los encabezados para 
	hacer un for each -->
	<h1 style="text-align:center">DATOS</h1>
	
	<table border="1">
		<tr>
			<td>publicationDate</td><td>value</td><td>value</td><td>magnitud</td>
			<td>estado</td><td>estacion</td><td>periodo</td>
		</tr>
		<%
		//Definir el for según el nombre del request y el tipo
		for(String dato : request.getAttribute(name)){%>
			<tr>
				<td><%=dato %></td>
			</tr>
		<%}%>
	</table>
</body>
</html>