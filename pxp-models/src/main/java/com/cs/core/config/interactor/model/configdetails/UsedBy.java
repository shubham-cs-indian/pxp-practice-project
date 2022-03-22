package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

public class UsedBy implements IUsedBy {
  
  protected String       entityType;
  protected List<String> linkedIds;
  
  @Override
  public String getEntityType()
  {
    return entityType;
  }
  
  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
  
  @Override
  public List<String> getLinkedIds()
  {
    return linkedIds;
  }
  
  @Override
  public void setLinkedIds(List<String> linkedIds)
  {
    this.linkedIds = linkedIds;
  }
}
