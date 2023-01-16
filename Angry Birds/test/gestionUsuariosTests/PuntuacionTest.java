package gestionUsuariosTests;

import static org.junit.Assert.*;

import org.junit.Test;

import gestionUsuarios.Puntuacion;

public class PuntuacionTest {

	private static String fecha = "2022-12-02T18:17:50.123252900+01:00[Europe/Madrid]";
	private static int estrellas = 3;			
	private static int nivel = 5;	
	private static Puntuacion puntuacion = new Puntuacion(fecha, estrellas, nivel);
	
	@Test
	public void testGetFecha() {
		assertEquals(fecha, puntuacion.getFecha());
	}
	
	@Test
	public void testGetEstrellas() {
		assertEquals(estrellas, puntuacion.getEstrellas());	
	}
	
	@Test
	public void testGetNivel() {
		assertEquals(nivel, puntuacion.getNivel());		
	}
	
	@Test
	public void testToString() {
		assertEquals(String.format("%d conseguidas el %s", estrellas, "2022-12-02"), puntuacion.toString());
	}
}
