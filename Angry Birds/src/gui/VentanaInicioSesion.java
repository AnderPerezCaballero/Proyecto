package gui;

public class VentanaInicioSesion extends VentanaSesion{

	private static final long serialVersionUID = 1L;

	public VentanaInicioSesion() {
		super(5);
		botonAceptar.setText("Iniciar Sesión");
	}
	
	
	@Override
	protected void siguienteVentana() {
		new MensajeCarga("Iniciando Sesión", botonAceptar).start();
	}	
	
	public static void main(String[] args) {
		new VentanaInicioSesion().iniciar();
	}
}
