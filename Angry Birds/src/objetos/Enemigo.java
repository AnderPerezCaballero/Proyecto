package objetos;

import java.awt.Point;

public class Enemigo extends ObjetoPrimitivo{
	
	private static final String IMAGEN = "/imgs/cerdo.png"; 
	
	private int radio;
	
	public Enemigo(int x,int y,int radio) {
		super(x,y);
		this.radio=radio;
	}
	
	public void setX(int x) {
		if (x-radio<0) {
		}else {
			this.x=x;
		}
	}
	
	public void setY(int y) {	
		if (y-radio<0) {
		}else {
			this.y=y;
		}
	}
	
	public int getRadio() {
		return radio;
	}
	
	public void setRadio(int radio) {
		this.radio = radio;
	}
	
	public boolean contienePunto(Point punto) {
		double dist= Math.sqrt(Math.pow(x-punto.getX(),2)+Math.pow(y-punto.getY(),2));
		return dist<= radio;
	}
	
	public void dibuja(VentanaJuego v) {
		//v.dibujaCirculo(x, y, radio, 3, Color.GREEN,Color.GREEN);
		v.dibujaImagen(IMAGEN, (double)y, (double) x,60,60, v.getEscalaDibujo(),0,1.0f);
	}
}
