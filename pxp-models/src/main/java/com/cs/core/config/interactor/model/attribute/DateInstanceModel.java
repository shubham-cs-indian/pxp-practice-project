package com.cs.core.config.interactor.model.attribute;

import com.cs.core.runtime.interactor.entity.propertyinstance.PropertyInstance;
import com.cs.core.runtime.interactor.model.klassinstance.IDateInstanceModel;

public class DateInstanceModel extends PropertyInstance implements IDateInstanceModel {
  
  private static final long serialVersionUID = 1L;
  protected String          value;
  
  @Override
  public String getValue()
  {
    return value;
  }
  
  @Override
  public void setValue(String value)
  {
    this.value = value;
  }
}
