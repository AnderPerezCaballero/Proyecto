package objetos;
import java.util.List;

import objetos.pajaros.Pajaro;

public class Nivel {
	
	private String rutaMapa;
	private int id;
	private int numCerdos;
	private List<Pajaro> listaPajaros;
	
	public Nivel(String rutaMapa, int id, int numCerdos, List<Pajaro> listaPajaros) {
		super();
		this.rutaMapa = rutaMapa;
		this.id = id;
		this.numCerdos = numCerdos;
		this.listaPajaros = listaPajaros;
	}

	public String getRutaMapa() {
		return rutaMapa;
	}

	public void setRutaMapa(String rutaMapa) {
		this.rutaMapa = rutaMapa;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumCerdos() {
		return numCerdos;
	}

	public void setNumCerdos(int numCerdos) {
		this.numCerdos = numCerdos;
	}

	public List<Pajaro> getListaPajaros() {
		return listaPajaros;
	}

	public void setListaPajaros(List<Pajaro> listaPajaros) {
		this.listaPajaros = listaPajaros;
	}
}
