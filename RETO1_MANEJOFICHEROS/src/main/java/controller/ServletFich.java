package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

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
		
		//Guardara el jsp que va a redirigir el servlet
		String page = "";
		String[] arrayDatos = request.getParameterValues("dato");
		boolean datosVacios = false;
		getServletContext().setAttribute("datosVacios", false);
		
		//Recoge la accion elegida por el usuario para lectura o escritura
		String accionFichero = request.getParameter("accionFichero") != null ? (String)request.getParameter("accionFichero") : "";
		
		//Comprobar si los datos no estan vacios
		for (String s : arrayDatos) {
			if (s.isBlank()) {
				datosVacios = true;
			}
		}
		
		if (!datosVacios) {
			
			page = "";
		} else {
			
			getServletContext().setAttribute("datosVacios", true);
			page = "TratamientoFich.jsp";
		}

		request.getRequestDispatcher(page).forward(request, response);
	}
	
	private 

}
