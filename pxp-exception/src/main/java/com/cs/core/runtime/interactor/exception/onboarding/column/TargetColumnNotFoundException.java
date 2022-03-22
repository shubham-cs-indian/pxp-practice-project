package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.RelationshipComponentColumnNotFoundException;

public class TargetColumnNotFoundException extends RelationshipComponentColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public TargetColumnNotFoundException()
  {
  }
  
  public TargetColumnNotFoundException(TargetColumnNotFoundException targetColumnNotFoundExceptions)
  {
    super();
  }
  
  public TargetColumnNotFoundException(String message)
  {
    super(message);
  }
}
