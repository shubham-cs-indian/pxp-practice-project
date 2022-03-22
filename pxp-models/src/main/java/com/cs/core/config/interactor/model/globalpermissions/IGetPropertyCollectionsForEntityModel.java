package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermissionsForRole;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetPropertyCollectionsForEntityModel extends IGlobalPermissionsForRole, IModel {
  
  public static final String ENTITY_IDS = "entityIds";
  public static final String TYPE       = "type";
  
  public List<String> getEntityIds();
  
  public void setEntityIds(List<String> entityIds);
  
  public String getType();
  
  public void setType(String type);
}
