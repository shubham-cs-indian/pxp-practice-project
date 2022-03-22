package com.cs.core.config.interactor.model.duplicatecode;

import com.cs.core.config.interactor.entity.datarule.ICheckForDuplicateCodeModel;

public class CheckForDuplicateCodeModel implements ICheckForDuplicateCodeModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          entityType;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getEntityType()
  {
    return entityType;
  }
  
  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
}
