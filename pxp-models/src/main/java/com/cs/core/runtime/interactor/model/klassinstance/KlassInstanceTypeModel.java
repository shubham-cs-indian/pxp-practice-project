package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.*;

public class KlassInstanceTypeModel implements IKlassInstanceTypeModel {
  
  private static final long           serialVersionUID       = 1L;
  protected Collection<String>        types;
  protected List<String>              taxonomyIds            = new ArrayList<>();
  protected List<String>              selectedTaxonomyIds    = new ArrayList<>();
  protected Map<String, List<String>> tagIdTagValueIdsMap    = new HashMap<>();
  protected String                    endpointId;
  protected String                    organizationId;
  protected String                    physicalCatalogId;
  protected List<String>              languageCodes;
  protected String                    baseType;
  protected List<String>              parentKlassIds;
  protected List<String>              parentTaxonomyIds;
  protected String                    name;
  
  @Override
  public Collection<String> getTypes()
  {
    if (types == null) {
      types = new HashSet<>();
    }
    return types;
  }
  
  @Override
  public void setTypes(Collection<String> types)
  {
    this.types = types;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    if (selectedTaxonomyIds == null) {
      selectedTaxonomyIds = new ArrayList<>();
    }
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
  }
  
  @Override
  public List<String> getParentKlassIds()
  {
    if (parentKlassIds == null) {
      parentKlassIds = new ArrayList<>();
    }
    return parentKlassIds;
  }
  
  @Override
  public void setParentKlassIds(List<String> parentKlassIds)
  {
    this.parentKlassIds = parentKlassIds;
  }
  
  @Override
  public List<String> getParentTaxonomyIds()
  {
    if (parentTaxonomyIds == null) {
      parentTaxonomyIds = new ArrayList<>();
    }
    return parentTaxonomyIds;
  }
  
  @Override
  public void setParentTaxonomyIds(List<String> parentTaxonomyIds)
  {
    this.parentTaxonomyIds = parentTaxonomyIds;
  }
  
  @Override
  public Map<String, List<String>> getTagIdTagValueIdsMap()
  {
    return tagIdTagValueIdsMap;
  }
  
  @Override
  public void setTagIdTagValueIdsMap(Map<String, List<String>> tagIdTagValueIdsMap)
  {
    this.tagIdTagValueIdsMap = tagIdTagValueIdsMap;
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
  public List<String> getLanguageCodes()
  {
    return languageCodes;
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
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
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
}
