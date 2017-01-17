package core;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import db.DBGeneralBRT;
import db.TRecorrido;

public class Recorrido {
	
	private String claveRecorrido;
	private Ruta ruta;
	private String horaPartida;
	private String horaFinalizacion;
	private LinkedHashMap<String,String> horario;
	
	public Recorrido(String claveRecorrido) {		
		
		this.claveRecorrido = claveRecorrido;
		BasicDBObject recorridoDB = new BasicDBObject("Clave", claveRecorrido);
		DBGeneralBRT mongo = new DBGeneralBRT();
		DBObject recorrido = mongo.consultarMDB("Recorrido", recorridoDB);
		ruta = new Ruta((String) recorrido.get("Ruta"));
		horario=(LinkedHashMap<String, String>) recorrido.get("Horario");
		this.horaPartida = getHoraPorIndice(0);
		this.horaFinalizacion = getHoraPorIndice(horario.keySet().toArray().length-1);
		mongo.cerrarConexion();
	}

	public String getHoraPartida() {
		return horaPartida;
	}
	
	public String getHoraFinalizacion()
	{
		return horaFinalizacion;
	}
	
	public boolean editarHorario(String parada, String horaAnterior, String horaNueva)
	{
		return horario.replace(parada, horaAnterior, horaNueva);
	}
	
	/**
	 * Metodo encargado de obtener una parada directamente desde la ruta con todos sus atributos.
	 * @param i
	 * @return
	 */
	public Parada getObjetoPadadaPorIndice(int i)
	{
		return ruta.getParadas().get(i);
	}
	
	//Metodo de pruebas para comprobar valores
	public void mostarHorario()
	{
		Object[] keys=horario.keySet().toArray();
	    for (int i=0;i<keys.length;i++)System.out.println(getClaveParadaPorIndice(i)+" "+getHoraPorIndice(i));
	}
	
	//Inicia la pareja de metodos encargada de obtener valores del hashmap de horario
	public String getHoraPorIndice(int i) {
		return horario.get(horario.keySet().toArray()[i]);
	}
	
	public String getClaveParadaPorIndice(int i){
		Object[] keys=horario.keySet().toArray();
		return (String)keys[i];
	}
	//Terimina el la obtencion del horario

	public void setHoraPartida(String horaPartida) {
		this.horaPartida = horaPartida;
	}

	public String getClaveRecorrido() {
		return claveRecorrido;
	}

	public Ruta getRuta() {
		return ruta;
	}

	public LinkedHashMap<String, String> getHorario() {
		return horario;
	}
	
	

}
