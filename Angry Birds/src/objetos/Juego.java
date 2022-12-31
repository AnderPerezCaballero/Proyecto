package objetos;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import gui.componentes.Imagenes;
import gui.juego.VentanaJuego;
import gui.juego.VentanaOpcionesJuego;
import objetos.pajaros.Pajaro;


public class Juego {
	private static final int MILISEGUNDOSENTREFRAMES = 10; // Msgs. de duración de cada fotograma (aprox. = espera entre fotograma y siguiente)
	//	private static final double DIST_MARGEN_CASILLA = 0.001;  // Margen de error en píxels de comparación de centro de casilla en movimientos finos (por debajo de esto se entiende que está en el mismo píxel)
	//	private static final Color[] COLORESPOSIBLES =  { Color.RED, Color.GREEN, Color.BLUE };
	//	private static final int TAMANYO_Estructura = 50;  // Tamaño estándar de la Estructura
	private static final double GRAVEDADY = 9.81;
	private static final double GRAVEDADX = 0;
	private static VentanaJuego ventanaJuego;
	private static JFrame ventana;
	//	private static GrupoOP grupoPajaros;
	//	private static GrupoOP grupoEstructuras;
	//	private static GrupoOP grupoEnemigos;
	//	private static GrupoOP grupoOP;

	private static long milisAbierta;

	private static Nivel nivel;

	private static Point posicionRaton;

	private static Pajaro pajaro;

	private static final Point POSICIONINICIALPAJARO = new Point(225, 785);
	private static final Color COLORTIRAPAJAROS = new Color(48, 22, 8);
	private static final int YPOSICIONSUELO = 909;
	private static final int XPOSICIONTIRAPAJAROS = 221;
	private static final int YPOSICIONTIRAPAJAROS = 840;
	

	public static void init(int lvl) {
		ventanaJuego = new VentanaJuego(String.format("Nivel %d", lvl));
		nivel = new Nivel(lvl);
		pajaro = new Pajaro(POSICIONINICIALPAJARO);
		milisAbierta = System.currentTimeMillis();
		buclePrincipal(lvl);
	}

	public static void buclePrincipal(int lvl) {
		while (!ventanaJuego.estaCerrada()) {
			//Se actualiza la situación de la ventana
			ventana = ventanaJuego.getJFrame();

			//Coordenadas del ratón dependiendo de si está en pantalla completa o no
			posicionRaton = MouseInfo.getPointerInfo().getLocation();

			//Los 25 pixels de el encabezado
			posicionRaton.translate(0, -25);
			
			if(!pajaro.isLanzado()) {

				//Identificar si el pájaro está siendo seleccionado o no
				if(ventanaJuego.isRatonPulsado() && posicionRaton.distance(pajaro.getLocation()) < Pajaro.getRadio()){
					pajaro.setSeleccionado(true);				
				}

				//Mover el pájaro si esta seleccionado a corde con la posición del ratón
				if(ventanaJuego.isRatonPulsado() && pajaro.isSeleccionado()) {
					pajaro.setLocation(posicionRaton);

					//Choques
					if(pajaro.choqueConSuelo()) {
						pajaro.setY(YPOSICIONSUELO - Pajaro.getRadio());
					}
					if(pajaro.choqueConLimiteVertical()) {
						pajaro.setX(Pajaro.getRadio());
					}

					//Animación de catapulta
					ventanaJuego.dibujaLinea(215, 795, pajaro.getX(), pajaro.getY(), 4, COLORTIRAPAJAROS);
					pajaro.dibuja(ventanaJuego);
					ventanaJuego.dibujaLinea(240, 790, pajaro.getX(), pajaro.getY(), 4, COLORTIRAPAJAROS);

					pajaro.dibujarVectorLanzamiento(ventanaJuego, pajaro.getLocation(), POSICIONINICIALPAJARO);
				}else {

					//Lanzamiento del pájaro
					if(pajaro.isSeleccionado() && !ventanaJuego.isRatonPulsado()) {
						if(pajaro.getLocation().distance(POSICIONINICIALPAJARO) > 30) {
							pajaro.lanzar(pajaro.getLocation(), POSICIONINICIALPAJARO);

							//Movimiento del pájaro
							pajaro.move(MILISEGUNDOSENTREFRAMES, GRAVEDADX, GRAVEDADY, nivel);
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
				if(pajaro.isQuieto() || !ventana.contains(pajaro.getPosicionPintado(POSICIONINICIALPAJARO))){
					pajaro.cancelarMovimiento();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!nivel.reducirPajarosDisponibles() || !nivel.hayCerdos()) {
						milisAbierta = System.currentTimeMillis() - milisAbierta;
						new VentanaOpcionesJuego(nivel.getPajarosDeInicio() - nivel.getPajarosDisponibles() + 1, milisAbierta, !nivel.hayCerdos(), lvl);
						ventana.setEnabled(false);
						break;
					}
				
					pajaro = new Pajaro(POSICIONINICIALPAJARO);	
								
				}
			}
			//			grupoEnemigos.dibuja(ventanaJuego);




			//			grupoPajaros.dibuja(ventanaJuego);
			//						if(pajaro.getLocation().distance(new Point(223, 785)) < 30) {
			//					sy	}



			//			System.out.println(pajaro.getLocation().equals(posicionRaton));

			//			grupoEnemigos.dibuja(ventanaJuego);
			//			grupoEstructuras.dibuja(ventanaJuego);


			try {
				Thread.sleep(MILISEGUNDOSENTREFRAMES);
			}catch(InterruptedException e) {}


			ventanaJuego.repaint();
		}
		pajaro.cancelarMovimiento();
		System.err.println("JUEGO TERMINADO");
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

	/** Devuelve la coordenada Y del suelo del juego (En pantalla completa)
	 * @return Int que representa el pixel de la coordenada Y del suelo del juego
	 */
	public static int getYSuelo() {
		return YPOSICIONSUELO;
	}

	/** Devuelve la coordenada X del centro del tirapajaros/catapulta
	 * @return Int que representa el pixel de la coordenada X del tirapajaros
	 */
	public static int getXTiraPajaros() {
		return XPOSICIONTIRAPAJAROS;
	}
	
	public static void main(String[] args) {
		init(1);
	}








}