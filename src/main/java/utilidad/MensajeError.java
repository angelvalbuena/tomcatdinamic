package utilidad;

import javax.json.Json;
import javax.json.JsonObject;

import com.mongodb.BasicDBObject;

public class MensajeError {

	public static JsonObject servicioCaido()
	{
		JsonObject mensaje = Json.createObjectBuilder().add("Error","El servicio de base de datos no responde").build();
		return mensaje;
	}
	public static BasicDBObject denegar()
	{
		BasicDBObject mensaje = new BasicDBObject("Error","La placa del bus no esta registrada. Comuniquese con un administrador"
				+ "del sistema para agregarla");
		return mensaje;
	}
	public static JsonObject noEncontroElElemento(String elemento,String clave)
	{
		JsonObject mensaje = Json.createObjectBuilder().add("Error","No se encontro el " + elemento + " con la clave " + clave + " en la base de datos").build();
		return mensaje;
	}
	
}
