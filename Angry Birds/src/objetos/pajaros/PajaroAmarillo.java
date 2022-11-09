package objetos.pajaros;

public class PajaroAmarillo extends Pajaro {
	protected Habilidad habilidad;
	public PajaroAmarillo(int x,int y, int vX, int vY,int radio) {
		super(x,y,radio);
		habilidad= Habilidad.Destello;
		// TODO Auto-generated constructor stub
	}
}
