package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaInicioSesion extends VentanaSesion{

	private static final long serialVersionUID = 1L;

	public VentanaInicioSesion() {
		super(5);
		
		// Color de los paneles
		colorPaneles(FONDOOSCURO);
		
		// Color de los componentes
		colorComponentes(FONDOOSCURO);
		
		panelDatos.add(panelMensaje);
		botonAceptar.setText("Iniciar Sesión");
		
		
		inputContraseña.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (inputContraseña.getPassword() != null) {
					botonAceptar.requestFocus();
				}
			}
		});
		
	}
	
	
	@Override
	protected void siguienteVentana() {
		new MensajeCarga("Iniciando Sesión", botonAceptar).start();
	}	
	
	public static void main(String[] args) {
		new VentanaInicioSesion().iniciar();
	}
}
