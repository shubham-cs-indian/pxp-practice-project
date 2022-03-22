package com.cs.core.config.interactor.entity.template;

import java.util.List;

public interface ITemplateHomeTab extends ITemplateTab {
  
  public static final String PROPERTY_COLLECTIONS = "propertyCollections";
  public static final String NATURE_RELATIONSHIPS = "natureRelationships";
  public static final String PAGINATION_SIZE      = "paginationSize";
  
  public Integer getPaginationSize();
  
  public void setPaginationSize(Integer paginationSize);
  
  public List<String> getPropertyCollections();
  
  public void setPropertyCollections(List<String> propertyCollections);
  
  public List<String> getNatureRelationships();
  
  public void setNatureRelationships(List<String> natureRelationships);
}
