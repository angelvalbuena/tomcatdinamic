package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import db.DBPersonal;

public class ConductoresRT {

	private static ArrayList<Conductor> conductores;
	private static ConductoresRT c;
	private final String coleccion = "Conductores";

	
	
	private ConductoresRT()
	{
		inicializarConductores();
	}
	private void inicializarConductores()
	{
		DBPersonal conexion = new DBPersonal();
		DBCollection collection = conexion.consultarColeccion(coleccion);
		DBCursor cursor = collection.find();
		conductores = new ArrayList<>();
		while (cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject) cursor.next();
			conductores.add(new Conductor(obj.getString("Cedula")));
		}
	}
	
	public static ConductoresRT getConductoresRT()
	{
		if (c==null)
		{
			c = new ConductoresRT();
		}
		
		return c;

	}
	
	public ArrayList<Conductor> getConductores()
	{
		return conductores;
	}
	
	public Conductor encontrarConductor(String cedula)
	{
		Iterator<Conductor> i = conductores.iterator();
		Conductor temp;
		while (i.hasNext())
		{
			temp = i.next();
			if (cedula.equals(temp.getCedula()))
			{
				return temp;
			}
		}
		return null;
	}
	
	
	
	/**
	 * Agrega un conductor al arreglo en tiempo de ejecucion
	 * @param cedula
	 * @return
	 */
	public boolean agregarConductor(String cedula)
	{
		if (encontrarConductor(cedula)== null)
		{
			conductores.add(new Conductor(cedula));
			return true;
		}
		return false;
	}
	
	public boolean modificarConductor(String cedula,String atributo,String NuevoDato)
	{
		Conductor encontrado = encontrarConductor(cedula);
		if (encontrado != null)
		{
			switch (atributo) {
			case "Primer Nombre":
				encontrado.setPrimerNombre(NuevoDato);
				return true;
			case "Segundo Nombre":
				encontrado.setSegundoNombre(NuevoDato);
				return true;
			case "Primer Apellido":
				encontrado.setPrimerApellido(NuevoDato);
				return true;
			case "Segundo Apellido":
				encontrado.setSegundoApellido(NuevoDato);
				return true;
			case "Numero de Licencia":
				encontrado.setLicencia(NuevoDato);
				return true;
			case "Grupo Sanguineo":
				encontrado.setTipoSangre(NuevoDato);
				return true;
				

				
			default:
				System.out.println("Digito mal el tipo");
				return false;
			}
		}
		return false;
	}
	

	
	/**
	 * Encuentra un conductor en el arreglo y lo remueve.
	 * @param cedula
	 * @return
	 */
	public boolean eliminarConductor(String cedula) {
		Iterator<Conductor> i = conductores.iterator();
		Conductor temp;
		while (i.hasNext()) {
			temp = i.next();
			if (cedula.equals(temp.getCedula())) {
				i.remove();
				return true;
			}
		}
		return false;
	}
	
	public void mostarConductores()
	{
		System.out.println(conductores.size());
		for (Conductor b : conductores)
		{
			System.out.println(b.getPrimerNombre());
		}
	}
	
	
}
