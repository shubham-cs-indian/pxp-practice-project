package com.cs.core.runtime.interactor.model.collections;

public class CollectionRequestModel implements ICollectionRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected int             from;
  protected int             size;
  
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
  public int getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(int from)
  {
    this.from = from;
  }
  
  @Override
  public int getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(int size)
  {
    this.size = size;
  }
}
