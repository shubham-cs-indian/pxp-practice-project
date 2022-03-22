package com.cs.core.config.interactor.entity.template;

import java.util.ArrayList;
import java.util.List;

public class TemplateHomeTab extends AbstractTemplateTab implements ITemplateHomeTab {
  
  private static final long serialVersionUID    = 1L;
  protected List<String>    propertyCollections = new ArrayList<>();
  protected List<String>    natureRelationships = new ArrayList<>();
  protected Integer         paginationSize;
  
  @Override
  public List<String> getPropertyCollections()
  {
    return propertyCollections;
  }
  
  @Override
  public void setPropertyCollections(List<String> propertyCollections)
  {
    this.propertyCollections = propertyCollections;
  }
  
  @Override
  public List<String> getNatureRelationships()
  {
    return natureRelationships;
  }
  
  @Override
  public void setNatureRelationships(List<String> natureRelationships)
  {
    this.natureRelationships = natureRelationships;
  }
  
  @Override
  public Integer getPaginationSize()
  {
    return paginationSize;
  }
  
  @Override
  public void setPaginationSize(Integer paginationSize)
  {
    this.paginationSize = paginationSize;
  }
}
