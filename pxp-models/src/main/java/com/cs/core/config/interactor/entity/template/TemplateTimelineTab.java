package com.cs.core.config.interactor.entity.template;

import java.util.ArrayList;
import java.util.List;

public class TemplateTimelineTab extends AbstractTemplateTab implements ITemplateTimelineTab {
  
  private static final long serialVersionUID    = 1L;
  protected List<String>    propertyCollections = new ArrayList<>();
  protected List<String>    natureRelationships = new ArrayList<>();
  protected Integer         paginationSize;
  protected List<String>    relationships       = new ArrayList<>();
  
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
  
  @Override
  public List<String> getRelationships()
  {
    return relationships;
  }
  
  @Override
  public void setRelationships(List<String> relationships)
  {
    this.relationships = relationships;
  }
}
