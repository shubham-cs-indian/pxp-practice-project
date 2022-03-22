package com.cs.core.config.interactor.entity.template;

public interface ITemplateEventsTab extends ITemplateTab {
  
  public static final String PAGINATION_SIZE = "paginationSize";
  
  public Integer getPaginationSize();
  
  public void setPaginationSize(Integer paginationSize);
}
