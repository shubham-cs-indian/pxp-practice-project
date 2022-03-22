package com.cs.core.config.interactor.model.user;

import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetGridUsersResponseModel implements IGetGridUsersResponseModel {
  
  private static final long                serialVersionUID = 1L;
  
  protected List<IUserModel>               usersList;
  protected Long                           count;
  protected Map<String, IIdLabelTypeModel> referencedRoles;
  
  @Override
  public List<IUserModel> getUsersList()
  {
    if (usersList == null) {
      usersList = new ArrayList<>();
    }
    return usersList;
  }
  
  @JsonDeserialize(contentAs = UserModel.class)
  @Override
  public void setUsersList(List<IUserModel> userList)
  {
    this.usersList = userList;
  }
  
  @Override
  public Long getCount()
  {
    if (count == null) {
      count = new Long(0);
    }
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  @Override
  public Map<String, IIdLabelTypeModel> getReferencedRoles()
  {
    return referencedRoles;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelTypeModel.class)
  public void setReferencedRoles(Map<String, IIdLabelTypeModel> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
  }
}
