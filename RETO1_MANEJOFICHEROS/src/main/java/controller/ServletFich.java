package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

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
		//lecturaRDF();
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
	
	private void lecturaRDF() {
        Model model = ModelFactory.createDefaultModel();
        
        try (InputStream in = ServletFich.class.getClassLoader().getResourceAsStream("calidad-aire.rdf")) {
            if (in == null) {
                throw new IllegalArgumentException("Archivo no encontrado: ");
            }

            // Leer el modelo RDF
            model.read(in, null, "RDF/XML");

            // Iterar sobre los triples
            StmtIterator iter = model.listStatements();
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                Resource subject = stmt.getSubject();     // Sujeto
                Property predicate = stmt.getPredicate(); // Predicado
                RDFNode object = stmt.getObject();        // Objeto

                System.out.println(subject.toString() + " " +
                                   predicate.toString() + " " +
                                   object.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
