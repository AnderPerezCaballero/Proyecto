package juego.objetos.nivel;

import java.awt.Point;

import gui.juego.VentanaJuego;
import juego.objetos.Objeto;
import juego.objetos.Pajaro;



public class Viga extends Objeto implements ObjetoNivel{

	private int anchura;
	private int altura;
	private Material material;
	private int angsRotacion;
	private int vida;
	private String imagen;

	/**	Construye una nueva viga
	 * @param location centro de la viga
	 * @param radsRotacion	rotacion de la viga en grados (0 o 90)
	 * @param material	material de la viga
	 */
	public Viga(Point location, int angulo, Material material) {
		super(location.x, location.y);
		if(angulo == 0) {
			this.anchura = 16;
			this.altura = 120;
		}else {
			this.anchura = 120;
			this.altura = 16;
		}
		this.material = material;
		this.angsRotacion = angulo;
		if(material == Material.CRISTAL) {
			vida = 100;
			imagen = "/imgs/VigaCristal.png";
		}else if(material == Material.MADERA) {
			vida = 200;
			imagen = "/imgs/VigaMadera.png";
		}else {
			vida = 300;
			imagen = "/imgs/VigaPiedra.png";
		}
	}	

	/** Método que comprueba si una viga está chocando con un pájaro
	 * @param pajaro pájaro con el que la viga choca
	 * @return true si el pájaro está dentro del perímetro de la viga, false si no
	 */
	public boolean chocaConPajaro(Pajaro pajaro) {
		int radio = Pajaro.RADIO;
		int xPajaro = pajaro.getX();
		int yPajaro = pajaro.getY();
		return (x - anchura / 2) < (xPajaro + radio) && (x + anchura / 2) > (xPajaro - radio) &&
				(y - altura / 2) < (yPajaro + radio) && (y + altura / 2) > (yPajaro - radio);
	}

	/** Indica si la viga es eliminada después de colisionar y cambia el aspecto que tiene
	 *
	 */
	public boolean eliminado() {
		vida -= 100;
		if(vida <= 0) {
			return true;
		}else {
			if(vida == 200) {
				imagen = "/imgs/VigaPiedraDañada.png";
			}else{
				if(material == Material.PIEDRA) {
					imagen = "/imgs/VigaPiedraMuyDañada.png";
				}else {
					imagen = "/imgs/VigaMaderaDañada.png";
				}
			}
			return false;
		}
	}


	/** Devuelve la anchura de la viga
	 * @return Anchura de la viga en pixeles
	 */
	public int getAnchura() {
		return anchura;
	}

	/** Devuelve la altura de la viga
	 * @return Altura de la viga en pixeles
	 */
	public int getAltura() {
		return altura;
	}

	/** Dibuja la viga en la pantalla dependiendo de la rotación de la misma
	 *@param v Ventana en la que dibujar la viga
	 */
	public void dibuja(VentanaJuego v) {
		if(angsRotacion == 0) {
			v.dibujaImagen(imagen, x, y, anchura, altura, 1, Math.toRadians(angsRotacion), 1.0f);
		}else {
			v.dibujaImagen(imagen, x, y, altura, anchura, 1, Math.toRadians(angsRotacion), 1.0f);
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Viga(getLocation(), angsRotacion, material);
	}

	@Override
	public String toString() {
		return String.format("Viga de %s(%d, %d)[%dx%d]", material.toString().toLowerCase(), x, y, altura, anchura);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Viga) {
			Viga viga = (Viga) obj;
			return viga.x == x && viga.y == y;
		}
		return false;
	}
	
	

	//	public boolean tieneSoporte(Viga e) {
	//	if (x-altura/2-1==0) {
	//		//el suelo es el soporte
	//		return true;
	//	}if (altura/2-1==e.getX()+e.altura/2) {
	//		return true;
	//	}else {
	//		return false;
	//	}
	//}
	//	
	//	public boolean contienePunto(Point punto) {
	//	double distX= Math.abs(x-punto.getX());
	//	double distY=  Math.abs(y-punto.getY());
	//	return ((distX<=x+anchura/2)&&(distY<=altura/2));
	//}
	//	
	//	public void setX(int x) {
	//	if (x-anchura/2<0) {
	//	}else {
	//		this.x=x;
	//	}
	//}
	//
	//public void setY(int y) {	
	//	if (y-altura/2<0) {
	//	}else {
	//		this.y=y;
	//	}
	//}
	//
	//
	//public void setAnchura(double anchura) {
	//	if (!(anchura<0)) {
	//		this.anchura = anchura;
	//	}else {
	//	}
	//}
	//
	//public void setAltura(double altura) {
	//	if (!(altura<0)) {
	//	this.altura = altura;
	//	}else {
	//	}
	//}

}