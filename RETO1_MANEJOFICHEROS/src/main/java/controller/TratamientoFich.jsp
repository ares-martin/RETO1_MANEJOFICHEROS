<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="ServletFich" method="post">
	<h1>TRATAMIENTO FICHEROS</h1>
	
	<table>
		<tr>
			<td colspan="2"></td>
			<td>DATO1: <input type="text" name="dato"/></td>
		</tr>
		
		<tr>
			<td colspan="2"></td>
			<td>DATO2: <input type="text" name="dato"/></td>
		</tr>
		
		<tr>
			<td>Formato del fichero: 
				<select name="formatoFich">
					<option value="XML">XML</option>
					<option value="JSON">JSON</option>
					<option value="CSV">CSV</option>
					<option value="XLS">XLS</option>
					<option value="RDF">RDF</option>
				</select>
			</td>
			<td colspan="1"></td>
			<td>DATO3: <input type="text" name="dato"/></td>
		</tr>
		
		<tr>
			<td>¿Qué quiere hacer con el fichero?</td>
			<td colspan="1"></td>
			<td>DATO4: <input type="text" name="dato"/></td>
		</tr>
		
		<tr>
			<td>
				Lectura: <input type="radio" name="eleccionFich" value="lectura" checked>
				Escritura: <input type="radio" name="eleccionFich" value="escritura">
			</td>
			<td colspan="1"></td>
			<td>DATO5: <textarea name="dato" rows="3" cols="20"></textarea></td>
		</tr>
		
		<tr>
			<td colspan="2"></td>
			<td>DATO6: <input type="text" name="dato"/></td>
		</tr>
	</table>
	
	<%
	if(request.getAttribute("error")!=null){
		if(request.getAttribute("error").equals("error2")){
			%><p style="color:red">(*) Los campos no pueden estar vacíos</p>
		<%}
	}
%>
	
	<input type="submit" value="Enviar" name="boton"/>
</form>
</body>
</html>