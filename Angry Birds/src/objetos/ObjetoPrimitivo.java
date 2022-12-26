package objetos;

import java.awt.Point;
import gui.juego.VentanaJuego;

public abstract class ObjetoPrimitivo {
	
	protected int x;
	protected int y;
	
	public ObjetoPrimitivo(int x,int y) {
		this.x=x;
		this.y=y;
	}
	
	public Point getLocation() {
		return new Point(x,y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public abstract void dibuja(VentanaJuego v);
	
//	public abstract boolean contienePunto(Point punto);
	
//	public abstract void setY(int y);
	
//	public abstract boolean chocaConPajaro(Pajaro pajaro);
	
//	public abstract void setX(int x);	
}