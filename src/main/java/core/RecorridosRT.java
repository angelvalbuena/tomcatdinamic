package core;

import java.util.ArrayList;
import java.util.Iterator;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import db.DBGeneralBRT;

public class RecorridosRT {

	private static ArrayList<Recorrido> recorridos;
	private static RecorridosRT r;
	private final String coleccion = "Recorrido";
	
	private RecorridosRT()
	{
		inicializarRecorridos();
	}
	private void inicializarRecorridos()
	{
		DBGeneralBRT conexion = new DBGeneralBRT();
		DBCollection collection = conexion.consultarColeccion(coleccion);
		DBCursor cursor = collection.find();
		recorridos = new ArrayList<>();
		while (cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject) cursor.next();
			recorridos.add(new Recorrido((String)obj.get("Clave")));
		}
	}
	
	public static RecorridosRT getRecorridosRT()
	{
		if (r==null)
		{
			r = new RecorridosRT();
		}
		
		return r;

	}
	
	/**
	 * Agrega un nuevo recorrido verificando si previamente no existe uno ya creado con la misma clave
	 * @param clave
	 * @return
	 */
	public boolean agregarRecorrido(String clave)
	{
		if (encontrarRecorrido(clave)==null)
		{
			recorridos.add(new Recorrido(clave));
			return true;
		}
		
		return false;
	}
	
	public ArrayList<Recorrido> getRecorridos()
	{
		return recorridos;
	}
	
	public Recorrido encontrarRecorrido(String clave)
	{
		Iterator<Recorrido> i = recorridos.iterator();
		Recorrido temp;
		while (i.hasNext())
		{
			temp = i.next();
			if (clave.equals(temp.getClaveRecorrido()))
			{
				return temp;
			}
		}
		return null;
	}
	
	public void mostarRecorridos()
	{
		System.out.println(recorridos.size());
		for (Recorrido b : recorridos)
		{
			System.out.println(b.getClaveRecorrido());
		}
	}
	
	public boolean editarHoraRecorridoRT(String clave,String parada, String horaAnterior,String horaNueva)
	{
		Recorrido encontrado = encontrarRecorrido(clave);
		if (encontrado!= null)
		{
			return encontrado.editarHorario(parada, horaAnterior, horaNueva);
		}
		return false;
		
	}
	
	public boolean eliminarRecorrido(String clave) {
		Iterator<Recorrido> i = recorridos.iterator();
		Recorrido temp;
		while (i.hasNext()) {
			temp = i.next();
			if (clave.equals(temp.getClaveRecorrido())) {
				i.remove();
				return true;
			}
		}
		return false;
	}
	
}
