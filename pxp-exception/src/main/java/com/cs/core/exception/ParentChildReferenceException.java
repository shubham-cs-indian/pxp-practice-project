package com.cs.core.exception;

public class ParentChildReferenceException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ParentChildReferenceException()
  {
  }
  
  public ParentChildReferenceException(NotFoundException e)
  {
    super(e);
  }
}
