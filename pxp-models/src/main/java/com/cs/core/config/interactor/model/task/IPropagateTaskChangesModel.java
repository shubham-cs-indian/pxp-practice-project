package com.cs.core.config.interactor.model.task;

import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IPropagateTaskChangesModel extends IModel {
  
  public static final String ID               = "id";
  public static final String NEW_TYPE         = "newType";
  public static final String REFERENCED_ROLES = "referencedRoles";
  
  public String getId();
  
  public void setId(String id);
  
  public String getNewType();
  
  public void setNewType(String newType);
  
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
}
