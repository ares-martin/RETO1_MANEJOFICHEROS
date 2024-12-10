<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Acceso Datos A</title>
</head>
<body>
<h1 style="text-align:center">DATOS</h1>

<table border="1" align="center">
    <tr>
        <th>publicationDate</th>
        <th>value</th>
        <th>magnitud</th>
        <th>estado</th>
        <th>estacion</th>
        <th>periodo</th>
    </tr>
    <%
        List<Map<String, Object>> data = (List<Map<String, Object>>) request.getAttribute("data");

        if (data != null) {
            for (Map<String, Object> dato : data) {
    %>
    <tr>
        <td><%= dato.get("publicationDate") %></td>
        <td><%= dato.get("value") %></td>
        <td><%= dato.get("magnitud") %></td>
        <td><%= dato.get("estado") %></td>
        <td><%= dato.get("estacion") %></td>
        <td><%= dato.get("periodo") %></td>
    </tr>
    <%
            }
        }
    %>
</table>
</body>
</html>
