package com.cs.core.runtime.interactor.model.klassinstance;

public class DeleteInstanceDetails implements IDeleteInstanceDetails {
  
  protected String baseType;
  protected String id;
  protected String classifierCode;
  
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
  public String getClassifierCode()
  {
    return classifierCode;
  }
  
  @Override
  public void setClassifierCode(String classifierCode)
  {
    this.classifierCode = classifierCode;
  }
  
}
