package com.cs.core.runtime.interactor.model.targetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetTargetKlassesModel extends IModel {
  
  public static final String KLASS_ID         = "klassId";
  public static final String USER_ID          = "userId";
  public static final String RELATIONSHIP_ID  = "relationshipId";
  public static final String X_RAY_ATTRIBUTES = "xrayAttributes";
  public static final String X_RAY_TAGS       = "xrayTags";
  public static final String SIDE_ID          = "sideId";
  
  public String getKlassId();
  
  public void setKlassId(String klassId);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public List<String> getXRayAttributes();
  
  public void setXRayAttributes(List<String> xRayAttributes);
  
  public List<String> getXRayTags();
  
  public void setXRayTags(List<String> xRayTags);
  
  public String getSideId();
  
  public void setSideId(String sideId);
}
