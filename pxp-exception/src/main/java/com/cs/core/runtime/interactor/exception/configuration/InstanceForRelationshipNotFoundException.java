package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.NotFoundException;

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
