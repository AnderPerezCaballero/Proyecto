package gui.componentes;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**	Clase para crear un JPanel con una imagen de fondo (la cual se reescala)
 * @author diego
 *
 */
@SuppressWarnings("serial")
public class MiPanel extends JPanel {

	private Image imagen;



	/**Crea un JPanel con una imagen de fondo con el layout especificado
	 * @param nombreImagen ruta de la imagen
	 * @param layout layout del panel
	 */
	public MiPanel(String nombreImagen, LayoutManager layout) {
		if (nombreImagen != null) {
			imagen = new ImageIcon(getClass().getResource(nombreImagen)).getImage();
		}
		this.setLayout(layout);
	}

	/**Añade una nueva imagen de fondo al panel. Si ya existe una, esta es remplazada
	 * @param nombreImagen	Ruta de la imagen
	 */
	public void setImagen(String nombreImagen) {
		if (nombreImagen != null) {
			imagen = new ImageIcon(getClass().getResource(nombreImagen)).getImage();
		}else {
			imagen = null;
		}
		repaint();
	}

	
	//Pintado de la imagen en el JPanel
	@Override
	public void paint(Graphics g) {
		//si no existe imagen, el panel no es transparente
		if (imagen != null) {
			//dibujado de la imagen
			g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
			setOpaque(false);
		} else {
			setOpaque(true);
		}
		
		//Para que aparezca de fondo, primero se pinta la imagen y luego los demás componentes
		super.paint(g);
	}
}
