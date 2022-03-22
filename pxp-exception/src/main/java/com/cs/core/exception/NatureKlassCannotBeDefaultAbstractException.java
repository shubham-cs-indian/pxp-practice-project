package com.cs.core.exception;


public class NatureKlassCannotBeDefaultAbstractException extends Exception{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public NatureKlassCannotBeDefaultAbstractException()
  {
    super();
  }
  
  public NatureKlassCannotBeDefaultAbstractException(Exception e)
  {
    super(e);
  }

  public NatureKlassCannotBeDefaultAbstractException(String message)
  {
    super(message);
  }
  
}
