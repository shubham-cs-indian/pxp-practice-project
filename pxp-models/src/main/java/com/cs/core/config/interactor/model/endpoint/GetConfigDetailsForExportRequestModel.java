package com.cs.core.config.interactor.model.endpoint;

import java.util.ArrayList;
import java.util.List;

public class GetConfigDetailsForExportRequestModel
    implements IGetConfigDetailsForExportRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          endpointId;
  protected List<String>    klassIds         = new ArrayList<>();
  protected List<String>    taxonomyIds      = new ArrayList<>();
  protected String          organizationId;
  protected String          physicalCatalogId;
  
  public String getEndpointId()
  {
    return endpointId;
  }
  
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  public List<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }
  
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
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
