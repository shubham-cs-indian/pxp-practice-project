package com.cs.core.runtime.interactor.model.dynamichierarchy;

public class IdAndNameModel implements IIdAndNameModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          name;
  
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
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
}
