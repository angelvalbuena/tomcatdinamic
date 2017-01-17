package servicios.front;

import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import core.Bus;
import core.BusesRT;
import core.ConductoresRT;
import core.Coordenadas;
import core.RecorridosRT;
import db.TBus;
import db.TConductor;
import db.TItinerario;
import db.TParada;
import db.TRecorrido;
import db.TRuta;
import utilidad.Diccionario;

@Path("admin")
public class Admin {

	private JsonObject respuesta;

	///////
	/////////////////////////////////////// BUSES/////////////////////////////////////////////
	///////

	// GET

	/**
	 * Servicio que permite eliminar un bus en especifico utilizando su placa y
	 * devuelve como resultado si fue satisfactoria o no la tarea
	 * 
	 * @param placaBus
	 * @return {@link Boolean}
	 */
	@Path("buses/eliminar/{placaBus}")
	@GET
	@Produces("application/json")
	public Response eliminarBuses(@PathParam("placaBus") String placaBus) {
		boolean progreso;
		
		BusesRT.getBusesRT().eliminarBus(placaBus); // Elimina el bus en RT
		progreso = TBus.eliminarBus(placaBus);
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		return Response.status(200).entity(respuesta.toString()).build();
	}

	/**
	 * Servicio que permite modificar el estado de un bus. True (Bus funcional)
	 * False (Bus no funcional) y devuelve como resultado si fue satisfactoria o
	 * no la tarea.
	 * 
	 * @param placaBus
	 * @param estado
	 * @return {@link Boolean}
	 */
	@Path("buses/modificar/{placaBus},{estado}")
	@GET
	@Produces("application/json")
	public Response modificarEstado(@PathParam("placaBus") String placaBus, @PathParam("estado") boolean estado) {
		BusesRT.getBusesRT().modificarEstado(placaBus, estado); // Modifica el estado del bus en RT
		boolean progreso = TBus.modificarEstado(placaBus, estado);//Modifica en DB
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		return Response.status(200).entity(respuesta.toString()).build();
	}

	// POST

	/**
	 * Crea un nuevo bus mediante un json enviado por POST
	 * 
	 * @param incomingData
	 * @return {@link Response}
	 */
	@Path("buses/crear")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response crearBus(InputStream incomingData) {
		JsonObject entrada;
		String placa, tipoBus,operador;
		int capacidad;
		boolean estado, progreso;
		JsonReader jsonReader = Json.createReader(incomingData);
		entrada = jsonReader.readObject();

		placa = entrada.getString("Placa");
		capacidad = entrada.getInt("Capacidad");
		tipoBus = entrada.getString("TipoBus");
		estado = Boolean.parseBoolean(entrada.getString("Estado"));
		operador = entrada.getString("Operador");

		progreso = TBus.crearBus(placa, capacidad, tipoBus, estado,operador); //Modifica en DB
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		System.out.println("Placa:" + placa + " Capacidad:" + capacidad + " TipoBus:" + tipoBus + " Estado:" + estado);
		BusesRT.getBusesRT().agregarNuevo(new Bus(placa)); // Agrega un nuevo bus al RT
		return Response.status(200).entity(respuesta.toString()).build();

	}

	/////////
	//////////////////////////////////////////// PARADAS//////////////////////////////////////////
	/////////

	// GET

	/**
	 * Servicio que permite eliminar una parada mediante su clave. Devuelve si
	 * la tarea fue satisfactoria o no.
	 * 
	 * @param clave
	 * @return {@link Boolean}
	 */
	@Path("paradas/eliminar/{clave}")
	@GET
	@Produces("application/json")
	public Response eliminarParada(@PathParam("clave") String clave) {
		boolean progreso;
		progreso = TParada.eliminarParada(clave);
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		
		return Response.status(200).entity(respuesta.toString()).build();
	}

	// POST

	/**
	 * Crea una nueva parada empleando la informacion obtenida desde un Json
	 * enviado por POST
	 * 
	 * @param incomingData
	 * @return {@link Response}
	 */
	@Path("paradas/crear")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response crearParada(InputStream incomingData) {
		JsonObject entrada;
		JsonObject coordenada;
		String clave, nombre;
		String slatitud, slongitud;
		Coordenadas c1;
		boolean progreso;

		JsonReader jsonReader = Json.createReader(incomingData);
		entrada = jsonReader.readObject();

		clave = entrada.getString("Clave");
		nombre = entrada.getString("Nombre");
		coordenada = entrada.getJsonObject("Coordenada");
		slatitud = coordenada.getString("Latitud");
		slongitud = coordenada.getString("Longitud");
		c1 = new Coordenadas(Double.parseDouble(slatitud), Double.parseDouble(slongitud));
		progreso = TParada.crearParada(clave, nombre, c1.getLatitud(), c1.getLongitud());
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		System.out.println("Clave:" + clave + " Nombre:" + nombre + " Latitud:" + c1.getLatitud() + " Longitud:"
				+ c1.getLongitud());
		return Response.status(200).entity(respuesta.toString()).build();

	}

	///////
	///////////////////////////////////////// RUTAS////////////////////////////////////////////////
	///////

	// GET

	/**
	 * Elimina todas las paradas de una ruta existente. La ruta tiene que
	 * existir para poder efectuarse la tarea. Finalmente devuelve si fue o no
	 * satisfactoria la tarea
	 * 
	 * @param nombre
	 * @return
	 */
	@Path("rutas/remover/paradas/{nombre}")
	@GET
	@Produces("application/json")
	public Response eliminarParadasDeRuta(@PathParam("nombre") String nombre) {
		boolean progreso;
		progreso = TRuta.eliminarParadas(nombre);
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		return Response.status(200).entity(respuesta.toString()).build();
	}

	/**
	 * Remueve una parada de una ruta dando una posicion.
	 * 
	 * @param nombreRuta
	 * @param posicion
	 * @return {@link Response}
	 */
	@Path("rutas/remover/paradas/{nombreRuta},{posicion}")
	@GET
	@Produces("application/json")
	public Response eliminarParadaEspecificaDeRuta(@PathParam("nombreRuta") String nombreRuta,
			@PathParam("posicion") int posicion) {
		boolean progreso;
		progreso = TRuta.removerParadaDeRuta(nombreRuta, posicion);
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		return Response.status(200).entity(respuesta.toString()).build();
	}

	/**
	 * Elimina una ruta por completo
	 * 
	 * @param nombreRuta
	 * @return {@link Response}
	 */
	@Path("rutas/eliminar/{nombreRuta}")
	@GET
	@Produces("application/json")
	public Response eliminarRuta(@PathParam("nombreRuta") String nombreRuta) {
		boolean progreso;
		progreso = TRuta.eliminarRuta(nombreRuta);
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		return Response.status(200).entity(respuesta.toString()).build();
	}

	/**
	 * Agrega una parada al final de una ruta especificada en la url.
	 * 
	 * @param nombreRuta
	 * @param clave
	 * @return {@link Response}
	 */
	@Path("rutas/agregar/paradas/{nombreRuta},{clave}")
	@GET
	@Produces("application/json")
	public Response agregarParadasAlFinal(@PathParam("nombreRuta") String nombreRuta,
			@PathParam("clave") String clave) {
		boolean progreso;
		progreso = TRuta.anadirAFinalDeRuta(nombreRuta, clave);
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		return Response.status(200).entity(respuesta.toString()).build();
	}

	/**
	 * Crea una nueva ruta sin paradas, con los parametros categoria y
	 * descripcion vacios
	 * 
	 * @param nombreRuta
	 * @return {@link Response}
	 */
	@Path("rutas/crear/{nombreRuta}")
	@GET
	@Produces("application/json")
	public Response crearRuta(@PathParam("nombreRuta") String nombreRuta) {
		boolean progreso;
		String categoria = "";
		String descripcion = "";
		progreso = TRuta.crearRuta(nombreRuta, categoria, descripcion);
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		return Response.status(200).entity(respuesta.toString()).build();
	}

	// POST

	/**
	 * Agrega una parada a una ruta dada su posicion. Los datos se obtienen de
	 * un json recibido por POST
	 * 
	 * @param incomingData
	 * @return {@link Response}
	 */
	@Path("rutas/agregar/paradas")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response anadirXPosicionARuta(InputStream incomingData) {
		JsonObject entrada;
		String nombreRuta, clave;
		int posicion;

		boolean progreso;

		JsonReader jsonReader = Json.createReader(incomingData);
		entrada = jsonReader.readObject();

		nombreRuta = entrada.getString("NombreRuta");
		clave = entrada.getString("ClaveParada");
		posicion = Integer.parseInt(entrada.getString("PosicionParada"));

		progreso = TRuta.anadirXPosicionARuta(nombreRuta, clave, posicion);
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		System.out.println("Clave:" + clave + " Nombre:" + nombreRuta + " Posicion:" + posicion);
		return Response.status(200).entity(respuesta.toString()).build();

	}

	/**
	 * Agrega una parada remplazando la existente en la posicion especificada
	 * 
	 * @param incomingData
	 * @return {@link Response}
	 */
	@Path("rutas/reemplazar/paradas")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response remplazarParada(InputStream incomingData) {
		JsonObject entrada;
		String nombreRuta, clave;
		int posicion;

		boolean progreso;

		JsonReader jsonReader = Json.createReader(incomingData);
		entrada = jsonReader.readObject();

		nombreRuta = entrada.getString("NombreRuta");
		clave = entrada.getString("ClaveParada");
		posicion = Integer.parseInt(entrada.getString("PosicionParada"));

		progreso = TRuta.reemplazarParadaDeRuta(nombreRuta, clave, posicion);
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		System.out.println("Clave:" + clave + " Nombre:" + nombreRuta + " Posicion:" + posicion);
		return Response.status(200).entity(respuesta.toString()).build();

	}

	/**
	 * modifica las caracteristicas de una ruta
	 * 
	 * @param incomingData
	 * @return {@link Response}
	 */
	@Path("rutas/modificar/datos")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modificarCaracteristica(InputStream incomingData) {

		JsonObject entrada;
		JsonReader jsonReader = Json.createReader(incomingData);
		boolean progreso;
		entrada = jsonReader.readObject();
		progreso = TRuta.modificarDatoRuta(entrada.getString("Ruta"), entrada.getString("Atributo"),
				entrada.getString("Datos"));
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		return Response.status(200).entity(respuesta.toString()).build();

	}

	///////
	///////////////////////////////////////// RECORRIDOS///////////////////////////////////////////
	///////
	//GET
	
		/**
		 * Servicio que permite eliminar un itinerario en especifico utilizando su clave y
		 * devuelve como resultado si fue satisfactoria o no la tarea
		 * 
		 * @param clave
		 * @return {@link Boolean}
		 */
		@Path("recorrido/eliminar/{clave}")
		@GET
		@Produces("application/json")
		public Response eliminarRecorrido(@PathParam("clave") String clave) {
			boolean progreso;
			
			RecorridosRT.getRecorridosRT().eliminarRecorrido(clave); // Elimina el bus en RT
			progreso = TRecorrido.eliminarRecorrido(clave);
			respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
			return Response.status(200).entity(respuesta.toString()).build();
		}

		

	// POST

	@Path("recorridos/crear")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response crearRecorrido(InputStream incomingData) {
		JsonObject entrada;
		String clave, ruta, horaDePartida;
		boolean progreso;
		JsonReader jsonReader = Json.createReader(incomingData);
		entrada = jsonReader.readObject();

		clave = entrada.getString("Clave");
		ruta = entrada.getString("Ruta");
		horaDePartida = entrada.getString("HoraDePartida");
		progreso = TRecorrido.crearRecorridoAutomatico(clave, ruta, horaDePartida);//Modifica en DB
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		
		RecorridosRT.getRecorridosRT().agregarRecorrido(clave); // Crea un recorrido en RT
		
		return Response.status(200).entity(respuesta.toString()).build();

	}

	@Path("recorridos/editar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editarRecorrido(InputStream incomingData) {
		JsonObject entrada;
		String clave, parada, horaAnterior, horaNueva;
		boolean progreso;
		JsonReader jsonReader = Json.createReader(incomingData);
		entrada = jsonReader.readObject();

		clave = entrada.getString("Clave");
		parada = entrada.getString("Parada");
		horaAnterior = entrada.getString("HoraAnterior");
		horaNueva = entrada.getString("HoraNueva");
		RecorridosRT.getRecorridosRT().editarHoraRecorridoRT(clave, parada, horaAnterior, horaNueva); /*Edita la hora de un punto de
		un recorrido en RT*/
		progreso = TRecorrido.editarHoraRecorrido(clave, parada, horaAnterior, horaNueva);//Modifica en DB
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		return Response.status(200).entity(respuesta.toString()).build();

	}

	///////
	///////////////////////////////////////// CONDUCTORES///////////////////////////////////////////
	///////

	// GET

	@Path("conductores/eliminar/{cedula}")
	@GET
	@Produces("application/json")
	public Response eliminarConductor(@PathParam("cedula") String cedula) {
		boolean progreso;
		ConductoresRT.getConductoresRT().eliminarConductor(cedula); // Elimina un conductor en RT
		progreso = TConductor.eliminarConductor(cedula);//Modifica en DB
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		return Response.status(200).entity(respuesta.toString()).build();

	}

	// POST

	/**
	 * Crea un nuevo conductor mediante un json enviado por POST
	 * 
	 * @param incomingData
	 * @return {@link Response}
	 */
	@Path("conductores/crear")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response crearConductor(InputStream incomingData) {
		JsonObject entrada;

		String cedula;
		String licencia;
		String primerNombre, segundoNombre, primerApellido, segundoApellido, tipoSangre;

		boolean progreso;
		JsonReader jsonReader = Json.createReader(incomingData);
		entrada = jsonReader.readObject();

		cedula = entrada.getString("Cedula");
		primerNombre = entrada.getString("Primer Nombre");
		segundoNombre = entrada.getString("Segundo Nombre");
		primerApellido = entrada.getString("Primer Apellido");
		segundoApellido = entrada.getString("Segundo Apellido");
		licencia = entrada.getString("Numero de Licencia");
		tipoSangre = entrada.getString("Grupo Sanguineo");

		progreso = TConductor.crearConductor(cedula, primerNombre, segundoNombre, primerApellido, segundoApellido,
				licencia, tipoSangre); //Modifica en DB
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		ConductoresRT.getConductoresRT().agregarConductor(cedula);// Agrega un conductor al RT
		return Response.status(200).entity(respuesta.toString()).build();

	}

	/**
	 * Modifica uno de los parametros del conductor segun se indique en el json
	 * 
	 * @param incomingData
	 * @return {@link Response}
	 */
	@Path("conductores/modificarDato")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modificarDato(InputStream incomingData) {
		JsonObject entrada;

		String cedula;
		String dato;
		String nuevoValor;

		boolean progreso;
		JsonReader jsonReader = Json.createReader(incomingData);
		entrada = jsonReader.readObject();

		cedula = entrada.getString("Cedula");
		dato = entrada.getString("Dato");
		nuevoValor = entrada.getString("NuevoValor");

		String datoMapeado =Diccionario.atributoConduc(dato);
		
		ConductoresRT.getConductoresRT().modificarConductor(cedula,datoMapeado, nuevoValor);//Modifica RT
		
		progreso = TConductor.modificarDatoConductor(cedula, datoMapeado, nuevoValor);//Modifica la DB
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		return Response.status(200).entity(respuesta.toString()).build();

	}
	

	///////
	/////////////////////////////////////// ITINERARIO /////////////////////////////////////////////
	///////
	
    //GET
	
	/**
	 * Servicio que permite eliminar un itinerario en especifico utilizando su clave y
	 * devuelve como resultado si fue satisfactoria o no la tarea
	 * 
	 * @param clave
	 * @return {@link Boolean}
	 */
	@Path("itinerario/eliminar/{clave}")
	@GET
	@Produces("application/json")
	public Response eliminarItinerario(@PathParam("clave") String clave) {
		boolean progreso;
		
		TItinerario.modificarTerminado(clave, true);
		progreso = TItinerario.eliminarItinerario(clave);
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		return Response.status(200).entity(respuesta.toString()).build();
	}

	
	// POST

	/**
	 * Crea un nuevo itinerario mediante un json enviado por POST
	 * 
	 * @param incomingData
	 * @return {@link Response}
	 */
	@Path("itinerario/crear")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response crearItinerario (InputStream incomingData) {
		JsonObject entrada;
		String clave, conductor, bus, recorrido,horaSalidaEstimada ;
		boolean progreso;
		JsonReader jsonReader = Json.createReader(incomingData);
		entrada = jsonReader.readObject();
		

		clave = entrada.getString("Clave");
		conductor = entrada.getString("Conductor");
		bus = entrada.getString("Placa");
		recorrido = entrada.getString("Recorrido");
		horaSalidaEstimada = entrada.getString("HoraSalidaEstimada");

		progreso = TItinerario.crearItinerario(clave, conductor, bus, recorrido,horaSalidaEstimada); //Modifica en DB
		respuesta = Json.createObjectBuilder().add("Encontrado", progreso).build();
		return Response.status(200).entity(respuesta.toString()).build();

	}

}
