package com.cs.core.runtime.interactor.exception.process;

public class DiCreateException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  public DiCreateException()
  {
    super();
  }
  
  public DiCreateException(Exception e)
  {
    super(e);
  }
  
  public DiCreateException(String exceptionMsg)
  {
    super(exceptionMsg);
  }
}
