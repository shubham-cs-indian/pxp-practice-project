package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.ColumnNotFoundException;

public class SecondaryColumnNotFoundException extends ColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public SecondaryColumnNotFoundException()
  {
  }
  
  public SecondaryColumnNotFoundException(
      SecondaryColumnNotFoundException secondaryColumnNotFoundExceptions)
  {
    super();
  }
  
  public SecondaryColumnNotFoundException(String message)
  {
    super(message);
  }
}
