package utilidad;

public class MediadorHE {

	/*
     * status se encarga de mantener con vida el hilo y step es el numero de ejecucion del hilo 
     * (simulando el tiempo transcurrido)
     */
	
	private boolean status;
    private static int step;
 
	/**
	 * Inicia la ejecucion del hilo
	 */
    
    
	 public void iniciar()
	    {
	    	 status = true;
	    	 Hilo hilo = Hilo.getHilo(this);
	    }
	    
	 
	 /**
	  * Contiene la logica que se ejecuta cada intervalo en el hilo
	  */
	    public void step()
	    {
	    	step++;
	    	System.out.println(step + "ejecucion");
	    }
		
	    
	    /**
	     * Permite conocer si el hilo esta activo o no
	     * @return status Estado del hilo
	     */
		public boolean getStatus()
		{
		return status;	
		}
		
		
		/**
		 * Detiene la ejecucion del hilo
		 */
		
		public void detener()
		{
		       status = false;
		}
		
		
		/**
		 * Devuele el numero del ciclo en el que se encuentra el hilo
		 * @return step Ciclo actual del hilo
		 */
		public int getStep()
		{
			return step;
		}
		
	
}
