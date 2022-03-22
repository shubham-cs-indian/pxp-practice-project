package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDataIntegrationRequestModel extends IModel {
  
  public static final String MODE             = "mode";
  public static final String ENDPOINT_ID      = "endpointId";
  public static final String DASHBOARD_TAB_ID = "dashboardTabId";
  public static final String USER_ID          = "userId";
  public static final String FROM             = "from";
  public static final String SIZE             = "size";
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getSize();
  
  public void setSize(Long size);
  
  public String getMode();
  
  public void setMode(String mode);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getDashboardTabId();
  
  public void setDashboardTabId(String dashboardTabId);
  
  public String getUserId();
  
  public void setUserId(String userId);
}
