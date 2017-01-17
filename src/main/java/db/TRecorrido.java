package db;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import core.Bus;
import core.Coordenadas;
import utilidad.FormatearDatos;
import utilidad.GeoMatematicas;

public class TRecorrido {

	private static DBGeneralBRT mongo;
	private static final String nombreColeccion = "Recorrido";
	private static final String colleccionRuta = "Ruta";
	private static String horaInicial;
	private static String horaFinal;
	private static double velMed = Bus.getVelMed(); // MetrosPorSegundo
																	

	public static boolean crearRecorridoAutomatico(String clave, String ruta, String horaDePartida) {

		DBObject consultaRecorrido, consultarRuta;
		BasicDBObject data, rutaDB;
		clave = clave.toUpperCase();
		ruta = ruta.toUpperCase();
		mongo = new DBGeneralBRT();
		data = new BasicDBObject("Clave", clave);
		rutaDB = new BasicDBObject("Nombre", ruta);
		consultarRuta = mongo.consultarMDB(colleccionRuta, rutaDB);
		consultaRecorrido = mongo.consultarMDB(nombreColeccion, data);
		if (consultaRecorrido == null) {
			if (consultarRuta != null) {
				ArrayList<BasicDBObject> temp = (ArrayList<BasicDBObject>) consultarRuta.get("Ruta");
				if (temp.size() != 0) {
					data.append("Ruta", ruta);
					data.append("HoraPartida", horaDePartida);
					data.append("Horario", construirHorario(consultarRuta, horaDePartida));
					System.out.println(data.toString());
					mongo.insertarMDB(nombreColeccion, data);
				}
				else
				{
					System.out.println("Error: No se puede crear un recorrido de una ruta sin paradas");
				}
				mongo.cerrarConexion();
				return true;
			} else {
				System.out.println("Error: No se le puede crear un recorrido a una ruta inexistente");
			}
		} else {
			System.out.println("Error: El elemento ya existe en la base de datos");
		}
		mongo.cerrarConexion();
		return false;
	}

	/**
	 * Si existe el recorrido intenta encontrar una pareja coincidente
	 * "(parada,horaAnterior)" en el hashmap y edita su hora a una hora nueva
	 * 
	 * @param clave
	 * @param parada
	 * @param horaAnterior
	 * @param horaNueva
	 * @return {@link Boolean}
	 */
	public static boolean editarHoraRecorrido(String clave, String parada, String horaAnterior, String horaNueva) {
		DBObject recorrido;
		boolean remplazado = false;
		clave = clave.toUpperCase();
		BasicDBObject nuevaData, dataARemplazar;
		nuevaData = new BasicDBObject("Clave", clave);
		dataARemplazar = new BasicDBObject("Clave", clave);
		mongo = new DBGeneralBRT();
		recorrido = mongo.consultarMDB(nombreColeccion, nuevaData);
		if (recorrido != null) {
			nuevaData.append("Ruta", recorrido.get("Ruta"));
			nuevaData.append("HoraPartida", recorrido.get("HoraPartida"));
			LinkedHashMap<String, String> horario = null;
			horario = (LinkedHashMap<String, String>) recorrido.get("Horario");
			remplazado = horario.replace(parada, horaAnterior, horaNueva);
			nuevaData.append("Horario", horario);
			mongo.actualizarMDB(nombreColeccion, nuevaData, dataARemplazar);
			if (remplazado != true) {
				System.out.println("Error: Edicion insactifactoria. No se encontro la pareja especificada parada:"
						+ parada + " horaAnterior:" + horaAnterior);
			}
		} else {
			System.out.println("Error: No se encontro la ruta " + clave + " en la base de datos");
		}

		mongo.cerrarConexion();
		return remplazado;
	}

	/**
	 * Se encarga de hacer una estimacion del tiempo que se tarda un bus al
	 * desplazarse entre dos paradas.
	 * 
	 * @return {@link LinkedHashMap} devuelve el mapa de datos ordenado que
	 *         contiene la pareja {parada,tiempo}
	 */
	public static LinkedHashMap<String, String> construirHorario(DBObject consultarRuta, String horaDePartida) {
		int cantParadas;
		String horaAcumulada = horaDePartida;
		BasicDBObject anterior, actual;
		horaInicial = horaDePartida;
		LinkedHashMap<String, String> recorrido = new LinkedHashMap<>();
		ArrayList<BasicDBObject> paradas = new ArrayList<>();

		paradas = (ArrayList<BasicDBObject>) consultarRuta.get("Ruta");
		cantParadas = paradas.size();
		anterior = paradas.get(0);
		recorrido.put(anterior.getString("Clave"), horaDePartida);
		Coordenadas coorAnt, coorAct;
		double tiempo = 0;
		coorAnt = null;
		coorAct = null;
		for (int i = 1; i < cantParadas; i++) {
			BasicDBObject temp;
			actual = paradas.get(i);
			temp = (BasicDBObject) anterior.get("Coordenada");
			coorAnt = new Coordenadas((double) temp.getDouble("Latitud"), (double) temp.getDouble("Longitud"));
			temp = (BasicDBObject) actual.get("Coordenada");
			coorAct = new Coordenadas((double) temp.getDouble("Latitud"), (double) temp.getDouble("Longitud"));
			tiempo = GeoMatematicas.hallarTiempo(GeoMatematicas.calcDistancia(coorAnt, coorAct), velMed);
			tiempo = tiempo + FormatearDatos.removerFormatoDeTiempo(horaAcumulada);
			horaAcumulada = FormatearDatos.formatoDeTiempo(tiempo);
			recorrido.put(actual.getString("Clave"), "" + horaAcumulada);
			anterior = paradas.get(i);
		}

		horaFinal = horaAcumulada;

		return recorrido;

	}
	public static boolean eliminarRecorrido(String clave) {

		
		BasicDBObject data;
		clave = clave.toUpperCase();
		mongo = new DBGeneralBRT();
		data = new BasicDBObject("Clave", clave);
		boolean elimino = mongo.eliminarMDB(nombreColeccion, data);
		if (elimino == true) {
			System.out.println("Se ha eliminado el recorrido " + clave);
		} else {
			System.out.println(
					"No se ha podido eliminar el recorrido " + clave + " Por que  no existe en la base de datos ");
		}
		mongo.cerrarConexion();
		return false;
	}
	
}
