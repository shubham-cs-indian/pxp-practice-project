package com.cs.core.runtime.interactor.exception.configuration;

public class CSException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  public CSException()
  {
    super();
  }
  
  public CSException(Throwable e)
  {
    super(e);
  }
  
  public CSException(String message)
  {
    super(message);
  }
  
  public CSException(String message, Exception e)
  {
    super(message, e);
  }
}
