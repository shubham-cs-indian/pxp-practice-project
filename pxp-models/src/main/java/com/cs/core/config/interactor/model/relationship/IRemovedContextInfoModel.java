package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRemovedContextInfoModel extends IModel {
  
  public static final String REMOVED_SIDE1_CONTEXTID = "removedSide1ContextId";
  public static final String REMOVED_SIDE2_CONTEXTID = "removedSide2ContextId";
  public static final String RELATIONSHIP_PROPERTYID = "relationshipPropertyId";
  
  public String getRemovedSide1ContextId();
  
  public void setRemovedSide1ContextId(String removedSide1ContextId);
  
  public String getRemovedSide2ContextId();
  
  public void setRemovedSide2ContextId(String removedSide2ContextId);
  
  public Long getRelationshipPropertyId();
  
  public void setRelationshipPropertyId(Long relationshipPropertyId);
}
