package com.cs.core.config.interactor.model.role;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetRolesAndKlassesForUserModel extends IModel {
  
  public List<IRoleModel> getRoles();
  
  public void setRoles(List<IRoleModel> roles);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
}
