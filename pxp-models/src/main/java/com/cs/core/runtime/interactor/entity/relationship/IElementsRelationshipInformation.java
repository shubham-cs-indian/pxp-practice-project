package com.cs.core.runtime.interactor.entity.relationship;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IElementsRelationshipInformation extends IEntity {
  
  public static final String COMMON_RELATIONSHIP_INSTANCE_ID = "commonRelationshipInstanceId";
  public static final String RELATIONSHIP_OBJECT_ID          = "relationshipObjectId";
  
  public String getCommonRelationshipInstanceId();
  
  public void setCommonRelationshipInstanceId(String commonRelationshipInstanceId);
  
  public String getRelationshipObjectId();
  
  public void setRelationshipObjectId(String relationshipObjectId);
}
