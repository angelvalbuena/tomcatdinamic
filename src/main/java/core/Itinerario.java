package core;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import db.DBGeneralBRT;
import db.TItinerario;
import interfaces.Observer;
import interfaces.Subject;
import utilidad.GeoMatematicas;

public class Itinerario implements Subject {

	private String clave;
	private int cantParadas;
	private LinkedHashMap<String, String> horarioReal;
	private Conductor conductorDesignado;
	private Recorrido recorridoDesignado;
	private Bus busDesignado;
	private ArrayList<Observer> observers;
	private String horaSalidaReal;
	private int index;
	private String fecha;
	private boolean terminado;

	public Itinerario(String clave) {
		this.clave = clave;
		crearItinerario();
	}

	public String getId() {
		return clave;
	}

	public boolean getTerminado() {
		return terminado;
	}
	
	public Parada getParadaAnterior()
	{
		 int temp = index-1;
		return recorridoDesignado.getObjetoPadadaPorIndice(temp);
	}
	

	
	public Parada getParadaSiguiente()
	{
		return recorridoDesignado.getObjetoPadadaPorIndice(index);
	}
	
	 public double getAvancePorcentual() {
	 
		 Parada anterior,siguiente;
		 double distParcial,distTotal;
		 double porcentaje;
		 int temp = index-1;
		 anterior = recorridoDesignado.getObjetoPadadaPorIndice(temp);
		 siguiente=recorridoDesignado.getObjetoPadadaPorIndice(index);
		 distParcial = GeoMatematicas.DisProyCoor(anterior.getCoordenada(),busDesignado.getCoor(),siguiente.getCoordenada());
		 distTotal = GeoMatematicas.calcDistancia(anterior.getCoordenada(), siguiente.getCoordenada());
		 porcentaje = (distParcial/distTotal)*100;
		 
		 return porcentaje;
		 
	  }
	 
	
	 


	public void actualizarHorarioReal(Parada p) {
		BasicDBObject itinerarioDB = new BasicDBObject("Clave", clave);
		DBGeneralBRT mongo = new DBGeneralBRT();
		DBObject itineario = mongo.consultarMDB("Itinerario", itinerarioDB);
		horarioReal = (LinkedHashMap<String, String>) itineario.get("HorarioReal");
		horarioReal.put(p.getClave(), Fecha.getFechaClass().gethora());
		TItinerario.marcarHora(clave, horarioReal);
		mongo.cerrarConexion();

	}

	public int getCantParadas() {
		return cantParadas;
	}

	public String getHoraSalidaReal() {
		return horaSalidaReal;
	}

	public int getIndex() {
		return index;
	}
	
	public void resetIndex()
	{
		index = 0;
	}

	public synchronized void encontrar() {

		
		System.out.println("Cantidad de paradas: "+ cantParadas);
		System.out.println("Indice: "+index);
		
		if (cantParadas > index) {

			Parada p = recorridoDesignado.getRuta().getParadas().get(index);
			boolean resultado = p.estaDentro(busDesignado.getCoor());
			int updtIndex = busDesignado.getProximaParada();
			System.out.println("Este es el indice anterior: "+updtIndex);
			
			if (index != updtIndex) 
			{
				index = updtIndex;
			}
			
			double porcentaje = getAvancePorcentual();
			
			if (porcentaje>100)
			{
				index++;
				busDesignado.setProximaParada(index);
				TItinerario.modificarProximaParada(this.getId(), index);
			}
			
			
			
			if (resultado == true) {
				actualizarHorarioReal(p);
				NotifyObservers();
				index++;
				busDesignado.setProximaParada(index);
				System.out.println("Este es el indice siguiente: "+busDesignado.getProximaParada());
				TItinerario.modificarProximaParada(this.getId(), index);
			}
			else
			{
				System.out.println("no pase por aca");
			}
		} else {
			terminado = true;
			TItinerario.modificarTerminado(this.getId(), true);
			//Despacho.getDespacho().Refrescar();
			System.out.println("se ha terminado este itinerario");
		}
	}

	public void setId(String clave) {
		this.clave = clave;
	}

	public Conductor getConductorDesignado() {
		return conductorDesignado;
	}

	public void setConductorDesignado(Conductor conductorDesignado) {
		this.conductorDesignado = conductorDesignado;
	}

	public Recorrido getRecorridoDesignado() {
		return recorridoDesignado;
	}

	public void setRecorridoDesignado(Recorrido recorridoDesignado) {
		this.recorridoDesignado = recorridoDesignado;
	}

	public Bus getBusDesignado() {
		return busDesignado;
	}

	public void setBusDesignado(Bus busDesignado) {
		this.busDesignado = busDesignado;
	}

	public void mostrarHoraReal() {
		System.out.println(horarioReal.toString());
	}

	public synchronized void crearItinerario() {
		BasicDBObject itinerarioDB = new BasicDBObject("Clave", clave);
		DBGeneralBRT mongo = new DBGeneralBRT();
		DBObject itineario = mongo.consultarMDB("Itinerario", itinerarioDB);
		horarioReal = (LinkedHashMap<String, String>) itineario.get("HorarioReal");
		horaSalidaReal = (String) itineario.get("HoraSalidaReal");
		busDesignado = BusesRT.getBusesRT().encontrarBus((String) itineario.get("Bus"));
		recorridoDesignado = RecorridosRT.getRecorridosRT().encontrarRecorrido((String) itineario.get("Recorrido"));
		conductorDesignado = ConductoresRT.getConductoresRT().encontrarConductor((String) itineario.get("Conductor"));
		fecha = (String)itineario.get("Fecha");
		terminado = (boolean) itineario.get("Terminado");
		index = (int) itineario.get("ProximaParada");
		System.out.println("INDICE: " +index);
		cantParadas = recorridoDesignado.getRuta().getParadas().size();
		observers = new ArrayList<>();
		mongo.cerrarConexion();
	}
	
	

	public String getFecha() {
		return fecha;
	}

	@Override
	public void AddObserver(Observer e) {
		// TODO Auto-generated method stub
		observers.add(e);
	}

	@Override
	public void RemoveObserver(Observer e) {
		// TODO Auto-generated method stub
		observers.remove(e);
	}

	@Override
	public void NotifyObservers() {
		// TODO Auto-generated method stub
		busDesignado.Update();
	}

}
