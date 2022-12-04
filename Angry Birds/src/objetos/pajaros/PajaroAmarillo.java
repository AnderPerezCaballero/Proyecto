package objetos.pajaros;

import java.awt.Color;

public class PajaroAmarillo extends Pajaro {
	protected Habilidad habilidad;

	public PajaroAmarillo(int x, int y, Color color, Habilidad habilidad) {
		super(x, y, color);
		this.habilidad = habilidad;
	}
	
}