package com.cs.core.exception;

public class KlassNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public KlassNotFoundException()
  {
  }
  
  public KlassNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
