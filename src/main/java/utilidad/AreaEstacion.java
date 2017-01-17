package utilidad;

import core.Coordenadas;


public class AreaEstacion {

	private double radio;
	private double distancia;
	
	public AreaEstacion(double radio)
	{
		setRadio(radio);
	}
	
	public AreaEstacion()
	{
		radio = 200;
	}
	
	public void setRadio(double radio)
	{
		this.radio = radio;
	}
	
	public double getDistancia()
	{
		return distancia;
	}
			
	public boolean estaDentro(Coordenadas vertice,Coordenadas bus)
	{
		double temp;
		temp = GeoMatematicas.calcDistancia(vertice,bus);
		this.distancia = temp;
		if (temp>= radio)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
