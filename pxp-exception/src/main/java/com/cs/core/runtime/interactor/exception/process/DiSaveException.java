package com.cs.core.runtime.interactor.exception.process;

public class DiSaveException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  public DiSaveException()
  {
    super();
  }
  
  public DiSaveException(Exception e)
  {
    super(e);
  }
  
  public DiSaveException(String exceptionMsg)
  {
    super(exceptionMsg);
  }
}
