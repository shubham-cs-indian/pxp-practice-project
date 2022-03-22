package com.cs.core.runtime.interactor.model.dynamichierarchy;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IIdLabelTypeModel extends IIdLabelCodeModel {
  
  public static final String TYPE = "type";
  
  public String getType();
  
  public void setType(String type);
}
