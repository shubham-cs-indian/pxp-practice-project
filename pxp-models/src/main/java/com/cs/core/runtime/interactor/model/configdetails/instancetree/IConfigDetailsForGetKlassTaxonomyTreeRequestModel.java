package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.List;

import com.cs.core.base.interactor.model.IModuleEntitiesWithUserIdModel;

public interface IConfigDetailsForGetKlassTaxonomyTreeRequestModel extends IModuleEntitiesWithUserIdModel {
  
  public static final String KPI_ID                = "kpiId";
  public static final String SELECTED_TAXONOMY_IDS = "selectedTaxonomyIds";
  public static final String PARENT_TAXONOMY_ID    = "parentTaxonomyId";
  public static final String SELECTED_CATEGORY     = "selectedCategory";
  public static final String FROM                  = "from";
  public static final String SIZE                  = "size";
  public static final String SEARCH_TEXT           = "searchText";
  public static final String MODULE_ID             = "moduleId";
  public static final String MODULE_ENTITIES       = "moduleEntities";
  
  
  public String getKpiId();
  public void setKpiId(String kpiId);
  
  public List<String> getSelectedTaxonomyIds();
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public String getParentTaxonomyId();
  public void setParentTaxonomyId(String parentTaxonomyId);
  
  public String getSelectedCategory();
  public void setSelectedCategory(String selectedType);
  
  public Integer getFrom();
  public void setFrom(Integer from);
  
  public Integer getSize();
  public void setSize(Integer size);
  
  public String getSearchText();
  public void setSearchText(String allSearch);
  
  public String getModuleId();
  public void setModuleId(String moduleId);
  
  public List<String> getModuleEntities();
  public void setModuleEntities(List<String> moduleEntites);
}
