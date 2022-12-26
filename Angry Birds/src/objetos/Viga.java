package objetos;
import java.awt.Color;
import java.awt.Point;

import gui.juego.VentanaJuego;
import objetos.pajaros.Pajaro;



public class Viga extends ObjetoPrimitivo implements ElementoNivel{
	
	private int anchura;
	private int altura;
	private Material material;
	private double rotacion;
	private int vida;
	private String imagen;
	
	/**	Construye una nueva viga
	 * @param location centro de la viga
	 * @param radsRotacion	rotacion de la viga en grados
	 * @param material	material de la viga
	 */
	public Viga(Point location, int angulo, Material material) {
		super(location.x, location.y);
		this.anchura = 16;
		this.altura = 120;
		this.material = material;
		this.rotacion = angulo;
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
		return (x - anchura / 2) < (pajaro.getX() + pajaro.getRadio()) && (x + anchura / 2) > (pajaro.getX() - pajaro.getRadio()) &&
			   (y - altura / 2) < (pajaro.getY() + pajaro.getRadio()) && (y + altura / 2) > (pajaro.getY() - pajaro.getRadio());
	}
	
	
	/** Devuelve la anchura de la viga
	 * @return Anchura de la viga en pixeles
	 */
	public double getAnchura() {
		return anchura;
	}
	
	/** Devuelve la altura de la viga
	 * @return Altura de la viga en pixeles
	 */
	public double getAltura() {
		return altura;
	}
	
	public void dibuja(VentanaJuego v) {
		v.dibujaImagen(imagen, x, y, anchura, altura, 1, Math.toRadians(rotacion), 1.0f);
	}
	
	@Override
	public String toString() {
		return String.format("Viga de %s(%d, %d)[%dx%d]", material.toString().toLowerCase(), x, y, altura, anchura);
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