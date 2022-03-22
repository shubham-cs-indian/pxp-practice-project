package com.cs.core.config.interactor.model.permission;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetPermissionRequestModel extends IModel {
  
  public static final String ID          = "id";
  public static final String ROLE_ID     = "roleId";
  public static final String ENTITY_TYPE = "entityType";
  
  public String getId();
  
  public void setId(String id);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
}
