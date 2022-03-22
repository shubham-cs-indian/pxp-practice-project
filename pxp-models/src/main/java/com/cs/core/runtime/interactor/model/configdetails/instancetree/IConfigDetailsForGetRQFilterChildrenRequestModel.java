package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;

public interface IConfigDetailsForGetRQFilterChildrenRequestModel extends IConfigDetailsForGetFilterChildrenRequestModel {
  
  public static final String TYPE             = "type";
  public static final String RELATIONSHIP_ID  = "relationshipId";
  public static final String SIDE_ID          = "sideId";
  public static final String TARGET_ID        = "targetId";
  
  public String getType();
  public void setType(String type);
  
  public String getSideId();
  public void setSideId(String sideId);
  
  public String getRelationshipId();
  public void setRelationshipId(String relationshipId);
  
  public String getTargetId();
  public void setTargetId(String targetId);
  
}
