package com.cs.core.config.interactor.exception.relationship;

import com.cs.core.exception.NotFoundException;

public class RelationshipSectionNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public RelationshipSectionNotFoundException()
  {
  }
  
  public RelationshipSectionNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
