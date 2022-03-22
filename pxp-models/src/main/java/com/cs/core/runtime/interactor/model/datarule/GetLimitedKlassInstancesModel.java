package com.cs.core.runtime.interactor.model.datarule;

import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IGetAllModel;
import com.cs.core.runtime.interactor.model.relationship.IGetLimitedKlassInstanceModel;

import java.util.ArrayList;
import java.util.List;

public class GetLimitedKlassInstancesModel implements IGetLimitedKlassInstanceModel {
  
  protected List<String>     dimensionalTagIds = new ArrayList<>();
  protected IGetAllModel     getAllModel;
  protected String           currentUserId;
  protected List<IRoleModel> roles;
  
  @Override
  public List<String> getDimensionalTagIds()
  {
    return this.dimensionalTagIds;
  }
  
  @Override
  public void setDimensionalTagIds(List<String> dimensionalTagIds)
  {
    this.dimensionalTagIds = dimensionalTagIds;
  }
  
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
