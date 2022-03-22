package com.cs.core.config.interactor.model.versionrollback;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IPropertyCouplingInformationModel extends IModel {
  
  public static final String COUPLING_TYPE = "couplingType";
  public static final String DEFAULT_VALUE = "defaultValue";
  
  public String getCouplingType();
  
  public void setCouplingType(String couplingType);
  
  public Object getDefaultValue();
  
  public void setDefaultValue(Object defaultValue);
}
