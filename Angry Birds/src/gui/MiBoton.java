package gui;

import
java.awt.Color;

import
javax.swing.JButton;
import javax.swing.JFrame;

import
java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/** Clase para dibujar botones con formas
 * @author diego
 *
 */
public class MiBoton extends JButton{

	private static final long serialVersionUID = 1L;
	private	boolean redondo;
	private Color colorFondo, colorPresionado;
	private int diamHorizontal, diamVertical;

	/** Crea un nuevo botón circular
	 * @param colorFondo Color de fondo del boton
	 * @param colorPresionado Color del boton al ser presionado
	 */
	public MiBoton(Color colorFondo, Color colorPresionado) {
		super();
		this.redondo = true;
		this.colorFondo = colorFondo;
		this.colorPresionado = colorPresionado;
		setContentAreaFilled(false);
	} 

	/** Crea un botón ractangular con bordes curvos
	 * @param colorFondo
	 * @param colorPresionado
	 * @param diamHorizontal el ancho del arco que se utilizará para redondear las esquinas del rectángulo
	 * @param diamVertical el alto del arco que se utilizará para redondear las esquinas del rectángulo
	 */
	public MiBoton(Color colorFondo, Color colorPresionado, int diamHorizontal, int diamVertical) {
		super();
		this.diamHorizontal = diamHorizontal;
		this.diamVertical = diamVertical;
		this.redondo = false;
		this.colorFondo = colorFondo;
		this.colorPresionado = colorPresionado;
		setContentAreaFilled(false);
	}

	@ Override
	protected void paintComponent(Graphics g) {
		
		//Colores
		if(getModel().isArmed()) {
			g.setColor(colorPresionado);
		} else{
			g.setColor(colorFondo);
		}
		
		//Dependiendo de si se quiere hacer un círculo o un rectángulo con bordes redondeados
		if(redondo) {
			g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
		} else{
			g.fillRoundRect(0, 0, getSize().width- 1, getSize().height - 1, diamHorizontal, diamVertical);
		}
		super.paintComponent(g);
	}

	//Sobreescritura del borde
	@ Override
	protected void
	paintBorder(Graphics g) {
		g.setColor(Color.BLACK);
		if(redondo) {
			g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
		} else{
			g.drawRoundRect(0, 0, getSize().width - 1, getSize().height	- 1, diamHorizontal, diamVertical);
		}
	}
	
	@Override
	public boolean contains(int x, int y) {
		Shape figura;
		if (redondo) {
			figura = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		else{
			figura = new RoundRectangle2D.Double(0, 0,getWidth(),getHeight(), diamHorizontal, diamVertical);
		}
		return(figura.contains(x, y));
	}
}