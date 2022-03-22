package com.cs.core.runtime.interactor.model.configuration;

import java.util.Map;

public interface IIdAndTypeModel extends IAdditionalPropertiesModel {
  
  public static String ID                    = "id";
  public static String TYPE                  = "type";
  public static String ADDITIONAL_PROPERTIES = "additionalProperties";
  
  public String getId();
  
  public void setId(String id);
  
  public String getType();
  
  public void setType(String type);
  
  public Map<String, Object> getAdditionalProperties();
  
  public void setAdditionalProperties(Map<String, Object> additionalProperties);
}
