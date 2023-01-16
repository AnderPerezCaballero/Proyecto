package objetosTests;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import juego.objetos.Pajaro;

public class ObjetoTest {

	@Test
	public void testGetLocation() {
		Pajaro p = new Pajaro(0,0);
		assertEquals(new Point(0,0), p.getLocation());
	}
	
	public void testGetX() {
		Pajaro p = new Pajaro(0,0);
		assertEquals(0, p.getX(),0);
	}
	
	public void testGetY() {
		Pajaro p = new Pajaro(0,0);
		assertEquals(0, p.getY(),0);
	}
	
	public void testSetX() {
		Pajaro p = new Pajaro(0,0);
		p.setX(2);
		assertEquals(2, p.getX(),0);
	}
	
	public void testSetY() {
		Pajaro p = new Pajaro(0,0);
		p.setY(2);
		assertEquals(2, p.getY(),0);
	}

}
