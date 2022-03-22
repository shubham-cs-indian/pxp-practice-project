package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.VariantComponentColumnNotFoundException;

public class ParentVariantColumnNotFoundException extends VariantComponentColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ParentVariantColumnNotFoundException()
  {
  }
  
  public ParentVariantColumnNotFoundException(
      ParentVariantColumnNotFoundException parentVariantColumnNotFoundException)
  {
    super();
  }
  
  public ParentVariantColumnNotFoundException(String message)
  {
    super(message);
  }
}
