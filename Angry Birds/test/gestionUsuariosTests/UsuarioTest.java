package gestionUsuariosTests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import gestionUsuarios.Puntuacion;
import gestionUsuarios.Token;
import gestionUsuarios.Usuario;

public class UsuarioTest {
	
	private static String nombre = "nombre";					
	private static String contraseña = "contraseña";		
	private static int tiempoJugado = 0;				
	private static List<Puntuacion> puntuaciones = Arrays.asList(new Puntuacion(2, 1), new Puntuacion(1, 2), new Puntuacion(3, 5));
	private static Usuario usuario = new Usuario(nombre, contraseña, tiempoJugado, puntuaciones, null);
	
	
	@Test
	public void testGetNombre() {
		assertEquals(nombre, usuario.getNombre());
	}
	
	@Test
	public void testGetContraseña() {
		assertEquals(contraseña, usuario.getContraseña());
	}
	
	@Test
	public void testAddTiempoGetTiempo() {
		usuario.addTiempo(2);
		assertEquals(2, usuario.getTiempoJugado());
	}
	
	@Test
	public void testGetPuntuaciones() {
		assertEquals(puntuaciones, usuario.getPuntuaciones());
	}
	
	@Test
	public void testGetSetToken() {
		assertNull(usuario.getToken());
		Token token = new Token(usuario);
		usuario.setToken(token);
		assertEquals(token, usuario.getToken());
	}
	@Test
	public void testHashCode() {
		assertEquals(nombre.hashCode(), usuario.hashCode());
	}
	
	@Test
	public void testEquals() {
		assertTrue(usuario.equals(new Usuario(nombre, "123", 12312412, puntuaciones, null)));
		assertTrue(usuario.equals(usuario));
		assertFalse(usuario.equals(new Usuario("nombre2", contraseña, tiempoJugado, puntuaciones, null)));
	}
	
	@Test
	public void testToString() {
		assertEquals(usuario.getNombre(), usuario.toString());
	}
}
