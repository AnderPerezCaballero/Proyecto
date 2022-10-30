package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class VentanaRegistroSesion extends VentanaSesion{

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
		new MensajeCarga("Creando Nuevo Usuario", botonAceptar).start();
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
		if(color.equals(FONDOOSCURO)) {			
			labelConfirmarContraseña.setForeground(Color.WHITE);
			inputConfirmarContraseña.setForeground(Color.WHITE);
			inputConfirmarContraseña.setBackground(Color.BLACK);
		}else {
			labelConfirmarContraseña.setForeground(Color.BLACK);
			inputConfirmarContraseña.setForeground(Color.BLACK);
			inputConfirmarContraseña.setBackground(Color.WHITE);
		}
	}
	
	
	
	
}
