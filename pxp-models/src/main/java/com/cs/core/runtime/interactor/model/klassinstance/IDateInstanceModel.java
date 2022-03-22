package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDateInstanceModel extends IPropertyInstance, IModel {
  
  public static final String VALUE = "value";
  
  public String getValue();
  
  public void setValue(String value);
}
