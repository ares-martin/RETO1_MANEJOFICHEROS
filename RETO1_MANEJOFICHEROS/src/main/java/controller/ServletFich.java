package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.google.gson.Gson;

/**
 * Servlet implementation class ServletFich
 */
@WebServlet("/ServletFich")
public class ServletFich extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletFich() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = "";
		String[] arrayDatos = request.getParameterValues("dato");
		boolean datosVacios = false;
		getServletContext().setAttribute("datosVacios", false);
		String eleccionFich = request.getParameter("eleccionFich") != null ? (String)request.getParameter("eleccionFich") : "";
		
		for (String s : arrayDatos) {
			if (s.isBlank()) {
				datosVacios = true;
			}
		}
		
		if (datosVacios) {
			getServletContext().setAttribute("datosVacios", true);
			page = "TratamientoFich.jsp";
		} else {
			page = "";
		}

		request.getRequestDispatcher(page).forward(request, response);
		
		if(request.getAttribute("formatoFich").equals("JSON")) {
			lecturaJSON();
		}
	}
	
	protected void lecturaJSON() {
		Gson gson = new Gson();
		
		String fichero = "calidad-aire.json";

		try (BufferedReader br = new BufferedReader(new FileReader("calidad-aire.json"))) {
		    String linea;
		    while ((linea = br.readLine()) != null) {
		        fichero += linea;
		    }

		} catch (FileNotFoundException ex) {
		    System.out.println(ex.getMessage());
		} catch (IOException ex) {
		    System.out.println(ex.getMessage());
		}
		
		Properties properties = gson.fromJson(fichero, Properties.class);
	}

}
