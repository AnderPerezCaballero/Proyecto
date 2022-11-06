package gui;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class MensajeCarga extends Thread{
	
	private String mensajeCarga;
	private String mensajeFinal;
	private JButton boton;
	private boolean interrumpido;
	
	/**Clase que extiende de Thread, cuyo objetivo es sacar un mensaje de carga en un botón concreto
	 * @param mensajeCarga	Mensaje de carga
	 * @param mensajeFinal Mensaje que expresa que la carga ha finalizado
	 * @param boton	Botón en el que sacar el mensaje
	 */
	public MensajeCarga(String mensajeCarga, String mensajeFinal, JButton boton) {
		super();
		this.mensajeCarga = mensajeCarga;
		this.mensajeFinal = mensajeFinal;
		this.boton = boton;
		this.interrumpido = false;
	}
	
	@Override
	public void run() {
		String puntos = "";
		int contador = 0;
		while(contador <= 1) {
			if(puntos.length() > 2) {
				puntos = "";
			}else {
				puntos += ".";
			}
			
			final String PUNTOS = puntos;
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					boton.setText(mensajeCarga + PUNTOS);
				}
			});
			try {
				Thread.sleep(1000);
			}catch(InterruptedException e) {
				interrumpido = true;
			}
			if(Thread.currentThread().isInterrupted() && !interrumpido) {
				interrumpido = true;
			}
			if(interrumpido && puntos.equals("...")) {
				contador++;
			}
		}
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				boton.setText(mensajeFinal);
			}
		});
	}
}
