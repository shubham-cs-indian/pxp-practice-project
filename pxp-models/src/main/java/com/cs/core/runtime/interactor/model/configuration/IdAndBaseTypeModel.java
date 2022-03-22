package com.cs.core.runtime.interactor.model.configuration;

public class IdAndBaseTypeModel implements IIdAndBaseTypeModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          baseType;
  
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
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
}
