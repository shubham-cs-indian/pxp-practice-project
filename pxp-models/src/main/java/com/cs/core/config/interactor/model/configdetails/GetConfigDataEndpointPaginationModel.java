package com.cs.core.config.interactor.model.configdetails;

public class GetConfigDataEndpointPaginationModel extends GetConfigDataEntityPaginationModel
    implements IGetConfigDataEndpointPaginationModel {
  
  private static final long serialVersionUID = 1L;
  protected String          physicalCatalogId;
  protected String          organizationId;
  
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

  @Override
  public String getOrganizationId()
  {
    return organizationId;
  }

  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  
}
