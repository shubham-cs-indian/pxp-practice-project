package com.cs.core.config.interactor.model.dashboardtabs;

public class GetAllDashboardTabsRequestModel implements IGetAllDashboardTabsRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          currentUserId;
  protected String          physicalCatalogId;
  
  @Override
  public String getCurrentUserId()
  {
    return currentUserId;
  }
  
  @Override
  public void setCurrentUserId(String currentUserId)
  {
    this.currentUserId = currentUserId;
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
}
