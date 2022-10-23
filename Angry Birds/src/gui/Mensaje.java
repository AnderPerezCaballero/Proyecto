package gui;

import javax.swing.JButton;

public class Mensaje extends Thread{
	
	private String mensaje;
	private JButton boton;
	
	public Mensaje(String mensaje, JButton boton) {
		super();
		this.mensaje = mensaje;
		this.boton = boton;
	}
	
	@Override
	public void run() {
		char[] puntos = new char[3];
		int contador = 0;
		while(!Thread.interrupted()) {
			if(contador > 2) {
				puntos = new char[3];
			}else {
				puntos[contador] = '.';
			}
			boton.setText(mensaje + puntos);
			try {
				Thread.sleep(1000);
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			contador++;
		}
	}
}
