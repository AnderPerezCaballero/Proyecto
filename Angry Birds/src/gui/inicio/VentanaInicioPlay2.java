package gui.inicio;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gui.componentes.BlinkLabel;
import gui.juego.VentanaNiveles;

@SuppressWarnings("serial")
public class VentanaInicioPlay2 extends VentanaInicio{
private KeyListener kl;
	
	public VentanaInicioPlay2() {
		super();
		
		panelAbajo = new JPanel();
		panelAbajo.setOpaque(false);
		panelPrincipal.add(panelAbajo, BorderLayout.SOUTH);

		panelAbajo.setLayout(new FlowLayout());
		BlinkLabel bl = new BlinkLabel("PULSA CUALQUIER TECLA PARA JUGAR");
		panelAbajo.add(bl);
		bl.setFont(new Font("Arial", Font.CENTER_BASELINE,15));
		
		panelPrincipal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				VentanaNiveles nv = new VentanaNiveles();
				dispose();
				
			}
		});
		
		kl = new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_ESCAPE) {
					VentanaNiveles nv = new VentanaNiveles();
					dispose();				
				} 
						
			}

		};
		
		this.addKeyListener(kl);
		this.addKeyListener(escCerrar);
		
		this.setVisible(true);
	}
	
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new VentanaInicioPlay2();
			}
		});
	}
		
}


