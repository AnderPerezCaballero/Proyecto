package objetosTests;
import static org.junit.Assert.*;

import java.awt.Point;

import javax.swing.JFrame;

import org.junit.Test;

import objetos.Enemigo;
import objetos.Estructura;
import objetos.pajaros.Pajaro;

public class PajaroTest {

	@Test
	public void testGetRadio() {
		Pajaro e= new Pajaro(2,4,5);
		assertEquals(5,e.getRadio(),0);
	}
	public void testSetY() {
		Pajaro p= new Pajaro(2,50,10);
		p.setY(25);
		assertEquals(25, p.getY(),0);
		p.setY(4);
		assertEquals(25,p.getY(),0);
	}
	/**Metodo para comprobar que se permite cambiar el radio.Se comprueba que no te permita meter enteros negativos
	 * 
	 */
	@Test
	public void testSetRadio() {
		Pajaro p= new Pajaro(2,4,5);
		p.setRadio(15);
		assertEquals(15,p.getRadio(),0);
		p.setRadio(-50);
		assertEquals(15, p.getRadio(),0);
	}
	@Test
	public void testGetVX() {
		Pajaro p= new Pajaro(2,4,5);
		assertEquals(0,p.getvX(),0);
	}
	@Test
	public void testSetVX() {
		Pajaro p= new Pajaro(2,4,5);
		p.setvX(5);
		assertEquals(5,p.getvX(),0);
	}
	@Test
	public void testGetVY() {
		Pajaro p= new Pajaro(2,4,5);
		assertEquals(0,p.getvY(),0);
	}
	@Test
	public void testSetVY() {
		Pajaro p= new Pajaro(2,4,5);
		p.setvY(1);
		assertEquals(1,p.getvY(),0);
	}
	@Test
	public void testChoqueConLimitesLaterales() {
		JFrame v= new JFrame();
		v.setSize(30, 100);
		Pajaro p= new Pajaro(10, 4, 5);
		assertFalse(p.choqueConLimitesLaterales(v));
		p.setX(25);
		assertTrue(p.choqueConLimitesLaterales(v));
		p.setX(3);
		assertTrue(p.choqueConLimitesLaterales(v));
		p.setX(v.getWidth());
		assertTrue(p.choqueConLimitesLaterales(v));
	}
	@Test
	public void testChoqueConLimitesVerticales() {
		JFrame v= new JFrame();
		v.setSize(30, 20);
		Pajaro p= new Pajaro(10, 10, 5);
		assertFalse(p.choqueConLimitesVertical(v));
		p.setY(15);
		assertTrue(p.choqueConLimitesVertical(v));
		p.setY(3);
		assertTrue(p.choqueConLimitesVertical(v));
		p.setY(v.getHeight());
		assertTrue(p.choqueConLimitesVertical(v));
	}
	@Test
	public void testChoqueConEnemigos() {
		Enemigo e= new Enemigo(15,25,5);
		Pajaro p= new Pajaro(0, 10, 5);
		assertFalse(p.choqueConEnemigos(e));
		p.setY(20);
		p.setX(15);
		assertTrue(p.choqueConEnemigos(e));
	}
	@Test
	public void testChoqueConEstructuras() {
		Estructura e= new Estructura(100, 100,20,30);
		Pajaro p= new Pajaro(0, 0, 10);
		assertFalse(p.choqueConEstructura(e));
		p.setX(85);
		p.setY(80);
		assertTrue(p.choqueConEstructura(e));
		p.setX(110);
		assertTrue(p.choqueConEstructura(e));
		p.setY(120);
		assertTrue(p.choqueConEstructura(e));
		p.setX(90);
		assertTrue(p.choqueConEstructura(e));
	}
	@Test
	public void testContienePunto() {
		Point punto= new Point (13,8);
		Pajaro e= new Pajaro(10, 10, 5);
		assertTrue(e.contienePunto(punto));
		e.setRadio(2);
		assertFalse(e.contienePunto(punto));
	}
}
