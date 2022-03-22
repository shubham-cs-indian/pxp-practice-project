package com.cs.core.config.interactor.entity.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IElementConflictingValuesModel extends IModel {
  
  public static final String ID          = "id";
  public static final String TYPE        = "type";
  public static final String SOURCE_TYPE = "sourceType";
  
  public String getType();
  
  public void setType(String type);
  
  public String getId();
  
  public void setId(String id);
  
  public String getSourceType();
  
  public void setSourceType(String sourceType);
}
