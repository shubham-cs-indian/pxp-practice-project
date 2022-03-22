package com.cs.core.exception;

public class PropertyCollectionNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public PropertyCollectionNotFoundException()
  {
  }
  
  public PropertyCollectionNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
