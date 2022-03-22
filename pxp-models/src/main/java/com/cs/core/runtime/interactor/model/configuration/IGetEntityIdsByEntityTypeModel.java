package com.cs.core.runtime.interactor.model.configuration;

public interface IGetEntityIdsByEntityTypeModel extends IModel {
  
  public static String ENTITY_TYPE = "entityType";
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
}
