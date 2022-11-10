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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import gui.sesion.VentanaSesion;

@SuppressWarnings("serial")
public abstract class VentanaInicio extends JFrame {

	// Componentes
	protected JPanel panelCentral;
	protected JPanel panelAbajo;
	protected JLabel imagen;
	private JMenu sonido;
	private JMenuBar barraMenu;
	private JMenuItem itemMenu;
	protected JCheckBoxMenuItem itemMenu1;
	private Thread hilo;
	private Clip clip;

	// Listeners

	KeyListener escCerrar;
	ActionListener actionMute;
	WindowListener actionVentana;

	// Siempre igual
	private static final int alturaVentana = 685;
	private static final int anchuraVentana = 700;
	private static final String nombreVentana = "Angry Birds";

	public VentanaInicio() {

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(anchuraVentana, alturaVentana);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null); // deja la ventana en el centro
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaInicioPlay1.class.getResource("/imgs/Icono.png")));
		this.setTitle(nombreVentana);

		actionVentana = new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				ReproducirMusica("res/audio/Cancion.wav");
			}

			@Override
			public void windowClosed(WindowEvent e) {
				clip.close();

			}

		};

		this.addWindowListener(actionVentana);

		barraMenu = new JMenuBar();
		setJMenuBar(barraMenu);
		barraMenu.setBackground(VentanaSesion.getFondooscuro());

		sonido = new JMenu("SONIDO");
		sonido.setForeground(Color.white);
		sonido.setMnemonic(KeyEvent.VK_S);
		barraMenu.add(sonido);

		itemMenu1 = new JCheckBoxMenuItem(VentanaSesion.imagenReescalada("/imgs/mute.png", 10, 10));
		itemMenu1.setBackground(Color.WHITE);
		sonido.add(itemMenu1);

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

		itemMenu1.addActionListener(actionMute);

		panelCentral = new JPanel();
		panelCentral.setLayout(new BorderLayout());
		imagen = new JLabel(VentanaSesion.imagenReescalada("/imgs/AngryBirdsInicio.jpg", 700, 600));
		this.getContentPane().add(panelCentral, BorderLayout.CENTER);
		panelCentral.add(imagen, BorderLayout.CENTER);

		panelAbajo = new JPanel();
		panelAbajo.setBackground(VentanaSesion.getFondooscuro());
		this.add(panelAbajo, BorderLayout.SOUTH);

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
}
