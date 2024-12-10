package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;


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
		getServletContext().setAttribute("datosVacios", false);
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
						leerRDF(request, response);
					}
				}
				page = "AccesoDatosA.jsp";
				break;
			}
			case "escritura": {
				String[] arrayDatos = request.getParameterValues("dato");
				boolean datosVacios = false;
				
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
							escribirRDF(request, response);
							leerRDF(request, response);
						}
					}
					page = "AccesoDatosA.jsp";
				}
				break;
			}
		}
		request.getRequestDispatcher(page).forward(request, response);
	}
	
	private void leerRDF(HttpServletRequest request, HttpServletResponse response) {
		try (InputStream inputStream = ServletFich.class.getClassLoader().getResourceAsStream("calidad-aire.rdf")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Archivo no encontrado: calidad-aire.rdf");
            }
            
            Model model = ModelFactory.createDefaultModel();
            model.read(inputStream, null, "RDF/XML");

            // Mostrar los triples
            List<ObjetoRDF> objetos = new ArrayList<>();
            Set<String> todasLasPropiedades = new HashSet<>();
            // Iterar sobre los recursos del modelo
            for (Resource resource : model.listSubjects().toList()) {
                // Crear un objeto RDF para cada recurso
                ObjetoRDF objeto = new ObjetoRDF(resource.getURI());
                
                // Iterar sobre cada propiedad de ese recurso
                for (Statement stmt : resource.listProperties().toList()) {
                    String propiedad = stmt.getPredicate().getLocalName(); // Nombre de la propiedad
                    String valor = stmt.getObject().isLiteral() 
                                    ? stmt.getObject().asLiteral().getString() // Si es literal, obtiene el valor
                                    : stmt.getObject().toString(); // Si es un recurso, conviértelo a string
                                    
                    // Agregar la propiedad al objeto RDF
                    objeto.setPropiedad(propiedad, valor);
                    todasLasPropiedades.add(propiedad);
                }
                objetos.add(objeto);
            }
            
            request.setAttribute("objetos", objetos);
    		request.setAttribute("propiedades", todasLasPropiedades);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void escribirRDF(HttpServletRequest request, HttpServletResponse response) {
		String namespace = "http://example.org/observation/observation-";
		String [] datos = request.getParameterValues("dato");
		

            // 3️⃣ Cargar el modelo RDF desde el InputStream
            Model model = ModelFactory.createDefaultModel();

          

            // 4️⃣ Crear un nuevo recurso con una URI única
            String uniqueId = UUID.randomUUID().toString();
            String resourceURI = namespace + uniqueId;
            Resource nuevoRecurso = model.createResource(resourceURI);

            // 5️⃣ Agregar las propiedades al recurso
            nuevoRecurso.addProperty(model.createProperty(namespace, "publicationDate"), datos[0]);
            nuevoRecurso.addProperty(model.createProperty(namespace, "value"), datos[1]);
            nuevoRecurso.addProperty(model.createProperty(namespace, "magnitud"), datos[2]);
            nuevoRecurso.addProperty(model.createProperty(namespace, "estado"), datos[3]);
            nuevoRecurso.addProperty(model.createProperty(namespace, "estacion"), datos[4]);
            nuevoRecurso.addProperty(model.createProperty(namespace, "periodo"), datos[5]);
            
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("calidad-aire.rdf");

            if (inputStream != null) {
                model.read(inputStream, null, "RDF/XML");
            }
            // 6️⃣ Sobrescribir el archivo RDF

            File rdfFile = new File(getServletContext().getRealPath("/calidad-aire.rdf"));
            try (FileOutputStream out = new FileOutputStream(rdfFile)) {
                model.write(out, "RDF/XML");
            } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
