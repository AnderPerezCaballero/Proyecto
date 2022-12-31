package objetos;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArrayList;

import gui.juego.VentanaJuego;
import objetos.pajaros.Pajaro;

public class Nivel {
	private int pajarosDisponibles;
	private int pajarosInicio;
	private int cerdosDisponibles;
	private List<ObjetoNivel> objetosNivel;
	private Map<Point, ObjetoNivel> mapaObjetos;	//Para poder acceder a los objetos originales a través de las copias

	/** Crea un nuevo objeto nivel a partir de un fichero
	 * @param id id del nivel a cargar
	 */
	public Nivel(int id) {
		objetosNivel = Collections.synchronizedList(new ArrayList<>());
		mapaObjetos = new HashMap<>();
		cerdosDisponibles = 0;
		
		//Carga del nivel
		try(BufferedReader in = new BufferedReader(new InputStreamReader(Nivel.class.getResourceAsStream(String.format("/niveles/Nivel%d.txt", id))))) {
			String linea;
			while ((linea = in.readLine()) != null) {
				linea = linea.toLowerCase();
				if(!linea.equals("")) {
					if(!linea.substring(0,1).equals("#")) { //Comentarios
						if(linea.equals("cerdos{") || linea.equals("cerdos {")) {
							while(!(linea = in.readLine().toLowerCase()).equals("}")) {
								if(!linea.equals("") && !linea.equals("\t")) {
									if(!linea.contains("#")) { //Comentarios dentro de las llaves
										StringTokenizer st = new StringTokenizer(linea, " ,\t");
										try {
											objetosNivel.add(new Cerdo(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
											cerdosDisponibles ++;
										}catch (NoSuchElementException | NumberFormatException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}else if(linea.equals("vigas{") || linea.equals("vigas {")) {
							while(!(linea = in.readLine().toLowerCase()).equals("}")) {
								if(!linea.equals("") && !linea.equals("\t")) {
									if(!linea.contains("#")) { //Comentarios dentro de las llaves
										StringTokenizer st = new StringTokenizer(linea, " ,\t");
										try {
											objetosNivel.add(new Viga(new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())), Integer.parseInt(st.nextToken()), Material.getMaterial(st.nextToken())));
										}catch (NoSuchElementException | IllegalArgumentException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}else {			
							StringTokenizer st = new StringTokenizer(linea, " ,\t");
							if(st.nextToken().equals("pajaros") && st.nextToken().equals("=")) {
								try {
									pajarosDisponibles = Integer.parseInt(st.nextToken());
								}catch(NumberFormatException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(ObjetoNivel objeto : objetosNivel) {
			mapaObjetos.put(objeto.getLocation(), objeto);
		}
		
		pajarosInicio = pajarosDisponibles;
	}
	
	
	/** Devuelve el objeto original asociado a una copia del objeto
	 * @param objetoCopia	Copia del objeto
	 * @return	Objeto original asociado a la copia
	 */
	public ObjetoNivel getReferenciaCopia(ObjetoNivel objetoCopia) {
		return mapaObjetos.get(objetoCopia.getLocation());
	}
	
	/** Elimina o cambia de estado el objeto especificado
	 * @param objeto Objeto que hay que eliminar o cambiar
	 */
	public void remove(ObjetoNivel objeto) {
		if(objeto.eliminado()) {
			objetosNivel.remove(objeto);
			if(objeto instanceof Cerdo) {
				cerdosDisponibles --;
			}
		}
	}

	/** Elimina o cambia de estado los objetos especificados
	 * @param objetos Objetos que hay que eliminar o cambiar
	 */
	public void removeAll(ObjetoNivel ... objetos) {
		for(ObjetoNivel objeto : objetos) {
			remove(objeto);
		}
	}
	
	/** Devuelve la lista de objetos del nivel
	 * @return La lista de los objetos del nivel
	 */
	public List<ObjetoNivel> getObjetos() {
		return objetosNivel;
	}
	
	/** Devuelve una copia profunda de la lista de los objetos del nivel
	 * @return copia de la lista de los objetos del nivel
	 */
	public List<ObjetoNivel> getCopiaObjetos() {
		List<ObjetoNivel> copia = new ArrayList<>();
		for(ObjetoNivel objeto : objetosNivel) {
			try {
				copia.add((ObjetoNivel) objeto.clone());
			}catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return copia;
	}

	/** Reduce por uno el número de pájaros disponibles
	 * @return True en caso de que el número de pájaros se pueda reducir, false si no
	 */
	public boolean reducirPajarosDisponibles() {
		if(pajarosDisponibles <= 1) {
			return false;
		}
		pajarosDisponibles--;
		return true;
	}
	
	/** Indica si quedan cerdos en el nivel
	 * @return true si hay uno o más cerdos activos en el nivel, false si no
	 */
	public boolean hayCerdos() {
		return cerdosDisponibles > 0;
	}
	
	/** Devuelve el número de pájaros disponibles al empezar el nivel
	 * @return int que representa el número de pajaros que se pueden utilizar en el nivel
	 */
	public int getPajarosDeInicio(){
		return pajarosInicio;
	}
	
	/** Devuelve el número de pájaros disponibles en ese momento
	 * @return in que representan el número de pájaros disponibles en ese momento concreto
	 */
	public int getPajarosDisponibles() {
		return pajarosDisponibles;
	}
	
	/** Dibuja los elementos del nivel
	 * @param v Ventana en la que se dibujan
	 */
	public void dibujaElementos(VentanaJuego v) {
		try {
			for(Dibujable elemento : objetosNivel) {
				elemento.dibuja(v);
			}
		}catch(ConcurrentModificationException e) {}
	}
	
	/** Dibuja en la ventana los pájaros disponibles
	 * @param v Ventana en la que los pájaros disponibles se dibujan
	 */
	public void dibujaPajarosDisponibles(VentanaJuego v) {
		int radio = Pajaro.getRadio();
		int ySuelo = Juego.getYSuelo();
		int xTiraPajaros = Juego.getXTiraPajaros() - 30;
		for(int i = 0; i < pajarosDisponibles - 1; i++) {
			v.dibujaImagen(Pajaro.getRutaImagen(), xTiraPajaros - i*radio*3, ySuelo - radio, radio * 2, radio * 2, 1, 0, 1.0f);
		}
	}
}