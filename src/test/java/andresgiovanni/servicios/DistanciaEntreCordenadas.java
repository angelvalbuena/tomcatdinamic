package andresgiovanni.servicios;

import core.*;
import utilidad.*;

//Buffers para lectura del contenido de fichero.

import java.io.InputStream;

//import javax.print.attribute.standard.Media;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import java.lang.Math;
import java.util.ArrayList;


@Path("/distancia")
public class DistanciaEntreCordenadas {
	//private String ruta;
	private Extractor coorExtractor;
	private LecturaJson leer;
	private ArrayList<Coordenadas> coordenadasObtenidas;

	/**
	 * Recibe un objeto Json dotado de dos coordenadas en un formato
	 * y retorna otro objeto Json en respuesta de servidor con la distancia
	 * entre los dos puntos. la respuesta esta dada en Kilometros.
	 * 
	 * @param incomingData
	 * @return Response
	 */
	@Path("/distR")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response calcularDistancia(InputStream incomingData) {
		
		leer = new LecturaJson(incomingData);
		coorExtractor = new Extractor();//lee el json que envian y genera un string
		
		coordenadasObtenidas = coorExtractor.extraerCoordenadas(leer.getLectura());
		
		for(Coordenadas d: coordenadasObtenidas){
			
			System.out.println(d.getLatitud());
			System.out.println(d.getLongitud());
		}
		JsonObject respuesta = Json.createObjectBuilder()
				.add("distancia",calcular(coordenadasObtenidas)).build();
		
		return Response.status(200).entity(respuesta.toString()).build();
		
		
	}


	/**
	 * Calcula la distancia entre dos cordenadas mediante su latitud
	 * y longitud, recibiendo un array de objetos cordenadas que 
	 * contienen doubles para el c�lculo
	 * 
	 * @param entrada
	 * @return double
	 */
	public double calcular(ArrayList<Coordenadas> entrada) {
	
		double lat1=0,lng1=0;
		double lat2=0,lng2=0;
		
		lat1 = entrada.get(0).getLatitud();
		lng1 = entrada.get(0).getLongitud();
		lat2 = entrada.get(1).getLatitud();
		lng2= entrada.get(1).getLongitud();		
		
		double radioTierra = 6371;// en kilometros
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double va1 = Math.pow(sindLat, 2)
				+ Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
		double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
		double distancia = radioTierra * va2;
		return distancia*1000;
	}

	
}

