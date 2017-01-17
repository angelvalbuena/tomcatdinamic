package utilidad;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Esta clase se encarga de leer los datos entrantes al servidor en formato Json
 * y transformarlos en Strings
 * 
 * @author Carlos Pereira & Giovanni Flores
 *
 */
public class LecturaJson {

	private String lectura;

	public LecturaJson(InputStream incomingData) {

		leerArchivo(incomingData);

	}

	/**
	 * Este metodo se encarga de recibir la informacion del cliente enviada en
	 * formato Json y convertirla en un String para su posterior uso.
	 * 
	 * @param incomingData
	 * @return String
	 */
	private void leerArchivo(InputStream incomingData) {
		StringBuilder constructor = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				constructor.append(line);
				constructor.append("\n");
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		lectura = constructor.toString();

	}

	/**
	 * Se encarga de entregar un listado de coordenadas
	 * 
	 * @return ArrayList<Coordenadas>
	 */
	public String getLectura() {
		return lectura;
	}

}