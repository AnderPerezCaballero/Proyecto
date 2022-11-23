package gui.juego;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.componentes.MiPanel;
import gui.sesion.VentanaSesion;

@SuppressWarnings("serial")
public class VentanaNiveles extends JFrame{

	//JLabels
	private JLabel titulo;
	
	//JPanels
	private JPanel panelCentral;
	
	public VentanaNiveles() {
		
		// Inicialización de la ventana
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaSesion.class.getResource("/imgs/Icono.png")));
		
		setContentPane(new MiPanel("/imgs/FondoNiveles.jpg", new BorderLayout()));
		
		panelCentral = new JPanel(new GridLayout(3, 3));
		panelCentral.setOpaque(false);
		
		for (int i = 0; i < 9; i++) {
			JPanel p = new JPanel(new GridLayout(3, 3));
			p.setOpaque(false);
			for (int j = 0; j < 9; j++) {
				if(j == 4) {
					p.add(new JButton(String.format("Nivel %d", i+1)));
				}else {
					JPanel jp = new JPanel();
					jp.setOpaque(false);
					p.add(jp);
				}
			}
			panelCentral.add(p);
		}
		
		titulo = new JLabel("Selecciona un nivel");
		add(panelCentral, "Center");
		add(titulo, "North");
	}
	
	/**Incia la ventana de niveles
	 * 
	 */
	public void iniciar() {
		setVisible(true);
//		iniciarAnimaciones();
	}
	
	public static void main(String[] args) {
		new VentanaNiveles().iniciar();
	}
}
