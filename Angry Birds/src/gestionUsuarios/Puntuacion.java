package gestionUsuarios;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

public class Puntuacion implements Comparable<Puntuacion>{
	
	private ZonedDateTime fecha;	//fecha en la que se hizo la puntuacion
	private int estrellas;			//número de estrellas conseguidas
	private int nivel;				//nivel en el que se ha conseguido la puntuacion

	 
	/**Crea una nueva puntuacion de un usuario a la que le corresponde la fecha de hoy
	 * @param estrellas	Estrellas conseguidas, cuyo valor no puede ser mayor de 3 ni negativo
	 * @param nivel Nivel en el que se consiguen las estrellas
	 */
	public Puntuacion(int estrellas, int nivel){
		this.fecha = ZonedDateTime.now();
		this.estrellas = estrellas;
		this.nivel = nivel;
	}
	
	/** Crea una nueva puntuacion a partir de los parámetros indicados
	 * @param fecha String de la fecha en la que se hace la puntuacion
	 * @param estrellas	número de estrellas conseguidas, cuyo valor no puede ser mayor de 3 ni negativo
	 * @param nivel Nivel en el que se consiguen las estrellas
	 * @throws DateTimeParseException Lanza está excepción en el caso que el formato de la fecha sea incorrecto
	 */
	public Puntuacion(String fecha, int estrellas, int nivel) throws DateTimeParseException{
		this.fecha = ZonedDateTime.parse(fecha);
		this.estrellas = estrellas;
		this.nivel = nivel;
	}

	/**Devuelve la fecha en formato string
	 * @return	fecha en formato <día/mes/año>
	 */
	public String getFecha() {
		return fecha.toString();
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
