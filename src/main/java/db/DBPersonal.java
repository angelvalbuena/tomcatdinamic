package db;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class DBPersonal {
	private MongoClient mongo;
	private DBCollection Colleccion;
	private DB db;
	private final String DB = "Personal";

	public DBPersonal() {

		try {
			
			mongo = new MongoClient();
			mongo.getAddress();

		} catch (UnknownHostException UKHe) {

			System.out.println("Error: Base de datos desconocida");
		}

	}

	public synchronized DBObject consultarMDB(String Collection, BasicDBObject clave) {

		DBCursor encontrar;
		// Si no existe la base de datos la crea
		db = mongo.getDB(DB);
		// Crea una tabla si no existe y agrega datos
		Colleccion = db.getCollection(Collection);
		encontrar = Colleccion.find(clave);
		if (encontrar.hasNext()) {
			// devuelve el objeto DBObject si el cursor tiene un elemento
			// siguiente (analogia con null)
			return encontrar.next();
		} else {
			// no encontro el objeto
			return null;
		}
	}

	public synchronized DBCollection consultarColeccion(String Collection) {

		DBCursor encontrar;
		// Si no existe la base de datos la crea
		db = mongo.getDB(DB);
		// Crea una tabla si no existe y agrega datos
		Colleccion = db.getCollection(Collection);
		
		return Colleccion;
		
	}

	public synchronized void  insertarMDB(String Collection, BasicDBObject Document) {

		// Si no existe la base de datos la crea
		db = mongo.getDB(DB);
		// Crea una tabla si no existe y agrega datos
		Colleccion = db.getCollection(Collection);
		// Inserta Documento a la base de datos
		Colleccion.insert(Document);

	}

	public synchronized void actualizarMDB(String Collection, BasicDBObject DocToChange, BasicDBObject IdDoc) {

		// Si no existe la base de datos la crea
		db = mongo.getDB(DB);
		// Crea una tabla si no existe y agrega datos
		Colleccion = db.getCollection(Collection);
		// Dato que se desea actualizar
		BasicDBObject ActualizarDato = new BasicDBObject();
		ActualizarDato.append("$set", DocToChange);

		// Documento en el cual se desea actualizar el dato
		BasicDBObject searchById = IdDoc;

		Colleccion.update(searchById, ActualizarDato);

	}

	public synchronized boolean eliminarMDB(String Collection, BasicDBObject clave) {
		db = mongo.getDB(DB);
		Colleccion = db.getCollection(Collection);
		WriteResult resultado = Colleccion.remove(clave);
		if (resultado.getN() == 0){
			return false;
		}else{
			return true;
		}
		
	}

	/**
	 * Clase que imprime por pantalla todas las bases de datos MongoDB.
	 * 
	 * @param mongo
	 *            conexion a MongoDB
	 */
	private static void printDatabases(MongoClient mongo) {
		List<String> dbs = mongo.getDatabaseNames();
		for (String db : dbs) {
			System.out.println(" - " + db);
		}
	}

	public void cerrarConexion() {
		mongo.close();
	}

}
