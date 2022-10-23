package gui;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


/** Clase para dibujar imágenes
 * @author diego
 *
 */
public class Imagen extends JPanel{
	
    private Image fondo = null;
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), null);
    }
   
}