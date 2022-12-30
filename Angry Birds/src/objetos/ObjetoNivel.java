package objetos;

import objetos.pajaros.Pajaro;

public interface ObjetoNivel extends Dibujable, Cloneable{
	public boolean chocaConPajaro(Pajaro pajaro);
	public boolean eliminado();
	public Object clone() throws CloneNotSupportedException;
}
