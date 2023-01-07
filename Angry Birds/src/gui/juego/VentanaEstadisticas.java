package gui.juego;

import java.awt.Color;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class VentanaEstadisticas extends JFrame{
	
	/** Crea una nueva ventana que contiene un JTable que muestra las estadísticas del usuario, como las puntuaciones logradas y el tiempo de juego
	 * 
	 */
	public VentanaEstadisticas() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBackground(Color.WHITE);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
	}
}
