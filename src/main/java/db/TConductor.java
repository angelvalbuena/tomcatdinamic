package db;

import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import core.ConductoresRT;
import utilidad.Diccionario;
import utilidad.FormatearDatos;

public class TConductor {

	private static DBPersonal mongo;
	private static final String nombreColeccion = "Conductores";

	public static boolean crearConductor(String cedula, String primerNombre, String segundoNombre, String primerApellido,
			String segundoApellido, String licencia, String tipoSangre) {

		primerNombre = FormatearDatos.mayusInicial(primerNombre);
		segundoNombre = FormatearDatos.mayusInicial(segundoNombre);
		primerApellido = FormatearDatos.mayusInicial(primerApellido);
		segundoApellido = FormatearDatos.mayusInicial(segundoApellido);
		tipoSangre = tipoSangre.toUpperCase();

		BasicDBObject data;
		DBObject consulta;
		mongo = new DBPersonal();
		data = new BasicDBObject("Cedula", cedula);
		consulta = mongo.consultarMDB(nombreColeccion, data);
		if (consulta == null) {

			data.append("Primer Nombre", primerNombre).append("Segundo Nombre", segundoNombre)
					.append("Primer Apellido", primerApellido).append("Segundo Apellido", segundoApellido)
					.append("Numero de Licencia", licencia).append("Grupo Sanguineo", tipoSangre);
			mongo.insertarMDB(nombreColeccion, data);
			mongo.cerrarConexion();
			return true;
		} else {
			System.out.println("Error: El conductor con cedula: " + cedula + " ya existe en la base de datos");

		}
		mongo.cerrarConexion();
		return false;
	}

	public static boolean modificarDatoConductor(String cedula, String dato, String nuevo) {
		DBObject conductor;
		dato = dato.toUpperCase();
		BasicDBObject nuevaData, dataAReemplazar;
		String atributo = dato;

		mongo = new DBPersonal();
		dataAReemplazar = new BasicDBObject("Cedula", cedula);
		conductor = mongo.consultarMDB(nombreColeccion, dataAReemplazar);
		if (conductor != null) {
			nuevaData = new BasicDBObject(atributo, nuevo);
			mongo.actualizarMDB(nombreColeccion, nuevaData, dataAReemplazar);
			return true;
		} else {
			System.out.println("Error: No se encontro el bus con placa: " + cedula + "  en la base de datos");

		}
		mongo.cerrarConexion();
		return false;

	}

	public static boolean eliminarConductor(String cedula) {

		boolean elimino;
		mongo = new DBPersonal();
		BasicDBObject cedulaAEliminar = new BasicDBObject("Cedula", cedula);
		elimino = mongo.eliminarMDB(nombreColeccion, cedulaAEliminar);
		mongo.cerrarConexion();
		if (elimino == true) {
			System.out.println("Se elimino el conductor con la cedula" + cedula);

			return true;
		} else {
			System.out.println("Error: El conductor con cedula: " + cedula + " ya existe en la base de datos");

			return false;
		}

	}

	
}
