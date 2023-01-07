package juego.objetos;

import java.awt.Point;

/** Interfaz para identificar los objetos que pueden localizarse
 * @author diego
 *
 */
public interface Localizable {
	public Point getLocation();
	public int getX();
	public int getY();
}
