package gui.componentes;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import gui.juego.VentanaJuego;

/** Clase implementada para optimizar el acceso a las imágenes del juego
 * @author diego
 *
 */
public class Imagenes {

	private static Map<String,ImageIcon> imagenes = new HashMap<>();	// Variable local para guardar las imágenes y no recargarlas cada vez
 
	/** Intenta cargar la imagen del mapa interno de la clase, sino, la carga desde el disco. Devuelve null si no se ha podido conseguir la imagen
	 * @param rutaImagen ruta de la imagen a cargar
	 * @return null si no se puede cargar la imagen
	 */
	public static ImageIcon getImageIcon(String rutaImagen) {
		ImageIcon imageIcon = imagenes.get(rutaImagen);
		if (imageIcon==null) {
			try {
				imageIcon = new ImageIcon(VentanaJuego.class.getResource(rutaImagen));
				imagenes.put(rutaImagen, imageIcon);
			} catch (NullPointerException e) {  // Mirar si está en la clase llamadora en lugar de en la clase de la ventana de juego
				StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
				for (int i=1; i<stElements.length; i++) {
					StackTraceElement ste = stElements[i];
					if (!ste.getClassName().endsWith("VentanaJuego")) {  // Busca la clase llamadora a VentanaJuego y busca ahí el recurso
						try {
							Class<?> c = Class.forName(ste.getClassName());
							URL url = c.getResource(rutaImagen);
							if (url==null) return null;
							imageIcon = new ImageIcon(url);
							imagenes.put(rutaImagen, imageIcon);
							return imageIcon;
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
							return null;
						}
					}
				}
				return null;
			}			
		}
		return imageIcon;
	}
}
