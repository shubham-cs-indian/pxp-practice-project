package com.cs.core.exception;

public class ParentNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ParentNotFoundException()
  {
  }
  
  public ParentNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
