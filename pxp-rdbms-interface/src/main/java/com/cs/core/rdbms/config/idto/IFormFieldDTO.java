package com.cs.core.rdbms.config.idto;

import java.util.Map;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IFormFieldDTO extends ISimpleDTO {
  
  public static final String ID         = "id";
  public static final String LABEL      = "label";
  public static final String TYPE       = "type";
  public static final String VALUE      = "value";
  public static final String VALUES      = "values";
  public static final String PROPERTIES = "properties";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Map<String, Object> getType();
  
  public void setType(Map<String, Object> type);
  
  public Map<String, Object> getValue();
  
  public void setValue(Map<String, Object> value);
  
  public Map<String, String> getProperties();
  
  public void setProperties(Map<String, String> properties);
  
  public String getId();
  
  public void setId(String id);
  
  public String toString();
  
}
