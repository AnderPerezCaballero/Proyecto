package gui.inicio;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import gui.componentes.BlinkLabel;

@SuppressWarnings("serial")
public class VentanaInicioPlay2 extends VentanaInicio{
private KeyListener kl;
	
	public VentanaInicioPlay2() {
		super();
		
		BlinkLabel bl = new BlinkLabel("PULSA CUALQUIER TECLA PARA JUGAR");
		panelAbajo.add(bl);
		bl.setFont(new Font("Arial", Font.CENTER_BASELINE,15));
		
		panelPrincipal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				dispose();
				
			}
		});
		
		kl = new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_ESCAPE) {
					dispose();				
				} 
						
			}
			
		};
		
		this.addKeyListener(kl);
		this.addKeyListener(escCerrar);
		
		//Los listeners tendrían que pasar a la ventana de niveles pero no está hecha
		
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


