package utilidad;

import java.util.ArrayList;

import core.*;



public class CapturaCoor {
	
	private static ArrayList<Coordenadas> CoorToAverage;
	private static CapturaCoor CC;
	
	private CapturaCoor(){
		
		
	}
	
	public static CapturaCoor getCapturaCoor() {
		if(CC == null){
			CC = new CapturaCoor();
			CoorToAverage = new ArrayList<>();
		}
		return CC;
		
	}
	
	public void addToColeccion(Coordenadas C){
		Coordenadas coorToAdd;
		coorToAdd = C;
		
		CoorToAverage.add(coorToAdd);
		
		
	}

	public static ArrayList<Coordenadas> getCoorToAverage() {
		return CoorToAverage;
	}
	
	public static void resetColeccion(){
		
		CoorToAverage = new ArrayList<>();
		
	}
}


