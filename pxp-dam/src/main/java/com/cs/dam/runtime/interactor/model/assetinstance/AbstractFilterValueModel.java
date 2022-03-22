package com.cs.dam.runtime.interactor.model.assetinstance;

public abstract class AbstractFilterValueModel implements IFilterValueModel {
  
  protected String id;
  protected String comparisonOperator;
  protected String baseType = this.getClass()
      .toString();
  protected String type;
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getComparisonOperator()
  {
    return this.comparisonOperator;
  }
  
  @Override
  public void setComparisonOperator(String comparisonOperator)
  {
    this.comparisonOperator = comparisonOperator;
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
  
  @Override
  public String getType()
  {
    return this.type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
}
