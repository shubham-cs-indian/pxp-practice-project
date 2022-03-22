package com.cs.core.exception;


public class IDPMustNotBeEmptyException extends Exception{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public IDPMustNotBeEmptyException()
  {
    super();
  }
  
  public IDPMustNotBeEmptyException(Exception e)
  {
    super(e);
  }

  public IDPMustNotBeEmptyException(String message)
  {
    super(message);
  }
  
}
