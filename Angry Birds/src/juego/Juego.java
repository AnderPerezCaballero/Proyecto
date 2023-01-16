package juego;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.JFrame;
import gui.juego.VentanaJuego;
import gui.juego.VentanaOpcionesJuego;
import juego.objetos.Pajaro;
import juego.objetos.nivel.Nivel;


public class Juego {
	
	public static final int YPOSICIONSUELO = 909;
	public static final int XPOSICIONTIRAPAJAROS = 221;
	public static final double GRAVEDADY = 9.81;
	public static final double GRAVEDADX = 0;	
	public static final int MILISEGUNDOSENTREFRAMES = 10; // Msgs. de duración de cada fotograma (aprox. = espera entre fotograma y siguiente)
	public static final Point POSICIONINICIALPAJARO = new Point(225, 785);
	
	private static final Color COLORTIRAPAJAROS = new Color(48, 22, 8);
	private static final int YPOSICIONTIRAPAJAROS = 840;
	private static final int DISTANCIAMAXIMALANZAMIENTO = 175;

	private static VentanaJuego ventanaJuego;
	private static JFrame ventana;

	private static long milisAbierta;
	private static Nivel nivel;

	private static Point posicionRaton;

	private static Pajaro pajaro;



	/** Inicia el juego en el nivel especificado
	 * @param lvl Nivel a iniciar
	 */
	public static void init(int lvl) {
		ventanaJuego = new VentanaJuego(String.format("Nivel %d", lvl));
		nivel = new Nivel(lvl);
		pajaro = new Pajaro();
		milisAbierta = System.currentTimeMillis();

		// Hago que el bucle se inicie desde un hilo para no bloquear el main
		new Thread(() -> {
			buclePrincipal(lvl);
		}).start();
	}


	private static void buclePrincipal(int lvl) {
		while (!ventanaJuego.estaCerrada()) {

			//Se actualiza la situación de la ventana
			ventana = ventanaJuego.getJFrame();

			//Coordenadas del ratón dependiendo de si está en pantalla completa o no
			posicionRaton = MouseInfo.getPointerInfo().getLocation();

			//Los 25 pixels de el encabezado
			posicionRaton.translate(0, -25);

			if(!pajaro.isLanzado()) {

				//Identificar si el pájaro está siendo seleccionado o no
				if(ventanaJuego.isRatonPulsado() && posicionRaton.distance(pajaro.getLocation()) < Pajaro.RADIO){
					pajaro.setSeleccionado(true);				
				}

				//Mover el pájaro si esta seleccionado a corde con la posición del ratón
				if(ventanaJuego.isRatonPulsado() && pajaro.isSeleccionado()) {

					//Dentro de la distancia máxima de lanzamiento
					if(posicionRaton.distance(POSICIONINICIALPAJARO) < DISTANCIAMAXIMALANZAMIENTO) {
						pajaro.setLocation(posicionRaton);

						//Fuera de la distancia máxima
					}else {

						Point centroCirculo = new Point(POSICIONINICIALPAJARO.x, POSICIONINICIALPAJARO.y);
						ventanaJuego.dibujaCirculo(centroCirculo.x, centroCirculo.y, DISTANCIAMAXIMALANZAMIENTO, 1, Color.RED);
						double distancia = POSICIONINICIALPAJARO.distance(posicionRaton);

						/**EXPLICACIÓN DEL CALCULO:
						 * Partimos de la base de que se tiene un círculo, con un radio y coordenadas sabidas, y un punto (la posicion del ratón), 
						 * que se encuentra fuera de este. Entonces, se necesita encontrar un punto, que este en el borde del círculo, y que este contenido
						 * en la línea que une el centro del círculo y el punto que está fuera.
						 * De esta manera, basándome en el teorema de pitágoras y en el hecho de que la distancia entre el punto en el borde del círculo 
						 * y el centro del círculo debe ser igual al radio del círculo, después de simplificar calculos llegamos a la formula que se ve en
						 * la declaración de puntoEnBorde
						 */

						Point puntoEnBorde = new Point(
								(int) Math.round(centroCirculo.x + DISTANCIAMAXIMALANZAMIENTO * (posicionRaton.x - centroCirculo.x) / distancia),
								(int) Math.round(centroCirculo.y + DISTANCIAMAXIMALANZAMIENTO * (posicionRaton.y - centroCirculo.y) / distancia)
								);

						pajaro.setLocation(puntoEnBorde);
					}

					if(pajaro.choqueConSuelo()) {
						pajaro.setY(YPOSICIONSUELO - Pajaro.RADIO);
					}

					//Animación de catapulta
					ventanaJuego.dibujaLinea(215, 795, pajaro.getX(), pajaro.getY(), 4, COLORTIRAPAJAROS);
					pajaro.dibuja(ventanaJuego);
					ventanaJuego.dibujaLinea(240, 790, pajaro.getX(), pajaro.getY(), 4, COLORTIRAPAJAROS);

				}else {

					//Lanzamiento del pájaro
					if(pajaro.isSeleccionado() && !ventanaJuego.isRatonPulsado()) {
						if(pajaro.getLocation().distance(POSICIONINICIALPAJARO) > 30) {
							pajaro.lanzar(pajaro.getLocation(), POSICIONINICIALPAJARO);

							//Movimiento del pájaro
							pajaro.move(nivel);
						}else {
							//Volver a dejar el pájaro en su sitio
							pajaro.setLocation(POSICIONINICIALPAJARO);
						}
					}
					pajaro.setSeleccionado(false);
				}
				dibujado();

			}else {
				dibujado();
				pajaro.eliminarObjetos(nivel);

				//Reiniciar el lanzamiento
				if(pajaro.isQuieto() || !ventana.contains(pajaro.getPosicionPintado())){
					pajaro.cancelarMovimiento();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!nivel.reducirPajarosDisponibles() || !nivel.hayCerdos()) {
						milisAbierta = System.currentTimeMillis() - milisAbierta;
						new VentanaOpcionesJuego(nivel.getPajarosDeInicio(),  nivel.getPajarosDisponibles() - 1, milisAbierta, !nivel.hayCerdos(), lvl, ventana);
						ventana.setEnabled(false);
						break;
					}

					pajaro = new Pajaro();	

				}
			}

			try {
				Thread.sleep(MILISEGUNDOSENTREFRAMES);
			}catch(InterruptedException e) {}

			ventanaJuego.repaint();
		}
		pajaro.cancelarMovimiento();
	}

	/**Dibuja los elementos del juego
	 * 
	 */
	private static void dibujado() {
		nivel.dibujaElementos(ventanaJuego);
		nivel.dibujaPajarosDisponibles(ventanaJuego);
		pajaro.dibuja(ventanaJuego);
		ventanaJuego.dibujaImagen("/imgs/TiraPajarosDelante.png", XPOSICIONTIRAPAJAROS, YPOSICIONTIRAPAJAROS, 0.74, 0, 1);
	}


	public static void main(String[] args) {
		init(4);
	}






}