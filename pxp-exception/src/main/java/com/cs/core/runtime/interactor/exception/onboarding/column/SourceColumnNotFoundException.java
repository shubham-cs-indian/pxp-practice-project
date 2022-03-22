package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.RelationshipComponentColumnNotFoundException;

public class SourceColumnNotFoundException extends RelationshipComponentColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public SourceColumnNotFoundException()
  {
  }
  
  public SourceColumnNotFoundException(SourceColumnNotFoundException sourceColumnNotFoundExceptions)
  {
    super();
  }
  
  public SourceColumnNotFoundException(String message)
  {
    super(message);
  }
}
