package gui;

public class VentanaInicioSesion extends VentanaSesion{

	private static final long serialVersionUID = 1L;

	public VentanaInicioSesion() {
		super(5);
		botonAceptar.setText("Iniciar Sesión");
	}
	
	
	@Override
	public void siguienteVentana() {
//		new Mensaje("Iniciando Sesión", botonAceptar).run();
	}	
	
	public static void main(String[] args) {
		new VentanaInicioSesion().iniciar();
	}
}
