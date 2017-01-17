package angel;

import static org.junit.Assert.*;

import org.junit.Test;

import andresgiovanni.servicios.DistanciaEntreCordenadas;
import core.Coordenadas;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;




public class CalDistTest {

	
	
	@Test	
	public void testDistanciaSimple() {
		//Creamos un objeto de tipo de la clase al que se le va a hacer test
		DistanciaEntreCordenadas dc = new DistanciaEntreCordenadas();
		
		//Creaci�n de coleccion que almacenara objetos coordenadas
		ArrayList<Coordenadas> listaEntrada = new ArrayList<Coordenadas>();
		
		//creacion de coordenada 1
		Coordenadas c1 = new Coordenadas();
		c1.setLatitud(7.123643);
		c1.setLongitud(-73.117458);
		
		//creaci�n e coordenada 2
		Coordenadas c2 = new Coordenadas();
		c2.setLatitud(7.122962);
		c2.setLongitud(-73.117179);
		
		//Agregar los objetos coordenadas
		listaEntrada.add(c1);
		listaEntrada.add(c2);
		
		//Creacion de variable local de resultado
		double resultado = dc.calcular(listaEntrada);
		
		//Assercion de resultado esperado con margen de error
		assertEquals(80.53, resultado, 1.5);
		
	}
	
	
}


