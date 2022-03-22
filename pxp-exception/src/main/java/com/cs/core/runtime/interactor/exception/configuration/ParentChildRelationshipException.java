package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.NotFoundException;

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
