package utilidad;

import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class FormatearDatos {

private static final double factorDeConversionKmhToMs=0.277778;
	
	/**
	 * Coloca una unica mayuscula inicial el resto de caracteres en minuscula
	 * @param original
	 * @return
	 */
	public static String mayusInicial(String original) {
		original = original.toLowerCase();
		if (original == null || original.length() == 0) {
			return original;
		}
		return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
	/**
	 * Coloca mayuscula inicial a cada elemento espaciado.
	 * @param frase
	 * @return
	 */
	public static String mayusInicialMulti(String frase){
		StringTokenizer st = new StringTokenizer(frase, " ");
		String result ="";
		while (st.hasMoreElements()) {
			result += mayusInicial(st.nextElement().toString())+" ";
		}
		StringBuilder borrar = new StringBuilder(result);
		borrar.deleteCharAt(result.length()-1);
		result = borrar.toString();

		return result;
	}
	
	public static String ArreglarCharset(String corrupto)
	{
		 byte[] bytes = corrupto.getBytes();
		 return new String(bytes, StandardCharsets.ISO_8859_1);
	}
	
	public static double hoursToSeconds(double hour){
		hour = hour * 3600;
		return hour;
	}

	public static double hoursToMinutes(double hour){
		hour = hour * 60;
		return hour;
	}
	
	public static double kmxhTomxs(double velocidad){
		return factorDeConversionKmhToMs*velocidad;
	}
	
	public static String formatoDeTiempo(double tiempo){
		String formato;
		String horasS,minutosS,segundosS;
		int horas = (int) (tiempo / 3600);
		int minutos = (int) ((tiempo % 3600) / 60 ) ;
		int segundos =  (int) (tiempo % 60);
		
		if (horas==24)horas = 0;
		
		if (horas<=9)horasS="0"+horas;
		else horasS=""+horas;
		
		if (minutos<=9)minutosS="0"+minutos;
		else minutosS=""+minutos;
		
		if (segundos<=9)segundosS="0"+segundos;
		else segundosS=""+segundos;
		
		formato = "" + horasS +":" + minutosS + ":" +segundosS;
		
		return formato;
		
	}
	
	public static double removerFormatoDeTiempo(String hora) {
		String horaT, minT, segT;
		int horaI, minI, segI;
		int segundos;
		StringTokenizer st = new StringTokenizer(hora, ":");
		horaT = st.nextToken();
		minT = st.nextToken();
		segT = st.nextToken();
		horaI = (int) Double.parseDouble(horaT);
		minI = (int) Double.parseDouble(minT);
		segI = (int) Double.parseDouble(segT);

		segundos = horaI * 3600 + minI * 60 + segI;
		return segundos;

	}
	
	public static String sumarAHora(String hora,String duracion)
	{
		double horaPartidaSegundos=removerFormatoDeTiempo(hora);
		double duracionSegundos = removerFormatoDeTiempo(duracion);
		double suma = horaPartidaSegundos+duracionSegundos;
		String horaFinalizacion = formatoDeTiempo(suma);
		return horaFinalizacion;
	}
	
	
	/*public static void quicksort(double A[], int izq, int der) {

		  double pivote=A[izq]; // tomamos primer elemento como pivote
		  int i=izq; // i realiza la b�squeda de izquierda a derecha
		  int j=der; // j realiza la b�squeda de derecha a izquierda
		  double aux;
		 
		  while(i<j){            // mientras no se crucen las b�squedas
		     while(A[i]<=pivote && i<j) i++; // busca elemento mayor que pivote
		     while(A[j]>pivote) j--;         // busca elemento menor que pivote
		     if (i<j) {                      // si no se han cruzado                      
		         aux= A[i];                  // los intercambia
		         A[i]=A[j];
		         A[j]=aux;
		     }
		   }
		   A[izq]=A[j]; // se coloca el pivote en su lugar de forma que tendremos
		   A[j]=pivote; // los menores a su izquierda y los mayores a su derecha
		   if(izq<j-1)
		      quicksort(A,izq,j-1); // ordenamos subarray izquierdo
		   if(j+1 <der)
		      quicksort(A,j+1,der); // ordenamos subarray derecho

		}*/
	
	public static void quickSort(int lowerIndex, int higherIndex,double[] array) {
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        double pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (array[i] < pivot) {
                i++;
            }
            while (array[j] > pivot) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j,array);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j,array);
        if (i < higherIndex)
            quickSort(i, higherIndex,array);
    }
 
    private static void exchangeNumbers(int i, int j,double[] array) {
        double temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
	
}
