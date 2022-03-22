package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.instancetree.IGetBookmarkRequestModel;

public interface IDynamicCollectionModel extends ICollectionModel {
  
  public static final String GET_REQUEST_MODEL   = "getRequestModel";
  public static final String ORGANIZATION_ID     = "organizationId";
  public static final String ENDPOINT_ID         = "endpointId";
  public static final String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  public static final String PORTAL_ID           = "portalId";
  public static final String LOGICAL_CATALOG_ID  = "logicalCatalogId";
  public static final String SYSTEM_ID           = "systemId";
  
  public IGetBookmarkRequestModel getGetRequestModel();
  
  public void setGetRequestModel(IGetBookmarkRequestModel getRequestModel);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getPortalId();
  
  public void setPortalId(String portalId);
  
  public String getLogicalCatalogId();
  
  public void setLogicalCatalogId(String logicalCatalogId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
}
