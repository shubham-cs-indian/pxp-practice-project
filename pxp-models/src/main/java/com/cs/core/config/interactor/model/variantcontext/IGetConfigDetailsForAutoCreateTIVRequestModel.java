package com.cs.core.config.interactor.model.variantcontext;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigDetailsForAutoCreateTIVRequestModel extends IModel{
  
  public static final String CONTEXT_IDS         = "contextIds";
  public static final String ORAGANIZATION_ID    = "organizationId";
  public static final String ENDPOINT_ID         = "endpointId";
  public static final String PORTAL_ID           = "portalId";
  public static final String BASE_TYPE           = "baseType";
  public static final String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  
  public List<String> getContextIds();
  public void setContextIds(List<String> contextIds);
  
  public String getOrganizationId();
  public void setOrganizationId(String organizationId);
  
  public String getEndpointId();
  public void setEndpointId(String endpointId);
  
  public String getPortalId();
  public void setPortalId(String portalId);
  
  public String getBaseType();
  public void setBaseType(String baseType);
  
  public String getPhysicalCatalogId();
  public void setPhysicalCatalogId(String physicalCatalogId);
  
}
