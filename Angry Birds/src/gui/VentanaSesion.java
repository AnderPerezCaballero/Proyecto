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
	
	//Atributos no estáticos inmutables
	protected final Color FONDO = new Color(35, 39, 42);
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
	private JPanel panelSuperiorIzquierdo;
	private JPanel panelSuperiorDerecho;
	private JPanel panelAceptar;

	//JLabels
	private JLabel labelUsuario;
	private JLabel labelContraseña;
	protected JLabel labelMensaje;	
	private JLabel volver;
	private JLabel imagenPrincipal;
	private JLabel imagenModoOscuro;

	//Botones
	protected JButton botonAceptar;

	//Campos de texto
	private JTextField inputUsuario;
	private JPasswordField inputContraseña;

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
		panelSuperior = new JPanel(new GridLayout(1, 2));
		panelSuperiorIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
		panelSuperiorDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
		panelCentral = new JPanel(new GridLayout(2, 1));
		panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));

		panelDatos = new JPanel(new GridLayout(numeroDeDatos, 1));
		imagenPrincipal = new JLabel(ImagenReescalada("src/imgs/Fondo.png", 220, 220));

		volver = new JLabel(ImagenReescalada("src/imgs/FlechaBlanca.png", 40, 40));
		imagenModoOscuro = new JLabel(ImagenReescalada("src/imgs/ModoClaro.png", 40, 40));

		panelUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelInputUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelContraseña = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelInputContraseña = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelMensaje = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

		labelUsuario = new JLabel("Usuario:");
		labelContraseña = new JLabel("Contraseña:");
		inputUsuario = new JTextField(COLUMNAS);
		inputContraseña = new JPasswordField(COLUMNAS);
		labelMensaje = new JLabel("Mensaje");

		botonAceptar = new MiBoton(Color.WHITE, FONDO.brighter(), 35, 35);

		labelUsuario.setForeground(Color.WHITE);
		labelContraseña.setForeground(Color.WHITE);
		inputUsuario.setBackground(Color.BLACK);
		inputContraseña.setBackground(Color.BLACK);
		inputUsuario.setForeground(Color.WHITE);
		inputContraseña.setForeground(Color.WHITE);


		// Configurar componentes
		botonAceptar.setEnabled(false);
		botonAceptar.setToolTipText("Pulsa aquí para confirmar tus datos");
		botonAceptar.setBackground(Color.WHITE);
		botonAceptar.setPreferredSize(new Dimension(anchuraVentana - 40, 40));
		labelMensaje.setForeground(Color.WHITE);


		//Añadir componentes a contenedores				

		panelUsuario.add(labelUsuario);
		panelContraseña.add(labelContraseña);
		panelInputUsuario.add(inputUsuario);
		panelInputContraseña.add(inputContraseña);
		panelAceptar.add(botonAceptar);
		

		getContentPane().add(panelSuperior, "North");
		getContentPane().add(panelCentral, "Center");
		getContentPane().add(panelInferior, "South");

		panelDatos.add(panelUsuario);
		panelDatos.add(panelInputUsuario);
		panelDatos.add(panelContraseña);
		panelDatos.add(panelInputContraseña);
		panelDatos.add(panelMensaje);

		panelSuperiorIzquierdo.add(volver);
		panelSuperiorDerecho.add(imagenModoOscuro);

		panelCentral.add(imagenPrincipal);
		panelCentral.add(panelDatos);

		panelSuperior.add(panelSuperiorIzquierdo);
		panelSuperior.add(panelSuperiorDerecho);

		panelInferior.add(panelAceptar);

		//EVENTOS
		KeyListener cierraConEsc = new KeyAdapter() {
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

		inputContraseña.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (inputContraseña.getPassword() != null) {
					botonAceptar.requestFocus();
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
	public void iniciarAnimaciones() {
		while (!estaCerrada) {
			try {
				//Si hay algo escrito en los dos campos, se puede aceptar, sino no
				if (inputUsuario.getText().length() > 0 && inputContraseña.getPassword().length > 0) {
					botonAceptar.setEnabled(true);
				}else {
					//Por si hay algo escrito y luego se borra en algún campo
					botonAceptar.setEnabled(false);
				}
				if (inputUsuario.getText().length() > 0 || inputContraseña.getPassword().length > 0) {
					labelMensaje.setText(null);
				}
			}catch(NullPointerException e) {
				//Por si hay algo escrito y luego se borra en algún campo
				botonAceptar.setEnabled(false);
			}
		}
	}

	/**Cierra la aplicación definitivamente
	 */
	public void cerrar() {
		this.setVisible(false);
		if (JOptionPane.showConfirmDialog(null, "¿Realmente desea salir de la aplicación?", "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
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
		ImageIcon imageIcon = new ImageIcon(ruta);
		Image image = imageIcon.getImage(); 
		Image newimg = image.getScaledInstance(ancho, alto,  java.awt.Image.SCALE_SMOOTH);  
		return new ImageIcon(newimg);
	}
	
	protected void colorPaneles(Color color) {
		panelSuperiorIzquierdo.setBackground(color);
		panelSuperiorDerecho.setBackground(color);
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
}