package com.cs.core.config.interactor.model.mapping;

import com.cs.core.config.interactor.entity.mapping.IMapping;

public interface IOutBoundMapping extends IMapping {
  
  public static final String IS_ALL_CLASSES_SELECTED             = "isAllClassesSelected";
  public static final String IS_ALL_PROPERTY_COLLECTION_SELECTED = "isAllPropertyCollectionSelected";
  public static final String IS_ALL_TAXONOMIES_SELECTED          = "isAllTaxonomiesSelected";
  public static final String IS_ALL_RELATIONSHIPS_SELECTED       = "isAllRelationshipsSelected";
  public static final String IS_ALL_CONTEXTS_SELECTED            = "isAllContextsSelected";
  
  public Boolean getIsAllClassesSelected();
  
  public void setIsAllClassesSelected(Boolean isAllClassesSelected);
  
  public Boolean getIsAllTaxonomiesSelected();
  
  public void setIsAllTaxonomiesSelected(Boolean isAllTaxonomiesSelected);
  
  public Boolean getIsAllPropertyCollectionSelected();
  
  public void setIsAllPropertyCollectionSelected(Boolean isAllPropertyCollectionSelected);
  
  public Boolean getIsAllRelationshipsSelected();
  
  public void setIsAllRelationshipsSelected(Boolean isAllRelationshipsSelected);

  public Boolean getIsAllContextsSelected();
  
  public void setIsAllContextsSelected(Boolean isAllContextsSelected);
}
