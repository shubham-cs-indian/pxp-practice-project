package com.cs.core.config.interactor.exception.relationship;

import com.cs.core.exception.NotFoundException;

public class RelationshipNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public RelationshipNotFoundException()
  {
  }
  
  public RelationshipNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
