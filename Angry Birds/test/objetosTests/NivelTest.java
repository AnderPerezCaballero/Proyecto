package teses;
import static org.junit.Assert.*;import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import objetos.Enemigo;
import objetos.Estructura;
import objetos.Nivel;
import objetos.pajaros.Pajaro;

public class NivelTest {
	
	private Nivel nv;
	private List<Pajaro> listaPajaros;
	
	@Before //Crea antes del test un nivel y así no ceramos en cada test un nivel nuevo todo el rato
	public void creadorNivel() {
		
		listaPajaros = new ArrayList<Pajaro>();
		listaPajaros.add(new Pajaro(10, 15, 10, null));
		listaPajaros.add(new Pajaro(15, 10, 15, null));

		nv = new Nivel("RutaEjemplo", 1, 4, listaPajaros);	
	}
	
	@Test
	public void getRutaMapaTest() {
		assertEquals("RutaEjemplo", nv.getRutaMapa() );
		
	}

	@Test 
	public void setRutaMapaTest() {
		nv.setRutaMapa("NuevaRuta");
		assertEquals("NuevaRuta", nv.getRutaMapa());
	}
	
	@Test
	public void getIdTest() {
		assertEquals(1, nv.getId());
		
	}
	
	@Test
	public void setIdTest() {
		nv.setId(2);
		assertEquals(2, nv.getId());
	}
	
	@Test
	public void getNumCerdosTest() {
		assertEquals(4, nv.getNumCerdos());
	}
	
	@Test
	public void setNumCerdosTest() {
		nv.setNumCerdos(2);
		assertEquals(2, nv.getNumCerdos());
	}
	
	@Test
	public void getListaPajarosTest() {
		assertEquals(listaPajaros, nv.getListaPajaros());
		
	}
	
	@Test
	public void setListaPajarosTest() {
		List<Pajaro> nuevaLista = new ArrayList<>();
		nuevaLista.add(new Pajaro(13, 10, 12, null));
		nuevaLista.add(new Pajaro(15, 6, 10, null));
		nuevaLista.add(new Pajaro(15, 10, 7, null));
		
		nv.setListaPajaros(nuevaLista);
		assertEquals(nuevaLista, nv.getListaPajaros());
	}
	


}