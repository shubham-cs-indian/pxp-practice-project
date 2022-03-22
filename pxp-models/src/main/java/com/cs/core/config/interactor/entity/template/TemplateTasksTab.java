package com.cs.core.config.interactor.entity.template;

public class TemplateTasksTab extends AbstractTemplateTab implements ITemplateTasksTab {
  
  private static final long serialVersionUID = 1L;
  
  protected Integer         paginationSize;
  
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
