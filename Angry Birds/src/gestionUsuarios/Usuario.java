package gestionUsuarios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Usuario implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String nombre;						//nombre del usuario
	private String contraseña;					//contraseña del usuario
	private long tiempoJugado;					//tiempo total jugado por el usuario (en segundos)
	private Set<Puntuacion> puntuaciones;		//set que guarda todas las puntuaciones conseguidas por el usuario
	private Token token;						//token asociado al usuario
	

	/**Crea un nuevo usuario con el nombre, la contraseña  y el token especificado, pero con 0 tiempo jugado y el mapa de puntuaciones vacío
	 * @param nombre nombre del usuario
	 * @param contraseña contraseña del usuario
	 * @param token token del usuario
	 */
	public Usuario(String nombre, String contraseña, Token token) {
		this(nombre, contraseña, 0, new TreeSet<>(), token);
	}
	
	
	/** Crea un nuevo usuario con el nombre, contraseña, tiempoJugado, TreeSet de puntuaciones y token especificado
	 * @param nombre nombre del usuario
	 * @param contraseña contraseña del usuario
	 * @param tiempoJugado tiempo jugado del usuario (en segundos)
	 * @param puntuaciones TreeSet de puntuaciones del usuario
	 * @param token token asociado al usuario
	 */
	public Usuario(String nombre, String contraseña, long tiempoJugado, Set<Puntuacion> puntuaciones, Token token) {
		this.nombre = nombre;
		this.contraseña = contraseña;
		this.tiempoJugado = tiempoJugado;
		this.puntuaciones = puntuaciones;
		this.token = token;
	}
	
	/** Devuelve el nombre del usuario
	 * @return Nombre del usuario
	 */
	public String getNombre() {
		return nombre;
	}

	/** Devuelve la contraseña del usuario
	 * @return Contraseña del usuario
	 */
	public String getContraseña() {
		return contraseña;
	}

	/** Devuelve el tiempo total jugado por el usuario
	 * @return	Tiempo jugado del usuario en total (en segundos), sin tener en cuenta las pantallas de inicio
	 */
	public long getTiempoJugado() {
		return tiempoJugado;
	}
	
	/** Añade cierta cantidad de segundos al tiempo total jugado por el usuario
	 * @param tiempo Segundos a añadir al total
	 */
	public void addTiempo(long tiempo) {
		this.tiempoJugado += tiempo;
	}
	
	/** Devuelve la Lista de puntuaciones del usuario
	 * @return Lista de puntuaciones del usuario
	 */
	public List<Puntuacion> getPuntuaciones(){
		return new ArrayList<Puntuacion>(puntuaciones);
	}	
	
	/** Añade una nueva puntuacion al set de puntuaciones del usuario
	 * @param puntuacion Nueva puntuacion a añadir
	 */
	public void addPuntuacion(Puntuacion puntuacion) {
		puntuaciones.add(puntuacion);
	}
	
	/** Devuelve el token asociado al usuario
	 * @return token del usuario
	 */
	public Token getToken() {
		return token;
	}
	
	/** Cambia el token asociado al usuario
	 * @param token nuevo token del usuario
	 */
	public void setToken(Token token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		return this.nombre.hashCode();
	}

	@Override
	public boolean equals(Object obj) {	
		if (obj instanceof Usuario) {
			return ((Usuario) obj).nombre.equals(this.nombre);
		}else return false;
	}
	
	@Override
	public String toString() {
		return nombre;
	}	
}
