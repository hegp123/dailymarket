package ar.com.tsoluciones.arcom.hibernate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})

/**
 * Anotaci�n para demarcar m�todos de servicios que deben ser transaccionales.
 */
public @interface Transactional {
    DataSource value() default DataSource.F911;
}
