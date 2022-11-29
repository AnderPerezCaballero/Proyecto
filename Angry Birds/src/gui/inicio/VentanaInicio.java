package gui.inicio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.plaf.LayerUI;

import gui.componentes.MiPanel;
import gui.sesion.VentanaSesion;

@SuppressWarnings("serial")
public abstract class VentanaInicio extends JFrame {

	// Componentes
	protected JPanel panelAbajo;
	protected JLabel imagen;
	protected MiPanel panelPrincipal;
	private JMenu sonido;
	private JMenuBar barraMenu;
	protected JCheckBoxMenuItem itemMenu1;
	private Clip clip;
	
	//Estado del inicio
	private static boolean cerrado;

	// Listeners
	KeyListener escCerrar;
	ActionListener actionMute;
	WindowListener actionVentana;

	// Siempre igual
	private static final String nombreVentana = "Angry Birds";

	public VentanaInicio() {

		this.setExtendedState(MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaSesion.class.getResource("/imgs/Icono.png")));
		this.setTitle(nombreVentana);
		cerrado = false;
		
		panelPrincipal = new MiPanel("/imgs/AngryBirdsInicio.jpg", new BorderLayout());
		
		barraMenu = new JMenuBar();
		barraMenu.setOpaque(false);
		panelPrincipal.add(barraMenu, "North");
		
		sonido = new JMenu("SONIDO");
		sonido.setMnemonic(KeyEvent.VK_S);
		sonido.setOpaque(false);
		barraMenu.add(sonido);
	

		itemMenu1 = new JCheckBoxMenuItem(VentanaSesion.imagenReescalada("/imgs/mute.png", 10, 10));
		itemMenu1.setMnemonic(KeyEvent.VK_S);
		sonido.add(itemMenu1);       
		
		//EVENTOS
		
		// Para que cuando el checkBox de mute este pulsado la cancion pare
		// y si se vuelve a pulsar para desmutear, vuelva a ponerse la cancion desde el
		// principio
		actionMute = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clip.close();

				if (itemMenu1.getState() == false) {
					ReproducirMusica("res/audio/Cancion.wav");
				}
			}
		};

		actionVentana = new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				ReproducirMusica("res/audio/Cancion.wav");
			}

			@Override
			public void windowClosed(WindowEvent e) {
				clip.close();
				if(!cerrado) {
					VentanaSesion.cerrar(VentanaInicio.this);
				}
			}
		};

		this.addWindowListener(actionVentana);
		itemMenu1.addActionListener(actionMute);
		
		setContentPane(panelPrincipal);
		
		escCerrar = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // Hacemos que al soltar el escape la ventana se cierre
					clip.close();
					dispose();
				}
			}
		};

		itemMenu1.addKeyListener(escCerrar);

	}

	// Para reproducir sonido
	public void ReproducirMusica(String ruta) {
		try {
			AudioInputStream is = AudioSystem.getAudioInputStream(new File(ruta));
			clip = AudioSystem.getClip();
			clip.open(is);
			clip.loop(0);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	/**Define un nuevo valor para determinar si proceso relacionado con la selección de iniciar sesion/registrar usuario ha acabado
	 * @param valor nuevo valor booleano
	 */
	public static void setEstaCerrada(boolean valor) {
		cerrado = valor;
	}

	/** Devuelve el panelPrincipal de la ventana
	 * @return el panelPrincipal a devolver
	 */
	public MiPanel getPanelPrincipal() {
		return panelPrincipal;
	}
	
	
}
