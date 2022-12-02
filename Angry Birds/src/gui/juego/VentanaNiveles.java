package gui.juego;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.componentes.MiPanel;
import gui.sesion.VentanaSesion;

@SuppressWarnings("serial")
public class VentanaNiveles extends JFrame{

	private static final int RADIOICONONIVELES = 126;
	private static final int NIVELESBLOQUEADOS = 4;		//Número de niveles bloqueados
	
	//JLabels
	private JLabel labelTitulo;

	//JPanels
	private JPanel panelCentral;
	private JPanel panelSuperior;

	public VentanaNiveles() {

		// Inicialización de la ventana
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaSesion.class.getResource("/imgs/Icono.png")));
		setContentPane(new MiPanel("/imgs/FondoNiveles.jpg", new BorderLayout()));

		labelTitulo = new JLabel("SELECCIONA UN NIVEL");
		panelSuperior = new JPanel(new FlowLayout());

		panelCentral = new JPanel(new GridLayout(3, 3));

		panelCentral.setOpaque(false);

		panelSuperior.add(labelTitulo);
		panelSuperior.setOpaque(false);

		labelTitulo.setFont(new Font(Font.SERIF, Font.BOLD, 50));
		labelTitulo.setForeground(Color.WHITE);

		for (int i = 0; i < 9; i++) {
			final int NIVEL = i + 1;
			JLabel jl = new JLabel(VentanaSesion.imagenReescalada(String.format("/imgs/Nivel%d.png", NIVEL), RADIOICONONIVELES, RADIOICONONIVELES));
			jl.addMouseMotionListener(new MouseAdapter() {
				
				@Override
				public void mouseMoved(MouseEvent e) {
					if(e.getPoint().distance(jl.getSize().getWidth() / 2, jl.getSize().getHeight() / 2) < jl.getIcon().getIconHeight() / 2) {
						if(jl.getIcon().getIconHeight() <= RADIOICONONIVELES) {
							if(NIVEL <= 9 - NIVELESBLOQUEADOS) {
								jl.setIcon(VentanaSesion.imagenReescalada("/imgs/Empezar.png", RADIOICONONIVELES + 30, RADIOICONONIVELES + 30));
							}else {
								jl.setIcon(VentanaSesion.imagenReescalada("/imgs/Candado.png", RADIOICONONIVELES + 30, RADIOICONONIVELES + 30));
							}
						}	
					}else {
						jl.setIcon(VentanaSesion.imagenReescalada(String.format("/imgs/Nivel%d.png", NIVEL), RADIOICONONIVELES, RADIOICONONIVELES));
					}
				}
				
			});
			
			jl.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getPoint().distance(jl.getSize().getWidth() / 2, jl.getSize().getHeight() / 2) < jl.getIcon().getIconHeight() / 2) {
						if(NIVEL <= NIVELESBLOQUEADOS) {
							System.out.println("Empezando el nivel " + NIVEL);
							System.exit(0);
						}else {
							jl.setIcon(VentanaSesion.imagenReescalada(String.format("/imgs/Nivel%d.png", NIVEL), RADIOICONONIVELES, RADIOICONONIVELES));
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
				VentanaSesion.cerrar(VentanaNiveles.this);
			}
		});
	}

	public static void main(String[] args) {
		new VentanaNiveles().setVisible(true);
	}
}