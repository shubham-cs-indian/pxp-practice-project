package com.cs.core.runtime.interactor.exception.relationshipinstance;

import com.cs.core.runtime.interactor.exception.configuration.InstanceAlreadyExistsException;

public class InstanceAlreadyPresentInAnotherLinkedVariantRelationshipException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;
  
  public InstanceAlreadyPresentInAnotherLinkedVariantRelationshipException()
  {
    super();
  }
  
  public InstanceAlreadyPresentInAnotherLinkedVariantRelationshipException(InstanceAlreadyExistsException e)
  {
    super(e);
  }
}
