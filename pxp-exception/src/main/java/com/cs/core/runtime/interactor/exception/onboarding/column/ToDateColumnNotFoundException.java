package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.VariantComponentColumnNotFoundException;

public class ToDateColumnNotFoundException extends VariantComponentColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ToDateColumnNotFoundException()
  {
  }
  
  public ToDateColumnNotFoundException(ToDateColumnNotFoundException toDateColumnNotFoundExceptions)
  {
    super();
  }
  
  public ToDateColumnNotFoundException(String message)
  {
    super(message);
  }
}
