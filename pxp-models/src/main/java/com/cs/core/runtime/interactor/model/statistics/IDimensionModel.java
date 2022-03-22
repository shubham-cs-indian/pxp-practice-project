package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDimensionModel extends IModel {
  
  public static final String ID    = "id";
  public static final String VALUE = "value";
  
  public String getId();
  
  public void setId(String id);
  
  public Double getValue();
  
  public void setValue(Double value);
}
