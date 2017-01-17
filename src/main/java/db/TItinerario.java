package db;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import core.Despacho;
import core.Fecha;

public class TItinerario {

	private static DBGeneralBRT mongo;
	private static final String nombreColeccion = "Itinerario";
	private static LinkedHashMap<String, String> horarioReal;

	public static boolean crearItinerario(String clave, String conductor, String bus, String recorrido,String horaSalidaEstimada) {

		DBObject consultaItinerario;
		String fecha;
		BasicDBObject data;
		clave = clave.toUpperCase();
		bus = bus.toUpperCase();
		recorrido = recorrido.toUpperCase();
		horarioReal = new LinkedHashMap<>();
		mongo = new DBGeneralBRT();
		data = new BasicDBObject("Clave", clave);
		consultaItinerario = mongo.consultarMDB(nombreColeccion, data);
		if (consultaItinerario == null) {
					fecha = Fecha.getFechaClass().getYMD();
					data.append("Conductor", conductor);
					data.append("Bus", bus );
					data.append("Recorrido",recorrido);
					data.append("HoraSalidaEstimada",horaSalidaEstimada);
					data.append("HoraSalidaReal", "");
					data.append("ProximaParada",1);
					data.append("Terminado", true);
					data.append("HorarioReal", horarioReal);
					data.append("Fecha",fecha);
					
					
					mongo.insertarMDB(nombreColeccion, data);
					return true;
				
		} else {
			System.out.println("Error: El elemento ya existe en la base de datos");
		}
		mongo.cerrarConexion();
		return false;
	}
	
public synchronized static boolean modificarProximaParada(String clave, int proximaParada) {
		
		DBObject consultaItinerario;
		BasicDBObject data,nuevaData,dataAReemplazar;
		clave = clave.toUpperCase();
		mongo = new DBGeneralBRT();
		data = new BasicDBObject("Clave", clave);
		dataAReemplazar = new BasicDBObject("Clave", clave);
		consultaItinerario = mongo.consultarMDB(nombreColeccion, data);
		
		if(consultaItinerario != null){
			nuevaData = new BasicDBObject("ProximaParada", proximaParada);
			mongo.actualizarMDB(nombreColeccion, nuevaData, dataAReemplazar);
			mongo.cerrarConexion();			
			return true;
		}else{
			System.out.println("Error: No se encontro el itinerario: " + clave + "  en la base de datos");
		}
		mongo.cerrarConexion();
		return false;
	}
	
public static boolean modificarTerminado(String clave, boolean estado) {
		
		DBObject consultaItinerario;
		BasicDBObject data,nuevaData,dataAReemplazar;
		clave = clave.toUpperCase();
		mongo = new DBGeneralBRT();
		data = new BasicDBObject("Clave", clave);
		dataAReemplazar = new BasicDBObject("Clave", clave);
		consultaItinerario = mongo.consultarMDB(nombreColeccion, data);
		
		if(consultaItinerario != null){
			nuevaData = new BasicDBObject("Terminado", estado);
			if (estado == true) // Runtime
			{
				//en caso de que terminen
				System.out.println("Termino: "+clave);
				Despacho.getDespacho().removerDeMapas(clave);
			}
			else
			{
				//en caso de que inicie
				System.out.println("Inicio: "+clave);
				Despacho.getDespacho().agregarItinerario(clave); // agrega el itinerario a los mapas cuando inicia
			}
			mongo.actualizarMDB(nombreColeccion, nuevaData, dataAReemplazar);
			mongo.cerrarConexion();			
			return true;
		}else{
			System.out.println("Error: No se encontro el itinerario: " + clave + "  en la base de datos");
		}
		mongo.cerrarConexion();
		return false;
	}

	public static boolean iniciarItinerario(String clave, String horaSalidaReal) {
		
		DBObject consultaItinerario;
		BasicDBObject data,nuevaData,dataAReemplazar;
		clave = clave.toUpperCase();
		horarioReal = new LinkedHashMap<>();
		LinkedHashMap<String,String> antLink;
		mongo = new DBGeneralBRT();
		data = new BasicDBObject("Clave", clave);
		dataAReemplazar = new BasicDBObject("Clave", clave);
		consultaItinerario = mongo.consultarMDB(nombreColeccion, data);
		
		if(consultaItinerario != null){
			BasicDBObject dataDeRecorrido = new BasicDBObject("Clave",consultaItinerario.get("Recorrido"));
			DBObject consultar1Parada = mongo.consultarMDB("Recorrido",dataDeRecorrido);
			antLink = (LinkedHashMap<String, String>) consultar1Parada.get("Horario");
			ArrayList<String> arregloAntLink= new ArrayList<>(antLink.keySet());
			horarioReal.put(arregloAntLink.get(0),horaSalidaReal);
			nuevaData = new BasicDBObject("HoraSalidaReal", horaSalidaReal);
			nuevaData.append("HorarioReal",horarioReal);
			mongo.actualizarMDB(nombreColeccion, nuevaData, dataAReemplazar);
			mongo.cerrarConexion();	
			modificarTerminado(clave, false);
			modificarProximaParada(clave, 1);
			return true;
		}else{
			System.out.println("Error: No se encontro el itinerario: " + clave + "  en la base de datos");
		}
		mongo.cerrarConexion();
		return false;
	}
	
public synchronized static boolean marcarHora(String clave, LinkedHashMap<String, String> horarioReal) {
		
		DBObject consultaItinerario;
		BasicDBObject data,nuevaData,dataAReemplazar;
		clave = clave.toUpperCase();
		mongo = new DBGeneralBRT();
		data = new BasicDBObject("Clave", clave);
		dataAReemplazar = new BasicDBObject("Clave", clave);
		consultaItinerario = mongo.consultarMDB(nombreColeccion, data);
		
		if(consultaItinerario != null){
			nuevaData = new BasicDBObject("HorarioReal", horarioReal);
			mongo.actualizarMDB(nombreColeccion, nuevaData, dataAReemplazar);
			mongo.cerrarConexion();			
			return true;
		}else{
			System.out.println("Error: No se encontro el itinerario: " + clave + "  en la base de datos");
		}
		mongo.cerrarConexion();
		return false;
	}
	
public static boolean eliminarItinerario(String clave) {

	
	BasicDBObject data;
	modificarTerminado(clave,true);
	clave = clave.toUpperCase();
	mongo = new DBGeneralBRT();
	data = new BasicDBObject("Clave", clave);
	boolean elimino = mongo.eliminarMDB(nombreColeccion, data);
	if (elimino == true) {
		System.out.println("Se ha eliminado el itinerario " + clave);
		
	} else {
		System.out.println(
				"No se ha podido eliminar el itinerario " + clave + " Por que  no existe en la base de datos ");
	}
	mongo.cerrarConexion();
	return elimino;
}
}
