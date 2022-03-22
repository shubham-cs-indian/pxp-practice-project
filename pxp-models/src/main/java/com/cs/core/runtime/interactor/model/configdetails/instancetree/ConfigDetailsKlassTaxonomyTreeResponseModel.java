package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigDetailsKlassTaxonomyTreeResponseModel extends GetKlassTaxonomyTreeResponseModel 
      implements IConfigDetailsKlassTaxonomyTreeResponseModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Set<String>     taxonomyIdsHavingRP;
  protected Set<String>     klassIdsHavingRP;
  protected List<String>    allowedEntities;
  protected List<String>    searchableAttributes;
  protected List<String>    translatableAttributes;
  protected List<String>    majorTaxonomyIds;
  protected Long            totalChildrenCount;

  
  @Override
  public Set<String> getKlassIdsHavingRP()
  {
    if (klassIdsHavingRP == null) {
      klassIdsHavingRP = new HashSet<>();
    }
    return klassIdsHavingRP;
  }
  
  @Override
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP)
  {
    this.klassIdsHavingRP = klassIdsHavingRP;
  }
  
  @Override
  public Set<String> getTaxonomyIdsHavingRP()
  {
    if(taxonomyIdsHavingRP == null)
    {
      taxonomyIdsHavingRP = new HashSet<>();
    }
    return taxonomyIdsHavingRP;
  }

  @Override
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP)
  {
    this.taxonomyIdsHavingRP = taxonomyIdsHavingRP;
  }
  
  @Override
  public List<String> getAllowedEntities()
  {
    if(allowedEntities == null)
    {
      allowedEntities = new ArrayList<String>();
    }
    return allowedEntities;
  }

  @Override
  public void setAllowedEntities(List<String> allowedEntities)
  {
    this.allowedEntities = allowedEntities;
  }
  
  @Override
  public List<String> getSearchableAttributeIds()
  {
    if(searchableAttributes == null) {
      searchableAttributes = new ArrayList<>();
    }
    return searchableAttributes;
  }
  
  @Override
  public void setSearchableAttributeIds(List<String> searchableAttributes)
  {
    this.searchableAttributes = searchableAttributes;
  }
  
  @Override
  public List<String> getTranslatableAttributeIds()
  {
    if(translatableAttributes == null) {
      translatableAttributes = new ArrayList<>();
    }
    return translatableAttributes;
  }
  
  @Override
  public void setTranslatableAttributeIds(List<String> translatableAttributes)
  {
    this.translatableAttributes = translatableAttributes;
  }
  
  @Override
  public List<String> getMajorTaxonomyIds() {
  if(majorTaxonomyIds == null) {
     majorTaxonomyIds = new ArrayList<String>();
  }
  return majorTaxonomyIds;
  }

  @Override
  public void setMajorTaxonomyIds(List<String> majorTaxonomyIds) {
  this.majorTaxonomyIds = majorTaxonomyIds;
  }

  @Override
  public Long getTotalChildrenCount()
  {
    return totalChildrenCount;
  }

  @Override
  public void setTotalChildrenCount(Long count)
  {
    this.totalChildrenCount = count;
  }
}
