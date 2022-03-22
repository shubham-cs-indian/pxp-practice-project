package com.cs.core.config.interactor.model.dashboardtabs;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAllDashboardTabsRequestModel extends IModel {
  
  public static String CURRENT_USER_ID     = "currentUserId";
  
  public static String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  
  public String getCurrentUserId();
  
  void setCurrentUserId(String currentUserId);
  
  String getPhysicalCatalogId();
  
  void setPhysicalCatalogId(String physicalCatalogId);
}
