package com.cs.core.runtime.interactor.exception.exporttoexcel;

public class UnauthorizedNatureKlassException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;
  
  public UnauthorizedNatureKlassException()
  {
  }
  
  public UnauthorizedNatureKlassException(UnauthorizedNatureKlassException e)
  {
    super(e);
  }
  
  public UnauthorizedNatureKlassException(String message)
  {
    super(message);
  }
}
