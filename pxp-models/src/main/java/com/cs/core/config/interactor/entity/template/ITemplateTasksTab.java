package com.cs.core.config.interactor.entity.template;

public interface ITemplateTasksTab extends ITemplateTab {
  
  public static final String PAGINATION_SIZE = "paginationSize";
  
  public Integer getPaginationSize();
  
  public void setPaginationSize(Integer paginationSize);
}
