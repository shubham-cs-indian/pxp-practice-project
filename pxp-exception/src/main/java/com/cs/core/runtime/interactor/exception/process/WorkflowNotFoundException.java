package com.cs.core.runtime.interactor.exception.process;

public class WorkflowNotFoundException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  public WorkflowNotFoundException()
  {
    super();
  }
  
  public WorkflowNotFoundException(Exception e)
  {
    super(e);
  }
  
  public WorkflowNotFoundException(String exceptionMsg)
  {
    super(exceptionMsg);
  }
}
