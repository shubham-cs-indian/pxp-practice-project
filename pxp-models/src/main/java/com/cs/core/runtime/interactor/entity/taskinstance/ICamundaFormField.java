package com.cs.core.runtime.interactor.entity.taskinstance;

import java.util.Map;

public interface ICamundaFormField  {
  
  public static final String ID         = "id";
  public static final String LABEL      = "label";
  public static final String TYPE       = "type";
  public static final String VALUE      = "value";
  public static final String PROPERTIES = "properties";
  
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Map<String, Object> getType();
  
  public void setType(Map<String, Object> type);
  
  public Map<String, Object> getValue();
  
  public void setValue(Map<String, Object> value);
  
  public Map<String, String> getProperties();
  
  public void setProperties(Map<String, String> properties);
}
