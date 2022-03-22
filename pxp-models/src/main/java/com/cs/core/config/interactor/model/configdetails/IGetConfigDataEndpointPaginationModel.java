package com.cs.core.config.interactor.model.configdetails;

public interface IGetConfigDataEndpointPaginationModel  extends IGetConfigDataEntityPaginationModel {
  
  public static final String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  public static final String ORGANIZATION_ID     = "organizationId";
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);

  public String getOrganizationId();

  public void setOrganizationId(String organizationId);
}
