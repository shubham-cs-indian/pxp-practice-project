package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IGetAllModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetLimitedAssetInstanceModel extends IModel {
  
  public static final String GET_ALL_MODEL   = "cetAllModel";
  public static final String CURRENT_USER_ID = "currentUserId";
  public static final String ROLES           = "roles";
  
  public IGetAllModel getGetAllModel();
  
  public void setGetAllModel(IGetAllModel getAllModel);
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
  
  public List<IRoleModel> getRoles();
  
  public void setRoles(List<IRoleModel> roles);
}
