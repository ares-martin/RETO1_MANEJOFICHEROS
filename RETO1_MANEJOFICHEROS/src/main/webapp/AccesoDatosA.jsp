<%@page import="java.util.Set"%>
<%@page import="controller.ObjetoRDF"%>
<%@page import="java.util.List"%>
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
	<h1 style="text-align: center">DATOS</h1>
	<%
	// Obtener las listas de objetos y propiedades desde el request
	List<ObjetoRDF> objetos = (List<ObjetoRDF>) request.getAttribute("objetos");
	Set<String> propiedades = (Set<String>) request.getAttribute("propiedades");
	%>
	<table border="1">
		<tr>
			<th>URI</th>
			<%
			for (String propiedad : propiedades) {
			%>
			<th><%=propiedad%></th>
			<%
			}
			%>
		</tr>
		<%
		for (ObjetoRDF objeto : objetos) {
		%>
		<tr>
			<td><%=objeto.getUri()%></td>
			<%
			for (String propiedad : propiedades) {
				String valor = objeto.getPropiedades().get(propiedad);
			%>
			<td><%=(valor != null) ? valor : "-"%></td>
			<%
			}
			%>
		</tr>
		<%
		}
		%>
	</table>
</body>
</html>