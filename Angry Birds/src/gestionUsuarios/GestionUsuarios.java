package gestionUsuarios;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class GestionUsuarios {

	private HashMap<String, Usuario> mapaUsuarios;	//Mapa de usuarios, cuya clave es el nombre del usuario

	/** Crea un nuevo objeto de gestion de usuarios con un mapa de usuarios vacio
	 * 
	 */
	public GestionUsuarios() {
		this.mapaUsuarios =  new HashMap<>();
	}

	/** Añade un nuevo usuario al mapa
	 * @param usuario	objeto de tipo usuario a añadir
	 */
	public void add(Usuario usuario) {
		mapaUsuarios.put(usuario.getNombre(), usuario);
	}

	/**
	 *  Método que comprueba si un usuario es correcto o no
	 * @param usuario usuario a comprobar
	 * @return	true si es correcto, false si no lo es
	 * @throws NullPointerException excepcion lanzada en caso de que el usuario no esté en el mapa
	 */
	public boolean comprobarUsuario(Usuario usuario) throws NullPointerException{
		return mapaUsuarios.get(usuario.getNombre()).getContraseña().equals(usuario.getContraseña());
	}

	/** Método que devuelve un usuario del mapa
	 * @param nombre	Nombre del usuario
	 * @return	Usuario con el nombre asociado
	 */
	public Usuario getUsuario(String nombre) {
		return mapaUsuarios.get(nombre);
	}

	/**Elimina un usuario del mapa
	 * @param usuario	Usuario a eliminar
	 * @return	true si se ha eliminado, false si no
	 */
	public boolean remove(Usuario usuario) {
		return mapaUsuarios.remove(usuario.getNombre(), usuario);
	}

	/**Actualiza los datos de un usuario del mapa
	 * @param usuario	Usuario al que actualizarle los datos
	 * @param tiempoJugado	Tiempo jugado a incrementar respecto al total
	 * @param estrellas	estrellas conseguidos en el nivel
	 * @throws EstrellasIncorrectasException esta excepcion se lanza en el caso que el número de estrellas introducidas sea incorrecto
	 */
	public void actualizarUsuario(Usuario usuario, double tiempoJugado, int estrellas) throws EstrellasIncorrectasException{
		Usuario user = mapaUsuarios.get(usuario.getNombre());
		user.addTiempo(tiempoJugado);
		user.addPuntos(estrellas);
	}

	/**	Comprueba si un usuario está almacenado en el mapa
	 * @param usuario	nombre del usuario a comprobar	
	 * @return	true si está, false si no
	 */
	public boolean contains(String nombreUsuario) {
		return mapaUsuarios.containsKey(nombreUsuario);	
	}

	/**Guarda la información recogida en el mapa en la base de datos
	 * 
	 */
	public void guardarDatos() {
		//TODO Programar método que guarde los datos de los usuarios en la base de datos
	}

	/**Carga los datos del mapa de la base de datos
	 * 
	 */
	public void cargarDatos(String nomFichero) {
		//TODO Programar método que carge los datos de los usuarios de la base de datos
	}

	@Override
	public String toString() {
		return String.format("Mapa de usuarios: %s", mapaUsuarios.toString());
	}


}


