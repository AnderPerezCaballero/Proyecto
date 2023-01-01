package gui.juego;

import java.awt.Color;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class VentanaEstadisticas extends JFrame{
	
	public VentanaEstadisticas() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBackground(Color.WHITE);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
	}
}
