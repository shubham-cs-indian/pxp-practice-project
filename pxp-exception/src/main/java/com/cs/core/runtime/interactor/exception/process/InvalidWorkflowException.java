package com.cs.core.runtime.interactor.exception.process;

public class InvalidWorkflowException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidWorkflowException()
  {
    super();
  }
  
  public InvalidWorkflowException(Exception e)
  {
    super(e);
  }
  
  public InvalidWorkflowException(String exceptionMsg)
  {
    super(exceptionMsg);
  }
}
