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
		vent.setDibujadoInmediato( false ); 
//		Nivel= new Nivel(rutaMapa, 0, 0, grupoPajaros);
//		Nivel.dibujaMapa();
		grupoPajaros = new GrupoPajaros();
		grupoEnemigos= new GrupoOP();
		grupoEstructuras= new GrupoOP((int)Math.random()*20);
		for (int i=0;i<3;i++) {
			Pajaro p= new Pajaro((i+1)*100,300,25,Color.red);
			grupoPajaros.anyadeObjetoPrimitivo(p);
			Enemigo e= new Enemigo(1000+100*i,700,20);
			grupoEnemigos.anyadeObjetoPrimitivo(e);
		}
		grupoPajaros.dibuja(vent);
		grupoEstructuras.dibuja(vent);
		grupoEnemigos.dibuja(vent);
		vent.repaint();
		
	}
	
	public static void buclePrincipal() {
		vent.setDibujadoInmediato( false );
		while (!vent.estaCerrada()) {
			// Movimiento
			Point puls=vent.getRatonPulsado();
			if (null!=grupoPajaros.buscaPuntoEnObjetoPrimitivos(puls)) {
				Point destino=vent.getVectorRatonMovido();
				Pajaro pajaro1= (Pajaro)grupoPajaros.buscaPuntoEnObjetoPrimitivos(puls);
				pajaro1.setvX(pajaro1.getX()-destino.x);
				pajaro1.setvY(pajaro1.getY()-destino.y);
				pajaro1.mueve(0);
			}
			if (avanceFot) { // Gestión del movimiento solo si no está en pausa, o solo un fotograma
				// Choque con bordes
				grupoPajaros.compruebaChoqueLimites( vent);
				// Choque entre bolas
				grupoPajaros.compruebaChoqueCon(grupoEnemigos);
				grupoPajaros.compruebaChoqueCon(grupoEstructuras);
			}
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
//	public static void juego() {
//		boolean juegoActivo = true;
//		while (!vent.estaCerrada() && juegoActivo) {  // Mientras siga jugando...
//			Point puls = vent.getRatonPulsado(); // Controlamos la interacción:
//			if (puls!=null) { // No hay interacción - nada que hacer
//				if (grupoPajaros.buscaPuntoEnObjetoPrimitivos(puls) instanceof Pajaro) { // en este caso no hace falta
//					Pajaro pajaro1 = (Pajaro) grupoPajaros.buscaPuntoEnObjetoPrimitivos( puls );
//					if (pajaro1!=null) { // El punto clicado está dentro de la Pajaro: a dibujar 
//						Point origen = new Point( (int)pajaro1.getX(), (int)pajaro1.getY() );
//						Point ultDrag = puls;
//						Point drag = vent.getRatonPulsado();
//						while (drag!=null && drag.x>=0 && drag.x<vent.getAnchura() && drag.y>=0 && drag.y<vent.getAltura() ) {
//							pajaro1.setvX(pajaro1.getX()-drag.x);
//							pajaro1.setvY(pajaro1.getY()-drag.y);
//						}
//					}pajaro1.mueveYDibuja(vent, 10);
//				}
//			
//			Thread hilo = new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					vent.borra();
//					vent.repaint();
//				}
//			});
//			hilo.start();
//			}
//		}
//	}
//}				
//	private static void dibujaTablero() {
//		vent.borra();
//		grupoPajaros.dibuja( vent );
//		// Añadido para animación de las Estructuras
//		grupoEstructuras.dibuja( vent );
//		vent.repaint(); // Añadido para evitar flickering  (con ventana dibujado inmediato = off)
//	}
//	// Devuelve la fila más cercana a la x indicada
//	private static int getFilaCercana( double x ) {
//		return (int)Math.round(x) / ANCHO_CASILLA;
//	}
//	// Devuelve la columna más cercana a la y indicada
//	private static int getColCercana( double y ) {
//		return (int)Math.round(y) / ALTO_CASILLA;
//	}
//	// Devuelve la coordenada x del centro de la casilla de fila indicada
//	private static int xCentroDeCasilla( int fila ) {
//		return fila * ANCHO_CASILLA + (ANCHO_CASILLA/2);
//	}
//	// Devuelve la coordenada y del centro de la casilla de columna indicada
//	private static int yCentroDeCasilla( int col ) {
//		return col * ALTO_CASILLA + (ALTO_CASILLA/2);
//	}
//	// Devuelve la Pajaro que esté en la casilla (fila,columna) indicada, null si no hay ninguna
//	private static Pajaro estaPajaroEnCasilla( int fila, int col ) {
//		for (int i=0; i<grupoPajaros.getNumGrupoOP(); i++) {
//			ObjetoPrimitivo op = grupoPajaros.getObjetoPrimitivo(i);
//			if (Math.abs(op.getX()-xCentroDeCasilla(fila))<DIST_MARGEN_CASILLA && Math.abs(op.getY()-yCentroDeCasilla(col))<DIST_MARGEN_CASILLA) {  // Calculamos cercanía con margen de precisión de doubles
//				return (Pajaro) op;
//			}
//		}
//		return null;
//	}
//	// Recoloca en una animación de 0,5 segundos la Pajaro indicada en la posición x,y indicada
//	private static void recolocar( Pajaro p, double xDestino, double yDestino ) {
//		// medio segundo (500 milisegundos) movimiento a 50 FPS = 25 movimimentos
//		int numMovimientos = (int) (500 / TIEMPO_FOTOGRAMA);
//		double movX = (xDestino - p.getX());
//		double movY = (yDestino - p.getY());
//		for (int i=0; i<numMovimientos; i++) {
//			p.moverYDibujar( vent, movX, movY );
//			p.dibuja( vent );  // Dibuja la Pajaro que se está moviendo por "encima" de todo
//			vent.espera( TIEMPO_FOTOGRAMA );
//		}
//		p.setX((int) xDestino );  // Coloca la Pajaro en su posición final exacta para evitar error de decimales
//		p.setY((int)yDestino );
//	}
//	
	// Lógica de borrado de las Pajaros
//	private static void quitarPajarosSiSeguidas() {
//		Pajaro tableroLogica[][] = new Pajaro[altoTablero][anchoTablero];  // Estructura de las Pajaros
//		boolean tableroAQuitar[][] = new boolean[altoTablero][anchoTablero];  // Marcas de las que hay que borrar
//		for (int i=0; i<grupoPajaros.getNumGrupoOP(); i++) {
//			Pajaro Pajaro = (Pajaro) grupoPajaros.getObjetoPrimitivo( i );
//			int fila = getFilaCercana( Pajaro.getX() );
//			int col = getColCercana( Pajaro.getY() );
//			tableroLogica[fila][col] = Pajaro;
//		}
//		// Busca filas a quitar
//		for (int fila=0; fila<altoTablero; fila++) {
//			int numSeguidas = 0;
//			Color colorAnt = null;
//			for (int col=0; col<anchoTablero; col++) {
//				Color color = null;
//		
//				if (tableroLogica[fila][col]!=null) color = tableroLogica[fila][col].getColor();
//				if (color==null) {
//					quitaEnFilaSiProcede( fila, col-1, numSeguidas, tableroAQuitar );
//					numSeguidas = 1;
//				} else if (color==colorAnt) { // Seguidas!
//					numSeguidas++;
//				} else { // No seguidas
//					quitaEnFilaSiProcede( fila, col-1, numSeguidas, tableroAQuitar );
//					numSeguidas = 1;
//				}
//				colorAnt = color;
//			}
//			quitaEnFilaSiProcede( fila, anchoTablero-1, numSeguidas, tableroAQuitar );
//		}
//		// Busca columnas a quitar
//		for (int col=0; col<anchoTablero; col++) {
//			int numSeguidas = 0;
//			Color colorAnt = null;
//			for (int fila=0; fila<altoTablero; fila++) {
//				Color color = null;
//				if (tableroLogica[fila][col]!=null) color = tableroLogica[fila][col].getColor();
//				if (color==null) {
//					quitaEnColumnaSiProcede( fila-1, col, numSeguidas, tableroAQuitar );
//					numSeguidas = 1;
//				} else if (color==colorAnt) { // Seguidas!
//					numSeguidas++;
//				} else { // No seguidas
//					quitaEnColumnaSiProcede( fila-1, col, numSeguidas, tableroAQuitar );
//					numSeguidas = 1;
//				}
//				colorAnt = color;
//			}
//			quitaEnColumnaSiProcede( altoTablero-1, col, numSeguidas, tableroAQuitar );
//		}
//		// Quita las marcadas en el array de booleanos
//		Pajaro ultimaQuitada = null;
//		int numQuitadas = 0;
//		for (int fila=0; fila<altoTablero; fila++) {
//			for (int col=0; col<anchoTablero; col++) {
//				if (tableroAQuitar[fila][col]) {
//					ultimaQuitada = tableroLogica[fila][col];
//					grupoPajaros.borraObjetoJuego( ultimaQuitada );
//					numQuitadas++;
//				}
//			}
//		}
//		if (numQuitadas>0) {  // Si se han quitado Pajaros, se redibuja el tablero
//			if (numQuitadas>=numParaBonus) {
//				// TODO Calcular la puntuación
//				// Añadimos una Estructura donde esté la última Pajaro quitada, si se han eliminado más de numParaBonus Pajaros
//				Estructura Estructura = new Estructura( ultimaQuitada.getX(), ultimaQuitada.getY(), TAMANYO_Estructura, TAMANYO_Estructura );
//				grupoEstructuras.anyadeObjetoJuego( Estructura );
//			}
//			dibujaTablero(); 
//		}
//	}

//	// Comprueba si hay que quitar en fila, dada la última fila-columna y el número de fichas seguidas que hay
//	private static void quitaEnFilaSiProcede( int fila, int col, int numSeguidas, boolean tableroAQuitar[][] ) {
//		if (numSeguidas>=numSeguidasAQuitar) { // 3 o más seguidas!  A quitar
//			for (int colIni=col-numSeguidas+1; colIni<=col; colIni++)
//				tableroAQuitar[fila][colIni] = true;
//		}
//	}
//	// Comprueba si hay que quitar en columna, dada la última fila-columna y el número de fichas seguidas que hay
//	private static void quitaEnColumnaSiProcede( int fila, int col, int numSeguidas, boolean tableroAQuitar[][] ) {
//		if (numSeguidas>=numSeguidasAQuitar) { // 3 o más seguidas!  A quitar
//			for (int filaIni=fila-numSeguidas+1; filaIni<=fila; filaIni++)
//				tableroAQuitar[filaIni][col] = true;
//		}
//	}



