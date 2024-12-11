<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="Error.jsp"%>
<!DOCTYPE html>
<%
boolean datosVacios = application.getAttribute("datosVacios") != null ? (boolean) application.getAttribute("datosVacios") : false;
%>
<html>
<head>
<meta charset="UTF-8">
<title>Tratamiento Fichero</title>
</head>
<body>
	<form action="ServletFich" method="post">
		<h1>TRATAMIENTO FICHEROS</h1>

		<table>
			<tr>
				<td></td>
				<td></td>
				<td>DATO1: <input type="text" name="dato" /></td>
			</tr>

			<tr>
				<td></td>
				<td></td>
				<td>DATO2: <input type="text" name="dato" /></td>
			</tr>

			<tr>
				<td>Formato del fichero: <select name="formatoFich">
						<option value="XML">XML</option>
						<option value="JSON">JSON</option>
						<option value="CSV">CSV</option>
						<option value="XLS">XLS</option>
						<option value="RDF">RDF</option>
				</select>
				</td>
				<td></td>
				<td>DATO3: <input type="text" name="dato" /></td>
			</tr>

			<tr>
				<td>¿Qué quiere hacer con el fichero?</td>
				<td></td>
				<td>DATO4: <input type="text" name="dato" /></td>
			</tr>

			<tr>
				<td>Lectura: <input type="radio" name="eleccionFich" value="lectura" checked></td>
				<td></td>
				<td>DATO5: <textarea name="dato" rows="3" cols="20"></textarea></td>
			</tr>

			<tr>
				<td>Escritura: <input type="radio" name="eleccionFich" value="escritura"></td>
				<td></td>
				<td>DATO6: <input type="text" name="dato" /></td>
			</tr>
			
			<tr>
				<td></td>
				<td></td>
				<td>
					<% if (datosVacios) { %>
						<p style="color: red">(*) Los campos no pueden estar vacíos</p>
					<% } %>
				</td>
			</tr>
		</table>
		
		
		<input type="submit" value="Enviar" name="boton" />
	</form>
</body>
</html>
