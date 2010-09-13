package ar.com.tsoluciones.arcom.struts;

import ar.com.tsoluciones.arcom.cor.ServiceException;

/**
 * MappingException.java
 * Copyright (c) Telef�nica Soluciones
 * Todos los derechos reservados.
 */

/**
 * <p>
 * Excepcion lanzada cuando hay un problema de mapping. Hereda de ServiceException,
 * puede emplearse como validacion de negocio.
 * </p>
 *
 * @author despada
 * @version 1.0, Oct 19, 2004, 10:59:48 AM
 * @see MappingHelper
 * @since 1.1
 */
public class MappingException extends ServiceException
{
	String mappedValue;

	/**
	 * Construye un MappingException
	 * @param message Mensaje de la excepci�n
	 */
	public MappingException(String message)
	{
		super(message);
	}

	/**
	 * Construye un MappingException
	 * @param message Mensaje de la excepci�n
	 * @param mappedValue Valor que se intento mapear
	 */
	public MappingException(String message, String mappedValue)
	{
		super(message);
		this.mappedValue = mappedValue;
	}

	/**
	 * Construye un MappingException
	 * @param message Mensaje de la excepci�n
	 * @param e	Excepci�n ra�z
	 */
	public MappingException(String message, Throwable e)
	{
		super(message, e);
	}

	/**
	 * Construye un MappingException
	 * @param message Mensaje de la excepcion
	 * @param e Excepcion raiz
	 * @param mappedValue Valor que se intento mapear
	 */
	public MappingException(String message, Throwable e, String mappedValue)
	{
		super(message, e);
		this.mappedValue = mappedValue;
	}

	/**
	 * El valor que se intento mapear
	 * @return El valor que se intento mapear
	 */
	public String getMappedValue()
	{
		return mappedValue;
	}

}
