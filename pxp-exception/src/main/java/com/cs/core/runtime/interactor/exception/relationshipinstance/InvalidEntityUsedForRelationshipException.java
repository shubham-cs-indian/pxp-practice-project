package com.cs.core.runtime.interactor.exception.relationshipinstance;


public class InvalidEntityUsedForRelationshipException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  
  
  public InvalidEntityUsedForRelationshipException()
  {
    super();
  }
  
  public InvalidEntityUsedForRelationshipException(Exception e)
  {
    super(e);
  }
}
