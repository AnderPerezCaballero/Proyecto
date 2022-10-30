package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import gestionUsuarios.GestionUsuarios;

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
		try {
			if(!GestionUsuarios.comprobarUsuario(inputUsuario.getText())) {
				resetTextos();
				labelMensaje.setText("El usuario introducido no existe");
			}else if(!GestionUsuarios.comprobarContraseña(inputUsuario.getText(), String.valueOf(inputContraseña.getPassword()))) {
				resetTextos();
				labelMensaje.setText("La contraseña introducida es incorrecta");
			}else {
				mensajeDeCarga = new MensajeCarga("Iniciando Sesión", botonAceptar);
				mensajeDeCarga.start();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static void main(String[] args) {
		new VentanaInicioSesion().iniciar();
	}
}
