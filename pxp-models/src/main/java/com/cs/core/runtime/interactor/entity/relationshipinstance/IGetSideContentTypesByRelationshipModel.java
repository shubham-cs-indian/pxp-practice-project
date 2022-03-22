package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetSideContentTypesByRelationshipModel extends IModel {
  
  public static final String CONTENT_ID      = "contentId";
  public static final String SIDE_ID         = "sideId";
  public static final String RELATIONSHIP_ID = "relationshipId";
  
  public Long getContentId();
  
  public void setContentId(Long contentId);
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
}
