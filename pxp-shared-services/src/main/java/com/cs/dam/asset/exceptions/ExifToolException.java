package com.cs.dam.asset.exceptions;

/**
 * This exception is for metadata extraction related exceptions.
 * @author pranav.huchche
 *
 */
public class ExifToolException extends Exception {

  private static final long serialVersionUID = 1L;

  public ExifToolException(Exception e) {
    super(e);
  }

  public ExifToolException(String msg) {
    super(msg);
  }
}
