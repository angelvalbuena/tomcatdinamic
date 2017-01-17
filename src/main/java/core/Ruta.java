package core;

import java.util.ArrayList;

import javax.json.JsonObject;

import org.bson.BSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import db.DBGeneralBRT;

public class Ruta {
	
	private String nombre,categoria,descripcion;
	private ArrayList<BasicDBObject> paradasDB;
	private ArrayList<Parada> paradas;
	
	public Ruta(String nombre)
	{
		this.nombre=nombre;
		DBGeneralBRT conexion = new DBGeneralBRT();
		DBObject json;
		json = conexion.consultarMDB("Ruta", new BasicDBObject("Nombre", nombre));
		categoria = (String) json.get("Categoria");
		descripcion = (String) json.get("Descripcion");
		paradasDB =  (ArrayList<BasicDBObject>) json.get("Ruta");
		paradas = new ArrayList<>();
		construirRuta();
	}
	
	private void construirRuta()
	{
		for (BasicDBObject temp : paradasDB)
		{
			BSONObject coordenada = (BSONObject) temp.get("Coordenada");
			Parada p = new Parada(temp.getString("Clave"),temp.getString("Nombre"),(double)coordenada.get("Latitud"),(double)coordenada.get("Longitud"));
			paradas.add(p);
		}
	}
	
	public void mostrarRuta()
	{
		for (Parada p : paradas)
		{
			System.out.println(p.getCoordenada().getLatitud()+" "+p.getCoordenada().getLongitud());
		}
	}

	public ArrayList<Parada> getParadas() {
		return paradas;
	}
	
	public String getNombre()
	{
		return nombre;
	}
	
	
	
}
