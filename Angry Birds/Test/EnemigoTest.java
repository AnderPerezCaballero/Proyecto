import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import objetos.Enemigo;
import objetos.Estructura;
import objetos.pajaros.Pajaro;

public class EnemigoTest {
	@Test
	public void testSetX() {
		Enemigo e= new Enemigo(50,4,10);
		e.setX(25);
		assertEquals(25, e.getX(),0);
		e.setX(2);
		assertEquals(25,e.getX(),0);
	}
	@Test
	public void testSetY() {
		Enemigo e= new Enemigo(2,50,10);
		e.setY(25);
		assertEquals(25, e.getY(),0);
		e.setY(4);
		assertEquals(25,e.getY(),0);
	}

	@Test
	public void testGetRadio() {
		Enemigo e= new Enemigo(2,4,5);
		assertEquals(5,e.getRadio(),0);
	}
	@Test
	public void testSetRadio() {
		Enemigo e= new Enemigo(2,4,5);
		assertEquals(5,e.getRadio(),0);
	}
	@Test
	public void testContienePunto() {
		Point punto= new Point (13,8);
		Enemigo e= new Enemigo(10, 10, 5);
		assertTrue(e.contienePunto(punto));
		e.setRadio(2);
		assertFalse(e.contienePunto(punto));
	}
	
}
