package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class VentanaInicioPlay1 extends VentanaInicio {
	
	public VentanaInicioPlay1() {
		super();
		panelAbajo = new JPanel();
		panelAbajo.setBackground(FONDOOSCURO);
		this.add(panelAbajo, BorderLayout.SOUTH);

		panelAbajo.setLayout(new FlowLayout());

		JButton inicioS = new MiBoton(Color.WHITE, Color.gray.brighter(), 60, 60);
		inicioS.setText("Inicar Sesion");
		inicioS.setFont(new Font("Arial", Font.BOLD,15));
		panelAbajo.add(inicioS, BorderLayout.WEST);

		JButton registro = new MiBoton(Color.WHITE, Color.gray.brighter(), 60, 60);
		registro.setText("Registrarse");
		registro.setFont(new Font("Arial", Font.BOLD,15));
		panelAbajo.add(registro, BorderLayout.EAST);
		
		inicioS.addActionListener(new ActionListener() {
			  
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaInicioSesion vsi = new VentanaInicioSesion();
				dispose();
			}
		});

		registro.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaRegistroSesion vss = new VentanaRegistroSesion();
				dispose();
			}
		});
		
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
