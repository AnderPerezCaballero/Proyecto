package gestionUsuarios;

public class EstrellasIncorrectasException extends Exception{
	
    /** Excepción lanzada en caso de que las estrellas introducidas para crear un objeto puntuación sean mayores de 3 o negativas
     * @param numeroDeEstrellasIntroducido	el número de estrellas incorrectas que han hecho que la excepción sea lanzada
     */
    public EstrellasIncorrectasException(int numeroDeEstrellasIntroducido) {
        super(String.format("El número de estrellas (%d) introducido es incorrecto", numeroDeEstrellasIntroducido));
      }
}
