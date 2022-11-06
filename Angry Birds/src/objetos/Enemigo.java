package objetos;

public class Enemigo extends ObjetoPrimitivo{
	protected int radio;
	public Enemigo() {
		// TODO Auto-generated constructor stub
		super();
		radio=2;
	}
	public Enemigo(int x,int y,int radio) {
		super(x,y);
		this.radio=radio;
	}
	public int getRadio() {
		return radio;
	}
	public void setRadio(int radio) {
		this.radio = radio;
	}

}
