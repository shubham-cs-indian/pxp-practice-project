package com.cs.base.interactor.model.portal;

import java.util.List;

public class PortalModel implements IPortalModel {
  
  protected String       id;
  protected List<String> allowedEntities;
  
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  public List<String> getAllowedEntities()
  {
    return allowedEntities;
  }
  
  public void setAllowedEntities(List<String> allowedEntities)
  {
    this.allowedEntities = allowedEntities;
  }
}
