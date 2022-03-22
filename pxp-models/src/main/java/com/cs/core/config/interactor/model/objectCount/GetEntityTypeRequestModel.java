package com.cs.core.config.interactor.model.objectCount;


public class GetEntityTypeRequestModel implements IGetEntityTypeRequestModel{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public String entityType;
  
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
