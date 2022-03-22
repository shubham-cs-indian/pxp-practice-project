package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IAttributeInformationModel extends IConfigEntityInformationModel {
  
  public static final String TYPE = "type";
  
  public String getType();
  
  public void setType(String type);
}
