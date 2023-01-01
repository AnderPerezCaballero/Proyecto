package juego.objetos.nivel;

import juego.objetos.Dibujable;
import juego.objetos.Pajaro;

public interface ObjetoNivel extends Dibujable, Cloneable{
	public boolean chocaConPajaro(Pajaro pajaro);
	public boolean eliminado();
	public Object clone() throws CloneNotSupportedException;
}
