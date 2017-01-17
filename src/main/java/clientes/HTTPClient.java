package clientes;


import java.io.IOException;

import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;





public class HTTPClient{
	
	//campo que contendra el string del json que se lee
private String respuesta;

/**
 * Obtiene la respuesta del servicio a la solicitud de la URL
 * @return String
 */
public String getRespuesta() {
	return respuesta;
}


public void traer()
{
	JsonObject jo;
	JsonReader jr;
	try {
		/**
		 * Url del servicio que desea utilizar
		 */
		URL url = new URL(
				"http://localhost:8080/cloudBRT/api/Proximidad/estaDentro");
		String input =""; 			
		
		
		 /**
		  * Json que se construye para enviar
		  */
	     JsonObject Entrada = Json.createObjectBuilder().add("coordenada1",Json.createObjectBuilder()
	    		 																.add("latitud", 7.137157)
	    		 																.add("longitud",-73.122247))
	    		 										.add("coordenada2",Json.createObjectBuilder()
	    		 																.add("latitud", 7.136681)
	    		 																.add("longitud",-73.122551))
	    		 .build();
	     
	     
			input= Entrada.toString();
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");

		

		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();
					if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

	
	
	 jr = Json.createReader(conn.getInputStream());
	 jo = jr.readObject();
	 
	 /*
	  * String de la respuesta del servidor
	  */
	    respuesta=jo.toString();
	
		System.out.println("Output from Server .... \n");
		System.out.println(respuesta);
		conn.disconnect();

	} catch (MalformedURLException e) {

		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();

	}
	
}
	
}
