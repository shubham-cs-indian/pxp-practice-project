package com.cs.core.runtime.interactor.model.configuration;

import java.util.HashMap;
import java.util.Map;

public class IdAndTypeModel implements IIdAndTypeModel {
  
  private static final long     serialVersionUID     = 1L;
  
  protected String              id;
  protected String              type;
  protected Map<String, Object> additionalProperties = new HashMap<>();
  
  public IdAndTypeModel()
  {
  }
  
  public IdAndTypeModel(String id)
  {
    this.id = id;
  }
  
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String toString()
  {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("{");
    strBuilder.append("\"id\":\"" + id + "\"");
    strBuilder.append("}");
    return strBuilder.toString();
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public void setAdditionalProperty(String key, Object value)
  {
    additionalProperties.put(key, value);
  }
  
  @Override
  public Object getAdditionalProperty(String key)
  {
    return additionalProperties.get(key);
  }
  
  @Override
  public Map<String, Object> getAdditionalProperties()
  {
    return additionalProperties;
  }
  
  @Override
  public void setAdditionalProperties(Map<String, Object> additionalProperties)
  {
    this.additionalProperties.putAll(additionalProperties);
  }
}
