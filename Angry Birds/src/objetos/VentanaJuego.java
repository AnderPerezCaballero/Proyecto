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
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class VentanaJuego {

	private JFrame ventana;         // Ventana que se visualiza
	private boolean cerrada;        // Indica el estado de la ventana
	private JPanel panel;           // Panel principal
	private BufferedImage buffer;   // Buffer gráfico de la ventana
	private Graphics2D graphics;    // Objeto gráfico sobre el que dibujar (del buffer)
	private boolean ratonPresionado;  // Información sobre si el ratón está siendo actualmente pulsado
	private double escalaDibujo = 1.0;               // Escala de dibujado (1=1 píxeles por defecto)

	private Object lock = new Object();  // Tema de sincronización de hilos para que el programador usuario no necesite usarlos

	/** Construye una nueva ventana de juego que se inicia en pantalla completa
	 */
	public VentanaJuego() {
		this("Angry Birds");
	}

	/** Construye una nueva ventana de juego que se inicia en pantalla completa
	 * @param titulo	Título de la ventana
	 */
	@SuppressWarnings("serial")
	public VentanaJuego(String titulo) {
		cerrada = false;
		ventana = new JFrame(titulo);
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		buffer = new BufferedImage(2000, 1500, BufferedImage.TYPE_INT_ARGB);
		graphics = buffer.createGraphics();
		panel = new JPanel() {
			{
				setLayout(new BorderLayout());
			}
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				((Graphics2D)g).drawImage(buffer, null, 0, 0);
			}
		};
		panel.setLayout(null);  // Layout nulo para cuando saquemos componentes encima del dibujo
		ventana.getContentPane().add(panel, BorderLayout.CENTER);
		ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
		ventana.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cerrada = true;
			}
		});
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				synchronized (lock) {
					ratonPresionado = false;
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				synchronized (lock) {
					ratonPresionado = true;
				}
			}
		});
		panel.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				synchronized (lock) {
					ratonPresionado = true;
				}
			}
		});
		Runnable r = new Runnable() {
			@Override
			public void run() {
				ventana.setVisible(true);
			}
		};
		if (SwingUtilities.isEventDispatchThread()) {
			r.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(r); //Si no se puede hacer visible la ventana, espera a que se pueda. Si sale un error, se saca por pantalla
			} catch (InvocationTargetException | InterruptedException e1) {
				JOptionPane.showMessageDialog(ventana, "No se ha podido inicializar la ventana", "Error", JOptionPane.WARNING_MESSAGE);
				e1.printStackTrace();
			}
		}
	}

	/** Cierra la ventana (también ocurre cuando se pulsa el icono de cierre)
	 */
	public void fin() {
		if (!cerrada) {
			ventana.dispose();
		}
		cerrada = true;
	}

	/** Convierte x de coordenadas propuestas a coordenadas visuales (con zoom y desplazamiento)
	 * 
	 */
	private double calcX(double x) {
		return x * escalaDibujo;
	}

	/** Convierte y de coordenadas propuestas a coordenadas visuales (con zoom y desplazamiento)
	 * 
	 */
	private double calcY(double y) {
		return y * escalaDibujo;
	}

	/** Dibuja un rectángulo en la ventana
	 * @param rectangulo	Rectángulo a dibujar
	 * @param grosor	Grueso de la línea del rectángulo (en píxels)
	 * @param color  	Color del rectángulo
	 */
	public void dibujaRect(Rectangle rectangulo, float grosor, Color color) {
		dibujaRect(rectangulo.getX(), rectangulo.getY(), rectangulo.getX()+rectangulo.getWidth(), rectangulo.getY()+rectangulo.getHeight(), grosor, color);
	}

	/** Dibuja un rectángulo en la ventana
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo (en píxels) 
	 * @param altura	Altura del rectángulo (en píxels)
	 * @param grosor	Grueso del rectángulo (en píxels)
	 * @param color  	Color del rectángulo
	 */
	public void dibujaRect(double x, double y, double anchura, double altura, float grosor, Color color) {
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(grosor));
		graphics.drawRect((int)Math.round(calcX(x)), (int)Math.round(calcY(y))-(int)Math.round(altura*escalaDibujo), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo));
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
	public void dibujaRect(double x, double y, double anchura, double altura, float grosor, Color color, Color colorRell) {
		graphics.setColor(colorRell);
		graphics.setStroke(new BasicStroke(grosor));
		graphics.fillRect((int)Math.round(calcX(x)), (int)Math.round(calcY(y))-(int)Math.round(altura*escalaDibujo), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo));
		graphics.setColor(color);
		graphics.drawRect((int)Math.round(calcX(x)), (int)Math.round(calcY(y))-(int)Math.round(altura*escalaDibujo), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo));
	}


	/** Dibuja un rectángulo azul en la ventana
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo (en píxels) 
	 * @param altura	Altura del rectángulo (en píxels)
	 * @param grosor	Grueso del rectángulo (en píxels)
	 */
	public void dibujaRect(double x, double y, double anchura, double altura, float grosor) {
		dibujaRect(x, y, anchura, altura, grosor, Color.blue);
	}

	/** Dibuja un círculo relleno en la ventana
	 * @param x	Coordenada x del centro del círculo
	 * @param y	Coordenada y del centro del círculo
	 * @param radio	Radio del círculo (en píxels) 
	 * @param grosor	Grueso del círculo (en píxels)
	 * @param color  	Color del círculo
	 * @param colorRelleno  	Color de relleno del círculo
	 */
	public void dibujaCirculo(double x, double y, double radio, float grosor, Color color, Color colorRelleno) {
		graphics.setStroke(new BasicStroke(grosor));
		graphics.setColor(colorRelleno);
		graphics.fillOval((int)Math.round(calcX(x)-radio*escalaDibujo), (int)Math.round(calcY(y)-radio*escalaDibujo), (int)Math.round(radio*2*escalaDibujo), (int)Math.round(radio*2*escalaDibujo));
		graphics.setColor(color);
		graphics.drawOval((int)Math.round(calcX(x)-radio*escalaDibujo), (int)Math.round(calcY(y)-radio*escalaDibujo), (int)Math.round(radio*2*escalaDibujo), (int)Math.round(radio*2*escalaDibujo));
	}

	/** Dibuja un círculo en la ventana
	 * @param x	Coordenada x del centro del círculo
	 * @param y	Coordenada y del centro del círculo
	 * @param radio	Radio del círculo (en píxels) 
	 * @param grosor	Grueso del círculo (en píxels)
	 * @param color  	Color del círculo
	 */
	public void dibujaCirculo(double x, double y, double radio, float grosor, Color color) {
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(grosor));
		graphics.drawOval((int)Math.round(calcX(x)-radio*escalaDibujo), (int)Math.round(calcY(y)-radio*escalaDibujo), (int)Math.round(radio*2*escalaDibujo), (int)Math.round(radio*2*escalaDibujo));
	}

	/** Dibuja un círculo azul en la ventana
	 * @param x	Coordenada x del centro del círculo
	 * @param y	Coordenada y del centro del círculo
	 * @param radio	Radio del círculo (en píxels) 
	 * @param grosor	Grueso del círculo (en píxels)
	 */
	public void dibujaCirculo(double x, double y, double radio, float grosor) {
		dibujaCirculo(x, y, radio, grosor, Color.blue);
	}

	/** Dibuja una línea en la ventana
	 * @param linea	a dibujar
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 */
	public void dibujaLinea(Line2D linea, float grosor, Color color) {
		dibujaLinea(linea.getX1(), linea.getY1(), linea.getX2(), linea.getY2(), grosor, color);
	}

	/** Dibuja una línea en la ventana
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea 
	 * @param y2	Coordenada y del segundo punto de la línea
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 */
	public void dibujaLinea(double x, double y, double x2, double y2, float grosor, Color color) {
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(grosor));
		graphics.drawLine((int)Math.round(calcX(x)), (int)Math.round(calcY(y)), (int)Math.round(calcX(x2)), (int)Math.round(calcY(y2)));
	}

	/** Dibuja una línea azul en la ventana
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea 
	 * @param y2	Coordenada y del segundo punto de la línea
	 * @param grosor	Grueso de la línea (en píxels)
	 */
	public void dibujaLinea(double x, double y, double x2, double y2, float grosor) {
		dibujaLinea(x, y, x2, y2, grosor, Color.blue);
	}

	/** Dibuja una flecha en la ventana
	 * @param linea	a dibujar (el segundo punto es la punta de la flecha)
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 */
	public void dibujaFlecha(Line2D linea, float grosor, Color color) {
		dibujaFlecha(linea.getX1(), linea.getY1(), linea.getX2(), linea.getY2(), grosor, color);
	}

	/** Dibuja una flecha en la ventana
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea (el de la flecha)
	 * @param y2	Coordenada y del segundo punto de la línea (el de la flecha)
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 */
	public void dibujaFlecha(double x, double y, double x2, double y2, float grosor, Color color) {
		dibujaFlecha(x, y, x2, y2, grosor, color, 10);
	}

	/** Dibuja una flecha en la ventana
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea (el de la flecha)
	 * @param y2	Coordenada y del segundo punto de la línea (el de la flecha)
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 * @param largoFl	Pixels de largo de la flecha
	 */
	public void dibujaFlecha(double x, double y, double x2, double y2, float grosor, Color color, int largoFl) {
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(grosor));
		graphics.drawLine((int)Math.round(calcX(x)), (int)Math.round(calcY(y)), (int)Math.round(calcX(x2)), (int)Math.round(calcY(y2)));
		double angulo = Math.atan2(y2-y, x2-x) + Math.PI;
		double angulo1 = angulo - Math.PI / 10;  // La flecha se forma rotando 1/10 de Pi hacia los dos lados
		double angulo2 = angulo + Math.PI / 10;
		graphics.drawLine((int)Math.round(calcX(x2)), (int)Math.round(calcY(y2)),
				(int)Math.round(calcX(x2)+largoFl*Math.cos(angulo1)), (int)Math.round(calcY(y2)+largoFl*Math.sin(angulo1)));
		graphics.drawLine((int)Math.round(calcX(x2)), (int)Math.round(calcY(y2)), 
				(int)Math.round(calcX(x2)+largoFl*Math.cos(angulo2)), (int)Math.round(calcY(y2)+largoFl*Math.sin(angulo2)));
	}

	/** Dibuja un texto en la ventana
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param texto	Texto a dibujar 
	 * @param font	Tipo de letra con el que dibujar el texto
	 * @param color	Color del texto
	 */
	public void dibujaTexto(double x, double y, String texto, Font font, Color color) {
		graphics.setColor(color);
		graphics.setFont(font);
		graphics.drawString(texto, (int)Math.round(calcX(x)), (int)Math.round(calcY(y)));
	}

	/** Dibuja un texto en la ventana, centrado en un rectángulo dado
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo
	 * @param altura	Altura del rectángulo
	 * @param texto	Texto a dibujar 
	 * @param font	Tipo de letra con el que dibujar el texto
	 * @param color	Color del texto
	 */
	public void dibujaTextoCentrado(double x, double y, double anchura, double altura, String texto, Font font, Color color) {
		Rectangle2D rect = graphics.getFontMetrics(font).getStringBounds(texto, graphics);  // Dimensiones del texto que se va a pintar
		graphics.setColor(color);
		graphics.setFont(font);
		double xCalc = calcX(x) + anchura/2.0*escalaDibujo - rect.getWidth()/2.0;
		double yCalc = calcY(y) + altura*escalaDibujo - graphics.getFontMetrics(font).getDescent() - (altura*escalaDibujo - rect.getHeight())/2.0;
		graphics.drawString(texto, (float)calcX(xCalc), (float)calcY(yCalc));
	}

	/** Dibuja un texto en la ventana, centrado en un rectángulo dado
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo
	 * @param altura	Altura del rectángulo
	 * @param texto	Texto a dibujar 
	 * @param font	Tipo de letra con el que dibujar el texto
	 * @param color	Color del texto
	 * @param ajustaSiMayor	true si se disminuye el texto hasta que quepa (si no cabe), false se dibuja con el tamaño indicado.
	 */
	public void dibujaTextoCentrado(double x, double y, double anchura, double altura, String texto, Font font, Color color, boolean ajustaSiMayor) {
		Rectangle2D rect = graphics.getFontMetrics(font).getStringBounds(texto, graphics);  // Dimensiones del texto que se va a pintar
		while (rect.getWidth() > anchura*escalaDibujo || rect.getHeight() > altura*escalaDibujo) {
			font = new Font(font.getName(), font.getStyle(), font.getSize() - 1);
			rect = graphics.getFontMetrics(font).getStringBounds(texto, graphics);  // Dimensiones del texto que se va a pintar
		}
		graphics.setColor(color);
		graphics.setFont(font);
		double xCalc = calcX(x) + anchura/2.0*escalaDibujo - rect.getWidth()/2.0;
		double yCalc = calcY(y) + (altura*escalaDibujo - graphics.getFontMetrics(font).getDescent() - (altura*escalaDibujo - rect.getHeight())/2.0) + (rect.getHeight()/2);
		graphics.drawString(texto, (float)xCalc, (float)yCalc);
	}

	/** Devuelve el objeto de gráfico sobre el que pintar, correspondiente al 
	 * panel principal de la ventana. Después de actualizar graphics hay que llamar a {@link #repaint()}
	 * si se quiere que se visualice en pantalla
	 * @return	Objeto gráfico principal de la ventana
	 */
	public Graphics2D getGraphics() {
		return graphics;
	}

	/** Repinta la ventana. En caso de que el dibujado inmediato esté desactivado,
	 * es imprescindible llamar a este método para que la ventana gráfica se refresque.
	 */
	public void repaint() {
		panel.repaint();
		graphics.drawImage(Imagenes.getImageIcon("/imgs/FondoNivel.jpg").getImage(), 0, 0, ventana.getWidth(), ventana.getHeight(), ventana);
	}

	/** Carga una imagen de un fichero gráfico y la dibuja en la ventana. Si la imagen no puede cargarse, no se dibuja nada.
	 * El recurso gráfico se busca en el paquete de esta clase o en la clase llamadora.
	 * El recurso gráfico se carga en memoria, de modo que al volver a dibujar la misma imagen, no se vuelve a cargar ya de fichero
	 * @param recursoGrafico	Nombre del fichero (path absoluto desde la carpeta raíz de clases del proyecto o relativo desde este paquete)  (p. ej. "img/prueba.png")
	 * @param centroX	Coordenada x de la ventana donde colocar el centro de la imagen 
	 * @param centroY	Coordenada y de la ventana donde colocar el centro de la imagen
	 * @param anchuraDibujo	Pixels de anchura con los que dibujar la imagen (se escala de acuerdo a su tamaño)
	 * @param alturaDibujo	Pixels de altura con los que dibujar la imagen (se escala de acuerdo a su tamaño)
	 * @param zoom	Zoom a aplicar (mayor que 1 aumenta la imagen, menor que 1 y mayor que 0 la disminuye)
	 * @param radsRotacion	Rotación en radianes
	 * @param opacity	Opacidad (0.0f = totalmente transparente, 1.0f = totalmente opaca)
	 */
	public void dibujaImagen(String recursoGrafico, double centroX, double centroY, 
			int anchuraDibujo, int alturaDibujo, double zoom, double radsRotacion, float opacity) {
		
		ImageIcon imageIcon = Imagenes.getImageIcon(recursoGrafico); if (imageIcon==null) return;
		
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR); // Configuración para mejor calidad del gráfico escalado
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
		graphics.translate(calcX(centroX)-anchuraDibujo/2*escalaDibujo, calcY(centroY)-alturaDibujo/2*escalaDibujo);
		graphics.rotate(radsRotacion, anchuraDibujo/2*escalaDibujo, alturaDibujo/2*escalaDibujo);  // Incorporar al gráfico la rotación definida
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity)); // Incorporar la transparencia definida
		
		int anchoDibujado = (int)Math.round(anchuraDibujo*zoom*escalaDibujo);  // Calcular las coordenadas de dibujado con el zoom, siempre centrado en el label
		int altoDibujado = (int)Math.round(alturaDibujo*zoom*escalaDibujo);
		int difAncho = (int) ((anchuraDibujo* escalaDibujo - anchoDibujado) / 2);  
		int difAlto = (int) ((alturaDibujo* escalaDibujo - altoDibujado) / 2);     
		
		graphics.drawImage(imageIcon.getImage(), difAncho, difAlto, anchoDibujado, altoDibujado, null);  // Dibujar la imagen con el tamaño calculado tras aplicar el zoom
		graphics.setTransform(new AffineTransform()); 
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));  // Restaurar graphics (pintado sin transparencia)
	}

	/** Carga una imagen de un fichero gráfico y la dibuja en la ventana. Si la imagen no puede cargarse, no se dibuja nada.
	 * El recurso gráfico se busca en el paquete de esta clase o en la clase llamadora.
	 * El recurso gráfico se carga en memoria, de modo que al volver a dibujar la misma imagen, no se vuelve a cargar ya de fichero
	 * @param recursoGrafico	Nombre del fichero (path absoluto desde la carpeta raíz de clases del proyecto o relativo desde este paquete)  (p. ej. "img/prueba.png")
	 * @param centroX	Coordenada x de la ventana donde colocar el centro de la imagen 
	 * @param centroY	Coordenada y de la ventana donde colocar el centro de la imagen
	 * @param zoom	Zoom a aplicar (mayor que 1 aumenta la imagen, menor que 1 y mayor que 0 la disminuye)
	 * @param radsRotacion	Rotación en radianes
	 * @param opacity	Opacidad (0.0f = totalmente transparente, 1.0f = totalmente opaca)
	 */
	public void dibujaImagen(String recursoGrafico, double centroX, double centroY, 
			double zoom, double radsRotacion, float opacity) {
		ImageIcon imageIcon = Imagenes.getImageIcon(recursoGrafico); if (imageIcon==null) return;
		dibujaImagen(recursoGrafico, centroX, centroY, imageIcon.getIconWidth(), imageIcon.getIconHeight(), zoom, radsRotacion, opacity);
	}

	/** Devuelve el objeto ventana (JFrame) correspondiente a la ventana de juego
	 * @return	Objeto JFrame de la ventana
	 */
	public JFrame getJFrame() {
		return ventana;
	}

	/** Consultor de estado de visibilidad de la ventana
	 * @return	false si sigue activa, true si ya se ha cerrado
	 */
	public boolean estaCerrada() {
		return cerrada;
	}

	/** Indica si el ratón está actualmente pulsado
	 * @return	True si lo está, false si no
	 */
	public boolean isRatonPulsado() {
		synchronized (lock) {
			return ratonPresionado;
		}
	}

	/** Devuelve el panel de dibujo de la ventana
	 * @return	Panel donde se dibuja en la ventana
	 */
	public JPanel getPanelDibujo() {
		return panel;
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
	
	/** Modifica la escala de dibujado de la ventana
	 * @param escala	Nueva escala (1.0 escala 1=1). Por ejemplo con 0.5 el dibujo se escala a la mitad de tamaño, con 2.0 se escala al doble de tamaño
	 */
	public void setEscalaDibujo(double escala) {
		this.escalaDibujo = escala;
	}

	/** Devuelve la escala de dibujado de la ventana
	 * @return Escala de dibujado (1.0 escala 1=1)
	 */
	public double getEscalaDibujo() {
		return escalaDibujo;
	}

}
