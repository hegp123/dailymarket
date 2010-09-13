package ar.com.tsoluciones.arcom.pagination;

import java.io.Serializable;
import java.lang.reflect.Array;

/**
 * Copyright (c) Telef�nica Soluciones
 * Todos los derechos reservados.
 */

/**
 * <p>Dado un Object[] completa (con todos sus items), esta clase realiza la paginacion de la misma</p>
 *
 * @author aclocchiatti, despada
 * @version 1.0, Mar 10, 2005, 7:58:27 AM
 */
public final class CompleteSetPager implements Serializable
{
  private final Object[] objectArray; // Coleccion de objetos
  private final int pages; // Cantidad de p�ginas.
  private final int pageSize; // Tama�o de la p�gina.

  private int currentPageNumber = 1; // P�gina actual.
  /**
   * Construye un Pager con un Object[] inicial y un tama�o de pagina.
   * @param objectArray Arreglo a paginar.
   * @param pageSize Tama�o de la pagina.
   */

  public CompleteSetPager(Object[] objectArray, int pageSize) {
    if (objectArray == null) {
      throw new IllegalArgumentException(
          "No puede enviar un array de objetos en null como argumento");
    }

    if (pageSize <= 0) {
      throw new IllegalArgumentException(
          "pageSize es menor o igual a cero, esto es incorrecto");
    }

    this.objectArray = objectArray;
    this.pageSize = pageSize;

    // Setear al tama�o de las paginas
    int pages = this.objectArray.length / pageSize;

    // Caso de ultima pagina incompleta
    if ( (objectArray.length % pageSize) != 0) {
      pages++;

    }
    this.pages = pages;
  }

  /**
   * Devuelve la cantidad de p�ginas en el Object[] .
   *
   * @return Cant. de pag.
   */
  public int getPages() {
    return this.pages;
  }

  /**
   * Devuelve true si hay una pr�xima pagina.
   *
   * @return true o false.
   */
  public boolean getHasNext() {
    if (currentPageNumber < pages) {
      return true;
    }

    return false;
  }

  /**
   * Devuelve true si hay una pagina previa.
   *
   * @return true o false.
   */
  public boolean getHasPrevious() {
    if (currentPageNumber > 1) {
      return true;
    }

    return false;
  }

  /**
   * Devuelve la pr�xima p�gina.
   *
   * @return Object[].
   */
  public Object[] getNextPage() {
    currentPageNumber++;
    return getPage(currentPageNumber);
  }

  /**
   * Devuelve la p�gina anterior.
   *
   * @return Object[].
   */
  public Object[] getPreviousPage() {
    currentPageNumber--;
    return getPage(currentPageNumber);
  }

  /**
   * Retorna una Object[], correspondiente a la p�gina actual.
   *
   * @param pageNumber Numero de p�gina.
   * @return La Object[].
   */
  public Object[] getPage(int pageNumber) {
    if (pages == 0) {
      throw new IllegalStateException(
          "El paginador no tiene cargados paginas de objetos");
    }

    if (pageNumber > this.pages) {
      throw new IllegalArgumentException(
          "La pagina pedida es mayor a la cantidad de paginas totales");
    }

    if (pageNumber < 1) {
      throw new IllegalArgumentException("La pagina pedida es menor a 1");
    }

    int begin = (pageNumber * pageSize) - pageSize;
    int end = begin + pageSize;

    if (end > objectArray.length) {
      end = objectArray.length;

    }
    assert(begin >= 0):"begin ha dado menor que 0, esto no es correcto";

    Object[] objectArray = (Object[]) Array.newInstance(this.objectArray[0].
        getClass(), end - begin);

    for (int i = begin; i < end; i++) {
      objectArray[i - begin] = this.objectArray[i];

      // Guardar numero de pagina
    }
    currentPageNumber = pageNumber;

    return objectArray;
  }

  /**
   * Devuelve la �ltima p�gina.
   *
   * @return Object[].
   */
  public Object[] getLastPage() {
    currentPageNumber = pages;
    return getPage(pages - 1);
  }

  /**
   * Primer pagina.
   *
   * @return La Object[].
   */
  public Object[] getFirstPage() {
    currentPageNumber = 1;
    return getPage(1);
  }

  /**
   * Numero de pagina actual.
   *
   * @return Numero de pag.
   */
  public int getCurrentPageNumber() {
    return this.currentPageNumber;
  }

  /**
   * Devuelva una Object[] correspondiente a la p�gina actual.
   *
   * @return La Object[].
   */
  public Object[] getCurrentPage() {
    return getPage(currentPageNumber);
  }

  /**
   * Setea la pagina actual.
   *
   * @param currentPageNumber Pag. actual.
   */
  public void setCurrentPageNumber(int currentPageNumber) {
    if (currentPageNumber < 1 || currentPageNumber > pages) {
      throw new IllegalArgumentException(
          "La pagina que ha pedido esta fuera del intervalo permitido");
    }

    this.currentPageNumber = currentPageNumber;
  }

  /**
       * Hace un resize del tama�o de la pagina y devuelve el objeto con el nuevo size
   * @param pageSize Nuevo tama�o de pagina
   * @return Objeto con nuevo tama�o de pagina
   */
  public CompleteSetPager setPageSize(int pageSize) {
    return new CompleteSetPager(this.objectArray, pageSize);
  }
}
