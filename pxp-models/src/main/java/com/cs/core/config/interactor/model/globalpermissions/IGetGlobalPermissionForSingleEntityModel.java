package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetGlobalPermissionForSingleEntityModel extends IModel {
  
  public static final String ENTITY_ID                      = "entityId";
  public static final String TYPE                           = "type";
  public static final String ROLE_IDS_CONTAINING_LOGIN_USER = "roleIdsContainingLoginUser";
  public static final String USER_ID                        = "userId";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getType();
  
  public void setType(String type);
  
  public List<String> getRoleIdsContainingLoginUser();
  
  public void setRoleIdsContainingLoginUser(List<String> roleIdsContainingLoginUser);
  
  public String getUserId();
  
  public void setUserId(String userId);
}
