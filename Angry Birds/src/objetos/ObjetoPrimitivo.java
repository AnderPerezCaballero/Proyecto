package objetos;

import java.awt.Point;

public abstract class ObjetoPrimitivo {
	protected int x;
	protected int y;
	public ObjetoPrimitivo() {
		x=(int)Math.random()*1000;
		y=(int)Math.random()*1000;
	}
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
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}
