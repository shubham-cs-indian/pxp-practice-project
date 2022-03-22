package com.cs.core.runtime.interactor.exception.onboarding.column;

public class ParentIdColumnNotFoundException extends PrimaryKeyColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ParentIdColumnNotFoundException()
  {
  }
  
  public ParentIdColumnNotFoundException(
      ParentIdColumnNotFoundException parentIdColumnNotFoundExceptions)
  {
    super();
  }
  
  public ParentIdColumnNotFoundException(String message)
  {
    super(message);
  }
}
