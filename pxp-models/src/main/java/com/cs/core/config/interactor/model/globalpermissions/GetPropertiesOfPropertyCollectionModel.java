package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermissionsForRole;

public class GetPropertiesOfPropertyCollectionModel extends GlobalPermissionsForRole
    implements IGetPropertiesOfPropertyCollectionModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          propertyCollectionId;
  
  @Override
  public String getPropertyCollectionId()
  {
    
    return propertyCollectionId;
  }
  
  @Override
  public void setPropertyCollectionId(String propertyCollectionId)
  {
    this.propertyCollectionId = propertyCollectionId;
  }
}
