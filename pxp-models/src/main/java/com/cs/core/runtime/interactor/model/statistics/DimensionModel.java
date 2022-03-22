package com.cs.core.runtime.interactor.model.statistics;

public class DimensionModel implements IDimensionModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected Double          value;
  
  public DimensionModel()
  {
    
  }
  
  public DimensionModel(String id, Double value)
  {
    this.id = id;
    this.value = value;
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
  public Double getValue()
  {
    return value;
  }
  
  @Override
  public void setValue(Double value)
  {
    this.value = value;
  }
}
