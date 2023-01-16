package objetosTests;
import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import juego.objetos.Pajaro;
import juego.objetos.nivel.Cerdo;
import juego.objetos.nivel.Material;
import juego.objetos.nivel.Viga;


public class VigaTest {

	@Test
	public void testEquals() {
		Viga  viga= new Viga(new Point(0,0),120,Material.CRISTAL);
		assertTrue(viga.equals(new Viga(new Point(0,0),120,Material.CRISTAL)));
		assertFalse(viga.equals(new Viga(new Point(300,0),120,Material.CRISTAL)));
		
		assertFalse(viga.equals(new Cerdo(0, 0)));
	}

	@Test
	public void testEliminado() {
		Viga  viga= new Viga(new Point(0,0),0,Material.CRISTAL);
		assertTrue(viga.eliminado());
	}
	
	@Test
	public void testChocaConPajaro() {
		Viga  viga= new Viga(new Point(0,0),120,Material.PIEDRA);
		assertTrue(viga.chocaConPajaro(new Pajaro(0,0)));
		assertFalse(viga.chocaConPajaro(new Pajaro(120,120)));
	}
	
	@Test
	public void testClone() {
		Viga  viga= new Viga(new Point(0,0),120,Material.MADERA);
		try {
		Viga viga1= (Viga)viga.clone();
		assertTrue(viga.equals(viga1));
		}catch (CloneNotSupportedException e) {
		}
	}
	
	@Test
	public void testToString() {
		Viga  viga= new Viga(new Point(0,0),120,Material.CRISTAL);
		assertEquals("Viga de cristal(0, 0)[16x120]",viga.toString());
	}
}
