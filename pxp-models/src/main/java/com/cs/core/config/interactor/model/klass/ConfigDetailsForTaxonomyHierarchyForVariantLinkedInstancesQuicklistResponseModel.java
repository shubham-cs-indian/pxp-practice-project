package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configdetails.CategoryInformationModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashSet;
import java.util.Set;

public class ConfigDetailsForTaxonomyHierarchyForVariantLinkedInstancesQuicklistResponseModel
    implements IConfigDetailsForTaxonomyHierarchyForVariantLinkedInstancesQuicklistResponseModel {
  
  private static final long           serialVersionUID    = 1L;
  
  protected ICategoryInformationModel categoryInfo;
  protected Set<String>               klassIdsHavingRP    = new HashSet<>();
  protected Set<String>               entities            = new HashSet<>();
  protected Set<String>               taxonomyIdsHavingRP = new HashSet<>();
  
  @Override
  public ICategoryInformationModel getCategoryInfo()
  {
    return categoryInfo;
  }
  
  @JsonDeserialize(as = CategoryInformationModel.class)
  @Override
  public void setCategoryInfo(ICategoryInformationModel categoryInfo)
  {
    this.categoryInfo = categoryInfo;
  }
  
  @Override
  public Set<String> getKlassIdsHavingRP()
  {
    return klassIdsHavingRP;
  }
  
  @Override
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP)
  {
    this.klassIdsHavingRP = klassIdsHavingRP;
  }
  
  @Override
  public Set<String> getEntities()
  {
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
    return taxonomyIdsHavingRP;
  }
  
  @Override
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP)
  {
    this.taxonomyIdsHavingRP = taxonomyIdsHavingRP;
  }
}
