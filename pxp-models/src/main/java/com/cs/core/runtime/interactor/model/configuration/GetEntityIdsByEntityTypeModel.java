package com.cs.core.runtime.interactor.model.configuration;

public class GetEntityIdsByEntityTypeModel implements IGetEntityIdsByEntityTypeModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          entityType;
  
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
