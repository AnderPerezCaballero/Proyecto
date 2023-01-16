package gui.juego;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import gestionUsuarios.GestionUsuarios;
import gestionUsuarios.Puntuacion;
import gestionUsuarios.Usuario;
import gui.sesion.VentanaSesion;
import juego.Juego;

@SuppressWarnings("serial")
public class VentanaOpcionesJuego extends JFrame{

	public static final int ANCHURA = 700;
	public static final int ALTURA = 500;
	
	private static Font fuentePrincipal;
	private static Font fuenteSecundaria;

	private static Properties propiedades;

	private JFrame anteriorVentana;

	private JPanel panelSuperior;
	private JPanel panelCentral;
	private JPanel panelInferior;

	private JLabel titulo;
	private JLabel tiempoJugado;
	private JLabel pajarosGastados;
	private JLabel estrellasConseguidas;
	private JLabel mensaje;

	private JButton reiniciarNivel;
	private JButton volverMenu;
	private JButton sacarEstadisticas;
	private JButton salir;

	private Usuario usuario;
	private int pajaros;
	private int estrellas;


	/** Saca informacion al usuario sobre como ha ido su intento de superar el nivel y sobre lo que puede hacer después de haber jugado
	 * También actualiza el tiempo de juego del usuario que ha jugado al nivel
	 * @param pajarosInicio número de pájaros disponibles al inicio del nivel
	 * @param pajarosDisponibles número de pájaros disponibles con los que se acaba el nivel
	 * @param milis número de milisegundos del usuario jugando al nivel
	 * @param nivelCompletado true si el nivel se ha completado, false si no
	 * @param nivel Identificativo del nivel al que se acaba de jugar
	 */
	public VentanaOpcionesJuego(int pajarosInicio, int pajarosDisponibles, long milis, boolean nivelCompletado, int nivel, JFrame anteriorVentana) {
		this.anteriorVentana = anteriorVentana;
		pajaros = pajarosInicio - pajarosDisponibles;
		usuario = VentanaSesion.getUsuario();
		setSize(ANCHURA, ALTURA);
		setLocationRelativeTo(null);
		setBackground(VentanaSesion.getFondooscuro());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Menú");

		cargarFuentePrincipal("/fonts/Northash.ttf");
		cargarFuenteSecundaria("/fonts/Semur.ttf");

		//Cálculo del número de estrellas obtenidas en el nivel
		if(nivelCompletado) {
			double proporcion = pajarosDisponibles / (double) pajarosInicio;
			if(proporcion > 0.66) {
				estrellas = 3;
			}else if(proporcion > 0.33) {
				estrellas = 2;
			}else {
				estrellas = 1;
			}	
		}else {
			estrellas = 0;
		}

		//Actualizar el usuario
		usuario.addTiempo(milis);
		Puntuacion puntuacion = new Puntuacion(estrellas, nivel);
		usuario.addPuntuacion(puntuacion);
		
		propiedades = new Properties();

		boolean undecorated = false;	//Por defecto undecorated
		try (InputStream input = new FileInputStream("lib/propiedades.properties")) {
			propiedades.load(input);
			undecorated = Boolean.parseBoolean(propiedades.getProperty("Undecorated"));

		} catch (IOException | IllegalArgumentException e) {
			JOptionPane.showMessageDialog(VentanaOpcionesJuego.this, "Propiedades de la ventana no cargadas", "Aviso", JOptionPane.WARNING_MESSAGE);
		}	
		setUndecorated(undecorated);

		getRootPane().setBorder(new LineBorder(VentanaSesion.getFondooscuro().brighter().brighter(), 5));

		if (nivelCompletado) {
			titulo = new JLabel("Nivel Completado");
			mensaje = new JLabel(String.format("Bien hecho %s! Puedes volver a jugar al nivel, volver al menu de seleccion de niveles o ver tus estadisticas", usuario.getNombre()));
		} else {
			titulo = new JLabel("Intento Fallido");
			mensaje = new JLabel(String.format("No pasa nada %s, puedes intentarlo de nuevo o sino puedes volver al menu de seleccion de niveles o ver tus estadisticas", usuario.getNombre()));
		}

		tiempoJugado = new JLabel(String.format("TIEMPO: %.2f segundos", milis / 1000.0));
		pajarosGastados = new JLabel(String.format("PAJAROS GASTADOS: %d", pajaros));
		estrellasConseguidas = new JLabel(String.format("ESTRELAS CONSEGUIDAS: %d", estrellas));

		configurarFuente();

		reiniciarNivel = new JButton("Reiniciar");
		volverMenu = new JButton("Menu");
		sacarEstadisticas = new JButton("Estadisticas");
		salir = new JButton("Salir");

		panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
		panelSuperior.add(titulo);
		add(panelSuperior, "North");

		panelCentral = new JPanel(new GridLayout(4, 1));
		panelCentral.add(tiempoJugado);
		panelCentral.add(pajarosGastados);
		panelCentral.add(estrellasConseguidas);
		panelCentral.add(mensaje);

		add(panelCentral, "Center");

		panelInferior = new JPanel(new FlowLayout());
		panelInferior.add(reiniciarNivel);
		panelInferior.add(volverMenu);
		panelInferior.add(sacarEstadisticas);
		panelInferior.add(salir);

		add(panelInferior, "South");

		//Por si el usuario decide que se pueda redimensionar la ventana (undecorated = false)
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				ajustarTamanoFuente(mensaje);	
			}
		});

		reiniciarNivel.addActionListener(e -> {
			Juego.init(nivel);		
			cerrar();
		});

		volverMenu.addActionListener(e ->{
			new VentanaNiveles().setVisible(true);
			cerrar();
		});

		sacarEstadisticas.addActionListener(e -> {
			String[] s = {"Ver Ranking", "Ver Puntuaciones"}; //Opciones del JOptionPane
			if (JOptionPane.showOptionDialog(VentanaOpcionesJuego.this, "¿Qué estadisticas quieres ver?", "Estadísticas", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, VentanaSesion.imagenReescalada("/imgs/Icono.png", 30, 30), s, 0) == 0) {
				new Ranking(this);
			}else {
				new VentanaEstadisticas(usuario, this);
			}
		});

		salir.addActionListener(e ->{
			System.exit(0);
		});

		//Actualizar el usuario en la base de datos
		try {
			GestionUsuarios.actualizarUsuario(usuario);
			GestionUsuarios.addPuntuacion(usuario, puntuacion);
		} catch (SQLException e) {
			JLabel mensajeError = new JLabel("TUS DATOS NO HAN PODIDO ACTUALIZARSE");
			mensajeError.setFont(fuenteSecundaria.deriveFont(30.0f));
			JPanel nuevoPanelCentral = new JPanel(new GridLayout(panelCentral.getComponentCount() + 1, 1));
			nuevoPanelCentral.add(mensajeError);
			
			for(Component componente : panelCentral.getComponents()) {
				nuevoPanelCentral.add(componente);
			}
			
			remove(panelCentral);
			panelCentral = nuevoPanelCentral;
			add(panelCentral);
			
			GestionUsuarios.log(Level.SEVERE, String.format("Los datos del usuario %s despues de jugar el nivel %d no han podido actualizarse", usuario.toString(), nivel), e);
		}

		establecerAspecto();

		setVisible(true);

		configurarFuenteMensaje();
	}

	/** Método para establecer el aspecto de los componentens de la ventana
	 *  Entre los elementos a establecer se encuentran el color de fondo, el color del texto o el alineamiento horizontal
	 */
	private void establecerAspecto() {
		for(Component componente : getContentPane().getComponents()) {
			componente.setBackground(VentanaSesion.getFondooscuro());
			for(Component jComponent : ((JPanel) componente).getComponents()) {
				if(jComponent instanceof JButton) {
					jComponent.setBackground(Color.WHITE);
					jComponent.setPreferredSize(new Dimension(120, 30));
				}else{
					JLabel jLabel = (JLabel) jComponent;
					jLabel.setHorizontalAlignment(SwingConstants.CENTER);
					jLabel.setForeground(Color.WHITE);
				}
			}
		}
	}

	/**Método para configurar las fuentes de los elementos de la ventana, menos la del mensaje
	 * 
	 */
	private void configurarFuente() {
		titulo.setFont(fuentePrincipal.deriveFont(100.0f));
		tiempoJugado.setFont(fuentePrincipal.deriveFont(30.0f));
		pajarosGastados.setFont(fuentePrincipal.deriveFont(30.0f));
		estrellasConseguidas.setFont(fuentePrincipal.deriveFont(30.0f));
	}

	/**Configura la fuente del mensaje
	 * Este método debe ser llamado después de hacer visible la ventana, pues hace uso de sus recursos gráficos
	 */
	private void configurarFuenteMensaje() {
		mensaje.setFont(fuentePrincipal.deriveFont(30.0f));
		ajustarTamanoFuente(mensaje);
	}


	/** Carga la fuente principal importada desde un fichero
	 * @param rutaFichero ruta del fichero desde el que se carga la fuente
	 */
	private void cargarFuentePrincipal(String rutaFichero){
		try {
			InputStream is = getClass().getResourceAsStream(rutaFichero);
			fuentePrincipal = Font.createFont(Font.TRUETYPE_FONT, is);
			is.close();
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
	}

	/** Carga la fuente secundaria importada desde un fichero
	 * @param rutaFichero ruta del fichero desde el que se carga la fuente
	 */
	private void cargarFuenteSecundaria(String rutaFichero){
		try {
			InputStream is = getClass().getResourceAsStream(rutaFichero);
			fuenteSecundaria = Font.createFont(Font.TRUETYPE_FONT, is);
			is.close();
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
	}

	/** Método para ajustar el tamaño de la fuente del JLabel para que el texto quepa en el tamaño del JLabel. Por defecto, se usa la fuente principal,
	 *  pero en caso de tener que disminuir su tamaño, se usa la secundaria
	 * @param label label cuya fuente debe ser comprobada para reajustarse
	 */
	private void ajustarTamanoFuente(JLabel label) {
		Font fuenteLabel = fuentePrincipal.deriveFont(30f);
		FontMetrics fm = label.getGraphics().getFontMetrics(fuenteLabel);

		if(fm.stringWidth(label.getText()) + 10 > getWidth()) {
			fuenteLabel = fuenteSecundaria.deriveFont(30f);
			fm = label.getGraphics().getFontMetrics(fuenteLabel);
			while (fm.stringWidth(label.getText()) + 10 > getWidth()) {
				fuenteLabel = fuenteLabel.deriveFont(fuenteLabel.getSize() - 1f);
				fm = label.getGraphics().getFontMetrics(fuenteLabel);
			}
		}

		label.setFont(fuenteLabel);
	}

	/** Cierra la ventana actual y la anterior
	 * 
	 */
	private void cerrar() {
		dispose();
		anteriorVentana.dispose();
	}
}
