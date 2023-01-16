import java.sql.SQLException;

import gestionUsuarios.GestionUsuarios;
import gestionUsuarios.Usuario;
import gui.inicio.VentanaJugar1;
import gui.inicio.VentanaJugar2;
import gui.sesion.VentanaSesion;

public class Main {
	public static void main(String[] args) {
		VentanaSesion.setUsuario(GestionUsuarios.usuarioAsociado());
		if(VentanaSesion.getUsuario() == null) {
			new VentanaJugar1();
		}else {
			new VentanaJugar2();
		}
	}
}