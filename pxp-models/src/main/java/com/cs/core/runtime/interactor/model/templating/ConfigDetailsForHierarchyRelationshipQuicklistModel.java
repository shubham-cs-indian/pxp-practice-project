package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigDetailsForHierarchyRelationshipQuicklistModel
    implements IConfigDetailsForHierarchyRelationshipQuicklistModel {
  
  private static final long                         serialVersionUID = 1L;
  protected String                                  klassType;
  protected Set<String>                             entities;
  protected Set<String>                             taxonomyIdsHavingRP;
  protected List<IConfigEntityTreeInformationModel> categoryInfo;
  protected List<String>                            klassesIds;
  protected Set<String>                             allowedTypes;
  
  @Override
  public String getKlassType()
  {
    return klassType;
  }
  
  @Override
  public void setKlassType(String klassType)
  {
    this.klassType = klassType;
  }
  
  @Override
  public Set<String> getEntities()
  {
    if (entities == null) {
      entities = new HashSet<>();
    }
    return entities;
  }
  
  @Override
  public void setEntities(Set<String> entities)
  {
    this.entities = entities;
  }
  
  @Override
  public Set<String> getTaxonomyIdsHavingRP()
  {
    if (taxonomyIdsHavingRP == null) {
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
  @JsonDeserialize(contentAs = ConfigEntityTreeInformationModel.class)
  public List<IConfigEntityTreeInformationModel> getCategoryInfo()
  {
    if (categoryInfo == null) {
      categoryInfo = new ArrayList<>();
    }
    return categoryInfo;
  }
  
  @Override
  public void setCategoryInfo(List<IConfigEntityTreeInformationModel> categoryInfo)
  {
    this.categoryInfo = categoryInfo;
  }
  
  @Override
  public List<String> getKlassesIds()
  {
    if (klassesIds == null) {
      klassesIds = new ArrayList<>();
    }
    return klassesIds;
  }
  
  @Override
  public void setKlassesIds(List<String> klassesIds)
  {
    this.klassesIds = klassesIds;
  }
  
  @Override
  public Set<String> getAllowedTypes()
  {
    if (allowedTypes == null) {
      allowedTypes = new HashSet<>();
    }
    return allowedTypes;
  }
  
  @Override
  public void setAllowedTypes(Set<String> allowedTypes)
  {
    this.allowedTypes = allowedTypes;
  }
}
