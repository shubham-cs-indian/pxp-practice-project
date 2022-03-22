package com.cs.core.runtime.interactor.model.dynamichierarchy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IIdAndNameModel extends IModel {
  
  public static final String ID   = "id";
  public static final String NAME = "name";
  
  public String getId();
  
  public void setId(String id);
  
  public String getName();
  
  public void setName(String name);
}
