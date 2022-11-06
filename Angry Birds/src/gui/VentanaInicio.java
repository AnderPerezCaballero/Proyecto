package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

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

	private static final long serialVersionUID = 1L;
	private static final int alturaVentana = 670;
	private static final int anchuraVentana = 700;
	static final Color FONDOOSCURO = new Color(35, 39, 42);
	private static final String nombreVentana = "Angry Birds";

	public VentanaInicio() {

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(anchuraVentana, alturaVentana);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null); // deja la ventana en el centro
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("src/imgs/Icono.png"));
		this.setTitle(nombreVentana);
		
		
		barraMenu = new JMenuBar();
		setJMenuBar(barraMenu);
		barraMenu.setBackground(FONDOOSCURO);

		sonido = new JMenu("SONIDO");
		sonido.setForeground(Color.white);
		sonido.setMnemonic(KeyEvent.VK_S);
		barraMenu.add(sonido);

		itemMenu1 = new JCheckBoxMenuItem(ImagenReescalada("src/imgs/mute.png", 10, 10));
		itemMenu1.setBackground(Color.WHITE);
		sonido.add(itemMenu1);
		
		
		panelCentral = new JPanel();
		panelCentral.setLayout(new BorderLayout());
		imagen = new JLabel(new ImageIcon("src/imgs/Angry-juego.jpg"));
		this.getContentPane().add(panelCentral, BorderLayout.CENTER);
		panelCentral.add(imagen, BorderLayout.CENTER);

		panelAbajo = new JPanel();
		panelAbajo.setBackground(FONDOOSCURO);
		this.add(panelAbajo, BorderLayout.SOUTH);
		
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
	
	// Para poner una imagen con otro tamanyo

		private ImageIcon ImagenReescalada(String ruta, int ancho, int alto) {
			return new ImageIcon(
					new ImageIcon(ruta).getImage().getScaledInstance(ancho, alto, java.awt.Image.SCALE_SMOOTH));
		}
	

}
