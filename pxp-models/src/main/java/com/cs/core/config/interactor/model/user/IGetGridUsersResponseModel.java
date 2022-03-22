package com.cs.core.config.interactor.model.user;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;

import java.util.List;
import java.util.Map;

public interface IGetGridUsersResponseModel extends IModel {
  
  public static final String USERS_LIST       = "usersList";
  public static final String COUNT            = "count";
  public static final String REFERENCED_ROLES = "referencedRoles";
  
  public List<IUserModel> getUsersList();
  
  public void setUsersList(List<IUserModel> userssList);
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public Map<String, IIdLabelTypeModel> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IIdLabelTypeModel> referencedRoles);
}
