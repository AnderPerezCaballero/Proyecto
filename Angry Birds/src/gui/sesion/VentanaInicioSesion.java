package gui.sesion;

import java.sql.SQLException;
import gestionUsuarios.GestionUsuarios;
import gestionUsuarios.Usuario;
import gui.componentes.MensajeCarga;

public class VentanaInicioSesion extends VentanaSesion{

	private static final long serialVersionUID = 1L;

	/** Crea una nueva ventana para iniciar sesion con un usuario
	 * 
	 */
	public VentanaInicioSesion() {
		super(6);

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
				setMensajeDeCarga(new MensajeCarga("Iniciando Sesión", "Sesión iniciada", getBotonAceptar()));
				getMensajeDeCarga().start();
				usuario = new Usuario(getInputUsuario().getText(), String.valueOf(getInputContraseña().getPassword()));
				if(getGuardarDispositivo().isSelected()) {
					if(!GestionUsuarios.recordarUsuario(usuario)) {
						getLabelMensaje().setText("No ha sido posible recordar el usuario en este dispositivo");
					}
				}
				getMensajeDeCarga().interrupt();
			}
		} catch (SQLException e) {
			getLabelMensaje().setText("No ha sido posible iniciar sesión");
		}
	}	

	public static void main(String[] args) {
		usuario = GestionUsuarios.usuarioAsociado();
		if(usuario == null) {
			new VentanaInicioSesion().iniciar();	
		}else {
			System.out.format("Se ha iniciado sesion con el siguiente usuario: %s", usuario);
		}
		new VentanaInicioSesion().iniciar();	
	}
}
