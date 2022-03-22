package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.ColumnNotFoundException;

public class KlassColumnNotFoundException extends ColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public KlassColumnNotFoundException()
  {
  }
  
  public KlassColumnNotFoundException(KlassColumnNotFoundException klassColumnNotFoundExceptions)
  {
    super();
  }
  
  public KlassColumnNotFoundException(String message)
  {
    super(message);
  }
}
