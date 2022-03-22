package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetConfigDetailsForExportRequestModel extends IModel {
  
  public static final String ENDPOINT_ID         = "endpointId";
  public static final String KLASS_IDS           = "klassIds";
  public static final String TAXONOMY_IDS        = "taxonomyIds";
  public static final String ORAGANIZATION_ID    = "organizationId";
  public static final String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
}
