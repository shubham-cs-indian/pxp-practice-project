package com.cs.core.config.interactor.entity.template;

import java.util.List;

public interface ITemplateRelationshipTab extends ITemplateTab {
  
  public static final String RELATIONSHIPS   = "relationships";
  public static final String PAGINATION_SIZE = "paginationSize";
  
  public Integer getPaginationSize();
  
  public void setPaginationSize(Integer paginationSize);
  
  public List<String> getRelationships();
  
  public void setRelationships(List<String> relationships);
}
