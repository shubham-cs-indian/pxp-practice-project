package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetKlassInstanceWithRolesModel extends IModel {
  
  public static final String ID              = "id";
  public static final String ROLES           = "roles";
  public static final String CURRENT_USER_ID = "currentUserId";
  
  public String getId();
  
  public void setId(String id);
  
  public List<IRoleModel> getRoles();
  
  public void setRoles(List<IRoleModel> roles);
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
}
