package com.cs.core.runtime.interactor.exception.process;

public class TalendComponentException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  public TalendComponentException()
  {
    super();
  }
  
  public TalendComponentException(Exception e)
  {
    super(e);
  }
  
  public TalendComponentException(String exceptionMsg)
  {
    super(exceptionMsg);
  }
}
