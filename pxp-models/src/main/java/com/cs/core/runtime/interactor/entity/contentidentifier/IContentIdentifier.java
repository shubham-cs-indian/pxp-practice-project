package com.cs.core.runtime.interactor.entity.contentidentifier;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IContentIdentifier extends IEntity {
  
  public static final String ORGANIZATION_ID     = "organizationId";
  public static final String ENDPOINT_ID         = "endpointId";
  public static final String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  public static final String PORTAL_ID           = "portalId";
  public static final String LOGICAL_CATALOG_ID  = "logicalCatalogId";
  public static final String SYSTEM_ID           = "systemId";
  
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
