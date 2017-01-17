package core;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.google.common.collect.HashMultimap;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import db.DBGeneralBRT;
import utilidad.FormatearDatos;

/**
 * Clase encargada de administrar todos los itinerarios.
 * 
 * @author 123
 *
 */
public class Despacho {

	private static Despacho d = null;
	private LinkedHashMap<String,Itinerario> itinerariosXPlaca;
	private LinkedHashMap<String,Itinerario> itinerariosXId;
	private HashMultimap<String,Itinerario> itinerariosXRuta;
	private final String coleccion = "Itinerario";

	private Despacho() {

		inicializarDespacho();
	}

	private void inicializarDespacho() {
		// TODO Auto-generated method stub

		DBGeneralBRT conexion = new DBGeneralBRT();
		itinerariosXId = new LinkedHashMap<>();
		itinerariosXPlaca = new LinkedHashMap<>();
		itinerariosXRuta = HashMultimap.create();
		DBCollection collection = conexion.consultarColeccion(coleccion);
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject) cursor.next();
			Itinerario i = new Itinerario((String) obj.get("Clave"));
			if (i.getTerminado() == false) {
				String ruta;
				ruta=i.getRecorridoDesignado().getRuta().getNombre();
				itinerariosXRuta.put(ruta,i);
				itinerariosXPlaca.put(i.getBusDesignado().getPlaca(),i);
				itinerariosXId.put((String) obj.get("Clave"),i);
			}
		}
	}

	public static Despacho getDespacho() {
		if (d == null) {
			d = new Despacho();
			return d;
		} else {
			return d;
		}
	}
	
	public void removerDeMapas(String clave)
	{
		Itinerario t = itinerariosXId.remove(clave);
		System.out.println(t.getId());
		itinerariosXPlaca.remove(t.getBusDesignado().getPlaca());
		itinerariosXRuta.remove(t.getRecorridoDesignado().getRuta().getNombre(),t);
	}
	
	

	public void agregarItinerario(String clave) {
		Itinerario nuevo = new Itinerario(clave);
		itinerariosXPlaca.put(nuevo.getBusDesignado().getPlaca(),nuevo);
		itinerariosXId.put(nuevo.getId(),nuevo);
		itinerariosXRuta.put(nuevo.getRecorridoDesignado().getRuta().getNombre(),nuevo);
		System.out.println("XPLACA"+itinerariosXPlaca);
	}

	public ArrayList<Itinerario> getItinerarios() {
		return new ArrayList<Itinerario>(itinerariosXId.values());
	}

	public Itinerario encontrarItinerarioxID(String id) {
		return itinerariosXId.get(id);
	}

	/**
	 * Todos los itinerarios que contengan el bus con la placa dada
	 * 
	 * @return
	 */
	public Itinerario encontarXBus(String Placa) {
		return itinerariosXPlaca.get(Placa);
	}

	/**
	 * Busca todos los itinerarios que tengan la misma ruta solicitada
	 * 
	 * @param ruta
	 * @return
	 */
	public ArrayList<Itinerario> encontarXRuta(String ruta) {
			return new ArrayList<>(itinerariosXRuta.get(ruta));
		
	}

	public ArrayList<Itinerario> encontarXRecorrido(String recorrido) {
		return null;
	}



	/*public ArrayList<Itinerario> ordenarItinerarios(ArrayList<Itinerario> busqueda) {
		int tamano = busqueda.size();
		double[] entrada = new double[tamano];
		LinkedHashMap<Double,Itinerario> diccionario=new LinkedHashMap<>();
		int indice = 0;
		for (Itinerario b : busqueda) {
			
			entrada[indice] = FormatearDatos.removerFormatoDeTiempo(b.getRecorridoDesignado().getHoraPartida());
			diccionario.put(entrada[indice],b);
			indice++;
		}
		FormatearDatos.quickSort(0, entrada.length - 1, entrada);
		busqueda = new ArrayList<>();
		for (int i = 0; i < tamano; i++) {
			
			busqueda.add(diccionario.get(entrada[i]));
		}
		
		return busqueda;
	}*/

	/**
	 * Metodo de pruebas
	 */
	public void mostrarItinerarios() {
	}
}
