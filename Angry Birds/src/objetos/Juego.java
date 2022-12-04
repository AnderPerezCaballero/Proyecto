package objetos;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.JFrame;

import gui.componentes.Imagenes;
import gui.juego.VentanaJuego;
import objetos.pajaros.Pajaro;


public class Juego {
	private static final long milisEntreFrames = 10;  // Msgs. de duración de cada fotograma (aprox. = espera entre fotograma y siguiente)
	private static final double DIST_MARGEN_CASILLA = 0.001;  // Margen de error en píxels de comparación de centro de casilla en movimientos finos (por debajo de esto se entiende que está en el mismo píxel)
	private static final Color[] COLORESPOSIBLES =  { Color.RED, Color.GREEN, Color.BLUE };
	private static final int TAMANYO_Estructura = 50;  // Tamaño estándar de la Estructura
	private static VentanaJuego ventanaJuego;
	private static JFrame ventana;
	private static GrupoPajaros grupoPajaros;
	private static GrupoOP grupoEstructuras;
	private static GrupoOP grupoEnemigos;
	private static long milisAbierta;
	private Nivel Nivel;
	
	private static Point posicionRaton;
	private static Pajaro pajaro;
	private static final Color COLORTIRAPAJAROS = new Color(48, 22, 8);
	
	public static void init() {
		
		ventanaJuego = new VentanaJuego();
		grupoPajaros = new GrupoPajaros();
		grupoEnemigos= new GrupoOP();
		grupoEstructuras= new GrupoOP((int)Math.random()*20);
//		grupoPajaros.anyadeObjetoPrimitivo();
		for (int i=0;i<3;i++) {
			grupoEnemigos.anyadeObjetoPrimitivo(new Enemigo(1000+100*i,700,20));
		}
		
		pajaro = new Pajaro(150, 515, null);
		buclePrincipal();
	}
	
	public static void buclePrincipal() {
		while (!ventanaJuego.estaCerrada()) {
			
			//Se actualiza la situación de la ventana
			ventana = ventanaJuego.getJFrame();
			
			//Coordenadas del ratón dependiendo de si está en pantalla completa o no
			if(ventana.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
				posicionRaton = MouseInfo.getPointerInfo().getLocation();
				
				//Los 25 pixels de el encabezado
				posicionRaton.translate(0, -25);
				
			}else {
				posicionRaton = new Point(MouseInfo.getPointerInfo().getLocation().x - ventana.getLocation().x, MouseInfo.getPointerInfo().getLocation().y - ventana.getLocation().y);
			}
			
			if(ventanaJuego.isRatonPulsado()){
				pajaro.setLocation(posicionRaton);
				ventanaJuego.dibujaLinea(160, 520, pajaro.getX(), pajaro.getY(), 4, COLORTIRAPAJAROS);
				pajaro.dibuja(ventanaJuego);
				ventanaJuego.dibujaLinea(140, 525, pajaro.getX(), pajaro.getY(), 4, COLORTIRAPAJAROS);
				
//				System.out.println(posicionRaton.distance(new Point(pajaro.getX() + pajaro.getRadio(),  pajaro.getY() + pajaro.getRadio())));
//				System.out.println(posicionRaton.distance(new Point(pajaro.getX() + pajaro.getRadio(),  posicionRaton.y)));
//				System.out.println(posicionRaton.distance(new Point(posicionRaton.x,  pajaro.getY() + pajaro.getRadio())));
			}else {
				pajaro.dibuja(ventanaJuego);
			}
			
//			grupoPajaros.dibuja(ventanaJuego);
//			if(pajaro.getLocation().distance(new Point(150, 515)) < pajaro.getRadio()) {
				ventanaJuego.dibujaImagen("/imgs/TiraPajarosDelante.png", 149, 551, 0.47, 0, 1);
//			}
			

			
//			System.out.println(pajaro.getLocation().equals(posicionRaton));
			
			grupoEnemigos.dibuja(ventanaJuego);
			grupoEstructuras.dibuja(ventanaJuego);
			
			
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
	
	
	
	
	
	
	
	
	
	
	
}