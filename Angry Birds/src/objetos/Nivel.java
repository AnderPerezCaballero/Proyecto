package objetos;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import gui.juego.VentanaJuego;
import objetos.pajaros.Pajaro;

public class Nivel {
	private int numPajaros;
	private List<ElementoNivel> elementos;

	/** Crea un nuevo objeto nivel a partir de un fichero
	 * @param id id del nivel a cargar
	 */
	public Nivel(int id) {
		elementos = new ArrayList<>();
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
											elementos.add(new Cerdo(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
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
											elementos.add(new Viga(new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())), Integer.parseInt(st.nextToken()), Material.getMaterial(st.nextToken())));
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
									numPajaros = Integer.parseInt(st.nextToken());
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
	}

	public void remove(ElementoNivel elemento) {
		elementos.remove(elemento);
	}
	
	public List<ElementoNivel> getElementos() {
		return elementos;
	}

	/** Dibuja los elementos del nivel
	 * @param v Ventana en la que se dibujan
	 */
	public void dibujaElementos(VentanaJuego v) {
		for(ElementoDibujable elemento : elementos) {
			elemento.dibuja(v);
		}
	}
}