package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermissionsForRole;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetPropertiesOfPropertyCollectionModel extends IGlobalPermissionsForRole, IModel {
  
  public static final String PROPERTY_COLLECTION_ID = "propertyCollectionId";
  
  public String getPropertyCollectionId();
  
  public void setPropertyCollectionId(String propertyCollectionId);
}
