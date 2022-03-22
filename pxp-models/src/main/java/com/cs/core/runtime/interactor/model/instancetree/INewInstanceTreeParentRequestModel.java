package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface INewInstanceTreeParentRequestModel extends IModel {
  
  public static final String FROM                        = "from";
  public static final String SIZE                        = "size";
  public static final String MODULE_ID                   = "moduleId";
  public static final String MODULE_ENTITIES             = "moduleEntities";
  public static final String IS_ARCHIVE_PORTAL           = "isArchivePortal";
  public static final String SELECTED_TYPES              = "selectedTypes";
  public static final String SELECTED_TAXONOMY_IDS       = "selectedTaxonomyIds";
  public static final String ALL_SEARCH                  = "allSearch";
  public static final String SEARCHABLE_ATTRIBUTE_IDS    = "searchableAttributeIds";

  
  public Integer getFrom();
  public void setFrom(Integer from);
  
  public Integer getSize();
  public void setSize(Integer size);
  
  public String getModuleId();
  public void setModuleId(String moduleId);
  
  public List<String> getModuleEntities();
  public void setModuleEntities(List<String> moduleEntities);
  
  public Boolean getIsArchivePortal();
  public void setIsArchivePortal(Boolean isArchivalPortal);
  
  public List<String> getSelectedTypes();
  public void setSelectedTypes(List<String> selectedTypes);
  
  public List<String> getSelectedTaxonomyIds();
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public String getAllSearch();
  public void setAllSearch(String allSearch);
  
  public List<String> getSearchableAttributeIds();
  public void setSearchableAttributeIds(List<String> searchableAttributes);
  
}
