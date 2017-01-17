/**
 * Esta clase se encarga de construir una fecha y hora de acuerdo al formato indicado ano/mes/dia hora/minuto/segundo
 * y devuelve la misma como un String
 */

package core;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Fecha {

	private String fecha;//cadena que contendra la fecha 
	private static Fecha fechaClass;
	private String ymd;
	private String hora;
	
	/**
	 *Constructor sin parametros donde se define el formato y se construye la fecha 
	 */
	private Fecha()
	{
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    Date date = new Date();
		fecha=dateFormat.format(date);
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
		Date date2 = new Date();
		ymd = dateFormat2.format(date2);
		DateFormat dateFormat3 = new SimpleDateFormat("HH:mm:ss");
		Date date3 = new Date();
		hora = dateFormat3.format(date3);
		
		
	}
	
	/**
	 * Devuelve la fecha
	 * @return fecha
	 */
	public String getFecha()
	{	fechaClass = new Fecha();
		return fecha;
	}
	public String getYMD()
	{
		return ymd;
	}
	
	public String gethora(){
		return hora;
	}
	
	public long convtHoraToMlls(String fecha,String hora)
	{
		String date = fecha+" "+hora;
		long ms = 0;
	     SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			Date d = f.parse(date);
			ms = d.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ms;
	}
	
	public static Fecha getFechaClass(){
		if(fechaClass == null){
			fechaClass = new Fecha();
		}
		return fechaClass;
	}
}