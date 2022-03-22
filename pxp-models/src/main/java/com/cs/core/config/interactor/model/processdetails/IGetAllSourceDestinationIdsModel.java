package com.cs.core.config.interactor.model.processdetails;

public interface IGetAllSourceDestinationIdsModel extends IGetAllInstanceIdByProcessIdsModel {
  
  public static final String ENTITY_TYPE = "entityType";
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
}
