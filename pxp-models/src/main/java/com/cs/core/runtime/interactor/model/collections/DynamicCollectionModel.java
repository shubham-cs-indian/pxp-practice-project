package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.instancetree.GetBookmarkRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetBookmarkRequestModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DynamicCollectionModel extends CollectionModel implements IDynamicCollectionModel {
  
  private static final long          serialVersionUID = 1L;
  protected IGetBookmarkRequestModel getRequestModel;
  protected String                   organizationId;
  protected String                   physicalCatalogId;
  protected String                   portalId;
  protected String                   logicalCatalogId;
  protected String                   systemId;
  protected String                   endpointId;
  
  @Override
  public IGetBookmarkRequestModel getGetRequestModel()
  {
    return getRequestModel;
  }
  
  @JsonDeserialize(as = GetBookmarkRequestModel.class)
  @Override
  public void setGetRequestModel(IGetBookmarkRequestModel getRequestModel)
  {
    this.getRequestModel = getRequestModel;
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
  
  @Override
  public String getSystemId()
  {
    return systemId;
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
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
  
  @Override
  public String getLogicalCatalogId()
  {
    return logicalCatalogId;
  }
  
  @Override
  public void setLogicalCatalogId(String logicalCatalogId)
  {
    this.logicalCatalogId = logicalCatalogId;
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
  public String getPortalId()
  {
    return portalId;
  }
  
  @Override
  public void setPortalId(String portalId)
  {
    this.portalId = portalId;
  }
}
