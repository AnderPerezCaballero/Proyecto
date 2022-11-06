package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Ventanas.VentanaInicioPruebas;

public class VentanaInicioPlay1 extends VentanaInicio {
	
	public VentanaInicioPlay1() {
		super();
		panelAbajo = new JPanel();
		panelAbajo.setBackground(FONDOOSCURO);
		this.add(panelAbajo, BorderLayout.SOUTH);

		panelAbajo.setLayout(new FlowLayout());

		JButton inicioS = new MiBoton(Color.WHITE, Color.gray.brighter(), 60, 60);
		inicioS.setText("Inicar Sesion");
		panelAbajo.add(inicioS, BorderLayout.WEST);

		JButton registro = new MiBoton(Color.WHITE, Color.gray.brighter(), 60, 60);
		registro.setText("Registrarse");
		panelAbajo.add(registro, BorderLayout.EAST);
		
		inicioS.addKeyListener(escCerrar);
		registro.addKeyListener(escCerrar);

		this.setVisible(true);
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new VentanaInicioPlay1();
			}
		});
	}
}
