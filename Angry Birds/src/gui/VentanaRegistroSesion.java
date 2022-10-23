package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class VentanaRegistroSesion extends VentanaSesion{

	
	
	public VentanaRegistroSesion() {
		super(8);
		botonAceptar.setText("Registrarme");
		botonAceptar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				botonAceptar.getAction();
				botonAceptar.setText("Registrando nuevo Usuario...");
			}
		});
	}
	
	@Override
	public void siguienteVentana() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		new VentanaRegistroSesion().iniciar();
	}
}
