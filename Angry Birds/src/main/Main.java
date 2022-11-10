package main;

import gestionUsuarios.GestionUsuarios;
import gui.inicio.VentanaInicioPlay1;
import gui.sesion.VentanaSesion;

public class Main {
	public static void main(String[] args) {
		VentanaSesion.setUsuario(GestionUsuarios.usuarioAsociado());
		if(VentanaSesion.getUsuario() == null) {
			VentanaSesion.centrar(new VentanaInicioPlay1());
		}else {
			//TODO lanzar la otra ventana´
			System.out.format("Sesión iniciada con el usuario: %s", VentanaSesion.getUsuario());
		}
	}
}
