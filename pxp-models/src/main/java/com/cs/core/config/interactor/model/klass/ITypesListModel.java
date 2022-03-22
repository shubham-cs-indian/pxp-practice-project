package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ITypesListModel extends IModel {
  
  public static final String KLASS_IDS                     = "klassIds";
  public static final String TAXONOMY_IDS                  = "taxonomyIds";
  public static final String COLLECTION_IDS                = "collectionIds";
  public static final String SHOULD_SEARCH_ON_TAXONOMY_IDS = "shouldSearchOnTaxonomyIds";
  public static final String ORGANIZATION_ID               = "organizationId";
  public static final String ENDPOINT_ID                   = "endpointId";
  public static final String PHYSICAL_CATALOG_ID           = "physicalCatalogId";
  public static final String PORTAL_ID                     = "portalId";
  public static final String LANGUAGE_CODES                = "languageCodes";
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getCollectionIds();
  
  public void setCollectionIds(List<String> collectionIds);
  
  public Boolean getShouldSearchOnTaxonomyIds();
  
  public void setShouldSearchOnTaxonomyIds(Boolean shouldSearchOnTaxonomyIds);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getPortalId();
  
  public void setPortalId(String portalId);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
}
