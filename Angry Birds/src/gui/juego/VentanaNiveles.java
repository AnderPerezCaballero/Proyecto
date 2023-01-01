package gui.juego;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.componentes.MiPanel;
import gui.sesion.VentanaSesion;
import juego.Juego;

@SuppressWarnings("serial")
public class VentanaNiveles extends JFrame{

	private static int radioIconoNiveles = 175;
	private static int nivelesBloqueados = 6;		//Número de niveles bloqueados
	
	//JLabels
	private JLabel labelTitulo;

	//JPanels
	private JPanel panelCentral;
	private JPanel panelSuperior;
	
	private Properties propiedades;
	private boolean siguienteNivel;

	public VentanaNiveles() {
		
		propiedades = new Properties();
		
		try (InputStream input = new FileInputStream("lib/propiedades.properties")) {
		    propiedades.load(input);
		    
		    radioIconoNiveles = Integer.parseInt(propiedades.getProperty("Radio_Icono_Niveles"));
		    nivelesBloqueados = Integer.parseInt(propiedades.getProperty("Numero_Niveles_Bloqueados"));
			
		} catch (IOException | IllegalArgumentException e) {
		    JOptionPane.showMessageDialog(VentanaNiveles.this, "Propiedades de la ventana no cargadas", "Aviso", JOptionPane.WARNING_MESSAGE);
		}	
		
		// Inicialización de la ventana
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaSesion.class.getResource("/imgs/Icono.png")));
		setContentPane(new MiPanel("/imgs/FondoNiveles.jpg", new BorderLayout()));
		siguienteNivel = false;
		
		labelTitulo = new JLabel("SELECCIONA UN NIVEL");
		panelSuperior = new JPanel(new FlowLayout());

		panelCentral = new JPanel(new GridLayout(3, 3));

		panelCentral.setOpaque(false);

		panelSuperior.add(labelTitulo);
		panelSuperior.setOpaque(false);

		try {
			labelTitulo.setFont(Font.createFont(Font.TRUETYPE_FONT, VentanaNiveles.class.getResourceAsStream("/fonts/LowBudget.ttf")).deriveFont(100.0f));
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		labelTitulo.setForeground(Color.WHITE);

		for (int i = 0; i < 9; i++) {
			final int NIVEL = i + 1;
			JLabel jl = new JLabel(VentanaSesion.imagenReescalada(String.format("/imgs/Nivel%d.png", NIVEL), radioIconoNiveles, radioIconoNiveles));
			jl.addMouseMotionListener(new MouseAdapter() {
				
				@Override
				public void mouseMoved(MouseEvent e) {
					if(e.getPoint().distance(jl.getSize().getWidth() / 2, jl.getSize().getHeight() / 2) < jl.getIcon().getIconHeight() / 2) {
						if(jl.getIcon().getIconHeight() <= radioIconoNiveles) {
							if(NIVEL <= 9 - nivelesBloqueados) {
								jl.setIcon(VentanaSesion.imagenReescalada("/imgs/Empezar.png", radioIconoNiveles + 30, radioIconoNiveles + 30));
							}else {
								jl.setIcon(VentanaSesion.imagenReescalada("/imgs/Candado.png", radioIconoNiveles + 30, radioIconoNiveles + 30));
							}
						}	
					}else {
						jl.setIcon(VentanaSesion.imagenReescalada(String.format("/imgs/Nivel%d.png", NIVEL), radioIconoNiveles, radioIconoNiveles));
					}
				}
				
			});
			
			jl.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getPoint().distance(jl.getSize().getWidth() / 2, jl.getSize().getHeight() / 2) < jl.getIcon().getIconHeight() / 2) {
						if(NIVEL <= 9 - nivelesBloqueados) {
							siguienteNivel = true;
							dispose();

							//Para no bloquear Swing
							new Thread(new Runnable() {
								@Override
								public void run() {
									Juego.init(NIVEL);		
								}
							}).start();
						}else {
							jl.setIcon(VentanaSesion.imagenReescalada(String.format("/imgs/Nivel%d.png", NIVEL), radioIconoNiveles, radioIconoNiveles));
							JOptionPane.showMessageDialog(VentanaNiveles.this, "Nivel bloqueado", "Aviso", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			});
			panelCentral.add(jl);
		}
		add(panelCentral, "Center");
		add(panelSuperior, "North");
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(!siguienteNivel) {
					VentanaSesion.cerrar(VentanaNiveles.this);
				}
			}
		});
	}
}