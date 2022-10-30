package gestionUsuarios;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Puntuacion implements Comparable<Puntuacion>{
	
	private ZonedDateTime fecha;	//fecha en la que se hizo la puntuacion
	private int estrellas;			//número de estrellas conseguidas
	private int nivel;				//nivel en el que se ha conseguido la puntuacion
	private Usuario usuario; 		//usuario asociado con el objeto
	 
	/**Crea una nueva puntuacion de un usuario a la que le corresponde la fecha de hoy
	 * @param estrellas	Estrellas conseguidas, cuyo valor no puede ser mayor de 3 ni negativo
	 * @param usuario	Usuario al que le corresponde la puntuación
	 * @param nivel Nivel en el que se consiguen las estrellas
	 * @throws EstrellasIncorrectasException Lanza esta excepción en el caso que el número de estrellas introducido sea incorrecto
	 */
	public Puntuacion(int estrellas, Usuario usuario, int nivel) throws EstrellasIncorrectasException{
		this.fecha = ZonedDateTime.now();
		if(estrellas <= 3 && estrellas >= 0) {
			this.estrellas = estrellas;
		}else {	
			throw new EstrellasIncorrectasException(estrellas);
		}
		this.usuario = usuario;
		this.nivel = nivel;
	}
	
	/**Devuelve la fecha en formato string
	 * @return	fecha en formato <día/mes/año>
	 */
	public String getFecha() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return dtf.format(fecha);
	}
	
	/**Devuelve las estrellas conseguidas por el usuario
	 * @return	numero de estrellas conseguidas
	 */
	public int getEstrellas() {
		return estrellas;
	}
	
	/**Devuelve el nivel asociado a la puntuacion
	 * @return	número del nivel
	 */
	public int getNivel() {
		return nivel;
	}
	

	/** Ordena por fecha, de más antiguo a más reciente
	 *
	 */
	@Override
	public int compareTo(Puntuacion puntuacion) {
		return fecha.compareTo(puntuacion.fecha);
	}
	
	@Override
	public String toString() {
		return String.format("%d conseguidas el %s", estrellas, getFecha());
	}
	
	
}
