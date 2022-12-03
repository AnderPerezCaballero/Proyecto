package objetos;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import objetos.pajaros.Pajaro;

public class VentanaJuego {
	private JFrame ventana;         // Ventana que se visualiza
	private boolean cerrada;        // Lógica de cierre (false al inicio)
	private JPanel panel;           // Panel principal
	private JLabel lMens;           // Etiqueta de texto de mensajes en la parte inferior
	private BufferedImage buffer;   // Buffer gráfico de la ventana
	private Graphics2D graphics;    // Objeto gráfico sobre el que dibujar (del buffer)
	private Point pointPressed;     // Coordenada pulsada de ratón (si existe)
	private boolean botonIzquierdo; // Información de si el último botón pulsado es izquierdo o no lo es
	private boolean botonDerecho;   // Información de si el ultimo botón pulsado es derecho o no lo es
	private boolean botonMedio;     // Información de si el ultimo botón pulsado es el del medio o no lo es
	private Point pointMoved;       // Coordenada pasada de ratón (si existe)
	private Point pointMovedPrev;   // Coordenada pasada anterior de ratón (si existe)
	private boolean dibujadoInmediato = true; // Refresco de dibujado en cada orden de dibujado
	private Point offsetInicio = new Point( 0, 0 );  // Offset de inicio de coordenadas ((0,0) por defecto)
	private double escalaDibujo = 1.0;               // Escala de dibujado (1=1 píxeles por defecto)
	private boolean ejeYInvertido = true;            // Eje Y invertido con respecto a la representación matemática clásica (por defecto true -crece hacia abajo-)
	private Color colorFondo = Color.white; // El color de fondo
	private static volatile HashMap<String,ImageIcon> recursosGraficos = new HashMap<>();
	private Lock lock= new Lock() {
		
		@Override
		public void unlock() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean tryLock() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public Condition newCondition() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void lockInterruptibly() throws InterruptedException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void lock() {
			// TODO Auto-generated method stub
			
		}
	};
	
	public void acaba() {
		if (!cerrada) ventana.dispose();
		cerrada = true;
	}
	
	public Graphics2D getGraphics() {
		return this.graphics;
	}
	
	/** Construye una nueva ventana gráfica con fondo blanco y la visualiza en el centro de la pantalla
	 * @param anchura	Anchura en píxels (valor positivo) de la zona de pintado
	 * @param altura	Altura en píxels (valor positivo) de la zona de pintado
	 * @param titulo	Título de la ventana
	 */
	public VentanaJuego( int anchura, int altura, String titulo ) {
		this(anchura, altura, titulo, Color.WHITE );
	}
	
	/** Construye una nueva ventana gráfica y la visualiza en el centro de la pantalla
	 * @param anchura	Anchura en píxels (valor positivo) de la zona de pintado
	 * @param altura	Altura en píxels (valor positivo) de la zona de pintado
	 * @param titulo	Título de la ventana
	 * @param colorDeFondo  El color de fondo de la ventana gráfica
	 */
//	@SuppressWarnings("serial")
	public VentanaJuego(int anchura, int altura, String titulo, Color colorDeFondo) {
		this.colorFondo = colorDeFondo;
		cerrada = false;
		ventana = new JFrame( titulo );
		ventana.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		// ventana.setSize( anchura, altura ); -- se hace en función del tamaño preferido del panel de dibujo
		buffer = new BufferedImage( 2000, 1500, BufferedImage.TYPE_INT_ARGB );
		graphics = buffer.createGraphics();
		graphics.setPaint ( colorFondo );
		graphics.fillRect ( 0, 0, 2000, 1500 );
		panel = new JPanel() {
			{
				setLayout( new BorderLayout() );
			}
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				((Graphics2D)g).drawImage( buffer, null, 0, 0 );
			}
		};
		panel.setPreferredSize( new Dimension( anchura, altura ));
		panel.setLayout( null );  // Layout nulo para cuando saquemos componentes encima del dibujo
		lMens = new JLabel( " " );
		ventana.getContentPane().add( panel, BorderLayout.CENTER );
		ventana.getContentPane().add( lMens, BorderLayout.SOUTH );
		ventana.pack();
		ventana.setLocationRelativeTo( null );
		ventana.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cerrada = true;
			}
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		panel.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				synchronized (lock) {
					pointPressed = null;
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				synchronized (lock) {
					pointPressed = e.getPoint();
					botonIzquierdo = SwingUtilities.isLeftMouseButton(e);
					botonDerecho = SwingUtilities.isRightMouseButton(e);
					botonMedio = SwingUtilities.isMiddleMouseButton(e);
					
				}
			}
		});
		panel.addMouseMotionListener( new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				synchronized (lock) {
					pointMoved = e.getPoint();
				}
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				synchronized (lock) {
					pointPressed = e.getPoint();
					botonIzquierdo = SwingUtilities.isLeftMouseButton(e);
					botonDerecho = SwingUtilities.isRightMouseButton(e);
					botonMedio = SwingUtilities.isMiddleMouseButton(e);
				}
			}
		});
		Runnable r = new Runnable() {
			@Override
			public void run() {
				ventana.setVisible( true );
			}
		};
		if (SwingUtilities.isEventDispatchThread()) {
			r.run();
		} else {
			try {
				SwingUtilities.invokeAndWait( r );
			} catch (InvocationTargetException | InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/** Devuelve el último vector de movimiento del ratón (desde la última vez que se llamó a este mismo método)
	 * @return	Punto relativo a la ventana, null si el ratón no se ha movido nunca
	 */
	public Point getVectorRatonMovido() {
		synchronized (lock) {
			Point ret = new Point( 0, 0 );
			if (pointMovedPrev!=null) {
				ret.setLocation( pointMoved.getX()-pointMovedPrev.getX(), pointMoved.getY()- pointMovedPrev.getY() );
			}
			pointMovedPrev = pointMoved;
			return ret;
		}
	}	
	
	/** Cambia el mensaje de la ventana (línea inferior de mensajes)
	 * @param mensaje	Texto de mensaje
	 */
	public void setMensaje( String mensaje ) {
		if (mensaje==null || mensaje.isEmpty())
			lMens.setText( " " );
		else
			lMens.setText( mensaje );
	}
	
	/** Devuelve el mensaje actual de la ventana (línea inferior de mensajes)
	 * @return	Mensaje actual
	 */
	public String getMensaje() {
		return lMens.getText();
	}
	
	/** Cambia el tipo de letra de la línea inferior de mensajes
	 * @param font	Tipo de letra a utilizar
	 */
	public void setMensajeFont( Font font ) {
		lMens.setFont( font );
	}
	
	/** Devuelve la altura del panel de dibujo de la ventana
	 * @return	Altura del panel principal (última coordenada y) en píxels
	 */
	public int getAltura() {
		return panel.getHeight()-1;
	}
	
	/** Devuelve la altura del panel de dibujo de la ventana
	 * @return	Altura del panel principal (última coordenada y) en unidades de dibujo (aplicando la escala, si la tiene)
	 */
	public double getAlturaConEscala() {
		return panel.getHeight()/escalaDibujo;
	}
	
	/** Devuelve la anchura del panel de dibujo de la ventana
	 * @return	Anchura del panel principal (última coordenada x) en píxels
	 */
	public int getAnchura() {
		return panel.getWidth()-1;
	}
	
	/** Devuelve la anchura del panel de dibujo de la ventana
	 * @return	Anchura del panel principal (última coordenada x) en unidades de dibujo (aplicando la escala, si la tiene)
	 */
	public double getAnchuraConEscala() {
		return panel.getWidth()/escalaDibujo;
	}
	
	/** Borra toda la ventana (pinta de color blanco)
	 */
	public void borra() {
		graphics.setColor( colorFondo );
		graphics.fillRect( 0, 0, panel.getWidth()+2, panel.getHeight()+2 );
		if (dibujadoInmediato) repaint();
	}
	
	/** Dibuja un rectángulo en la ventana
	 * @param rectangulo	Rectángulo a dibujar
	 * @param grosor	Grueso de la línea del rectángulo (en píxels)
	 * @param color  	Color del rectángulo
	 */
	public void dibujaRect( Rectangle rectangulo, float grosor, Color color ) {
		dibujaRect( rectangulo.getX(), rectangulo.getY(), rectangulo.getX()+rectangulo.getWidth(), rectangulo.getY()+rectangulo.getHeight(), grosor, color );
	}
	
	/** Dibuja un rectángulo en la ventana
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo (en píxels) 
	 * @param altura	Altura del rectángulo (en píxels)
	 * @param grosor	Grueso del rectángulo (en píxels)
	 * @param color  	Color del rectángulo
	 */
	public void dibujaRect( double x, double y, double anchura, double altura, float grosor, Color color ) {
		graphics.setColor( color );
		graphics.setStroke( new BasicStroke( grosor ));
		if (ejeYInvertido)
			graphics.drawRect( (int)Math.round(calcX(x)), (int)Math.round(calcY(y)), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo) );
		else
			graphics.drawRect( (int)Math.round(calcX(x)), (int)Math.round(calcY(y))-(int)Math.round(altura*escalaDibujo), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo) );
		if (dibujadoInmediato) repaint();
	}

		// Convierte x de coordenadas propuestas a coordenadas visuales (con zoom y desplazamiento)
		private double calcX( double x ) {
			return x * escalaDibujo - offsetInicio.x * escalaDibujo;
		}
		// Convierte y de coordenadas propuestas a coordenadas visuales (con zoom y desplazamiento)
		private double calcY( double y ) {
			return (ejeYInvertido?1.0:-1.0) * y * escalaDibujo - offsetInicio.y * escalaDibujo;
		}

	/** Dibuja un rectángulo relleno en la ventana
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo (en píxels) 
	 * @param altura	Altura del rectángulo (en píxels)
	 * @param grosor	Grueso del rectángulo (en píxels)
	 * @param color  	Color de la línea del rectángulo
	 * @param colorRell	Color del relleno del rectángulo
	 */
	public void dibujaRect( double x, double y, double anchura, double altura, float grosor, Color color, Color colorRell ) {
		graphics.setColor( colorRell );
		graphics.setStroke( new BasicStroke( grosor ));
		if (ejeYInvertido)
			graphics.fillRect( (int)Math.round(calcX(x)), (int)Math.round(calcY(y)), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo) );
		else
			graphics.fillRect( (int)Math.round(calcX(x)), (int)Math.round(calcY(y))-(int)Math.round(altura*escalaDibujo), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo) );
		graphics.setColor( color );
		if (ejeYInvertido)
			graphics.drawRect( (int)Math.round(calcX(x)), (int)Math.round(calcY(y)), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo) );
		else
			graphics.drawRect( (int)Math.round(calcX(x)), (int)Math.round(calcY(y))-(int)Math.round(altura*escalaDibujo), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo) );
		if (dibujadoInmediato) repaint();
	}
	
	/** Dibuja un rectángulo azul en la ventana
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo (en píxels) 
	 * @param altura	Altura del rectángulo (en píxels)
	 * @param grosor	Grueso del rectángulo (en píxels)
	 */
	public void dibujaRect( double x, double y, double anchura, double altura, float grosor ) {
		dibujaRect( x, y, anchura, altura, grosor, Color.blue );
	}
	
	/** Borra un rectángulo en la ventana
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo (en píxels) 
	 * @param altura	Altura del rectángulo (en píxels)
	 * @param grosor	Grueso del rectángulo (en píxels)
	 */
	public void borraRect( double x, double y, double anchura, double altura, float grosor ) {
		dibujaRect( x, y, anchura, altura, grosor, colorFondo );
	}
	
	/** Dibuja un círculo relleno en la ventana
	 * @param x	Coordenada x del centro del círculo
	 * @param y	Coordenada y del centro del círculo
	 * @param radio	Radio del círculo (en píxels) 
	 * @param grosor	Grueso del círculo (en píxels)
	 * @param color  	Color del círculo
	 * @param colorRelleno  	Color de relleno del círculo
	 */
	public void dibujaCirculo( double x, double y, double radio, float grosor, Color color, Color colorRelleno ) {
		graphics.setStroke( new BasicStroke( grosor ));
		graphics.setColor( colorRelleno );
		graphics.fillOval( (int)Math.round(calcX(x)-radio*escalaDibujo), (int)Math.round(calcY(y)-radio*escalaDibujo), (int)Math.round(radio*2*escalaDibujo), (int)Math.round(radio*2*escalaDibujo) );
		graphics.setColor( color );
		graphics.drawOval( (int)Math.round(calcX(x)-radio*escalaDibujo), (int)Math.round(calcY(y)-radio*escalaDibujo), (int)Math.round(radio*2*escalaDibujo), (int)Math.round(radio*2*escalaDibujo) );
		if (dibujadoInmediato) repaint();
	}
	
	/** Dibuja un círculo en la ventana
	 * @param x	Coordenada x del centro del círculo
	 * @param y	Coordenada y del centro del círculo
	 * @param radio	Radio del círculo (en píxels) 
	 * @param grosor	Grueso del círculo (en píxels)
	 * @param color  	Color del círculo
	 */
	public void dibujaCirculo( double x, double y, double radio, float grosor, Color color ) {
		graphics.setColor( color );
		graphics.setStroke( new BasicStroke( grosor ));
		graphics.drawOval( (int)Math.round((x)-radio*escalaDibujo), (int)Math.round((y)-radio*escalaDibujo), (int)Math.round(radio*2*escalaDibujo), (int)Math.round(radio*2*escalaDibujo) );
		//if (dibujadoInmediato) repaint();
	}
	
	public void dibujaImagen( String recursoGrafico, double centroX, double centroY, 
			double zoom, double radsRotacion, float opacity ) {
		ImageIcon ii = getRecursoGrafico(recursoGrafico); if (ii==null) return;
		dibujaImagen( recursoGrafico, centroX, centroY, ii.getIconWidth(), ii.getIconHeight(), zoom, radsRotacion, opacity);
	}
	public double getEscalaDibujo() {
		return escalaDibujo;
	}
	public ImageIcon getRecursoGrafico( String recursoGrafico ) {
		ImageIcon ii = recursosGraficos.get( recursoGrafico );
		if (ii==null) {
			try {
				ii = new ImageIcon( VentanaJuego.class.getResource( recursoGrafico ));
				recursosGraficos.put( recursoGrafico, ii );
			} catch (NullPointerException e) {  // Mirar si está en la clase llamadora en lugar de en la ventana gráfica
				StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
				for (int i=1; i<stElements.length; i++) {
					StackTraceElement ste = stElements[i];
					if ( !ste.getClassName().endsWith("VentanaGrafica") ) {  // Busca la clase llamadora a VentanaGrafica y busca ahí el recurso
						try {
							Class<?> c = Class.forName( ste.getClassName() );
							URL url = c.getResource( recursoGrafico );
							if (url==null) return null;
							ii = new ImageIcon( url );
							recursosGraficos.put( recursoGrafico, ii );
							return ii;
						} catch (ClassNotFoundException e1) {
							return null;
						}
					}
				}
				return null;
			}			
		}
		return ii;
	}
	
	public void dibujaImagen( String recursoGrafico, double centroX, double centroY, 
			int anchuraDibujo, int alturaDibujo, double zoom, double radsRotacion, float opacity ) {
		ImageIcon ii = getRecursoGrafico(recursoGrafico); if (ii==null) return;
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR); // Configuración para mejor calidad del gráfico escalado
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
		graphics.translate( calcX(centroX)-anchuraDibujo/2*escalaDibujo, calcY(centroY)-alturaDibujo/2*escalaDibujo );
		graphics.rotate( radsRotacion, anchuraDibujo/2*escalaDibujo, alturaDibujo/2*escalaDibujo );  // Incorporar al gráfico la rotación definida
		graphics.setComposite(AlphaComposite.getInstance( AlphaComposite.SRC_OVER, opacity ) ); // Incorporar la transparencia definida
        int anchoDibujado = (int)Math.round(anchuraDibujo*zoom*escalaDibujo);  // Calcular las coordenadas de dibujado con el zoom, siempre centrado en el label
        int altoDibujado = (int)Math.round(alturaDibujo*zoom*escalaDibujo);
        int difAncho = (int) ((anchuraDibujo* escalaDibujo - anchoDibujado) / 2);  // Offset x para centrar
        int difAlto = (int) ((alturaDibujo* escalaDibujo - altoDibujado) / 2);     // Offset y para centrar
        // graphics.drawRect( difAncho, difAlto, anchoDibujado, altoDibujado );
        graphics.drawImage( ii.getImage(), difAncho, difAlto, anchoDibujado, altoDibujado, null);  // Dibujar la imagen con el tamaño calculado tras aplicar el zoom
		graphics.setTransform( new AffineTransform() );  // Restaurar graphics  (sin rotación ni traslación)
		graphics.setComposite(AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f ));  // Restaurar graphics (pintado sin transparencia)
		if (dibujadoInmediato) repaint();
	}
	public void repaint() {
		panel.repaint();
		// panel.paintImmediately( 0, 0, panel.getWidth(), panel.getHeight() );
	}

	public void pintadoInmediato() {
		panel.paintImmediately( 0, 0, panel.getWidth(), panel.getHeight() );
		lMens.paintImmediately( 0, 0, lMens.getWidth(), lMens.getHeight() );
	}

	public void setDibujadoInmediato( boolean dibujadoInmediato ) {
		this.dibujadoInmediato = dibujadoInmediato;
	}
	public void espera( long milis ) {
		try {
			Thread.sleep( milis );
		} catch (InterruptedException e) {
		}
	}

	/** Informa del modo de dibujado (por defecto el modo es de dibujado inmediato = true)
	 * @return true si cada orden de dibujado inmediatamente pinta la ventana. false si se
	 * van acumulando las órdenes y se pinta la ventana solo al hacer un {@link #repaint()}.
	 */
	public boolean isDibujadoInmediato() {
		return dibujadoInmediato;
	}
	public boolean estaCerrada() {
		return cerrada;
	}
	public Point getRatonPulsado() {
		synchronized (lock) {
			return pointPressed;
		}
	}
	public Point getRatonMovido() {
		synchronized (lock) {
			return pointMoved;
		}
	}
	
	
}
