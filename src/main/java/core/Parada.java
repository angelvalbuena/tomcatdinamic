package core;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import utilidad.AreaEstacion;

public class Parada {

	private String clave;
	private String nombre;
	private Coordenadas coordenada;
	private AreaEstacion geoArea;
	
	public Parada(String clave,String nombre,Double lat,Double lng)
	{
		this.clave = clave;
		this.nombre=nombre;
		coordenada=new Coordenadas(lat,lng);
		geoArea = new AreaEstacion(80);//Queda por definir el area de las cercas
	}

	public String getClave() {
		return clave;
	}
	
	/**
	 * Metodo que permite conocer si el bus esta dentro del area de esta parada
	 * @param bus
	 * @return
	 */
	public boolean estaDentro(Coordenadas bus)
	{
	
		return geoArea.estaDentro(coordenada, bus);
	}
	

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Coordenadas getCoordenada() {
		return coordenada;
	}

	public void setCoordenada(Coordenadas coordenada) {
		this.coordenada = coordenada;
	}
	
	public BasicDBObject getJsonParada()
	{
		BasicDBObject paradaJson = new BasicDBObject();
		paradaJson.append("clave", clave);
		paradaJson.append("nombre", nombre);
		paradaJson.append("coordenada",coordenada.getBsonCoordenada());
		return paradaJson;
	}
	
	
}
