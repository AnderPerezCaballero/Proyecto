package gestionUsuarios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GestionUsuarios {

	/** Añade un nuevo usuario a la base de datos
	 * @param usuario	objeto de tipo usuario a añadir
	 */
	public static void add(Usuario usuario) {
		cargarLibreria();

		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection("jdbc:sqlite:lib/users.db")) {

			//Para Utilizar la conexión, se construye la plantilla de la SQL
			try(PreparedStatement insertSQL = conn.prepareStatement(String.format("INSERT INTO usuarios (ID, nombre, contraseña, tiempoJugado) VALUES (?, ?, ?, ?)"))){
				
				//Rellenar la plantilla
				insertSQL.setInt(1, usuario.getNombre().hashCode());
				insertSQL.setString(2, usuario.getNombre());
				insertSQL.setString(3, usuario.getContraseña());
				insertSQL.setInt(4, usuario.getTiempoJugado());

				//Ejecutar sentencia
				insertSQL.executeUpdate();
			}	

			try {
				for(Puntuacion puntuacion : usuario.getPuntuaciones()) {
					try(PreparedStatement insertSQL = conn.prepareStatement(String.format("INSERT INTO puntuaciones (ID, IDusuario, estrellas, nivel, fecha) VALUES (?, ?, ?, ?, ?)"))){
						
						//Rellenar la plantilla
						insertSQL.setInt(1, puntuacion.getFecha().hashCode());
						insertSQL.setInt(2, usuario.getNombre().hashCode());
						insertSQL.setInt(3, puntuacion.getEstrellas());
						insertSQL.setInt(4, puntuacion.getNivel());
						insertSQL.setString(5, puntuacion.getFecha());
						
						//Ejecutar sentencia
						insertSQL.executeUpdate();
					}
				}
			}catch(NullPointerException e) {}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** Actualiza los datos de un usuario en la base de datos
	 * @param usuario Objeto usuario YA ACTUALIZADO
	 */
	public static void actualizarUsuario(Usuario usuario) {
		cargarLibreria();

		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection("jdbc:sqlite:lib/users.db")) {

			//Para Utilizar la conexión
			//Se construye la plantilla de la SQL. Atencion a los huecos para los valores marcados con "?"
			try(PreparedStatement insertSQL = conn.prepareStatement(String.format("UPDATE usuarios SET ID = ?, nombre = ?, contraseña = ?, tiempoJugado = ? WHERE ID = %d", usuario.hashCode()))){

				//Insertamos una nueva fila en la base de datos
				insertSQL.setInt(1, usuario.getNombre().hashCode());
				insertSQL.setString(2, usuario.getNombre());
				insertSQL.setString(3, usuario.getContraseña());
				insertSQL.setInt(4, usuario.getTiempoJugado());

				//Ejecutar sentencia
				insertSQL.executeUpdate();
			}	

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** Método que comprueba si la contraseña introducida para un usuario es correcta
	 * @param nombre nombre del usuario
	 * @param contraseña contraseña del usuario
	 * @return	true si es correcto, false si no lo es
	 * @throws SQLException En caso de que ocurra algún tipo de error relacionado con la gestión de la base de datos
	 */
	public static boolean comprobarContraseña(String nombre, String contraseña) throws SQLException{
		cargarLibreria();

		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection("jdbc:sqlite:lib/users.db")) {

			//Utilizar la conexión
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT contraseña FROM usuarios WHERE ID = %s;", nombre.hashCode()))){
				return stmt.executeQuery().getString("contraseña").equals(contraseña);
			}
		}
	}

	/** Comprueba si un usuario existe en la base de datos
	 * @param nombre nombre del usuario a comprobar
	 * @return true si el usuario existe, false si no
	 * @throws SQLException En caso de que ocurra algún tipo de error relacionado con la gestión de la base de datos
	 */
	public static boolean comprobarUsuario(String nombre) throws SQLException{
		cargarLibreria();

		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection("jdbc:sqlite:lib/users.db")) {

			//Utilizar la conexión
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT nombre FROM usuarios WHERE ID = %s;", nombre.hashCode()))){
				 try {
					 return stmt.executeQuery().getString("nombre").equals(nombre);
				 }catch(NullPointerException e) {
					 return false;
				 }
			}
		}
	}
	
	/**Método que carga la libreria de la base de datos
	 * 
	 */
	private static void cargarLibreria() {
		try {
			Class.forName("org.sqlite.JDBC");
		}catch(ClassNotFoundException e) {
			System.err.println("No se ha podido cargar el driver de la base de datos");
		}
	}
}