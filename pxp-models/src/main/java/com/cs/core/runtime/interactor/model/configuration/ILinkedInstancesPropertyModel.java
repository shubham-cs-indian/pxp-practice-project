package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;

import java.util.List;

public interface ILinkedInstancesPropertyModel extends IPropertyInstance, IModel {
  
  public static final String VALUES = "values";
  
  public List<String> getValues();
  
  public void setValues(List<String> values);
}
