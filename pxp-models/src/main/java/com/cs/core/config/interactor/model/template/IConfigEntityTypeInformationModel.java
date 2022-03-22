package com.cs.core.config.interactor.model.template;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IConfigEntityTypeInformationModel extends IConfigEntityInformationModel {
  
  public static final String TYPE = "type";
  public static final String CODE = "code";
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getType();
  
  public void setType(String type);
}
