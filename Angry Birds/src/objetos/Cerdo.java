package objetos;

import gui.juego.VentanaJuego;
import objetos.pajaros.Pajaro;

public class Cerdo extends Objeto implements ObjetoNivel{
	
	private static final String IMAGEN = "/imgs/Cerdo.png"; 
	private static final int radio = 20;
	
	/** Crea un nuevo cerdo con 100 puntos de vida
	 * @param x Coordenada x del centro del cerdo
	 * @param y Coordenada y del centro del cerdo
	 */
	public Cerdo(int x,int y) {
		super(x,y);
	}
	
	/** Indica si el cerdo choca con el pájaro
	 * @return true si choca, false si no
	 */
	public boolean chocaConPajaro(Pajaro pajaro) {
		return pajaro.getLocation().distance(getLocation()) < radio + Pajaro.getRadio();
	}
	
	/** Indica si el cerdo es eliminado después de colisionar. Devuelve true por defecto, pues el cerdo está diseñado para ser eliminado siempre
	 *@return true por defecto
	 */
	public boolean eliminado() {
		return true;
	}
	
	public void dibuja(VentanaJuego v) {
		v.dibujaImagen(IMAGEN, (double) x, (double) y, radio * 2, radio * 2, v.getEscalaDibujo(),0,1.0f);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Cerdo(x, y);
	}

	@Override
	public String toString() {
		return String.format("Cerdo(%d, %d)", x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Cerdo) {
			Cerdo cerdo = (Cerdo) obj;
			return cerdo.x == x && cerdo.y == y;
		}
		return false;
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
