package com.cs.core.exception;


public class SSODomainMustNotBeEmptyException extends Exception{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public SSODomainMustNotBeEmptyException()
  {
    super();
  }
  
  public SSODomainMustNotBeEmptyException(Exception e)
  {
    super(e);
  }

  public SSODomainMustNotBeEmptyException(String message)
  {
    super(message);
  }
  
}
