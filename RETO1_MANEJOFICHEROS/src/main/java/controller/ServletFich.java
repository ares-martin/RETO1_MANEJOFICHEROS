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
	}

}
