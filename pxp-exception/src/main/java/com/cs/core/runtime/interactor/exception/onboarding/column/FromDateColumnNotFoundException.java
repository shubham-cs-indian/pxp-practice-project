package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.VariantComponentColumnNotFoundException;

public class FromDateColumnNotFoundException extends VariantComponentColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public FromDateColumnNotFoundException()
  {
  }
  
  public FromDateColumnNotFoundException(
      FromDateColumnNotFoundException fromDateColumnNotFoundExceptions)
  {
    super();
  }
  
  public FromDateColumnNotFoundException(String message)
  {
    super(message);
  }
}
