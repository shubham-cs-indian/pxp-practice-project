package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionModel;

import java.util.ArrayList;
import java.util.List;

public class GetGlobalPermissionModel implements IGetGlobalPermissionModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    klassIds;
  protected List<String>    taxonomyIds;
  protected String          loginUserId;
  protected List<String>    roleIds;
  
  @Override
  public String getLoginUserId()
  {
    return loginUserId;
  }
  
  @Override
  public void setLoginUserId(String loginUserId)
  {
    this.loginUserId = loginUserId;
  }
  
  @Override
  public List<String> getRoleIdsContainingLoginUser()
  {
    if (roleIds == null) {
      roleIds = new ArrayList<>();
    }
    return roleIds;
  }
  
  @Override
  public void setRoleIdsContainingLoginUser(List<String> roleIds)
  {
    this.roleIds = roleIds;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    if (klassIds == null) {
      klassIds = new ArrayList<>();
    }
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
}
