package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermissionsForRole;

import java.util.List;

public class GetPropertyCollectionsForEntityModel extends GlobalPermissionsForRole
    implements IGetPropertyCollectionsForEntityModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    entityIds;
  protected String          type;
  
  @Override
  public List<String> getEntityIds()
  {
    
    return entityIds;
  }
  
  @Override
  public void setEntityIds(List<String> entityIds)
  {
    this.entityIds = entityIds;
  }
  
  @Override
  public String getType()
  {
    
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
}
