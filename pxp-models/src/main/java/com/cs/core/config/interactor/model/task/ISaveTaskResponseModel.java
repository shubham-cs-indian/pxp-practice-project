package com.cs.core.config.interactor.model.task;

import com.cs.core.config.interactor.entity.role.IRole;

import java.util.Map;

public interface ISaveTaskResponseModel extends ITaskModel {
  
  public static final String IS_TYPE_SWITCHED = "isTypeSwitched";
  public static final String REFERENCED_ROLES = "referencedRoles";
  
  public Boolean getIsTypeSwitched();
  
  public void setIsTypeSwitched(Boolean isTypeSwitched);
  
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
}
