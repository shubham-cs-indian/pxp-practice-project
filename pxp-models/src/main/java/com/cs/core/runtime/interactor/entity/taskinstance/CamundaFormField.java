package com.cs.core.runtime.interactor.entity.taskinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

public class CamundaFormField implements ICamundaFormField {
  
  private static final long     serialVersionUID = 1L;
  protected String              id;
  protected String              label;
  protected Map<String, Object> type;
  protected Map<String, Object> value;
  protected Map<String, String> properties;
  
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
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public Map<String, Object> getType()
  {
    return type;
  }
  
  @Override
  @JsonDeserialize(as = Map.class)
  public void setType(Map<String, Object> type)
  {
    this.type = type;
  }
  
  @Override
  public Map<String, Object> getValue()
  {
    return value;
  }
  
  @Override
  @JsonDeserialize(as = Map.class)
  public void setValue(Map<String, Object> value)
  {
    this.value = value;
  }
  
  @Override
  public Map<String, String> getProperties()
  {
    return properties;
  }
  
  @Override
  public void setProperties(Map<String, String> properties)
  {
    this.properties = properties;
  }
}
