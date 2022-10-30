package gestionUsuarios;

import java.util.TreeSet;

public class Usuario{
	
	private String nombre;						//nombre del usuario
	private String contraseña;					//contraseña del usuario
	private int tiempoJugado;				//tiempo total jugado por el usuario (en segundos)
	private TreeSet<Puntuacion> puntuaciones;	//treeset que guarda todas las puntuaciones conseguidas por el usuario
	

	/**Crea un nuevo usuario con el nombre y la contraseña especificada, pero con 0 tiempo jugado y el mapa de puntuaciones vacío
	 * @param nombre nombre del usuario
	 * @param contraseña contraseña del usuario
	 */
	public Usuario(String nombre, String contraseña) {
		this.nombre = nombre;
		this.contraseña = contraseña;
		this.tiempoJugado = 0;
		this.puntuaciones = new TreeSet<>();
	}
	
	
	
	/** Crea un nuevo usuario con el nombre, contraseña, tiempoJugado y TreeSet de puntuaciones especificado
	 * @param nombre nombre del usuario
	 * @param contraseña contraseña del usuario
	 * @param tiempoJugado tiempo jugado del usuario (en segundos)
	 * @param puntuaciones TreeSet de puntuaciones del usuario
	 */
	public Usuario(String nombre, String contraseña, int tiempoJugado, TreeSet<Puntuacion> puntuaciones) {
		this.nombre = nombre;
		this.contraseña = contraseña;
		this.tiempoJugado = tiempoJugado;
		this.puntuaciones = puntuaciones;
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

	@Override
	public int hashCode() {
		return this.nombre.hashCode();
	}

	@Override
	public boolean equals(Object obj) {	
		if (obj instanceof Usuario) {
			Usuario usuario = (Usuario) obj;
			return usuario.nombre.equals(this.nombre);
		}else return false;
	}
	
	@Override
	public String toString() {
		return String.format("Nombre: %s \t Contraseña: %s", nombre, contraseña);
	}	
}
