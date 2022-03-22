package com.cs.core.runtime.interactor.model.configdetails.instancetree;

public interface IConfigDetailsForRelationshipQuicklistRequestModel extends IGetConfigDetailsForGetNewInstanceTreeRequestModel {
  
  public static final String RELATIONSHIP_ID  = "relationshipId";
  public static final String SIDE_ID          = "sideId";
  public static final String TARGET_ID        = "targetId";
  
  public String getSideId();
  public void setSideId(String sideId);
  
  public String getRelationshipId();
  public void setRelationshipId(String relationshipId);
  
  public String getTargetId();
  public void setTargetId(String targetId);
  
}
