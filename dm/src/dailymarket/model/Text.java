package dailymarket.model;

/**
 * Clase utiliria con m�todos para operar sobre cadenas de texto.
 */
public class Text {
    
    /**
     * Hace trim de un String controlando la nulidad del mismo
     * @param string el string del que se desea eliminar los espacios
     * @return el string sin los espacios o null si el par�metro es null 
     */
    public static String trim(String string) {
    	return string != null ? string.trim() : null;
    }
}
