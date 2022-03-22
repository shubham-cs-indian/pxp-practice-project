package com.cs.core.exception;


public class InvalidSSOIDPTypeException extends Exception{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public InvalidSSOIDPTypeException()
  {
    super();
  }
  
  public InvalidSSOIDPTypeException(Exception e)
  {
    super(e);
  }

  public InvalidSSOIDPTypeException(String message)
  {
    super(message);
  }
  
}
