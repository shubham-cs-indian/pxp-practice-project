package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.base.interactor.model.ModuleEntitiesWithUserIdModel;

public class ConfigDetailsForGetKlassTaxonomyTreeRequestModel extends ModuleEntitiesWithUserIdModel
      implements IConfigDetailsForGetKlassTaxonomyTreeRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          kpiId;
  protected List<String>    selectedTaxonomyIds;
  protected String          parentTaxonomyId;
  protected String          selectedCategory;
  protected int             from;
  protected int             size;
  protected String          searchText;
  protected String          moduleId;
  protected List<String>    moduleEntities;
  
  @Override
  public String getKpiId()
  {
    return kpiId;
  }

  @Override
  public void setKpiId(String kpiId)
  {
    this.kpiId = kpiId;
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
  public String getParentTaxonomyId()
  {
    return parentTaxonomyId;
  }

  @Override
  public void setParentTaxonomyId(String parentTaxonomyId)
  {
    this.parentTaxonomyId = parentTaxonomyId;
  }

  
  public String getSelectedCategory()
  {
    return selectedCategory;
  }

  
  public void setSelectedCategory(String selectedCategory)
  {
    this.selectedCategory = selectedCategory;
  }

  @Override
  public Integer getFrom()
  {
    return from;
  }

  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }

  @Override
  public Integer getSize()
  {
    return size;
  }

  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }

  @Override
  public String getSearchText()
  {
    return searchText;
  }

  @Override
  public void setSearchText(String searchText)
  {
    this.searchText = searchText;
  }

  @Override
  public String getModuleId()
  {
    return moduleId;
  }

  @Override
  public void setModuleId(String moduleId)
  {
    this.moduleId = moduleId;
  }

  @Override
  public List<String> getModuleEntities()
  {
    return moduleEntities;
  }

  @Override
  public void setModuleEntities(List<String> moduleEntites)
  {
    this.moduleEntities = moduleEntites;
  }
  
}
