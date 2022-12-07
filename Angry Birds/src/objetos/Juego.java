package objetos;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.JFrame;

import gui.componentes.Imagenes;
import gui.juego.VentanaJuego;
import objetos.pajaros.Pajaro;


public class Juego {
	private static final int milisEntreFrames = 10;  // Msgs. de duración de cada fotograma (aprox. = espera entre fotograma y siguiente)
	private static final double DIST_MARGEN_CASILLA = 0.001;  // Margen de error en píxels de comparación de centro de casilla en movimientos finos (por debajo de esto se entiende que está en el mismo píxel)
	private static final Color[] COLORESPOSIBLES =  { Color.RED, Color.GREEN, Color.BLUE };
	private static final int TAMANYO_Estructura = 50;  // Tamaño estándar de la Estructura
	private static VentanaJuego ventanaJuego;
	private static JFrame ventana;
	private static GrupoOP grupoPajaros;
	private static GrupoOP grupoEstructuras;
	private static GrupoOP grupoEnemigos;
	private static long milisAbierta;
	private Nivel Nivel;

	private static Point posicionRaton;

	private static Pajaro pajaro;

	private static final Point POSICIONINICIALPAJARO = new Point(225, 785);
	private static final Color COLORTIRAPAJAROS = new Color(48, 22, 8);
	private static final int YPOSICIONSUELO = 909;

	public static void init() {

		ventanaJuego = new VentanaJuego();
		grupoPajaros = new GrupoOP();
		grupoEnemigos= new GrupoOP();
		grupoEstructuras= new GrupoOP((int)Math.random()*20);
		//		grupoPajaros.anyadeObjetoPrimitivo();
		for (int i=0;i<3;i++) {
			grupoEnemigos.anyadeObjetoPrimitivo(new Enemigo(700,(1000+100*i),60));
		}

		pajaro = new Pajaro(POSICIONINICIALPAJARO, null);
		buclePrincipal();
		
	}

	public static void buclePrincipal() {
		while (!ventanaJuego.estaCerrada()) {

			//Se actualiza la situación de la ventana
			ventana = ventanaJuego.getJFrame();

			//Coordenadas del ratón dependiendo de si está en pantalla completa o no
			posicionRaton = MouseInfo.getPointerInfo().getLocation();

			//Los 25 pixels de el encabezado
			posicionRaton.translate(0, -25);
			
//			System.out.println(posicionRaton);

			if(!pajaro.isLanzado()) {
				
				//Identificar si el pájaro está siendo seleccionado o no
				if(ventanaJuego.isRatonPulsado() && posicionRaton.distance(pajaro.getLocation()) < pajaro.getRadio()){
					pajaro.setSeleccionado(true);				
				}
				
				//Mover el pájaro si esta seleccionado a corde con la posición del ratón
				if(ventanaJuego.isRatonPulsado() && pajaro.isSeleccionado()) {
					pajaro.setLocation(posicionRaton);
					
					//Choques
					if(pajaro.choqueConLimitesHorizontales()) {
						pajaro.setY(YPOSICIONSUELO - pajaro.getRadio());
					}
					if(pajaro.choqueConLimitesVerticales()) {
						pajaro.setX(pajaro.getRadio());
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
						}else {
							//Volver a dejar el pájaro en su sitio
							pajaro.setLocation(POSICIONINICIALPAJARO);
						}
					}
					pajaro.setSeleccionado(false);
				}
				
				
			}else {
				
				//Movimiento del pájaro
				pajaro.move(milisEntreFrames);
				
				//Choques
				if(pajaro.choqueConLimitesHorizontales()) {
					pajaro.setvY(-pajaro.getvY());
				}
				if(pajaro.choqueConLimitesVerticales()) {
					pajaro.setvX(-pajaro.getvX());
				}
				
//				grupoEnemigos.remover(pajaro.choqueConEnemigos(grupoEnemigos));
			}

			pajaro.dibuja(ventanaJuego);
			grupoEnemigos.dibuja(ventanaJuego);



			//			grupoPajaros.dibuja(ventanaJuego);
			//			if(pajaro.getLocation().distance(new Point(150, 515)) < pajaro.getRadio()) {
			ventanaJuego.dibujaImagen("/imgs/TiraPajarosDelante.png", 223, 840, 0.74, 0, 1);
			//			}



			//			System.out.println(pajaro.getLocation().equals(posicionRaton));

			//			grupoEnemigos.dibuja(ventanaJuego);
			//			grupoEstructuras.dibuja(ventanaJuego);


			try {
				Thread.sleep(milisEntreFrames);
			}catch(InterruptedException e) {}


			ventanaJuego.repaint();
		}
		//		ventanaJuego.fin();
	}

	public static void main(String[] args) {
		init();
	}


	
	
	public static int getYSuelo() {
		return YPOSICIONSUELO;
	}









}