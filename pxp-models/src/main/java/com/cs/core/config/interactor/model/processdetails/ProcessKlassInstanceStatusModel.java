package com.cs.core.config.interactor.model.processdetails;

public class ProcessKlassInstanceStatusModel extends ProcessFailureModel
    implements IProcessKlassInstanceStatusModel {
  
  private static final long serialVersionUID = 1L;
  protected String          entityId;
  protected String          entityType;
  
  public String getEntityId()
  {
    return entityId;
  }
  
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
  
  public String getEntityType()
  {
    return entityType;
  }
  
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
}
