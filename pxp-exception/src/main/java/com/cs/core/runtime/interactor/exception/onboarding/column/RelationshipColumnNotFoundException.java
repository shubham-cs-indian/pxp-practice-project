package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.RelationshipComponentColumnNotFoundException;

public class RelationshipColumnNotFoundException
    extends RelationshipComponentColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public RelationshipColumnNotFoundException()
  {
  }
  
  public RelationshipColumnNotFoundException(
      RelationshipColumnNotFoundException relationshipColumnNotFoundExceptions)
  {
    super();
  }
  
  public RelationshipColumnNotFoundException(String message)
  {
    super(message);
  }
}
