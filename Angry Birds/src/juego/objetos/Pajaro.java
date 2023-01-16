package juego.objetos;

import java.awt.Color;import java.awt.Point;import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import gui.juego.VentanaJuego;
import juego.Juego;
import juego.objetos.nivel.Cerdo;
import juego.objetos.nivel.Nivel;
import juego.objetos.nivel.ObjetoNivel;
import juego.objetos.nivel.Viga;

public class Pajaro extends Objeto implements Dibujable{

	public static final String IMAGEN = "/imgs/PajaroRojo.png";
	public static final int RADIO = 15;

	private double vX;
	private double vY;

	private double segundosEnAire;
	private double momentoLanzado;

	private List<Point> posiciones;
	private Point posicionPintado;

	private boolean estaSeleccionado;
	private boolean lanzado;
	private boolean mover;

	private Map<Point, List<ObjetoNivel>> mapaObjetosAEliminar;

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
		mapaObjetosAEliminar = new HashMap<>();
	}

	/** Crea un nuevo pájaro
	 * @param p Coordenadas del centro del pájaro
	 */
	public Pajaro(Point p) {
		this(p.x, p.y);
	}

	/**Crea un nuevo pájaro en la posición inicial que le corresponde en el juego
	 * 
	 */
	public Pajaro() {
		this(Juego.POSICIONINICIALPAJARO.x, Juego.POSICIONINICIALPAJARO.y);
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
		posiciones.add(getLocation());
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
	private void rebotaCon(ObjetoNivel elementoNivel) {
		if(elementoNivel instanceof Cerdo) {
			Cerdo cerdo = (Cerdo) elementoNivel;

			double distancia = Math.hypot(this.x - cerdo.getX(), this.y - cerdo.getY());

			//Colocar el pájaro en el borde del cerdo
			x = (int) Math.round(cerdo.getX() + Cerdo.RADIO * (x - cerdo.getX()) / distancia);
			y = (int) Math.round(cerdo.getY() + Cerdo.RADIO * (y - cerdo.getY()) / distancia);

			double v = Math.hypot(this.vX, this.vY);
			double angulo = Math.atan2(this.y - cerdo.getY(), this.x - cerdo.getX()) - Math.atan2(this.vY, this.vX);
			double restitution = 0.5; //constante de restitución -> energía conservada en una colisión estática

			this.vX = -restitution * v * Math.cos(angulo);
			this.vY = -restitution * v * Math.sin(angulo);

		}else {
			Viga viga = (Viga) elementoNivel;

			//Rebota por la izquierda o por la derecha
			if(viga.getY() - viga.getAltura() / 2 < y && viga.getY() + viga.getAltura() / 2 > y) {

				//Rebota por la izquierda
				if(vX > 0) {
					x = viga.getX() - viga.getAnchura() / 2 - RADIO;

					//Rebota por la derecha
				}else {
					x = viga.getX() + viga.getAnchura() / 2 + RADIO;
				}

				vX = -vX;
				aplicarRozamiento(10);

				//Rebota por arriba o por abajo
			} else if(viga.getX() - viga.getAnchura() / 2 < x && viga.getX() + viga.getAnchura() / 2 > x) {
				//Rebota por arriba
				if(vY < 0) {
					y =  viga.getY() - viga.getAltura() / 2 - RADIO;

					//Rebota por abajo
				}else {
					y = viga.getY() + viga.getAltura() / 2 + RADIO;
				}

				vY = -vY - 20;

			//Rebota con una esquina
			}else {
				
				if(vX > 0) {
					if(viga.getX() - viga.getAnchura() / 2 > x) {
						vX = -vX;
					}
				}else {
					if(viga.getX() - viga.getAnchura() / 2 < x) {
						vX = -vX;
					}					
				}
				vY = -vY - 20;
				aplicarRozamiento(10);	
			}

		}
	}

	/** Mueve el pájaro en función de una espera entre fotogramas y una gravedad
	 * 
	 */
	public void move(Nivel nivel) {
		mover = true;
		new Thread(() -> {

			// Creo una copia profunda de los elementos de la lista de objetos del nivel, para poder modificarlos sin interferir en el dibujado
			List<ObjetoNivel> objetosNivel = nivel.getCopiaObjetos();
			while(mover) {
				segundosEnAire = System.currentTimeMillis() / 1000.0 - momentoLanzado + Juego.MILISEGUNDOSENTREFRAMES / 1000.0;

				vY = vY - Juego.GRAVEDADY * segundosEnAire;
				vX = vX - Juego.GRAVEDADX * segundosEnAire;

				x = x + (int) Math.round(vX * segundosEnAire - 0.5 * Juego.GRAVEDADX * segundosEnAire * segundosEnAire);
				y = y - (int) Math.round(vY * segundosEnAire - 0.5 * Juego.GRAVEDADY * segundosEnAire * segundosEnAire);

				//Choques
				if(choqueConSuelo()) {
					vY = -vY - 20;
					y = Juego.YPOSICIONSUELO- RADIO;
					aplicarRozamiento(20);
				}
				if(choqueConLimiteVertical()) {
					vX = -vX;
					x = RADIO;
					aplicarRozamiento(10);
				}

				List<ObjetoNivel> eliminados = new ArrayList<>();
				for(ObjetoNivel objetoNivel : objetosNivel) {
					if(objetoNivel.chocaConPajaro(Pajaro.this)) {
						rebotaCon(objetoNivel);
						Point posicion = new Point(x, y);

						mapaObjetosAEliminar.putIfAbsent(posicion, new ArrayList<>());
						mapaObjetosAEliminar.get(posicion).add(objetoNivel);
						if(objetoNivel.eliminado()) {
							eliminados.add(objetoNivel);
						}

					}
				}
				if(eliminados.size() > 0) {
					objetosNivel.removeAll(eliminados);
				}
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
		return x - RADIO <= 0;
	}

	/**Metodo para comprobar que el pájaro rebota con el suelo
	 * @return true si choca, false si no
	 */
	public boolean choqueConSuelo() {
		return y + RADIO >= Juego.YPOSICIONSUELO;
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
					vX += rozamiento;
				}else {
					vX = 0;		
				}
			}
		}
	}

	/** Dibuja el pájaro en la ventana
	 *@param v Ventana en la que se dibuja el pájaro
	 */
	public void dibuja(VentanaJuego v) {
		if(mover) {
			if(posiciones.size() > 0 && posiciones.get(0) != null) {
				posicionPintado = posiciones.get(0);
				posiciones.remove(0);
			}
			try {
				for(int j = 0; j < 2; j++) {
					int i = 0;
					while(mapaObjetosAEliminar.containsKey(posiciones.get(i))) {
						i++;
					}
					posiciones.remove(i);
				}
			}catch (IndexOutOfBoundsException e) {}
			v.dibujaImagen(IMAGEN, posicionPintado.x, posicionPintado.y, RADIO * 2, RADIO * 2, 1, 0, 1.0f);
		}else {
			v.dibujaImagen(IMAGEN, x, y, RADIO * 2, RADIO * 2, 1, 0, 1.0f);	
		}
	}

	/** Manda a eliminar los objetos del nivel que requieran de ser eliminados en ese momento en función de la posición de pintado
	 * @param nivel Nivel cuyos objetos han de ser eliminados
	 */
	public void eliminarObjetos(Nivel nivel) {		
		List<ObjetoNivel> objetosAEliminar = mapaObjetosAEliminar.get(posicionPintado);
		if(objetosAEliminar != null) {
			for(ObjetoNivel objeto : objetosAEliminar) {
				nivel.remove(nivel.getReferenciaCopia(objeto));
			}
		}
	}

	/** Detiene el hilo a través del cual se calculan las posiciones del pájaro
	 * 
	 */
	public void cancelarMovimiento() {
		mover = false;
	}

	/** Establece una nueva posición para el pájaro
	 * @param p Coordenadas de posición nuevas para el pájaro
	 */
	public void setLocation(Point p) {
		x = p.x;
		y = p.y;		
	}

	/** Establece una nueva posición en el eje x para el pájaro
	 * @param x Nueva coordenada del eje x para el pájaro
	 */
	public void setX(int x) {
		this.x = x;
	}

	/** Establece una nueva posición en el eje y para el pájaro
	 * @param y Nueva coordenada del eje y para el pájaro
	 */
	public void setY(int y) {
		this.y = y;
	}

	/** Indica si el pájaro se encuentra actualmente seleccionado por el usuario
	 * @return true si está seleccionado, false si no
	 */
	public boolean isSeleccionado() {
		return estaSeleccionado;
	}

	/** Establece un nuevo valor para el estado de selección del pájaro
	 * @param seleccionado nuevo valor booleano de selección a establecer
	 */
	public void setSeleccionado(boolean seleccionado) {
		estaSeleccionado = seleccionado;
	}

	/** Devuelve si el pájaro ha sido lanzado, es decir, si está en el aire o no
	 * @return true si ya ha sido lanzado, false si no
	 */
	public boolean isLanzado() {
		return lanzado;
	}

	/** Indica si el pájaro está actualmente quieto
	 * @return true si sus siguientes 5 posiciones son iguales, falase si nos
	 */
	public boolean isQuieto() {
		try {
			for(int i = 1; i < 5; i++) {
				if(!posicionPintado.equals(posiciones.get(i))) {
					return false;
				}
			}
			return true;
		}catch(NullPointerException | ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}



	/** Devuelve la posición en la que se está pintando el pájaro
	 * @return la posición en la que se está pintando el pájaro
	 */
	public Point getPosicionPintado() {
		if(posicionPintado == null) {
			return getLocation();
		}else {
			return posicionPintado;
		}
	}

	@Override
	public String toString() {
		return String.format("Pájaro(%d, %d)", x, y);
	}
}