package com.cs.core.config.interactor.model.hidden;

import java.util.HashMap;
import java.util.Map;

public class PropertyModificationInputModel implements IPropertyModificationInputModel {
  
  private static final long     serialVersionUID = 1L;
  protected Map<String, String> properties;
  protected String              id;
  protected String              entityType;
  
  @Override
  public Map<String, String> getProperties()
  {
    return properties;
  }
  
  @Override
  public void setProperties(Map<String, String> properties)
  {
    if (properties == null) {
      properties = new HashMap<String, String>();
    }
    this.properties = properties;
  }
  
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
  public String getEntityType()
  {
    return entityType;
  }
  
  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
}
