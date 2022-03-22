package com.cs.core.config.interactor.entity.template;

import java.util.ArrayList;
import java.util.List;

public class TemplateRelationshipTab extends AbstractTemplateTab
    implements ITemplateRelationshipTab {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    relationships    = new ArrayList<>();
  protected Integer         paginationSize;
  
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
