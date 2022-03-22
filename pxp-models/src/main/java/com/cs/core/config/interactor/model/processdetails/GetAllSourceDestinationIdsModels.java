package com.cs.core.config.interactor.model.processdetails;

public class GetAllSourceDestinationIdsModels extends GetAllInstanceIdByProcessIdsModel
    implements IGetAllSourceDestinationIdsModel {
  
  private static final long serialVersionUID = 1L;
  protected String          entityType;
  
  public String getEntityType()
  {
    return entityType;
  }
  
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
}
