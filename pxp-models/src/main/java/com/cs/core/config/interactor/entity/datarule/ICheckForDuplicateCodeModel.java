package com.cs.core.config.interactor.entity.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICheckForDuplicateCodeModel extends IModel {
  
  public static final String ID         = "id";
  public static final String ENTITYTYPE = "entityType";
  
  public String getId();
  
  public void setId(String id);
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
}
