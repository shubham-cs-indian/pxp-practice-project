package com.cs.core.runtime.interactor.model.configdetails.instancetree;

public interface IConfigDetailsForRQFilterAndSortDataRequestModel extends IConfigDetailsForFilterAndSortInfoRequestModel {
  
  public static final String USER_ID          = "userId";
  public static final String TYPE             = "type";
  public static final String RELATIONSHIP_ID  = "relationshipId";
  public static final String SIDE_ID          = "sideId";
  public static final String TARGET_ID        = "targetId";
  
  public String getUserId();
  public void setUserId(String userId);
  
  public String getSideId();
  public void setSideId(String sideId);
  
  public String getRelationshipId();
  public void setRelationshipId(String relationshipId);
  
  public String getType();
  public void setType(String type);
  
  public String getTargetId();
  public void setTargetId(String targetId);
  
}
