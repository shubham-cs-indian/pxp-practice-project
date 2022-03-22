package com.cs.core.runtime.interactor.model.configuration;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAdditionalPropertiesModel implements IAdditionalPropertiesModel {
  
  private static final long     serialVersionUID     = 1L;
  protected Map<String, Object> additionalProperties = new HashMap<>();
  
  @Override
  public void setAdditionalProperty(String key, Object value)
  {
    this.additionalProperties.put(key, value);
  }
  
  @Override
  public Object getAdditionalProperty(String key)
  {
    return this.additionalProperties.get(key);
  }
}
