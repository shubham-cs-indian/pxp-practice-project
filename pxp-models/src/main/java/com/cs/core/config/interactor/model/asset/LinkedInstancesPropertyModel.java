package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.entity.propertyinstance.PropertyInstance;
import com.cs.core.runtime.interactor.model.configuration.ILinkedInstancesPropertyModel;

import java.util.List;

public class LinkedInstancesPropertyModel extends PropertyInstance
    implements ILinkedInstancesPropertyModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    values;
  
  @Override
  public List<String> getValues()
  {
    return values;
  }
  
  @Override
  public void setValues(List<String> values)
  {
    this.values = values;
  }
}
