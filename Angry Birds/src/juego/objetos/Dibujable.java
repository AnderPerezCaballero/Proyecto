package juego.objetos;

import gui.juego.VentanaJuego;

/** Interfaz para identificar los objetos que pueden dibujarse en pantalla
 * @author diego
 *
 */
public interface Dibujable extends Localizable{
	public void dibuja(VentanaJuego v);
}
