package com.cs.core.config.interactor.model.hidden;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IPropertyModificationInputModel extends IModel {
  
  public static final String PROPERTIES  = "properties";
  public static final String ID          = "id";
  public static final String ENTITY_TYPE = "entityType";
  
  public Map<String, String> getProperties();
  
  public void setProperties(Map<String, String> properties);
  
  public String getId();
  
  public void setId(String id);
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
}
