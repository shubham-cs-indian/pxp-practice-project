package com.cs.core.runtime.interactor.exception.process;

public class JmsComponentException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  public JmsComponentException()
  {
    super();
  }
  
  public JmsComponentException(Exception e)
  {
    super(e);
  }
  
  public JmsComponentException(String exceptionMsg)
  {
    super(exceptionMsg);
  }
}
