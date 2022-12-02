package gui.sesion;

import java.sql.SQLException;

import gestionUsuarios.GestionUsuarios;
import gestionUsuarios.Usuario;
import gui.componentes.MensajeCarga;
import gui.inicio.VentanaJugar;
import gui.inicio.VentanaJugar1;

@SuppressWarnings("serial")
public class VentanaInicioSesion extends VentanaSesion{

	/** Crea una nueva ventana para iniciar sesion con un usuario
	 * 
	 */
	public VentanaInicioSesion(VentanaJugar1 ventanaAnterior) {
		super(6, ventanaAnterior);
		
		// Color de los paneles
		colorPaneles(getFondooscuro());

		// Color de los componentes
		colorComponentes(getFondooscuro());

		getPanelDatos().add(getPanelMensaje());
		getPanelDatos().add(getPanelGuardarDispositivo());

		getBotonAceptar().setText("Iniciar Sesión");


		getInputContraseña().addActionListener(e -> {
			if (getInputContraseña().getPassword() != null) {
				getBotonAceptar().requestFocus();
			}
		});
	}

	@Override
	protected void siguienteVentana() {
		try {
			if(!GestionUsuarios.comprobarUsuario(getInputUsuario().getText())) {
				resetTextos();
				getLabelMensaje().setText("El usuario introducido no existe");
			}else if(!GestionUsuarios.comprobarContraseña(getInputUsuario().getText(), String.valueOf(getInputContraseña().getPassword()))) {
				resetTextos();
				getLabelMensaje().setText("La contraseña introducida es incorrecta");
			}else {
				setMensajeDeCarga(new MensajeCarga("Iniciando Sesión", "Sesión iniciada", this));
				getMensajeDeCarga().start();
				setUsuario(new Usuario(getInputUsuario().getText(), String.valueOf(getInputContraseña().getPassword())));
				if(getGuardarDispositivo().isSelected()) {
					if(!GestionUsuarios.recordarUsuario(getUsuario())) {
						getLabelMensaje().setText("No ha sido posible recordar el usuario en este dispositivo");
					}
				}
				getMensajeDeCarga().interrupt();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			getLabelMensaje().setText("No ha sido posible iniciar sesión");
		}
	}	
}
