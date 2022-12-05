package objetos.pajaros;

import java.awt.Color;
import java.awt.Point;

import gui.juego.VentanaJuego;
import objetos.Enemigo;
import objetos.Estructura;
import objetos.GrupoOP;
import objetos.ObjetoPrimitivo;

public class Pajaro extends ObjetoPrimitivo {

	private static final String IMAGEN = "/imgs/PajaroRojo.png";

	private static int radio = 30;
	private double vX;
	private double vY;
	private Habilidad habilidad;
	private boolean estaSeleccionado;
	private boolean lanzado;
	private long tiempoEnAire;
	private long momentoLanzado;

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
		estaSeleccionado = false;
		lanzado = false;
	}
	
	public Pajaro(Point p, Color color) {
		this(p.x, p.y, color);
	}

	public void dibuja(VentanaJuego v) {
		v.dibujaImagen(IMAGEN, x, y, radio, radio, 1, 0, 1.0f);
		
	}
	
	public void lanzar(Point posicionLanzado, Point posicionInicial) {
		double distanciaX = posicionLanzado.distance(posicionInicial.x, posicionLanzado.y); 
		double distanciaY = posicionLanzado.distance(posicionLanzado.x, posicionInicial.y); 
		vX = distanciaX * (distanciaX / (distanciaX * distanciaX + distanciaY * distanciaY));
		vY = distanciaY * (distanciaX / (distanciaX * distanciaX + distanciaY * distanciaY));
		lanzado = true;
		tiempoEnAire = 0;
		momentoLanzado = System.currentTimeMillis();
	}
	
	public void move(int milisEntreFrames) {
		tiempoEnAire += System.currentTimeMillis() - momentoLanzado;
		System.out.println(x);
		System.out.println(y);
		x = (int) Math.round(x + vX * milisEntreFrames);
		y = (int) Math.round(y + vY * milisEntreFrames );
	}
	//	public void vuela() {
	//		this.setX(x+vX);/ (1000 * milisEntreFrames) +  tiempoEnAire * tiempoEnAire / (1000 * 1000 * milisEntreFrames * milisEntreFrames));
	//}
	//		this.setY(vY);// sera mas desarrollado hasta aplicar una función óptima
	//	} 

	/**Metodo para comprobar si el pajaro rebota con los bordes de la pantalla de manera horizontal
	 * @param v ventana cuyos bordes se comprobaran
	 * @return booleano indicando si existe o no choque
	 */
	public boolean choqueConLimitesVerticales(VentanaJuego v) {
		return x+getRadio()>=v.getAnchura()||x-getRadio()<=0;
	}

	/**Metodo para comprobar si el pajaro rebota con los bordes de la pantalla de manera horizontal
	 * @param v ventana cuyos bordes se comprobaran
	 * @return booleano indicando si existe o no choque
	 */
	public boolean choqueConLimitesHorizontales(VentanaJuego v) {
		return y+getRadio()<=0||y-getRadio()>=700;
	}

	/**Metdod para comprobar el choque con estructuras
	 * @param e Estructura (con la que se comprueba el choque)
	 * @return Boolean
	 */
	public Estructura choqueConEstructura(GrupoOP op) {
		for (ObjetoPrimitivo o: op.getGrupoOP()) {
			if(o instanceof Estructura) {
				Estructura e = (Estructura)o;
				if ((x+radio <= e.getX()+e.getAnchura()/2&&x+radio>= e.getX()-e.getAnchura()/2)&&((y+getRadio()<= e.getY()+e.getAltura()/2)&&(y+getRadio()>= e.getY()-e.getAltura()/2))
				|| (x+getRadio()<= e.getX()+e.getAnchura()/2&&x+getRadio()>= e.getX()-e.getAnchura()/2)&&(y-getRadio()<= e.getY()+e.getAltura()/2&&y-getRadio()>= e.getY()-e.getAltura()/2)
				|| (x-getRadio()<= e.getX()+e.getAnchura()/2&&x-getRadio()>= e.getX()-e.getAnchura()/2)&&(y+getRadio()<= e.getY()+e.getAltura()/2&&y+getRadio()>= e.getY()-e.getAltura()/2)
				|| (x-getRadio()<= e.getX()+e.getAnchura()/2&&x-getRadio()>= e.getX()-e.getAnchura()/2)&&(y-getRadio()<= e.getY()+e.getAltura()/2&&y-getRadio()>= e.getY()-e.getAltura()/2)){
					return e;
			}
		}
		}return null;
	}

	/**Metodo booleano para comprobar el choque con Enemigos 
	 * @param e Enemigo
	 * @return boolean
	 */
	public Enemigo choqueConEnemigos(GrupoOP op) {
		for (ObjetoPrimitivo o: op.getGrupoOP()) {
			if (o instanceof Enemigo) {
				Enemigo e= (Enemigo) o;
				Point p= new Point(e.getX(), e.getY());
				if(p.distance(x,y)<e.getRadio()+getRadio()){
					return e;
				}
			}
		}return null;
		//ambos metodos posiblemente deban tener uno complementario comprobando la fuerza del impacto
	}

	/**Metodo booleano que devuelve si el pajaro choca con algun objeto
	 * @param p el objeto con el que se realiza la prueba
	 * @return boolean
	 */
	public ObjetoPrimitivo choqueCon (ObjetoPrimitivo p) {
		if(p instanceof Estructura) {
			Estructura e= (Estructura) p;
			return choqueConEstructura(e);
		}if (p instanceof Enemigo) {
			Enemigo e = (Enemigo) p;
			return choqueConEnemigos(e);
		}else {
			return null;
		}
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
		return radio - 20;
	}

	/**Metodo set del parametro radio que no permite poner numeros negativos
	 * @param radio solo permite enteros positivos
	 */

	public double getvX() {
		return vX;
	}

	public void setvX(double vX) {
		this.vX = vX; // Metodo que sera usuado para aplicarle el movimiento al pajaroo en conunto de setvY
	}

	public double getvY() {
		return vY;
	}

	public void setvY(double vY) {
		this.vY = vY;
	}

	public boolean contienePunto(Point punto) {
		return punto.distance(radio, radio) < getRadio() * 2;
	}

	public int gravedad(int tiempo) {
		if(tiempo==5) {
			return (int)Math.pow(2,5);
		}
		return 2*gravedad(tiempo+1);
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

}
