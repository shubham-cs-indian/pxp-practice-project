package com.cs.core.runtime.interactor.model.dashboard;

import java.util.HashMap;
import java.util.Map;

public class DashboardRequestModel implements IDashboardRequestModel {
  
  private String              dashboardName;
  private String              mode;
  private Map<String, Object> properties;
  
  @Override
  public String getDashboardName()
  {
    return dashboardName;
  }
  
  @Override
  public void setDashboardName(String dashboardName)
  {
    this.dashboardName = dashboardName;
  }
  
  @Override
  public String getMode()
  {
    return mode;
  }
  
  @Override
  public void setMode(String mode)
  {
    this.mode = mode;
  }
  
  @Override
  public Map<String, Object> getProperties()
  {
    if (properties == null) {
      properties = new HashMap<>();
    }
    return properties;
  }
  
  @Override
  public void setProperties(Map<String, Object> properties)
  {
    this.properties = properties;
  }
}
