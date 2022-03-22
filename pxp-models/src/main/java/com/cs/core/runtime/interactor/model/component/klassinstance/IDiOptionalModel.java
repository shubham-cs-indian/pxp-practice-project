package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDiOptionalModel extends IModel {
  
  public static String CONTEXT    = "context";
  public static String PROPERTIES = "properties";
  
  public IDiContextModel getContext();
  
  public void setContext(IDiContextModel context);
  
  public IDiPropertiesModel getProperties();
  
  public void setProperties(IDiPropertiesModel properties);
}
