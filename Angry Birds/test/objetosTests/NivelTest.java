package objetosTests;
import static org.junit.Assert.*;import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import juego.objetos.Objeto;
import juego.objetos.nivel.Cerdo;
import juego.objetos.nivel.Nivel;
import juego.objetos.nivel.ObjetoNivel;


public class NivelTest {
	@Test
	public void testNivel() {
		Nivel n= new Nivel(1);
		assertTrue(n.getObjetos().contains(new Cerdo(1550, 754)));
	}
	
	@Test 
	public void testGetReferenciaCopia() {
		Nivel n= new Nivel(1);
		ObjetoNivel o=n.getObjetos().get(1);
		try {
			ObjetoNivel o1= (ObjetoNivel)o.clone();
			assertTrue(o.equals(n.getReferenciaCopia(o)));
		}catch(CloneNotSupportedException e) {
		}
	}
	@Test
	public void testRemove() {
		Nivel n= new Nivel(1);
		ObjetoNivel o= n.getObjetos().get(5);
		n.remove(n.getObjetos().get(5));
		assertFalse(n.getObjetos().contains(o));
		n.remove(n.getObjetos().get(1));
	}
	
	
	@Test 
	public void testRemoveAll() {
		Nivel n= new Nivel(1);
		n.removeAll(new Cerdo(1550, 754),new Cerdo(1650, 754));
		assertFalse(n.getObjetos().contains(new Cerdo(1550, 754)));
		assertFalse(n.getObjetos().contains(new Cerdo(1650,754)));
		}
	
	@Test 
	public void testGetCopiaObjetos() {
		Nivel n= new Nivel(1);
		List<ObjetoNivel> l= n.getCopiaObjetos();
		assertTrue(l.equals(n.getObjetos()));	
	}
	
	@Test
	
	public void testReducirNumeroPajarosDisponibles() {
		Nivel n= new Nivel(1);
		assertTrue(n.reducirPajarosDisponibles());
		n.reducirPajarosDisponibles();
		n.reducirPajarosDisponibles();
		n.reducirPajarosDisponibles();
		assertFalse(n.reducirPajarosDisponibles());
		}
	
	@Test
	public void testHayCerdos() {
		Nivel n= new Nivel(1);
		assertTrue(n.hayCerdos());
		n.setCerdosDisponibles(0);
		assertFalse(n.hayCerdos());
	}
	
	@Test
	public void testGetPajarosInicio() {
		Nivel n= new Nivel(1);
		assertEquals(3, n.getPajarosDeInicio(),0);
	}
	
	@Test
	public void testGetPajarosDisponibles() {
		Nivel n= new Nivel(1);
		assertEquals(3, n.getPajarosDisponibles());
	}
}
