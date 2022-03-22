package com.cs.core.runtime.interactor.model.filter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
    property = "baseType", visible = true)
public interface IFilterValueRoleModel extends IFilterValueModel {
  
  public static final String USERS = "users";
  
  public List<String> getUsers();
  
  public void setUsers(List<String> users);
}
