package objetos;

import gui.juego.VentanaJuego;

public class Cerdo extends ObjetoPrimitivo{
	
	private static final String IMAGEN = "/imgs/Cerdo.png"; 
	private static final int radio = 20;
	
	public Cerdo(int x,int y) {
		super(x,y);
	}
	
	public void dibuja(VentanaJuego v) {
		v.dibujaImagen(IMAGEN, (double) x, (double) y, radio * 2, radio * 2, v.getEscalaDibujo(),0,1.0f);
	}

	@Override
	public String toString() {
		return String.format("Cerdo(%d, %d)", x, y);
	}
	
	

	
//	public boolean contienePunto(Point punto) {
//	double dist= Math.sqrt(Math.pow(x-punto.getX(),2)+Math.pow(y-punto.getY(),2));
//	return dist<= radio;
//}
//	
//	@Override
//	public void setY(int y) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	public int getRadio() {
//	return radio;
//}
//
//public void setRadio(int radio) {
//	this.radio = radio;
//}
//	
//	public void setX(int x) {
//	if (x-radio<0) {
//	}else {
//		this.x=x;
//	}
//}
//
//public void setY(int y) {	
//	if (y-radio<0) {
//	}else {
//		this.y=y;
//	}
//}
}
