package objetos.pajaros;

import java.awt.Color;import java.awt.Point;
import java.util.List;

import gui.juego.VentanaJuego;
import objetos.Cerdo;
import objetos.ElementoDibujable;
import objetos.ElementoNivel;
import objetos.Viga;
import objetos.Juego;
import objetos.ObjetoPrimitivo;

public class Pajaro extends ObjetoPrimitivo implements ElementoDibujable{

	private static final String IMAGEN = "/imgs/PajaroRojo.png";

	private static int radio = 15;

	private double vX;
	private double vY;

	private double segundosEnAire;
	private double momentoLanzado;

	private Habilidad habilidad;
	private boolean estaSeleccionado;
	private boolean lanzado;

	/**Constructor de pajaro normal
	 * @param x entero que indica la posicion en el eje x y ha de ser positivo o 0
	 * @param y entero que indica la posicion en el eje y y ha de ser positivo o 0
	 * @param color color del pájaro
	 */
	public Pajaro(int x, int y,Color color) {
		super(x, y);
		vX = 0;// al principio los pajaros tiene que ser estaticos por lo que su velocidad es de 0 en ambas direcciones hasta que se realice el lanzamiento
		vY = 0;
		habilidad = Habilidad.SINHABILIDAD;
		estaSeleccionado = false;
		lanzado = false;
	}

	public Pajaro(Point p, Color color) {
		this(p.x, p.y, color);
	}

	public void lanzar(Point posicionLanzado, Point posicionInicial) {
		double distanciaX = posicionLanzado.distance(posicionInicial.x, posicionLanzado.y); 
		double distanciaY = posicionLanzado.distance(posicionLanzado.x, posicionInicial.y);

		double angulo = Math.atan(distanciaY / distanciaX);		//en radianes
		double v0 = Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);

		vX = v0 * Math.cos(angulo);
		vY = v0 * Math.sin(angulo);

		if(posicionLanzado.x > posicionInicial.x) {
			vX = -vX;
		}
		if(posicionLanzado.y < posicionInicial.y) {
			vY = -vY;
		}

		lanzado = true;
		segundosEnAire = 0;
		momentoLanzado = System.currentTimeMillis() / 1000.0;
	}


	/** Dibuja un vector rojo que indica la trayectoria que va a seguir el pájaro al ser lanzado
	 * @param v ventana en la que se dibuja el vector
	 * @param posicionLanzado Posición desde la que se está lanzando el pájaro
	 * @param posicionInicial Posición inicial desde la que se ha arrastrado el pájaro para ser lanzado
	 */
	public void dibujarVectorLanzamiento(VentanaJuego v, Point posicionLanzado, Point posicionInicial) {
		v.dibujaFlecha(posicionLanzado.x, posicionLanzado.y, posicionInicial.x, posicionInicial.y, 2, Color.RED);
	}

	/** Realiza el rebote con el elemento del nivel
	 * @param elementoNivel elemento del nivel con el que se desea que el pájaro rebote
	 */
	public void rebotaCon(ElementoNivel elementoNivel) {
		if(elementoNivel instanceof Cerdo) {
			vX = -vX;
			vY = -vY;
		}else {
			Viga viga = (Viga) elementoNivel;
			if((viga.getX() - viga.getAnchura() / 2) < (x + radio) && (viga.getX() + viga.getAnchura() / 2) > (x - radio)) {
				vX = -vX;
			}
			if((viga.getY() - viga.getAltura() / 2) < (y + radio) && (viga.getY() + viga.getAltura() / 2) > (y - radio)) {
				vY = -vY;
			}
		}
	}


	/** Mueve el pájaro en función de una espera entre fotogramas y una gravedad
	 * @param milisEntreFrames espera entre fotogramas
	 * @param gravedadX vector de gravedad en el eje X
	 * @param gravedadY vector de gravedad en el eje Y
	 */
	public void move(int milisEntreFrames, double gravedadX, double gravedadY) {

		segundosEnAire = System.currentTimeMillis() / 1000.0 - momentoLanzado + milisEntreFrames / 1000.0;

		vY = vY - gravedadY * segundosEnAire;
		vX = vX - gravedadX * segundosEnAire;

		x = x + (int) Math.round(vX * segundosEnAire - 0.5 * gravedadX * segundosEnAire * segundosEnAire);
		y = y - (int) Math.round(vY * segundosEnAire - 0.5 * gravedadY * segundosEnAire * segundosEnAire);
		
	}


	/**Metodo para comprobar si el pajaro rebota con los bordes de la pantalla de manera horizontal. Solo se tiene en cuenta el borde izquierdo
	 * @param v ventana cuyos bordes se comprobaran
	 * @return booleano indicando si existe o no choque
	 */
	public boolean choqueConLimitesVerticales() {
		return x-getRadio()<=0;
	}

	/**Metodo para comprobar que el pájaro rebota con el suelo
	 * @return booleano indicando si existe o no choque
	 */
	public boolean choqueConSuelo() {
		return y + radio >= Juego.getYSuelo();
	}

	/** Aplica un rozamiento (disminución de la velocidad) al pájaro en el eje x
	 * @param rozamiento rozamiento a aplicar (número de puntos a disminuir en v)
	 */
	public void aplicarRozamiento(int rozamiento) {
		if(vX != 0) {
			if(vX > 0) {
				if(vX > rozamiento) {
					vX -= rozamiento;
				}else {
					vX = 0;		
				}
			}else {
				if(vX < rozamiento) {
					
				}else {
					vX = 0;		
				}
			}
		}
	}

	public void dibuja(VentanaJuego v) {
		v.dibujaImagen(IMAGEN, x, y, radio * 2, radio * 2, 1, 0, 1.0f);

	}

	public void setX(int x) {
		this.x=x;
	}

	public void setY(int y) {	
		this.y=y;
	}

	public void setLocation(Point p) {
		setX(p.x);
		setY(p.y);		
	}

	public int getRadio() { 
		return radio;
	}

	public boolean contienePunto(Point punto) {
		return punto.distance(radio, radio) < getRadio() * 2;
	}

	public boolean isSeleccionado() {
		return estaSeleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		estaSeleccionado = seleccionado;
	}

	public boolean isLanzado() {
		return lanzado;
	}

	public void setLanzado(boolean lanzado) {
		this.lanzado = lanzado;
	}

	public double getV() {
		return Math.sqrt(vX * vX + vY * vY);
	}

	public double getVX() {
		return vX;
	}

	public double getVY() {
		return vY;
	}

	public void reverseVY() {
		vY = -vY - 5;
	}

	public void reversevX() {
		vX = -vX;
	}

	@Override
	public String toString() {
		return String.format("Pájaro(%d, %d)", x, y);
	}
	
	
	//	/**Metodo booleano que devuelve si el pajaro choca con algun objeto
	//	 * @param p el objeto con el que se realiza la prueba
	//	 * @return boolean
	//	 */
	//	public ObjetoPrimitivo choqueCon (ObjetoPrimitivo p) {
	//		if(p instanceof Estructura) {
	//			Estructura e= (Estructura) p;
	//			return choqueConEstructura(e);
	//		}if (p instanceof Enemigo) {
	//			Enemigo e = (Enemigo) p;
	//			return choqueConEnemigos(e);
	//		}else {
	//			return null;
	//		}
	//	}
}