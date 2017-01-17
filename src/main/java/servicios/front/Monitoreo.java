package servicios.front;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.MongoWaitQueueFullException;
import com.sun.jersey.server.impl.BuildId;

import core.BusesRT;
import core.Coordenadas;
import core.Despacho;
import core.Fecha;
import core.Itinerario;
import db.DBGeneralBRT;
import db.TItinerario;
import servicios.recolector.UbicacionBus;
import utilidad.FormatearDatos;
import utilidad.GeoMatematicas;
import utilidad.MensajeError;

@Path("monitoreo")
public class Monitoreo {

	private JsonObject respuesta;

	///////
	/////////////////////////////////////// BUSES/////////////////////////////////////////////
	///////

	// GET

	/**
	 * Servicio que permitira obtener todas los buses que se encuentren
	 * almacenados en la base de datos en un arreglo incluido dentro de un
	 * objeto de tipo Json
	 * 
	 * @return Response respuesta del servicio
	 */
	@Path("buses/consultar")
	@GET
	@Produces("application/json")
	public Response obtenerBuses() {
		DBGeneralBRT conexion = new DBGeneralBRT();
		DBCollection collection = conexion.consultarColeccion("Bus");
		DBCursor cursor = collection.find();
		ArrayList<BasicDBObject> buses = new ArrayList<>();
		while (cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject) cursor.next();
			BasicDBObject bso = new BasicDBObject();
			bso.append("Placa", obj.get("Placa"));
			bso.append("Capacidad", obj.get("Capacidad"));
			bso.append("TipoBus", obj.get("TipoBus"));
			bso.append("Estado", obj.get("Estado"));
			bso.append("Operador",obj.get("Operador"));
			bso.append("Coordenada",
					BusesRT.getBusesRT().encontrarBus(obj.getString("Placa")).getJsonBus().get("coordenada"));
			buses.add(bso);
		}

		return Response.status(200).entity(buses.toString()).build();
	}

	/**
	 * Servicio que permitira obtener un bus en especifico almacenado en la base
	 * de datos en un objeto de tipo Json
	 * 
	 * @return Response respuesta del servicio
	 */
	@Path("buses/consultar/{placaBus}")
	@GET
	@Produces("application/json")
	public Response obtenerBus(@PathParam("placaBus") String placaBus) {
		DBGeneralBRT conexion = new DBGeneralBRT();
		// placaBus = FormatearDatos.ArreglarCharset(placaBus);(correccion de
		// charset)
		System.out.println(placaBus);
		placaBus = placaBus.toUpperCase();
		JsonObject respuesta;
		DBObject json = null;
		BasicDBObject dbo = new BasicDBObject();
		json = conexion.consultarMDB("Bus", new BasicDBObject("Placa", placaBus));
		if (json != null) {
			dbo.append("Placa", json.get("Placa"));
			dbo.append("Capacidad", json.get("Capacidad"));
			dbo.append("TipoBus", json.get("TipoBus"));
			dbo.append("Estado", json.get("Estado"));
			dbo.append("Operador",json.get("Operador"));
			dbo.append("Coordenada", BusesRT.getBusesRT().encontrarBus(placaBus).getJsonBus().get("coordenada"));
			JsonReader jsonReader = Json.createReader(new StringReader(dbo.toString()));
			respuesta = jsonReader.readObject();
		} else {
			respuesta = MensajeError.noEncontroElElemento("bus", placaBus);
		}
		return Response.status(200).entity(respuesta.toString()).build();
	}

	///////
	/////////////////////////////////////// PARADAS/////////////////////////////////////////////
	///////

	// GET

	/**
	 * Servicio que permitira obtener todas las Paradas que se encuentren
	 * almacenadas en la base de datos en un arreglo incluido dentro de un
	 * objeto de tipo Json
	 * 
	 * @return Response respuesta del servicio
	 */
	@Path("paradas/consultar")
	@GET
	@Produces("application/json")
	public Response obtenerParadas() {
		DBGeneralBRT conexion = new DBGeneralBRT();
		DBCollection collection = conexion.consultarColeccion("Parada");
		DBCursor cursor = collection.find();
		ArrayList<BasicDBObject> paradas = new ArrayList<>();
		while (cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject) cursor.next();
			BasicDBObject bso = new BasicDBObject();
			bso.append("Clave", obj.get("Clave"));
			bso.append("Nombre", obj.get("Nombre"));
			bso.append("Coordenada", obj.get("Coordenada"));
			paradas.add(bso);
		}

		return Response.status(200).entity(paradas.toString()).build();
	}

	/**
	 * Servicio que permitira obtener una parada en especifico almacenada en la
	 * base de datos en un objeto de tipo Json
	 * 
	 * @return Response respuesta del servicio
	 */
	@Path("paradas/consultar/{claveParada}")
	@GET
	@Produces("application/json")
	public Response obtenerParada(@PathParam("claveParada") String claveParada) {
		DBGeneralBRT conexion = new DBGeneralBRT();
		claveParada = claveParada.toUpperCase();
		DBObject json = null;
		BasicDBObject dbo = new BasicDBObject();
		json = conexion.consultarMDB("Parada", new BasicDBObject("Clave", claveParada));
		if (json != null) {
			dbo.append("Clave", claveParada);
			dbo.append("Nombre", json.get("Nombre"));
			dbo.append("Coordenada", json.get("Coordenada"));
			JsonReader jsonReader = Json.createReader(new StringReader(dbo.toString()));
			respuesta = jsonReader.readObject();
		} else {
			respuesta = MensajeError.noEncontroElElemento("parada", claveParada);
		}
		return Response.status(200).entity(respuesta.toString()).build();
	}

	///////
	/////////////////////////////////////// RUTAS/////////////////////////////////////////////
	///////

	// GET

	/**
	 * Servicio que permitira obtener todas las rutas que se encuentren
	 * almacenadas en la base de datos en un arreglo incluido dentro de un
	 * objeto de tipo Json
	 * 
	 * @return Response respuesta del servicio
	 */
	@Path("rutas/consultar")
	@GET
	@Produces("application/json")
	public Response obtenerRutas() {
		DBGeneralBRT conexion = new DBGeneralBRT();
		DBCollection collection = conexion.consultarColeccion("Ruta");
		DBCursor cursor = collection.find();
		ArrayList<BasicDBObject> rutas = new ArrayList<>();
		ArrayList<BasicDBObject> arrayParadas = null;
		while (cursor.hasNext()) {
			ArrayList<BasicDBObject> paradasSinId = new ArrayList<>();
			BasicDBObject obj = (BasicDBObject) cursor.next();
			BasicDBObject bso = new BasicDBObject();
			bso.append("Nombre", obj.get("Nombre"));
			bso.append("Categoria", obj.get("Categoria"));
			bso.append("Descripcion", obj.get("Descripcion"));
			arrayParadas = (ArrayList<BasicDBObject>) obj.get("Ruta");
			for (BasicDBObject temp : arrayParadas) {
				BasicDBObject parada = new BasicDBObject();
				parada.append("Nombre", temp.get("Nombre"));
				parada.append("Coordenada", temp.get("Coordenada"));
				paradasSinId.add(parada);
			}
			bso.append("Ruta", paradasSinId);

			rutas.add(bso);
		}
		return Response.status(200).entity(rutas.toString()).build();
	}

	/**
	 * Servicio que permitira obtener una ruta en especifico almacenada en la
	 * base de datos en un objeto Json
	 * 
	 * @return Response respuesta del servicio
	 */
	@Path("rutas/consultar/{nombreRuta}")
	@GET
	@Produces("application/json")
	public Response obtenerRuta(@PathParam("nombreRuta") String nombreRuta) {
		nombreRuta = nombreRuta.toUpperCase();
		DBGeneralBRT conexion = new DBGeneralBRT();
		JsonObject respuesta;
		DBObject json = null;
		json = conexion.consultarMDB("Ruta", new BasicDBObject("Nombre", nombreRuta));
		if (json != null) {
			ArrayList<BasicDBObject> arrayParadas = null;
			BasicDBObject bso = new BasicDBObject();
			bso.append("Nombre", json.get("Nombre"));
			bso.append("Categoria", json.get("Categoria"));
			bso.append("Descripcion", json.get("Descripcion"));
			arrayParadas = (ArrayList<BasicDBObject>) json.get("Ruta");
			ArrayList<BasicDBObject> nuevoArrayParadas = new ArrayList<>();
			for (BasicDBObject temp : arrayParadas) {
				BasicDBObject parada = new BasicDBObject();
				parada.append("Nombre", temp.get("Nombre"));
				parada.append("Coordenada", temp.get("Coordenada"));
				nuevoArrayParadas.add(parada);
			}
			bso.append("Ruta", nuevoArrayParadas);

			JsonReader jsonReader = Json.createReader(new StringReader(bso.toString()));
			respuesta = jsonReader.readObject();
		} else {
			respuesta = MensajeError.noEncontroElElemento("ruta", nombreRuta);
		}
		return Response.status(200).entity(respuesta.toString()).build();
	}
	@Path("rutas/consultar/buses/{ruta}")
	@GET
	@Produces("application/json")
	public Response busxRuta(@PathParam("ruta") String nombreRuta) {
		nombreRuta = nombreRuta.toUpperCase();
		ArrayList<Itinerario> itinerarios;
		itinerarios = Despacho.getDespacho().encontarXRuta(nombreRuta);
		BasicDBObject bso = new BasicDBObject();
		ArrayList<BasicDBObject> buses = new ArrayList<>();
		for(Itinerario i : itinerarios){
			bso.append("Placa", i.getBusDesignado().getPlaca());
			bso.append("Capacidad", i.getBusDesignado().getCapacidad());
			bso.append("TipoBus", i.getBusDesignado().getTipoBus());
			bso.append("Estado", i.getBusDesignado().getEstado());
			bso.append("Operador", i.getBusDesignado().getOperador());
			
			bso.append("Coordenada", i.getBusDesignado().getJsonBus().get("coordenada"));
			buses.add(bso);
		}
		
		return Response.status(200).entity(buses.toString()).build();
	}

	///////
	/////////////////////////////////////// RECORRIDOS/////////////////////////////////////////////
	///////

	// GET

	@Path("recorridos/consultar")
	@GET
	@Produces("application/json")
	public Response obtenerRecorridos() {
		DBGeneralBRT conexion = new DBGeneralBRT();
		DBCollection collection = conexion.consultarColeccion("Recorrido");
		DBCursor cursor = collection.find();
		ArrayList<BasicDBObject> recorridos = new ArrayList<>();
		while (cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject) cursor.next();
			BasicDBObject bso = new BasicDBObject();
			bso.append("Clave", obj.get("Clave"));
			bso.append("Ruta", obj.get("Ruta"));
			bso.append("HoraPartida", obj.get("HoraPartida"));
			bso.append("Horario", obj.get("Horario"));
			recorridos.add(bso);
		}
		return Response.status(200).entity(recorridos.toString()).build();
	}

	@Path("recorridos/consultar/{claveRecorrido}")
	@GET
	@Produces("application/json")
	public Response obtenerRecorrido(@PathParam("claveRecorrido") String claveRecorrido) {
		claveRecorrido = claveRecorrido.toUpperCase();
		DBGeneralBRT conexion = new DBGeneralBRT();
		DBObject json = null;
		BasicDBObject dbo = new BasicDBObject();
		json = conexion.consultarMDB("Recorrido", new BasicDBObject("Clave", claveRecorrido));
		if (json != null) {
			dbo.append("Clave", claveRecorrido);
			dbo.append("Ruta", json.get("Ruta"));
			dbo.append("HoraPartida", json.get("HoraPartida"));
			dbo.append("Horario", json.get("Horario"));
			JsonReader jsonReader = Json.createReader(new StringReader(dbo.toString()));
			respuesta = jsonReader.readObject();
		} else {
			respuesta = MensajeError.noEncontroElElemento("Recorrido", claveRecorrido);
		}
		return Response.status(200).entity(respuesta.toString()).build();
	}

	///////
	/////////////////////////////////////// ITINERARIO
	/////// /////////////////////////////////////////////
	///////

	@Path("itinerario/consultar")
	@GET
	@Produces("application/json")
	public Response obtenerItinerarios() {
		DBGeneralBRT conexion = new DBGeneralBRT();
		DBCollection collection = conexion.consultarColeccion("Itinerario");
		DBCursor cursor = collection.find();
		ArrayList<BasicDBObject> itinerarios = new ArrayList<>();
		while (cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject) cursor.next();
			BasicDBObject bso = new BasicDBObject();
			bso.append("Clave", obj.get("Clave"));
			bso.append("Bus", obj.get("Bus"));
			bso.append("Conductor", obj.get("Conductor"));
			bso.append("Recorrido", obj.get("Recorrido"));
			try {
				DBObject datosRecorrido;
				datosRecorrido = conexion.consultarMDB("Recorrido", new BasicDBObject("Clave", obj.get("Recorrido")));
				bso.append("HoraSalidaEstimada", datosRecorrido.get("HoraPartida"));
				bso.append("HoraSalidaReal", obj.get("HoraSalidaReal"));
				bso.append("HorarioEstimado", datosRecorrido.get("Horario"));
				bso.append("HorarioReal", obj.get("HorarioReal"));

			} catch (MongoException e) {
				String respuesta = "{Mensaje: No se el recorrido del itinerario }";
				return Response.status(404).entity(respuesta).build();
			}
			bso.append("EstaTerminado", obj.get("Terminado"));
			bso.append("ProximaParada", obj.get("ProximaParada"));
			itinerarios.add(bso);
		}
		return Response.status(200).entity(itinerarios.toString()).build();
	}

	// NO DEVUELVE LOS ERRORES
	@Path("itinerario/consultar/{claveItinerario}")
	@GET
	@Produces("application/json")
	public Response obtenerItinerario(@PathParam("claveItinerario") String claveItinerario) {
		claveItinerario = claveItinerario.toUpperCase();
		DBGeneralBRT conexion = new DBGeneralBRT();
		DBObject obj = null;
		BasicDBObject dbo = new BasicDBObject();
		obj = conexion.consultarMDB("Itinerario", new BasicDBObject("Clave", claveItinerario));
		BasicDBObject bso = new BasicDBObject();
		if (obj != null) {
			bso.append("Clave", obj.get("Clave"));
			bso.append("Bus", obj.get("Bus"));
			bso.append("Conductor", obj.get("Conductor"));
			bso.append("Recorrido", obj.get("Recorrido"));
			try {
				DBObject datosRecorrido;
				datosRecorrido = conexion.consultarMDB("Recorrido", new BasicDBObject("Clave", obj.get("Recorrido")));
				bso.append("HoraSalidaEstimada", datosRecorrido.get("HoraPartida"));
				bso.append("HoraSalidaReal", obj.get("HoraSalidaReal"));
				bso.append("HorarioEstimado", datosRecorrido.get("Horario"));
				bso.append("HorarioReal", obj.get("HorarioReal"));

			} catch (MongoException e) {
				String respuesta = "{Mensaje: No se el recorrido del itinerario }";
				return Response.status(404).entity(respuesta).build();
			}
			bso.append("EstaTerminado", obj.get("Terminado"));
			bso.append("ProximaParada", obj.get("ProximaParada"));

		} else {
			respuesta = MensajeError.noEncontroElElemento("Itinerario", claveItinerario);
			return Response.status(404).entity(respuesta).build();
		}
		return Response.status(200).entity(bso.toString()).build();
	}

	@Path("itinerario/porcentaje/{placaBus}")
	@GET
	@Produces("application/json")
	public Response posBusItinerario(@PathParam("placaBus") String placaBus) {
		placaBus = placaBus.toUpperCase();
		BasicDBObject dbo = new BasicDBObject();
		Itinerario i = Despacho.getDespacho().encontarXBus(placaBus);
		if (i != null) {
			dbo.append("clave", i.getId());
			dbo.append("avancePorcentaje", i.getAvancePorcentual());
			dbo.append("paradaSiguiente", i.getParadaSiguiente().getJsonParada());
			dbo.append("paradaAnterior", i.getParadaAnterior().getJsonParada());
		} else {
			dbo.append("Error", "No se encontro un itinerario asociado al bus");
			return Response.status(404).entity(dbo.toString()).build();
		}
		return Response.status(200).entity(dbo.toString()).build();
	}

	

	/**
	 * Este servicio devuelve el tiempo que se espera que le tome a los buses
	 * que correspondan a la ruta en ir hasta cada parada y el tiempo teorico
	 * que lleva en el recorrido
	 * 
	 * @param claveItinerario
	 * @return
	 */
	@Path("itinerario/tiempos/ruta/{ruta}")
	@GET
	@Produces("application/json")
	public Response tiemposBusRuta(@PathParam("ruta") String ruta) {
		ArrayList<Itinerario> itinerarios = Despacho.getDespacho().encontarXRuta(ruta);
		BasicDBObject dboExterno = new BasicDBObject();
		dboExterno.append("hora",UbicacionBus.getHoraDelSistema());
		ArrayList<BasicDBObject> entradas = new ArrayList<>();
		if (!itinerarios.isEmpty()) {//Si hay itinerarios relacionados con esa ruta
			for (Itinerario temp : itinerarios) {
				System.out.println(temp.getId());
				LinkedHashMap<String, String> horario = temp.getRecorridoDesignado().getHorario();
				double avanceBus = GeoMatematicas.avanceBusTeorico(temp.getRecorridoDesignado(),temp.getBusDesignado());
				double tiempoEntreEstaciones = GeoMatematicas.duracion(horario.get(temp.getParadaSiguiente().getClave()),horario.get(temp.getParadaAnterior().getClave()));
				BasicDBObject dbo = new BasicDBObject();
				dbo.append("id",temp.getBusDesignado().getOperador()+"/"+temp.getBusDesignado().getPlaca());
				dbo.append("idRecorrido",temp.getRecorridoDesignado().getClaveRecorrido());
				dbo.append("horaSaliDete",Fecha.getFechaClass().convtHoraToMlls(temp.getFecha(),temp.getRecorridoDesignado().getHoraPartida()));
				dbo.append("horaSaliReal",Fecha.getFechaClass().convtHoraToMlls(temp.getFecha(),temp.getHoraSalidaReal()));
				dbo.append("tiemAcumDete",avanceBus);
				dbo.append("tiempEstaDete",tiempoEntreEstaciones);
				dbo.append("porcAvan",((temp.getIndex()-1)*100)+temp.getAvancePorcentual());
				entradas.add(dbo);
			}
			dboExterno.append("Buses",entradas);
			return Response.status(200).entity(dboExterno.toString()).build();
		}
		dboExterno.append("Error", "No hay itinerarios en ejecucion asociados con esa ruta");
		return Response.status(404).entity(dboExterno.toString()).build();

	}

}
