package telefront;

/**
 * <p>
 * Esta excepci�n tiene como prop�sito reflejar el escenario en donde por alg�n problema no se puede 
 * llegar hasta el servidor.
 * </p> 
 */
public class ServerUnreachableException extends ConnectionException {
	
	public ServerUnreachableException(String msg, Throwable t) {
		super(msg, t);
	}
}
