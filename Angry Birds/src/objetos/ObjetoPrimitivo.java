package objetos;

import java.awt.Point;

public abstract class ObjetoPrimitivo {
	protected int x;
	protected int y;
	public ObjetoPrimitivo(int x,int y) {
		// TODO Auto-generated constructor stub
		this.x=x;
		this.y=y;
	}
	public Point getLocation() {
		Point p= new Point(x,y);
		return p;
	}
	
	public int getX() {
		return x;
	}
	public abstract void setX(int x);
	
	public int getY() {
		return y;
	}
	public abstract void setY(int y);
}
