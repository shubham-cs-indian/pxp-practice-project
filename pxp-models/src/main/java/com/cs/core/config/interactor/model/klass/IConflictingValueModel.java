package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IConflictingValueModel extends IModel {
  
  public static final String COUPLING_TYPE = "couplingType";
  public static final String VALUES        = "values";
  // public static final String TYPE = "type";
  
  public String getCouplingType();
  
  public void setCouplingType(String couplingType);
  
  public List<IValueIdModel> getValues();
  
  public void setValues(List<IValueIdModel> values);
}
