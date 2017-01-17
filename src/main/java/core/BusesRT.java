package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import db.DBGeneralBRT;

public class BusesRT {

	private HashMap<String, Bus> busesHashMap;
	private static BusesRT p;
	private HashMap<String, ArrayList<BasicDBObject>> registros;
	private final String coleccion = "Bus";

	private BusesRT() {
		busesHashMap = new LinkedHashMap<>();
		registros = new LinkedHashMap<>();
		inicializarParque();
		
	}

	

	public HashMap<String, Bus> getBusesHashMap() {
		return busesHashMap;
	}



	private void inicializarParque() {
		DBGeneralBRT conexion = new DBGeneralBRT();
		DBCollection collection = conexion.consultarColeccion(coleccion);
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject) cursor.next();
			busesHashMap.put((String) obj.get("Placa"), new Bus((String) obj.get("Placa")));
		}
	}
	
	private void iniciarMapa() {
		System.out.println("ME CREO SOLO UNA VEZ");
		ArrayList<String>placas = p.getBuses();
		for (int x = 0; x < placas.size(); x++) {
			registros.put(placas.get(x), new ArrayList<BasicDBObject>());
		}

	}
	


	public static BusesRT getBusesRT() {
		if (p == null) {
			p = new BusesRT();
			p.iniciarMapa();
		}

		return p;

	}
	

	
	public  HashMap<String, ArrayList<BasicDBObject>> getRegistros() {
		return registros;
	}



	//Devuelve un arreglo de placas
	public ArrayList<String> getBuses() {
		
		return new ArrayList<>(p.getBusesHashMap().keySet());
	}

	public Bus encontrarBus(String placa) {
		return busesHashMap.get(placa);
	}

	/**
	 * Encuentra un bus en el arreglo y modifica su estado
	 * 
	 * @param placa
	 * @param estado
	 */
	public void modificarEstado(String placa, boolean estado) {
		Bus encontrado = encontrarBus(placa);
		if (encontrado != null) {
			encontrado.setEstado(estado);
		}

	}

	/**
	 * Agrega un nuevo bus al arreglo solo si no existe previamente
	 * 
	 * @param bus
	 */

	public void agregarNuevo(Bus bus) {

		if (encontrarBus(bus.getPlaca()) == null) {
			busesHashMap.put(bus.getPlaca(), bus);
			registros.put(bus.getPlaca(),new ArrayList<BasicDBObject>());
		}

	}

	/**
	 * Encuentra un bus y lo elimina del arreglo.
	 * 
	 * @param placa
	 * @return
	 */

	public boolean eliminarBus(String placa) {
		Bus estado = busesHashMap.remove(placa);
		if (estado != null) {
			registros.remove(placa);
			return true;
		}
		return false;
	}


}
