package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRelationshipIdSideIdModel extends IModel {
  
  public static final String SIDE_ID         = "sideId";
  public static final String RELATIONSHIP_ID = "relationshipId";
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
}
