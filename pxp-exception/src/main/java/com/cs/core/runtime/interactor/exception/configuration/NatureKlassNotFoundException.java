package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.NotFoundException;

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
