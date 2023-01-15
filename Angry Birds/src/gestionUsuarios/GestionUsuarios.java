package gestionUsuarios;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionUsuarios {

	private static final String LIBRERIA = "jdbc:sqlite:lib/users.db";
	private static final String FICHEROTOKEN = "archivos/token.dat";
	private static Logger logger = null;

	/** Añade un nuevo usuario a la base de datos
	 * @param usuario objeto de tipo usuario a añadir
	 * @throws SQLException En caso de que ocurra algún tipo de error relacionado con la gestión de la base de datos y no se pueda añadir el usuario
	 */
	public static void addUsuario(Usuario usuario) throws SQLException {
		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {

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
				log(Level.INFO, String.format("Usuario %s creado con exito", usuario.toString()), null);
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
			log(Level.INFO, String.format("Puntuaciones del usuario %s añadidas con exito", usuario.toString()), null);
		}
	}

	/** Actualiza los datos de un usuario en la base de datos
	 * @param usuario Objeto usuario YA ACTUALIZADO
	 * @throws SQLException Excepcion lanzada si no se consigue actualizar el usuario
	 */
	public static void actualizarUsuario(Usuario usuario) throws SQLException {
		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {

			//Para Utilizar la conexión
			//Se construye la plantilla de la SQL. 
			try(PreparedStatement insertSQL = conn.prepareStatement(String.format("UPDATE usuarios SET ID = ?, nombre = ?, contraseña = ?, tiempoJugado = ?, token = ?, caducidadToken = ? WHERE ID = %d", usuario.hashCode()))){

				//Rellenar plantilla
				insertSQL.setInt(1, usuario.getNombre().hashCode());
				insertSQL.setString(2, usuario.getNombre());
				insertSQL.setString(3, usuario.getContraseña());
				insertSQL.setLong(4, usuario.getTiempoJugado());
				if(usuario.getToken() != null) {
					insertSQL.setString(5, usuario.getToken().getToken());
					insertSQL.setString(6, usuario.getToken().getCaducidad().toString());
				}

				//Ejecutar sentencia
				insertSQL.executeUpdate();
				log(Level.INFO, String.format("Datos del usuario %s actualizado con exito", usuario.toString()), null);
			}	
		}
	}


	/** Añade una nueva puntuación a las puntuaciones del usuario en la base de datos
	 * @param usuario Usuario al que añadir la puntuacion
	 * @param puntuacion Puntuacion a añadir
	 * @throws SQLException Excepcion lanzada en caso de haber algun error relacionado con la base de datos a la hora de añadir la puntuacion
	 */
	public static void addPuntuacion(Usuario usuario, Puntuacion puntuacion) throws SQLException {

		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {
			
			try(PreparedStatement insertSQL = conn.prepareStatement("INSERT INTO puntuaciones (IDusuario, estrellas, nivel, fecha) VALUES (?, ?, ?, ?)")){

				//Rellenar la plantilla
				insertSQL.setInt(1, usuario.getNombre().hashCode());
				insertSQL.setInt(2, puntuacion.getEstrellas());
				insertSQL.setInt(3, puntuacion.getNivel());
				insertSQL.setString(4, puntuacion.getFecha());

				//Ejecutar sentencia
				insertSQL.executeUpdate();
			}
		}
		log(Level.INFO, String.format("Puntuaciones del usuario %s actualizadas con exito", usuario.toString()), null);
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

			//Utilizar la conexión
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT contraseña FROM usuarios WHERE ID = %s;", nombre.hashCode()))){
				boolean contraseñaCorrecta = contraseña.equals(stmt.executeQuery().getString("contraseña"));
				if(contraseñaCorrecta) {
					log(Level.INFO, String.format("La contraseña introducida para el usuario %s es correcta", nombre), null);
				}else {
					log(Level.INFO, String.format("La contraseña introducida para el usuario %s es incorrecta", nombre), null);
				}
				return contraseñaCorrecta;
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

			//Utilizar la conexión
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT nombre FROM usuarios WHERE ID = %s;", nombre.hashCode()))){
				try {
					boolean usuarioCorrecto = nombre.equals(stmt.executeQuery().getString("nombre"));
					if (usuarioCorrecto) {
						log(Level.INFO, String.format("El usuario %s existe", nombre), null);
					}else {
						log(Level.INFO, String.format("El usuario %s no existe", nombre), null);
					}
					return usuarioCorrecto;
				}catch(NullPointerException e) {
					log(Level.INFO, String.format("El usuario %s no existe", nombre), null);
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

			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT token, caducidadToken FROM usuarios WHERE ID = %d;", usuario.hashCode()))){
				stoken = stmt.executeQuery().getString("token");
				caducidad = stmt.executeQuery().getString("caducidadToken");

				//Un mismo usuario puede iniciar la sesion en varias terminales distintas
				if(stoken == null || caducidad == null) {
					token = new Token(usuario);
				}else {
					token = new Token(stoken, caducidad, usuario);	
				}
				log(Level.INFO, String.format("Token asocidao al usuario %s creado correctamente", usuario.toString()), null);
			}

			if(token.isCaducado()) {
				token = new Token(usuario);
				actualizarUsuario(usuario);
			}
			usuario.setToken(token);
			guardarTokenEnFichero(usuario);
			log(Level.INFO, String.format("Token asocidao al usuario %s guardado correctamente en la base de datos", usuario.toString()), null);
			return true;
		} catch (SQLException e) {
			log(Level.SEVERE, String.format("Error en el proceso de creación/guardado de token asociado al usuario %s", usuario.toString()), e);
			return false;
		}
	}

	/** Devuelve un usuario de la base de datos a partir de un ID
	 * @param ID ID del usuario
	 * @return Objeto usuario asociado a ese ID
	 */
	public static Usuario getUsuario(int ID) {
		List<Puntuacion> puntuaciones = new ArrayList<>();
		String nombre;
		String contraseña;
		long tiempoJugado;
		String sToken;
		String caducidadToken;

		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {

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
			log(Level.SEVERE, String.format("Error en el proceso de retorno del usuario asociado al ID %d", ID), e);
			return null;
		}
		Usuario usuario = new Usuario(nombre, contraseña, tiempoJugado, puntuaciones, null);
		if(sToken != null) {
			usuario.setToken(new Token(sToken, caducidadToken, usuario));
		}
		log(Level.INFO, String.format("Usuario asociado al ID %d encontrado y retornado correctamente", ID), null);
		return usuario;
	}
	
	/** Devuelve todos los usuarios de la base de datos
	 * @return Lista de usuarios
	 */
	public static List<Usuario> getUsuarios() {
		List<Usuario> usuarios = new ArrayList<>();

		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {

			try(PreparedStatement stmt = conn.prepareStatement("SELECT nombre FROM usuarios")){
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					usuarios.add(getUsuario(rs.getString("nombre").hashCode()));
				}
			}
		} catch (SQLException e) {
			log(Level.SEVERE, "Error en el retorno de la lista de los usuarios de la base de datos", e);
			e.printStackTrace();
			return null;
		}
		log(Level.INFO, String.format("Lista de usuarios < %s > creada y retornada correctamente", usuarios.toString().substring(1, usuarios.toString().length() - 1)), null);
		return usuarios;
	}
	
	

	/** Método que especifica si el dispositivo desde el que se esta ejecutando el programa tiene algún usuario asociado el cual ha pedido que se recuerde
	 * @return objeto usuario del usuario asociado, null si no hay ningún usuario asociado  
	 */
	public static Usuario usuarioAsociado(){
		Token token = cargarTokenDeFichero();

		//Token inexistente
		if(token == null) {
			log(Level.INFO, "El dispositivo no cuenta con ningún usuario asociado", null);
			return null;
		}

		//Lista de puntos del usuario
		List<Puntuacion> puntuaciones = new ArrayList<>();

		//ESTABLECER CONEXIÓN CON LA BASE DE DATOS
		try(Connection conn = DriverManager.getConnection(LIBRERIA)) {

			//Utilizar la conexión
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT estrellas, nivel, fecha FROM puntuaciones WHERE IDusuario = %s;", token.getUsuario().getNombre().hashCode()))){
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {

					//Mientras tenga filas cogemos cada columna contenida en la fila
					puntuaciones.add(new Puntuacion(rs.getString("fecha"), rs.getInt("estrellas"), rs.getInt("nivel")));
				}
			}
			try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT nombre, contraseña, tiempoJugado, token FROM usuarios WHERE ID = %s;", token.getUsuario().getNombre().hashCode()))){
				Usuario usuario = stmt.executeQuery().getString("token").equals(token.getToken())? new Usuario(stmt.executeQuery().getString("nombre"), stmt.executeQuery().getString("contraseña"), stmt.executeQuery().getInt("tiempoJugado"), puntuaciones, token): null;
				if(usuario == null) {
					log(Level.INFO, "El dispositivo no cuenta con ningún usuario asociado", null);					
				}else {
					log(Level.INFO, String.format("El dispositivo cuenta con el usuario %s asociado", usuario.toString()), null);
				}
				return usuario;
			}catch(NullPointerException e) {
				log(Level.INFO, "El dispositivo no cuenta con ningún usuario asociado", null);			
				return null;
			}

		} catch (SQLException e) {
			log(Level.SEVERE, "No se ha podido establecer una conexion con la base de datos", e);
			return null;
		}
	}

	/**Método que carga la libreria de la base de datos
	 * 
	 */
	public static void cargarLibreria() {
		try {
			Class.forName("org.sqlite.JDBC");
			log(Level.INFO, "Libreria de la base de datos cargada con exito", null);
		}catch(ClassNotFoundException e) {
			log(Level.SEVERE, "No se ha podido cargar la libreria de la base de datos", e);
		}
	}

	/** Guarda en un fichero el token asociado a un usuario
	 * @param usuario Usuario que contiene el token a guardar
	 * @throws IOException Excepcion lanzada en caso de no encontrar el fichero
	 */
	private static void guardarTokenEnFichero(Usuario usuario){
		try {
			Files.createDirectory(Paths.get("archivos"));
		} catch (IOException e) {
			log(Level.SEVERE, String.format("No ha sido posible crear el directorio para guardar el token en el fichero %s", FICHEROTOKEN), e);
			return;
		}
		
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(FICHEROTOKEN)))){
			oos.writeObject(usuario.getToken());
			log(Level.INFO, String.format("Token asocidao al usuario %s guardado correctamente en el fichero %s", usuario.toString(), FICHEROTOKEN), null);
		} catch (IOException e) {
			log(Level.SEVERE, String.format("No ha sido posible guardar el token en el fichero %s", FICHEROTOKEN), e);
		} 
	}

	/** Carga el token asociado a un usuario desde el fichero de tokens
	 * @return el token del usuario, null si no existe ningún token
	 */
	private static Token cargarTokenDeFichero() {
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(FICHEROTOKEN)))){
			return (Token) ois.readObject();
		}catch(IOException e) {
			log(Level.INFO, String.format("El usuario no cuenta con ningún token, pues el fichero %s no existe", FICHEROTOKEN), null);
			return null;			
		}catch(ClassNotFoundException e) {
			log(Level.SEVERE, String.format("Ha habído un error en la conversion de datos en el fichero %s", FICHEROTOKEN), e );
			return null;
		}
	}	

	/** Método para loggear un mensaje asocidado con la gestion de usuarios 
	 * @param level Uno de los identificadores de nivel de mensaje, por ejemplo, SEVERE
	 * @param mensage El mensaje de cadena (o una clave en el catálogo de mensajes)
	 * @param excepcion Throwable asociado con el mensaje de registro. Null si no existe ningún Throwable asociado
	 */
	public static void log(Level level, String mensage, Throwable excepcion) {
		if (logger == null) { 
			logger = Logger.getLogger("BD users");  
			logger.setLevel(Level.ALL);
			try {
				logger.addHandler(new FileHandler("lib/users.log.xml", true));
			}catch (Exception e) {
				logger.log(Level.SEVERE, "No se pudo crear el fichero de log", e);
			}
		}
		if (excepcion == null) {
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