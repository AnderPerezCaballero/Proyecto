package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

import gestionUsuarios.GestionUsuarios;
import gestionUsuarios.Usuario;

public abstract class VentanaSesion extends JFrame{

	//Atributos estáticos de la ventana
	private static final long serialVersionUID = 1L;
	private static final int anchuraVentana = 350;
	private static final int alturaVentana = 575;
	protected static final Color FONDOOSCURO = new Color(35, 39, 42);

	//Atributos no estáticos inmutables
	protected final int COLUMNAS = 30;

	//Atributos respecto al inicio de sesion
	private static GestionUsuarios usuarios;

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

	//JLabels
	private JLabel labelUsuario;
	private JLabel labelContraseña;
	private JLabel labelMensaje;	
	private JLabel volver;
	private JLabel imagenPrincipal;

	//Botones
	protected JButton botonAceptar;

	//Campos de texto
	protected JTextField inputUsuario;
	protected JPasswordField inputContraseña;

	//Eventos que se usarán en herencia
	protected KeyListener cierraConEsc;

	//Variables del usuario
	private static GestionUsuarios gestionUsuarios;
	private static Usuario usuario;

	//Variable que indica el estado de la ventana
	private boolean estaCerrada;


	/**Constructor de la ventana de menú
	 * @param tituloVentana Título de la ventana
	 * @param incioSesion true si es inicio de sesion, false si es registro
	 */
	public VentanaSesion(int numeroDeDatos) {

		// Inicialización de la ventana
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(anchuraVentana, alturaVentana);
		this.estaCerrada = false;
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("src/imgs/Icono.png"));

		gestionUsuarios = new GestionUsuarios();
		//gestionUsuarios.cargarDatos();

		panelAceptar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelCentral = new JPanel(new GridLayout(2, 1));
		panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));

		panelDatos = new JPanel(new GridLayout(numeroDeDatos, 1));
		imagenPrincipal = new JLabel(ImagenReescalada("src/imgs/Fondo.png", 220, 220));

		volver = new JLabel(ImagenReescalada("src/imgs/FlechaBlanca.png", 40, 40));

		panelUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelInputUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelContraseña = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelInputContraseña = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelMensaje = new JPanel();
		panelMensaje.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

		labelUsuario = new JLabel("Usuario:");
		labelContraseña = new JLabel("Contraseña:");
		inputUsuario = new JTextField(COLUMNAS);
		inputContraseña = new JPasswordField(COLUMNAS);
		labelMensaje = new JLabel("Mensaje");

		botonAceptar = new MiBoton(Color.WHITE, FONDOOSCURO.brighter(), 35, 35);

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
					cerrar();
				}
			}
		};

		//A todos los elementos que pueden tener foco les hago que cuando se presione el escape se cierren 
		inputUsuario.addKeyListener(cierraConEsc);
		inputContraseña.addKeyListener(cierraConEsc);
		botonAceptar.addKeyListener(cierraConEsc);

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
				volver.setIcon(ImagenReescalada("src/imgs/FlechaBlanca.png", volver.getIcon().getIconWidth(), volver.getIcon().getIconHeight()));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				volver.setIcon(ImagenReescalada("src/imgs/FlechaRoja.png", volver.getIcon().getIconWidth(), volver.getIcon().getIconHeight()));
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
				cerrar();
			}
		});
	}

	/**Recoge el nombre de usuario y la contraseña del usuario
	 * 
	 */
	protected void iniciarAnimaciones() {
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
				labelMensaje.setText(null);
			}
		}
	}

	/**Cierra la aplicación definitivamente
	 */
	private void cerrar() {
		this.setVisible(false);
		String[] s = {"Si", "No"}; //Opciones del JOptionPane
		if (JOptionPane.showOptionDialog(this, "¿Realmente desea salir de la aplicación?", "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, ImagenReescalada("src/imgs/Icono.png", 30, 30), s, 0) == 0) {
			//Quiero cerrar el programa entero, no cerrar solo la ventana -> dispose() no me sirve
			System.exit(0);
		}else {
			this.setVisible(true);
		}
	}

	/**Vuelve a la ventana anterior
	 * 
	 */
	protected void anteriorVentana(){
		this.setVisible(false);
	}

	/**Pasa a la siguiente ventana, recogiendo los datos introducidos
	 * 
	 */
	protected abstract void siguienteVentana();

	/**Incia el menu de inicio de sesion
	 * 
	 */
	protected void iniciar() {
		this.centrar();
		this.setVisible(true);
		this.iniciarAnimaciones();
	}

	/**Devuelve el usuario correspondiente al inicio de sesión/registro
	 * @return objeto de tipo usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	public static GestionUsuarios getGestionUsuarios() {
		return gestionUsuarios;
	}

	public static void setGestionUsuarios(GestionUsuarios gestionUsuarios) {
		VentanaSesion.gestionUsuarios = gestionUsuarios;
	}

	/** Método que centra la ventana en pantalla
	 * 
	 */
	private void centrar() {
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		int x = pantalla.width / 2;
		int y = pantalla.height / 2;		
		this.setLocation(x - getWidth() / 2, y - getHeight() / 2);
	}

	/** Devuelve una imagen reescalada a unas dimensiones especificas
	 * @param ruta Ruta de la imagen
	 * @param ancho	Anchura que se le quiere dar a la imagen
	 * @param alto Altura que se le quiere dar a la imagen
	 * @return Objeto ImageIcon reescalado de una manera "smooth"
	 */
	private ImageIcon ImagenReescalada(String ruta, int ancho, int alto) {   
		return new ImageIcon(new ImageIcon(ruta).getImage().getScaledInstance(ancho, alto,  java.awt.Image.SCALE_SMOOTH));
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
	}
}