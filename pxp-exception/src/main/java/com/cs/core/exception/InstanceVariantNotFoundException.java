package com.cs.core.exception;

public class InstanceVariantNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public InstanceVariantNotFoundException()
  {
  }
  
  public InstanceVariantNotFoundException(InstanceVariantNotFoundException e)
  {
    super(e);
  }
}
