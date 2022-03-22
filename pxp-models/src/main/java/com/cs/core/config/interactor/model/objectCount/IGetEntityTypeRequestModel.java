package com.cs.core.config.interactor.model.objectCount;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetEntityTypeRequestModel extends IModel{
   
  public static String ENTITY_TYPE = "entityType";
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
}
