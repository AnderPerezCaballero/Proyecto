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
	private static final int MILISEGUNDOSENTREFRAMES = 10; // Msgs. de duración de cada fotograma (aprox. = espera entre fotograma y siguiente)
	private static final double GRAVEDADY = 9.81;
	private static final double GRAVEDADX = 0;
	
	private static final Point POSICIONINICIALPAJARO = new Point(225, 785);
	private static final Color COLORTIRAPAJAROS = new Color(48, 22, 8);
	private static final int YPOSICIONSUELO = 909;
	private static final int XPOSICIONTIRAPAJAROS = 221;
	private static final int YPOSICIONTIRAPAJAROS = 840;
	
	private static VentanaJuego ventanaJuego;
	private static JFrame ventana;

	private static long milisAbierta;
	private static Nivel nivel;
	
	private static Point posicionRaton;
	
	private static Pajaro pajaro;

	

	/** Inicia el juego
	 * @param lvl Nivel a iniciar
	 */
	public static void init(int lvl) {
		ventanaJuego = new VentanaJuego(String.format("Nivel %d", lvl));
		nivel = new Nivel(lvl);
		pajaro = new Pajaro(POSICIONINICIALPAJARO);
		milisAbierta = System.currentTimeMillis();
		buclePrincipal(lvl);
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
						new VentanaOpcionesJuego(nivel.getPajarosDeInicio(),  nivel.getPajarosDisponibles() - 1, milisAbierta, !nivel.hayCerdos(), lvl, ventana);
						ventana.setEnabled(false);
						break;
					}
				
					pajaro = new Pajaro(POSICIONINICIALPAJARO);	
								
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
	








}