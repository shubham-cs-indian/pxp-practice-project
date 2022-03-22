package com.cs.runtime.interactor.model.indsserver;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IINDSScriptArgumentsModel extends IModel {
  
  public static final String NAME  = "name";
  public static final String VALUE = "value";
  
  public String getName();
  public void setName(String name);
  
  public String getValue();
  public void setValue(String value);
  
}