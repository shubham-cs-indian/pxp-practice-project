package com.cs.core.config.interactor.model.klass;

public class AddedSectionElementModel implements IAddedSectionElementModel {
  
  protected String entityId;
  protected String type;
  
  @Override
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
}
