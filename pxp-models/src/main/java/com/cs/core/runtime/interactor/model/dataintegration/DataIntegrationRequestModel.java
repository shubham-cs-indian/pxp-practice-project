package com.cs.core.runtime.interactor.model.dataintegration;

public class DataIntegrationRequestModel implements IDataIntegrationRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          mode;
  protected String          endpointId;
  protected String          userId;
  protected String          dashboardTabId;
  protected Long            from;
  protected Long            size;
  
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
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
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
  public String getDashboardTabId()
  {
    return dashboardTabId;
  }
  
  @Override
  public void setDashboardTabId(String dashboardTabId)
  {
    this.dashboardTabId = dashboardTabId;
  }
  
  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
  
  @Override
  public Long getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Long size)
  {
    this.size = size;
  }
}
