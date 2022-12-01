package main;

import gestionUsuarios.GestionUsuarios;
import gui.inicio.VentanaInicioPlay1;
import gui.inicio.VentanaInicioPlay2;
import gui.sesion.VentanaSesion;

public class Main {
	public static void main(String[] args) {
		VentanaSesion.setUsuario(GestionUsuarios.usuarioAsociado());
		if(VentanaSesion.getUsuario() == null) {
			new VentanaInicioPlay1();
		}else {
			new VentanaInicioPlay2();
		}
	}
}
