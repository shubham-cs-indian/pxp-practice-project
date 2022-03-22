package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.config.interactor.model.role.IRoleModel;

import java.util.List;

public interface IIdParameterModel extends IAdditionalPropertiesModel {
  
  public static String ID              = "id";
  public static String TYPE            = "type";
  public static String ROLES           = "roles";
  public static String CURRENT_USER_ID = "currentUserId";
  
  public String getId();
  
  public void setId(String id);
  
  public List<IRoleModel> getRoles();
  
  public void setRoles(List<IRoleModel> roles);
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
  
  public String getType();
  
  public void setType(String type);
}
