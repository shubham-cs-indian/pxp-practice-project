package com.cs.core.runtime.interactor.model.dashboard;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IDashboardRequestModel extends IModel {
  
  public String getDashboardName();
  
  public void setDashboardName(String dashboardName);
  
  public String getMode();
  
  public void setMode(String mode);
  
  public Map<String, Object> getProperties();
  
  public void setProperties(Map<String, Object> properties);
}
