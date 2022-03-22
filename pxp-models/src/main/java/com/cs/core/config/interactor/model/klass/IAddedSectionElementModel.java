package com.cs.core.config.interactor.model.klass;

public interface IAddedSectionElementModel {
  
  public static final String ENTITY_ID = "entityId";
  public static final String TYPE      = "type";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getType();
  
  public void setType(String type);
}
