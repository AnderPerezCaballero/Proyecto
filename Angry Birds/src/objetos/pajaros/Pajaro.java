package objetos.pajaros;

import java.awt.Color;import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import gui.juego.VentanaJuego;
import objetos.Cerdo;
import objetos.Dibujable;
import objetos.ObjetoNivel;
import objetos.Viga;
import objetos.Juego;
import objetos.Nivel;
import objetos.Objeto;

public class Pajaro extends Objeto implements Dibujable{

	private static final String IMAGEN = "/imgs/PajaroRojo.png";

	private static int radio = 15;

	private double vX;
	private double vY;

	private double segundosEnAire;
	private double momentoLanzado;

	private List<Point> posiciones;
	
	private boolean estaSeleccionado;
	private boolean lanzado;
	private boolean mover;

	/** Crea un nuevo pájaro
	 * @param x Posición en el eje x del centro del pájaro
	 * @param y Posición en el eje y del centro del pájaro
	 */
	public Pajaro(int x, int y) {
		super(x, y);
		vX = 0;// al principio los pajaros tiene que ser estaticos por lo que su velocidad es de 0 en ambas direcciones hasta que se realice el lanzamiento
		vY = 0;
		estaSeleccionado = false;
		lanzado = false;
		mover = false;
		posiciones = Collections.synchronizedList(new CopyOnWriteArrayList<>());
	}

	/** Crea un nuevo pájaro
	 * @param p Centro del pájaro
	 */
	public Pajaro(Point p) {
		this(p.x, p.y);
	}

	/** Lanza el pájaro desde el tirapájaros
	 * @param posicionLanzado Posición desde la que se lanza el pájaro
	 * @param posicionInicial Posición desde la que se ha arrastrado al pájaro para ser lanzado
	 */
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
	public void rebotaCon(ObjetoNivel elementoNivel) {
		if(elementoNivel instanceof Cerdo) {
			Cerdo cerdo = (Cerdo) elementoNivel;
//			vX = cerdo.getX() - x;
//			vY = y - cerdo.getY();
		}else {
			Viga viga = (Viga) elementoNivel;
			
			//Rebota por la izquierda o por la derecha
			if(viga.getY() - viga.getAltura() / 2 < y && viga.getY() + viga.getAltura() / 2 > y) {
			
				//Rebota por la izquierda
				if(vX > 0) {
					x = viga.getX() - viga.getAnchura() / 2 - radio;
					System.out.println("izquierda");
				
				//Rebota por la derecha
				}else {
					x = viga.getX() + viga.getAnchura() / 2 + radio;
					System.out.println("derecha");
				}
				vX = -vX;

			//Rebota por arriba o por abajo
			}else if(viga.getX() - viga.getAnchura() / 2 > x && viga.getX() + viga.getAnchura() / 2 < x) {
			
				//Rebota por arriba
				if(vY > 0) {
					y =  viga.getY() - viga.getAltura() / 2 + radio;
					System.out.println("arriba");
				
				//Rebota por abajo
				}else {
					y = viga.getY() + viga.getAltura() / 2 - radio;
					System.out.println("abajo");
				}
				vY = -vY;
				
			//Rebota con una esquina
			}else {
				vX = -vX;
				vY = -vY;
			}
		}
	}

	/** Mueve el pájaro en función de una espera entre fotogramas y una gravedad
	 * @param milisEntreFrames espera entre fotogramas
	 * @param gravedadX vector de gravedad en el eje X
	 * @param gravedadY vector de gravedad en el eje Y
	 */
	public void move(int milisEntreFrames, double gravedadX, double gravedadY, Nivel nivel) {
		mover = true;
		new Thread(() -> {
				while(mover) {
					segundosEnAire = System.currentTimeMillis() / 1000.0 - momentoLanzado + milisEntreFrames / 1000.0;

					vY = vY - gravedadY * segundosEnAire;
					vX = vX - gravedadX * segundosEnAire;
					
					x = x + (int) Math.round(vX * segundosEnAire - 0.5 * gravedadX * segundosEnAire * segundosEnAire);
					y = y - (int) Math.round(vY * segundosEnAire - 0.5 * gravedadY * segundosEnAire * segundosEnAire);

					//Choques
					if(choqueConSuelo()) {
						vY = -vY - 5;
						y = Juego.getYSuelo()- radio;
						aplicarRozamiento(10);
					}
					if(choqueConLimiteVertical()) {
						vX = -vX;
						x = radio;
						aplicarRozamiento(10);
					}
					
					List<ObjetoNivel> objetosAEliminar = new ArrayList<>();
					for(ObjetoNivel objetoNivel : nivel.getElementos()) {
						if(objetoNivel.chocaConPajaro(Pajaro.this)) {
							rebotaCon(objetoNivel);
							objetosAEliminar.add(objetoNivel);
						}
					}
					nivel.remove(objetosAEliminar);
					posiciones.add(new Point(x, y));
					
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
		}).start();		
		
	}

	/**Metodo para comprobar si el pajaro rebota con el borde horizontal izquierdo de la pantalla
	 * @return booleano indicando si existe o no choque
	 */
	public boolean choqueConLimiteVertical() {
		return x - radio <= 0;
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
		if(mover) {
			Point posicion = posiciones.get(0);
			posiciones.remove(0);
			v.dibujaImagen(IMAGEN, posicion.x, posicion.y, radio * 2, radio * 2, 1, 0, 1.0f);
		}else {
			v.dibujaImagen(IMAGEN, x, y, radio * 2, radio * 2, 1, 0, 1.0f);	
		}
	}
	
	public void cancelarMovimiento() {
		mover = false;
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

	public boolean contienePunto(Point punto) {
		return punto.distance(radio, radio) < radio * 2;
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

	@Override
	public String toString() {
		return String.format("Pájaro(%d, %d)", x, y);
	}
	
	public static String getRutaImagen() {
		return IMAGEN;
	}

	public static int getRadio() { 
		return radio;
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