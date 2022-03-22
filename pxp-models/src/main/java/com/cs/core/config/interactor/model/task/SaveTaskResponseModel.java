package com.cs.core.config.interactor.model.task;

import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.role.Role;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class SaveTaskResponseModel extends TaskModel implements ISaveTaskResponseModel {
  
  private static final long    serialVersionUID = 1L;
  
  protected Boolean            isTypeSwitched;
  protected Map<String, IRole> referencedRoles  = new HashMap<>();
  
  @Override
  public Map<String, IRole> getReferencedRoles()
  {
    return referencedRoles;
  }
  
  @Override
  @JsonDeserialize(contentAs = Role.class)
  public void setReferencedRoles(Map<String, IRole> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
  }
  
  @Override
  public Boolean getIsTypeSwitched()
  {
    return isTypeSwitched;
  }
  
  @Override
  public void setIsTypeSwitched(Boolean isTypeSwitched)
  {
    this.isTypeSwitched = isTypeSwitched;
  }
}
