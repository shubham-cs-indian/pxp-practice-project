package com.cs.base.interactor.model.portal;

import java.util.List;

public interface IPortalModel {
  
  String ID               = "id";
  String ALLOWED_ENTITIES = "allowedEntities";
  
  public String getId();
  
  public void setId(String id);
  
  public List<String> getAllowedEntities();
  
  public void setAllowedEntities(List<String> allowedEntities);
}
