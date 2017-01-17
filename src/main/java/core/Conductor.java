package core;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import db.DBPersonal;

public class Conductor {

	private String cedula;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;	
	private String licencia;
	private	String tipoSangre;
	private final String coleccion = "Conductores";
	
	
	public Conductor(String cedula) {
		this.cedula = cedula;
		DBPersonal mongo = new DBPersonal();
		BasicDBObject data = new BasicDBObject("Cedula", cedula);
		DBObject consulta = mongo.consultarMDB(coleccion,data);
		this.primerNombre = (String) consulta.get("Primer Nombre");
		this.segundoNombre = (String) consulta.get("Segundo Nombre");
		this.primerApellido = (String) consulta.get("Primer Apellido");
		this.segundoApellido = (String) consulta.get("Segundo Apellido");
		this.licencia = (String) consulta.get("Numero de Licencia");
		this.tipoSangre = (String)consulta.get("Grupo Sanguineo");
	}


	public String getLicencia() {
		return licencia;
	}


	public void setLicencia(String licencia) {
		this.licencia = licencia;
	}


	public String getTipoSangre() {
		return tipoSangre;
	}


	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}


	public String getCedula() {
		return cedula;
	}


	public String getPrimerNombre() {
		return primerNombre;
	}


	public String getSegundoNombre() {
		return segundoNombre;
	}


	public String getPrimerApellido() {
		return primerApellido;
	}


	public String getSegundoApellido() {
		return segundoApellido;
	}
	
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}


	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}


	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}


	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}


	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}


	
	
	
	
	
	
}
