package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDetermineSidesForRolledbackContentRequestModel extends IModel {
  
  String RELATIONSHIP_SIDE1_ID = "relationshipSide1Id";
  String ROLLEDBACK_CONTENT_ID = "rolledbackContentId";
  String ADDED_CONTENT_ID      = "addedContentId";
  
  public String getRelationshipSide1Id();
  
  public void setRelationshipSide1Id(String relationshipSide1Id);
  
  public String getRolledbackContentId();
  
  public void setRolledbackContentId(String rolledbackContentId);
  
  public String getAddedContentId();
  
  public void setAddedContentId(String addedContentId);
}
