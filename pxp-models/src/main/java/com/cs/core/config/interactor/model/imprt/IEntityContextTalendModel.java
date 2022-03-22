package com.cs.core.config.interactor.model.imprt;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IEntityContextTalendModel extends IModel {
  
  public static final String CONTEXT = "context";
  
  public String[] getContext();
  
  public void setContext(String[] context);
}
