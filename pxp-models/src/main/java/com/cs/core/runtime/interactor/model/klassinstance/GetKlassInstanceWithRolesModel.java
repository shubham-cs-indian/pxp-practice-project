package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.role.IRoleModel;

import java.util.ArrayList;
import java.util.List;

public class GetKlassInstanceWithRolesModel implements IGetKlassInstanceWithRolesModel {
  
  protected String           id;
  
  protected List<IRoleModel> roles;
  
  protected String           currentUserId;
  
  public GetKlassInstanceWithRolesModel(String id, String currentUserId)
  {
    this.id = id;
    this.currentUserId = currentUserId;
  }
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<IRoleModel> getRoles()
  {
    if (roles == null) {
      roles = new ArrayList<IRoleModel>();
    }
    return this.roles;
  }
  
  @Override
  public void setRoles(List<IRoleModel> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public String getCurrentUserId()
  {
    return this.currentUserId;
  }
  
  @Override
  public void setCurrentUserId(String currentUserId)
  {
    this.currentUserId = currentUserId;
  }
}
