package com.cs.core.config.interactor.entity.mapping;

public interface IOutBoundMapping extends IMapping {
  
  public static final String IS_ALL_CLASSES_SELECTED             = "isAllClassesSelected";
  public static final String IS_ALL_PROPERTY_COLLECTION_SELECTED = "isAllPropertyCollectionSelected";
  public static final String IS_ALL_TAXONOMIES_SELECTED          = "isAllTaxonomiesSelected";
  
  public Boolean getIsAllClassesSelected();
  
  public void setIsAllClassesSelected(Boolean isAllClassesSelected);
  
  public Boolean getIsAllTaxonomiesSelected();
  
  public void setIsAllTaxonomiesSelected(Boolean isAllTaxonomiesSelected);
  
  public Boolean getIsAllPropertyCollectionSelected();
  
  public void setIsAllPropertyCollectionSelected(Boolean isAllPropertyCollectionSelected);
  
}
