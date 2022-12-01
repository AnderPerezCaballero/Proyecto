package gui.componentes;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import gui.sesion.VentanaSesion;

public class MensajeCarga extends Thread{
	
	private String mensajeCarga;
	private String mensajeFinal;
	private JButton boton;
	private boolean interrumpido;
	private VentanaSesion ventanaSesion;
	
	/**Clase que extiende de Thread, cuyo objetivo es sacar un mensaje de carga en un botón concreto
	 * @param mensajeCarga	Mensaje de carga
	 * @param mensajeFinal Mensaje que expresa que la carga ha finalizado
	 * @param ventanaSesion ventana de inicio sesion o registro en la que se quiere sacar el mensaje
	 */
	public MensajeCarga(String mensajeCarga, String mensajeFinal, VentanaSesion ventanaSesion) {
		super();
		this.mensajeCarga = mensajeCarga;
		this.mensajeFinal = mensajeFinal;
		this.ventanaSesion = ventanaSesion;
		this.boton = ventanaSesion.getBotonAceptar();
		this.interrumpido = false;
	}
	
	@Override
	public void run() {
		String puntos = "";
		while(!interrumpido || !puntos.equals("...")) {
			try {
				Thread.sleep(1000);
			}catch(InterruptedException e) {
				interrumpido = true;
			}
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
			if(Thread.currentThread().isInterrupted() && !interrumpido) {
				interrumpido = true;
			}
		}
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				boton.setText(mensajeFinal);
			}
		});		
		try {
			Thread.sleep(100);
		}catch(InterruptedException e) {}
		
		ventanaSesion.fin();
	}
}
