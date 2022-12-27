package objetos;

import java.awt.Point;
import gui.juego.VentanaJuego;

public abstract class Objeto implements Dibujable{
	
	protected int x;
	protected int y;
	
	/** Crea un nuevo objeto del juego
	 * @param x Posición en el eje x del centro del objeto
	 * @param y Posición en el eje y del centro del objeto
	 */
	public Objeto(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	/** Devuelve las coordenadas del centro del objeto
	 * @return Objeto Point que indica cual es el centro del objeto
	 */
	public Point getLocation() {
		return new Point(x,y);
	}
	
	/** Devuelve la coordenada x del centro del objeto
	 * @return Int que indica la coordenada x
	 */
	public int getX() {
		return x;
	}
	
	/** Devuelve la coordenada y del centro del objeto
	 * @return Int que indica la coordenada y
	 */
	public int getY() {
		return y;
	}
	
	public abstract void dibuja(VentanaJuego v);
	
//	public abstract boolean contienePunto(Point punto);
	
//	public abstract void setY(int y);
	
//	public abstract boolean chocaConPajaro(Pajaro pajaro);
	
//	public abstract void setX(int x);	
}