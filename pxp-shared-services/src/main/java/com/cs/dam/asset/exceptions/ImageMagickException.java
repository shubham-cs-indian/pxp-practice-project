package com.cs.dam.asset.exceptions;

/**
 * This exception is for image conversion related exceptions.
 * @author pranav.huchche
 *
 */
public class ImageMagickException extends Exception {

  private static final long serialVersionUID = 1L;

  public ImageMagickException(Exception e) {
    super(e);
  }
  
  public ImageMagickException(String msg) {
    super(msg);
  }
}
