package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

public interface IUsedBy {
  
  public static final String ENTITY_TYPE = "entityType";
  public static final String LINKED_IDS  = "linkedIds";
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
  
  public List<String> getLinkedIds();
  
  public void setLinkedIds(List<String> linkedIds);
}
