package com.cs.core.config.strategy.model.context;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigDetailsForLinkedVariantDuplicateCheckRequestModel extends IModel {
  
  public static final String CONTEXT_ID      = "contextId";
  public static final String RELATIONSHIP_ID = "relationshipId";
  
  public String getContextId();
  public void setContextId(String contextId);
  
  public String getRelationshipId();
  public void setRelationshipId(String relationshipId);
  
}
