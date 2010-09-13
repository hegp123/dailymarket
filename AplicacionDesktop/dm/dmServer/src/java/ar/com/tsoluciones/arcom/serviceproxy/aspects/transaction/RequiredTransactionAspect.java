package ar.com.tsoluciones.arcom.serviceproxy.aspects.transaction;

import ar.com.tsoluciones.arcom.hibernate.DataSource;
import ar.com.tsoluciones.arcom.hibernate.HibernateService;
import ar.com.tsoluciones.arcom.hibernate.Transactional;
import ar.com.tsoluciones.arcom.logging.Log;
import ar.com.tsoluciones.arcom.logging.TSLogger;
import ar.com.tsoluciones.arcom.serviceproxy.aspects.Aspect;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


/**
 * Copyright (c) Telef�nica Soluciones
 * Todos los derechos reservados.
 */

/**
 * <p>
 * Cuando se asigna este aspecto a un m�todo, coordina su transacci�n, comenz�ndola, confirm�ndola
 * o cancel�ndola seg�n sea necesario.
 * </p>
 *
 * @author despada
 * @version 1.0, Mar 1, 2005, 10:50:49 AM
 * @see Aspect
 * @since 1.0.0.0
 */
public class RequiredTransactionAspect extends Aspect {
    private DataSource ds;
    private Transaction transaction;
    private boolean nested = false;
    private long start;

    @Override
	public void onBeforeMethod(Method method, Object[] args, Annotation[] annotations) {
    	start = System.currentTimeMillis();
        TSLogger logger = Log.getLogger(RequiredTransactionAspect.class);
        String methodClass = method.getName() + "*******" + method.getDeclaringClass();
        ds = getDataSource(method, annotations);
        Session currentSession = ds.getCurrentSession();
        nested = currentSession.getTransaction().isActive();
        if (!nested) {
            logger.debug("Comenzando transacci�n para m�todo " + methodClass);
            TSLogger.getLogger(HibernateService.SQL_CATEGORY_LOG)
                .debug("********* Iniciando transacci�n para m�todo " + methodClass);
            try {
                transaction = ds.getCurrentSession().beginTransaction();
            } catch (HibernateException e) {
                throw new RuntimeException("Error al intentar abrir transacci�n", e);
            }
            logger.debug("Se inici� la transacci�n para el m�todo " + method.getName());
        } else {
        	TSLogger.getLogger(HibernateService.SQL_CATEGORY_LOG).debug("Transacci�n anidada para " + methodClass);
        }


    }

    private DataSource getDataSource(Method method, Annotation[] annotations) {
        TSLogger log = Log.getLogger(RequiredTransactionAspect.class);
        Transactional transactional = null;
        if (annotations != null) {
            for(Annotation annot : annotations) {
                if (Transactional.class.isAssignableFrom(annot.getClass())) {
                    transactional = (Transactional)annot;
                    break;
                }
            }
        }
        ds = DataSource.F911;
        if (transactional != null) {
            ds = transactional.value();
            log.info(String.format("El m�todo %s debe aplicar una transacci�n contra %s", method.getName(),
                        ds.toString()));
        }
        return ds;
    }

    @Override
	public void onAfterMethod(Method method, Object[] args, Annotation[] annotations) {
        TSLogger logger = Log.getLogger(RequiredTransactionAspect.class);
        if (!nested) {
            logger.debug("Confirmando transacci�n para metodo " + method.getName() + " ----- " + method.getDeclaringClass());
            try {
                if (transaction.isActive())
                    transaction.commit();
                else
                    logger.warn("Se intent� hacer commit de una transacci�n no activa. M�todo " + method.getName());
                TSLogger.getLogger(HibernateService.SQL_CATEGORY_LOG)
                    .debug("--------- Commiteando transacci�n para m�todo " + method.getName() +
                    		" --------- ("+ (System.currentTimeMillis() - start) + "ms)");
            } catch (HibernateException e) {
                throw new RuntimeException("Error al intentar confirmar transacci�n en el m�todo " + method, e);
            }
        } else {
        	TSLogger.getLogger(HibernateService.SQL_CATEGORY_LOG).debug("Transacci�n anidada para commit en: " + method.getName());
        }

    }

    @Override
	public void onException(Method method, Exception e, Object[] args) {
        TSLogger logger = Log.getLogger(RequiredTransactionAspect.class);
        if (!nested) {
        	logger.error("Cancelando transacci�n para metodo " + method.getName());
        	logger.error("Haciendo rollback", e);
            try {
                if (transaction.isActive()) {
                    transaction.rollback();
                    logger.warn("Se hizo rollback de la transacci�n para el m�todo " + method.getName());
                } else {
                    logger.warn("Se intent� hacer rollback de una transacci�n no activa. M�todo " + method.getName());
                    logger.warn("�Fue rollbacked? " + transaction.wasRolledBack());
                }
            } catch (HibernateException he) {
                throw new RuntimeException("Error al intentar cancelar transacci�n", he);
            }
        }
    }

    /**
     * Cierro la sesi�n obtenida en el m�todo onBeforeMethod
     */
    @Override
	public void onFinally(Method method, Object[] args) {
    	if (!nested) {
    		ds.close();
    	}
    }

    @Override
	public Object clone() {
		Aspect aspect = new RequiredTransactionAspect();
		aspect.setLayerNumber(getLayerNumber());
		return aspect;
    }
}
