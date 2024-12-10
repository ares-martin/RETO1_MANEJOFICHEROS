<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map, java.util.List" %>
<!DOCTYPE html>
<%
Map<String, List<String>> dataMap = (Map<String, List<String>>) request.getAttribute("dataMap");
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Acceso a Datos</title>
</head>
<body>
    <h1 style="text-align:center">DATOS</h1>
    
    <table border="1" style="width: 100%; text-align: center;">
        <thead>
            <tr>
                <% 
                    // Generar cabeceras dinámicamente
                    if (dataMap != null) {
                        for (String header : dataMap.keySet()) {
                            out.print("<th>" + header + "</th>");
                        }
                    }
                %>
            </tr>
        </thead>
        <tbody>
            <%
                if (dataMap != null) {
                    // Calcular la cantidad de filas (número de elementos en las listas)
                    int filas = dataMap.values().iterator().next().size(); 

                    // Generar las filas dinámicamente
                    for (int i = 0; i < filas; i++) {
                        out.print("<tr>");
                        for (String header : dataMap.keySet()) {
                            List<String> values = dataMap.get(header);
                            out.print("<td>" + (values.size() > i ? values.get(i) : "") + "</td>");
                        }
                        out.print("</tr>");
                    }
                } else {
                    out.print("<tr><td colspan='7'>No hay datos disponibles</td></tr>");
                }
            %>
        </tbody>
    </table>
</body>
</html>
