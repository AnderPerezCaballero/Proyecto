package objetosTests;
import static org.junit.Assert.*;

import javax.swing.DefaultRowSorter;

import org.junit.Test;

import juego.objetos.Pajaro;
import juego.objetos.nivel.Cerdo;
import juego.objetos.nivel.Material;
import juego.objetos.nivel.Viga;

public class CerdoTest {

	@Test
	public void testEquals() {
		Cerdo  cerdo= new Cerdo(120,120);
		assertTrue(cerdo.equals(new Cerdo(120,120)));
		assertFalse(cerdo.equals(new Cerdo(0,0)));
		assertFalse(cerdo.equals(new Cerdo(120,0)));
		assertFalse(cerdo.equals(cerdo.equals(new Cerdo(0,120))));
		assertFalse(cerdo.equals(new Viga(cerdo.getLocation(),0,Material.CRISTAL)));
	}

	@Test
	public void testEliminado() {
		Cerdo  cerdo= new Cerdo(120,120);
		assertTrue(cerdo.eliminado());
	}
	
	@Test
	public void testChocaConPajaro() {
		Cerdo  cerdo= new Cerdo(120,120);
		assertTrue(cerdo.chocaConPajaro(new Pajaro(120,120)));
		assertFalse(cerdo.chocaConPajaro(new Pajaro(0, 0)));
	}
	
	@Test
	public void testClone() {
		Cerdo  cerdo= new Cerdo(120,120);
		try{
		Cerdo cerdo1= (Cerdo)cerdo.clone();
		assertTrue(cerdo.equals(cerdo1));
		}catch(CloneNotSupportedException e) {
		}
		
	}
	
	@Test
	public void testToString() {
		Cerdo  cerdo= new Cerdo(120,120);
		assertEquals("Cerdo(120, 120)",cerdo.toString());
	}
}
