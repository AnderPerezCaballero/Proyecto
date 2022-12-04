package objetos;

import java.awt.Point;



public class Estructura extends ObjetoPrimitivo {
	
	private static final String IMAGEN = "/imgs/estructura.jpg";
	
	private double anchura;
	private double altura;
	
	public Estructura(int x, int y,double anchura,double altura) {
		super(x,y);
		this.anchura=anchura;
		this.altura=altura;
	}
	
	public void setX(int x) {
		if (x-anchura/2<0) {
		}else {
			this.x=x;
		}
	}
	
	public void setY(int y) {	
		if (y-altura/2<0) {
		}else {
			this.y=y;
		}
	}
	
	public double getAnchura() {
		return anchura;
	}

	public void setAnchura(double anchura) {
		if (!(anchura<0)) {
			this.anchura = anchura;
		}else {
		}
	}

	public double getAltura() {
		return altura;
	}

	public void setAltura(double altura) {
		if (!(altura<0)) {
		this.altura = altura;
		}else {
		}
	}
	
	public boolean contienePunto(Point punto) {
		double distX= Math.abs(x-punto.getX());
		double distY=  Math.abs(y-punto.getY());
		return ((distX<=x+anchura/2)&&(distY<=altura/2));
	}
	
	public boolean tieneSoporte(Estructura e) {
		if (x-altura/2-1==0) {
			//el suelo es el soporte
			return true;
		}if (altura/2-1==e.getX()+e.getAltura()/2) {
			return true;
		}else {
			return false;
		}
	}
	
	public void dibuja(VentanaJuego v) {
		v.dibujaImagen(IMAGEN, (double)y, (double) x, v.getEscalaDibujo(),0,0);
	}
}
