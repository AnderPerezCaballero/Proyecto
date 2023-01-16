package gestionUsuariosTests;

import static org.junit.Assert.*;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import gestionUsuarios.Puntuacion;
import gestionUsuarios.Token;
import gestionUsuarios.Usuario;

public class TokenTest {
	
	private static String sToken = "token";
	private static ZonedDateTime caducidad = ZonedDateTime.now().minusDays(4);
	private static Usuario usuario = new Usuario("nombre", "contraseña", 10000000000000000l, Arrays.asList(new Puntuacion(2, 1), new Puntuacion(1, 2), new Puntuacion(3, 5)), null);
	private static Token token = new Token(sToken, caducidad.toString(), usuario);
	
	@Before
	public void setUp() throws Exception {
		usuario.setToken(token);
	}
	
	@Test
	public void testGenerarToken(){
		try {
			//genera siempre un Token diferente
			assertNotEquals(UUID.fromString(usuario.getNombre()).toString().toUpperCase(), new Token("token2", caducidad.toString(), usuario).getToken());
		}catch(IllegalArgumentException e) {
			//string que cumple el formato
			assertNotEquals(UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c").toString().toUpperCase(), new Token(usuario).getToken());
		}
	}
	
	@Test
	public void testGetToken() {
		assertEquals(sToken, token.getToken());
	}
	
	@Test
	public void testGetCaducidad() {
		assertEquals(caducidad, token.getCaducidad());		
	}
	
	@Test
	public void testGetUsuario() {
		assertEquals(usuario, token.getUsuario());
		assertNotEquals(new Usuario("Diego", "Merino", null), token.getUsuario());
	}
	
	@Test
	public void testIsCaducado() {
		assertTrue(token.isCaducado());
		assertFalse(new Token(usuario).isCaducado());
	}
	
	@Test
	public void testEquals() {
		assertTrue(token.equals(token));
		assertTrue(token.equals(new Token(sToken, caducidad.toString(), usuario)));
		assertFalse(token.equals(new Token(usuario)));
	}

}
