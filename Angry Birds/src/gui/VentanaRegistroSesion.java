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
		super(8);
		
		
		panelConfirmarContraseña = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelConfirmarContraseña2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		//Cambiar el color de los paneles
		colorPaneles(FONDO);
		
		labelConfirmarContraseña = new JLabel("Confirmar Contraseña:");
		inputConfirmarContraseña = new JPasswordField(COLUMNAS);
		
		panelConfirmarContraseña.add(labelConfirmarContraseña);
		panelConfirmarContraseña2.add(inputConfirmarContraseña);
		panelMensaje.add(labelMensaje);
		
		panelDatos.add(panelConfirmarContraseña);
		panelDatos.add(panelConfirmarContraseña2);
		
		
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
	
	
}
