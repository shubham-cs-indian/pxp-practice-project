package com.cs.dam.asset.exceptions;

/**
 * This exception is for video conversion related exceptions.
 * @author pranav.huchche
 *
 */
public class FFMPEGException extends Exception {

  private static final long serialVersionUID = 1L;

  public FFMPEGException(Exception e) {
    super(e);
  }

  public FFMPEGException(String msg) {
    super(msg);
  }
}
