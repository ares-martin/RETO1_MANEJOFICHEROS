package com.gf.RETO1_ManejoFicheros.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.UUID;

import org.w3c.dom.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gf.RETO1_ManejoFicheros.entities.ObjetoPOJO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
					break;
				}
				case "JSON": {
					lecturaJSON(request);
					break;
				}
				case "CSV": {
					// Método para leer CSV
					lecturaCSV(request);
					break;
				}
				case "XLS": {
					lecturaXLS(request);
					break;
				}
				case "RDF": {
					lecturaRDF(request, response);
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
						escrituraXML(arrayDatos);
						break;
					}
					case "JSON": {
						escrituraJSON(request, response);
						break;
					}
					case "CSV": {
						// Método para escribir en CSV
						escrituraCSV(request, response);
						break;
					}
					case "XLS": {
						escrituraXLS(request);
						break;
					}
					case "RDF": {
						escrituraRDF(request, response);
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
	
	private void lecturaJSON(HttpServletRequest request) {
		List<ObjetoPOJO> datos = new ArrayList<>();
		Set<String> cabeceras = new HashSet<>();

		// Cabeceras permitidas
		Set<String> cabecerasPermitidas = Set.of("publicationDate", "value", "magnitud", "estado", "estacion",
				"periodo");

		try {
			// Cargar el archivo JSON
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("calidad-aire.json");

			try (InputStreamReader reader = new InputStreamReader(inputStream)) {
				Gson gson = new Gson();
				Map<String, Object> jsonMap = gson.fromJson(reader, Map.class);

				// Extraer la lista de resultados
				if (jsonMap.containsKey("result")) {
					List<Map<String, Object>> result = (List<Map<String, Object>>) jsonMap.get("result");

					for (Map<String, Object> item : result) {
						ObjetoPOJO pojo = new ObjetoPOJO();

						// Rellenar el mapa de propiedades
						for (Map.Entry<String, Object> entry : item.entrySet()) {
							String clave = entry.getKey();
							String valor = (entry.getValue() != null) ? entry.getValue().toString() : null;

							// Se agregan solo las propiedades permitidas
							if (cabecerasPermitidas.contains(clave)) {
								pojo.setPropiedad(clave, valor);
								cabeceras.add(clave);
							}
						}
						datos.add(pojo);
					}
				}
				request.setAttribute("datos", datos);
				request.setAttribute("cabeceras", cabeceras);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void escrituraJSON(HttpServletRequest request, HttpServletResponse response) {
		// Ruta del archivo JSON
		File archivo = new File(ServletFich.class.getClassLoader().getResource("calidad-aire.json").getFile());
		String[] datos = request.getParameterValues("dato");

		try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
			// Leer el contenido del archivo JSON
			StringBuilder jsonContent = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonContent.append(line);
			}

			// Crear objeto GSON para trabajar con JSON
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(jsonContent.toString(), JsonObject.class);

			// Obtener datos desde JSP y asignarlos a las claves correctas
			JsonObject newData = new JsonObject();
			newData.addProperty("publicationDate", datos[0]);
			newData.addProperty("value", datos[1]);
			newData.addProperty("magnitud", datos[2]);
			newData.addProperty("estado", datos[3]);
			newData.addProperty("estacion", datos[4]);
			newData.addProperty("periodo", datos[5]);

			// Agregar el nuevo objeto a la lista 'result'
			JsonArray resultArray = jsonObject.getAsJsonArray("result");
			resultArray.add(newData);

			// Escribir el JSON modificado de nuevo en el archivo
			try (FileWriter file = new FileWriter(archivo)) {
				gson.toJson(jsonObject, file);
				file.flush();
			}

			// Abrir el archivo en el equipo
			if (java.awt.Desktop.isDesktopSupported()) {
				java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
				desktop.open(archivo);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void lecturaXLS(HttpServletRequest request) throws Exception {
		List<ObjetoPOJO> datos = new ArrayList<ObjetoPOJO>();
		Set<String> cabeceras = new HashSet<>();

		// Leer archivo
		FileInputStream file = new FileInputStream(
				new File(ServletFich.class.getClassLoader().getResource("calidad-aire.xlsx").getFile()));
		Workbook workbook = WorkbookFactory.create(file);
		Sheet sheet = workbook.getSheetAt(0);

		// Bandera para identificar la primera fila
		boolean isHeaderRow = true;
		List<String> cabecerasAux = new ArrayList<>(); // Lista auxiliar

		// Leer filas y celdas
		for (Row row : sheet) {
			if (isHeaderRow) {
				// Procesar cabeceras
				for (Cell cell : row) {
					String cabecera = cell.getStringCellValue().trim();
					cabeceras.add(cabecera);
					cabecerasAux.add(cabecera);
				}
				isHeaderRow = false;
			} else {
				// Procesar filas
				ObjetoPOJO pojo = new ObjetoPOJO();
				int columnIndex = 0;

				for (Cell cell : row) {
					if (columnIndex < cabecerasAux.size()) {
						String key = cabecerasAux.get(columnIndex);
						String value;

						// Convertir valor según tipo
						switch (cell.getCellType()) {
						case STRING:
							value = cell.getStringCellValue();
							break;
						case NUMERIC:
							value = String.valueOf(cell.getNumericCellValue());
							break;
						case BOOLEAN:
							value = String.valueOf(cell.getBooleanCellValue());
							break;
						default:
							value = "";
						}
						pojo.setPropiedad(key, value);
					}
					columnIndex++;
				}
				datos.add(pojo);
			}
		}
		request.setAttribute("datos", datos);
		request.setAttribute("cabeceras", cabeceras);

		workbook.close();
		file.close();
	}

	public static void escrituraXLS(HttpServletRequest request) throws Exception {
		// Obtener datos del formulario
		String[] datos = request.getParameterValues("dato");

		File file = new File(ServletFich.class.getClassLoader().getResource("calidad-aire.xlsx").getFile());
		FileInputStream inputStream = new FileInputStream(file);
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);

		int ultimaFila = sheet.getLastRowNum() + 1; // Índice de la nueva fila
		Row nuevaFila = sheet.createRow(ultimaFila);

		// Agregar datos a las celdas
		int colum = 0;
		for (String dato : datos) {
			nuevaFila.createCell(colum).setCellValue(dato);
			colum++;
		}

		// Cerrar inputStream ANTES de escribir
		inputStream.close();

		// Guardar cambios
		FileOutputStream outputStream = new FileOutputStream(file);
		workbook.write(outputStream);

		outputStream.close();
		workbook.close();

		if (java.awt.Desktop.isDesktopSupported()) {
			java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
			desktop.open(file);
		}
	}

	private void lecturaRDF(HttpServletRequest request, HttpServletResponse response) {
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

	private void escrituraRDF(HttpServletRequest request, HttpServletResponse response) {
		String namespace = "http://example.org/observation/";
		String[] datos = request.getParameterValues("dato");

		// Ruta del archivo RDF
		// He tenido que modificar en el Working directory al del proyecto para que me cogiese bien la ruta
		File f = new File("src/main/resources/calidad-aire.rdf");
		
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

	public static void lecturaCSV(HttpServletRequest request) {
        List<ObjetoPOJO> datos = new ArrayList<>();
        Set<String> cabeceras = new LinkedHashSet<>();

        try {
        	// Ruta del archivo CSV
            InputStream inputStream = ServletFich.class.getClassLoader().getResourceAsStream("calidad-aire.csv"); 
            if (inputStream == null) {
                throw new FileNotFoundException("Archivo no encontrado: calidad-aire.csv");
            }

            
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {			// Leer el archivo usando BufferedReader
                String linea;
                boolean esCabecera = true;

                while ((linea = br.readLine()) != null) {
                    String[] valores = linea.split(";"); 												// Separar por comas

                    if (esCabecera) {
                        for (String cabecera : valores) {
                            cabeceras.add(cabecera.trim());												//separar la cabezera
                        }
                        esCabecera = false;
                    } else {
                        ObjetoPOJO pojo = new ObjetoPOJO();
                        int indice = 0;
                        for (String valor : valores) {
                            pojo.setPropiedad(cabeceras.toArray(new String[0])[indice], valor.trim());	//guardar los datos del CSV
                            indice++;
                        }
                        datos.add(pojo);																//añadir los datos al POJO para su lectura
                    }
                }
            }

            // Pasar los datos al JSP
            request.setAttribute("datos", datos);
            request.setAttribute("cabeceras", cabeceras);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	public static void escrituraCSV(HttpServletRequest request, HttpServletResponse response) {
	    try {
	        String[] datos = request.getParameterValues("dato");	//recoger los datos del JSP        

	        String publicationDate = datos[0].trim(); 				//organizar los datos por cada columna
	        String value = datos[1].trim();
	        String magnitud = datos[2].trim();
	        String estado = datos[3].trim();
	        String estacion = datos[4].trim();
	        String periodo = datos[5].trim();

	        String nuevaLinea = publicationDate + ";" 				// introducir en una sola linea todas las columnas en formato CSV
	                          + value + ";" 
	                          + magnitud + ";" 
	                          + estado + ";" 
	                          + estacion + ";" 
	                          + periodo;

	        File file = new File(ServletFich.class.getClassLoader().getResource("calidad-aire.csv").getFile()); 				//ruta del archivo CSV

	        try (FileWriter fw = new FileWriter(file, true);		//hacer que el CSV se pueda escribir en el
	             BufferedWriter bw = new BufferedWriter(fw)) {
	            bw.write(nuevaLinea);								//escribir la linea con todos los datos
	            bw.newLine();										//pasar de linea
	        }

	        lecturaCSV(request);

		// Abrir el archivo en el equipo
	     	if (java.awt.Desktop.isDesktopSupported()) {
	     		java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
	     		desktop.open(file);
	     	}
		    
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
