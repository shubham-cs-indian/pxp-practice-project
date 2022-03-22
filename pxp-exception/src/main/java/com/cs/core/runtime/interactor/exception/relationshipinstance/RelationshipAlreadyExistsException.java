package com.cs.core.runtime.interactor.exception.relationshipinstance;

import com.cs.core.runtime.interactor.exception.configuration.InstanceAlreadyExistsException;

public class RelationshipAlreadyExistsException extends InstanceAlreadyExistsException {
  
  private static final long serialVersionUID = 1L;
  
  public RelationshipAlreadyExistsException()
  {
    super();
  }
  
  public RelationshipAlreadyExistsException(InstanceAlreadyExistsException e)
  {
    super(e);
  }
}
