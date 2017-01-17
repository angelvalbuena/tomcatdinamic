package utilidad;

import java.util.HashMap;

import core.ConductoresRT;

public class Diccionario {

	private static HashMap<String, String> diccionario;
	
	
	private static HashMap<String, String> getDicConduc()
	{
		if (diccionario==null)
		{
		diccionario = new HashMap<>();
		diccionario.put("PN", "Primer Nombre");
		diccionario.put("SN", "Segundo Nombre");
		diccionario.put("PA", "Primer Apellido");
		diccionario.put("SA", "Segundo Apellido");
		diccionario.put("LIC", "Numero de Licencia");
		diccionario.put("TS", "Grupo Sanguineo");
		}
		return diccionario;
	}
	
	public static String atributoConduc(String tipo)
	{
		return Diccionario.getDicConduc().get(tipo);
	}
	
	
	
}
