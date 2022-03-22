package com.cs.core.runtime.interactor.exception.process;

public class DiRelationshipTypeNotSupportedException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  public DiRelationshipTypeNotSupportedException()
  {
    super();
  }
  
  public DiRelationshipTypeNotSupportedException(Exception e)
  {
    super(e);
  }
  
  public DiRelationshipTypeNotSupportedException(String exceptionMsg)
  {
    super(exceptionMsg);
  }
}
