package gestionUsuarios;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.Set;
import java.util.TreeSet;

public class GestionUsuarios {

	private static final String LIBRERIA = "jdbc:sqlite:lib/users.db";
	private static final String FICHEROTOKEN = "archivos/token.dat";
	private static Logger logger = null;

	/** Añade un nuevo usuario a la base de datos
	 * @param usuario objeto de tipo usuario a añadir
	 * @throws SQLException En caso de que ocurra algún tipo de error relacionado con la gestión de la base de datos y no se pueda añadir el usuario
	 */
	public static void add(Usuario usuario) throws SQLException {
		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {
			log(Level.INFO, "Conexion establecida con la base de datos", null);

			//Para Utilizar la conexión, se construye la plantilla de la SQL
			try(PreparedStatement insertSQL = conn.prepareStatement("INSERT INTO usuarios (ID, nombre, contraseña, tiempoJugado, token) VALUES (?, ?, ?, ?, ?)")){

				//Rellenar la plantilla
				insertSQL.setInt(1, usuario.getNombre().hashCode());
				insertSQL.setString(2, usuario.getNombre());
				insertSQL.setString(3, usuario.getContraseña());
				insertSQL.setLong(4, usuario.getTiempoJugado());

				try {
					insertSQL.setString(5, usuario.getToken().getToken());
				}catch(NullPointerException e) {
					insertSQL.setString(5, null);
				}

				//Ejecutar sentencia
				insertSQL.executeUpdate();
				log(Level.INFO, "Usuario insertado con exito", null);
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
					log(Level.INFO, "Puntuaciones insertadas con exito", null);
				}
			}
		}
	}

	/** Actualiza los datos de un usuario en la base de datos
	 * @param usuario Objeto usuario YA ACTUALIZADO
	 * @throws SQLException Si no se consigue actualizar el usuario
	 */
	public static void actualizarUsuario(Usuario usuario) throws SQLException {
		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {
			log(Level.INFO, "Conexion establecida con la base de datos", null);

			//Para Utilizar la conexión
			//Se construye la plantilla de la SQL. 
			try(PreparedStatement insertSQL = conn.prepareStatement(String.format("UPDATE usuarios SET ID = ?, nombre = ?, contraseña = ?, tiempoJugado = ?, token = ?, caducidadToken = ? WHERE ID = %d", usuario.hashCode()))){

				//Rellenar plantilla
				insertSQL.setInt(1, usuario.getNombre().hashCode());
				insertSQL.setString(2, usuario.getNombre());
				insertSQL.setString(3, usuario.getContraseña());
				insertSQL.setLong(4, usuario.getTiempoJugado());
				insertSQL.setString(5, usuario.getToken().getToken());
				insertSQL.setString(6, usuario.getToken().getCaducidad().toString());

				//Ejecutar sentencia
				insertSQL.executeUpdate();
				log(Level.INFO, "Usuario actualizado con exito", null);
			}	

			for(Puntuacion puntuacion : usuario.getPuntuaciones()) {
				try(PreparedStatement insertSQL = conn.prepareStatement(String.format("UPDATE puntuaciones SET IDusuario = ?, estrellas = ?, nivel = ?, fecha = ? = ? WHERE IDusuario = %d", usuario.hashCode()))){

					//Rellenar la plantilla
					insertSQL.setInt(1, usuario.getNombre().hashCode());
					insertSQL.setInt(2, puntuacion.getEstrellas());
					insertSQL.setInt(3, puntuacion.getNivel());
					insertSQL.setString(4, puntuacion.getFecha());

					//Ejecutar sentencia
					insertSQL.executeUpdate();
					log(Level.INFO, "Puntuaciones actualizadas con exito", null);
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
		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {
			log(Level.INFO, "Conexion establecida con la base de datos", null);

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
		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {
			log(Level.INFO, "Conexion establecida con la base de datos", null);

			//Utilizar la conexión
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT nombre FROM usuarios WHERE ID = %s;", nombre.hashCode()))){
				try {
					if (stmt.executeQuery().getString("nombre").equals(nombre)) {
						log(Level.INFO, "El usuario existe", null);
						return true;
					}else {
						log(Level.INFO, "El usuario no existe", null);
						return false;
					}
				}catch(NullPointerException e) {
					log(Level.WARNING, "No se ha reconocido el usuario", null);
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
		Token token;
		String stoken;
		String caducidad;
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {
			log(Level.INFO, "Conexion establecida con la base de datos", null);
			
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT token, caducidadToken FROM usuarios WHERE ID = %d;", usuario.hashCode()))){
				stoken = stmt.executeQuery().getString("token");
				caducidad = stmt.executeQuery().getString("caducidadToken");

				//Un mismo usuario puede iniciar la sesion en varias terminales distintas
				if(stoken == null || caducidad == null) {
					token = new Token(usuario);
				}else {
					token = new Token(stoken, caducidad, usuario);	
				}
			}

			usuario.setToken(token);
			guardarTokenEnFichero(usuario);
			if(token.isCaducado()) {
				token = new Token(usuario);
				actualizarUsuario(usuario);
			}
			log(Level.INFO, "Guardado realizado correctamente", null);
			return true;
		} catch (SQLException e) {
			log(Level.SEVERE, "No se ha podido establecer una conexion con la base de datos", e);
			e.printStackTrace();
			return false;
		}
	}

	/** Devuelve un usuario de la base de datos a partir de un ID
	 * @param ID ID del usuario
	 * @return Objeto usuario asociado a ese ID
	 */
	public static Usuario getUsuario(int ID) {
		Set<Puntuacion> puntuaciones = new TreeSet<>();
		String nombre;
		String contraseña;
		long tiempoJugado;
		String sToken;
		String caducidadToken;
		
		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {
			log(Level.INFO, "Conexion establecida con la base de datos", null);
			
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT nombre, contraseña, tiempoJugado, token, caducidadToken FROM usuarios WHERE ID = %d;", ID))){
				ResultSet rs = stmt.executeQuery();
				nombre = rs.getString("nombre");
				contraseña = rs.getString("contraseña");
				tiempoJugado = rs.getInt("tiempoJugado");
				sToken = rs.getString("token");
				caducidadToken = rs.getString("caducidadToken");				
			}
			
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT estrellas, nivel, fecha FROM puntuaciones WHERE IDusuario = %d;", ID))){
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					//Mientras hayan filas cogemos cada columna contenida en la fila
					puntuaciones.add(new Puntuacion(rs.getString("fecha"), rs.getInt("estrellas"), rs.getInt("nivel")));
				}
			}

		} catch (SQLException e) {
			log(Level.SEVERE, "No se ha podido establecer una conexion con la base de datos", e);
			e.printStackTrace();
			return null;
		}
		Usuario usuario = new Usuario(nombre, contraseña, tiempoJugado, puntuaciones, null);
		usuario.setToken(new Token(sToken, caducidadToken, usuario));
		return usuario;
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
			log(Level.WARNING, "No se ha encontrado ningun token en el fichero", e);
			return null;
		}

		//Lista de puntos del usuario
		Set<Puntuacion> puntuaciones = new TreeSet<>();

		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {
			log(Level.INFO, "Conexion establecida con la base de datos", null);

			//Utilizar la conexión
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT estrellas, nivel, fecha FROM puntuaciones WHERE IDusuario = %s;", token.getUsuario().getNombre().hashCode()))){
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					//Mientras tnega filas cogemos cada columna contenida en la fila
					puntuaciones.add(new Puntuacion(rs.getString("fecha"), rs.getInt("estrellas"), rs.getInt("nivel")));
				}
			}
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT nombre, contraseña, tiempoJugado, token FROM usuarios WHERE ID = %s;", token.getUsuario().getNombre().hashCode()))){
				Usuario us = stmt.executeQuery().getString("token").equals(token.getToken())? new Usuario(stmt.executeQuery().getString("nombre"), stmt.executeQuery().getString("contraseña"), stmt.executeQuery().getInt("tiempoJugado"), puntuaciones, token): null;
				if (us != null) {
					log(Level.INFO, "Asociacion de usuario correcta", null);
					return us;
				}else {
					return null;
				}
			}catch(NullPointerException e) {
				log(Level.WARNING, "Fallo en la asociacion de usuarios", e);
				return null;
			}

		} catch (SQLException e) {
			log(Level.SEVERE, "No se ha podido establecer una conexion con la base de datos", e);
			e.printStackTrace();
			return null;
		} catch(NullPointerException e) {
			log(Level.INFO, "No se ha encontrado ningun usuario asociado", e);
			e.printStackTrace();
			return null;
		}
	}

	/**Método que carga la libreria de la base de datos
	 * 
	 */
	public static void cargarLibreria() {
		try {
			Class.forName("org.sqlite.JDBC");
			log(Level.INFO, "Libreria cargada con exito", null);
		}catch(ClassNotFoundException e) {
			log(Level.SEVERE, "No se ha podido cargar la libreria", e);
			e.printStackTrace();
		}
	}

	/** Guarda en un fichero el token asociado a un usuario
	 * @param usuario Usuario que contiene el token a guardar
	 * @throws IOException Excepcion lanzada en caso de no encontrar el fichero
	 */
	private static void guardarTokenEnFichero(Usuario usuario){
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(FICHEROTOKEN)))){
			oos.writeObject(usuario.getToken());
		} catch (FileNotFoundException e) {
			log(Level.SEVERE, "No se ha encontrado ningun fichero llamado: token.dat", e);
			e.printStackTrace();
		} catch (IOException e) {
			log(Level.SEVERE, "No se ha podido leer el fichero llamado: token.dat", e);
			e.printStackTrace();
		} 
	}

	/** Carga el token asociado a un usuario desde el fichero de tokens
	 * @return el token del usuario, null si no existe ningún token
	 */
	private static Token cargarTokenDeFichero() {
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(FICHEROTOKEN)))){
			return (Token) ois.readObject();
		}catch(IOException e) {
			log(Level.SEVERE, "No se ha podido leer el fichero llamado: token.dat", e);
			e.printStackTrace();
			return null;			
		}catch(ClassNotFoundException e) {
			log(Level.SEVERE, "Ha habído un error en la conversion de datos en el fichero token.dat", e );
			e.printStackTrace();
			return null;
		}
	}	

	@SuppressWarnings("unused")
	private static void log(Level level, String mensage, Throwable excepcion) {
		if (logger==null) { 
			logger=Logger.getLogger("BD users");  
			logger.setLevel(Level.ALL);
			try {
				logger.addHandler(new FileHandler("users.log.xml", true));
			}catch (Exception e) {
				logger.log(Level.SEVERE, "No se pudo crear el fichero de log", e);
			}
		}
		if (excepcion==null) {
			logger.log(level, mensage);
		}
		else {
			logger.log(level, mensage, excepcion);
		}
	}

	//	private void crearTablas() throws SQLException{
	//	
	//	try {
	//		
	//		Connection conexion = DriverManager.getConnection(LIBRERIA);
	//		Statement stmnt = conexion.createStatement();
	//		stmnt.executeUpdate("CREATE TABLE if not exists Nivel(rutaMapa String, id Integer, numCerdos Integer)");
	//		log(Level.INFO, "Creada la tabla Nivel en la BD users", null );
	//		
	//	} catch (Exception e) {
	//		log(Level.SEVERE, "La tabla Nivel no se ha podido crear en la BD users", e);
	//		e.printStackTrace();
	//	}
	//}
	//
	//private void anyadirNivel(Nivel nvl) {
	//	
	//	try {
	//		
	//		Connection conexion = DriverManager.getConnection(LIBRERIA);
	//		Statement stmnt = conexion.createStatement();
	//		stmnt.executeUpdate(String.format("INSERT INTO Nivel VALUES( %s, %d, %d)", nvl.getRutaMapa(), nvl.getId(), nvl.getNumCerdos()));
	//		log(Level.INFO, "Nivel anyadido correctamente", null);
	//		
	//	}catch (Exception e) {
	//		log(Level.SEVERE, "No se ha podido anyadir el nivel correctamente", e);
	//		e.printStackTrace();
	//		
	//	}
	//}
	//
	//private List<Nivel> cargarNivel() {
	//	
	//	try {
	//		Connection conexion = DriverManager.getConnection(LIBRERIA);
	//		Statement stmnt = conexion.createStatement();
	//		String stringSQL = "SELECT * FROM Nivel";
	//		ResultSet rs = stmnt.executeQuery(stringSQL);
	//		
	//		List<Nivel> listaNiveles = new ArrayList<Nivel>();
	//		
	//		while(rs.next()) {
	//			Nivel nvl = new Nivel(rs.getString("rutaMapa"), rs.getInt("numCerdos"), rs.getInt("id"), null);
	//			listaNiveles.add(nvl);			
	//		}
	//		rs.close();
	//		log(Level.INFO, "Todos los niveles de la BD users han sido seleccionados con exito", null);
	//		return listaNiveles;
	//		
	//	}catch (Exception e) {
	//		log(Level.SEVERE, "No se han podido seleccionar todos los niveles de la BD users", e);
	//		e.printStackTrace();
	//		return null;	
	//	}		
	//	
	//}

}