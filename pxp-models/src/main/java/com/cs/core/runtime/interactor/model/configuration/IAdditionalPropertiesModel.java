package com.cs.core.runtime.interactor.model.configuration;

public interface IAdditionalPropertiesModel extends IModel {
  
  public static String ADDITIONAL_PROPERTY = "additionalProperty";
  
  public void setAdditionalProperty(String key, Object value);
  
  public Object getAdditionalProperty(String key);
}
