package gestionUsuarios;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.FileHandler;

import objetos.Nivel;

public class GestionUsuarios {

	private static final String LIBRERIA = "jdbc:sqlite:lib/users.db";
	private static final String FICHEROTOKEN = "archivos/token.dat";
	private static Logger logger = null;

	/** Añade un nuevo usuario a la base de datos
	 * @param usuario objeto de tipo usuario a añadir
	 * @throws SQLException En caso de que ocurra algún tipo de error relacionado con la gestión de la base de datos y no se pueda añadir el usuario
	 */
	public static void add(Usuario usuario) throws SQLException {
		cargarLibreria();

		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {

			//Para Utilizar la conexión, se construye la plantilla de la SQL
			try(PreparedStatement insertSQL = conn.prepareStatement("INSERT INTO usuarios (ID, nombre, contraseña, tiempoJugado, token) VALUES (?, ?, ?, ?, ?)")){

				//Rellenar la plantilla
				insertSQL.setInt(1, usuario.getNombre().hashCode());
				insertSQL.setString(2, usuario.getNombre());
				insertSQL.setString(3, usuario.getContraseña());
				insertSQL.setInt(4, usuario.getTiempoJugado());

				try {
					insertSQL.setString(5, usuario.getToken().getToken());
				}catch(NullPointerException e) {
					insertSQL.setString(5, null);
				}

				//Ejecutar sentencia
				insertSQL.executeUpdate();
			}	

			for(Puntuacion puntuacion : usuario.getPuntuaciones()) {
				try(PreparedStatement insertSQL = conn.prepareStatement("INSERT INTO puntuaciones (IDusuario, estrellas, nivel, fecha, token, caducidadToken) VALUES (?, ?, ?, ?, ?, ?)")){

					//Rellenar la plantilla
					insertSQL.setInt(1, usuario.getNombre().hashCode());
					insertSQL.setInt(2, puntuacion.getEstrellas());
					insertSQL.setInt(3, puntuacion.getNivel());
					insertSQL.setString(4, puntuacion.getFecha());
					insertSQL.setString(5, usuario.getToken().getToken());
					insertSQL.setString(6, usuario.getToken().getCaducidad().toString());

					//Ejecutar sentencia
					insertSQL.executeUpdate();
				}
			}
		}
	}

	/** Actualiza los datos de un usuario en la base de datos
	 * @param usuario Objeto usuario YA ACTUALIZADO
	 * @throws SQLException Si no se consigue actualizar el usuario
	 */
	public static void actualizarUsuario(Usuario usuario) throws SQLException {
		cargarLibreria();

		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {

			//Para Utilizar la conexión
			//Se construye la plantilla de la SQL. Atencion a los huecos para los valores marcados con "?"
			try(PreparedStatement insertSQL = conn.prepareStatement(String.format("UPDATE usuarios SET ID = ?, nombre = ?, contraseña = ?, tiempoJugado = ?, token = ?, caducidadToken = ? WHERE ID = %d", usuario.hashCode()))){
				
				//Rellenar plantilla
				insertSQL.setInt(1, usuario.getNombre().hashCode());
				insertSQL.setString(2, usuario.getNombre());
				insertSQL.setString(3, usuario.getContraseña());
				insertSQL.setInt(4, usuario.getTiempoJugado());
				insertSQL.setString(5, usuario.getToken().getToken());
				insertSQL.setString(6, usuario.getToken().getCaducidad().toString());

				//Ejecutar sentencia
				insertSQL.executeUpdate();
			}	
			
			for(Puntuacion puntuacion : usuario.getPuntuaciones()) {
				try(PreparedStatement insertSQL = conn.prepareStatement(String.format("UPDATE puntuaciones SET IDusuario = ?, estrellas = ?, nivel = ?, fecha = ?, token = ?, caducidadToken = ? WHERE IDusuario = %d", usuario.hashCode()))){
					//Rellenar la plantilla
					insertSQL.setInt(1, usuario.getNombre().hashCode());
					insertSQL.setInt(2, puntuacion.getEstrellas());
					insertSQL.setInt(3, puntuacion.getNivel());
					insertSQL.setString(4, puntuacion.getFecha());
					insertSQL.setString(5, usuario.getToken().getToken());
					insertSQL.setString(6, usuario.getToken().getCaducidad().toString());

					//Ejecutar sentencia
					insertSQL.executeUpdate();
				}
			}
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
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {

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
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {

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

	/** Método que crea un token asociado a un usuario para que se recuerde su sesión, y lo guarda en la base de datos y en un fichero 
	 * @param usuario Usuario que se desea recordar
	 * @return true si se consigue guardar, false si no
	 */
	public static boolean recordarUsuario(Usuario usuario){
		cargarLibreria();
		Token token;
		String stoken;
		String caducidad;
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT token, caducidadToken FROM usuarios WHERE ID = %d;", usuario.hashCode()))){
				stoken = stmt.executeQuery().getString("token");
				caducidad = stmt.executeQuery().getString("caducidadToken");
				
				//Un mismo usuario puede iniciar la sesion en varias terminales distintas
				if(stoken == null || caducidad == null) {
					token = new Token(usuario);
				}else {
					token = new Token(stoken, caducidad, usuario);	
					if(token.isCaducado()) {
						token = new Token(usuario);
					}
				}
			}
			usuario.setToken(token);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		try {
			guardarTokenEnFichero(usuario);
			
			//Solo se crea un nuevo token en caso de que sea la primera vez que se pide para ese usuario que se guarde en un dispositivo 
			if(stoken == null || caducidad == null) {
				actualizarUsuario(usuario);
			}
			return true;
		}catch(IOException e) {
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}		
	}

	/**Método que especifica si el dispositivo desde el que se esta ejecutando el programa tiene algún usuario asociado el cual ha pedido que se recuerde
	 * @return objeto usuario del usuario asociado, null si no hay ningún usuario asociado  
	 */
	public static Usuario usuarioAsociado(){
		Token token = cargarTokenDeFichero();

		//Token caducado o inexistente
		try {
			if(token == null) {
				return null;
			}
		}catch(NullPointerException e) {
			return null;
		}

		//Lista de puntos del usuario
		TreeSet<Puntuacion> puntuaciones = new TreeSet<>();

		cargarLibreria();

		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {

			//Utilizar la conexión
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT estrellas, nivel, fecha FROM puntuaciones WHERE IDusuario = %s;", token.getUsuario().getNombre().hashCode()))){
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					//Mientras tnega filas cogemos cada columna contenida en la fila
					puntuaciones.add(new Puntuacion(rs.getString("fecha"), rs.getInt("estrellas"), rs.getInt("nivel")));
				}
			}
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT nombre, contraseña, tiempoJugado, token FROM usuarios WHERE ID = %s;", token.getUsuario().getNombre().hashCode()))){
				return stmt.executeQuery().getString("token").equals(token.getToken())? new Usuario(stmt.executeQuery().getString("nombre"), stmt.executeQuery().getString("contraseña"), stmt.executeQuery().getInt("tiempoJugado"), puntuaciones, token): null;
			}catch(NullPointerException e) {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch(NullPointerException e) {
			e.printStackTrace();
			return null;
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

	/** Guarda en un fichero el token asociado a un usuario
	 * @param usuario Usuario que contiene el token a guardar
	 * @throws IOException Excepcion lanzada en caso de no encontrar el fichero
	 */
	private static void guardarTokenEnFichero(Usuario usuario) throws IOException{
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(FICHEROTOKEN)))){
			oos.writeObject(usuario.getToken());
		}
	}

	/** Carga el token asociado a un usuario desde el fichero de tokens
	 * @return el token del usuario, null si no existe ningún token
	 */
	private static Token cargarTokenDeFichero() {
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(FICHEROTOKEN)))){
			return (Token) ois.readObject();
		}catch(IOException e) {
			return null;			
		}catch(ClassNotFoundException e) {
			System.err.format("Error en la conversión de datos en %s", FICHEROTOKEN);
			return null;
		}
	}
	
	private void log(Level level, String mensage, Throwable excepcion) {
		if (logger==null) { 
			logger=Logger.getLogger("BD users");  
			logger.setLevel(Level.ALL);
			try {
				logger.addHandler(new FileHandler("users.log.xml", true));
			}catch (Exception e) {
				logger.log(Level.SEVERE, "No se pudo crear el fichero de log", e);
			}
		}
		if (excepcion==null)
			logger.log(level, mensage);
		else
			logger.log(level, mensage, excepcion);
	}
}