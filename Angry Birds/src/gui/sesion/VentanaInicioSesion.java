package gui.sesion;

import java.sql.SQLException;

import gestionUsuarios.GestionUsuarios;
import gui.componentes.MensajeCarga;
import gui.inicio.VentanaJugar1;

@SuppressWarnings("serial")
public class VentanaInicioSesion extends VentanaSesion{

	/** Crea una nueva ventana para iniciar sesion con un usuario
	 * 
	 */
	public VentanaInicioSesion(VentanaJugar1 ventanaAnterior) {
		super(6, ventanaAnterior);
		
		this.setTitle("Iniciar Sesión");
		
		// Color de los paneles
		colorPaneles(getFondooscuro());

		// Color de los componentes
		colorComponentes();

		panelDatos.add(panelMensaje);
		panelDatos.add(panelGuardarDispositivo);

		botonAceptar.setText("Iniciar Sesión");

		inputContraseña.addActionListener(e -> {
			if (inputContraseña.getPassword() != null) {
				botonAceptar.requestFocus();
			}
		});
	}

	@Override
	protected void siguienteVentana() {
		try {
			if(!GestionUsuarios.comprobarUsuario(inputUsuario.getText())) {
				resetTextos(true);
				labelMensaje.setText("El usuario introducido no existe");
			}else if(!GestionUsuarios.comprobarContraseña(inputUsuario.getText(), String.valueOf(inputContraseña.getPassword()))) {
				resetTextos(false);
				labelMensaje.setText("La contraseña introducida es incorrecta");
				inputContraseña.requestFocus();
			}else {
				mensajeDeCarga = new MensajeCarga("Iniciando Sesión", "Sesión iniciada", this, botonAceptar);
				mensajeDeCarga.start();
				setUsuario(GestionUsuarios.getUsuario(inputUsuario.getText().hashCode()));
				if(guardarDispositivo.isSelected()) {
					if(!GestionUsuarios.recordarUsuario(getUsuario())) {
						labelMensaje.setText("No ha sido posible recordar el usuario en este dispositivo");
					}
				}
				mensajeDeCarga.interrupt();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			labelMensaje.setText("No ha sido posible iniciar sesión");
		}
	}	
}
