package gui.juego;

import java.awt.Color;
import java.awt.Component;
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
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import gui.sesion.VentanaSesion;

@SuppressWarnings("serial")
public class VentanaOpcionesJuego extends JFrame{

	private static Font fuente;
	private static Properties propiedades;

	private JPanel panelSuperior;
	private JPanel panelCentral;
	private JPanel panelInferior;

	private JLabel titulo;
	private JLabel tiempoJugado;
	private JLabel pajarosGastados;
	private JLabel mensaje;

	private JButton reiniciarNivel;
	private JButton volverMenu;
	private JButton sacarEstadisticas;


	/** Saca informacion al usuario sobre como ha ido su intento de superar el nivel y sobre lo que puede hacer después de haber jugado
	 * @param pajaros número de pájaros necesitados para superar el nivel
	 * @param milis número de milisegundos del usuario jugando al nivel
	 * @param nivelCompletado true si el nivel se ha completado, false si no
	 * @param nivel Identificativo del nivel al que se acaba de jugar
	 */
	public VentanaOpcionesJuego(int pajaros, long milis, boolean nivelCompletado, int nivel) {
		setSize(700, 500);
		setLocationRelativeTo(null);
		setBackground(VentanaSesion.getFondooscuro());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Menú");

		propiedades = new Properties();

		boolean undecorated = false;
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
			mensaje = new JLabel("Bien hecho! Puedes volver a jugar al nivel, volver al menu de seleccion de niveles o ver tus estadisticas");
		} else {
			titulo = new JLabel("Intento Fallido");
			mensaje = new JLabel("No pasa nada, puedes intentarlo de nuevo, o sino puedes volver al menu de selección de niveles o ver tus estadisticas");
		}

		tiempoJugado = new JLabel(String.format("TIEMPO: %.2f segundos", milis / 1000.0));

		pajarosGastados = new JLabel(String.format("PAJAROS GASTADOS: %d", pajaros));
		
		configurarFuente();

		reiniciarNivel = new JButton("Reiniciar");
		volverMenu = new JButton("Menu");
		sacarEstadisticas = new JButton("Estadisticas");

		panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
		panelSuperior.add(titulo);
		add(panelSuperior, "North");

		panelCentral = new JPanel(new GridLayout(3, 1));
		panelCentral.add(tiempoJugado);
		panelCentral.add(pajarosGastados);
		panelCentral.add(mensaje);

		add(panelCentral, "Center");

		panelInferior = new JPanel(new FlowLayout());
		panelInferior.add(reiniciarNivel);
		panelInferior.add(volverMenu);
		panelInferior.add(sacarEstadisticas);

		add(panelInferior, "South");

		establecerAspecto();

		setVisible(true);
	
		configurarFuenteMensaje();

		//Por si el usuario decide que se pueda redimensionar la ventana
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				ajustarTamanoFuente(mensaje);	
			}
		});
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
		cargarFuente("/fonts/Northash.ttf");
		titulo.setFont(fuente.deriveFont(100.0f));
		tiempoJugado.setFont(fuente.deriveFont(30.0f));
		pajarosGastados.setFont(fuente.deriveFont(30.0f));
	}
	
	/**Configura la fuente del mensaje
	 * Este método debe ser llamado después de hacer visible la ventana, pues hace uso de sus recursos gráficos
	 */
	private void configurarFuenteMensaje() {
		mensaje.setFont(fuente.deriveFont(30.0f));
		ajustarTamanoFuente(mensaje);
	}
	

	/** Método para cargar la fuente importada desde un fichero
	 * @param rutaFichero ruta del fichero desde el que se carga la fuente
	 */
	private void cargarFuente(String rutaFichero){
		try {
			InputStream is = getClass().getResourceAsStream(rutaFichero);
			fuente = Font.createFont(Font.TRUETYPE_FONT, is);
			is.close();
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
	}

	/** Método para ajustar el tamaño de la fuente del JLabel para que el texto quepa en el tamaño del JLabel
	 * @param label label cuya fuente tiene que reajustarse
	 */
	private void ajustarTamanoFuente(JLabel label) {
		Font fuenteLabel = fuente.deriveFont(30f);
		FontMetrics fm = label.getGraphics().getFontMetrics(fuenteLabel);

		while (fm.stringWidth(label.getText()) + 10 > getWidth()) {
			fuenteLabel = fuenteLabel.deriveFont(fuenteLabel.getSize() - 1f);
			fm = label.getGraphics().getFontMetrics(fuenteLabel);
		}

		label.setFont(fuenteLabel);
	}

	public static void main(String[] args) {
		new VentanaOpcionesJuego(1, 1992883, false, 4);
	}
}
