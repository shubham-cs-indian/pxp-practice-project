package com.cs.core.config.interactor.model.variantcontext;

import java.util.ArrayList;
import java.util.List;


public class GetConfigDetailsForAutoCreateTIVRequestModel
    implements IGetConfigDetailsForAutoCreateTIVRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  private List<String> contextIds = new ArrayList<>();
  private String       organizationId;
  private String       endpointId;
  private String       portalId;
  private String       baseType;
  private String       physicalCatalogId;
  
  @Override
  public List<String> getContextIds()
  {
    return contextIds;
  }
  
  @Override
  public void setContextIds(List<String> contextIds)
  {
    this.contextIds = contextIds;
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
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
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
