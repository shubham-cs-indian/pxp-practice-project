package com.cs.core.runtime.interactor.model.pluginexception;

import java.io.Serializable;
import java.util.Map;

public interface IExceptionDetailModel extends Serializable {
  
  public static final String KEY        = "key";
  public static final String ITEM_ID    = "itemId";
  public static final String ITEM_NAME  = "itemName";
  public static final String PROPERTIES = "properties";
  
  public String getKey();
  
  public void setKey(String key);
  
  public String getItemId();
  
  public void setItemId(String itemId);
  
  public String getItemName();
  
  public void setItemName(String itemName);
  
  public Map<String, String> getProperties();
  
  public void setProperties(Map<String, String> properties);
}
