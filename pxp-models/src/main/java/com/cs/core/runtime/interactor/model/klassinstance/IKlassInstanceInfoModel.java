package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IKlassInstanceInfoModel extends IModel {
  
  public static String KLASS_INSTANCE_ID     = "klassInstanceId";
  public static String BASETYPE              = "basetype";
  public static String TYPES                 = "types";
  public static String TAXONOMY_IDS          = "taxonomyIds";
  public static String SELECTED_TAXONOMY_IDS = "selectedTaxonomyIds";
  public static String ORGANISATION_ID       = "organisationId";
  public static String PHYSICAL_CATALOG_ID   = "physicalCatalogId";
  public static String ENDPOINT_ID           = "endpointId";
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getBasetype();
  
  public void setBasetype(String basetype);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getSelectedTaxonomyIds();
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public String getOrganisationId();
  
  public void setOrganisationId(String organisationId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
}
