package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAllStatisticsRequestModel extends IModel {
  
  public static final String FROM                = "from";
  public static final String SIZE                = "size";
  public static final String DASHBOARD_TAB_ID    = "dashboardTabId";
  public static final String ORGANIZATION_ID     = "organizationId";
  public static final String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  public static final String PORTAL_ID           = "portalId";
  public static final String ENDPOINT_ID         = "endpointId";
  public static final String USER_ID             = "userId";
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getSize();
  
  public void setSize(Long size);
  
  public String getDashboardTabId();
  
  public void setDashboardTabId(String dashboardTabId);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getPortalId();
  
  public void setPortalId(String portalId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getUserId();
  
  public void setUserId(String userId);
}
