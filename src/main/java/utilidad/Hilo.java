/**
 * Esta clase se encarga de construir el hilo mediante Singleton y solo se puede acceder al hilo mediante su metodo estatico
 */

package utilidad;

public class Hilo extends Thread {
    /*
     * se crean dos objetos colaboradores. Mediador que se encarga de contener la logica que se ejecutara en el hilo y el hilo necesario para singleton
     */
	private  MediadorHE m;
	private static  Hilo h;

	
/**
 * Constructor del Hilo que recibe como parametro el objeto Mediador que contiene 
 * la logica de corrida del hilo y se define con nombre Coor. Finalmente se inicializa.
 * @param m
 */
	private Hilo(MediadorHE m) {
		super("Coor");
		this.m = m;
		this.start();

	}
/**
 * Metodo que permite obtener un unico hilo no importa cuantas veces sea llamado el metodo
 * @param m
 * @return h Hilo en ejecucion
 */
	public static Hilo getHilo(MediadorHE m) {
		if(h == null){
			h = new Hilo(m);
		}
		return h;
		
	}

	/**
	 * Metodo encargado de la ejecucion del hilo
	 */
	public void run() {
		while (m.getStatus()) {
			m.step();
			pause();
		}
	}

	/**
	 * Temporizador que permite simular el paso del tiempo.
	 * El parametro en Thread.sleep es un tiempo dado en milisegundos
	 */
	private void pause() {
		try {
			Thread.sleep(1000); // pause for 1000 milliseconds (1 second)
		} catch (InterruptedException exc) {
		}
	}

}
