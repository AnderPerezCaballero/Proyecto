package objetos.pajaros;

import java.awt.Color;

public class PajaroAmarillo extends Pajaro {
	protected Habilidad habilidad;

	public PajaroAmarillo(int x, int y, int radio, Color color, Habilidad habilidad) {
		super(x, y, radio, color);
		this.habilidad = habilidad;
	}
	
}