package objetos;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.JFrame;

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
	
	public static void init() {
		
		ventanaJuego = new VentanaJuego();
		grupoPajaros = new GrupoPajaros();
		grupoEnemigos= new GrupoOP();
		grupoEstructuras= new GrupoOP((int)Math.random()*20);
		for (int i=0;i<3;i++) {
			grupoPajaros.anyadeObjetoPrimitivo(new Pajaro((i+1)*100,300,25,Color.red));
			grupoEnemigos.anyadeObjetoPrimitivo(new Enemigo(1000+100*i,700,20));
		}
		

		buclePrincipal();
	}
	
	public static void buclePrincipal() {
		while (!ventanaJuego.estaCerrada()) {
			
			//Se actualiza la situación de la ventana
			ventana = ventanaJuego.getJFrame();
			
			//Coordenadas del ratón dependiendo de si está en pantalla completa o no
			if(ventana.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
				posicionRaton = MouseInfo.getPointerInfo().getLocation();
			}else {
				posicionRaton = new Point(MouseInfo.getPointerInfo().getLocation().x - ventana.getLocation().x, MouseInfo.getPointerInfo().getLocation().y - ventana.getLocation().y);
			}
			
			grupoPajaros.dibuja(ventanaJuego);
			grupoEnemigos.dibuja(ventanaJuego);
			grupoEstructuras.dibuja(ventanaJuego);
			
			try {
				Thread.sleep(milisEntreFrames);
			}catch(InterruptedException e) {}
			
			ventanaJuego.repaint();
		}
		ventanaJuego.fin();
	}
	
	public static void main(String[] args) {
		init();
	}
	
	
	
	
	
	
	
	
	
	
	
}