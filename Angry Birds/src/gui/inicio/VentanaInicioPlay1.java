package gui.inicio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import gui.componentes.MiBoton;
import gui.sesion.VentanaInicioSesion;
import gui.sesion.VentanaRegistroSesion;
import gui.sesion.VentanaSesion;


@SuppressWarnings("serial")
public class VentanaInicioPlay1 extends VentanaInicio {

	public VentanaInicioPlay1() {
		super();
		panelAbajo = new JPanel();
		panelAbajo.setBackground(VentanaSesion.getFondooscuro());
		this.add(panelAbajo, BorderLayout.SOUTH);

		panelAbajo.setLayout(new FlowLayout());

		JButton inicioS = new MiBoton(Color.WHITE, Color.gray.brighter(), 35, 35);
		inicioS.setText("Inicar Sesion");
		inicioS.setFont(new Font("Arial", Font.BOLD,15));
		panelAbajo.add(inicioS, BorderLayout.WEST);

		JButton registro = new MiBoton(Color.WHITE, Color.gray.brighter(), 35, 35);
		registro.setText("Registrarse");
		registro.setFont(new Font("Arial", Font.BOLD,15));
		panelAbajo.add(registro, BorderLayout.EAST);

		inicioS.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabled(false);
				new VentanaInicioSesion(VentanaInicioPlay1.this).iniciar();
			}
		});

		registro.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabled(false);
				new VentanaRegistroSesion(VentanaInicioPlay1.this).iniciar();
			}
		});


		inicioS.addKeyListener(escCerrar);
		registro.addKeyListener(escCerrar);
		this.setVisible(true);
	}
}
