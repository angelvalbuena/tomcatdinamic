package db;

import com.mongodb.BasicDBObject;

import com.mongodb.DBObject;


public class BusDB {
	
	private BasicDBObject placa;
	private int capacidad;
	private String tipoBus;
	private boolean estado;
	private String operador;

	private String nombreColeccion;
	private DBGeneralBRT mongo;
	private DBObject datos;
	
	
	public BusDB(String placa)
	{
		
		this.nombreColeccion="Bus";
		this.placa = new BasicDBObject("Placa",placa);
	}

	public boolean valoresBaseDatos()
	{
		mongo = new DBGeneralBRT();
		
		datos = mongo.consultarMDB(nombreColeccion,placa);
		if (datos!=null)
		{
			capacidad = (int) datos.get("Capacidad");
			tipoBus = (String) datos.get("TipoBus");
			estado = (boolean) datos.get("Estado");
			operador = (String) datos.get("Operador");
			
			
				TColectorBus.crearHistoBus(placa);
			
			
			mongo.cerrarConexion();
			
			return true; 
		}
		else
		{
			return false;
		}
	}

	

	public int getCapacidad() {
		return capacidad;
	}

	public String getTipoBus() {
		return tipoBus;
	}

	public boolean isEstado() {
		return estado;
	}

	public String getOperador() {
		return operador;
	}
	
	

}