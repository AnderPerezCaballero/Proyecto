package objetos;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import objetos.pajaros.Pajaro;


public class BetaJuego {
	private static final int FPS = 50;  // Fotogramas por segundo
	private static final long milisEntreFrames=10;  // Msgs. de duración de cada fotograma (aprox. = espera entre fotograma y siguiente)
	private static final double DIST_MARGEN_CASILLA = 0.001;  // Margen de error en píxels de comparación de centro de casilla en movimientos finos (por debajo de esto se entiende que está en el mismo píxel)
//	private static final int ANCHO_CASILLA = 200;  // Ancho de casilla en píxels. Si no va a cambiar, atributos constantes
//	private static final int ALTO_CASILLA = 150;  // Alto de casilla en píxels.
	private static int altoTablero = 1000; 
	private static int anchoTablero = 1500; 
//	private static final int RADIO_MINIMO = 10;
//	private static final int RADIO_MAXIMO = 50;
	private static final Color[] COLORES_Pajaro =  { Color.RED, Color.GREEN, Color.BLUE };
	private static final int TAMANYO_Estructura = 50;  // Tamaño estándar de la Estructura
	private static VentanaJuego vent;
	private static GrupoPajaros grupoPajaros;
	private static GrupoOP grupoEstructuras;
	private static GrupoOP grupoEnemigos;
	private static long tiempo;
	private Nivel Nivel;
	private static boolean avanceFot;

	
	public void init() {
		vent = new VentanaJuego( anchoTablero, altoTablero, "Juego" );
//		Nivel= new Nivel(rutaMapa, 0, 0, grupoPajaros);
//		Nivel.dibujaMapa();
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
		while (!vent.estaCerrada()) {
			// Movimiento
//			Point puls=vent.getRatonPulsado();
//			if (null!=grupoPajaros.buscaPuntoEnObjetoPrimitivos(puls)) {
//				Point destino=vent.getVectorRatonMovido();
//				Pajaro pajaro1= (Pajaro)grupoPajaros.buscaPuntoEnObjetoPrimitivos(puls);
//				pajaro1.setvX(pajaro1.getX()-destino.x);
//				pajaro1.setvY(pajaro1.getY()-destino.y);
//				pajaro1.mueve(0);
//				// Gestión del movimiento solo si no está en pausa, o solo un fotograma
//				// Choque con bordes
//				pajaro1.choqueConLimitesLaterales(vent);
//				pajaro1.choqueConLimitesVertical(vent);
//				// Choque entre bolas
//				grupoPajaros.compruebaChoqueCon(grupoEnemigos);
//				grupoPajaros.compruebaChoqueCon(grupoEstructuras);
//			}
			avanceFot = false;
			// Dibujado
			vent.borra();
			//vent.dibujaImagen( ", vent.getAnchura()/2, vent.getAltura()/2, vent.getAnchura(), vent.getAltura(), 1.0, 0.0, 1.0f );
			grupoPajaros.dibuja( vent );
			grupoEnemigos.dibuja( vent );
			grupoEstructuras.dibuja(vent);
			vent.repaint();
			vent.espera( milisEntreFrames );
			// Reinicio de bucle
		}
		vent.acaba();
	}
	
}