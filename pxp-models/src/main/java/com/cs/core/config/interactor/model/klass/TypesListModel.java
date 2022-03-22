package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

public class TypesListModel implements ITypesListModel {
  
  private static final long serialVersionUID          = 1L;
  
  protected List<String>    klassIds                  = new ArrayList<>();
  protected List<String>    taxonomyIds               = new ArrayList<>();
  protected List<String>    collectionIds             = new ArrayList<>();
  protected Boolean         shouldSearchOnTaxonomyIds = false;
  protected String          endpointId;
  protected String          organizationId;
  protected String          physicalCatalogId;
  protected String          portalId;
  protected List<String>    languageCodes;
  
  @Override
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public List<String> getCollectionIds()
  {
    return collectionIds;
  }
  
  @Override
  public void setCollectionIds(List<String> collectionIds)
  {
    this.collectionIds = collectionIds;
  }
  
  @Override
  public Boolean getShouldSearchOnTaxonomyIds()
  {
    return shouldSearchOnTaxonomyIds;
  }
  
  @Override
  public void setShouldSearchOnTaxonomyIds(Boolean shouldSearchOnTaxonomyIds)
  {
    this.shouldSearchOnTaxonomyIds = shouldSearchOnTaxonomyIds;
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
  public List<String> getLanguageCodes()
  {
    if (languageCodes == null) {
      languageCodes = new ArrayList<>();
    }
    return languageCodes;
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
}
