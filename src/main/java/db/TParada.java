package db;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import utilidad.FormatearDatos;

public class TParada {
	
	private static DBGeneralBRT mongo;
	private static final String nombreColeccion = "Parada";

	/**
	 * Este metodo permite insertar una parada a la base de datos con su latitud
	 * y longitud.
	 * 
	 * @param nombre
	 * @param latitud
	 * @param longitud
	 */
	public static boolean crearParada(String clave, String nombre, double latitud, double longitud) {

		DBObject consulta, consulta2;
		BasicDBObject data, consNombre;
		clave = clave.toUpperCase();
		nombre = FormatearDatos.mayusInicialMulti(nombre);
		mongo = new DBGeneralBRT();
		data = new BasicDBObject("Clave", clave);
		consNombre = new BasicDBObject("Nombre", nombre);
		consulta = mongo.consultarMDB(nombreColeccion, data);
		consulta2 = mongo.consultarMDB(nombreColeccion, consNombre);
		if (consulta == null) {
			if (consulta2 == null) {
				data = new BasicDBObject("Clave", clave).append("Nombre", nombre).append("Coordenada",
						new BasicDBObject("Latitud", latitud).append("Longitud", longitud));
				mongo.insertarMDB(nombreColeccion, data);
				mongo.cerrarConexion();
				return true;
			} else {
				System.out.println("Error: Ya hay un elemento existente en la base de datos con el mismo nombre");
			}
		} else {
			System.out.println("Error: Ya hay un elemento existente en la base de datos con el mismo codigo");
		}
		return false;
	}

	public static boolean eliminarParada(String parada) {
		boolean elimino;
		parada = parada.toUpperCase();
		mongo = new DBGeneralBRT();
		BasicDBObject paradaAEliminar = new BasicDBObject("Clave", parada);
		elimino = mongo.eliminarMDB(nombreColeccion, paradaAEliminar);
		if (elimino == true) {
			System.out.println("Se ha eliminado la parada " + parada);
		} else {
			System.out.println(
					"No se ha podido eliminar la parada " + parada + " Por que  no existe en" + "la base de datos ");
		}
		mongo.cerrarConexion();
		return elimino;

	}
	

}
