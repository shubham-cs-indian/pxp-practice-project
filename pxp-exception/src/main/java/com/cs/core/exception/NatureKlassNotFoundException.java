package com.cs.core.exception;

public class NatureKlassNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public NatureKlassNotFoundException()
  {
  }
  
  public NatureKlassNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
