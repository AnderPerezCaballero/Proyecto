package gui.sesion;

import java.awt.Color;
import java.awt.FlowLayout;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import gestionUsuarios.GestionUsuarios;
import gestionUsuarios.Usuario;
import gui.componentes.MensajeCarga;

@SuppressWarnings("serial")
public class VentanaRegistroSesion extends VentanaSesion{
	
	private JPanel panelConfirmarContraseña;
	private JPanel panelConfirmarContraseña2;

	private JLabel labelConfirmarContraseña;

	private JPasswordField inputConfirmarContraseña;


	/**Crea una nueva ventana para registrar un usuario
	 * 
	 */
	public VentanaRegistroSesion(JFrame ventanaAnterior) {
		super(8, ventanaAnterior);

		panelConfirmarContraseña = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelConfirmarContraseña2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

		labelConfirmarContraseña = new JLabel("Confirmar Contraseña:");
		inputConfirmarContraseña = new JPasswordField(getColumnas());

		panelConfirmarContraseña.add(labelConfirmarContraseña);
		panelConfirmarContraseña2.add(inputConfirmarContraseña);

		inputConfirmarContraseña.addKeyListener(getCierraConEsc());

		getPanelDatos().add(panelConfirmarContraseña);
		getPanelDatos().add(panelConfirmarContraseña2);
		getPanelDatos().add(getPanelMensaje());
		getPanelDatos().add(getPanelGuardarDispositivo());

		getBotonAceptar().setText("Registrarme");

		// Color de los paneles
		colorPaneles(getFondooscuro());

		// Color de los componentes
		colorComponentes(getFondooscuro());

		getInputContraseña().addActionListener(e -> {
			if (getInputContraseña().getPassword() != null) {
				inputConfirmarContraseña.requestFocus();
			}
		
		});

		inputConfirmarContraseña.addActionListener(e -> {
			if (getInputContraseña().getPassword() != null) {
				getBotonAceptar().requestFocus();
			}
		});
	}

	@Override
	protected void siguienteVentana() {
		try {
			if(GestionUsuarios.comprobarUsuario(getInputUsuario().getText())) {
				resetTextos();
				getLabelMensaje().setText("El usuario que has introducido ya está registrado");
			}else if(!String.valueOf(getInputContraseña().getPassword()).equals(String.valueOf(inputConfirmarContraseña.getPassword()))) {
				getInputContraseña().setText(null);
				inputConfirmarContraseña.setText(null);
				getLabelMensaje().setText("Las contraseñas introducidas deben coincidir");
				getInputContraseña().requestFocus();
			}else {
				setMensajeDeCarga(new MensajeCarga("Registrando nuevo usuario", "Usuario creado", getBotonAceptar()));
				getMensajeDeCarga().start();
				setUsuario(new Usuario(getInputUsuario().getText(), String.valueOf(getInputContraseña().getPassword())));
				GestionUsuarios.add(getUsuario());
				if(getGuardarDispositivo().isSelected()) {
					if(!GestionUsuarios.recordarUsuario(getUsuario())) {
						getLabelMensaje().setText("No ha sido posible recordar el usuario en este dispositivo");
					}
				}
				getMensajeDeCarga().interrupt();
				fin();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			getLabelMensaje().setText("No ha sido posible registrar el usuario");
		}
	}	

	@Override
	protected void colorPaneles(Color color) {
		super.colorPaneles(color);
		panelConfirmarContraseña.setBackground(color);
		panelConfirmarContraseña2.setBackground(color);
	}

	@Override
	protected void colorComponentes(Color color) {
		super.colorComponentes(color);
		labelConfirmarContraseña.setForeground(Color.WHITE);
		inputConfirmarContraseña.setForeground(Color.WHITE);
		inputConfirmarContraseña.setBackground(Color.BLACK);
	}

	@Override
	protected boolean condicionesAceptar() throws NullPointerException {
		return super.condicionesAceptar() && inputConfirmarContraseña.getPassword().length > 0;
	}

	@Override
	protected boolean condicionesBorrarMensaje() throws NullPointerException {
		return getInputUsuario().getText().length() > 0 && (getInputContraseña().getPassword().length > 0 || inputConfirmarContraseña.getPassword().length > 0);
	}

	@Override
	protected void resetTextos() {
		super.resetTextos();
		inputConfirmarContraseña.setText(null);
	}	

	public static void main(String[] args) {
		setUsuario(GestionUsuarios.usuarioAsociado());
		if(getUsuario() == null) {
			new VentanaRegistroSesion(null).iniciar();	
		}else {
			System.out.format("Se ha iniciado sesion con el siguiente usuario: %s", getUsuario());
		}
		new VentanaRegistroSesion(null).iniciar();	

	}


}
