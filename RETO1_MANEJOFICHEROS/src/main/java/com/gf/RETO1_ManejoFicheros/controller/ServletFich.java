package com.gf.RETO1_ManejoFicheros.controller;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import com.gf.RETO1_ManejoFicheros.entities.ObjetoPOJO;

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		getServletContext().setAttribute("datosVacios", false);
		String page = "";

		switch (request.getParameter("eleccionFich")) {
		case "lectura": {
			switch (request.getParameter("formatoFich")) {
			case "XML": {
				// Método para leer XML
			}
			case "JSON": {
				// Método para leer JSON
			}
			case "CSV": {
				// Método para leer CSV
			}
			case "XLS": {
				// Método para leer XLS
			}
			case "RDF": {
				// Método para leer RDF
				leerRDF(request, response);
			}break;
			}
			page = "AccesoDatosA.jsp";
		}break;
		
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
					}
					case "JSON": {
						// Método para escribir en JSON
					}
					case "CSV": {
						// Método para escribir en CSV
					}
					case "XLS": {
						// Método para escribir en XLS
					}
					case "RDF": {
						// Método para escribir en RDF
						escribirRDF(request, response);
					}break;
				
				}
			}
			page = "TratamientoFich.jsp";
		}break;
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
			List<ObjetoPOJO> objetos = new ArrayList<>();
			Set<String> propiedades = new HashSet<>();
			// Iterar sobre los recursos del modelo
			for (Resource resource : model.listSubjects().toList()) {
				// Crear un objeto RDF para cada recurso
				ObjetoPOJO objeto = new ObjetoPOJO(resource.getURI());

				// Iterar sobre cada propiedad de ese recurso
				for (Statement stmt : resource.listProperties().toList()) {
					String propiedad = stmt.getPredicate().getLocalName(); // Nombre de la propiedad
					String valor = stmt.getObject().isLiteral() ? stmt.getObject().asLiteral().getString() // Si es
																											// literal,
																											// obtiene
																											// el valor
							: stmt.getObject().toString(); // Si es un recurso, conviértelo a string

					// Agregar la propiedad al objeto RDF
					objeto.setPropiedad(propiedad, valor);
					propiedades.add(propiedad);
				}
				objetos.add(objeto);
			}

			request.setAttribute("datos", objetos);
			request.setAttribute("cabeceras", propiedades);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void escribirRDF(HttpServletRequest request, HttpServletResponse response) {
		String namespace = "http://example.org/observation/observation-";
		String[] datos = request.getParameterValues("dato");

		// Ruta del archivo RDF
		File f = new File(getServletContext().getRealPath("/calidad-aire.rdf"));

		try {
			// Crear el modelo RDF desde el archivo existente
			Model model = ModelFactory.createDefaultModel();

			try (InputStream inputStream = new FileInputStream(f)) {
				model.read(inputStream, null, "RDF/XML");
			}

			// Crear un nuevo recurso con una URI única
			String uniqueId = UUID.randomUUID().toString();
			String resourceURI = namespace + uniqueId;
			Resource nuevoRecurso = model.createResource(resourceURI);

			// Agregar propiedades al recurso
			nuevoRecurso.addProperty(model.createProperty(namespace, "publicationDate"), datos[0]);
			nuevoRecurso.addProperty(model.createProperty(namespace, "value"), datos[1]);
			nuevoRecurso.addProperty(model.createProperty(namespace, "magnitud"), datos[2]);
			nuevoRecurso.addProperty(model.createProperty(namespace, "estado"), datos[3]);
			nuevoRecurso.addProperty(model.createProperty(namespace, "estacion"), datos[4]);
			nuevoRecurso.addProperty(model.createProperty(namespace, "periodo"), datos[5]);

			// Guardar el modelo en el archivo RDF
			try (FileOutputStream outputStream = new FileOutputStream(f)) {
				model.write(outputStream, "RDF/XML");

				// Abre la aplicacion de lectura predeterminada para ver el archivo modificado
				if (java.awt.Desktop.isDesktopSupported()) {
					java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
					desktop.open(f);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
