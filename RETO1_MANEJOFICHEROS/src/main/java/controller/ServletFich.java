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
		String page = "";

		switch (request.getParameter("eleccionFich")) {
			case "lectura": {
				switch (request.getParameter("formatoFich")) {
					case "XML": {
						//Método para leer XML
					}
					case "JSON": {
						//Método para leer JSON
					}
					case "CSV": {
						//Método para leer CSV
					}
					case "XLS": {
						//Método para leer XLS
					}
					case "RDF": {
						//Método para leer RDF
					}
				}
				page = "AccesoDatosA.jsp";
				break;
			}
			case "escritura": {
				String[] arrayDatos = request.getParameterValues("dato");
				boolean datosVacios = false;
				getServletContext().setAttribute("datosVacios", false);
				
				//Comprobación por si hay datos en blanco
				for (String s : arrayDatos) {
					if (s.isBlank()) {
						datosVacios = true;
					}
				}
				
				if (datosVacios) {
					getServletContext().setAttribute("datosVacios", true);
					page = "TratamientoFich.jsp";
				} else {
					switch (request.getParameter("formatoFich")) {
						case "XML": {
							//Método para escribir en XML
						}
						case "JSON": {
							//Método para escribir en JSON
						}
						case "CSV": {
							//Método para escribir en CSV
						}
						case "XLS": {
							//Método para escribir en XLS
						}
						case "RDF": {
							//Método para escribir en RDF
						}
					}
					//No sé como hacer para que salga de aquí sin pasar por el dispatchet!!!!
				}
				break;
			}
		}
		request.getRequestDispatcher(page).forward(request, response);
	}
}
