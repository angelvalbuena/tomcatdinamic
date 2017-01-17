package andresgiovanni.metodos;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.google.common.collect.HashMultimap;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.util.JSON;

import core.Bus;
import core.BusesRT;
import core.Conductor;
import core.ConductoresRT;
import core.Despacho;
import core.Fecha;
import core.Itinerario;
import core.Parada;
import core.Recorrido;
import core.RecorridosRT;
import core.Ruta;
import db.DBGeneralBRT;
import db.TBus;
import db.TColectorBus;
import db.TConductor;
import db.TItinerario;
import db.TParada;
import db.TRecorrido;
import db.TRuta;
import utilidad.FormatearDatos;
import utilidad.GeoMatematicas;

/**
 * Esta es una clase que utilizaremos para probar el codigo antes de utilizarlo
 * con otras clases.
 * 
 * Mensaje para Angel: Angel por favor no use esta clase cuando descargue esta
 * version.
 * 
 * @author Carlos Andres Pereira Grimaldo
 * @author Jose Giovanni Florez Nocua
 *
 */
public class Test {
	private DBGeneralBRT conexion;

	public static void main(String[] args) {
		// System.out.println(Despacho.getDespacho().encontarXBus("ZOE101").get(0).getIndex());
		// System.out.println(Despacho.getDespacho().getItinerarios().size());
		// Despacho.getDespacho().Refrescar();
		// TRecorrido.crearRecorridoAutomatico("P8-1","P8","00:00:00");
		/*
		  TItinerario.crearItinerario("I1T3", "1098755547", "XDB725", "T3-2");
		  TItinerario.crearItinerario("I3T3", "1098755548", "XB536A", "T3-2");
		  TItinerario.crearItinerario("I2T3", "1098755549", "ZOE101", "T3-2");
		  TItinerario.crearItinerario("I4T3", "1098755550", "AMB123", "T3-2");
		 */
		/*HashMultimap<String,String> test = HashMultimap.create();
		test.put("p1", "A");
		test.put("p1", "B");
		test.put("p1", "C");
		test.put("p2", "D");
		test.put("p2", "E");
		test.put("p2", "F");
		ArrayList<String> s = new ArrayList<>(test.get("p2"));
		for (String t : s)
		{
			System.out.println(t);
		}*/
		long A = Fecha.getFechaClass().convtHoraToMlls("2017/01/08","00:00:00");
		long B = Fecha.getFechaClass().convtHoraToMlls("2017/01/08","00:15:12");
		long C = B-A;
		System.out.println(C);
		
		
		// TItinerario.iniciarItinerario("I1T3", "23:24:38");
		//TItinerario.modificarTerminado("I2T3",true);
		/*
		ArrayList<Itinerario> dito = Despacho.getDespacho().encontarXBus("ZOE101");
		if (dito != null) {
			for (Itinerario temp : dito) {
				System.out.println(temp.getRecorridoDesignado().getHoraPartida());
			}
		}
		else
		{
			System.out.println("No hay itinerarios cargados");
		}*/
		
		//Parada p = new Parada("ST1","EST. TEMPRANA",7.002608, -73.055068);
		//System.out.println(p.getJsonParada());
		
		/*TRuta.crearRuta("T3","Troncal","BGA/PIE");
		TRuta.anadirAFinalDeRuta("T3","ST1");
		TRuta.anadirAFinalDeRuta("T3","ST2");
		TRuta.anadirAFinalDeRuta("T3","ST3");
		TRuta.anadirAFinalDeRuta("T3","ST4");
		TRuta.anadirAFinalDeRuta("T3","ST5");*/
		
		//TRecorrido.crearRecorridoAutomatico("T3-8","T3","00:00:00");
		
		// Despacho.getDespacho().mostrarItinerarios();
		// Despacho.getDespacho().encontarXBus("ZOE101");
		/*
		 * TransaccionesParada.crearParada("ST1", "EST. TEMPRANA",7.002608,
		 * -73.055068); TransaccionesParada.crearParada("ST2", "EST. ESPANOLITA"
		 * , 7.017099, -73.057545); TransaccionesParada.crearParada("ST3",
		 * "EST. PALMICHAL", 7.038484, -73.073994);
		 * TransaccionesParada.crearParada("ST4", "EST. ESTANCIA", 7.137213,
		 * -73.122289); TransaccionesParada.crearParada("ST5", "EST. LAGOS",
		 * 7.066655, -73.099632); TransaccionesParada.crearParada("ST6",
		 * "EST. CA�AVERAL", 7.070660, -73.104938);
		 * TransaccionesParada.crearParada("ST7", "EST. PROVENZA", 7.090184,
		 * -73.108926); TransaccionesParada.crearParada("ST8", "EST. LA ROSITA",
		 * 7.112485, -73.121840); TransaccionesParada.crearParada("ST9",
		 * "EST. CHORRERAS", 7.116745, -73.125948);
		 * TransaccionesParada.crearParada("ST10", "EST. SAN MATEO", 7.118520,
		 * -73.126639); TransaccionesParada.crearParada("ST11",
		 * "EST. QUEBRADASECA", 7.122087, -73.128070);
		 * TransaccionesParada.crearParada("ST12", "EST. CAMPO ALEGRE",
		 * 7.023763, -73.063267); TransaccionesParada.crearParada("ST13",
		 * "EST. MENZULY", 7.043636, -73.077585);
		 * TransaccionesParada.crearParada("ST14", "EST. PAYADOR", 7.137213,
		 * -73.122289); TransaccionesParada.crearParada("ST15", "EST. ISLA",
		 * 7.107813, -73.116056); TransaccionesParada.crearParada("ST16",
		 * "EST. MOLINOS", 7.075204, -73.108315);
		 * TransaccionesParada.crearParada("ST17", "EST. HORMIGUEROS", 7.078735,
		 * -73.108101); TransaccionesParada.crearParada("ST18", "EST. PAYADOR",
		 * 7.084418, -73.107869);
		 */
		// TransaccionesParada.crearParada("ST18", "EST. PAYADOR", 7.095377,
		// -73.110602);
		


		
	}

	public String imprimirRutas() {
		conexion = new DBGeneralBRT();
		DBCollection collection = conexion.consultarColeccion("Ruta");
		DBCursor cursor = collection.find();
		ArrayList<BasicDBObject> rutas = new ArrayList<>();
		ArrayList<BasicDBObject> ArrayParadas = null;
		while (cursor.hasNext()) {
			ArrayList<BasicDBObject> paradasSinId = new ArrayList<>();
			BasicDBObject obj = (BasicDBObject) cursor.next();
			BasicDBObject bso = new BasicDBObject();
			bso.append("Nombre", obj.get("Nombre"));
			ArrayParadas = (ArrayList<BasicDBObject>) obj.get("Ruta");
			for (BasicDBObject temp : ArrayParadas) {
				BasicDBObject parada = new BasicDBObject();
				parada.append("Nombre", temp.get("Nombre"));
				parada.append("Coordenada", temp.get("Coordenada"));
				paradasSinId.add(parada);
			}
			bso.append("Ruta", paradasSinId);

			rutas.add(bso);
		}
		BasicDBObject data = new BasicDBObject("Rutas", rutas);
		JsonReader jsonReader = Json.createReader(new StringReader(data.toString()));
		JsonObject json = jsonReader.readObject();
		System.out.println(json.toString());

		return null;

	}

}
