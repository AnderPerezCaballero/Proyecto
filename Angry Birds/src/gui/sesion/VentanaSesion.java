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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import gestionUsuarios.Usuario;
import gui.componentes.MensajeCarga;
import gui.componentes.MiBoton;
import gui.inicio.VentanaInicio;
import gui.juego.VentanaNiveles;

@SuppressWarnings("serial")
public abstract class VentanaSesion extends JFrame{

	//Atributos estáticos de la ventana
	private static final int ANCHURAVENTANA = 350;
	private static final int ALTURAVENTANA = 575;
	private static final Color FONDOOSCURO = new Color(35, 39, 42);
	private static final int COLUMNAS = 30;

	//Referencia de la ventana anterior
	private static JFrame ventanaAnterior;

	//Contenedores
	private JPanel panelSuperior;
	private JPanel panelCentral;
	private JPanel panelDatos;
	private JPanel panelInferior;

	private JPanel panelUsuario;
	private JPanel panelInputUsuario;
	private JPanel panelContraseña;
	private JPanel panelInputContraseña;
	private JPanel panelMensaje;
	private JPanel panelAceptar;
	private JPanel panelGuardarDispositivo;

	//JLabels
	private JLabel labelUsuario;
	private JLabel labelContraseña;
	private JLabel labelMensaje;	
	private JLabel volver;
	private JLabel imagenPrincipal;
	private JCheckBox guardarDispositivo;

	//Botones
	private MiBoton botonAceptar;

	//Campos de texto
	private JTextField inputUsuario;
	private JPasswordField inputContraseña;

	//Eventos que se usarán en herencia
	private KeyListener cierraConEsc;

	//Variable que indica el estado de la ventana
	private boolean estaCerrada;

	//Hilo ejecutado al cargar los datos del usuario
	private MensajeCarga mensajeDeCarga;

	//Usuario que va a hacer uso de la aplicación
	private static Usuario usuario;
	
	/** Constructor de la ventana 
	 * @param numeroDeDatos Variable que indica el número de datos que va a contener el gridLayout
	 * @param anteriorVentana ventana a la que se debe volver en caso de que el usuario decida ir hacia atrás
	 */
	public VentanaSesion(int numeroDeDatos, JFrame anteriorVentana) {

		ventanaAnterior = anteriorVentana;

		// Inicialización de la ventana
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(ANCHURAVENTANA, ALTURAVENTANA);
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
		inputUsuario = new JTextField(COLUMNAS);
		inputContraseña = new JPasswordField(COLUMNAS);
		labelMensaje = new JLabel();

		botonAceptar = new MiBoton(Color.WHITE, FONDOOSCURO.brighter(), 35, 35);

		volver = new JLabel(imagenReescalada("/imgs/FlechaBlanca.png", 40, 40));

		guardarDispositivo = new JCheckBox("Guardar Dispositivo");

		// Configurar componentes
		botonAceptar.setEnabled(false);
		botonAceptar.setToolTipText("Pulsa aquí para confirmar tus datos");
		botonAceptar.setPreferredSize(new Dimension(ANCHURAVENTANA - 40, 40));
		//		botonAceptar.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));

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
					cerrar(VentanaSesion.this);
				}
			}
		});
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

	/**Vuelve a la ventana anterior
	 * 
	 */
	protected void anteriorVentana(){
		estaCerrada = true;
		ventanaAnterior.setEnabled(true);
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
		return inputUsuario.getText().length() > 0 || inputContraseña.getPassword().length > 0;
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
	protected void colorComponentes(Color color) {
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

	/**Resetea los textos de todos los JTextFields de la ventana
	 * 
	 */
	protected void resetTextos() {
		inputUsuario.setText(null);
		inputContraseña.setText(null);
	}

	/**Cierra las ventanas relacionadas con el inicio de sesión y da comienzo al juego
	 * 
	 */
	public void fin() {
		estaCerrada = true;
		VentanaInicio.setEstaCerrada(true);
		ventanaAnterior.dispose();
		dispose();
		new VentanaNiveles().iniciar();
	}

	/** Devuelve el color del fondo de la ventana
	 * @return el color a devolver
	 */
	public static Color getFondooscuro() {
		return FONDOOSCURO;
	}

	/** Devuelve el panelDatos
	 * @return el panelDatos a devolver
	 */
	public JPanel getPanelDatos() {
		return panelDatos;
	}

	/** Devuelve el panelMensaje
	 * @return el panelMensaje a devolver
	 */
	public JPanel getPanelMensaje() {
		return panelMensaje;
	}

	/** Devuelve el panelGuardarDispositivo
	 * @return el panelGuardarDispositivo a devolver
	 */
	public JPanel getPanelGuardarDispositivo() {
		return panelGuardarDispositivo;
	}

	/** Devuelve el labelMensaje
	 * @return el labelMensaje a devolver
	 */
	public JLabel getLabelMensaje() {
		return labelMensaje;
	}

	/** Devuelve el guardarDispositivo
	 * @return el guardarDispositivo a devolver
	 */
	public JCheckBox getGuardarDispositivo() {
		return guardarDispositivo;
	}

	/** Devuelve el número de columnas de los JTextfield
	 * @return el número de columnas a devolver
	 */
	public static int getColumnas() {
		return COLUMNAS;
	}

	/** Devuelve el botonAceptar
	 * @return el botonAceptar a devolver
	 */
	public JButton getBotonAceptar() {
		return botonAceptar;
	}

	/** Devuelve el inputUsuario
	 * @return el inputUsuario a devolver
	 */
	public JTextField getInputUsuario() {
		return inputUsuario;
	}

	/** Devuelve el inputContraseña
	 * @return el inputContraseña a devolver
	 */
	public JPasswordField getInputContraseña() {
		return inputContraseña;
	}

	/** Devuelve el cierraConEsc
	 * @return el cierraConEsc a devolver
	 */
	public KeyListener getCierraConEsc() {
		return cierraConEsc;
	}

	/** Devuelve el hilo que se encarga de establecer el mensaje de carga en los botones
	 * @return el mensajeDeCarga a devolver
	 */
	public MensajeCarga getMensajeDeCarga() {
		return mensajeDeCarga;
	}

	/** Devuelve el usuario con el que se inicia el juego
	 * @return el usuario a devolver
	 */
	public static Usuario getUsuario() {
		return usuario;
	}	

	/** Modifica el hilo mensajeDeCarga
	 * @param mensajeDeCarga nuevo valor para el hilo
	 */
	protected void setMensajeDeCarga(MensajeCarga mensajeDeCarga) {
		this.mensajeDeCarga = mensajeDeCarga;
	}

	/** Modifica el usuario con el que se inicia sesion
	 * @param usuario nuevo valor del usuario
	 */
	public static void setUsuario(Usuario usuario) {
		//No se le puede asignar un valor nulo
		if(usuario != null) {
			VentanaSesion.usuario = usuario;
		}
	}	
}