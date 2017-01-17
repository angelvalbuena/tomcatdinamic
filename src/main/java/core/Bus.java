package core;

import javax.json.Json;
import javax.json.JsonObject;

import com.mongodb.BasicDBObject;

import db.BusDB;
import interfaces.Observer;
import interfaces.Subject;
import utilidad.FormatearDatos;
import utilidad.MensajeError;

public class Bus implements Observer{
	// Datos del bus
	private String placa;
	private int capacidad;
	private String tipoBus;
	public String getTipoBus() {
		return tipoBus;
	}

	private boolean estado;
	private Coordenadas coor;
	private BusDB busDB;
	private int proximaParada;
	private String operador;
	//Objetos de json
	private BasicDBObject JsonDatos;
	private static double velMed = FormatearDatos.kmxhTomxs(35);

	public Bus(String placa , Coordenadas coor){
		
		this.placa = placa;
		this.coor = coor;
		//valores default en caso de pedir el JSON sin la existencia del objeto en la BD
		this.tipoBus="";
		this.capacidad=0;
		this.estado=false;
		busDB = new BusDB(placa);
		proximaParada = 1;
		actualizarBusDesdeBD();
	}
	
	public int getCapacidad() {
		return capacidad;
	}

	public Bus(String placa)
	{
		//Constructor de buses runtime.
		coor = new Coordenadas(0,0);
		this.placa = placa;
		this.tipoBus="";
		this.capacidad=0;
		this.estado=false;
		busDB = new BusDB(placa);
		proximaParada = 1;
		actualizarBusDesdeBD();
	}
	
	public void actualizarBusDesdeBD()
	{
		if (busDB.valoresBaseDatos())
		{
			this.tipoBus=busDB.getTipoBus();
			this.capacidad=busDB.getCapacidad();
			this.estado=busDB.isEstado();
			this.operador=busDB.getOperador();
		}
	}
	
	public void actualizarJSON()
	{
		/*JsonDatos = Json.createObjectBuilder().add("placa", this.placa)
				.add("coordenada" , 
						Json.createObjectBuilder()
						.add("latitud", coor.getLatitud())
						.add("longitud", coor.getLongitud()))
				.add("capacidad", capacidad)
				.add("tipoBus", tipoBus)
				.add("estado", estado).build();*/
		
		JsonDatos = new BasicDBObject("placa",this.placa);
		BasicDBObject coorBD = new BasicDBObject();
		coorBD.append("latitud",coor.getLatitud());
		coorBD.append("longitud",coor.getLongitud());
		JsonDatos.append("coordenada",coorBD);
		JsonDatos.append("capacidad",capacidad);
		JsonDatos.append("tipoBus",tipoBus);
		JsonDatos.append("estado",estado);
		JsonDatos.append("Operador",operador);
		
	}
	
	/**
	 * Metodo que crea y devuelve el JSON del objeto Bus
	 * @return JsonObject representacion JSON del bus.
	 */
	public BasicDBObject getJsonBus(){
		if (busDB.valoresBaseDatos())
		{
			actualizarJSON();
		}
		else
		{
			JsonDatos = MensajeError.denegar();
		}
		return JsonDatos;
	}
	
	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getPlaca() {
		return placa;
	}
	
	public void setEstado(boolean estado)
	{
		this.estado = estado;
	}
	
	public boolean getEstado()
	{
		return estado;
	}

	public Coordenadas getCoor() {
		return coor;
	}
	
	public void setCoor(Coordenadas coor)
	{
		this.coor=coor;
	}
	
	

	public int getProximaParada() {
		return proximaParada;
	}
	
	
	public void setProximaParada(int proximaParada) {
		this.proximaParada = proximaParada;
	}
	

	public String getOperador() {
		return operador;
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		System.out.println("El bus de placa:"+placa);
	}

	public static double getVelMed() {
		return velMed;
	}

	public static void setVelMed(double velMed) {
		Bus.velMed = velMed;
	}
	
	




	
	
	

}
