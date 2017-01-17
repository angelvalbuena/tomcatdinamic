/**
 * Esta clase se encarga de Enviar coordenadas de forma dinamica incrementando la misma su 4 cifra decimal
 * a su vez se obtiene la fecha y hora de la solicitud.
 * 
 */

package andresgiovanni.servicios;

import core.*;
import utilidad.*;

import javax.ws.rs.GET;

import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


import javax.json.Json;
import javax.json.JsonObject;

@Path("/consultar")
public class EnvioCoor {
	//Declaracion de dos objetos colaboradores necesarios para iniciar el hilo y trabajar las coordenadas.
    private MediadorHE r;
    private Coordenadas c;
    
    
    /**
     * Servicio encargado de proporcionar informacion pertinente a un Bus que se esta desplazando por una via (simulado)
     * @return Response La respuesta en String proporcionada al cliente del servicio
     */
    @Path("/posicion")
	@GET
	@Produces("application/json")
	public  Response asda() {
    	r = new MediadorHE();
        c = new Coordenadas();
        double longpartida=-73.1049097579193;
        double latpartida=7.0704193901191745;
    	r.iniciar(); 
    	Fecha f = Fecha.getFechaClass();
    	double numero = ((double)r.getStep())/1000;
    	c.setLatitud(latpartida+numero);
    	c.setLongitud(longpartida+numero);
    	
    	JsonObject result = Json.createObjectBuilder()
    			.add("Ruta", "Ruta1")
    			.add("Buses", 2)
    			.add("Tiempo", f.getFecha())
    			.add("id","P10XYZ325").add("Coordenada",Json.createObjectBuilder()
    								  .add("latitud",c.getLatitud())
    								  .add("longitud", c.getLongitud())).build();
    	//r.detener(); discutir si detener este hilo que se qeuda ejecutandose.
		return Response.status(200).entity(result.toString()).build();
	}
    
    
    /**
     * Este servicio se encarga de brindar la Ubicacion que es proporcionada por los buses.
     * @return Response String que almacena el promedio de ubicacion del bus.
     */
    @Path("/wilsonPosi")
   	@GET
   	@Produces("application/json")
   	public  Response wilson() {
       	       
   		//String result = UbicacionBus.getBusProm();
    	String result = "hi";
   		return Response.status(200).entity(result.toString()).build();
   	}

  }