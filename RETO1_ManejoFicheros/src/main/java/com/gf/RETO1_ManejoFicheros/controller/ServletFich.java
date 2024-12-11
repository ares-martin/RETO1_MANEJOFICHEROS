package com.gf.RETO1_ManejoFicheros.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.*;

import com.gf.RETO1_ManejoFicheros.entities.ObjetoPOJO;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

		try {
			getServletContext().setAttribute("datosVacios", false);

			switch (request.getParameter("eleccionFich")) {
			case "lectura": {
				switch (request.getParameter("formatoFich")) {
				case "XML": {
					lecturaXML(request);
					page = "AccesoDatosA.jsp";
					break;
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
				}
				}
				// page = "AccesoDatosA.jsp";
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
						escrituraXML(arrayDatos);
						page = "TratamientoFich.jsp";
						break;
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

	
	public static void lecturaXML(HttpServletRequest request) {
		List<ObjetoPOJO> datos = new ArrayList<ObjetoPOJO>();
		Set<String> cabeceras = new HashSet<>();

		try {
			File archivo = new File(ServletFich.class.getClassLoader().getResource("calidad-aire.xml").getFile());

			// Crear una instancia de DocumentBuilderFactory
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder constructor = dbf.newDocumentBuilder();
			Document documento = constructor.parse(archivo);

			// Normalizar el documento
			documento.getDocumentElement().normalize();

			// Obtener todos los nodos <observation-aire>
			NodeList listaObservaciones = documento.getElementsByTagName("observation-aire");

			for (int i = 0; i < listaObservaciones.getLength(); i++) {
				Node nodo = listaObservaciones.item(i);

				if (nodo.getNodeType() == Node.ELEMENT_NODE) {
					Element elemento = (Element) nodo;

					// Crear un nuevo objeto ObjetoPOJO para almacenar los datos de este nodo
					ObjetoPOJO pojo = new ObjetoPOJO();

					// Iterar por las etiquetas hijas del nodo <observation-aire>
					NodeList hijos = elemento.getChildNodes();
					for (int j = 0; j < hijos.getLength(); j++) {
						Node hijo = hijos.item(j);

						if (hijo.getNodeType() == Node.ELEMENT_NODE) {
							String clave = hijo.getNodeName();
							String valor = hijo.getTextContent();

							// Agregar el nombre del nodo a las cabeceras
							cabeceras.add(clave);

							// Agregar la propiedad al POJO
							pojo.setPropiedad(clave, valor);
						}
					}

					// Agregar el POJO a la lista de datos
					datos.add(pojo);
				}
			}
			
			request.setAttribute("datos", datos);
			request.setAttribute("cabeceras", cabeceras);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void escrituraXML(String[] arrayDatos) {
		try {
			// Cargar el archivo XML
			File archivo = new File(ServletFich.class.getClassLoader().getResource("calidad-aire.xml").getFile());

			// Inicializar el parseador de XML
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder constructor = dbf.newDocumentBuilder();
			Document documento = constructor.parse(archivo);

			// Normalizar el documento
			documento.getDocumentElement().normalize();

			// Obtener o crear el nodo <result>
			Node nodoResult = documento.getElementsByTagName("result").item(0);
			if (nodoResult == null) {
				nodoResult = documento.createElement("result");
				documento.getDocumentElement().appendChild(nodoResult);
			}

			// Crear y agregar el nuevo nodo <observation-aire>
			Element nuevaObservacion = documento.createElement("observation-aire");
			String[] nombresElementos = { "publicationDate", "value", "magnitud", "estado", "estacion", "periodo" };

			for (int i = 0; i < nombresElementos.length; i++) {
				Element elemento = documento.createElement(nombresElementos[i]);
				elemento.appendChild(documento.createTextNode(arrayDatos[i]));
				nuevaObservacion.appendChild(elemento);
			}

			nodoResult.appendChild(nuevaObservacion);

			// Guardar los cambios en el archivo
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource fuente = new DOMSource(documento);
			StreamResult resultado = new StreamResult(archivo);

			transformer.transform(fuente, resultado);

			// Abrir el archivo en el equipo
			if (java.awt.Desktop.isDesktopSupported()) {
				java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
				desktop.open(archivo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}