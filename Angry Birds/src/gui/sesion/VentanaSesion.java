package gui.sesion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import gestionUsuarios.GestionUsuarios;
import gestionUsuarios.Usuario;
import gui.componentes.MensajeCarga;
import gui.componentes.MiBoton;
import gui.inicio.VentanaJugar;
import gui.inicio.VentanaJugar1;
import gui.juego.VentanaNiveles;

@SuppressWarnings("serial")
public abstract class VentanaSesion extends JFrame{

	//Atributos estáticos de la ventana
	private static int anchuraVentana = 400;
	private static int alturaVentana = 700;
	private static Color fondo = new Color(35, 39, 42);
	protected static int numColumnas = 30;

	//Referencia de la ventana anterior
	private static VentanaJugar1 ventanaAnterior;

	//Contenedores
	private JPanel panelSuperior;
	private JPanel panelCentral;
	protected JPanel panelDatos;
	private JPanel panelInferior;

	private JPanel panelUsuario;
	private JPanel panelInputUsuario;
	private JPanel panelContraseña;
	private JPanel panelInputContraseña;
	protected JPanel panelMensaje;
	private JPanel panelAceptar;
	protected JPanel panelGuardarDispositivo;

	//JLabels
	private JLabel labelUsuario;
	private JLabel labelContraseña;
	protected JLabel labelMensaje;	
	private JLabel volver;
	private JLabel imagenPrincipal;
	protected JCheckBox guardarDispositivo;

	//Botones
	protected MiBoton botonAceptar;

	//Campos de texto
	protected JTextField inputUsuario;
	protected JPasswordField inputContraseña;

	//Eventos que se usarán en herencia
	protected KeyListener cierraConEsc;

	//Variable que indica el estado de la ventana
	private boolean estaCerrada;

	//Hilo ejecutado al cargar los datos del usuario
	protected MensajeCarga mensajeDeCarga;

	//Usuario que va a hacer uso de la aplicación
	private static Usuario usuario;
	
	//Variable que indica el borrado del mensaje que sale por pantalla
	protected boolean borrar;
	
	//Propiedades de la ventana
	private Properties propiedades;
	
	/** Constructor de la ventana 
	 * @param numeroDeDatos Variable que indica el número de datos que va a contener el gridLayout
	 * @param anteriorVentana ventana a la que se debe volver en caso de que el usuario decida ir hacia atrás
	 */
	public VentanaSesion(int numeroDeDatos, VentanaJugar1 anteriorVentana) {

		propiedades = new Properties();
		
		//Se inicializa el label primero que todo para sacar mensajes en la ventana en caso de error
		labelMensaje = new JLabel();
		
		// Carga el fichero de propiedades en el objeto Properties
		try (InputStream input = new FileInputStream("lib/propiedades.properties")) {
		    propiedades.load(input);
		    
		    int r = Integer.parseInt(propiedades.getProperty("Fondo_R"));
		    int g = Integer.parseInt(propiedades.getProperty("Fondo_G"));
		    int b = Integer.parseInt(propiedades.getProperty("Fondo_B"));
		    fondo = new Color(r, g, b);
		    
		    anchuraVentana = Integer.parseInt(propiedades.getProperty("Anchura_Ventana"));
		    alturaVentana = Integer.parseInt(propiedades.getProperty("Altura_Ventana"));
			
		} catch (IOException | IllegalArgumentException e) {
		    labelMensaje.setText("Propiedades de la ventana no cargadas");
		}	
		
		ventanaAnterior = anteriorVentana;
		// Inicialización de la ventana
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(anchuraVentana, alturaVentana);
		this.estaCerrada = false;
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaSesion.class.getResource("/imgs/Icono.png")));

		panelAceptar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelCentral = new JPanel(new GridLayout(2, 1));
		panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));

		panelDatos = new JPanel(new GridLayout(numeroDeDatos, 1));
		imagenPrincipal = new JLabel(imagenReescalada("/imgs/Fondo.png", 220, 220));

		panelUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelInputUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelContraseña = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelInputContraseña = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelMensaje = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelGuardarDispositivo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

		labelUsuario = new JLabel("Usuario:");
		labelContraseña = new JLabel("Contraseña:");
		inputUsuario = new JTextField(numColumnas);
		inputContraseña = new JPasswordField(numColumnas);

		botonAceptar = new MiBoton(Color.WHITE, fondo.brighter(), 35, 35);

		volver = new JLabel(imagenReescalada("/imgs/FlechaBlanca.png", 40, 40));

		guardarDispositivo = new JCheckBox("Guardar Dispositivo");

		// Configurar componentes
		botonAceptar.setEnabled(false);
		botonAceptar.setToolTipText("Pulsa aquí para confirmar tus datos");
		botonAceptar.setPreferredSize(new Dimension(anchuraVentana - 40, 40));

		//Añadir componentes a contenedores				
		panelUsuario.add(labelUsuario);
		panelContraseña.add(labelContraseña);
		panelInputUsuario.add(inputUsuario);
		panelInputContraseña.add(inputContraseña);
		panelAceptar.add(botonAceptar);
		panelMensaje.add(labelMensaje);
		panelGuardarDispositivo.add(guardarDispositivo);


		getContentPane().add(panelSuperior, "North");
		getContentPane().add(panelCentral, "Center");
		getContentPane().add(panelInferior, "South");

		panelDatos.add(panelUsuario);
		panelDatos.add(panelInputUsuario);
		panelDatos.add(panelContraseña);
		panelDatos.add(panelInputContraseña);

		panelCentral.add(imagenPrincipal);
		panelCentral.add(panelDatos);

		panelSuperior.add(volver);

		panelInferior.add(panelAceptar);

		borrar = true;
		
		//EVENTOS
		cierraConEsc = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cerrar(VentanaSesion.this);
				}
			}
		};

		//A todos los elementos que pueden tener foco les hago que cuando se presione el escape se cierren 
		inputUsuario.addKeyListener(cierraConEsc);
		inputContraseña.addKeyListener(cierraConEsc);
		botonAceptar.addKeyListener(cierraConEsc);
		guardarDispositivo.addKeyListener(cierraConEsc);

		inputUsuario.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (inputUsuario.getText() != null) {
					inputContraseña.requestFocus();
				}
			}
		});

		botonAceptar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				siguienteVentana();
			}
		});

		botonAceptar.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					siguienteVentana();
				}
			}

		});

		volver.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {
				volver.setIcon(imagenReescalada("/imgs/FlechaBlanca.png", volver.getIcon().getIconWidth(), volver.getIcon().getIconHeight()));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				volver.setIcon(imagenReescalada("/imgs/FlechaRoja.png", volver.getIcon().getIconWidth(), volver.getIcon().getIconHeight()));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(volver.getMousePosition() != null) {
					anteriorVentana();
				}
			}
		});

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(!estaCerrada) {
					setVisible(false);
					ventanaAnterior.setVisible(false);
					String[] s = {"Si", "No"}; //Opciones del JOptionPane
					if (JOptionPane.showOptionDialog(VentanaSesion.this, "¿Realmente desea salir de la aplicación?", "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, imagenReescalada("/imgs/Icono.png", 30, 30), s, 0) == 0) {
						
						//Quiero cerrar el programa entero, no cerrar solo la ventana -> dispose() no me sirve
						System.exit(0);
					}else {
						ventanaAnterior.setVisible(true);
						setVisible(true);
					}
				}
			}
		});
		GestionUsuarios.cargarLibreria();
	}

	/**Inicia las animaciones de la ventana
	 * 
	 */
	protected void iniciarAnimaciones() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!estaCerrada) {
					try {
						//Si hay algo escrito en los dos campos, se puede aceptar, sino no
						if (condicionesAceptar()) {
							botonAceptar.setEnabled(true);
						}else {
							//Por si hay algo escrito y luego se borra en algún campo
							botonAceptar.setEnabled(false);
						}
						if (condicionesBorrarMensaje()) {
							labelMensaje.setText(null);
						}
					}catch(NullPointerException e) {
						//Por si hay algo escrito y luego se borra en algún campo
						botonAceptar.setEnabled(false);
					}
				}

			}
		}).start();
	}

	/**Vuelve a la ventana anterior
	 * 
	 */
	protected void anteriorVentana(){
		estaCerrada = true;
		ventanaAnterior.cambiarFondo(false);
		dispose();
	}

	/**Pasa a la siguiente ventana, recogiendo los datos introducidos
	 * 
	 */
	protected abstract void siguienteVentana();

	/**Incia el menu de inicio de sesion/registro
	 * 
	 */
	public void iniciar() {
		setLocationRelativeTo(null);
		setVisible(true);
		iniciarAnimaciones();
	}

	/** Devuelve una imagen reescalada a unas dimensiones especificas
	 * @param ruta Ruta de la imagen
	 * @param ancho	Anchura que se le quiere dar a la imagen
	 * @param alto Altura que se le quiere dar a la imagen
	 * @return Objeto ImageIcon reescalado de una manera "smooth"
	 */
	public static ImageIcon imagenReescalada(String ruta, int ancho, int alto) {  
		return new ImageIcon(new ImageIcon(VentanaSesion.class.getResource(ruta)).getImage().getScaledInstance(ancho, alto,  java.awt.Image.SCALE_SMOOTH));
	}


	/** Método que especifíca las condiciones que deben cumplirse para que se habilite el botón de aceptar
	 * @return true si las condiciones se cumplen, false si no
	 * @throws NullPointerException En caso que en algún campo no se haya escrito nada
	 */
	protected boolean condicionesAceptar() throws NullPointerException{
		return inputUsuario.getText().length() > 0 && inputContraseña.getPassword().length > 0;
	}

	/** Método que especifíca las condiciones que deben cumplirse para borrar el mensaje de información de la pantalla
	 * @return true si las condiciones se cumplen, false si no
	 * @throws NullPointerException En caso que en algún campo no se haya escrito nada
	 */
	protected boolean condicionesBorrarMensaje() throws NullPointerException {
		return (inputUsuario.getText().length() > 0 || inputContraseña.getPassword().length > 0) && borrar || botonAceptar.isEnabled();
	}
	
	/**Resetea los textos de todos los JTextFields de la ventana
	 * @param resetAll Variable que especifica si resetear todos los textos (true), o sólo la contraseña (false)
	 */
	protected void resetTextos(boolean resetAll) {
		if(resetAll) {
			inputUsuario.setText(null);
			borrar = true;
		}else {
			borrar = false;
		}
		inputContraseña.setText(null);
	}

	/** Pinta todos los paneles de la ventana de cierto color
	 * @param color	Color del que se pintan los paneles
	 */
	protected void colorPaneles(Color color) {
		panelSuperior.setBackground(color);
		panelCentral.setBackground(color);
		panelInferior.setBackground(color);
		panelDatos.setBackground(color);
		imagenPrincipal.setBackground(color);
		panelUsuario.setBackground(color);
		panelContraseña.setBackground(color);
		panelInputUsuario.setBackground(color);
		panelInputContraseña.setBackground(color);
		panelAceptar.setBackground(color);
		panelMensaje.setBackground(color);
		panelGuardarDispositivo.setBackground(color);
		guardarDispositivo.setBackground(color);
	}

	/** Establece un color a los componentes de la ventana
	 * @param color	Color a establecer
	 */
	protected void colorComponentes() {
		labelMensaje.setForeground(Color.RED);
		labelUsuario.setForeground(Color.WHITE);
		labelContraseña.setForeground(Color.WHITE);
		inputUsuario.setForeground(Color.WHITE);
		inputUsuario.setBackground(Color.BLACK);
		inputContraseña.setForeground(Color.WHITE);
		inputContraseña.setBackground(Color.BLACK);
		inputContraseña.setForeground(Color.WHITE);
		botonAceptar.setBackground(Color.WHITE);
		guardarDispositivo.setForeground(Color.WHITE);
	}

	/**Cierra las ventanas relacionadas con el inicio de sesión y da comienzo al juego
	 * 
	 */
	public void fin() {
		new VentanaNiveles().setVisible(true);
		estaCerrada = true;
		VentanaJugar.setEstaCerrada(true);
		ventanaAnterior.dispose();
		dispose();
	}

	/** Devuelve el color del fondo de la ventana
	 * @return el color a devolver
	 */
	public static Color getFondooscuro() {
		return fondo;
	}
	
	/** Devuelve el usuario con el que se inicia el juego
	 * @return el usuario a devolver
	 */
	public static Usuario getUsuario() {
		return usuario;
	}

	/** Modifica el usuario con el que se inicia sesion
	 * @param usuario nuevo valor del usuario, el cual no puede ser nulo
	 */
	public static void setUsuario(Usuario usuario) {
		
		//No se le puede asignar un valor nulo
		if(usuario != null) {
			VentanaSesion.usuario = usuario;
		}
	}	
	
	/**Cierra una aplicación definitivamente
	 */
	public static void cerrar(JFrame ventana) {
		ventana.setVisible(false);
		String[] s = {"Si", "No"}; //Opciones del JOptionPane
		if (JOptionPane.showOptionDialog(ventana, "¿Realmente desea salir de la aplicación?", "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, imagenReescalada("/imgs/Icono.png", 30, 30), s, 0) == 0) {
			//Quiero cerrar el programa entero, no cerrar solo la ventana -> dispose() no me sirve
			System.exit(0);
		}else {
			ventana.setVisible(true);
		}
	}
}