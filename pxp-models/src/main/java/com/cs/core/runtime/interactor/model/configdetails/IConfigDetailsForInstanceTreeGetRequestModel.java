package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IConfigDetailsForInstanceTreeGetRequestModel extends IModel {
  
  public static final String USER_ID          = "userId";
  public static final String ALLOWED_ENTITIES = "allowedEntities";
  public static final String IS_CALENDAR_VIEW = "isCalendarView";
  public static final String X_RAY_ATTRIBUTES = "xrayAttributes";
  public static final String X_RAY_TAGS       = "xrayTags";
  public static final String KPI_ID           = "kpiId";
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public List<String> getAllowedEntities();
  
  public void setAllowedEntities(List<String> allowedEntities);
  
  public Boolean getIsCalendarView();
  
  public void setIsCalendarView(Boolean isCalendarView);
  
  public List<String> getXRayAttributes();
  
  public void setXRayAttributes(List<String> xRayAttributes);
  
  public List<String> getXRayTags();
  
  public void setXRayTags(List<String> xRayTags);
  
  public String getKpiId();
  
  public void setKpiId(String kpiId);
}
