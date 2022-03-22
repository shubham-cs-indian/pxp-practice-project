package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IReceiveModel extends IModel {
  
  public static final String DATA = "data";
  
  public String getData();
  
  public void setData(String data);
}
