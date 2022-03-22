package com.cs.core.config.interactor.entity.template;

public class TemplateEventsTab extends AbstractTemplateTab implements ITemplateEventsTab {
  
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
