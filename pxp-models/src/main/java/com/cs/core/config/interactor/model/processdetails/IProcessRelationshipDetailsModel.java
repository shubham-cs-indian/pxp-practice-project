package com.cs.core.config.interactor.model.processdetails;

public interface IProcessRelationshipDetailsModel extends IProcessFailureModel {
  
  public static final String ENTITY_ID = "entityId";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
}
