package db;

import java.util.ArrayList;
import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TRuta {
	
	private static DBGeneralBRT mongo;
	private static final String nombreColeccion = "Ruta";

	/**
	 * Crea una ruta nueva sin paradas. No permite que se creen rutas repetidas
	 * sin importar que se envien rutas en minuscula. Transforma de minusculas a
	 * mayusculas la entrada.
	 * 
	 * @param nombre
	 */
	public static boolean crearRuta(String nombre, String categoria, String descripcion) {
		String nombreMayus;
		nombreMayus = nombre.toUpperCase();
		DBObject consulta;
		BasicDBObject data;
		mongo = new DBGeneralBRT();
		data = new BasicDBObject("Nombre", nombreMayus);
		consulta = mongo.consultarMDB(nombreColeccion, data);
		ArrayList<BasicDBObject> paradas = new ArrayList<>();
		if (consulta == null) {
			data.append("Categoria", categoria);
			data.append("Descripcion", descripcion);
			data.append(nombreColeccion, paradas);
			mongo.insertarMDB(nombreColeccion, data);
			mongo.cerrarConexion();
			return true;
		} else {
			System.out.println(
					"Error: No se puede crear la ruta " + nombreMayus + ". Esta ya existe " + "en la base de datos");
			
		}
		mongo.cerrarConexion();
		return false;
	}
	
	
	public static boolean modificarDatoRuta(String nombre, String dato, String nuevo){
		nombre = nombre.toUpperCase();
		dato = dato.toUpperCase();
		DBObject ruta;
		BasicDBObject nuevaData, dataAReemplazar;
		HashMap<String, String> diccionario = new HashMap<>();
		diccionario.put("CAT", "Categoria");
		diccionario.put("DES", "Descripcion");
		mongo = new DBGeneralBRT();
		dataAReemplazar = new BasicDBObject("Nombre", nombre);
		ruta = mongo.consultarMDB(nombreColeccion, dataAReemplazar);
		if (ruta != null) {
			nuevaData = new BasicDBObject(diccionario.get(dato), nuevo);
			mongo.actualizarMDB(nombreColeccion, nuevaData, dataAReemplazar);
			mongo.cerrarConexion();
			return true;
			
		} else {
			System.out.println("Error: No se encontro el bus con placa: " + nombre + "  en la base de datos");

		}
		mongo.cerrarConexion();
		return false;
	}

	public static boolean anadirXPosicionARuta(String nombreRuta, String clave, int posicion) {
		DBObject ruta;
		DBObject parada;
		clave = clave.toUpperCase();
		nombreRuta = nombreRuta.toUpperCase();
		posicion = posicion - 1;
		ArrayList<DBObject> paradas = new ArrayList<>();
		BasicDBObject nuevaData, dataARemplazar;
		nuevaData = new BasicDBObject("Nombre", nombreRuta);
		dataARemplazar = new BasicDBObject("Nombre", nombreRuta);
		mongo = new DBGeneralBRT();
		ruta = mongo.consultarMDB(nombreColeccion, dataARemplazar);
		if (ruta != null) {
			paradas = (ArrayList<DBObject>) ruta.get(nombreColeccion);
			if (posicion >= 0 && posicion <= paradas.size()) {
				parada = mongo.consultarMDB("Parada", new BasicDBObject("Clave", clave));
				if (parada != null) {
					paradas.add(posicion, parada);
					nuevaData.append(nombreColeccion, paradas);
					mongo.actualizarMDB(nombreColeccion, nuevaData, dataARemplazar);
					mongo.cerrarConexion();
					return true;
				} else {
					System.out.println("Error: La parada no existe. Debe crear la parada " + clave
							+ " primero antes de anadirle elementos");
					
				}
			} else {
				System.out.println("Error: Ingrese una posicion entre: 1 y " + paradas.size());
				
			}
		} else {
			System.out.println("Error: La ruta " + nombreRuta + " a la que esta intentando anadir elementos no existe");
			
		}
		mongo.cerrarConexion();
		return false;
	}

	public static boolean anadirAFinalDeRuta(String nombreRuta, String clave) {
		DBObject ruta;
		DBObject parada;
		nombreRuta =nombreRuta.toUpperCase();
		clave = clave.toUpperCase();
		ArrayList<DBObject> paradas = new ArrayList<>();
		BasicDBObject nuevaData, dataARemplazar;
		nuevaData = new BasicDBObject("Nombre", nombreRuta);
		dataARemplazar = new BasicDBObject("Nombre", nombreRuta);
		mongo = new DBGeneralBRT();
		ruta = mongo.consultarMDB(nombreColeccion, dataARemplazar);
		if (ruta != null) {
			paradas = (ArrayList<DBObject>) ruta.get(nombreColeccion);
			parada = mongo.consultarMDB("Parada", new BasicDBObject("Clave", clave));
			if (parada != null) {
				paradas.add(parada);
				nuevaData.append(nombreColeccion, paradas);
				mongo.actualizarMDB(nombreColeccion, nuevaData, dataARemplazar);
				mongo.cerrarConexion();
				return true;
			} else {
				System.out.println("Error: La parada no existe. Debe crear la parada " + clave
						+ " primero antes de anadirle elementos");
				
				
			}
		} else {
			System.out.println("Error: La ruta no existe. Debe crear la ruta " + nombreRuta
					+ " primero antes de anadirle elementos");
		}
		mongo.cerrarConexion();
		return false;
		
	}

	
	public static boolean eliminarRuta(String ruta) {
		boolean elimino;
		ruta = ruta.toUpperCase();
		mongo = new DBGeneralBRT();
		BasicDBObject rutaAEliminar = new BasicDBObject("Nombre", ruta);
		elimino = mongo.eliminarMDB(nombreColeccion, rutaAEliminar);
		

		if (elimino == true) {
			System.out.println("Se ha eliminado la ruta " + ruta);
		} else {
			System.out.println(
					"No se ha podido eliminar la ruta " + ruta + " Por que  no existe en" + "la base de datos ");
		}
		mongo.cerrarConexion();
		return elimino;

	}

	public static boolean removerParadaDeRuta(String nombreRuta, int posicion) {
		DBObject ruta;
		nombreRuta = nombreRuta.toUpperCase();
		posicion = posicion - 1;
		ArrayList<DBObject> paradas = new ArrayList<>();
		BasicDBObject nuevaData, dataARemplazar;
		nuevaData = new BasicDBObject("Nombre", nombreRuta);
		dataARemplazar = new BasicDBObject("Nombre", nombreRuta);
		mongo = new DBGeneralBRT();
		ruta = mongo.consultarMDB(nombreColeccion, dataARemplazar);
		if (ruta != null) {
			paradas = (ArrayList<DBObject>) ruta.get(nombreColeccion);
			if (posicion >= 0 && posicion <= paradas.size()) {

				paradas.remove(posicion);
				nuevaData.append(nombreColeccion, paradas);
				mongo.actualizarMDB(nombreColeccion, nuevaData, dataARemplazar);
				mongo.cerrarConexion();
				return true;
			} else {
				System.out.println("Error: Ingrese una posicion entre: 1 y " + paradas.size());
				
			}
		} else {
			System.out.println("Error: La parada " + nombreRuta + " a la que esta intentando acceder no existe");
			
		}
		mongo.cerrarConexion();
		return false;
	}
	
	public static boolean reemplazarParadaDeRuta(String nombreRuta, String clave, int posicion){
		boolean estado1,estado2;
		
		estado1 = anadirXPosicionARuta(nombreRuta, clave, posicion);
		if(estado1 == true){
			System.out.println("se anadio");
		estado2 = removerParadaDeRuta(nombreRuta, posicion+1);
		if(estado2 == true){
			System.out.println("se removio el siguiente");
		}else{
			estado1 = false;
		}
		}else {
			System.out.println("fallo");
		}
			
		return estado1 ;
	}
	
	public static boolean eliminarParadas(String nombre)
	{
		String nombreMayus;
		nombreMayus = nombre.toUpperCase();
		DBObject consulta;
		BasicDBObject dataNueva,dataAnterior;
		mongo = new DBGeneralBRT();
		dataNueva = new BasicDBObject("Nombre", nombreMayus);
		dataAnterior = new BasicDBObject("Nombre",nombreMayus);
		consulta = mongo.consultarMDB(nombreColeccion, dataNueva);
		ArrayList<BasicDBObject> paradas = new ArrayList<>();
		if (consulta != null) {
			dataNueva.append(nombreColeccion, paradas);
			mongo.actualizarMDB(nombreColeccion, dataNueva , dataAnterior);
			mongo.cerrarConexion();
			return true;
		} else {
			System.out.println(
					"Error: No se puede eliminar las paradas de la ruta " + nombreMayus + ". Ruta inexistente en la base de datos");
		}
		mongo.cerrarConexion();
		return false;
	}

	

}
