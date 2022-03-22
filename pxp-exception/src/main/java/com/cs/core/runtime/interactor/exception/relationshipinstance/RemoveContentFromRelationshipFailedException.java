package com.cs.core.runtime.interactor.exception.relationshipinstance;

import com.cs.core.exception.PluginException;

public class RemoveContentFromRelationshipFailedException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public RemoveContentFromRelationshipFailedException()
  {
    super();
  }
  
  public RemoveContentFromRelationshipFailedException(PluginException e)
  {
    super(e);
  }
}
