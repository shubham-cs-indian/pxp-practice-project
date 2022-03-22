package com.cs.core.runtime.interactor.model.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterValueRoleModel extends AbstractFilterValueModel
    implements IFilterValueRoleModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    users;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<String> getUsers()
  {
    if (users == null) {
      users = new ArrayList<>();
    }
    return users;
  }
  
  @Override
  public void setUsers(List<String> users)
  {
    this.users = users;
  }
}
