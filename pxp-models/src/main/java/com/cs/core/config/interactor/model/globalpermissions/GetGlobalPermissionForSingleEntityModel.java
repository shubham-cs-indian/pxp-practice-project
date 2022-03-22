package com.cs.core.config.interactor.model.globalpermissions;

import java.util.ArrayList;
import java.util.List;

public class GetGlobalPermissionForSingleEntityModel
    implements IGetGlobalPermissionForSingleEntityModel {
  
  private static final long serialVersionUID = 1L;
  protected String          entityId;
  protected String          type;
  protected List<String>    roleIdsContainingLoginUser;
  protected String          userId;
  
  @Override
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
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
  
  @Override
  public List<String> getRoleIdsContainingLoginUser()
  {
    if (roleIdsContainingLoginUser == null) {
      roleIdsContainingLoginUser = new ArrayList<>();
    }
    return roleIdsContainingLoginUser;
  }
  
  @Override
  public void setRoleIdsContainingLoginUser(List<String> roleIdsContainingLoginUser)
  {
    this.roleIdsContainingLoginUser = roleIdsContainingLoginUser;
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
}
