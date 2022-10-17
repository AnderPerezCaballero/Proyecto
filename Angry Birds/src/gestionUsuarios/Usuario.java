package gestionUsuarios;

import java.io.Serializable;
import java.util.Objects;
import java.util.TreeSet;

public class Usuario implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String nombre;						//nombre del usuario
	private String email;						//email del usuario
	private String contraseña;					//contraseña del usuario
	private double tiempoJugado;				//tiempo total jugado por el usuario (en segundos)
	private TreeSet<Puntuacion> puntuaciones;	//treeset que guarda todas las puntuaciones conseguidas por el usuario
	

	/**Crea un nuevo usuario con el nombre, email y la contraseña especificada, pero con 0 tiempo jugado y el mapa de puntuaciones vacío
	 * @param nombre nombre del usuario
	 * @param email email del usuario
	 * @param contraseña contraseña del usuario
	 */
	public Usuario(String nombre, String email, String contraseña) {
		this.nombre = nombre;
		this.email = email;
		this.contraseña = contraseña;
		this.tiempoJugado = 0;
		this.puntuaciones = new TreeSet<>();
	}
	
	/** Devuelve el nombre del usuario
	 * @return Nombre del usuario
	 */
	public String getNombre() {
		return nombre;
	}
	
	/** Actualiza el nombre del usuario
	 * @param nombre	Nuevo nombre del usuario
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/** Devuelve la contraseña del usuario
	 * @return Contraseña del usuario
	 */
	public String getContraseña() {
		return contraseña;
	}
	
	/** Actualiza la contraseña del usuario
	 * @param contraseña	Nueva contraseña del usuario
	 */
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	/** Devuelve el tiempo total jugado por el usuario
	 * @return	Tiempo jugado del usuario en total (en segundos), sin tener en cuenta las pantallas de inicio
	 */
	public double getTiempoJugado() {
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
	
	
	/**	Añade una nueva puntuación al mapa de puntuaciones del usuario, cuya fecha corresponde a la actual
	 * @param estrellas número de estrellas conseguidas
	 * @throws EstrellasIncorrectasException lanza esta excepción en el caso en el que el número de estrellas introducido sea incorrecto
	 */
	public void addPuntos(int estrellas) throws EstrellasIncorrectasException{
		Puntuacion puntuacion = new Puntuacion(estrellas, this);
		puntuaciones.add(puntuacion);
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
