package angel;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

import core.Coordenadas;
import utilidad.AreaEstacion;

import java.io.InputStream;


public class ProximidTest {
	
	@Test
	public void testCercano(){
		
		//Creamos un objeto de tipo de la clase al que se le va a hacer test
		AreaEstacion pr = new AreaEstacion();
		
		//Creaci�n de  coordenada
		Coordenadas c = new Coordenadas();
		c.setLatitud(7.123643);
		c.setLongitud(-73.117458);
		
		Coordenadas v = new Coordenadas();
		v.setLatitud(7.123643);
		v.setLongitud(-73.117458);
		
		
		
		//Creacion de variable local de resultado
		boolean result = pr.estaDentro(v,c);
		
		//Assercion de resultado esperado con margen de error
		assertEquals(true,result);
	}

	
}
