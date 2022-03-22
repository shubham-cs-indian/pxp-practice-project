package com.cs.core.config.interactor.model.processdetails;

public class ProcessRelationshipDetailsModel extends ProcessFailureModel
    implements IProcessRelationshipDetailsModel {
  
  private static final long serialVersionUID = 1L;
  protected String          entityId;
  
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
}
