package juego.objetos.nivel;

import juego.objetos.Dibujable;
import juego.objetos.Pajaro;

/** Interfaz para identificar los objetos que forman parte de un nivel.
 *  IMPORTANTE: Los pájaros no forman parte de este tipo de objetos
 * @author diego
 *
 */
public interface ObjetoNivel extends Dibujable, Cloneable{
	public boolean chocaConPajaro(Pajaro pajaro);
	public boolean eliminado();
	public Object clone() throws CloneNotSupportedException;
}


