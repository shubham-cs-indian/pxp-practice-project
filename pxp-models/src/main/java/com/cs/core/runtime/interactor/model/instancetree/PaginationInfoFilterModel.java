package com.cs.core.runtime.interactor.model.instancetree;

public class PaginationInfoFilterModel implements IPaginationInfoFilterModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Integer         size             = 0;
  
  @Override
  public Integer getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }
  
}
