package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IGetAllModel;

import java.util.ArrayList;
import java.util.List;

public class GetLimitedAssetInstancesModel implements IGetLimitedAssetInstanceModel {
  
  protected IGetAllModel     getAllModel;
  protected String           currentUserId;
  protected List<IRoleModel> roles;
  
  @Override
  public IGetAllModel getGetAllModel()
  {
    return this.getAllModel;
  }
  
  @Override
  public void setGetAllModel(IGetAllModel getAllModel)
  {
    this.getAllModel = getAllModel;
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
}
