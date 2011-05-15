package ar.com.tsoluciones.arcom.hibernate;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.com.tsoluciones.arcom.cor.InternalErrorException;
import ar.com.tsoluciones.arcom.logging.TSLogger;

/**
 * Esta clase utilitaria permite ejecutar un bloque de c�digo en un contexto transaccional.
 * Para ello simplemente hay que pasar una instancia de la clase abstracta Execution&lt;T&gt; que contenga el bloque
 * que requiere ser transaccional.
 *
 */
public class Executor {

    /**
     * <p>
     * M�todo gen�rico que demarca y ejecuta el bloque. Permite que la transacci�n utilice una conexi�n seteando el 
     * isolation level en Conection.READ_UNCOMMITTED lo cual supone menos lockeo sobre las tablas involucradas. 
     * </p>
     * @param <T> Tipo gen�rico de retorno resultante de la ejecuci�n del bloque.
     * @param e Instancia de Execution.
     * 
     * @return Una instancia del tipo T
     */
	public static <T> T execute(Execution<T> e, Boolean readUncommitted) {
		int transactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
	    boolean readOnly = false;	    
        Logger logger = Logger.getLogger(Executor.class);
		Session s = e.getSession();
        Transaction transaction = s.getTransaction();
        boolean nested = false;
        long start = System.currentTimeMillis();
		try {
			nested = transaction.isActive();
			if (!nested) {
				if (readUncommitted) {
					Connection conn = s.connection();
					transactionIsolation = conn.getTransactionIsolation();
					readOnly = conn.isReadOnly();
					conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					conn.setReadOnly(true);
				}
				transaction = s.beginTransaction();
			} else {
				logger.debug("Transacci�n anidada");
			}
				
			logger.debug("Iniciando transacci�n");
			if (e.actionName != null)
				TSLogger.getLogger(HibernateService.SQL_CATEGORY_LOG).debug("******** Iniciando transacci�n en Executor para " + e.actionName);
			T retValue = e.block();
			if (!nested)
				transaction.commit();
			if (e.actionName != null)
				TSLogger.getLogger(HibernateService.SQL_CATEGORY_LOG).debug("-------- Hice commit de la transacci�n en el Executor para " + e.actionName + 
						"(" + (System.currentTimeMillis() - start)+ "ms)");
			logger.debug("Commit");
			return retValue;
		} catch (Throwable t) {
            if (transaction != null && transaction.isActive() && !nested)
                transaction.rollback();
			logger.debug("Haciendo Rollback");
			if (e.actionName != null)
				TSLogger.getLogger(HibernateService.SQL_CATEGORY_LOG).debug("!!!!!!! Rollback en el Executor para " + e.actionName);
			throw new InternalErrorException("Se produjo un error en la transacci�n. Se hizo rollback", t);
		} finally {
			if (!nested) {
				if (readUncommitted) {
	                Connection conn = s.connection();
	                try {
		                conn.setTransactionIsolation(transactionIsolation);
		                conn.setReadOnly(readOnly);
	                } catch (SQLException sqle) {
	                	throw new RuntimeException("Imposible restaurar el isolation level de la conexi�n", sqle);
	                }
					
				}
				e.closeSession();
			}
				
        }		
	}
	
    /**
     * M�todo gen�rico que demarca y ejecuta el bloque.
     * @param <T> Tipo gen�rico de retorno resultante de la ejecuci�n del bloque.
     * @param e Instancia de Execution.
     * @return Una instancia del tipo T
     */
	public static <T> T execute(Execution<T> e){
		return execute(e, false);
	}

    /**
     * Clase base para los bloques de ejecuci�n que requieran ejecutarse en un contexto transaccional.
     *
     * @param <T> Tipo gen�rico de retorno resultante de la ejecuci�n del bloque.
     */
	public static abstract class Execution<T> {
		protected DataSource ds;
		protected String actionName;
		protected String datasourceName = null;

        public Execution(DataSource ds) {
			this.ds = ds;
		}
        
        public Execution(DataSource ds, String dynamicDSName) {
			this.ds = ds;
			datasourceName = dynamicDSName;
		}
        
        /**
         * En este m�todo va el c�digo a ejecutar.
         */
		public abstract T block();

	    /**
	     * Obtiene la sesi�n de hibernate vinculada al thread actual.
	     * @return Session
	     */
		public Session getSession() {
			if (datasourceName != null) {
				return ds.getCurrentSession(datasourceName);
			} else
				return ds.getCurrentSession();
		}

        /**
         * Cierra la sesi�n usada
         */
        public void closeSession() {
            ds.close();
        }

        public void setActionName(String s) {
        	this.actionName = s;
        }
	}
}


