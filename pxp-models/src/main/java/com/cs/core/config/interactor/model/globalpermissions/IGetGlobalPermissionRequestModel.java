package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetGlobalPermissionRequestModel extends IModel {
  
  public static final String ID                             = "id";
  public static final String ENTITY_ID                      = "entityId";
  public static final String TYPE                           = "type";
  public static final String ROLE_IDS_CONTAINING_LOGIN_USER = "roleIdsContainingLoginUser";
  public static final String CREATED_BY                     = "createdBy";
  
  public String getId();
  
  public void setId(String id);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getType();
  
  public void setType(String type);
  
  public List<String> getRoleIdsContainingLoginUser();
  
  public void setRoleIdsContainingLoginUser(List<String> roleIdsContainingLoginUser);
  
  public String getCreatedBy();
  
  public void setCreatedBy(String createdBy);
}
