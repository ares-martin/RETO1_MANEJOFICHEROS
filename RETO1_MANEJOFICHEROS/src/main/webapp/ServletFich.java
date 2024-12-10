package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

/**
 * Servlet implementation class ServletFich
 */
@WebServlet("/ServletFich")
public class ServletFich extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Gson gson = new Gson(); // variable para leer JSON

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String page = "";

		try {
			getServletContext().setAttribute("datosVacios", false);

			switch (request.getParameter("eleccionFich")) {
			case "lectura": {
				switch (request.getParameter("formatoFich")) {
				case "XML": {
					// Método para leer XML
					break;
				}
				case "JSON": {
					// Método para leer JSON
					List<Map<String, Object>> data = lecturaJSON();

					request.setAttribute("data", data);
					request.setAttribute("prueba", "prueba");
					break;
				}
				case "CSV": {
					// Método para leer CSV
					break;
				}
				case "XLS": {
					// Método para leer XLS
					break;
				}
				case "RDF": {
					// Método para leer RDF
					break;
				}
				}
				page = "AccesoDatosA.jsp";
				break;
			}
			case "escritura": {
				String[] arrayDatos = request.getParameterValues("dato");
				boolean datosVacios = false;

				// Comprobación por si hay datos en blanco
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
						// Método para escribir en XML
						break;
					}
					case "JSON": {
						// Método para escribir en JSON
						escrituraJSON(request, response);
						
						String jsonFilePath = Paths.get(getClass().getClassLoader().getResource("data.json").toURI()).toString();
						File archivo = new File(jsonFilePath);
						
						abrirFichero(archivo);
						break;
					}
					case "CSV": {
						// Método para escribir en CSV
						break;
					}
					case "XLS": {
						// Método para escribir en XLS
						break;
					}
					case "RDF": {
						// Método para escribir en RDF
						break;
					}
					}
					page = "TratamientoFich.jsp";
				}
				break;
			}
			}
		} catch (Exception e) {
			page = "Error.jsp";
		} finally {
			request.getRequestDispatcher(page).forward(request, response);
		}

	}

	protected List<Map<String, Object>> lecturaJSON() throws ServletException {

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("calidad-aire.json");
		if (inputStream == null) {
			throw new ServletException();

		}

		try (InputStreamReader reader = new InputStreamReader(inputStream)) {
			Map<String, Object> root = gson.fromJson(reader, Map.class);
			List<Map<String, Object>> data = (List<Map<String, Object>>) root.get("result");
			return data;

		} catch (IOException e) {
			throw new ServletException(e);

		}
	}
	
	protected void escrituraJSON(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String[] datosArray = request.getParameterValues("dato");
		if(datosArray == null || datosArray.length == 0) {
			request.setAttribute("datosVacios", true);
            request.getRequestDispatcher("/tratamientoFich.jsp").forward(request, response);
            return;
		}
		
		List<String> datosList = Arrays.asList(datosArray);
		
		String ruta;
		try {
			ruta = Paths.get(getClass().getClassLoader().getResource("calidad-aire.json").toURI()).toString();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			throw new ServletException(e);
		}
		
		try(FileWriter writer = new FileWriter(ruta)){
			gson.toJson(datosList, writer);
		}catch(JsonIOException | IOException e) {
			throw new ServletException(e);
		}
		 
	}
	
	protected void abrirFichero(File archivo) throws ServletException {
		 try {

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();

                if (archivo.exists() && archivo.isFile()) {
                    desktop.open(archivo);
                } 
            } 
        } catch (IOException e) {
            throw new ServletException();
        }
    }
	
}
