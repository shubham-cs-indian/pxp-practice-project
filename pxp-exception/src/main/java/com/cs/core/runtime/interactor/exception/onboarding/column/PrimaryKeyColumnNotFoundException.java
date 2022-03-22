package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.ColumnNotFoundException;

public class PrimaryKeyColumnNotFoundException extends ColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public PrimaryKeyColumnNotFoundException()
  {
  }
  
  public PrimaryKeyColumnNotFoundException(
      PrimaryKeyColumnNotFoundException primaryKeyColumnNotFoundExceptions)
  {
    super();
  }
  
  public PrimaryKeyColumnNotFoundException(String message)
  {
    super(message);
  }
}
