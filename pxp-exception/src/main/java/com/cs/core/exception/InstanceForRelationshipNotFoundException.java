package com.cs.core.exception;

public class InstanceForRelationshipNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public InstanceForRelationshipNotFoundException()
  {
    super();
  }
  
  public InstanceForRelationshipNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
