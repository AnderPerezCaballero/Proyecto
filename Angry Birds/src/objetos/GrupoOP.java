package objetos;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;



public class GrupoOP {
	private List<ObjetoPrimitivo> grupoOP;
	private final int NUM_MAX_POR_DEFECTO=10;
	private int tamanyoMaximo;
	
	public GrupoOP( int numMax ) {
		grupoOP = new ArrayList<>();  // new ArrayList<ObjetoPrimitivo>();
		tamanyoMaximo = numMax;
	}

	/** Crea un grupo de pajaros de tamaño 10
	 */
	public GrupoOP() {
		grupoOP = new ArrayList<>(NUM_MAX_POR_DEFECTO);
		tamanyoMaximo = NUM_MAX_POR_DEFECTO;
	}
	
	/** Devuelve el número de las pajaros actualmente en el grupo
	 * @return	Número de pajaros (entre 0 y núm máximo)
	 */
	public int getNumGrupoOP() {
		return grupoOP.size();
	}
	
	/** Añade una pelota al grupo
	 * @param pelota	Nueva pelota a añadir
	 * @return	true si el añadido es correcto, false si no cabe (el grupo está lleno)
	 */
	public boolean anyadeObjetoPrimitivo( ObjetoPrimitivo pelota ) {
		if (grupoOP.size()==tamanyoMaximo) { // Esto no haría falta porque el arraylist no tiene el límite de memoria que tiene el array
			return false;
		}
		grupoOP.add(pelota);
		return true;
	}
	
	/** Borra una pelota del grupo
	 * @param numObjetoPrimitivo	Índice de la pelota a borrar. Debe estar en el rango 0 a (n-1) siendo n el número de pajaros existentes.
	 */
	public void borraObjetoPrimitivo( int numObjetoPrimitivo ) {
		grupoOP.remove( numObjetoPrimitivo );  // Hace el trabajo de movimiento
	}
	
	/** Borra una pelota del grupo
	 * @param pelota	ObjetoPrimitivo a borrar. Si no está en el grupo, no se hace nada
	 */
	public void borraObjetoPrimitivo( ObjetoPrimitivo pelota ) {
		// grupoOP.remove( pelota );  // Vale cuando queramos comparar por equals
		int posi = buscaObjetoPrimitivo( pelota );
		if (posi!=-1) {
			borraObjetoPrimitivo( posi );
		}
	}
	
	/** Devuelve una pelota del grupo
	 * @param numObjetoPrimitivo	Índice de la pelota a devolver, de 0 a n-1
	 * @return	ObjetoPrimitivo correspondiente (la devuelve pero no la quita del grupo)
	 */
	public ObjetoPrimitivo getObjetoPrimitivo( int numObjetoPrimitivo ) {
		return grupoOP.get(numObjetoPrimitivo);
	}
	
	/** Busca una pelota en el grupo
	 * @param pelota	ObjetoPrimitivo a buscar (mismo objeto)
	 * @return	Posición donde está la pelota, -1 si no se encuentra
	 */
	public int buscaObjetoPrimitivo( ObjetoPrimitivo pelota ) {
		for (int i=0; i<grupoOP.size(); i++) {
			if (pelota==grupoOP.get(i)) return i;
		}
		return -1;
	}
	
	// Añadido para poder buscar pajaros iguales siendo distintos objetos
	/** Busca una pelota en el grupo
	 * @param pelota	ObjetoPrimitivo a buscar, de acuerdo a sus ATRIBUTOS
	 * @return	Posición donde está la pelota, -1 si no se encuentra
	 */
	public int buscaObjetoPrimitivoEquals( ObjetoPrimitivo pelota ) {
		return grupoOP.indexOf( pelota );
//		for (int i=0; i<grupoOP.size(); i++) {
//			if (pelota.equals(grupoOP.get(i))) return i;
//		}
//		return -1;
	}
	
	/** Busca un punto en todas las pajaros para ver si está dentro de alguna
	 * @param p	Punto de búsqueda
	 * @return	La pelota dentro de la que está ese punto, o null si no está en ninguna
	 */
	public ObjetoPrimitivo buscaPuntoEnObjetoPrimitivos( Point p ) {
		for (ObjetoPrimitivo ObjetoPrimitivo : grupoOP) {
			if (ObjetoPrimitivo.contienePunto( p )) {
				return ObjetoPrimitivo;
			}
		}
//		for (int i=0; i<grupoOP.size(); i++) {
//			ObjetoPrimitivo ObjetoPrimitivo = grupoOP.get(i);
//			if (ObjetoPrimitivo.contieneA( p )) {
//				return ObjetoPrimitivo;
//			}
//		}
		return null;
	}
	
	/** Dibuja todas las pajaros del grupo
	 * @param v	Ventana en la que dibujar
	 */
	public void dibuja( VentanaJuego v ) {
		for (ObjetoPrimitivo oj : grupoOP) {
			oj.dibuja( v );
		}
	}


}
