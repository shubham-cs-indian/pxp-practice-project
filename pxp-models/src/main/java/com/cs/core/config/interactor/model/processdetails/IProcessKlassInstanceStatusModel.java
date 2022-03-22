package com.cs.core.config.interactor.model.processdetails;

public interface IProcessKlassInstanceStatusModel extends IProcessFailureModel {
  
  public static final String ENTITY_ID   = "entityId";
  public static final String ENTITY_TYPE = "entityType";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
}
