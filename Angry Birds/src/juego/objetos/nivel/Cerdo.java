package juego.objetos.nivel;

import gui.juego.VentanaJuego;
import juego.objetos.Objeto;
import juego.objetos.Pajaro;

public class Cerdo extends Objeto implements ObjetoNivel{
	
	private static final String IMAGEN = "/imgs/Cerdo.png"; 
	private static final int radio = 20;
	
	/** Crea un nuevo cerdo con 100 puntos de vida
	 * @param x Coordenada x del centro del cerdo
	 * @param y Coordenada y del centro del cerdo
	 */
	public Cerdo(int x,int y) {
		super(x,y);
	}
	
	/** Indica si el cerdo choca con el pájaro
	 * @return true si choca, false si no
	 */
	public boolean chocaConPajaro(Pajaro pajaro) {
		return pajaro.getLocation().distance(getLocation()) < radio + Pajaro.getRadio();
	}
	
	/** Indica si el cerdo es eliminado después de colisionar. Devuelve true por defecto, pues el cerdo está diseñado para ser eliminado siempre
	 *@return true por defecto
	 */
	public boolean eliminado() {
		return true;
	}
	
	/** Dibuja el cerdo en la ventana
	 * @param v ventana en la que dibujar el cerdo
	 */
	public void dibuja(VentanaJuego v) {
		v.dibujaImagen(IMAGEN, (double) x, (double) y, radio * 2, radio * 2, v.getEscalaDibujo(),0,1.0f);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Cerdo(x, y);
	}

	@Override
	public String toString() {
		return String.format("Cerdo(%d, %d)", x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Cerdo) {
			Cerdo cerdo = (Cerdo) obj;
			return cerdo.x == x && cerdo.y == y;
		}
		return false;
	}
}
