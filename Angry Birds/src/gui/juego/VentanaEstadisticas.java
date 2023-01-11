package gui.juego;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.componentes.MiBoton;

@SuppressWarnings("serial")
public class VentanaEstadisticas extends JFrame{
	
	/** Crea una nueva ventana que contiene un JTable que muestra las estadísticas del usuario, las puntuaciones: FECHA, ESTRELLAS y NIVEL.
	 * También mostrará el nombre del usuario que está jugando la partida
	 * 
	 */
	public VentanaEstadisticas() {
		
		this.setTitle("ESTADÍSTICAS");
		// this.setSize(WIDTH, HEIGHT);
		this.setLayout(new BorderLayout());
		
		JPanel panelArriba = new JPanel();
		this.add(panelArriba, BorderLayout.NORTH);
		
		JPanel panelCentro = new JPanel();
		this.add(panelCentro, BorderLayout.CENTER);
		
		JPanel panelAbajo = new JPanel();
		this.add(panelAbajo, BorderLayout.SOUTH);
		
		JLabel titulo = new JLabel("ESTADÍSTICAS DEL USUARIO");
		titulo.setFont(new Font(Font.SERIF, Font.BOLD, 35));
		
		JButton atras = new MiBoton(Color.WHITE, Color.WHITE.darker(), 50, 50);
		atras.setText("Volver");
		panelAbajo.add(atras);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBackground(Color.WHITE);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		VentanaEstadisticas ve = new VentanaEstadisticas();
		
	}
}