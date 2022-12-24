package objetos;

public enum Material {
	CRISTAL, MADERA, PIEDRA;
	
	/** Método que devuelve el material asociado dependiendo del string que se le pase como parámetro
	 * @param material String que representa el material
	 * @return	El material asociado al string
	 * @throws IllegalArgumentException	Si no hay ningún String asociado a ese material
	 */
	public static Material getMaterial(String material) throws IllegalArgumentException{
		material = material.toUpperCase();
		if(material.equals("CRISTAL")) {
			return CRISTAL;
		}else if(material.equals("MADERA")) {
			return MADERA;
		}else if(material.equals("PIEDRA")){
			return PIEDRA;
		}
		throw new IllegalArgumentException(String.format("No existe ningún material asociado al tipo de material <%s>", material));
	}
}
