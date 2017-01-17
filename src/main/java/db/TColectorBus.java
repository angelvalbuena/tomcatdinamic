package db;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger ; //Libreria para utilizar numero incremental atomico

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import core.BusesRT;
import core.Fecha;

public class TColectorBus {

	private static DBColector mongo;
	private static String fecha;
	private final static int maxEntradas = 2500; // Tamano de la ventana de ingreso a la base de datos
	private static final String nombreColeccion = "HistoBuses";
	private static AtomicInteger incremento = new AtomicInteger(0); // Numero incremental atomico iniciado desde cero

	public static boolean regDiarioBusesold(DBObject data, String placa) {
		mongo = new DBColector();
		BasicDBObject nuevaData, dataARemplazar;
		fecha = Fecha.getFechaClass().getYMD();
		DBObject consulta;
		dataARemplazar = new BasicDBObject("Bus", placa);
		nuevaData = new BasicDBObject("Bus", placa);
		ArrayList<DBObject> fechas = new ArrayList<>();
		ArrayList<DBObject> registros = new ArrayList<>();
		consulta = mongo.consultarMDB(nombreColeccion, dataARemplazar);
		if (consulta != null) {
			fechas = (ArrayList<DBObject>) consulta.get(fecha);

			if (fechas == null) {

				nuevaData = (BasicDBObject) consulta;
				registros.add(data);
				nuevaData.append(fecha, registros);
				mongo.actualizarMDB(nombreColeccion, nuevaData, dataARemplazar);

			} else {
				registros = (ArrayList<DBObject>) consulta.get(fecha);
				registros.add(data);
				nuevaData = (BasicDBObject) consulta;
				nuevaData.replace(fecha, registros);
				mongo.actualizarMDB(nombreColeccion, nuevaData, dataARemplazar);
			}

		} else { // No encontro el historico
			System.out.println("Error El historico para " + dataARemplazar + "no ha sido encontrado");
		}

		return false;

	}

	/**
	 * Esta funcion debe estar sincronizada para que no se pierdan datos en el hashmap entradas
	 * @param data
	 * @param placa
	 * @return
	 */
	public synchronized static boolean regDiarioBuses(BasicDBObject data, String placa) {
		int Nentradas = incremento.incrementAndGet(); // Incrementa y devuelve el numero atomico
		System.out.println("Entrada nro: " + incremento);
		BusesRT.getBusesRT().getRegistros().get(placa).add(data);
		if (Nentradas % maxEntradas == 0) {
			// Aca se hace la insercion a la base de datos
			insertarADB();

		}
		return true;

	}
	
	private static void insertarADB()
	{
		ArrayList<String> placas = BusesRT.getBusesRT().getBuses();
		for (int x = 0 ; x<placas.size();x++)
		{
			mongo = new DBColector();
			BasicDBObject nuevaData = new BasicDBObject();
			BasicDBObject dataARemplazar = new BasicDBObject("Bus", placas.get(x)); // Consulta con cada placa
			DBObject consulta = mongo.consultarMDB(nombreColeccion, dataARemplazar);
			ArrayList<BasicDBObject> arregloAnterior = (ArrayList<BasicDBObject>) consulta.get(Fecha.getFechaClass().getYMD());
			if (arregloAnterior == null) {
				arregloAnterior = new ArrayList<>();
				consulta.put(Fecha.getFechaClass().getYMD(), arregloAnterior);
			}
			ArrayList<BasicDBObject> atributos = BusesRT.getBusesRT().getRegistros().get(placas.get(x));
		
				for (int y = 0; y<atributos.size();y++)
				{
					
				
						arregloAnterior.add(atributos.get(y)); // Se agrega el registro al arreglo viejo
				
				}
				
		
			nuevaData.append(Fecha.getFechaClass().getYMD(),arregloAnterior);
			mongo.actualizarMDB(nombreColeccion,nuevaData, dataARemplazar);
			BusesRT.getBusesRT().getRegistros().replace(placas.get(x),new ArrayList<>());
			mongo.cerrarConexion();
		}
	
	}

	public static void testear() {
		System.out.println(BusesRT.getBusesRT().getRegistros());
	}

	public static boolean crearHistoBus(BasicDBObject placa) {
		DBObject consulta;
		String Placa = placa.getString("Placa");
		BasicDBObject data;
		fecha = Fecha.getFechaClass().getYMD();
		mongo = new DBColector();
		data = new BasicDBObject("Bus", Placa);
		consulta = mongo.consultarMDB(nombreColeccion, data);
		ArrayList<BasicDBObject> registros = new ArrayList<>();
		ArrayList<ArrayList<BasicDBObject>> Fechas = new ArrayList<>();
		Fechas.add(registros);

		if (consulta == null) {
			data.append(fecha, Fechas);
			mongo.insertarMDB(nombreColeccion, data);
			mongo.cerrarConexion();
			return true;
		} else {

		}
		mongo.cerrarConexion();

		return true;
	}

}
