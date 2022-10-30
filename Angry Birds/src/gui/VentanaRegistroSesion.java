package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import gestionUsuarios.GestionUsuarios;
import gestionUsuarios.Usuario;

public class VentanaRegistroSesion extends VentanaSesion{

	private static final long serialVersionUID = 1L;
	
	private JPanel panelConfirmarContraseña;
	private JPanel panelConfirmarContraseña2;

	private JLabel labelConfirmarContraseña;


	private JPasswordField inputConfirmarContraseña;


	public VentanaRegistroSesion() {
		super(7);


		panelConfirmarContraseña = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelConfirmarContraseña2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

		labelConfirmarContraseña = new JLabel("Confirmar Contraseña:");
		inputConfirmarContraseña = new JPasswordField(COLUMNAS);

		panelConfirmarContraseña.add(labelConfirmarContraseña);
		panelConfirmarContraseña2.add(inputConfirmarContraseña);

		inputConfirmarContraseña.addKeyListener(cierraConEsc);

		panelDatos.add(panelConfirmarContraseña);
		panelDatos.add(panelConfirmarContraseña2);
		panelDatos.add(panelMensaje);

		botonAceptar.setText("Registrarme");

		// Color de los paneles
		colorPaneles(FONDOOSCURO);

		// Color de los componentes
		colorComponentes(FONDOOSCURO);



		inputContraseña.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (inputContraseña.getPassword() != null) {
					inputConfirmarContraseña.requestFocus();
				}
			}
		});

		inputConfirmarContraseña.addActionListener(new ActionListener() {

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
			if(GestionUsuarios.comprobarUsuario(inputUsuario.getText())) {
				resetTextos();
				labelMensaje.setText("El usuario que has introducido ya está registrado");
			}else if(!String.valueOf(inputContraseña.getPassword()).equals(String.valueOf(inputConfirmarContraseña.getPassword()))) {
				inputContraseña.setText(null);
				inputConfirmarContraseña.setText(null);
				labelMensaje.setText("Las contraseñas introducidas deben coincidir");
				inputContraseña.requestFocus();
			}else {
				mensajeDeCarga = new MensajeCarga("Registrando nuevo usuario", botonAceptar);
				GestionUsuarios.add(new Usuario(inputUsuario.getText(), String.valueOf(inputContraseña.getPassword())));
				mensajeDeCarga.start();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	

	public static void main(String[] args) {
		new VentanaRegistroSesion().iniciar();
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
		return inputUsuario.getText().length() > 0 && (inputContraseña.getPassword().length > 0 || inputConfirmarContraseña.getPassword().length > 0);
	}

	@Override
	protected void resetTextos() {
		super.resetTextos();
		inputConfirmarContraseña.setText(null);
	}	
	
	
}
