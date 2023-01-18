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

import gui.componentes.BlinkLabel;
import gui.juego.VentanaNiveles;

@SuppressWarnings("serial")
public class VentanaJugar2 extends VentanaJugar{
	
	private KeyListener kl;

	/** Crea una nueva ventana de juego, a través de la cual se lanza el juego al presionar cualquier tecla o botón del ratón
	 *  Esta ventana está pensada para ser lanzada en caso de que exista algún usuario asociado al dispositivo en el que se está ejecutando el programa
	 */
	public VentanaJugar2() {
		super();

		panelAbajo = new JPanel();
		panelAbajo.setOpaque(false);
		panelPrincipal.add(panelAbajo, BorderLayout.SOUTH);

		panelAbajo.setLayout(new FlowLayout());
		BlinkLabel bl = new BlinkLabel("PULSA CUALQUIER TECLA PARA JUGAR");
		panelAbajo.add(bl);
		bl.setFont(new Font("Arial", Font.CENTER_BASELINE, 50));
		
		panelPrincipal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fin();
			}
		});

		kl = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_ESCAPE) {
					fin();
				} 
			}
		};

		this.addKeyListener(kl);
		this.addKeyListener(escCerrar);
		
		//Si se presiona el enter el juego se inicia sin música
		addKeyListener(new KeyAdapter() {			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(clip.isActive()) {
						clip.close();
					}else {
						reproducirMusica();
					}
				}
			}
		});
		
		this.setVisible(true);

	}
	
	/** Cierra la ventana y da inicio al juego
	 * 
	 */
	private void fin() {
		new VentanaNiveles().setVisible(true);
		setEstaCerrada(true);
		dispose();
	}
}