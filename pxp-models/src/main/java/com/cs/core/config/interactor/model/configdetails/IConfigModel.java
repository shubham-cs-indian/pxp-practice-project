package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IEntityModel;

public interface IConfigModel extends IEntityModel {
  
  public static final String CODE = "code";
  
  public String getCode();
  
  public void setCode(String code);
}
