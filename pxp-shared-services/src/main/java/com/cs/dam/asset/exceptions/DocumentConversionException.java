package com.cs.dam.asset.exceptions;

/**
 * This exception is for document conversion related exceptions.
 * @author pranav.huchche
 *
 */
public class DocumentConversionException extends Exception {

  private static final long serialVersionUID = 1L;

  public DocumentConversionException(Exception e) {
    super(e);
  }
  
  public DocumentConversionException(String e) {
    super(e);
  }
}
