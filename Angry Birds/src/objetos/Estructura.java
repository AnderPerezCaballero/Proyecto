package objetos;

import objetos.pajaros.Pajaro;

public class Estructura extends ObjetoPrimitivo {
	protected double anchura;
	protected double altura;
	
	
	public Estructura(int x, int y,double anchura,double altura) {
		super(x,y);
		this.anchura=anchura;
		this.altura=altura;
		Pajaro p= new Pajaro(2,3,2,3,4);
		
		// TODO Auto-generated constructor stub
	}
	public double getAnchura() {
		return anchura;
	}


	public void setAnchura(double anchura) {
		this.anchura = anchura;
	}


	public double getAltura() {
		return altura;
	}


	public void setAltura(double altura) {
		this.altura = altura;
	}
	
}
