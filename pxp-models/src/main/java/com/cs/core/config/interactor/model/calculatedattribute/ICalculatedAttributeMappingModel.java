package com.cs.core.config.interactor.model.calculatedattribute;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ICalculatedAttributeMappingModel extends IModel {
  
  public static final String MAPPING = "mapping";
  
  public Map<String, Object> getMapping();
  
  public void setMapping(Map<String, Object> mapping);
}
