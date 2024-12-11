package com.gf.RETO1_ManejoFicheros.entities;

import java.util.HashMap;
import java.util.Map;

public class ObjetoPOJO {
	  private String uri; // Identificador único del objeto RDF
	  private Map<String, String> propiedades; // Mapa con todas las propiedades del objeto
	    
	    // Constructor RDF
	    public ObjetoPOJO(String uri) {
	        this.uri = uri;
	        this.propiedades = new HashMap<>();
	    }
	    
	    // Constructor para los demás archivos
	    public ObjetoPOJO() {
	        this.propiedades = new HashMap<>();
	    }

	    // Getters y Setters
	    public String getUri() {
	        return uri;
	    }

	    public void setUri(String uri) {
	        this.uri = uri;
	    }

	    public Map<String, String> getPropiedades() {
	        return propiedades;
	    }

	    public void setPropiedad(String clave, String valor) {
	        this.propiedades.put(clave, valor);
	    }
	    
}
