package com.cs.core.runtime.interactor.model.pluginexception;

import java.util.Map;

public class ExceptionDetailModel implements IExceptionDetailModel {
  
  private static final long     serialVersionUID = 1L;
  protected String              key;
  protected String              itemId;
  protected String              itemName;
  protected Map<String, String> properties;
  
  public String getItemId()
  {
    return itemId;
  }
  
  public void setItemId(String itemId)
  {
    this.itemId = itemId;
  }
  
  public String getItemName()
  {
    return itemName;
  }
  
  public void setItemName(String itemName)
  {
    this.itemName = itemName;
  }
  
  public String getKey()
  {
    return key;
  }
  
  public void setKey(String key)
  {
    this.key = key;
  }
  
  public Map<String, String> getProperties()
  {
    return properties;
  }
  
  public void setProperties(Map<String, String> properties)
  {
    this.properties = properties;
  }
}
