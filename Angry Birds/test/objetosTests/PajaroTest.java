package objetosTests;
import static org.junit.Assert.*;

import java.awt.Point;

import javax.swing.JFrame;

import org.junit.Test;
import  juego.objetos.Pajaro;
import juego.objetos.nivel.Cerdo;
import juego.objetos.nivel.Material;
import juego.objetos.nivel.Nivel;
import juego.objetos.nivel.Viga;
public class PajaroTest {


	@Test
	public void testGetVX() {
		Pajaro p= new Pajaro(4,5);
		assertEquals(0,p.getvX(),0);
	}
	@Test
	public void testSetVX() {
		Pajaro p= new Pajaro(4,5);
		p.setvX(5);
		assertEquals(5,p.getvX(),0);
	}
	@Test
	public void testGetVY() {
		Pajaro p= new Pajaro(4,5);
		assertEquals(0,p.getvY(),0);
	}
	@Test
	public void testSetVY() {
		Pajaro p= new Pajaro(4,5);
		p.setvY(1);
		assertEquals(1,p.getvY(),0);
	}
	@Test
	public void testChoqueConLimitesLaterales() {
		Pajaro p= new Pajaro(10, 4);
		assertTrue(p.choqueConLimiteVertical());
		p.setX(25);
		assertFalse(p.choqueConLimiteVertical());
	}
	@Test
	public void testChoqueConSuelo() {
		Pajaro p= new Pajaro(10, 10);
		assertFalse(p.choqueConSuelo());
		p.setY(900);
		assertTrue(p.choqueConSuelo());
	}
	@Test
	public void testAplicarRozamiento() {
		Pajaro p= new Pajaro(10, 10);
		p.aplicarRozamiento(1);
		assertEquals(0, p.getvX(),0);
		p.setvX(1);
		p.aplicarRozamiento(1);
		assertEquals(0, p.getvX(),0);
		p.setvX(2);
		p.aplicarRozamiento(1);
		assertEquals(1, p.getvX(),0);
		p.setvX(-1);
		p.aplicarRozamiento(1);
		assertEquals(0, p.getvX(),0);
		p.setvX(-2);
		p.aplicarRozamiento(1);
		assertEquals(-1, p.getvX(),0);
	}
	@Test
	public void testEliminarObjeto() {
		Pajaro p= new Pajaro(10, 10);
		p.eliminarObjetos(new Nivel(1));
	}
	
	@Test
	public void testCancelarMovimiento() {
		Pajaro p= new Pajaro(10, 10);
		p.cancelarMovimiento();
		assertFalse(p.isMover());
	}
	
}
