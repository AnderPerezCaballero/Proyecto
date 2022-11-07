package gestionUsuarios;

import java.io.Serializable;
import java.util.TreeSet;

public class Usuario implements Serializable{

	private static final long serialVersionUID = -1888981546445053100L;
	
	private String nombre;						//nombre del usuario
	private String contraseña;					//contraseña del usuario
	private int tiempoJugado;					//tiempo total jugado por el usuario (en segundos)
	private TreeSet<Puntuacion> puntuaciones;	//treeset que guarda todas las puntuaciones conseguidas por el usuario
	private Token token;						//token asociado al usuario
	

	/**Crea un nuevo usuario con el nombre y la contraseña especificada, pero con 0 tiempo jugado, el mapa de puntuaciones vacío y ningún token asociado
	 * @param nombre nombre del usuario
	 * @param contraseña contraseña del usuario
	 */
	public Usuario(String nombre, String contraseña) {
		this.nombre = nombre;
		this.contraseña = contraseña;
		this.tiempoJugado = 0;
		this.puntuaciones = new TreeSet<>();
		this.token = null;
	}
	
	
	/** Crea un nuevo usuario con el nombre, contraseña, tiempoJugado, TreeSet de puntuaciones y token especificado
	 * @param nombre nombre del usuario
	 * @param contraseña contraseña del usuario
	 * @param tiempoJugado tiempo jugado del usuario (en segundos)
	 * @param puntuaciones TreeSet de puntuaciones del usuario
	 * @param token token asociado al usuario
	 */
	public Usuario(String nombre, String contraseña, int tiempoJugado, TreeSet<Puntuacion> puntuaciones, Token token) {
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
	public int getTiempoJugado() {
		return tiempoJugado;
	}

	/** Cambia el token asociado al usuario
	 * @param token nuevo token del usuario
	 */
	public void setToken(Token token) {
		this.token = token;
	}
	
	/** Añade cierta cantidad de segundos al tiempo total jugado por el usuario
	 * @param tiempo Segundos a añadir al total
	 */
	public void addTiempo(double tiempo) {
		this.tiempoJugado += tiempo;
	}
	
	/** Devuelve el TreeSet de puntuaciones del usuario
	 * @return TreeSet de puntuaciones del usuario
	 */
	public TreeSet<Puntuacion> getPuntuaciones(){
		return puntuaciones;
	}	
	
	/** Devuelve el token asociado al usuario
	 * @return token del usuario
	 */
	public Token getToken() {
		return token;
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
		return String.format("Nombre: %s \t Contraseña: %s", nombre, contraseña);
	}	
}
