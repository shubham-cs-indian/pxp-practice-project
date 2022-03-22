package com.cs.core.runtime.interactor.exception.onboarding.notfound;

public class RelationshipComponentColumnNotFoundException extends ColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public RelationshipComponentColumnNotFoundException()
  {
  }
  
  public RelationshipComponentColumnNotFoundException(
      RelationshipComponentColumnNotFoundException relationshipComponentExceptions)
  {
    super();
  }
  
  public RelationshipComponentColumnNotFoundException(String message)
  {
    super(message);
  }
}
