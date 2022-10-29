package gui;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class MensajeCarga extends Thread{
	
	private String mensaje;
	private JButton boton;
	
	/**Clase que extiende de Thread, cuyo objetivo es sacar un mensaje de carga en un botón concreto
	 * @param mensaje	Mensaje de carga
	 * @param boton		Botón en el que sacar el mensaje
	 */
	public MensajeCarga(String mensaje, JButton boton) {
		super();
		this.mensaje = mensaje;
		this.boton = boton;
	}
	
	@Override
	public void run() {
		String puntos = "";
		while(!Thread.interrupted()) {
			if(puntos.length() > 2) {
				puntos = "";
			}else {
				puntos += ".";
			}
			
			final String ps = puntos;
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					boton.setText(mensaje + ps);
				}
			});
			try {
				Thread.sleep(1000);
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
