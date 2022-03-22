package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ITileInfoModel extends IModel {
  
  public static final String TYPE = "type";
  
  public String getType();
  
  public void setType(String type);
}
