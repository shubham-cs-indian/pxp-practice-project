package com.cs.core.config.interactor.model.calculatedattribute;

import java.util.Map;

public class CalculatedAttributeMappingModel implements ICalculatedAttributeMappingModel {
  
  private static final long     serialVersionUID = 1L;
  protected Map<String, Object> mapping;
  
  public CalculatedAttributeMappingModel(Map<String, Object> mapping)
  {
    this.mapping = mapping;
  }
  
  @Override
  public Map<String, Object> getMapping()
  {
    return mapping;
  }
  
  @Override
  public void setMapping(Map<String, Object> mapping)
  {
    this.mapping = mapping;
  }
}
