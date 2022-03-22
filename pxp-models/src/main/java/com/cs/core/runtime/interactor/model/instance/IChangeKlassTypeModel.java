package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IChangeKlassTypeModel extends IModel {
  
  public static final String IDS           = "ids";
  public static final String DEFAULT_KLASS = "defaultKlass";
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public Map<String, String> getDefaultKlass();
  
  public void setDefaultKlass(Map<String, String> defaultKlass);
}
