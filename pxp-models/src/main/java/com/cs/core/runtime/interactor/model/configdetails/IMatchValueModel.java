package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IMatchValueModel extends IModel {
  
  public static final String VALUE   = "value";
  public static final String PERCENT = "percent";
  public static final String COUNT   = "count";
  
  public String getValue();
  
  public void setValue(String value);
  
  public String getPercent();
  
  public void setPercent(String percentage);
  
  public String getCount();
  
  public void setCount(String count);
}
