package objetos.pajaros;

import java.util.stream.Stream;

import javax.swing.JFrame;

import objetos.Enemigo;
import objetos.Estructura;
import objetos.ObjetoPrimitivo;

public class Pajaro extends ObjetoPrimitivo {
	protected int radio;
	protected int vX;
	protected int vY;
	public Pajaro(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	public Pajaro(int x, int y, int radio, int vX, int vY) {
		super(x, y);
		this.radio = radio;
		this.vX = vX;
		this.vY = vY;
	}

	public void vuela() {
		this.setX(x+vX);
		this.setY(vY);
	} 
	
	public boolean choqueConLimitesLaterales(JFrame v) {
		return x+radio>=v.getWidth()||x-radio<=0;
	}
	public boolean choqueConLimitesVertical(JFrame v) {
		return y+radio>=v.getHeight()||y-radio<=0;
	}
	public boolean choqueConEstructura(Estructura e) {
//		Creo esste metodo para simplificar el metoddo choqueCon
		if (x+radio== e.getX()+e.getAnchura()/2||x+radio== e.getX()-e.getAnchura()/2) {
			return true;
		}else if (x-radio== e.getX()+e.getAnchura()/2||x-radio== e.getX()-e.getAnchura()/2) {
			return true;
		}else if (y+radio== e.getY()+e.getAltura()/2||y+radio== e.getY()-e.getAltura()/2) {
			return true;
		}else if (y-radio== e.getY()+e.getAltura()/2||y-radio== e.getY()-e.getAltura()/2) {
			return true;
		}else {
			return false;
		}
	}
	/**Metodo booleano para comprobar el choque con Enemigos 
	 * @param e Enemigo
	 * @return boolean
	 */
	public boolean choqueConEnemigos(Enemigo e) {
//		Creo este metodo para simplificar el metoddo choqueCon
		if (x+radio== e.getX()+e.getRadio()||x+radio== e.getX()-e.getRadio()) {
			return true;
		}if (x-radio== e.getX()+e.getRadio()||x-radio== e.getX()-e.getRadio()) {
			return true;
		}if (y+radio== e.getY()+e.getRadio()||y+radio== e.getY()-e.getRadio()) {
			return true;
		}if (y-radio== e.getY()+e.getRadio()||y-radio== e.getY()-e.getRadio()) {
			return true;
		}else {
			return false;
		}
	}
	/**Metodo booleano que devuelve si el pajaro choca con algun objeto
	 * @param p el objeto con el que se realiza la prueba
	 * @return boolean
	 */
	public boolean choqueCon (ObjetoPrimitivo p) {
		if(p instanceof Estructura) {
			Estructura e= (Estructura) p;
			return choqueConEstructura(e);
		}if (p instanceof Enemigo) {
			Enemigo e = (Enemigo) p;
			return choqueConEnemigos(e);
		}else {
			return false;
		}
	}
	public void dibuja(JFrame v) {
	}
	
	public int getRadio() { 
		return radio;
	}
	public void setRadio(int radio) {
		this.radio = radio;
	}
	public int getvX() {
		return vX;
	}
	public void setvX(int vX) {
		this.vX = vX;
	}
	public int getvY() {
		return vY;
	}
	public void setvY(int vY) {
		this.vY = vY;
	}  
}
