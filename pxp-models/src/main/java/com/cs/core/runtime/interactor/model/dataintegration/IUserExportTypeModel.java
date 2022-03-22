package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IUserExportTypeModel extends IModel {
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public String getType();
  
  public void setType(String type);
}
