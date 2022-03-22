package com.cs.core.runtime.interactor.model.instancetree;

public class PaginationInfoSortModel implements IPaginationInfoSortModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Long            from             = 0l;
  protected Long            size             = 0l;
  
  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
  
  @Override
  public Long getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Long size)
  {
    this.size = size;
  }
  
}
