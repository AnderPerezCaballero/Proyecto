package objetos.pajaros;

public class PajaroAmarillo extends Pajaro {
	protected Habilidad habilidad;
	public PajaroAmarillo(int x,int y, int vX, int vY,int radio) {
		super(x,y,vX,vY,radio);
		habilidad= Habilidad.Explosion;
		// TODO Auto-generated constructor stub
	}
}
