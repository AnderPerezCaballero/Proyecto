package objetos.pajaros;

import java.awt.Color;
import java.awt.Point;

import gui.juego.VentanaJuego;
import objetos.Enemigo;
import objetos.Estructura;
import objetos.ObjetoPrimitivo;

public class Pajaro extends ObjetoPrimitivo {

	private static final String IMAGEN = "/imgs/PajaroRojo.png";

	private static int radio = 30;
	private int vX;
	private int vY;
	private Habilidad habilidad;

	/**Constructor de pajaro normal
	 * @param x entero que indica la posicion en el eje x y ha de ser positivo o 0
	 * @param y entero que indica la posicion en el eje y y ha de ser positivo o 0
	 * @param radio entero que define el tamaño del pajaro
	 */
	public Pajaro(int x, int y,Color color) {
		super(x, y);
		vX = 0;// al principio los pajaros tiene que ser estaticos por lo que su velocidad es de 0 en ambas direcciones hasta que se realice el lanzamiento
		vY =0;
		habilidad = Habilidad.SINHABILIDAD;
	}

	public void dibuja(VentanaJuego v) {

		v.dibujaImagen(IMAGEN, x, y, radio, radio, 1, 0,1.0f);
	}

	//	public void vuela() {
	//		this.setX(x+vX);
	//		this.setY(vY);// sera mas desarrollado hasta aplicar una función óptima
	//	} 

	/**Metodo para comprobar si el pajaro rebota con los bordes de la pantalla de manera horizontal
	 * @param v ventana cuyos bordes se comprobaran
	 * @return booleano indicando si existe o no choque
	 */
	public boolean choqueConLimitesLaterales(VentanaJuego v) {
		return x+radio>=v.getAnchura()||x-radio<=0;
	}

	/**Metodo para comprobar si el pajaro rebota con los bordes de la pantalla de manera horizontal
	 * @param v ventana cuyos bordes se comprobaran
	 * @return booleano indicando si existe o no choque
	 */
	public boolean choqueConLimitesVertical(VentanaJuego v) {
		return y+radio>=v.getAltura()||y-radio<=0;
	}

	/**Metdod para comprobar el choque con estructuras
	 * @param e Estructura (con la que se comprueba el choque)
	 * @return Boolean
	 */
	public boolean choqueConEstructura(Estructura e) {
		//		Creo esste metodo para simplificar el metoddo choqueCon		
		//		if () {
		//			return true;
		//		}if ((x+radio<= e.getX()+e.getAnchura()/2&&x+radio>= e.getX()-e.getAnchura()/2)&&(y-radio<= e.getY()+e.getAltura()/2&&y-radio>= e.getY()-e.getAltura()/2)) {
		//			return true;
		//		}if ((x-radio<= e.getX()+e.getAnchura()/2&&x-radio>= e.getX()-e.getAnchura()/2)&&(y+radio<= e.getY()+e.getAltura()/2&&y+radio>= e.getY()-e.getAltura()/2)) {
		//			return true;
		//		}if ((x-radio<= e.getX()+e.getAnchura()/2&&x-radio>= e.getX()-e.getAnchura()/2)&&(y-radio<= e.getY()+e.getAltura()/2&&y-radio>= e.getY()-e.getAltura()/2)) {
		//			return true;
		//		}else {
		//			return false;
		//		}

		return (x+radio<= e.getX()+e.getAnchura()/2&&x+radio>= e.getX()-e.getAnchura()/2)&&(y+radio<= e.getY()+e.getAltura()/2&&y+radio>= e.getY()-e.getAltura()/2)
				|| (x+radio<= e.getX()+e.getAnchura()/2&&x+radio>= e.getX()-e.getAnchura()/2)&&(y-radio<= e.getY()+e.getAltura()/2&&y-radio>= e.getY()-e.getAltura()/2)
				|| (x-radio<= e.getX()+e.getAnchura()/2&&x-radio>= e.getX()-e.getAnchura()/2)&&(y+radio<= e.getY()+e.getAltura()/2&&y+radio>= e.getY()-e.getAltura()/2)
				|| (x-radio<= e.getX()+e.getAnchura()/2&&x-radio>= e.getX()-e.getAnchura()/2)&&(y-radio<= e.getY()+e.getAltura()/2&&y-radio>= e.getY()-e.getAltura()/2);
	}

	/**Metodo booleano para comprobar el choque con Enemigos 
	 * @param e Enemigo
	 * @return boolean
	 */
	public boolean choqueConEnemigos(Enemigo e) {
		//		Creo este metodo para simplificar el metoddo choqueCon
		//		if (x+radio== e.getX()-e.getRadio()) {
		//			return true;
		//		}if (x-radio== e.getX()+e.getRadio()) {
		//			return true;
		//		}if (y+radio== e.getY()-e.getRadio()) {
		//			return true;
		//		}if (y-radio== e.getY()+e.getRadio() ) {
		//			return true;
		return (Math.sqrt(Math.pow(x-e.getX(),2)+Math.pow(y-e.getY(),2))<=radio+e.getRadio());
		//ambos metodos posiblemente deban tener uno complementario comprobando la fuerza del impacto
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

	//	public void dibuja(JFrame v) {
	//	}

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

	/**Metodo set del parametro radio que no permite poner numeros negativos
	 * @param radio solo permite enteros positivos
	 */
	public void setRadio(int radio) {
		if (radio<0) {
		}else {
			this.radio = radio;
		}// luego despues de probar definire mejor entre que parametros podra variar verdaderamente el radio
	}

	public int getvX() {
		return vX;
	}

	public void setvX(int vX) {
		this.vX = vX; // Metodo que sera usuado para aplicarle el movimiento al pajaroo en conunto de setvY
	}

	public int getvY() {
		return vY;
	}

	public void setvY(int vY) {
		this.vY = vY;
	}

	public boolean contienePunto(Point punto) {
		return punto.distance(radio, radio) < radio * 2;
	}

	public void mueve(int tiempo) {
		this.x= x+vX;
		this.y=y+vY-gravedad(tiempo);
	}

	public int gravedad(int tiempo) {
		if(tiempo==5) {
			return (int)Math.pow(2,5);
		}
		return 2*gravedad(tiempo+1);
	}
}
