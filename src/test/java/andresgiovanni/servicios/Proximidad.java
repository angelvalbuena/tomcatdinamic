package andresgiovanni.servicios;

import java.io.InputStream;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import core.Coordenadas;
import utilidad.AreaEstacion;
import utilidad.Extractor;
import utilidad.LecturaJson;

@Path("/proximidad")
public class Proximidad {
	
	private Extractor coorExtractor;
	private LecturaJson leer;
	private ArrayList<Coordenadas> coordenadasObtenidas;
	private AreaEstacion area;
	@Path("/estaDentro")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response calcularDentro(InputStream incomingData) {
		area = new AreaEstacion(30);
		leer = new LecturaJson(incomingData);
		coorExtractor = new Extractor();//lee el json que envian y genera un string
		
		coordenadasObtenidas = coorExtractor.extraerCoordenadas(leer.getLectura());
		boolean dentro;
		dentro = area.estaDentro(coordenadasObtenidas.get(0),coordenadasObtenidas.get(1));
		double dist; 
		dist = area.getDistancia();
		JsonObject respuesta = Json.createObjectBuilder()
				.add("Ubicacion",dentro).add("distancia",dist).build();
		
		return Response.status(200).entity(respuesta.toString()).build();
		
		
	}
	

}
