import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import objetos.Estructura;
import objetos.ObjetoPrimitivo;

public class ObjetoPrimitivoTest {

	@Test
	public void testGetX() {
		ObjetoPrimitivo p= new Estructura(2,4,5,3);
		assertEquals(2, p.getX());
	}
	@Test
	public void testGetY() {
		ObjetoPrimitivo p= new Estructura(2,4,5,3);
		assertEquals(4, p.getY());
	}
	@Test 
	public void testGetLocation(){
		ObjetoPrimitivo p= new Estructura(2,4,5,3);
		Point punto= p.getLocation();
		assertEquals(punto.getX(), p.getX(),0);
		assertEquals(punto.getY(), p.getY(),0);
	}
}

