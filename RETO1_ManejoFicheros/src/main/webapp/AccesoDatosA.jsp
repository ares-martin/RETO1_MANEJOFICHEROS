<%@page import="java.util.Set"%>
<%@page import="com.gf.RETO1_ManejoFicheros.entities.*"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Acceso a Datos</title>
</head>
<body>
	<h1 style="text-align: center">DATOS</h1>
	<%
	// Obtener las listas de objetos y propiedades desde el request
	List<ObjetoPOJO> datos = (List<ObjetoPOJO>) request.getAttribute("datos");
	Set<String> cabeceras = (Set<String>) request.getAttribute("cabeceras");
	%>
	
	<table border="1">
		<tr>
			<th>URI</th>
			<% for (String propiedad : cabeceras) { %>
				<th><%=propiedad%></th>
			<% } %>
		</tr>
		
		<% for (ObjetoPOJO objeto : datos) { %>
		<tr>
			<td><%= objeto.getUri() != null?objeto.getUri():"-"%></td>
			
			<% for (String propiedad : cabeceras) {
					String valor = objeto.getPropiedades().get(propiedad);
			%>
				<td><%=(valor != null) ? valor : "-"%></td>
			<% } %>
		</tr>
		<% } %>
	</table>
</body>
</html>