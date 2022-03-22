package com.cs.core.runtime.interactor.model.configdetails;

import java.util.List;

public interface IMulticlassificationRequestForRelationshipsModel
    extends IMulticlassificationRequestModel {
  
  public static final String X_RAY_ATTRIBUTES = "xrayAttributes";
  public static final String X_RAY_TAGS       = "xrayTags";
  public static final String SIDE_ID          = "sideId";
  
  public List<String> getXRayAttributes();
  
  public void setXRayAttributes(List<String> xRayAttributes);
  
  public List<String> getXRayTags();
  
  public void setXRayTags(List<String> xRayTags);
  
  public String getSideId();
  
  public void setSideId(String sideId);
}
