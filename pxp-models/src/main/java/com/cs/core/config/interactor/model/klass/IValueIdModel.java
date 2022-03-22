package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IValueIdModel extends IModel {
  
  public static final String ID    = "id";
  public static final String VALUE = "value";
  public static final String TYPE  = "type";
  
  public String getId();
  
  public void setId(String id);
  
  public String getType();
  
  public void setType(String type);
}
