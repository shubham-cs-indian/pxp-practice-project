package com.cs.core.runtime.interactor.model.filter;

import java.util.List;

public interface IKlassInstanceRelationshipsFilterModel extends IKlassInstanceFilterModel {
  
  public static final String X_RAY_ATTRIBUTES       = "xrayAttributes";
  public static final String X_RAY_TAGS             = "xrayTags";
  public static final String IS_NATURE_RELATIONSHIP = "isNatureRelationship";
  public static final String SIDE_ID                = "sideId";
  
  public Boolean getIsNatureRelationship();
  
  public void setIsNatureRelationship(Boolean isNatureRelationship);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Boolean getIsLinked();
  
  public void setIsLinked(Boolean isLinked);
  
  public List<String> getXRayAttributes();
  
  public void setXRayAttributes(List<String> xRayAttributes);
  
  public List<String> getXRayTags();
  
  public void setXRayTags(List<String> xRayTags);
  
  public String getSideId();
  
  public void setSideId(String sideId);
}
