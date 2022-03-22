package com.cs.core.runtime.interactor.exception.onboarding.notfound;

import com.cs.core.runtime.interactor.exception.onboarding.component.ComponentException;

public class ColumnNotFoundException extends ComponentException {
  
  private static final long serialVersionUID = 1L;
  
  public ColumnNotFoundException()
  {
  }
  
  public ColumnNotFoundException(ColumnNotFoundException columnNotFoundExceptions)
  {
    super();
  }
  
  public ColumnNotFoundException(String message)
  {
    super(message);
  }
}
