package com.cs.core.runtime.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

public class ConfigDetailsForInstanceTreeGetRequestModel
    implements IConfigDetailsForInstanceTreeGetRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          userId;
  protected List<String>    allowedEntities;
  protected Boolean         isCalendarView   = false;
  protected List<String>    xRayAttributes   = new ArrayList<>();
  protected List<String>    xRayTags         = new ArrayList<>();
  protected String          kpiId;
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public List<String> getAllowedEntities()
  {
    return allowedEntities;
  }
  
  @Override
  public void setAllowedEntities(List<String> allowedEntities)
  {
    this.allowedEntities = allowedEntities;
  }
  
  @Override
  public Boolean getIsCalendarView()
  {
    return isCalendarView;
  }
  
  @Override
  public void setIsCalendarView(Boolean isCalendarView)
  {
    this.isCalendarView = isCalendarView;
  }
  
  @Override
  public List<String> getXRayAttributes()
  {
    return xRayAttributes;
  }
  
  @Override
  public void setXRayAttributes(List<String> xRayAttributes)
  {
    this.xRayAttributes = xRayAttributes;
  }
  
  @Override
  public List<String> getXRayTags()
  {
    return xRayTags;
  }
  
  @Override
  public void setXRayTags(List<String> xRayTags)
  {
    this.xRayTags = xRayTags;
  }
  
  @Override
  public String getKpiId()
  {
    return kpiId;
  }
  
  @Override
  public void setKpiId(String kpiId)
  {
    this.kpiId = kpiId;
  }
}
