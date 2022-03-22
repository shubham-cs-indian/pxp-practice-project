package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.model.configdetails.IIdPaginationModel;

public class IdPaginationModel implements IIdPaginationModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected Long            from;
  protected Long            size; 
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
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