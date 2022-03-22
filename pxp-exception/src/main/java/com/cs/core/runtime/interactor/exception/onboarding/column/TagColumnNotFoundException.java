package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.VariantComponentColumnNotFoundException;

public class TagColumnNotFoundException extends VariantComponentColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public TagColumnNotFoundException()
  {
  }
  
  public TagColumnNotFoundException(TagColumnNotFoundException tagColumnNotFoundExceptions)
  {
    super();
  }
  
  public TagColumnNotFoundException(String message)
  {
    super(message);
  }
}
