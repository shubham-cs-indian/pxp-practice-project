package com.cs.core.exception;

public class ParentChildRelationshipException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ParentChildRelationshipException()
  {
  }
  
  public ParentChildRelationshipException(NotFoundException e)
  {
    super(e);
  }
}
